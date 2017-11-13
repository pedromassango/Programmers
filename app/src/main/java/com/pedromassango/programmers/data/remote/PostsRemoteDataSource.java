package com.pedromassango.programmers.data.remote;

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
import com.google.gson.Gson;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.PostsDataSource;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by pedromassango on 11/12/17.
 */

public class PostsRemoteDataSource implements PostsDataSource {

    private static PostsRemoteDataSource INSTANCE = null;

    public static PostsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(final Callbacks.IResultsCallback<Post> callback) {
        Library.getAllPostsRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            callback.onDataUnavailable();
                            return;
                        }

                        // here we have the data

                        // Temp data list
                        List<Post> tempList = new ArrayList<Post>();

                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            Post p = shot.getValue(Post.class);

                            tempList.add(p);
                        }

                        // release the data
                        callback.onSuccess(tempList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void getAll(String category, final Callbacks.IResultsCallback<Post> callback) {
        showLog("searchPosts");

        Library.getPostsByCategoryRef(category)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showLog("searchPosts - onDataChange: " + dataSnapshot);

                        if (!dataSnapshot.exists()) {
                            showLog("searchPosts - onDataChange: snapshot dont exist.");
                            callback.onDataUnavailable();
                            return;
                        }

                        showLog("searchPosts - onDataChange: posts found");

                        ArrayList<Post> result = new ArrayList<>();

                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            Post post = shot.getValue(Post.class);
                            result.add(post);
                        }

                        // Release posts result
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        showLog("searchPosts ERROR: " + databaseError.getMessage());
                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void getById(String postId, final Callbacks.IResultCallback<Post> callback) {
        Library.getPostRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            callback.onDataUnavailable();
                            return;
                        }

                        // Here we have the data

                        Post result = dataSnapshot.getValue(Post.class);

                        // release the data
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void save(Post post, Callbacks.IRequestCallback callback) {
        String postImage = post.getImgUrl();

        // Check if there is an image
        if (postImage == null || postImage.isEmpty()) {

            // No image, so whe publish, without upload some image
            publishPost(post, callback);
            return;
        }

        // There is some image, so upload it and publish
        Uri uri = new Gson().fromJson(postImage, Uri.class);

        publishPostWithImage(post, uri, callback);
    }

    @Override
    public void search(String query, Callbacks.IResultsCallback<Post> callback) {

    }

    @Override
    public void handleCommentsPermission(String authorId, String postId, String category, boolean commentsActive, final Callbacks.IRequestCallback callback) {

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

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        showLog("handleCommentsPermission SUCCESSFULL");
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showLog("handleCommentsPermission ERROR - " + e.getMessage());
                        callback.onError();
                    }
                });
    }

    @Override
    public void update(Post post, final Callbacks.IRequestCallback callback) {
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

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        showLog("update SUCCESSFULL");
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showLog("update ERROR - " + e.getMessage());
                        callback.onError();
                    }
                });
    }

    private void publishPost(Post post, final Callbacks.IRequestCallback callback) {

        //Generate a new Post ID
        DatabaseReference postDb = Library.getAllPostsRef();
        String postId = postDb.push().getKey();
        post.setId(postId);

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
                            callback.onError();
                            return;
                        }

                        // notify the callback
                        callback.onSuccess();
                    }
                });
    }

    private void publishPostWithImage(final Post post, Uri imageUri, final Callbacks.IRequestCallback callback) {
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
                e.printStackTrace();
                callback.onError();
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

                        //Get the uploaded image URL.
                        post.setImgUrl(string_dwload);

                        // Publish the post, with the image URL
                        PostsRemoteDataSource.this.publishPost(post, callback);
                    }
                });
    }

    @Override
    public void delete(Post post, final Callbacks.IRequestCallback callback) {

        // Firebase rules
        String category = CategoriesUtils.getCategory(post.getCategory());
        String postId = post.getId();
        String senderId = post.getAuthorId();

        //All posts reference
        String allPostsRef = AppRules.getAllPostsRef(postId);
        //Posts by category reference
        String postsCategoryRef = AppRules.getPostsCategoryRef(category, postId);
        //Posts by user reference
        String postUserRef = AppRules.getPostUserRef(senderId, postId);

        //The references to update
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(allPostsRef, null);
        childUpdates.put(postsCategoryRef, null);
        childUpdates.put(postUserRef, null);

        //Start the Post publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        callback.onSuccess();
                    }
                });
    }
}
