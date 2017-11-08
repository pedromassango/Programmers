package com.pedromassango.programmers.server.logout;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.interfaces.ILogoutListener;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.server.Library;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

class Model implements Contract.Model {

    private FirebaseAuth firebaseAuth;
    private BaseContract.PresenterImpl basePresenter;
    private ILogoutListener logoutListener;


    /**
     * @param basePresenter  - the Presenter to retrive the context to show the logout dialog and get data.
     * @param logoutListener - to listen the logout
     */
    public Model(BaseContract.PresenterImpl basePresenter, ILogoutListener logoutListener) {
        this.basePresenter = basePresenter;

        this.logoutListener = logoutListener;
        this.firebaseAuth = Library.getFirebaseAuth();
    }

    @Override
    public void signOut() {

        // Bind a listener and
        // Logout user in Firebase.
        firebaseAuth.signOut();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth mFirebaseAuth) {
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // An error occour when trie to logout

            logoutListener.onLogoutError();
            return;
        }

        // signout successfull on Firebase
        // we need to remove the local state in Realm PrefsUtil
        PrefsUtil.endSession(basePresenter.getContext());

        logoutListener.onLogoutSuccess();
    }
}
