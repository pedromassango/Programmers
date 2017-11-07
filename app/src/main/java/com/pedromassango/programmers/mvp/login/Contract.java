package com.pedromassango.programmers.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.BaseContract;

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

        void showFailDialog(String error);

        void startMainActivity(Bundle userData);

        void actionGoogleSignin(int RC_SIGN_IN, Intent signInIntent);

        void startEditProfileActivity(Bundle bundle);

        void showProgressDialog();

        void dismissProgressDialog();

        void showToast(String message);

        void setEmail(String lastEmail);
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
    }
}
