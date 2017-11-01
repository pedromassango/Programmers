package com.pedromassango.programmers.mvp.profile.edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.activity.BaseActivity;
import com.pedromassango.programmers.mvp.main.activity.MainActivity;
import com.pedromassango.programmers.ui.dialogs.FailDialog;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity implements Contract.View {

    private static final int REQUEST_PICK_IMAGE = 1024;

    private static final int simple_list_item = android.R.layout.simple_list_item_1;

    private CircleImageView imgPicked;
    private EditText edtUsername;
    private EditText edtPhoneNumber;
    private EditText edtEmail;
    private EditText edtCity;
    private Spinner spLanguage;
    private Spinner spPlatform;
    private Spinner spAge;
    private Spinner spGender;
    private Spinner spCountry;

    private ArrayAdapter<String> languageAdapter;
    private ArrayAdapter<String> platformAdapter;
    private ArrayAdapter<String> ageAdapter;
    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<String> countryAdapter;

    private Presenter presenter;

    @Override
    public int layoutResource() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void initializeViews() {

        imgPicked = (CircleImageView) findViewById(R.id.img_picked);

        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPhoneNumber = (EditText) findViewById(R.id.edt_phone);
        edtCity = (EditText) findViewById(R.id.edt_city);

        spLanguage = (Spinner) findViewById(R.id.spinner_language);
        spPlatform = (Spinner) findViewById(R.id.spinner_platform);
        spAge = (Spinner) findViewById(R.id.spinner_age);
        spGender = (Spinner) findViewById(R.id.spinner_gender);
        spCountry = (Spinner) findViewById(R.id.spinner_country);

        //SETTING UP THE ADAPTER SPINNERS

        languageAdapter = new ArrayAdapter<>(this, simple_list_item, getResources().getStringArray(R.array.array_languages));
        spLanguage.setAdapter(languageAdapter);

        platformAdapter = new ArrayAdapter<>(this, simple_list_item, getResources().getStringArray(R.array.array_platform));
        spPlatform.setAdapter(platformAdapter);

        ageAdapter = new ArrayAdapter<>(this, simple_list_item, getResources().getStringArray(R.array.array_age));
        spAge.setAdapter(ageAdapter);

        genderAdapter = new ArrayAdapter<>(this, simple_list_item, getResources().getStringArray(R.array.array_gender));
        spGender.setAdapter(genderAdapter);

        countryAdapter = new ArrayAdapter<>(this, simple_list_item, getResources().getStringArray(R.array.array_country));
        spCountry.setAdapter(countryAdapter);

        (findViewById(R.id.editProfile_btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onSaveClicked();
            }
        });

        imgPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onPickImageClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.iniitialize(getIntent(), savedInstanceState);
    }

    @Override
    public void setupViews(Usuario usuario) {

        edtUsername.setText(usuario.getUsername());
        edtPhoneNumber.setText(usuario.getPhone());
        edtEmail.setText(usuario.getEmail());
        edtCity.setText(usuario.getCity());

        spLanguage.setSelection(languageAdapter.getPosition(usuario.getProgrammingLanguage()));
        spPlatform.setSelection(platformAdapter.getPosition(usuario.getPlatform()));
        spAge.setSelection(ageAdapter.getPosition(usuario.getAge()));
        spCountry.setSelection(countryAdapter.getPosition(usuario.getCountry()));
        spGender.setSelection(genderAdapter.getPosition(usuario.getGender()));

        ImageUtils.loadImageUser(this, usuario.getUrlPhoto(), imgPicked);
    }

    @Override
    public String getUsername() {
        return edtUsername.getText().toString();
    }

    @Override
    public void setUsernameError(@StringRes int error) {
        edtUsername.requestFocus();
        edtUsername.setError(getString(error));
    }

    @Override
    public String getEmail() {
        return edtEmail.getText().toString();
    }

    @Override
    public void setEmailError(@StringRes int error) {
        edtEmail.requestFocus();
        edtEmail.setError(getString(error));
    }

    @Override
    public String getPhone() {
        return edtPhoneNumber.getText().toString();
    }

    @Override
    public void setPhoneError(@StringRes int error) {
        edtPhoneNumber.requestFocus();
        edtPhoneNumber.setError(getString(error));
    }

    @Override
    public String getCity() {
        return edtCity.getText().toString();
    }

    @Override
    public void setCityError(@StringRes int error) {

        edtCity.requestFocus();
        edtCity.setError(getString(error));
    }

    @Override
    public void setImagePicked(Uri picked) {

        imgPicked.setPadding(0, 0, 0, 0);
        imgPicked.setBackgroundResource(android.R.color.transparent);
        ImageUtils.loadImageUser(this, picked.toString(), imgPicked);
    }

    @Override
    public String getLanguage() {
        return String.valueOf(spLanguage.getSelectedItem());
    }

    @Override
    public String getPlatform() {
        return String.valueOf(spPlatform.getSelectedItem());
    }

    @Override
    public String getAge() {
        return String.valueOf(spAge.getSelectedItem());
    }

    @Override
    public String getGender() {
        return String.valueOf(spGender.getSelectedItem());
    }

    @Override
    public String getCountry() {
        return String.valueOf(spCountry.getSelectedItem());
    }

    @Override
    public void disableEditEmail() {
        edtEmail.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
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
    public void onPickImageClicked() {

        Intent iGet = new Intent(Intent.ACTION_GET_CONTENT);
        iGet.setType("image/*");
        startActivityForResult(Intent.createChooser(iGet, getString(R.string.selecione_uma_imagem)), REQUEST_PICK_IMAGE);
    }

    @Override
    public void startMainActivity(Bundle userData) {

        IntentUtils.startActivityCleaningTask(this, userData, MainActivity.class);
    }

    @Override
    public void showFailDialog(String error) {

        new FailDialog(this, error, true)
                .show();

    }
}
