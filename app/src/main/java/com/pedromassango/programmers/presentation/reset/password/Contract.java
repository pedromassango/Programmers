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

        void setEmailError(String emailError);

        void showProgress(String string);

        void dismissProgress();

        void showError(String string, String error);

        void showDialogNextStep(String title, String message);

        void clearCurrentEmailText();
    }

    interface Presenter {

        Context getContext();
        void sendVerificationClicked();
    }

    interface OnSendResultListener{
        void onSendSuccess();
        void onSendError(String error);
    }
}
