package com.pedromassango.programmers.data.local;

import com.pedromassango.programmers.data.PostsDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;

import java.util.List;

import io.realm.Realm;

/**
 * Created by pedromassango on 11/12/17.
 */

/**
 * Retrieve data from local data source.
 */
public class PostsLocalDataSource implements PostsDataSource {

    private static PostsLocalDataSource INSTANCE = null;

    public static PostsLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostsLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(Callbacks.IResultsCallback<Post> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            List<Post> result = realm.where(Post.class).findAll();

            if (result == null || result.isEmpty()) {
                callback.onDataUnavailable();
                return;
            }

            // Retrieve the data
            callback.onSuccess(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onDataUnavailable();
        }
    }

    @Override
    public void getByUser(String authorId, Callbacks.IResultsCallback<Post> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            List<Post> result = realm
                    .where(Post.class)
                    .equalTo("authorId", authorId) // Fetch posts by author id
                    .findAll();

            if (result == null || result.isEmpty()) {
                callback.onDataUnavailable();
                return;
            }

            // Retrieve the data
            callback.onSuccess(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onDataUnavailable();
        }
    }

    @Override
    public void getAll(String category, Callbacks.IResultsCallback<Post> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            List<Post> result = realm
                    .where(Post.class)
                    .equalTo("category", category) // Fetch posts by category
                    .findAll();

            if (result == null || result.isEmpty()) {
                callback.onDataUnavailable();
                return;
            }

            // Retrieve the data
            callback.onSuccess(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onDataUnavailable();
        }
    }

    @Override
    public void getById(String postId, Callbacks.IResultCallback<Post> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            //Return the first item tha id match with postId
            Post result = realm
                    .where(Post.class)
                    .equalTo("id", postId)
                    .findFirst();

            // Check if there is a valid data
            if (result == null) {
                callback.onDataUnavailable();
                return;
            }

            // Return the data
            callback.onSuccess(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onDataUnavailable();
        }
    }

    @Override
    public void save(Post post, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction(); // open a transation
            realm.copyToRealmOrUpdate(post); // satve or update the data
            realm.commitTransaction(); // close the transation

            if (callback != null)
                callback.onSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();

            if (callback != null)
                callback.onError();
        }
    }

    @Override
    public void handleCommentsPermission(String authorId, String postId, String category, boolean commentsActive, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction(); // open a transation
            Post p = realm.where(Post.class).equalTo("id", postId).findFirst();

            if (p == null) {
                if (callback != null)
                    callback.onError();
                return;
            }

            p.setCommentsActive(commentsActive);
            realm.copyToRealmOrUpdate(p); // update the data
            realm.commitTransaction(); // close the transation

            if (callback != null)
                callback.onSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();

            if (callback != null)
                callback.onError();
        }
    }

    @Override
    public void update(Post post, Callbacks.IRequestCallback callback) {
        save(post, callback);
    }

    @Override
    public void search(String query, Callbacks.IResultsCallback<Post> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            List<Post> result = realm
                    .where(Post.class)
                    .equalTo("title", query) // Fetch posts by query (post title)
                    .findAll();

            if (result == null || result.isEmpty()) {
                callback.onDataUnavailable();
                return;
            }

            // Retrieve the data
            callback.onSuccess(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onDataUnavailable();
        }
    }

    @Override
    public void delete(Post post, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction(); // open a transation
            post.deleteFromRealm(); // delete the data
            realm.commitTransaction(); // close the transation

            // Retrieve the data
            callback.onSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onError();
        }
    }
}
