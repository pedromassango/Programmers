package com.pedromassango.programmers.presentation.splash;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.interfaces.ILoggedInListener;
import com.pedromassango.programmers.server.Library;

/**
 * Created by Pedro Massango on 03-03-2017 10:45.
 */

class Model implements Contract.Model {

    private FirebaseAuth firebaseAuth;
    private Presenter presenter;
    private FirebaseAuth.AuthStateListener authStateListener;

    Model(Presenter presenter) {
        this.presenter = presenter;
        this.firebaseAuth = Library.getFirebaseAuth();
    }

    @Override
    public void checkLoggedInStatus(final ILoggedInListener loggedInListener) {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //User is logged in
                if (null != user) {
                    loggedInListener.onLoggedIn();
                    return;
                }

                // Check if is first time that the app is opened
                if (PrefsUtil.isFirstTime(presenter.getContext())) {
                    loggedInListener.onFirstTime();
                    return;
                }

                //User not loggedIn
                loggedInListener.onNotLoggedIn();
            }
        };

        //Registering the listener
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void removeListeners() {

        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
