package com.pedromassango.programmers.presentation.signup;

import android.content.Context;
import android.util.Patterns;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

/**
 * Created by Pedro Massango on 21-02-2017 23:58.
 */

class Presenter implements Contract.Presenter, Callbacks.IRequestCallback {

    private Contract.View view;

    Presenter(Contract.View view) {
        this.view = view;
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void onSignupClicked() {
        String email = view.getEmail();
        String password = view.getPassword();
        String password_2 = view.getPassword_2();

        if (email.isEmpty()) {
            view.setEmailError(getContext().getString(R.string.empty_email));
            return;
        }
        view.setEmailError(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.setEmailError(getContext().getString(R.string.incorrect_email));
            return;
        }
        view.setEmailError(null);

        if (password.trim().length() <= 0) {
            view.setPasswordError(getContext().getString(R.string.empty_password));
            return;
        }
        view.setPasswordError(null);

        if (password.length() < 8 || password.length() > 14) {
            view.setPasswordError(getContext().getString(R.string.password_less_than_eight));
            return;
        }
        view.setPasswordError(null);

        if (password_2.trim().length() <= 0) {
            view.setPassword_2Error(getContext().getString(R.string.empty_password_2));
            return;
        }
        view.setPassword_2Error(null);

        if (!password.equals(password_2)) {
            view.setPassword_2Error(getContext().getString(R.string.password_dont_match));
            return;
        }
        view.setPassword_2Error(null);

        Usuario usuario = Util.getSignupUser("", email, password);
        usuario.setCodeLevel(getContext().getString(R.string.beginner));

        // show loader
        view.showProgressDialog();

        //signup task from server
        RepositoryManager.getInstance()
                .getUsersRepository()
                .signup(usuario, this);
    }

    @Override
    public void onLoginClicked() {

        view.startLoginActivity();
    }

    @Override
    public void onTermsAndConditionsClicked() {

        view.startPrivacyPolicyActivity();
    }

    //SIGNUP SUCCEDED

    public void onSuccess(Usuario usuario) {

    }

    @Override
    public void onSuccess() {
        view.dismissProgressDialog();

        view.startLoginActivity();
    }

    @Override
    public void onError() {

        view.dismissProgressDialog();
        view.showFailDialog();
    }
}
