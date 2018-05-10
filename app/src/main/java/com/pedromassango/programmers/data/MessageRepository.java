package com.pedromassango.programmers.data;

import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Message;

/**
 * Created by Pedro Massango on 11/23/17.
 */

public class MessageRepository implements MessageDataSource {

    private static MessageRepository INSTANCE = null;
    private final MessageDataSource remoteSource;
    private final MessageDataSource localSource;

    private MessageRepository(MessageDataSource remoteSource, MessageDataSource localSource){

        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }

    public static MessageRepository getInstance(MessageDataSource remoteSource, MessageDataSource localSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepository(remoteSource, localSource);
        }
        return INSTANCE;
    }

    @Override
    public void send(final Message message, final Callbacks.IRequestCallback callback) {
        remoteSource.send(message, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                //TODO: wy to save message in local database??? we are using FirebaseAdapter.
                //localSource.send(message, null);

                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}
