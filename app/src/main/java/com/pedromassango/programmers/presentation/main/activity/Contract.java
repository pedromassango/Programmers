package com.pedromassango.programmers.presentation.main.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.MenuItem;

import com.pedromassango.programmers.interfaces.IDialogRetryListener;
import com.pedromassango.programmers.interfaces.IGetUserListener;
import com.pedromassango.programmers.interfaces.IRecyclerScrollListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 23-02-2017 14:58.
 */

public class Contract {

    interface Model {
        void getUser(IGetUserListener listener);

        void logoutUser();

        void onDestroy();
    }

    /**
     * The {@link IRecyclerScrollListener} to listen
     * the recyclerView Scroll state
     * and hadle FloatingActionButton
     */
    public interface View extends IRecyclerScrollListener {

        void setFragmentByCategory(String category);

        void showHeaderInfo(Usuario usuario);

        //On CLICK FOR HEADER ImageView CLICK
        //DON'T REMOVE THIS
        void gotoProfile(Bundle b);

        void startNewPostActivity();

        void showProgress(@StringRes int message);

        void dismissprogess();

        void showToast(String error);

        void showDialogGetUserInfoError(String string, OnDialogListener listener);

        void startReportBugActivity();

        void quit();

        void vibrate(int intensity);

        void startSettingsActivity();

        void setFABVisibility(boolean visibility);

        void startAboutActivity();

        void startDonateActivity();

        void startNewLinkActivity();

        void startRateApp();
    }

    interface Presenter extends BaseContract.PresenterImpl{

        void initialize(Intent intent, Bundle savedState);

        PackageInfo packageInfo();

        void onLogoutClicked();

        void setFragmentByCategory(String category);

        boolean onOptionsItemSelected(MenuItem item);

        void onScrollStateChanged( int newState);

        void leavingActivity();

        String getSkill(int skill);

        void onSaveInstanceState(Bundle outState);
    }

    interface OnDialogListener extends IDialogRetryListener {

        void onQuit();
    }
}
