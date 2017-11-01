package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.models.Usuario;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public interface IGetUserListener {

    void onGetUserSuccess(Usuario usuario);

    void onGetUserError(String error);
}
