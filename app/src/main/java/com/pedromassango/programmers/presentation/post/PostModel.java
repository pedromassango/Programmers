package com.pedromassango.programmers.presentation.post;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IGetPostsListener;
import com.pedromassango.programmers.interfaces.INewPostListener;
import com.pedromassango.programmers.interfaces.IPostDeleteListener;
import com.pedromassango.programmers.interfaces.ISaveListener;
import com.pedromassango.programmers.interfaces.IUpdateListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 07/06/2017 at 12:48.
 */

public class PostModel implements PostContract.ModelImpl {

    private INewPostListener newPostListener;
    private DatabaseReference rootRef = Library.getRootReference();
    private BaseContract.PresenterImpl presenter;
    private final String userId;

    public PostModel(BaseContract.PresenterImpl presenter) {
        this.presenter = presenter;
        this.userId = PrefsHelper.getId();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {

        return PrefsHelper.getName();
    }

    @Override
    public void handleCommentsPermission(String authorId, String postId, String category, boolean commentsActive, final ISaveListener iSaveListener) {

        Map<String, Object> childUpdates = new HashMap<>();

        // Always invert the data.
        commentsActive = !commentsActive;

        // Firebase rules
        category = CategoriesUtils.getCategory(category);

        //All posts reference
        childUpdates.put("/posts/" + postId + "/commentsActive/", commentsActive);
        //Posts by category reference
        childUpdates.put(String.format("/posts-%s/", category) + "/" + postId + "/commentsActive/", commentsActive);
        //Posts by user reference
        childUpdates.put("/user-posts/" + authorId + "/" + postId + "/commentsActive/", commentsActive);

        rootRef.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        showLog("handleCommentsPermission SUCCESSFULL");
                        iSaveListener.onSaveSuccess(presenter.getContext().getString(R.string.done));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showLog("handleCommentsPermission ERROR - " + e.getMessage());
                        iSaveListener.onError();
                    }
                });
    }

    @Override
    public void update(final IUpdateListener listener, final Post post) {

        String postId = post.getId();
        String category = CategoriesUtils.getCategory(post.getCategory());

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        //All posts reference
        childUpdates.put("/posts/" + postId, postValues);

        //Posts by category reference
        childUpdates.put(String.format("/posts-%s/", category) + "/" + postId, postValues);

        //Posts by user reference
        childUpdates.put("/user-posts/" + post.getAuthorId() + "/" + postId, postValues);

        rootRef.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        showLog("update SUCCESSFULL");
                        listener.updateSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showLog("update ERROR - " + e.getMessage());
                        listener.updateError(e.getMessage());
                    }
                });
    }

    @Override
    public void handleLikes(IErrorListener errorListener, Post post, boolean like) {

        // Firebase rules
        String category = CategoriesUtils.getCategory(post.getCategory());

        Worker.handleLikes(presenter,
                getUserId(),
                like,
                post.getId(),
                category,
                post.getAuthorId(),
                errorListener);
    }

    @Override
    public void deletePost(final IPostDeleteListener listener, final Post post) {

        // Firebase rules
        String category = CategoriesUtils.getCategory(post.getCategory());

        Worker.deletePost(post.getId(), category, post.getAuthorId(), listener);
    }

    @Override
    public void searchPosts(String query, final IGetPostsListener listener) {
        final ArrayList<Post> posts = new ArrayList<>();
        showLog("searchPosts");

        Library.getAllPostsRef()
                .orderByChild("title")
                .startAt(query)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showLog("searchPosts - onDataChange: " + dataSnapshot);

                        if (!dataSnapshot.exists()) {
                            showLog("searchPosts - onDataChange: snapshot dont exist.");
                            listener.onError(presenter.getContext().getString(R.string.search_not_found));
                            return;
                        }

                        showLog("searchPosts - onDataChange: posts found");
                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            Post post = shot.getValue(Post.class);
                            posts.add(post);
                        }

                        // Release posts result
                        listener.onGetPostSuccessfull(posts);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        showLog("searchPosts ERROR: " + databaseError.getMessage());
                        listener.onError(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void increasePostViews(Post post, IErrorListener errorListener) {
        String postId = post.getId();

        DatabaseReference allPostRef = Library.getRootReference().child(AppRules.getAllPostsRef(postId));
        DatabaseReference PostByCategoryRef = Library.getRootReference().child(AppRules.getPostsCategoryRef(post.getCategory(), postId));
        DatabaseReference postByUserRef = Library.getRootReference().child(AppRules.getPostUserRef(post.getAuthorId(), postId));

        Worker.runPostViewsCountTransition(allPostRef, errorListener);
        Worker.runPostViewsCountTransition(PostByCategoryRef, errorListener);
        Worker.runPostViewsCountTransition(postByUserRef, errorListener);
    }

    @Override
    public void publishPost(final Context context, final INewPostListener listener, Uri imageUri, final Post post) {
        this.newPostListener = listener;

        //Generate a new Post ID
        DatabaseReference postDb = Library.getAllPostsRef();
        String postId = postDb.push().getKey();
        post.setId(postId);

        if (null != imageUri) {
            //Uploading Image
            //Uploading Image
            String postCategory = post.getCategory();
            FirebaseStorage postsImagesStorage = Library.getImageProfilesStorage();
            final StorageReference filepath = postsImagesStorage.getReference().child(postCategory)
                    .child(imageUri.getLastPathSegment());

            //Uploading image
            final UploadTask uploadTask = filepath.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showLog("UPLOAD POST IMAGE FAILED:");
                    listener.onError(Util.getError(e));
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            showLog("UPLOAD POST IMAGE SUCEESS:");
                            //get the download URL like this:

                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String string_dwload = downloadUrl.toString();

                            //Get the image URL then publish the post
                            post.setImgUrl(string_dwload);
                            PostModel.this.publishPost(post);
                        }
                    });

        } else {

            //Else just publish the post withou image
            this.publishPost(post);
        }
    }

    // To publish the post
    private void publishPost(final Post post) {

        // The post to publish
        Map<String, Object> postValues = post.toMap();

        // All posts reference
        String allPostsRef = AppRules.getAllPostsRef(post.getId());
        // Posts by category reference
        String postsCategoryRef = AppRules.getPostsCategoryRef(post.getCategory(), post.getId());
        // Posts by user reference
        String postUserRef = AppRules.getPostUserRef(post.getAuthorId(), post.getId());

        // The references to update
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(allPostsRef, postValues);
        childUpdates.put(postsCategoryRef, postValues);
        childUpdates.put(postUserRef, postValues);

        // Start the Post publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            newPostListener.onError(Util.getError(task.getException()));
                            return;
                        }

                        // Send an notification for all users
                        //NotificationSender.sendNotification(post);

                        // notify the user
                        newPostListener.onSuccess();
                    }
                });
    }
}
