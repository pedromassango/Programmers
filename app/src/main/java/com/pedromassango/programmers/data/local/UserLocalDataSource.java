package com.pedromassango.programmers.data.local;

import com.pedromassango.programmers.data.UserDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

import java.util.List;

/**
 * Created by pedromassango on 11/8/17.
 */

/**
 * Retrieve user data from local source.
 */
public class UserLocalDataSource implements UserDataSource {

    // Store the instance
    private static UserLocalDataSource INSTANCE;

    // prevent multiple instances of this class.
    public UserLocalDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(Callbacks.IResultsCallback<Usuario> callback) {

        //TODO: get users from local data source.
    }

    @Override
    public void getUserById(String userId, Callbacks.IResultCallback<Usuario> callback) {

        //TODO: get a sinle user from local data source.
    }

    @Override
    public void saveUser(Usuario usuario, Callbacks.IRequestCallback callback) {

        //TODO: save user localy
    }

    public void saveOrUpdateUser(Usuario usuario){

    }

    public void saveOrUpdateUsers(List<Usuario> usuarios){

    }

    @Override
    public void logout(Callbacks.IRequestCallback callback) {

        //TODO: end session localy
    }
}
