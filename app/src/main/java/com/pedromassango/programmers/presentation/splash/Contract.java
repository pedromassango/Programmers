package com.pedromassango.programmers.presentation.splash;

import android.content.Context;

import com.pedromassango.programmers.interfaces.ILoggedInListener;

/**
 * Created by Pedro Massango on 23-02-2017 14:37.
 */

class Contract {

    interface Model {

        void checkLoggedInStatus(ILoggedInListener loggedInListener);

        void removeListeners();
    }

    interface View {
        void startMainActivity();

        void startLoginActivity();

        void startIntroActivity();
    }

    interface Presenter {
        void onResume();

        Context getContext();

        void onDestroy();
    }
}
