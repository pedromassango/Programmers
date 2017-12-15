package com.pedromassango.programmers.data;

import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Message;

/**
 * Created by Pedro Massango on 11/23/17.
 */

public interface MessageDataSource {

    void send(Message message, Callbacks.IRequestCallback callback);
}
