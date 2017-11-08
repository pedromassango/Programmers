package com.pedromassango.programmers.data.local;

import com.pedromassango.programmers.data.UserDataSource;
import com.pedromassango.programmers.models.Usuario;

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
    public void getUsers(LoadUsersCallback callback) {
        //TODO: get users from local data source

        //callback.onSuccess( usuarios);
    }

    @Override
    public void saveUser(Usuario usuario) {
        //TODO: get sigle user from local data source
    }
}
