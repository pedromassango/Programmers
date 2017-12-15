package com.pedromassango.programmers.presentation.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;

import com.google.gson.Gson;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.UsersRepository;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;
import static com.pedromassango.programmers.extras.Constants._DEVELOP_MODE;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 22-02-2017 16:26.
 */

class Presenter implements Contract.Presenter, Callbacks.IRequestCallback {

    private static final int REQUEST_PICK_IMAGE = 1024;
    private static final int RESULT_OK = -1;
    private boolean FIRST_TIME = false;
    private Usuario usuario;
    private Contract.View view;
    private Uri imagePicked = null;
    private UsersRepository usersRepository;

    Presenter(Contract.View view) {
        this.view = view;
        this.usersRepository = RepositoryManager
                .getInstance()
                .getUsersRepository();
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void iniitialize(Intent intent, Bundle bundle) {
        if (!_DEVELOP_MODE) {

            if (bundle != null) {
                usuario = bundle.getParcelable(EXTRA_USER);
            } else {

                FIRST_TIME = intent.getBooleanExtra(Constants._FIRST_TIME, false);
                usuario = intent.getParcelableExtra(EXTRA_USER);
            }

            if (FIRST_TIME) {
                view.disableEditEmail();
            }

            view.setupViews(usuario);
        }
    }


    @Override
    public void onSaveClicked() {
        String username = view.getUsername();
        String email = view.getEmail();
        String phone = view.getPhone();
        String city = view.getCity();
        String language = view.getLanguage();
        String platform = view.getPlatform();
        String age = view.getAge();
        String gender = view.getGender();
        String country = view.getCountry();


        if (username.isEmpty()) {
            view.setUsernameError(R.string.empty_username);
            return;
        }

        if (!FIRST_TIME) {
            if (email.isEmpty()) {
                view.setEmailError(R.string.empty_email);
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.setEmailError(R.string.incorrect_email);
                return;
            }
        }


        if (phone.isEmpty()) {
            view.setPhoneError(R.string.empty_phone);
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            view.setPhoneError(R.string.incorrect_phone_number);
            return;
        }

        if (city.isEmpty()) {
            view.setCityError(R.string.empty_city);
            return;
        }

        // Just set image if it was selected
        if(imagePicked != null) {
            String uriString = new Gson().toJson(imagePicked);
            usuario.setUrlPhoto(uriString); // In repository will be converted back to URI
        }

        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPhone(phone);
        usuario.setCity(city);
        usuario.setProgrammingLanguage(language);
        usuario.setPlatform(platform);
        usuario.setAge(age);
        usuario.setGender(gender);
        usuario.setCountry(country);
        usuario.setAccountComplete(Constants.AcountStatus.COMPLETE);

        view.showProgress(R.string.updating_profile);
        usersRepository.saveUser(usuario, this);
    }

    @Override
    public void onPickImageClicked() {

        view.onPickImageClicked();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (null != data) {

                    try {
                        imagePicked = data.getData();

                        view.setImagePicked(imagePicked);

                    } catch (Exception e) {
                        showLog("GET IMAGE EXEPTION: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void onSuccess() {

        // subscribe user to selected topic
        String topic = CategoriesUtils.getCategoryTopic( usuario.getProgrammingLanguage());
        NotificationSender.subscribe( topic);

        view.dismissProgress();
        view.startMainActivity();
    }

    @Override
    public void onError() {
        view.dismissProgress();
        view.showFailDialog();
    }
}
