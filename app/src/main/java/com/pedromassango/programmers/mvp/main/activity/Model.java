package com.pedromassango.programmers.mvp.main.activity;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetUserListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;
import com.pedromassango.programmers.server.logout.LogoutHadler;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-02-2017 14:59.
 */

class Model implements Contract.Model, ValueEventListener {

    private final String TAG = "MainModel";

    private MainPresenter mainPresenter;
    private DatabaseReference userRef;
    private IGetUserListener iGetUserListener;

    Model(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void getUser(final IGetUserListener iGetUserListener) {
        Log.v(TAG, "MainModel - getUser");
        this.iGetUserListener = iGetUserListener;

        userRef = Library.getCurrentUserRef();
        userRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void logoutUser() {
        Log.v(TAG, "MainModel - logoutUser");
        LogoutHadler logoutHadler = new LogoutHadler(mainPresenter);
        logoutHadler.showAlertDialogLogout();
    }

    @Override
    public void onDestroy() {
        showLog("MainModel - onDestroy");
        if (userRef != null) {
            userRef.removeEventListener(this);
        }
    }

    // Whill be notified when obtained the user data
    // And it is ready to show
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.v(TAG, "MainModel - onDataChange");
        Usuario usuario = dataSnapshot.getValue(Usuario.class);
        //usuario.setId(Library.getUserId());

        // Saving session user info
        PrefsUtil.setId(mainPresenter.getContext(), usuario.getId());
        Log.v(TAG,"LOGGED USER ID: " +PrefsUtil.getId(mainPresenter.getContext()).substring(0,6));

        PrefsUtil.setName(mainPresenter.getContext(), usuario.getUsername());
        Log.v(TAG,"LOGGED USER NAME: " +PrefsUtil.getName(mainPresenter.getContext()).substring(0,6));

        PrefsUtil.setPhoto(mainPresenter.getContext(), usuario.getUrlPhoto());

        // START - send FCM token to server
       // Worker.sendCurrentUserFCMToken(mainPresenter.getContext());

        iGetUserListener.onGetUserSuccess(usuario);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.v(TAG, "MainModel - onCancelled");
        showLog("GET USR ERROR: " + databaseError.getMessage());
        iGetUserListener.onGetUserError(Util.getError(databaseError.toException()));
    }
}
