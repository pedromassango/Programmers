package com.pedromassango.programmers.presentation.post._new;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.extras.ImageUtils;

public class NewPostActivity extends BaseActivity implements Contract.View {


    private static final int REQUEST_PICK_IMAGE = 1024;

    private ImageView imgPicked;
    private RelativeLayout imgPickedLayout;
    private Spinner spLanguage;
    private EditText edtTitle;
    private EditText edtContent;

    private Presenter presenter;

    @Override
    public int layoutResource() {
        return R.layout.activity_new_post;
    }

    @Override
    public void initializeViews() {

        imgPicked = findViewById(R.id.new_post_imgPicked);

        spLanguage = findViewById(R.id.new_post_spinner);
        edtTitle = findViewById(R.id.edt_newPost_title);
        imgPickedLayout = findViewById(R.id.layout_img_picked);
        edtContent = findViewById(R.id.edt_newPost_Container);

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

        //Clear selected image
        findViewById(R.id.img_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.clearCurrentSelectedImageClicked();
            }
        });

        findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.publishPostClicked();
            }
        });

        //findViewById(R.id.img_pick).setEnabled(false);
        findViewById(R.id.img_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.pickImageClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.initialize(getIntent());
    }


    @Override
    public String getPostTitle() {
        return edtTitle.getText().toString();
    }

    @Override
    public void setTitleError(@StringRes int error) {
        edtTitle.requestFocus();
        edtTitle.setError(getString(error));
    }

    @Override
    public String getCategory() {
        return spLanguage.getSelectedItem().toString();
    }

    @Override
    public String getDescription() {
        return edtContent.getText().toString();
    }

    @Override
    public void setDescriptionError(@StringRes int error) {

        edtContent.requestFocus();
        edtContent.setError(getString(error));
    }

    @Override
    public void showProgress() {

        super.showProgressDialog(R.string.publishing);
    }

    @Override
    public void dismissProgress() {

        super.dismissProgressDialog();
    }

    @Override
    public void onPickImage(Intent iPickImage) {

        startActivityForResult(Intent.createChooser(iPickImage, getString(R.string.str_select)), REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showImagePicked(Uri imageUri) {

        ImageUtils.loadPostImage(this, imageUri, imgPicked);
    }

    @Override
    public void setImagePickedLayoutVisibility(int visibility) {

        imgPickedLayout.setVisibility(visibility);
    }

    @Override
    public void onSuccess() {

        this.finish();
    }

    @Override
    public void onError() {

        super.showToastMessage(R.string.something_was_wrong);
    }
}