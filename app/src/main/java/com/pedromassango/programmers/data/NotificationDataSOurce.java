package com.pedromassango.programmers.data;

import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Notification;

/**
 * Created by Pedro Massango on 11/18/17.
 */

public interface NotificationDataSOurce {

    void get(String userId, Callbacks.IResultsCallback<Notification> callback);

    void delete(Notification notification, Callbacks.IResultCallback<Notification> callback);
}
