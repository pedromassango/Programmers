package com.pedromassango.programmers.presentation.post._new;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 23-02-2017 11:49.
 */

class Contract {

    interface View {
        String getPostTitle();

        void setTitleError(@StringRes int error);

        String getCategory();

        String getDescription();

        void setDescriptionError(@StringRes int error);

        void showProgress();

        void dismissProgress();

        void onPickImage(Intent iPickImage);

        void showImagePicked(Uri imageUri);

        void setImagePickedLayoutVisibility(int visibility);

        void onSuccess();

        void onError();
    }

    interface Presenter extends BaseContract.PresenterImpl {

        void initialize(Intent intent);

        void pickImageClicked();

        void publishPostClicked();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void clearCurrentSelectedImageClicked();
    }
}
