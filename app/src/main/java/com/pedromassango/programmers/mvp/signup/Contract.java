package com.pedromassango.programmers.mvp.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.BaseContract;

/**
 * Created by Pedro Massango on 21-02-2017 23:58.
 */

public class Contract {

    interface Model {
        void signup(Context context, OnSignupListener listener, Usuario usuario);
    }

    interface View {

        String getEmail();

        String getPassword();

        String getPassword_2();

        void setEmailError(String error);

        void setPasswordError(String error);

        void setPassword_2Error(String error);

        void showProgressDialog();

        void dismissProgressDialog();

        void startLoginActivity();

        void startEditProfileActivity(Bundle bundle);

        void showFailDialog(String error);

        void startPrivacyPolicyActivity();
    }

    interface Presenter extends BaseContract.PresenterImpl {

        void onSignupClicked();

        void onLoginClicked();

        void onTermsAndConditionsClicked();
    }

    public interface OnSignupListener {
        void onSuccess(Usuario usuario);

        void onError(String error);
    }
}
