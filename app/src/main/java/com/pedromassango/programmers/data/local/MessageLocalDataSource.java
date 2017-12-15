package com.pedromassango.programmers.data.local;

import com.pedromassango.programmers.data.MessageDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Message;

import io.realm.Realm;

/**
 * Created by Pedro Massango on 11/23/17.
 */

public class MessageLocalDataSource implements MessageDataSource {

    private static MessageLocalDataSource INSTANCE = null;

    public static MessageLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void send(Message message, Callbacks.IRequestCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm( message);
            realm.commitTransaction();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
