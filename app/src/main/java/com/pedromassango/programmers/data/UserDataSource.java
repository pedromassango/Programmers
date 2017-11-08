package com.pedromassango.programmers.data;

/**
 * Created by pedromassango on 11/8/17.
 */

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

    interface LoadUsersCallback{
        void onSuccess(List<Usuario> usuarios);
        void onDataUnavailable();
    }

    void getUsers(LoadUsersCallback callback);

    void getUserById(String userId, Callbacks.IResultCallback callback);

    void saveUser(Usuario usuario, Callbacks.IRequestCallback callback);
}
