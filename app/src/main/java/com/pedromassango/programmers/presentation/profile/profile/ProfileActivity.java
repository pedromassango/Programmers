package com.pedromassango.programmers.presentation.profile.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.interfaces.IDialogRetryListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.adapters.ProfileTabsAdapter;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.presentation.conversations.messages.MessagesActivity;
import com.pedromassango.programmers.presentation.image.ViewImageDIalogFragment;
import com.pedromassango.programmers.presentation.profile.edit.EditProfileActivity;
import com.pedromassango.programmers.ui.dialogs.FailDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements Contract.View {

    // News implementations
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private int mMaxScrollSize;
    private boolean mIsAvatarShown = true;

    TabLayout tabLayout;
    ViewPager viewPager;

    private CircleImageView imgProfile;
    private TextView tvName, tvEmail;
    private ImageButton btnEditProfile;
    private ImageButton btnMakeCall;
    private ImageButton btnSendMessage;
    private ImageButton btnSendEmail;

    // MVP
    private Presenter presenter;

    @Override
    public int layoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    public void initializeViews() {

        tabLayout = findViewById(R.id.materialup_tabs);
        viewPager = findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout = findViewById(R.id.materialup_appbar);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        // END news

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);

        imgProfile = findViewById(R.id.img_user);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.imgProfileClicked();
            }
        });


        btnSendMessage = findViewById(R.id.btn_message);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ProfileActivity.super.showToastMessage(R.string.funtionality_off);

                presenter.onSendMessageClicked();
            }
        });

        btnEditProfile = findViewById(R.id.btn_edit);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.editProfileClicked();
            }
        });

        btnMakeCall = findViewById(R.id.btn_call);
        btnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.callButtonClicked();
            }
        });

        btnSendEmail = findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.SendEmailButtonClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.initialize(getIntent(), savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showProgress(@StringRes int text) {

        super.showProgressDialog(text);
    }

    @Override
    public void dismissProgress() {

        super.dismissProgressDialog();
    }

    @Override
    public void showNoInternetDialog(IDialogRetryListener noInternetListener) {

        new FailDialog(this, getString(R.string.internet_connection_error_description), noInternetListener)
                .show();
    }

    @Override
    public void showViewImageFragment(Bundle b) {

        IntentUtils.showFragment(this, b, new ViewImageDIalogFragment());
    }

    // To animator
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            imgProfile.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            imgProfile.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }


    @Override
    public void setButtonEditVisibility(int visibility) {

        btnEditProfile.setVisibility(visibility);
    }

    @Override
    public void setButtonCallVisibility(int visibility) {

        btnMakeCall.setVisibility(visibility);
    }

    @Override
    public void setButtonEmailVisibility(int visibility) {

        btnSendEmail.setVisibility(visibility);
    }

    @Override
    public void setButtonsEnabled(boolean state) {

        btnSendMessage.setEnabled(state);
        btnMakeCall.setEnabled(state);
        btnSendEmail.setEnabled(state);
        btnEditProfile.setEnabled(state);
    }

    @Override
    public void fillViews(Usuario usuario) {

        tvName.setText(usuario.getUsername());
        tvEmail.setText(usuario.getEmail());

        ImageUtils.loadImageUser(this, usuario.getUrlPhoto(), imgProfile);

        imgProfile.setVisibility(View.VISIBLE);

        viewPager.setAdapter(new ProfileTabsAdapter(getSupportFragmentManager(), usuario));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setButtonMessageVisibility(int visibility) {

        btnSendMessage.setVisibility(visibility);
    }

    @Override
    public void onGetDataSuccess() {

    }

    @Override
    public void onGetError(String error) {

        presenter.showNoPostsMessage();
    }

    @Override
    public void startEditProfileActivity(Bundle b) {

        IntentUtils.startActivity(this, b, EditProfileActivity.class);
        this.finish();
    }

    @Override
    public void startActivityToSendEmail(Intent iEmail) {

        IntentUtils.startActivity(this, Intent.createChooser(iEmail, "Enviar com"));
    }

    @Override
    public void startMessageActivity(Bundle b) {

        IntentUtils.startActivity(this, b, MessagesActivity.class);
    }

    @Override
    public void makeCall(Intent iCall) {

        this.startActivity(iCall);
    }
}
