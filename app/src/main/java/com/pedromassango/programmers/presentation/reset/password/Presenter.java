package com.pedromassango.programmers.presentation.reset.password;

import android.util.Patterns;

import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.server.Worker;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 07-03-2017 10:46.
 */

class Presenter implements Contract.Presenter, Callbacks.IRequestCallback {

    private Contract.View view;

    public Presenter(Contract.View view){
        this.view = view;
    }

    @Override
    public void sendVerificationClicked() {

        showLog("CLICKED");

        String email = view.getEmail();

        if(email.isEmpty()){
            view.setEmailError();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.clearCurrentEmailText();
            view.setEmailError();
            return;
        }

        // show a progress dialog
        view.showProgress();

        Worker.sendEmailVerification(this, email);
        view.clearCurrentEmailText();
    }

    @Override
    public void onSuccess() {
        view.dismissProgress();

        view.showDialogSuccess();
    }

    @Override
    public void onError() {

        view.dismissProgress();
        view.showError();
    }
}
