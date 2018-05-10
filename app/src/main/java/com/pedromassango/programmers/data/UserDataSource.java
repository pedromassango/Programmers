package com.pedromassango.programmers.data;

/**
 * Created by pedromassango on 11/8/17.
 */

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

import java.util.List;

/**
 * Interface to access the users data
 */

/**
 * Contains the users repository implementations, and some callbacks.
 */
public interface UserDataSource {

    void getUsers(Callbacks.IResultsCallback<Usuario> callback);

    void getUserById(String userId, Callbacks.IResultCallback<Usuario> callback);

    void saveUser(Usuario usuario, Callbacks.IRequestCallback callback);

    void login(final String email, final String password, Callbacks.IResultCallback<Usuario> callback);

    void signup(Usuario usuario, Callbacks.IRequestCallback callback);

    void firebaseAuthWithGoogle(GoogleSignInAccount account, Callbacks.IResultCallback<Usuario> callback);

    //void login( Callbacks.IRequestCallback callback);
    void checkLoggedInStatus(Callbacks.IRequestCallback callback);

    void logout(Callbacks.IRequestCallback callback);
}
