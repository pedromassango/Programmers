package com.pedromassango.programmers.presentation.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.main.activity.MainActivity;
import com.pedromassango.programmers.presentation.profile.edit.EditProfileActivity;
import com.pedromassango.programmers.presentation.reset.password.ResetPasswordDialogFragment;
import com.pedromassango.programmers.presentation.signup.SignupActivity;
import com.pedromassango.programmers.services.GoogleServices;
import com.pedromassango.programmers.ui.dialogs.FailDialog;
import com.vstechlab.easyfonts.EasyFonts;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Contract.View {

    private TextInputLayout tiEmail;
    private TextInputLayout tiPassword;
    private ProgressDialog dialog;
    private SignInButton signInButton;
    private Presenter presenter;

    @Override
    public void init() {

        // Set up the login form.
        ((TextView) findViewById(R.id.logo)).setTypeface(EasyFonts.caviarDreamsBold(this));

        tiEmail = findViewById(R.id.input_email);
        tiPassword = findViewById(R.id.input_password);
        signInButton = findViewById(R.id.btn_google_login);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.loginClicked();
            }
        });

        (findViewById(R.id.btn_signup)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.signupClicked();
            }
        });
        (findViewById(R.id.link_reset_password)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.resetPasswordClicked();
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.googleSiginClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.init();

        presenter = new Presenter(this);
        presenter.initialize();
        presenter.configGoogleSigin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This will check if the device have
        // the apropriated Play Service version
        // If not, the app will be closed
        new GoogleServices(this).isGooglePlayServicesAvailable();
    }

    @Override
    public void setEmail(String lastEmail) {

        tiEmail.getEditText().setText(lastEmail);
    }

    @Override
    public String getEmail() {
        return tiEmail.getEditText().getText().toString();
    }

    @Override
    public String getPassword() {
        return tiPassword.getEditText().getText().toString();
    }

    @Override
    public void setEmailError(@StringRes int error) {
        tiEmail.requestFocus();
        tiEmail.setError(getString(error));
    }

    @Override
    public void setPasswordError(@StringRes int error) {
        tiPassword.requestFocus();
        tiPassword.setError(getString(error));
    }

    @Override
    public void showToast(String message) {

        Util.showToast(this, message);
    }

    @Override
    public void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.autenticando));
        dialog.show();
    }

    @Override
    public void dismissProgressDialog() {

        dialog.dismiss();
    }

    @Override
    public void startSignupActivity() {

        IntentUtils.startActivity(this, SignupActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void actionGoogleSignin(int RC_SIGN_IN, Intent signInIntent) {

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFailDialog() {

        new FailDialog(this,
                getString(R.string.str_login_error_title),
                getString(R.string.something_was_wrong), true)
                .show();
    }

    @Override
    public void startMainActivity(Usuario usuario) {

        Bundle bundle = new Bundle();
        //bundle.putBoolean(Constants._FIRST_TIME, true);
        bundle.putParcelable(EXTRA_USER, usuario);

        IntentUtils.startActivity(this, bundle, MainActivity.class);
        this.finish(); //Removing from list of activities
    }

    @Override
    public void startEditProfileActivity(Usuario usuario) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants._FIRST_TIME, true);
        bundle.putParcelable(EXTRA_USER, usuario);

        IntentUtils.startActivity(this, bundle, EditProfileActivity.class);
        this.finish(); //Removing from list of activities
    }

    @Override
    public void startResetPasswordActivity() {

        IntentUtils.showFragment(this, new ResetPasswordDialogFragment());
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.leavingActivity();
    }
}

