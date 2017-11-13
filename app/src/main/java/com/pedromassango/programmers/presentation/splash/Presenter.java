package com.pedromassango.programmers.presentation.splash;

import android.content.Context;
import android.os.Handler;

import com.pedromassango.programmers.data.prefs.PrefsUtil;
import com.pedromassango.programmers.interfaces.ILoggedInListener;

/**
 * Created by Pedro Massango on 23-02-2017 14:36.
 */

class Presenter implements Contract.Presenter, ILoggedInListener {

    private static final long TIME_TO_SLEE_WHEN_LOGGED_IN = 500;
    private static final long TIME_TO_SLEE_WHEN_NOT_LOGGED_IN = 1200;
    private Contract.View view;
    private Model model;

    Presenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void onResume() {

        //check if we are connected
        model.checkLoggedInStatus(this);

        // Increase the times that the app was opened
        PrefsUtil.increaseTimesThatTheAppWasOpened(getContext());
    }

    @Override
    public void onFirstTime() {

        view.startIntroActivity();
    }

    @Override
    public void onLoggedIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                view.startMainActivity();
            }
        }, TIME_TO_SLEE_WHEN_LOGGED_IN);
    }

    @Override
    public void onNotLoggedIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                view.startLoginActivity();
            }
        }, TIME_TO_SLEE_WHEN_NOT_LOGGED_IN);
    }

    @Override
    public void onDestroy() {

        model.removeListeners();
    }
}
