package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.ui.dialogs.InfoDialogCustom;

/**
 * Created by Pedro Massango on 02/06/2017.
 *
 * Used by {@link InfoDialogCustom}
 * to listen when the user click on OK button
 */

public interface IDialogInfoOkClickedListener {
    void onOkClick();
}
