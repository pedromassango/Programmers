package com.pedromassango.programmers.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.data.CommentsDataSource;
import com.pedromassango.programmers.data.Transations;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 11/22/17.
 */

public class CommentsRemoteDataSource implements CommentsDataSource {

    private static CommentsRemoteDataSource INSTANCE = null;

    public static CommentsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentsRemoteDataSource();
        }
        return INSTANCE;
    }

    private String getUserId() {
        return PrefsHelper.getId();
    }

    @Override
    public void send(final Comment comment, Notification notification, final Callbacks.IRequestCallback callback) {
        final String postId = comment.getPostId();
        final String postAuthorId = comment.getPostAuthorId();
        final String postCategory = comment.getPostCategory();
        String commentId = comment.getId();

        Map<String, Object> childUpdates = new HashMap<>();

        //All Comments of the Current Post reference
        childUpdates.put("/comments-post/" + postId + "/" + commentId, comment.toMap());
        // send notification to post author.
        childUpdates.put("/notifications/" + notification.getToUserId() , notification.toMap());

        //Start the publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        callback.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // update all commentsCount properties on the post whit that id
                        updatePosts(postId, postAuthorId, postCategory, true);

                        // notify the callback
                        callback.onSuccess();
                    }
                });
    }

    private void updatePosts(String postId, String postAuthorId, String postCategory, boolean increment) {

        DatabaseReference allPostsRef = Library.getAllPostsRef().child(postId).getRef();
        DatabaseReference postByCategoryRef = Library.getPostsByCategoryRef(postCategory).child(postId).getRef();
        DatabaseReference postByUserRef = Library.getUserPostsRef(postAuthorId).child(postId).getRef();

        // Increment/Decrement on All posts reference
        Transations.runCommentsCountTransition(allPostsRef, increment);
        // Increment/Decrement on Posts by category reference
        Transations.runCommentsCountTransition(postByCategoryRef, increment);
        // Increment/Decrement on Posts by user reference
        Transations.runCommentsCountTransition(postByUserRef, increment);
    }

    @Override
    public void markAsUtil(final Comment comment, final boolean mark, final Callbacks.IResultCallback<Comment> callback) {
        final String postId = comment.getPostId();
        String commentId = comment.getId();
        final String authorId = comment.getAuthorId();

        // for local updates
        comment.addVote(getUserId());

        Map<String, Object> dataChild = new HashMap<>();
        dataChild.put(getUserId(), true);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/comments-post/" + postId + "/" + commentId + "/" + "votes", dataChild);
        childUpdates.put("/comments-post/" + postId + "/" + commentId + "/" + "votes", dataChild);

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

                        // Increment comment author skill
                        Transations.runReputationCountTransition(authorId, mark, false);
                    }
                });
    }

    @Override
    public void delete(final Comment comment, final Callbacks.IRequestCallback callback) {
        final String postId = comment.getPostId();
        final String postAuthorId = comment.getPostAuthorId();
        final String postCategory = comment.getPostCategory();
        String commentId = comment.getId();

        Map<String, Object> childUpdates = new HashMap<>();

        //Delete from center of comments of the current post
        childUpdates.put("/comments-post/" + postId + "/" + commentId, null);

        //Start the publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        callback.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // To update all posts that the comments size as changed
                        // update all commentsCount properties on the post whit that id
                        updatePosts(postId, postAuthorId, postCategory, false);

                        //notify the UI
                        callback.onSuccess();
                    }
                });
    }

    @Override
    public void getAll(String postId, Callbacks.IResultsCallback<Comment> callback) {
        //Library.getCommentsRef(postId)
    }
}
