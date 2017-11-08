package com.pedromassango.programmers.server.logout;

import com.google.firebase.auth.FirebaseAuth;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

class Contract {

    interface Model extends FirebaseAuth.AuthStateListener {

        void signOut();
    }

    interface View {

        void showAlertDialogLogout();

        void showProgressDialog();

        void dismissProgressDialog();

        void startLoginActivity();

        void showToast(String message);
    }

    interface Presenter extends BaseContract.PresenterImpl{

        void logoutClicked();
    }
}
