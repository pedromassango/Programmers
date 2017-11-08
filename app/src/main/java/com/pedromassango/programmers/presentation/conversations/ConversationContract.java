package com.pedromassango.programmers.presentation.conversations;

import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 27/06/2017 at 14:41.
 */

public class ConversationContract {

    public interface ModelImpl extends BaseContract.ModelImpl {

        void sendMessage(Message message, IErrorListener messageListener);

        void handleFriendPresence(String id, IPresenceLIstener friendPresenceListener);
    }
}
