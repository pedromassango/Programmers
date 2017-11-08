package com.pedromassango.programmers.presentation.repport;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IDialogRetryListener;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.ui.dialogs.FailDialog;

public class BugActivity extends BaseActivity implements Contract.View {


    private EditText edtDescription;
    private CheckBox cbAttachEmail;

    private Presenter presenter;

    @Override
    protected int layoutResource() {
        return R.layout.activity_bug;
    }

    @Override
    protected void initializeViews() {

        edtDescription = findViewById(R.id.edt_bug_text);
        cbAttachEmail = findViewById(R.id.cb_attach_email);

        findViewById(R.id.btn_send_bug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.sendBugClicked();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
    }

    @Override
    public String getDescription() {

        return edtDescription.getText().toString();
    }

    @Override
    public boolean getAttachEmail() {

        return cbAttachEmail.isChecked();
    }

    @Override
    public void setDescriptionError(@StringRes int empty_description) {

        edtDescription.setError(getString(empty_description));
    }

    @Override
    public void showProgess(@StringRes int message) {

        super.showProgressDialog(message);
    }

    @Override
    public void dismissProgress() {
        super.dismissProgressDialog();
    }

    @Override
    public void showFailDialog(String message, IDialogRetryListener retryListener) {

        new FailDialog(this, retryListener)
                .show();
    }

    @Override
    public void onResultSuccess() {

        this.finish();
    }
}
