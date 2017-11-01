package com.pedromassango.programmers.mvp.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.Settings;
import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.mvp.main.activity.MainActivity;
import com.pedromassango.programmers.server.Library;

import static com.pedromassango.programmers.extras.Util.showLog;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected Vibrator vibrator;
    private ProgressDialog progressDialog;
    private static DatabaseReference amOnline;
    private ValueEventListener onlineStatusListener;
    private Contact contact;

    protected abstract
    @LayoutRes
    int layoutResource();

    protected abstract void initializeViews();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResource());
        this.setupToolbar();
        this.initializeViews();
    }

    private void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Handle the user presence
    public final void makeMeOnline() {
        if (Constants._DEVELOP_MODE)
            return;

        if (contact == null) {
            showLog("BaseActivity - contact is null.");
            contact = new Contact();
            contact.setOnline(true);
            contact.setUserId(PrefsUtil.getId(this));
            contact.setUsername(PrefsUtil.getName(this));
            contact.setUserUrlPhoto(PrefsUtil.getPhoto(this));
        }

        final DatabaseReference userPresence = Library.getAllUsersChatsRef().child(contact.getUserId());
        final DatabaseReference lastOnlineRef = userPresence.child("lastOnline");
        final DatabaseReference onlineRef = userPresence.child("online");

        amOnline = FirebaseDatabase.getInstance().getReference(".info/connected");
        onlineStatusListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // If user is connected, set online
                if (dataSnapshot.getValue(Boolean.class)) {
                    showLog("handleUserPresence - onDataChange - use is connected");
                    showLog("handleUserPresence - onDataChange - setting online...");
                    userPresence.setValue(contact);
                    // When the user will be disconnected it will set the user offline
                    lastOnlineRef.onDisconnect().setValue(System.currentTimeMillis());
                    onlineRef.onDisconnect().setValue(Boolean.FALSE);
                    showLog("handleUserPresence - onDataChange - setting online - done.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                showLog("Presence Listener - user offline");
            }
        };

        amOnline.addValueEventListener(onlineStatusListener);
    }

    protected final void showProgressDialog(@StringRes int message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.onAttachedToWindow();
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(getString(message));
        progressDialog.show();
    }

    protected final void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.onAttachedToWindow();
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected final void showToastMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    protected final void showToastMessage(@StringRes int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    protected final void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.onDetachedFromWindow();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (this instanceof MainActivity) {
                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    this.onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
            } else {
                this.finish();
                //Util.killAppProccess();
            }
        } else {
            super.onBackPressed();
            //this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }
    }

    public final void vibratePhone(int intensity) {
        boolean vibrateEnabled = new Settings(this).getBoolean(SettingsPreference.VIBRATE);
        if (vibrateEnabled) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(intensity);
        }
    }
}
