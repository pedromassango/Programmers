package com.pedromassango.programmers.data.remote;

import com.pedromassango.programmers.data.UserDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

/**
 * Created by pedromassango on 11/8/17.
 */

/**
 * retrieve the user data from remote source (Firebase)
 */
public class UserRemoteDataSource implements UserDataSource {

    // Store the instance
    private static UserRemoteDataSource INSTANCE;

    // prevent multiple instances of this class.
    public UserRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(Callbacks.IResultsCallback<Usuario> callback) {

        //TODO: get users from Firebase
    }

    @Override
    public void getUserById(String userId, Callbacks.IResultCallback<Usuario> callback) {

        //TODO: get single user by ID from Firebase
    }

    @Override
    public void saveUser(Usuario usuario, Callbacks.IRequestCallback callback) {

        //TODO: save user on Firebase
    }

    @Override
    public void logout(Callbacks.IRequestCallback callback) {

        //TODO: End session on FIrebase.
    }
}
