package com.pedromassango.programmers.app;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Pedro Massango on 03-03-2017 11:02.
 */

public class Programmers extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //Setting up
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance();

        // Setup Realm database
        Realm.init( this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("appname_database.realm")
                .schemaVersion(111)
               // .migration(new MyMigration())
                .build();

        // Use the config
        Realm realm = Realm.getInstance(config);

        //Whill persit the application data on device
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
