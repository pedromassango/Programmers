package com.pedromassango.programmers.presentation.profile.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;

import com.pedromassango.programmers.interfaces.IDialogRetryListener;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.interfaces.IGetUserListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 23-02-2017 14:05.
 */

class Contract {

    interface Model {

        String getUserId();

        void getUser(String userId, IGetUserListener getUserListener);
    }

    interface View extends IGetDataCompleteListener, AppBarLayout.OnOffsetChangedListener {

        void fillViews(Usuario usuario);

        @Override
        void onGetDataSuccess();

        @Override
        void onGetError(String error);

        void startEditProfileActivity(Bundle b);

        void setButtonEditVisibility(int visibility);

        void setButtonCallVisibility(int visibility);

        void makeCall(Intent iCall);

        void setButtonEmailVisibility(int visibility);

        void startActivityToSendEmail(Intent iEmail);

        void startMessageActivity(Bundle b);

        void setButtonMessageVisibility(int gone);

        void showNoInternetDialog(IDialogRetryListener iDialogNoInternetListener);

        void showProgress(@StringRes int text);

        void dismissProgress();

        void setButtonsEnabled(boolean state);

        void showViewImageFragment(Bundle b);
    }

    interface Presenter extends BaseContract.PresenterImpl, IDialogRetryListener {

        void initialize(Intent intent, Bundle bundle);

        Context getContext();

        void onSaveInstanceState(Bundle outState);

        void editProfileClicked();

        void callButtonClicked();

        void SendEmailButtonClicked();

        void onSendMessageClicked();

        void showNoPostsMessage();

        void imgProfileClicked();
    }
}
