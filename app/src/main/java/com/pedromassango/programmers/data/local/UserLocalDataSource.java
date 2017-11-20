package com.pedromassango.programmers.data.local;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pedromassango.programmers.data.UserDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

import java.util.List;

import io.realm.Realm;

/**
 * Created by pedromassango on 11/8/17.
 */

/**
 * Retrieve user data from local source.
 */
public class UserLocalDataSource implements UserDataSource {

    // Store the instance
    private static UserLocalDataSource INSTANCE;

    private UserLocalDataSource() {

    }

    @Override
    public void login(String email, String password, Callbacks.IResultCallback<Usuario> callback) {
        //igonere here
    }

    @Override
    public void signup(Usuario usuario, Callbacks.IRequestCallback callback) {
//igonere here
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account, Callbacks.IResultCallback<Usuario> callback) {
//igonere here
    }

    // prevent multiple instances of this class.
    public static UserLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(Callbacks.IResultsCallback<Usuario> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            //realm.beginTransaction();
            List<Usuario> tempList = realm.where(Usuario.class).findAll();

            // if there is not data, notify empty data source
            if (tempList == null || tempList.isEmpty()) {
                callback.onDataUnavailable();
                return;
            }

            // Return the result to Repository
            callback.onSuccess(tempList);
        } catch (Exception e) {
            //print error
            e.printStackTrace();

            // notify error
            callback.onDataUnavailable();
        }
    }

    @Override
    public void getUserById(String userId, Callbacks.IResultCallback<Usuario> callback) {
        Realm realm = Realm.getDefaultInstance();

        try {
            Usuario usuario = realm.where(Usuario.class)
                    .beginsWith("id", userId).findFirst();

            // Check if the data is not null, if null there is no user with these Id
            if (usuario == null) {
                callback.onDataUnavailable();
                return;
            }

            // return the data
            callback.onSuccess(usuario);
        } catch (Exception e) {

            // print the error
            e.printStackTrace();
            // notify error
            callback.onDataUnavailable();
        }
    }

    @Override
    public void saveUser(Usuario usuario, Callbacks.IRequestCallback callback) {
        saveOrUpdateUser(usuario);
    }

    /**
     * This method is just to save with no callback required.
     *
     * @param usuario the uer to be saved.
     */
    public void saveOrUpdateUser(Usuario usuario) {
        Realm realm = Realm.getDefaultInstance();
        try {
            //save ususario on realm
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(usuario);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkLoggedInStatus(Callbacks.IRequestCallback callback) {
        // Ignored on local repository
    }

    @Override
    public void logout(Callbacks.IRequestCallback callback) {

        // Ignored on local repository
    }
}
