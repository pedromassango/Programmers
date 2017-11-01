package com.pedromassango.programmers.mvp.post.edit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.mvp.base.activity.BaseActivity;
import com.pedromassango.programmers.ui.dialogs.FailDialog;

public class EditPostActivity extends BaseActivity implements Contract.View {

    private Spinner spLanguage;
    private EditText edtTitle;
    private EditText edtContent;
    private Presenter presenter;
    private AlertDialog deletePostWarningDialog;

    @Override
    protected int layoutResource() {
        return R.layout.activity_edit_post;
    }

    @Override
    protected void initializeViews() {

        spLanguage = (Spinner) findViewById(R.id.new_post_spinner);
        edtTitle = (EditText) findViewById(R.id.edt_newPost_title);
        edtContent = (EditText) findViewById(R.id.edt_newPost_Container);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.array_languages));
        spLanguage.setAdapter(arrayAdapter);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView item = (TextView) view;
                item.setTextColor(getResources().getColor(R.color.primaryText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.updatePostClicked();
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.deletePostClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.initialize(getIntent(), savedInstanceState);
    }


    @Override
    public String getPostTitle() {
        return edtTitle.getText().toString();
    }

    @Override
    public String getPostDescription() {
        return edtContent.getText().toString();
    }

    @Override
    public String getPostCategory() {
        return String.valueOf(spLanguage.getSelectedItem());
    }

    @Override
    public void fillViews(Post post, int categoryPosition) {

        edtTitle.setText(post.getTitle());
        edtContent.setText(post.getBody());
        spLanguage.setSelection(categoryPosition, true);
    }

    @Override
    public void showFailDialog( String message) {

        new FailDialog(this, message, true)
                .show();
    }

    @Override
    public void showToast(@StringRes int message) {

        super.showToastMessage(message);
    }

    @Override
    public void showProgress(@StringRes int message) {

        super.showProgressDialog(message);
    }

    @Override
    public void dismissProgress() {
        super.dismissProgressDialog();
    }

    @Override
    public void actionPostUpdated() {
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void showDeletePostAlert(final Contract.OnUserPostDeleteChoseListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
            .setTitle(R.string.delete_post_title)
                .setMessage(R.string.delete_post_message)
                .setNegativeButton(R.string.str_cancel, null)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.deletePostConfirmed();
                    }
                });

        deletePostWarningDialog = builder.create();
        deletePostWarningDialog.show();
    }

    @Override
    public void postDeleted() {
        //Finish the activity
        this.finish();
    }
}
