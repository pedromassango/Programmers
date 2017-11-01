package com.pedromassango.programmers.interfaces;

/**
 * Created by JM on 9/19/2017.
 */

public interface ISaveListener extends IErrorListener {
    void onSaveSuccess(String message);
}
