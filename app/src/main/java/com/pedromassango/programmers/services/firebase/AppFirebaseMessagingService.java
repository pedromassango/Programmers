package com.pedromassango.programmers.services.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.ui.notifications.CustomNotification;

import org.json.JSONException;
import org.json.JSONObject;

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

        showLog("AppFirebaseMessagingService", "onMessageReceived: " + remoteMessage);

        if (remoteMessage.getData().size() == 0) {    // There are no messages
            return;
        }

        try {

            Map<String, String> messageData = remoteMessage.getData();
            String mapData = messageData.get("notification");

            showLog("AppFirebaseMessagingService", "notification: " + mapData);

            JSONObject data = new JSONObject(Constants.FCM_DATA);

            showLog("AppFirebaseMessagingService", "FCM_DATA: " + data);

            String messageType = data.getString(Constants.FCM_CONTENT_TYPE);

            showLog("AppFirebaseMessagingService", "messageType: " + messageType);

            String content = data.getString(Constants.FCM_CONTENT);

            showLog("AppFirebaseMessagingService", "content: " + content);

            switch (messageType) {
                case Constants.NotificationType.POST:
                    Post post = new Gson().fromJson(content, Post.class);
                    new CustomNotification()
                            .create(this)
                            .setType(post)
                            .show();
                    break;

                case Constants.NotificationType.COMMENT:
                    Comment comment = new Gson().fromJson(content, Comment.class);
                    new CustomNotification()
                            .create(this)
                            .setType(comment)
                            .show();
                    break;
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            showLog("AppFirebaseMessagingService", "EXCEPTION");
            Log.e("MessagingService", "EXCEPTION");
            showLog("AppFirebaseMessagingService", "EXCEPTION");
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
