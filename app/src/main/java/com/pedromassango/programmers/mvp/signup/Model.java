package com.pedromassango.programmers.mvp.signup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.server.Library;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 22-02-2017 0:18.
 */

public class Model implements Contract.Model, OnFailureListener, OnSuccessListener<AuthResult> {
    private static final String TAG = "output";
    private Usuario usuario;
    private BaseContract.PresenterImpl presenter;
    private OnSuccessListener<Void> onSuccessListener;
    private Contract.OnSignupListener onSignupListener;

    public Model(final BaseContract.PresenterImpl presenter) {

        this.presenter = presenter;
        this.onSuccessListener = new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Update account complete user status
                usuario.setAccountComplete(Constants.AcountStatus.COMPLETE);

                // Saving user info
                PrefsUtil.setId(presenter.getContext(), usuario.getId());
                PrefsUtil.setName(presenter.getContext(), usuario.getUsername());
                PrefsUtil.setPhoto(presenter.getContext(), usuario.getUrlPhoto());

                onSignupListener.onSuccess(usuario);
            }
        };
    }

    @Override
    public void signup(final Context context, final Contract.OnSignupListener listener, final Usuario usuario) {
        this.onSignupListener = listener;
        this.usuario = usuario;

        FirebaseAuth auth = Library.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getPassword())
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {

        //falha
        onSignupListener.onError(Util.getError(e));
    }

    //Account created successfull
    //Now we need to save user information on database
    @Override
    public void onSuccess(AuthResult authResult) {

        saveEmailAndPasswordAuth(authResult.getUser(), usuario, onSignupListener);
    }

    /***
     * This method is to save data when singned up with email and password
     * @param user the firebase user to retieve the user id and URL photo
     * @param usuario the user info to save
     * @param listener to notify when the task fail or succedded
     */
    public void saveEmailAndPasswordAuth(final FirebaseUser user, final Usuario usuario, final Contract.OnSignupListener listener) {
        this.usuario = usuario;
        onSignupListener = listener;
        String userId = user.getUid();
        usuario.setId(userId);
        Log.i(TAG, "signup: userID: " + userId);

        Library.getUsersRef()
                .child(usuario.getId())
                .setValue(usuario)
                .addOnFailureListener(this)
                .addOnSuccessListener(onSuccessListener);
    }

    /***
     * This method is to save data when singned up with Google
     * @param user the firebase user to retieve the user id and URL photo
     * @param usuario the user info to save
     * @param listener to notify when the task fail or succedded
     */
    public void saveGoogleSingnupAuth(final FirebaseUser user, final Usuario usuario, final Contract.OnSignupListener listener) {
        this.usuario = usuario;
        onSignupListener = listener;
        String userId = user.getUid();
        usuario.setId(userId);
        Log.i(TAG, "signup: userID: " + userId);

        Map<String, Object> childUpdates = new HashMap<>();
        //All user properties reference
        //childUpdates.put("/users/" + userId + "/email/", usuario.getEmail());
        //childUpdates.put("/users/" + userId + "/id/", userId);
        childUpdates.put("/users/" + userId + "/username/", usuario.getUsername());
        childUpdates.put("/users/" + userId + "/urlPhoto/", usuario.getUrlPhoto());

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(this)
                .addOnSuccessListener(onSuccessListener);
    }
}
