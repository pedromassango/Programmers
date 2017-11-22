package com.pedromassango.programmers.data.remote;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.data.NotificationDataSOurce;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.server.Library;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro Massango on 11/18/17.
 */

public class NotificationRemoteDataSource implements NotificationDataSOurce {

    private static NotificationRemoteDataSource INSTANCE = null;

    public static NotificationRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new NotificationRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void get(String userId, final Callbacks.IResultsCallback<Notification> callback) {
        Library.getNotificationsRef(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            callback.onDataUnavailable();
                            return;
                        }

                        List<Notification> tempData = new ArrayList<>();

                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                            Notification notification = shot.getValue(Notification.class);

                            tempData.add(notification);
                        }

                        callback.onSuccess(tempData);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onDataUnavailable();
                    }
                });
    }
/*
    @Override
    public void send(Notification notification, Callbacks.IRequestCallback callback) {
        String notificationId = Library.getRootReference().push().getKey();

        notification.setId( notificationId);

        Library.getNotificationsRef(notification.getToUserId())
                .child( notification.getId())
                .setValue(notification);
    }*/
}
