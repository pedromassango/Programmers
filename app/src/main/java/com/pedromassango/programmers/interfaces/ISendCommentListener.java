package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public interface ISendCommentListener {

    void onCommentSentSuccess();

    void onError(String error);
}
