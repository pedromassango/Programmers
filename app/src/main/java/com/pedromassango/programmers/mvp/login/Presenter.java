package com.pedromassango.programmers.mvp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Patterns;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.TextUtils;
import com.pedromassango.programmers.interfaces.ILoginListener;
import com.pedromassango.programmers.models.Usuario;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 21-02-2017 1:05.
 */

class Presenter implements Contract.Presenter, ILoginListener {

    private static final int RC_SIGN_IN = 1024;
    private Contract.View view;
    private Model model;
    private GoogleApiClient googleApiClient;

    Presenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this, this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize() {

        String lastEmail = PrefsUtil.readString(getContext(), Constants.LAST_EMAIL);
        view.setEmail(lastEmail);
    }

    @Override
    public void configGoogleSigin() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getContext().getString(R.string.default_google_web_client_id))
                .requestEmail()
                .build();

        //GoogleApiClient -> GOOGLE
        googleApiClient = new GoogleApiClient.Builder(getContext().getApplicationContext())
                .enableAutoManage(((FragmentActivity) view), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        showLog("GOOGLE SIGN_IN ERROR: " + connectionResult);

                       // view.showToast(getContext().getString(R.string.something_was_wrong));
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void googleSiginClicked() {

        //Call the GoogleSignInActivity
        //We no need to call progressdialog
        showLog(" calling Google Sign In Window... ");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        view.actionGoogleSignin(RC_SIGN_IN, signInIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                showLog(" Google Sign In state: " +result.getStatus().getStatusMessage());
                showLog(" Google Sign In state code: " +result.getStatus().getStatusCode());

                if (result.isSuccess()) {
                    showLog(" Google Sign In success, update UI appropriately:");

                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();

                    //Do login work
                    view.showProgressDialog();
                    model.firebaseAuthWithGoogle(account);
                } else {
                    showLog(" Google Sign In failed, update UI");

                    // Google Sign In failed, update UI appropriately
                    String googleSigninStatus = result.getStatus().getStatusMessage();
                    view.showToast(googleSigninStatus);
                }
            }
        }
    }

    @Override
    public void loginClicked() {
        String email = view.getEmail();
        String password = view.getPassword();

        if (TextUtils.isEmpty(email)) {
            view.setEmailError(R.string.empty_email);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.setEmailError(R.string.incorrect_email);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            view.setPasswordError(R.string.empty_password);
            return;
        }

        view.showProgressDialog();
        model.login(email, password);
    }

    @Override
    public void resetPasswordClicked() {

        view.startResetPasswordActivity();
    }

    @Override
    public void signupClicked() {

        view.startSignupActivity();
    }

    @Override
    public void loginAccountComplete(Usuario usuario) {

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_USER, usuario);

        view.dismissProgressDialog();
        view.startMainActivity(b);
    }

    @Override
    public void loginAccountInComplete(Usuario usuario) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants._FIRST_TIME, true);
        bundle.putParcelable(EXTRA_USER, usuario);

        view.dismissProgressDialog();
        view.startEditProfileActivity(bundle);
    }

    @Override
    public void loginError(String error) {
        view.dismissProgressDialog();
        view.showFailDialog(error);
    }

    @Override
    public void leavingActivity() {
        String currentEmail = view.getEmail();
        if (!TextUtils.isEmpty(currentEmail))
            PrefsUtil.saveToPreferences(getContext(), Constants.LAST_EMAIL, currentEmail);
        model.disconnectListeners();
    }
}
