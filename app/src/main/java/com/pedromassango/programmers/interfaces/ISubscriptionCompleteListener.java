package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 06/07/2017 at 23:33.
 */


/**
 *  This class is used to handle when tring to send an category topic to server
 */
public interface ISubscriptionCompleteListener extends IErrorListener{
    void onComplete(String category, boolean subscribed);
}
