package com.pedromassango.programmers.presentation.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 22-02-2017 16:26.
 */

public class Contract {

    interface View {
        String getUsername();

        void setUsernameError(@StringRes int error);

        String getEmail();

        void setEmailError(@StringRes int error);

        String getPhone();

        void setPhoneError(@StringRes int error);

        String getCity();

        void setCityError(@StringRes int error);

        void setImagePicked(Uri picked);

        String getLanguage();

        String getPlatform();

        String getAge();

        String getGender();

        String getCountry();

        void disableEditEmail();

        void showProgress(@StringRes int message);

        void dismissProgress();

        void onPickImageClicked();

        void startMainActivity();

        void showFailDialog();

        void setupViews(Usuario usuario);
    }

    public interface Presenter extends BaseContract.PresenterImpl {
        Context getContext();

        void iniitialize(Intent intent, Bundle bundle);

        void onPickImageClicked();

        void onSaveClicked();

        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

}
