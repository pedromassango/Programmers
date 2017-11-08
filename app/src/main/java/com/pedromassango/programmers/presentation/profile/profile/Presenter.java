package com.pedromassango.programmers.presentation.profile.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.profile.ProfileModel;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;

/**
 * Created by Pedro Massango on 23-02-2017 14:05.
 */

class Presenter implements Contract.Presenter {

    private Contract.View view;
    private ProfileModel model;
    private Usuario usuario;
    private Context context;
    private String userIdToLoadData;

    Presenter(Contract.View view) {
        this.view = view;
        this.context = (Context) view;
        this.model = new ProfileModel(this);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void initialize(Intent intent, Bundle bundle) {

        // Check if we have an user on this instance
        if (bundle != null) {
            usuario = bundle.getParcelable(EXTRA_USER);
            this.bindDataToViews(usuario);
            return;
        }

        // Check if we have reveived an User data
        if (intent.hasExtra(Constants.EXTRA_USER)) {
            usuario = intent.getParcelableExtra(EXTRA_USER);
            this.bindDataToViews(usuario);
            return;
        }

        // So, if we do not receive an User data
        // we received an USER_ID, to load they info, and show
        // Get that USER_ID
        userIdToLoadData = intent.getStringExtra(Constants.EXTRA_USER_ID);

        // Load the USER_DATA in server
        loadUserData(userIdToLoadData);
    }

    //START Private Methods they are not in LinkContract class
    private void bindDataToViews(Usuario usuario) {
        this.usuario = usuario;
        //TODO: remove this comments
        if (usuario.getId().equals(model.getUserId())) {
            view.setButtonEditVisibility(View.VISIBLE);
//            view.setButtonMessageVisibility(View.GONE);
//            view.setButtonCallVisibility(View.GONE);
            view.setButtonEmailVisibility(View.GONE);
//
        } else {
            view.setButtonEditVisibility(View.GONE);
//            view.setButtonMessageVisibility(View.VISIBLE);
//            view.setButtonCallVisibility(View.VISIBLE);
            view.setButtonEmailVisibility(View.VISIBLE);
        }

            view.fillViews(usuario);
    }

    private void loadUserData(String userId) {

        view.showProgress(R.string.a_carregar);
        model.getUser(userId, this);
    }
    //END Private Methods they are not in LinkContract class

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(EXTRA_USER, usuario);
    }

    @Override
    public void editProfileClicked() {

        Bundle b = new Bundle();
        b.putParcelable(EXTRA_USER, usuario);
        view.startEditProfileActivity(b);
    }

    @Override
    public void imgProfileClicked() {

        Bundle b = new Bundle();
        b.putString(Constants.EXTRA_IMAGE_URL, usuario.getUrlPhoto());
        view.showViewImageFragment(b);
    }

    @Override
    public void callButtonClicked() {

        Uri uri = Uri.parse("tel:" + usuario.getPhone());

        Intent iCall = new Intent(Intent.ACTION_CALL);
        iCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iCall.setData(uri);

        view.makeCall(iCall);
    }

    @Override
    public void SendEmailButtonClicked() {

        Intent iEmail = new Intent(Intent.ACTION_SEND);
        iEmail.putExtra(Intent.EXTRA_EMAIL, usuario.getEmail());

        //Need to get just a list of email clients app.
        iEmail.setType("message/rfc822");

        view.startActivityToSendEmail(iEmail);
    }

    @Override
    public void onSendMessageClicked() {

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_FRIEND_CONTACT, usuario.toContact());
        view.startMessageActivity(b);
    }

    @Override
    public void onGetUserSuccess(Usuario usuario) {
        view.dismissProgress();
        view.setButtonsEnabled(true);
        this.bindDataToViews(usuario);
    }

    @Override
    public void onGetUserError(String error) {
        view.dismissProgress();
        view.setButtonsEnabled(false);
        view.showNoInternetDialog(error, this);
    }


    @Override
    public void showNoPostsMessage() {

    }

    // When no internet connection
    // And a dialog retry button was clicked
    // Then re-try to load user data.
    @Override
    public final void onRetry() {

        loadUserData(userIdToLoadData);
    }
}
