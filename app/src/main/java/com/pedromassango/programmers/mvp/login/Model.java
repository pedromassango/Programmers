package com.pedromassango.programmers.mvp.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.ILoginListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import static com.pedromassango.programmers.extras.Constants.AcountStatus.INCOMPLETE;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 21-02-2017 1:28.
 */

public class Model implements Contract.Model, OnCompleteListener<AuthResult>, FirebaseAuth.AuthStateListener, com.pedromassango.programmers.mvp.signup.Contract.OnSignupListener {

    private static String TAG = "login_model";
    private BaseContract.PresenterImpl presenter;
    private FirebaseAuth firebaseAuth;
    private ILoginListener iLoginListener;
    private boolean loginWithGoogle;

    Model(BaseContract.PresenterImpl presenter, ILoginListener listener) {
        this.presenter = presenter;
        this.iLoginListener = listener;
        this.firebaseAuth = Library.getFirebaseAuth();
    }

    //Login with Email and Password
    @Override
    public void login(final String email, final String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this);

        //To listen if we are logged in or not
        firebaseAuth.addAuthStateListener(this);
    }

    //Login with Google
    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showLog("firebaseAuthWithGoogle: " + account.getId());

        loginWithGoogle = true;

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this);
    }

    // If sign in fails, display a message to the user. If sign in succeeds
    // the firebaseAuth state iLoginListener will be notified and logic to handle the
    // signed in user can be handled in the iLoginListener.
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        //Signin failed
        if (!task.isSuccessful()) {
            iLoginListener.loginError(Util.getError(task.getException()));
            showLog("signIn:failed");
        } else {
            if (loginWithGoogle) {
                showLog("signIn:success");
                final FirebaseUser user = task.getResult().getUser();
                showLog("signIn - UID: " + user.getUid());

                loginFlow(user);
            }
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        showLog("onAuthStateChanged: " + user);

        if (user == null) {
            // User is signed out
            showLog("onAuthStateChanged:signed_out");
            return;
        }

        // User is signed in
        //signin was successful
        showLog("onAuthStateChanged:signed_in:" + user.getUid());
        if (loginWithGoogle)
            return;

        loginFlow(user);
    }

    private void loginFlow(final FirebaseUser user) {

        Library.getUsersRef()
                .child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showLog("onDataChange - saving user info");
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        if (loginWithGoogle) {
                            loginWithGoogleFlow(user, usuario);
                            return;
                        }


                        // START - send FCM token to server
                        //Worker.sendCurrentUserFCMToken(presenter.getContext());

                        // Subscribe the user to their favorite category, to receive notifications
                        // AND
                        // Subscribe the user to receive notifications when have a NEWS
                        //Worker.handleUserSubscriptionInCategory(usuario.getProgrammingLanguage(), true, null);
                        //NotificationSender.subscribe(AppRules.NEWS);

                        // if user's account is incomplete
                        // Then send it to edit their profile
                        if (usuario.getAccountComplete() == INCOMPLETE) {

                            iLoginListener.loginAccountInComplete(usuario);
                            return;
                        }

                        // Saving user info
                        PrefsUtil.setId(presenter.getContext(), usuario.getId());
                        PrefsUtil.setName(presenter.getContext(), usuario.getUsername());
                        PrefsUtil.setPhoto(presenter.getContext(), usuario.getUrlPhoto());

                        iLoginListener.loginAccountComplete(usuario);
                    }

                    //Some error
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        showLog("LOGIN WAS CANCELLED: " + databaseError.getMessage());
                    }
                });
    }

    private void loginWithGoogleFlow(FirebaseUser user, Usuario mServerUsuario) {

        com.pedromassango.programmers.mvp.signup.Model sModel = new com.pedromassango.programmers.mvp.signup.Model(presenter);
        String urlPhoto = user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString();
        Usuario usuario = Util.getSignupUser(user.getDisplayName(), user.getEmail(), "");
        usuario.setId(user.getUid());
        usuario.setCodeLevel(presenter.getContext().getString(R.string.beginner));
        usuario.setUrlPhoto(urlPhoto);

        if (mServerUsuario == null) {
            Log.v(TAG, "server use is null - calling: saveEmailAndPassword");
            sModel.saveEmailAndPasswordAuth(user, usuario, this);
        } else {
            Log.v(TAG, "server use is not null - calling: saveGoogle");
            sModel.saveGoogleSingnupAuth(user, usuario, this);
        }
    }

    @Override
    public void disconnectListeners() {
        if (null != firebaseAuth)
            firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onSuccess(Usuario usuario) {
        Log.v(TAG, "Login success");

        if (usuario.getAccountComplete() == INCOMPLETE) {
            Log.v(TAG, "Login success - InComplete");
            iLoginListener.loginAccountInComplete(usuario);
        } else {
            Log.v(TAG, "Login success - Complete");
            iLoginListener.loginAccountComplete(usuario);
        }
    }

    @Override
    public void onError(String error) {

        iLoginListener.loginError(error);
    }
}
