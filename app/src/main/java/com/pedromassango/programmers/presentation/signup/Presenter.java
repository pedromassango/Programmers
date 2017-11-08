package com.pedromassango.programmers.presentation.signup;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Usuario;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;

/**
 * Created by Pedro Massango on 21-02-2017 23:58.
 */

class Presenter implements Contract.Presenter, Contract.OnSignupListener {

    private Contract.View view;
    private Model model;

    Presenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this);
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

        view.showProgressDialog();
        model.signup(getContext(), this, usuario);
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
    @Override
    public void onSuccess(Usuario usuario) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants._FIRST_TIME, true);
        bundle.putParcelable(EXTRA_USER, usuario);

        view.dismissProgressDialog();

        view.startEditProfileActivity(bundle);
    }

    //SIGNUP ERROR
    @Override
    public void onError(String error) {
        view.dismissProgressDialog();
        view.showFailDialog(error);
    }
}
