package com.pedromassango.programmers.presentation.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.UsersRepository;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.data.prefs.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.services.GoogleServices;

import static com.pedromassango.programmers.extras.Constants._DEVELOP_MODE;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-02-2017 14:58.
 */

public class MainPresenter implements Contract.Presenter, Callbacks.IResultCallback<Usuario>, Contract.OnDialogListener {

    private Usuario usuario = null;
    private Contract.View view;
    private UsersRepository usersRepository;
    private String userID;

    public MainPresenter(Contract.View view, UsersRepository usersRepository) {
        this.view = view;
        this.usersRepository = usersRepository;

        // Current user ID to get their info.
        userID = PrefsUtil.getId(getContext());
    }

    @Override
    public Context getContext() {
        return ((Context) view);
    }

    public Contract.View getView() {
        return view;
    }

    @Override
    public void initialize(Intent intent, Bundle savedState) {
        showLog("MAINPRENSETER STARTED");

        // Check if GooglePlayServices is avaliable and Installed
        GoogleServices services = new GoogleServices((Activity) getContext());
        if (!services.isGooglePlayServicesAvailable()) {
            return;
        }

        // Check if we have an instance saved
        if (savedState != null) {
            usuario = savedState.getParcelable(Constants.EXTRA_USER);
            showLog("show info from - savedState");
            // Show Usuario data and reload and get posts
            showUserInfoAndGetAllAppData(usuario);
            return;
        }

        // Check if the Activity sender, send an data
        // And if the data sent is an Usuario data.
        if (intent != null && intent.hasExtra(Constants.EXTRA_USER)) {
            usuario = intent.getParcelableExtra(Constants.EXTRA_USER);
            showLog("show info from - intent");
            // Show Usuario data and reload and get posts
            showUserInfoAndGetAllAppData(usuario);
            return;
        }

        // Test only
        if(Constants._DEVELOP_MODE){
            view.showHeaderInfo(Util.getUser());
            // set Empty, to get all default info on  all fragments
            view.setFragmentByCategory("");
            return;
        }

        showLog("getiing info from - firebase");

        //Call server worker here
        view.showProgress(R.string.getting_user_info);

        // get data from repository
        usersRepository.getLoggedUser(this);
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
            view.showDialogGetUserInfoError("Houve erros ao carregar os dados!", MainPresenter.this);
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

        // set Empty, to get all default info on  all fragments
        view.setFragmentByCategory("");
    }

    @Override
    public void onLogoutClicked() {

        view.showLogoutDialog();
    }

    //To check APP versionCode
    @Override
    public PackageInfo packageInfo() {
        PackageInfo packageInfo = null;
        try {
            String packageName = getContext().getPackageName();
            packageInfo = getContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            showLog("GET PACKAGE_NAME_ERROR", e.getMessage());
        }

        return packageInfo;
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

            case R.id.rate:
                view.startRateApp();
                break;

            case R.id.about:
                view.startAboutActivity();
                break;

            case R.id.settings:
                view.startSettingsActivity();
                break;

            case R.id.action_bug_report:
                view.startReportBugActivity();
                break;
        }
        return false;
    }

    @Override
    public void onRetry() {

        //Call server worker here
        view.showProgress(R.string.getting_user_info);

        // get data from repository
        usersRepository.getUserById(userID, this);
    }

    @Override
    public void onQuit() {

        view.quit();
    }
}
