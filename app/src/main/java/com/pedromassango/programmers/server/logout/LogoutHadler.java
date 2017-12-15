package com.pedromassango.programmers.server.logout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.UsersRepository;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
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

public class LogoutHadler implements Contract.View, Callbacks.IRequestCallback {

    private ProgressDialog progressDialogLogout;
    private UsersRepository usersRepository;
    private Context context;

    @Deprecated
    public LogoutHadler(BaseContract.PresenterImpl presenter) {
        //this.logoutPresenter = new Presenter(presenter.getContext(), this);
    }

    public LogoutHadler(Context context) {
        this.context = context;
        this.usersRepository = RepositoryManager.getInstance().getUsersRepository();
    }

    @Override
    public void showAlertDialogLogout() {
        // AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_description)
                .setNegativeButton(R.string.str_cancel, null)
                .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showProgressDialog();

                        usersRepository.logout(LogoutHadler.this);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showProgressDialog() {

        // ProgressDialog
        progressDialogLogout = new ProgressDialog(context);
        progressDialogLogout.setCancelable(false);
        progressDialogLogout.setIndeterminate(true);
        progressDialogLogout.setMessage(context.getString(R.string.str_quiting));

        progressDialogLogout.show();
    }

    @Override
    public void dismissProgressDialog() {

        progressDialogLogout.dismiss();
    }

    @Override
    public void startLoginActivity() {

        IntentUtils.startActivityCleaningTask(context, LoginActivity.class);
    }

    @Override
    public void showToast(String message) {

        Util.showToast(context, message);
    }

    @Override
    public void onSuccess() { //Logout success
        dismissProgressDialog();
        startLoginActivity();
    }

    @Override
    public void onError() { // Logout error
        dismissProgressDialog();
        showToast(context.getString(R.string.something_was_wrong));
    }
}
