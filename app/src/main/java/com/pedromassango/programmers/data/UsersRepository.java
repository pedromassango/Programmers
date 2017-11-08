package com.pedromassango.programmers.data;

/**
 * Created by pedromassango on 11/8/17.
 */

import com.pedromassango.programmers.data.local.UserLocalDataSource;
import com.pedromassango.programmers.data.remote.UserRemoteDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

/**
 * Retrieve the user data from local or remote source.
 */
public class UsersRepository implements UserDataSource{

    // Store the instance
    private static UsersRepository INSTANCE = null;

    // Retrieve data from remote data source
    private final UserRemoteDataSource remoteDataSource;

    // Retrieve data from local data source.
    private final UserLocalDataSource localDataSource;

    // Private to prevent instatiation by constructor
    private UsersRepository(UserRemoteDataSource remoteDataSource,
                            UserLocalDataSource localDataSource){

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    // To return always the same instance, create if necessary.
    public UsersRepository getInstance(UserRemoteDataSource remoteDataSource,
                                       UserLocalDataSource localDataSource){
        if(INSTANCE == null){
            INSTANCE = new UsersRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(Callbacks.IResultsCallback<Usuario> callback) {
        //TODO: get users from local and remote data source
    }

    @Override
    public void getUserById(String userId, Callbacks.IResultCallback<Usuario> callback) {
        //TODO: get user from local and remote data source
    }

    @Override
    public void saveUser(Usuario usuario, Callbacks.IRequestCallback callback) {
        //TODO: get save user on remote and local data source
    }

    @Override
    public void logout(Callbacks.IRequestCallback callback) {
        //TODO: end session localy and remotely
    }
}
