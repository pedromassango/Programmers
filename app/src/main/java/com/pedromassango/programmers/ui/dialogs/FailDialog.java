package com.pedromassango.programmers.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IDialogRetryListener;

/**
 * Created by Pedro Massango on 05/06/2017.
 */

public class FailDialog implements AlertDialog.OnClickListener {

    private AlertDialog.Builder builder;
    private IDialogRetryListener retryListener;
    private boolean cancelable;

    public FailDialog(Context context, IDialogRetryListener retryListener) {

        builder = new AlertDialog.Builder(context);
        this.retryListener = retryListener;
        builder.setTitle(R.string.internet_connection_error);
        builder.setMessage(R.string.internet_connection_error_description);
        builder.setPositiveButton(R.string.str_ok, null);
        builder.setNegativeButton(R.string.try_again, this);
    }

    public FailDialog(Context context, String title, String description, IDialogRetryListener retryListener) {

        builder = new AlertDialog.Builder(context);
        this.retryListener = retryListener;
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton(R.string.str_ok, null);
        builder.setNegativeButton(R.string.try_again, this);
    }

    public FailDialog(Context context, String description, IDialogRetryListener retryListener) {

        builder = new AlertDialog.Builder(context);
        this.retryListener = retryListener;
        builder.setTitle(R.string.internet_connection_error);
        builder.setMessage(description);
        builder.setPositiveButton(R.string.str_ok, null);
    }

    public FailDialog(Context context, String title, String description, boolean cancelable) {

        builder = new AlertDialog.Builder(context);
        this.cancelable = cancelable;
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton(R.string.str_ok, null);
    }

    public FailDialog(Context context, String description, boolean cancelable) {

        builder = new AlertDialog.Builder(context);
        this.cancelable = cancelable;
        builder.setTitle(R.string.internet_connection_error);
        builder.setMessage(description);
        builder.setPositiveButton(R.string.str_ok, null);
    }

    public void show() {

        builder.setCancelable(cancelable);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Retry button pressed
    @Override
    public void onClick(DialogInterface dialog, int which) {

        retryListener.onRetry();
    }
}
