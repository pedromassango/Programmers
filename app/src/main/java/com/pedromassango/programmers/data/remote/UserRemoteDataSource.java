package com.pedromassango.programmers.data.remote;

import com.pedromassango.programmers.data.UserDataSource;
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
    public void getUsers(LoadUsersCallback callback) {
        //TODO: get users from Firebase (remote server)
    }

    @Override
    public void saveUser(Usuario usuario) {
        //TODO: get save user on Firebase.
    }
}
