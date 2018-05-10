package com.pedromassango.programmers.data;

import com.pedromassango.programmers.data.local.NotificationLocalDataSource;
import com.pedromassango.programmers.data.remote.NotificationRemoteDataSource;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Notification;

import java.util.List;

/**
 * Created by Pedro Massango on 11/18/17.
 */

public class NotificationRepository implements NotificationDataSOurce {

    private static  NotificationRepository INSTANCE = null;

    private final NotificationDataSOurce remoteDataSource;
    private final NotificationDataSOurce localDataSource;

    private NotificationRepository(NotificationDataSOurce remoteDataSource,
                                   NotificationDataSOurce localDataSource){

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static NotificationRepository getInstance(NotificationDataSOurce remoteDataSource,
                                                     NotificationDataSOurce localDataSource){
        if(INSTANCE == null){
            INSTANCE = new NotificationRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void get(final String userId, final Callbacks.IResultsCallback<Notification> callback) {

        if(Constants._DEVELOP_MODE){
            callback.onSuccess(MockData.notifications);
            return;
        }

        localDataSource.get(userId, new Callbacks.IResultsCallback<Notification>() {
            @Override
            public void onSuccess(List<Notification> results) {

                callback.onSuccess(results);

                getFromRemoteAndSaveLocal(userId, callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteAndSaveLocal(userId, callback);
            }
        });
    }

    private void getFromRemoteAndSaveLocal(String userId, final Callbacks.IResultsCallback<Notification> callback){
        remoteDataSource.get(userId, new Callbacks.IResultsCallback<Notification>() {
            @Override
            public void onSuccess(List<Notification> results) {
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void delete(final Notification notification, final Callbacks.IResultCallback<Notification> callback) {
        remoteDataSource.delete(notification, new Callbacks.IResultCallback<Notification>() {
            @Override
            public void onSuccess(Notification result) {
                localDataSource.delete(notification, null);

                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    /*
    @Override
    public void send(final Notification notification, final Callbacks.IRequestCallback callback) {
        remoteDataSource.send(notification, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                localDataSource.send(notification, null);

                if(callback != null)
                    callback.onSuccess();
            }

            @Override
            public void onError() {
                if(callback != null)
                    callback.onError();
            }
        });
    }*/
}
