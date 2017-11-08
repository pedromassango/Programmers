package com.pedromassango.programmers.server.logout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.presentation.login.LoginActivity;

/**
 * Created by Pedro Massango on 26/05/2017.
 * <p>
 * This class HADLER the logout work
 * just call the showAlertDialogLogout(), to use it.
 *
 * @Method showAlertDialogLogout() - ask to the user if he is sure to quit.
 */

public class LogoutHadler implements Contract.View {

    private ProgressDialog progressDialogLogout;
    private BaseContract.PresenterImpl presenter;
    private Presenter logoutPresenter;

    public LogoutHadler(BaseContract.PresenterImpl presenter) {
        this.presenter = presenter;
        this.logoutPresenter = new Presenter(presenter.getContext(), this);
    }

    @Override
    public void showAlertDialogLogout() {
        // AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
        builder.setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_description)
                .setNegativeButton(R.string.str_cancel, null)
                .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logoutPresenter.logoutClicked();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showProgressDialog() {

        // ProgressDialog
        progressDialogLogout = new ProgressDialog(presenter.getContext());
        progressDialogLogout.setCancelable(false);
        progressDialogLogout.setIndeterminate(true);
        progressDialogLogout.setMessage(presenter.getContext().getString(R.string.str_quiting));

        progressDialogLogout.show();
    }

    @Override
    public void dismissProgressDialog() {

        progressDialogLogout.dismiss();
    }

    @Override
    public void startLoginActivity() {

        IntentUtils.startActivityCleaningTask(presenter.getContext(), LoginActivity.class);
    }

    @Override
    public void showToast(String message) {

        Util.showToast(presenter.getContext(), message);
    }
}
