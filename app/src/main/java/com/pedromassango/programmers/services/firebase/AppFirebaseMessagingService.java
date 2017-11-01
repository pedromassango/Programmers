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

public class AppFirebaseMessagingService {
/*public class AppFirebaseMessagingService extends FirebaseMessagingService {

    public static final String KEY_NOTIFICATION_VERSION = "com.pedromassango.programmers.services.onesignal.NOTIFICATION_VERSION_ID";
    public static final int NOTIFICATION_VERSION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        if (remoteMessage == null) return;

        Map<String, String> data = remoteMessage.getData();

        showLog("NOTIFY: " + data.toString());

        if (data.containsKey("version")) {
            int version = Integer.parseInt(data.get("version"));
            // Save the new app version
            new Settings(this).setInt(SettingsPreference.VERSION_KEY, version);

            // Check if the app is running
            if (Util.isAppRunning(this)) {
                showNewAppVersionDialog(this);
                return;
            }

            showNewAppVersionNotification(this);
            return;
        }

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

    //Necessário para quando o aplicativo estiver aberto
    private void showNewAppVersionDialog(Context context) {

        Intent intent = new Intent(LocalBroadcast.FILTER_KEY);
        LocalBroadcastManager
                .getInstance(context)
                .sendBroadcast(intent);
    }

    private void showNewAppVersionNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(getString(R.string.new_version_dialog_title))
                .setContentText(getString(R.string.new_version_dialog_message))
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_url)));
        PendingIntent piUpdateApp = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_vote_up_simple, getString(R.string.update), piUpdateApp);

        intent = new Intent(context, LocalBroadcast.class);
        intent.setAction(LocalBroadcast.CANCEL_NOTIFICATION_VERSION);
        intent.putExtra(KEY_NOTIFICATION_VERSION, NOTIFICATION_VERSION_ID);
        PendingIntent piLater = PendingIntent.getBroadcast(context, 9, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_clear, getString(R.string.later), piLater);

        // show notification
        nm.notify(NOTIFICATION_VERSION_ID, builder.build());
    }*/
}
