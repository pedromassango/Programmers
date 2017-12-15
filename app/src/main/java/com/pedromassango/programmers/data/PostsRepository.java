package com.pedromassango.programmers.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pedromassango on 11/12/17.
 */

/**
 * A class to manage Posts. Use to retrieve and save data.
 */
public class PostsRepository implements PostsDataSource {

    private static PostsRepository INSTANCE = null;
    private final PostsDataSource remoteSource;
    private final PostsDataSource localSource;

    // whether we nedd to get data from server
    private boolean shouldFetchFromServer;

    private PostsRepository(PostsDataSource remoteSource, PostsDataSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;

        shouldFetchFromServer = PrefsHelper.shouldFetchFromServer();
    }

    public static PostsRepository getInstance(PostsDataSource remoteSource, PostsDataSource localSource) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepository(remoteSource, localSource);
        }
        return INSTANCE;
    }

    public void invalidate(){
        INSTANCE = null;
    }

    @Override
    public void getAll(final Callbacks.IResultsCallback<Post> callback) {
        localSource.getAll(new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                if(shouldFetchFromServer)
                    getFromRemoteAndUpdateLocalSource(callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteAndUpdateLocalSource(callback);
            }
        });
    }

    @Override
    public void getByUser(final String authorId, final Callbacks.IResultsCallback<Post> callback) {
        localSource.getByUser(authorId, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                if(shouldFetchFromServer)
                    getFromRemoteByAuthorId(authorId, callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteByAuthorId(authorId, callback);
            }
        });
    }

    @Override
    public void getAll(final String category, final Callbacks.IResultsCallback<Post> callback) {
        localSource.getAll(category, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                if(shouldFetchFromServer)
                getFromRemoteByCategory(category, callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteByCategory(category, callback);
            }
        });
    }

    private void getFromRemoteAndUpdateLocalSource(final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.getAll(new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {

                updateLocalDataSource(results);
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void getFromRemoteByAuthorId(String authorId, final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.getByUser(authorId, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {

                updateLocalDataSource(results);
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void getFromRemoteByCategory(String category, final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.getAll(category, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {

                updateLocalDataSource(results);
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void getFromRemoteAndUpdateLocalSource(String postId, final Callbacks.IResultCallback<Post> callback) {
        remoteSource.getById(postId, new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post result) {

                localSource.save(result, null);
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void updateLocalDataSource(List<Post> results) {
        for (Post p : results) {
            localSource.save(p, null);
        }
    }

    public void increasePostViews(Post post) {
        String postId = post.getId();

        DatabaseReference allPostRef = Library.getRootReference().child(AppRules.getAllPostsRef(postId));
        DatabaseReference PostByCategoryRef = Library.getRootReference().child(AppRules.getPostsCategoryRef(post.getCategory(), postId));
        DatabaseReference postByUserRef = Library.getRootReference().child(AppRules.getPostUserRef(post.getAuthorId(), postId));

        //TODO: work here.
        Worker.runPostViewsCountTransition(allPostRef);
        Worker.runPostViewsCountTransition(PostByCategoryRef);
        Worker.runPostViewsCountTransition(postByUserRef);

        // Update the local post data
        post.setViews( post.getViews() +1);
        localSource.update(post, null);
    }

    @Override
    public void handleCommentsPermission(Post post, final Callbacks.IResultCallback<Post> callback) {
        remoteSource.handleCommentsPermission(post, new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post post) {
                localSource.handleCommentsPermission(post, null);

                callback.onSuccess(post);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    public void handleLikes(final Post post, boolean like, final Callbacks.IResultCallback<Post> callback) {

        final String loggedUserId = PrefsHelper.getId();
        final String senderId = post.getAuthorId();
        String postId = post.getId();
        String postCategory = post.getCategory();

        Log.v("output", "handleLikes:\n L_ID: " + loggedUserId + "\n S_ID: " + senderId);

        // Firebase rules
        String category = CategoriesUtils.getCategory(postCategory);

        //The vote data
        final Map<String, Object> likeValue = new HashMap<>();
        if (like) {
            likeValue.put(loggedUserId, Boolean.TRUE);

        } else {
            likeValue.put(loggedUserId, null);
        }

        // update here to aply on data source localy
        post.updateLikes(like, loggedUserId);

        // All posts reference
        String allPostsLikesRef = AppRules.getAllPostsLikesRef(postId);
        // Posts by category reference
        String postsCategoryLikesRef = AppRules.getPostsCategoryLikesRef(category, postId);
        // Posts by user reference
        String postUserLikesRef = AppRules.getPostUserLikesRef(senderId, postId);

        Log.v("output", "handleLikes - path: " + allPostsLikesRef);

        //The references to update
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(allPostsLikesRef, likeValue);
        childUpdates.put(postsCategoryLikesRef, likeValue);
        childUpdates.put(postUserLikesRef, likeValue);

        //Start the LIKE work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onDataUnavailable();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // update localy
                        localSource.update(post, null);

                        // If is the sender that like they own post
                        // we do not need to increment the skills
                        if (loggedUserId.equals(senderId))
                            return;

                        // increment or decrement the senderPost skill
                        Transations.runReputationCountTransition(senderId, true, true);
                        callback.onSuccess(post);
                    }
                });
    }

    @Override
    public void search(final String query, final Callbacks.IResultsCallback<Post> callback) {
        localSource.search(query, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                if(shouldFetchFromServer)
                    // get from remote but not notify if have error
                    searchOnRemote(false, query, callback);
            }

            @Override
            public void onDataUnavailable() {
                searchOnRemote(true, query, callback);
            }
        });
    }

    private void searchOnRemote(final boolean notifyError, String query, final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.search(query, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                updateLocalDataSource(results);
            }

            @Override
            public void onDataUnavailable() {
                if (notifyError)
                    callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void getById(final String postId, final Callbacks.IResultCallback<Post> callback) {
        localSource.getById(postId, new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post result) {

                callback.onSuccess(result);

                if(shouldFetchFromServer)
                    getFromRemoteAndUpdateLocalSource(postId, callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteAndUpdateLocalSource(postId, callback);
            }
        });
    }

    @Override
    public void save(final Post post, final Callbacks.IResultCallback<Post> callback) {
        remoteSource.save(post, new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post result) {

                //save it localy
                localSource.save(result, null);

                // subscribe this user to this POST, to receive notifications
                NotificationSender.subscribe( result.getId());

                // Send an notification for all users
                NotificationSender.sendNotification(result);

                callback.onSuccess( result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void update(final Post post, final Callbacks.IRequestCallback callback) {
        remoteSource.update(post, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                localSource.update(post, null);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void delete(final Post post, final Callbacks.IRequestCallback callback) {
        remoteSource.delete(post, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                localSource.delete(post, callback);
                //callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    public void updateLocaly(Post post) {
        localSource.update(post, null);
    }
}
