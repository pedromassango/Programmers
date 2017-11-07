package com.pedromassango.programmers.mvp.link.views;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.mvp.base.activity.BaseActivity;
import com.pedromassango.programmers.mvp.link.LinkContract;
import com.pedromassango.programmers.mvp.link.LinkPresenter;
import com.pedromassango.programmers.extras.CategoriesUtils;

public class LinkActivity extends BaseActivity implements LinkContract.ViewActivity {

    private EditText edtDescription, edtLink;
    private Spinner spCategory;

    private LinkPresenter presenter;


    @Override
    protected int layoutResource() {

        return R.layout.activity_link;
    }

    @Override
    protected void initializeViews() {

        spCategory = findViewById(R.id.spinner_language);

        edtDescription = findViewById(R.id.edt_title);
        edtLink = findViewById(R.id.edt_link);

        findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onPublishClicked();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LinkPresenter(this);
        presenter.initialize(getIntent());
    }

    @Override
    public String getSelectedCategory() {

        return spCategory.getSelectedItem().toString();
    }


    @Override
    public void bindViews(Link link) {
        edtDescription.setText(link.getDescription());
        edtLink.setText(link.getUrl());
        spCategory.setSelection(CategoriesUtils.getCPosition(this, link.getCategory()));
    }

    @Override
    public String getDescription() {

        return edtDescription.getText().toString();
    }

    @Override
    public String getUrl() {
        return edtLink.getText().toString();
    }

    @Override
    public void setDescriptionError(String error) {
        edtDescription.setError(error);
    }

    @Override
    public void setURLError(String error) {
        edtLink.setError(error);
    }

    @Override
    public void showPublishProgress() {

        super.showProgressDialog(R.string.publishing);
    }

    @Override
    public void showToast(String message) {

        super.showToastMessage(message);
    }
}
