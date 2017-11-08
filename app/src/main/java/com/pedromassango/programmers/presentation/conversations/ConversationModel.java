package com.pedromassango.programmers.presentation.conversations;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 27/06/2017 at 14:41.
 */

public class ConversationModel implements ConversationContract.ModelImpl {

    private BaseContract.PresenterImpl presenter;

    public ConversationModel(BaseContract.PresenterImpl presenter) {

        this.presenter = presenter;
    }

    @Override
    public String getUserId() {

        return PrefsUtil.getId(presenter.getContext());
    }


    public String getUsername() {

        return PrefsUtil.getName(presenter.getContext());
    }

    @Override
    public void handleFriendPresence(String friendId, IPresenceLIstener friendPresenceListener) {

        Worker.handleUserPresence(friendId, friendPresenceListener);
    }

    public void sendMessage(Message message, final IErrorListener errorListener) {

        // Message data
        Map<String, Object> messageValue = message.toMap();

        // Firebase childs to update
        Map<String, Object> childUpdates = new HashMap<>();

        // References to messages repository
        String myMessagesRef = AppRules.getMessagesRef(message.getAuthorId(), message.getReceiverId());
        String friendMessagesRef = AppRules.getMessagesRef(message.getReceiverId(), message.getAuthorId());

        childUpdates.put(myMessagesRef, messageValue);
        childUpdates.put(friendMessagesRef, messageValue);

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Failed to send Message
                        errorListener.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

    }
}
