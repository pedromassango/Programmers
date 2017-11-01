package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.models.Usuario;

/**
 * Created by JANU on 24/05/2017.
 */

public interface ILoginListener {
    void loginAccountComplete(Usuario usuario);

    void loginAccountInComplete(Usuario usuario);

    void loginError(String error);
}
