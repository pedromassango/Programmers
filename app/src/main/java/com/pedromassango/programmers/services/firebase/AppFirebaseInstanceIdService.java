package com.pedromassango.programmers.services.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.models.FcmToken;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 30/05/2017.
 */

//public class AppFirebaseInstanceIdService  {
public class AppFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        showLog("FCM TOKEN: " + refreshedToken);

        // START - save FCM token to localy
        saveTokenStatus(refreshedToken);

        // START - send FCM token to server
        //Worker.sendCurrentUserFCMToken(this);
        // END - send FCM token to server

        super.onTokenRefresh();
    }

    private void saveTokenStatus(String mToken) {

        FcmToken fcmToken = new FcmToken();
        fcmToken.setToken(mToken);
        fcmToken.setSaved(true);    // TRUE, we is saving it now.
        fcmToken.setSent(false);    // FALSE, we not send it on server yet

        // Save the Token on PrefsUtil
        PrefsHelper.saveFCMToken(fcmToken);
    }
}
