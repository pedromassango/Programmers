package com.pedromassango.programmers.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.data.MessageDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.server.Library;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 11/23/17.
 */

public class MessageRemoteDataSource implements MessageDataSource {

    private static MessageRemoteDataSource INSTANCE = null;

    public static MessageRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void send(Message message, final Callbacks.IRequestCallback callback) {
        // Message data
        Map<String, Object> messageValue = message.toMap();

        // Firebase childs to update
        Map<String, Object> childUpdates = new HashMap<>();

        // References to users messages ref.
        String myMessagesRef = AppRules.getMessagesRef(message.getAuthorId(), message.getReceiverId());
        String friendMessagesRef = AppRules.getMessagesRef(message.getReceiverId(), message.getAuthorId());

        // Here, we need to pass reference to mcurrent message location, with their ID
        childUpdates.put(myMessagesRef + "/" + message.getId(), messageValue);
        childUpdates.put(friendMessagesRef + "/" + message.getId(), messageValue);

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Failed to send Message
                        callback.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                });
    }
}
