package com.pedromassango.programmers.server.logout;

import android.content.Context;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.ILogoutListener;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

class Presenter implements Contract.Presenter, ILogoutListener {

    private Context context;
    private Contract.View view;
    private Contract.Model model;

    Presenter(Context context, Contract.View view) {
        this.context = context;
        this.view = view;
        this.model = new Model(this, this);
    }

    @Override
    public Context getContext() {

        return context;
    }

    @Override
    public void logoutClicked() {

        // show a progressDialog
        // And start the logout work
        view.showProgressDialog();
        model.signOut();
    }

    @Override
    public void onLogoutSuccess() {

        view.dismissProgressDialog();
        view.startLoginActivity();
    }

    @Override
    public void onLogoutError() {

        view.dismissProgressDialog();
        view.showToast(context.getString(R.string.something_was_wrong));
    }
}
