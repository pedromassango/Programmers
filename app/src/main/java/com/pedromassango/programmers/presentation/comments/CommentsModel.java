package com.pedromassango.programmers.presentation.comments;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.interfaces.IGetPostListener;
import com.pedromassango.programmers.interfaces.ISendCommentListener;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;

import java.util.HashMap;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 11/06/2017 at 00:36.
 */

public class CommentsModel implements CommentsContract.CommentsModelImpl {

    private BaseContract.PresenterImpl presenter;

    public CommentsModel(BaseContract.PresenterImpl presenter) {

        this.presenter = presenter;
    }

    @Override
    public String getUserId() {

        return PrefsUtil.getId(presenter.getContext());
    }

    @Override
    public String getUsername() {

        return PrefsUtil.getName(presenter.getContext());
    }

    @Override
    public String getUserUrlPhoto() {

        return PrefsUtil.getPhoto(presenter.getContext());
    }

    @Override
    public void markCommentUtil(final CommentsContract.OnSetUtilCommentListener listener, final Comment comment) {

        final String postId = comment.getPostId();
        String commentId = comment.getId();

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

                        listener.onError(Util.getError(e));
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DatabaseReference authorCommentRef = Library.getUserRef(comment.getAuthorId());
                        Worker.runReputationCountTransition(presenter, authorCommentRef, true, false);
                    }
                });
    }

    @Override
    public void unMarkCommentUtil(final CommentsContract.OnSetUtilCommentListener listener, final Comment comment) {

        final String postId = comment.getPostId();
        String commentId = comment.getId();

        Map<String, Object> dataChild = new HashMap<>();
        dataChild.put(getUserId(), null);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/comments-post/" + postId + "/" + commentId + "/" + "votes", null);

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onError(Util.getError(e));
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DatabaseReference authorCommentRef = Library.getUserRef(comment.getAuthorId());
                        Worker.runReputationCountTransition(presenter, authorCommentRef, false, false);
                    }
                });
    }

    @Override
    public void getPost(String postId, final IGetPostListener getPostListener) {
        Library.getPostRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()) {
                            return;
                        }

                        Post post = dataSnapshot.getValue(Post.class);
                        getPostListener.onGetPostSuccessfull(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        showLog("GET POST INFO ERROR: " + databaseError.getMessage());
                        getPostListener.onError(Util.getError(databaseError.toException()));
                    }
                });
    }

    /***
     * Generate a random ID to a new Comment
     * @return generated ID
     */
    @Override
    public String generateId() {
        return Library.getRootReference().push().getKey();
    }

    @Override
    public void sendComment(final ISendCommentListener listener, final Comment comment) {
        final String postId = comment.getPostId();
        final String postAuthorId = comment.getPostAuthorId();
        final String postCategory = comment.getPostCategory();
        String commentId = comment.getId();

        Map<String, Object> commentValues = comment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        //All Comments of the Current Post reference
        childUpdates.put("/comments-post/" + postId + "/" + commentId, commentValues);

        //Start the publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onError(Util.getError(e));
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // update all commentsCount properties on the post whit that id
                        updatePosts(postId, postAuthorId, postCategory, true);

                        // Send

                        listener.onCommentSentSuccess();
                    }
                });
    }

    private void updatePosts(String postId, String postAuthorId, String postCategory, boolean increment) {

        DatabaseReference allPostsRef = Library.getAllPostsRef().child(postId).getRef();
        DatabaseReference postByCategoryRef = Library.getPostsByCategoryRef(postCategory).child(postId).getRef();
        DatabaseReference postByUserRef = Library.getUserPostsRef(postAuthorId).child(postId).getRef();

        // Increment/Decrement on All posts reference
        Worker.runCommentsCountTransition(allPostsRef, increment);
        // Increment/Decrement on Posts by category reference
        Worker.runCommentsCountTransition(postByCategoryRef, increment);
        // Increment/Decrement on Posts by user reference
        Worker.runCommentsCountTransition(postByUserRef, increment);
    }

    @Override
    public void deleteComment(final CommentsContract.OnDeleteCommentListener listener, final Comment comment) {
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

                        listener.onDeleteError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // To update all posts that the comments size as changed
                        // update all commentsCount properties on the post whit that id
                        updatePosts(postId, postAuthorId, postCategory, false);

                        //notify the UI
                        listener.onDeleteSuccess();
                    }
                });
    }
}
