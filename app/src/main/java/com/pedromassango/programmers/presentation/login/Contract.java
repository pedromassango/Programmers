package com.pedromassango.programmers.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 21-02-2017 1:05.
 */

class Contract {

    public interface Model {

        void login(final String email, final String password);

        void firebaseAuthWithGoogle(GoogleSignInAccount account);

        void disconnectListeners();
    }

    interface View {

        void init();

        String getEmail();

        String getPassword();

        void setEmailError(@StringRes int error);

        void setPasswordError(@StringRes int error);

        void startSignupActivity();

        void startResetPasswordActivity();

        void showFailDialog();

        void startMainActivity(Usuario usuario);

        void actionGoogleSignin(int RC_SIGN_IN, Intent signInIntent);

        void startEditProfileActivity(Usuario usuario);

        void showProgressDialog();

        void dismissProgressDialog();

        void showToast(String message);

        void setEmail(String lastEmail);

        void stratIntroActivity();
    }

    interface Presenter extends BaseContract.PresenterImpl{

        void configGoogleSigin();

        void loginClicked();

        void resetPasswordClicked();

        void signupClicked();

        void googleSiginClicked();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void leavingActivity();

        void initialize();

        void checkFirstTimeStatus();
    }
}
