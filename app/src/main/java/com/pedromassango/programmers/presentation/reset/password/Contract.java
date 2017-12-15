package com.pedromassango.programmers.presentation.reset.password;

import android.content.Context;

/**
 * Created by Pedro Massango on 07-03-2017 10:46.
 */

class Contract {


    interface Model {
        void sendEmailVerification(OnSendResultListener listener, String email);
    }

    interface View {

        String getEmail();

        void setEmailError();

        void showProgress();

        void dismissProgress();

        void showError();

        void clearCurrentEmailText();

        void showDialogSuccess();
    }

    interface Presenter {
        void sendVerificationClicked();
    }

    interface OnSendResultListener{
        void onSendSuccess();
        void onSendError(String error);
    }
}
