package com.pedromassango.programmers.services;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Process;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pedromassango.programmers.extras.Util;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by JANU on 15/05/2017.
 */

public class GoogleServices implements DialogInterface.OnCancelListener {

    private static final int PLAY_SERVICE_RESOLUTION_REQUEST = 189;
    private Activity context;

    public GoogleServices(Activity activity) {
        this.context = activity;
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int code = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (code != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(code)) {
                googleApiAvailability.getErrorDialog(context, code, PLAY_SERVICE_RESOLUTION_REQUEST, this);
            } else {
                showLog("THIS DEVICE IS NOT SUPPORTED");
                Util.killAppProccess();
            }
            showLog("isGooglePlayServicesAvailable - " + "false");
            return false;
        }
        showLog("isGooglePlayServicesAvailable - " + "true");
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        showLog("THIS DEVICE IS NOT SUPPORTED");

        //context.finish();
        Util.killAppProccess();
    }

}
