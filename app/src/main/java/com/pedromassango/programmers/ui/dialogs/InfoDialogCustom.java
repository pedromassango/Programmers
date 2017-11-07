package com.pedromassango.programmers.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IDialogInfoOkClickedListener;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

public class InfoDialogCustom implements View.OnClickListener {

    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private IDialogInfoOkClickedListener onOkClickedListener;


    public InfoDialogCustom create(Context context, String title, String description) {

        //custom view for dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_info, null, false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_description)).setText(description);
        (view.findViewById(R.id.btn_ok)).setOnClickListener(this);

        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        return this;
    }

    public InfoDialogCustom create(Context context, String title, String description, boolean cancelable) {

        //custom view for dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_info, null, false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_description)).setText(description);
        (view.findViewById(R.id.btn_ok)).setOnClickListener(this);

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setView(view);

        return this;
    }

    public InfoDialogCustom setOnOkClickedListener(IDialogInfoOkClickedListener onOkClickedListener) {

        this.onOkClickedListener = onOkClickedListener;
        return this;
    }

    public void show(long delayTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
            }
        }, delayTime);
    }

    public void show() {
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

        //Close the dialog
        dialog.dismiss();

        //If onOkClickedListener is not NULL
        //we need to alert that the OK button was clicked
        if (onOkClickedListener == null) {
            return;
        }

        onOkClickedListener.onOkClick();
    }
}
