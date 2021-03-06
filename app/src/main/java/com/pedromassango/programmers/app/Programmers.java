package com.pedromassango.programmers.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.Util;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/// Coloque seu email para receber os erros.
@ReportsCrashes(formKey = "", // will not be used
        mailTo = "pedromassango.developer@gmail.com",
        customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text
)
/**
 * Created by Pedro Massango on 03-03-2017 11:02.
 */

public class Programmers extends MultiDexApplication {

    public static Context CONTEXT;

    public static Context getContext(){
        return CONTEXT;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(CONTEXT == null){
            CONTEXT = getApplicationContext();
        }

        //Setting up
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance();

        // First setup
        PrefsHelper.getInstance();

        // Setup Realm database
        Realm.init( this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("programmers-data.realm")
                .schemaVersion(111)
               // .migration(new MyMigration())
                .build();

        // Use the config
        Realm realm = Realm.getInstance(config);

        // Manage the current date to manage wheter we need to fetch from server
        // in our models
        long lastTime = PrefsHelper.getLastTimeOpened();

        PrefsHelper.setShouldFetchFromServer(Util.getTimeState( lastTime));


        // Save the time that the app was opened
        PrefsHelper.saveLastTimeOpened( System.currentTimeMillis());

        //Whill persit the application data on device
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
