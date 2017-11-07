package com.pedromassango.programmers.services.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.server.Library;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public class NotificationSender {

    /*// My Firebase sender id
    private static final String SENDER_ID = "781049805208";

    private static FirebaseMessaging firebaseMessaging = Library.getFirebaseMessaging();
    private static RemoteMessage.Builder builder = new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com");

    public static void sendNotification(Post post) {

        Map<String, String> notificationData = new HashMap<>();
        notificationData.put(Constants.EXTRA_POST, post.toString());

        builder.setMessageId(getNotificationId())
                .setMessageType(Constants.NotificationType.POST)
                .setData(notificationData);

        firebaseMessaging.send(builder.build());
    }

    public static void sendNotification(Comment comment) {

        Map<String, String> notificationData = new HashMap<>();
        notificationData.put(Constants.EXTRA_COMMENT, comment.toString());

        builder.setMessageId(getNotificationId())
                .setMessageType(Constants.NotificationType.COMMENT)
                .setData(notificationData);

        firebaseMessaging.send(builder.build());
    }

    public static void subscribe(String s) {

        firebaseMessaging.subscribeToTopic(s);
    }

    public static void unsubscribe(String s) {

        firebaseMessaging.unsubscribeFromTopic(s);
    }

    private static String getNotificationId() {

        return String.valueOf(System.currentTimeMillis() / 100);
    }*/
}
