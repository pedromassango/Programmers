package com.pedromassango.programmers.services.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.Settings;
import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.services.LocalBroadcast;
import com.pedromassango.programmers.ui.notifications.CustomNotification;

import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

//public class AppFirebaseMessagingService {
public class AppFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        if (remoteMessage == null) return;

        Map<String, String> data = remoteMessage.getData();

        showLog("NOTIFY: " + data.toString());

        switch (remoteMessage.getMessageType()) {
            case Constants.NotificationType.POST:
                Post post = new Gson().fromJson(data.get(Constants.EXTRA_POST), Post.class);
                new CustomNotification()
                        .create(this)
                        .setType(post)
                        .show();
                break;

            case Constants.NotificationType.COMMENT:
                Comment comment = new Gson().fromJson(data.get(Constants.EXTRA_COMMENT), Comment.class);
                new CustomNotification()
                        .create(this)
                        .setType(comment)
                        .show();
                break;
        }
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);

        showLog("FCM: message sent");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        showLog("FCM: message deleted");
    }
}
