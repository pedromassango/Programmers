package com.pedromassango.programmers.app;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Pedro Massango on 03-03-2017 11:02.
 */

public class Programmers extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Setting up
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance();

        //Whill persit the application data on device
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
