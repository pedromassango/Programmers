package com.pedromassango.programmers.mvp.repport;

import android.content.Context;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.interfaces.IDialogRetryListener;
import com.pedromassango.programmers.models.Bug;
import com.pedromassango.programmers.mvp.base.BaseContract;

/**
 * Created by Pedro Massango on 05-03-2017 20:07.
 */

class Contract {

    interface Model extends BaseContract.ModelImpl {

        void sendBug(OnSendBugListener listener, Bug bug);
    }

    interface View {
        String getDescription();

        boolean getAttachEmail();

        void setDescriptionError(@StringRes int empty_description);

        void showProgess(@StringRes int message);

        void dismissProgress();

        void showFailDialog(String message, IDialogRetryListener retryListener);

        void onResultSuccess();
    }

    interface Presenter extends BaseContract.PresenterImpl, IDialogRetryListener {

        void sendBugClicked();
    }

    interface OnSendBugListener {
        void onSuccess();

        void onError(String error);
    }
}
