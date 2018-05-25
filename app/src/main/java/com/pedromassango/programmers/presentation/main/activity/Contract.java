package com.pedromassango.programmers.presentation.main.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.MenuItem;

import com.pedromassango.programmers.interfaces.IDialogRetryListener;
import com.pedromassango.programmers.models.Usuario;

/**
 * Created by Pedro Massango on 23-02-2017 14:58.
 */

public class Contract {

    public interface View {

        void setFragmentByCategory(String category);

        void showHeaderInfo(Usuario usuario);

        //On CLICK FOR HEADER ImageView CLICK
        //DON'T REMOVE THIS
        void gotoProfile(Bundle b);

        void startNewPostActivity();

        void showProgress(@StringRes int message);

        void dismissprogess();

        void showToast(String error);

        void showDialogGetUserInfoError(OnDialogListener listener);

        void quit();

        void vibrate(int intensity);

        void startSettingsActivity();

        void setFABVisibility(boolean visibility);

        void startDonateActivity();

        void startRateApp();

        void showLogoutDialog();

        void openChatDrawer();

        void startLoginActivity();

        void showDefaultFragment();
    }

    interface Presenter{

        void init();

        void onLogoutClicked();

        void setFragmentByCategory(String category);

        boolean onOptionsItemSelected(MenuItem item);

        String getSkill(int skill);

        void onSaveInstanceState(Bundle outState);
    }

    interface OnDialogListener extends IDialogRetryListener {

        void onQuit();
    }
}
