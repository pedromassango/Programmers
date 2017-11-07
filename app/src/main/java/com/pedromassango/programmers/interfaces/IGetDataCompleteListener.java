package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public interface IGetDataCompleteListener {
    void onGetDataSuccess();
    void onGetError(String error);
}
