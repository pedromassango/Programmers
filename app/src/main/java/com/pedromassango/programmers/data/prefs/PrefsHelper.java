package com.pedromassango.programmers.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.pedromassango.programmers.app.Programmers;
import com.pedromassango.programmers.models.FcmToken;

/**
 * Created by Pedro Massango on 13/06/2017 at 21:52.
 */

public class PrefsHelper {

    // Keys to fetch
    private static final String TOKEN_KEY = "com.pedromassango.programmers.extras.token";

    private static final String PREF_NAME = "com.pedromassango.programmers.extras.programmers";
    private static final String KEY_TIMES_THAT_THE_APP_WAS_OPENED = "com.pedromassango.programmers.extras.programmers";
    private static final String KEY_USERNAME = "com.pedromassango.programmers.extras.usr_name";
    private static final String KEY_USERPHOTO = "com.pedromassango.programmers.extras.usr_photo";
    private static final String KEY_USERID = "com.pedromassango.programmers.extras.usr_id";
    private static final String KEY_FIRST_TIME = "com.pedromassango.programmers.extras.first_time";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private Context context;

    private static PrefsHelper INSTANCE = null;

    public static PrefsHelper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PrefsHelper();
        }
        return INSTANCE;
    }

    private PrefsHelper() {
        this.context = Programmers.getContext();
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void saveToPreferences(String preferenceName, String preferenceValue) {
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(String preferenceName, boolean preferenceValue) {
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void increaseTimesThatTheAppWasOpened() {
        int currentTimes = preferences.getInt(KEY_TIMES_THAT_THE_APP_WAS_OPENED, 0);
        editor.putInt(KEY_TIMES_THAT_THE_APP_WAS_OPENED, (currentTimes + 1));
        editor.apply();
    }

    public static int getTimesThatTheAppWasOpened() {
        return preferences.getInt(KEY_TIMES_THAT_THE_APP_WAS_OPENED, 0);
    }

    public static void saveToPreferences(String preferenceName, int preferenceValue) {
        editor.putInt(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String getName() {
        return preferences.getString(KEY_USERNAME, "Anónimo");
    }

    public static String getPhoto() {
        return preferences.getString(KEY_USERPHOTO, "");
    }

    public static String getId() {
        return preferences.getString(KEY_USERID, "");
    }

    public static void setName(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public static void setId(String userId) {
        editor.putString(KEY_USERID, userId);
        editor.apply();
    }

    public static void saveFCMToken(FcmToken fcmToken) {
        editor.putString(TOKEN_KEY, fcmToken.toString())
                .apply();
    }

    public static FcmToken getFCMToken() {
        String tokenJson = preferences.getString(TOKEN_KEY, null);
        FcmToken fcmToken;

        if (tokenJson != null) {
            fcmToken = new Gson().fromJson(tokenJson, FcmToken.class);
        } else {
            fcmToken = new FcmToken("", false, false);
        }
        Log.e("output", "getFCMToken: " + fcmToken.toString());
        return fcmToken;
    }

    public static void setPhoto(String photo) {
        editor.putString(KEY_USERPHOTO, photo);
        editor.apply();
    }

    public static String readString(String prefenrenceName) {
        return preferences.getString(prefenrenceName, "");
    }

    public static boolean readBoolean(String prefenrenceName) {
        return preferences.getBoolean(prefenrenceName, false);
    }

    public static int readInt(String prefenrenceName) {
        return preferences.getInt(prefenrenceName, 0);
    }

    public static boolean isFirstTime() {
        return preferences.getBoolean(KEY_FIRST_TIME, true);
    }

    public static void isFirstTime(boolean value) {
        preferences.edit().putBoolean(KEY_FIRST_TIME, value).apply();
    }

    public static void endSession() {
        //saveFCMToken(context, new FcmToken("", false, false));
        editor.remove(KEY_USERNAME)
                .remove(KEY_USERPHOTO)
                .remove(KEY_USERID)
                .apply();
    }
}
