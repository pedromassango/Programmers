package com.pedromassango.programmers.data;

/**
 * Created by pedromassango on 11/8/17.
 */

import com.pedromassango.programmers.data.local.UserLocalDataSource;
import com.pedromassango.programmers.data.remote.UserRemoteDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

import java.util.List;

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
    public static UsersRepository getInstance(UserRemoteDataSource remoteDataSource,
                                       UserLocalDataSource localDataSource){
        if(INSTANCE == null){
            INSTANCE = new UsersRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(final Callbacks.IResultsCallback<Usuario> callback) {
        localDataSource.getUsers(new Callbacks.IResultsCallback<Usuario>() {
            @Override
            public void onSuccess(List<Usuario> results) {
                // respond imediately with local results
                callback.onSuccess( results);

                // get from remote if availabe, and update the local source
                getUsersFromRemoteAndUpdateLocalSource(callback);
            }

            @Override
            public void onDataUnavailable() {
                // some thing goet wrong, notify the UI
                getUsersFromRemoteAndUpdateLocalSource(callback);
            }
        });
    }

    @Override
    public void getUserById(final String userId, final Callbacks.IResultCallback<Usuario> callback) {
        localDataSource.getUserById(userId, new Callbacks.IResultCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {
                // respond with result from local data source
                callback.onSuccess( result);

                // and, try to get updated usuario from remoteDataSource
                getUserFromRemoteAndUpdateLocalSource(userId, callback);
            }

            @Override
            public void onDataUnavailable() {
                // no user in local source, then fetch from remote
                getUserFromRemoteAndUpdateLocalSource(userId, callback);
            }
        });
    }

    private void getUsersFromRemoteAndUpdateLocalSource(final Callbacks.IResultsCallback<Usuario> callback){
        remoteDataSource.getUsers( new Callbacks.IResultsCallback<Usuario>() {
            @Override
            public void onSuccess(List<Usuario> result) {

                // update local dataSource
                localDataSource.saveOrUpdateUsers( result);

                // return the result
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }


    private void getUserFromRemoteAndUpdateLocalSource(String userId, final Callbacks.IResultCallback<Usuario> callback){
        remoteDataSource.getUserById(userId, new Callbacks.IResultCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {

                // update local dataSource
                localDataSource.saveOrUpdateUser( result);

                // return the result
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void saveUser(final Usuario usuario, final Callbacks.IRequestCallback callback) {

        remoteDataSource.saveUser(usuario, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                // user saved on remote data source

                // update the user current info on local database
                localDataSource.saveOrUpdateUser( usuario);

                // notify the UI
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void logout(final Callbacks.IRequestCallback callback) {
        remoteDataSource.logout(new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                // logged out from remote

                // remove local user data
                //localDataSource.logout();

                // Notify the UI
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}
