package com.pedromassango.programmers.data;

import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import java.util.List;

/**
 * Created by Pedro Massango on 11/22/17.
 */

public class CommentsRepository implements CommentsDataSource {

    private static CommentsRepository INSTANCE = null;

    private final CommentsDataSource remoteDataSource;
    private final CommentsDataSource localDataSource;

    private CommentsRepository(CommentsDataSource remoteDataSource, CommentsDataSource localDataSource){

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static CommentsRepository getInstance(CommentsDataSource remoteDataSource,
                                                 CommentsDataSource localDataSource){
        if(INSTANCE == null){
            INSTANCE = new CommentsRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void send(final Comment comment, final Notification notification, final Callbacks.IRequestCallback callback) {
        remoteDataSource.send(comment, notification, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                localDataSource.send(comment, notification, null);

                // Only subscribe if the current user is not the author of this comment
                if(!comment.getPostAuthorId().equals(PrefsHelper.getId())) {

                    // subscribe this user to this POST, to receive notifications
                    NotificationSender.subscribe(comment.getPostId());
                }

                // send notification to all users that was commented here
                NotificationSender.sendNotification(comment);

                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void markAsUtil(final Comment comment,boolean mark,  final Callbacks.IResultCallback<Comment> callback) {
        remoteDataSource.markAsUtil(comment, mark, new Callbacks.IResultCallback<Comment>() {
            @Override
            public void onSuccess(Comment result) {

                localDataSource.send(comment, null, null);
                callback.onSuccess( result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void delete(final Comment comment, final Callbacks.IRequestCallback callback) {
        remoteDataSource.delete(comment, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                localDataSource.delete(comment, null);
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void getAll(final String postId, final Callbacks.IResultsCallback<Comment> callback) {
        localDataSource.getAll(postId, new Callbacks.IResultsCallback<Comment>() {
            @Override
            public void onSuccess(List<Comment> results) {
                callback.onSuccess(results);

                getNewsCommentsFromRemote(postId, callback);
            }

            @Override
            public void onDataUnavailable() {
                getNewsCommentsFromRemote(postId, callback);
            }
        });
    }

    private void getNewsCommentsFromRemote(String postId, final Callbacks.IResultsCallback<Comment> callback) {
        remoteDataSource.getAll(postId, new Callbacks.IResultsCallback<Comment>() {
            @Override
            public void onSuccess(List<Comment> results) {
                callback.onSuccess(results);

                // update local source
                for(Comment c : results){
                    localDataSource.send(c, null, null);
                }
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }
}
