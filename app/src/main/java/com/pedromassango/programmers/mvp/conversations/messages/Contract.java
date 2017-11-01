package com.pedromassango.programmers.mvp.conversations.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.mvp.conversations.ConversationContract;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class Contract {

    interface View {

        String getMessage();

        void setActivityTitle(String username);

        void handleMessages(String userInfoId);

    void showToast(String message);

        void setFriendOnlineIconVisibility(int visibility);

        void setActivitySubtitle(String lastOnline);

        void clearTypedText();
    }

    interface Presenter extends BaseContract.PresenterImpl {

        void onSendMessageClicked();

        void initialize(Intent intent, Bundle savedInstanceState);
    }
}
