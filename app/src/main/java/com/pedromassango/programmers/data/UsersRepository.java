package com.pedromassango.programmers.data;

/**
 * Created by pedromassango on 11/8/17.
 */

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Retrieve the user data from local or remote source.
 */
public class UsersRepository implements UserDataSource {

    // Store the instance
    private static UsersRepository INSTANCE = null;

    // Retrieve data from remote data source
    private final UserDataSource remoteDataSource;

    // Retrieve data from local data source.
    private final UserDataSource localDataSource;

    private boolean shouldFetchFromServer;

    // Private to prevent instatiation by constructor
    private UsersRepository(UserDataSource remoteDataSource,
                            UserDataSource localDataSource) {

        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;

        shouldFetchFromServer = PrefsHelper.shouldFetchFromServer();
    }

    // To return always the same instance, create if necessary.
    public static UsersRepository getInstance(UserDataSource remoteDataSource,
                                              UserDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    public void getLoggedUser(final Callbacks.IResultCallback<Usuario> callback) {
        final String loggedUserId = PrefsHelper.getId();
        showLog("USER ID: " + loggedUserId);
        showLog("USER ID: " + loggedUserId);
        showLog("USER ID: " + loggedUserId);

        localDataSource.getUserById(loggedUserId, new Callbacks.IResultCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario loggedUser) {

                // Saving session user info
                PrefsHelper.setId(loggedUser.getId());
                Log.v("logged_data", "LOGGED USER ID: " + PrefsHelper.getId());

                PrefsHelper.setName(loggedUser.getUsername());
                Log.v("logged_data", "LOGGED USER NAME: " + PrefsHelper.getName());

                PrefsHelper.setPhoto(loggedUser.getUrlPhoto());

                // respond with result from local data source
                callback.onSuccess(loggedUser);
            }

            @Override
            public void onDataUnavailable() {
                remoteDataSource.getUserById( loggedUserId, callback);
            }
        });
    }

    @Override
    public void getUsers(final Callbacks.IResultsCallback<Usuario> callback) {
        localDataSource.getUsers(new Callbacks.IResultsCallback<Usuario>() {
            @Override
            public void onSuccess(List<Usuario> results) {
                // respond imediately with local results
                callback.onSuccess(results);

                if(shouldFetchFromServer)
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

    public void getUsersByCategory(String mCategory, final Callbacks.IResultsCallback<Usuario> callback) {

        final String category = CategoriesUtils.getCategory(mCategory);

        this.getUsers(new Callbacks.IResultsCallback<Usuario>() {
            @Override
            public void onSuccess(List<Usuario> results) {
                List<Usuario> temp = new ArrayList<>();

                for (Usuario user : results) {
                    if (user.getFavoritesCategory().containsKey(category)) {
                        temp.add(user);
                    }
                }

                callback.onSuccess(temp);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void getUserById(final String userId, final Callbacks.IResultCallback<Usuario> callback) {
        localDataSource.getUserById(userId, new Callbacks.IResultCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {
                // respond with result from local data source
                callback.onSuccess(result);

                if(shouldFetchFromServer)
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

    private void getUsersFromRemoteAndUpdateLocalSource(final Callbacks.IResultsCallback<Usuario> callback) {
        remoteDataSource.getUsers(new Callbacks.IResultsCallback<Usuario>() {
            @Override
            public void onSuccess(List<Usuario> result) {

                // update local dataSource
                updateCacheAndLocal(result);

                // return the result
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void updateCacheAndLocal(List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            localDataSource.saveUser(usuario, null);
        }
    }

    @Override
    public void checkLoggedInStatus(final Callbacks.IRequestCallback callback) {
        remoteDataSource.checkLoggedInStatus(callback);
    }

    @Override
    public void signup(Usuario usuario, Callbacks.IRequestCallback callback) {
        remoteDataSource.signup(usuario, callback);
    }

    @Override
    public void login(String email, String password, final Callbacks.IResultCallback<Usuario> callback) {
        remoteDataSource.login(email, password, new Callbacks.IResultCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {

                PrefsHelper.setPhoto(result.getUrlPhoto());
                PrefsHelper.setName(result.getUsername());
                PrefsHelper.setId(result.getId());

                showLog("logged_data", "Name: " + result.getUsername());
                showLog("logged_data", "Photo: " + result.getUrlPhoto());
                showLog("logged_data", "Id: " + result.getId());

                localDataSource.saveUser( result, null);
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account, Callbacks.IResultCallback<Usuario> callback) {
        remoteDataSource.firebaseAuthWithGoogle(account, callback);
    }

    private void getUserFromRemoteAndUpdateLocalSource(String userId, final Callbacks.IResultCallback<Usuario> callback) {
        remoteDataSource.getUserById(userId, new Callbacks.IResultCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {

                // update local dataSource
                localDataSource.saveUser(result, null);

                // return the result
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    public  void updateLoggedUser(Usuario usuario){
        localDataSource.saveUser(usuario,  null);
    }

    @Override
    public void saveUser(final Usuario usuario, final Callbacks.IRequestCallback callback) {

        remoteDataSource.saveUser(usuario, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                // user saved on remote data source

                // update the user current info on local database
                localDataSource.saveUser(usuario, null);

                PrefsHelper.setName(usuario.getUsername());
                PrefsHelper.setId(usuario.getId());
                PrefsHelper.setPhoto(usuario.getUrlPhoto());

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
                PrefsHelper.endSession();

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
