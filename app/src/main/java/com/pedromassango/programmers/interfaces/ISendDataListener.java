package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 15/06/2017 at 19:47.
 */

/**
 *  This class is used to notify wen we have making some operations on the server
 *  this is used to return de response from the server to Presenters
 *  @Method onSendSuccess() - notified when the action was successfull
 *  @Method onSendError() - notifified when thw action have exception
 */
public interface ISendDataListener {
    void onSendSuccess();
    void onSendError();
}
