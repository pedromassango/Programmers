package com.pedromassango.programmers.presentation.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.UsersRepository;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.services.GoogleServices;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import static com.pedromassango.programmers.extras.Constants._DEVELOP_MODE;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-02-2017 14:58.
 */

public class MainPresenter implements Contract.Presenter, Callbacks.IResultCallback<Usuario>, Contract.OnDialogListener {

    private Usuario usuario = null;
    private Contract.View view;
    private MainPresenter instance;
    private UsersRepository usersRepository;

    public MainPresenter(Contract.View view, UsersRepository usersRepository) {
        this.view = view;
        this.instance = this;
        this.usersRepository = usersRepository;
    }

    @Override
    public void init() {
        usersRepository.checkLoggedInStatus(new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                // Do nothing here if the user is logged in

                // Test only
                if (Constants._DEVELOP_MODE) {
                    view.showHeaderInfo(Util.getUser());

                    // Subscribe user to NEWS topic
                    NotificationSender.subscribe( Constants.NotificationTopics.NEWS);

                    // set Empty, to get all default info on  all fragments
                    view.setFragmentByCategory("");
                    return;
                }

                showLog("getiing info from - firebase");

                //Call server worker here
                view.showProgress(R.string.getting_user_info);

                // get data from repository
                usersRepository.getLoggedUser(instance);
            }

            @Override
            public void onError() {
                view.startLoginActivity();
            }
        });
    }

    @Override
    public void onSuccess(Usuario result) {
        usuario = result;

        view.dismissprogess();

        this.showUserInfoAndGetAllAppData(result);
    }

    @Override
    public void onDataUnavailable() {
        view.dismissprogess();

        if (!_DEVELOP_MODE) {
            view.showDialogGetUserInfoError(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.EXTRA_USER, usuario);
    }

    @Override
    public void setFragmentByCategory(String category) {

        view.setFragmentByCategory(category);
    }

    // Show Usuario data and get all Posts
    private void showUserInfoAndGetAllAppData(Usuario mUsuario) {
        showLog("showing user info...");
        showLog("setting up all fragments...");

        view.showHeaderInfo(mUsuario);

        //  Now tthat we fetch user data successful we can get and show all posts
        view.showDefaultFragment();

        // set Empty, to get all default info on  all fragments
        view.setFragmentByCategory("");
    }

    @Override
    public void onLogoutClicked() {

        view.showLogoutDialog();
    }

    @Override
    public String getSkill(int skill) {
        return String.format("%s%s", skill, "%");
    }

    /**
     * Hadle recyclerView scroll status
     * just to show FAB
     *
     * @param newState the new state of the recyclerView
     */
    @Override
    public void onScrollStateChanged(int newState) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE &&
                newState != RecyclerView.SCROLL_STATE_DRAGGING) {
            view.setFABVisibility(false);
        } else {
            view.setFABVisibility(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donate:
                view.startDonateActivity();
                break;

            case R.id.chat:
                view.openChatDrawer();
                break;

            case R.id.rate:
                view.startRateApp();
                break;

            case R.id.settings:
                view.startSettingsActivity();
                break;
        }
        return false;
    }

    @Override
    public void onRetry() {

        //Call server worker here
        view.showProgress(R.string.getting_user_info);

        // get data from repository
        usersRepository.getLoggedUser(this);
    }

    @Override
    public void onQuit() {

        view.quit();
    }

    public Contract.View getView() {
        return view;
    }
}
