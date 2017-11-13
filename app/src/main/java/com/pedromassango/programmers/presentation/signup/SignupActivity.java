package com.pedromassango.programmers.presentation.signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.presentation.policy.PrivacyPolicyDialogFragment;
import com.pedromassango.programmers.presentation.profile.edit.EditProfileActivity;
import com.pedromassango.programmers.ui.dialogs.FailDialog;
import com.pedromassango.programmers.extras.IntentUtils;
import com.vstechlab.easyfonts.EasyFonts;

public class SignupActivity extends AppCompatActivity implements Contract.View {

    private TextInputLayout titEmail;
    private TextInputLayout tiPassword;
    private TextInputLayout tiPassword_2;
    private ProgressDialog dialog;

    // MVP
    private Presenter presenter;

    private void init() {
        ((TextView) findViewById(R.id.logo)).setTypeface(EasyFonts.caviarDreamsBold(this));

        titEmail = findViewById(R.id.input_email);
        tiPassword = findViewById(R.id.input_password);
        tiPassword_2 = findViewById(R.id.input_password_2);

        (findViewById(R.id.createAccount_btnSignup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onSignupClicked();
            }
        });

        (findViewById(R.id.link_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onLoginClicked();
            }
        });

        (findViewById(R.id.link_terms_and_conditions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onTermsAndConditionsClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.init();

        presenter = new Presenter(this);
    }

    @Override
    public String getEmail() {
        return titEmail.getEditText().getText().toString();
    }

    @Override
    public String getPassword() {
        return tiPassword.getEditText().getText().toString();
    }

    @Override
    public String getPassword_2() {
        return tiPassword_2.getEditText().getText().toString();
    }

    @Override
    public void setPassword_2Error(String error) {
        tiPassword_2.requestFocus();
        tiPassword_2.setError(error);
    }

    @Override
    public void setEmailError(String error) {
        titEmail.requestFocus();
        titEmail.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        tiPassword.requestFocus();
        tiPassword.setError(error);
    }

    @Override
    public void showProgressDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setMessage(getString(R.string.cadastrando));
        }
        dialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void startLoginActivity() {

        //IntentUtils.startActivity(this, LoginActivity.class);
        this.finish();
    }

    @Override
    public void startPrivacyPolicyActivity() {

        IntentUtils.showFragment(this, new PrivacyPolicyDialogFragment());
    }

    @Override
    public void startEditProfileActivity(Bundle bundle) {

        IntentUtils.startActivityCleaningTask(this, bundle, EditProfileActivity.class);
    }

    @Override
    public void showFailDialog(String error) {

        new FailDialog(this, getString(R.string.error), error, false)
                .show();
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        presenter.onLoginClicked();
    }
}
