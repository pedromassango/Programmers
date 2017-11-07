package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 24/05/2017.
 */

public interface IPostDeleteListener {

    void onPostDeleteSuccess();

    void onPostDeleteError(String error);
}
