package com.pedromassango.programmers.data.local;

import com.pedromassango.programmers.data.CommentsDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Notification;

import java.util.List;

import io.realm.Realm;
import io.realm.internal.IOException;

/**
 * Created by Pedro Massango on 11/22/17.
 */

public class CommentsLocalDataSource implements CommentsDataSource {

    private static CommentsLocalDataSource INSTANCE = null;

    public static CommentsLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentsLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void send(Comment comment, Notification notification, Callbacks.IRequestCallback callback) {
        insertOrUpdate(comment, callback);
    }

    private void insertOrUpdate(Comment comment, Callbacks.IResultCallback<Comment> callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(comment);
            realm.commitTransaction();

            if (callback != null)
                callback.onSuccess(comment);
        } catch (IOException ex) {
            ex.printStackTrace();
            if (callback != null)
                callback.onDataUnavailable();
        }
    }

    private void insertOrUpdate(Comment comment, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(comment);
            realm.commitTransaction();

            if (callback != null)
                callback.onSuccess();
        } catch (IOException ex) {
            ex.printStackTrace();
            if (callback != null)
                callback.onError();
        }
    }


    @Override
    public void markAsUtil(Comment comment, boolean mark, Callbacks.IResultCallback<Comment> callback) {
        insertOrUpdate(comment, callback);
    }

    @Override
    public void delete(Comment comment, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            (comment).deleteFromRealm();
            realm.commitTransaction();

            if (callback != null)
                callback.onSuccess();
        } catch (IOException ex) {
            ex.printStackTrace();
            if (callback != null)
                callback.onError();
        }
    }

    @Override
    public void getAll(String postId, Callbacks.IResultsCallback<Comment> callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            List<Comment> results = realm.where(Comment.class)
                    .equalTo("postId", postId)
                    .findAll();

            if (results == null || results.size() == 0) {
                callback.onDataUnavailable();
                return;
            }

            callback.onSuccess(results);
        } catch (IOException ex) {
            ex.printStackTrace();
            callback.onDataUnavailable();
        }
    }
}
