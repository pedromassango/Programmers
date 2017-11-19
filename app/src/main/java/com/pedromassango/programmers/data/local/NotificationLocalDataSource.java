package com.pedromassango.programmers.data.local;

import com.pedromassango.programmers.data.NotificationDataSOurce;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Notification;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Pedro Massango on 11/18/17.
 */

public class NotificationLocalDataSource implements NotificationDataSOurce {

    @Override
    public void get(String postId, Callbacks.IResultsCallback<Notification> callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            List result = realm.where(Notification.class).equalTo("postId", postId).findAll();

            if(result.isEmpty()){
                if (callback != null)
                    callback.onDataUnavailable();
                return;
            }

            callback.onSuccess(result);
        }catch (Exception ex){
            if (callback != null)
                callback.onDataUnavailable();
        }
    }
/*
    @Override
    public void send(Notification notification, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate( notification);
            realm.commitTransaction();

            if(callback != null)
                callback.onSuccess();
        }catch (Exception ex){
            if(callback != null)
                callback.onError();
        }
    }*/
}
