package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 07/06/2017 at 14:05.
 */

public interface IUpdateListener {
    void updateSuccess();

    void updateError(String message);
}
