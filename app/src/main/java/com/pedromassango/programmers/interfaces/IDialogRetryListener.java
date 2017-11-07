package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 05/06/2017.
 */

import com.pedromassango.programmers.ui.dialogs.FailDialog;

/**
 * Class to use on {@link FailDialog}
 * to handle wen a internet connection fail, and the user decide to re-try.
 * @Method onRetry()
 */
public interface IDialogRetryListener {

    void onRetry();
}
