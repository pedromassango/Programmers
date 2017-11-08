package com.pedromassango.programmers.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Pedro Massango on 13-04-2017 at 22:45.
 */

/***
 * This class Handle the notification status, to close or show it.
 */
public class LocalBroadcast extends BroadcastReceiver {

    public static final String CANCEL_NOTIFICATION_VERSION = "com.pedromassango.programmers.services.CANCEL_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {

            case CANCEL_NOTIFICATION_VERSION:
                //int notificationId = intent.getIntExtra(AppFirebaseMessagingService.KEY_NOTIFICATION_VERSION, 0);

               // ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
               //         .cancel(notificationId);
                break;
        }
    }
}
