package com.pedromassango.programmers.presentation.conversations.messages;

import android.content.Intent;
import android.os.Bundle;

import com.pedromassango.programmers.presentation.base.BaseContract;

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
