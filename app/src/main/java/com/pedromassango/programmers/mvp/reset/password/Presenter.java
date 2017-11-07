package com.pedromassango.programmers.mvp.reset.password;

import android.content.Context;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Patterns;

import com.pedromassango.programmers.R;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 07-03-2017 10:46.
 */

class Presenter implements Contract.Presenter, Contract.OnSendResultListener {

    private Contract.View view;
    private Model model;

    public Presenter(Contract.View view){
        this.view = view;
        this.model = new Model();
    }

    @Override
    public Context getContext() {
        return ((AppCompatDialogFragment)view).getContext();
    }

    @Override
    public void sendVerificationClicked() {

        showLog("CLICKED");

        String email = view.getEmail();

        if(email.isEmpty()){
            view.setEmailError(getContext().getString(R.string.empty_email));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.clearCurrentEmailText();
            view.setEmailError(getContext().getString(R.string.incorrect_email));
            return;
        }

        view.showProgress(getContext().getString(R.string.sending));
        model.sendEmailVerification(this, email);
        view.clearCurrentEmailText();
    }

    @Override
    public void onSendSuccess() {

        view.dismissProgress();
        view.showDialogNextStep(getContext().getString(R.string.email_verification_success_title), getContext().getString(R.string.email_verification_success_message));
    }

    @Override
    public void onSendError(String error) {

        view.dismissProgress();
        view.showError(getContext().getString(R.string.something_was_wrong),error);
    }
}
