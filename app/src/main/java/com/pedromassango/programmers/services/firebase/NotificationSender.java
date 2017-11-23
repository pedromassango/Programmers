package com.pedromassango.programmers.services.firebase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.server.Library;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public class NotificationSender {

    // My Firebase sender id
    private static final String SENDER_ID = "AAAAwdXs0WI:APA91bGD1cAUXJ0kxmFzz0oJ35x85TtrX7WFHVWJPkf4sG-yNJQX8qjUwwayuISTUdu5PaCbFznLzTgvK8sJZ9eS2DQA-TOWeNcBZ-1TxBxyyt1scoO_vkIQTaKBGjkbolHTFrx5fA1r";
    private static final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "NotificationSender";

    private static FirebaseMessaging firebaseMessaging = Library.getFirebaseMessaging();

    public static void sendNotification(Post post) {
        Log.v(TAG, "sendNotification");
        Map<String, String> data = new HashMap<>();
        data.put(Constants.FCM_CONTENT, new Gson().toJson(post));
        data.put(Constants.FCM_CONTENT_TYPE, Constants.NotificationType.POST);

        String topic = Constants.NotificationTopics.NEWS;

        // send the notification
        send(data, topic);
    }

    public static void sendNotification(Comment comment) {
        Log.v(TAG, "sendNotification");

        Map<String, String> data = new HashMap<>();
        data.put(Constants.FCM_CONTENT, new Gson().toJson(comment));
        data.put(Constants.FCM_CONTENT_TYPE, Constants.NotificationType.COMMENT);

        String topic = comment.getPostId();

        // send notification
        send(data, topic);
    }

    public static void subscribe(String topic) {
        Log.v(TAG, "subscribe: " + topic);
        firebaseMessaging.subscribeToTopic(topic);
    }

    public static void unsubscribe(String s) {
        Log.v(TAG, "unsubscribe: " + s);
        firebaseMessaging.unsubscribeFromTopic(s);
    }

    private static void send(final Map<String, String> data, final String topic) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    FCM(data, topic);
                    Log.v(TAG, "sendNotification -> success");
                    Log.v(TAG, "sendNotification -> success");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.v(TAG, "sendNotification -> error");
                    Log.v(TAG, "sendNotification -> error");
                }
                return null;
            }
        }.execute();
    }

    private static void FCM(Map<String, String> data, String topic) throws Exception {
        URL url = new URL(API_URL_FCM);

        topic = topic.replace("-", "_").toLowerCase();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "key=" + SENDER_ID);
        connection.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("to", "/topics/" + topic);
        json.put(Constants.FCM_DATA, data);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write(json.toString());
        writer.flush();
        connection.getInputStream();
        //writer.close();
    }


    private static String getNotificationId() {
        return String.valueOf(System.currentTimeMillis() / 100);
    }
}
