package com.pedromassango.programmers.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.pedromassango.programmers.models.FcmToken;

/**
 * Created by Pedro Massango on 13/06/2017 at 21:52.
 */

public class PrefsUtil {

    // Keys to fetch
    private static final String TOKEN_KEY = "com.pedromassango.programmers.extras.token";

    private static final String PREF_NAME = "com.pedromassango.programmers.extras.programmers";
    private static final String KEY_TIMES_THAT_THE_APP_WAS_OPENED = "com.pedromassango.programmers.extras.programmers";
    private static final String KEY_USERNAME = "com.pedromassango.programmers.extras.usr_name";
    private static final String KEY_USERPHOTO = "com.pedromassango.programmers.extras.usr_photo";
    private static final String KEY_USERID = "com.pedromassango.programmers.extras.usr_id";
    private static final String KEY_FIRST_TIME = "com.pedromassango.programmers.extras.first_time";

    private static SharedPreferences preferences;

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void increaseTimesThatTheAppWasOpened(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int currentTimes = preferences.getInt(KEY_TIMES_THAT_THE_APP_WAS_OPENED, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_TIMES_THAT_THE_APP_WAS_OPENED, (currentTimes + 1));
        editor.apply();
    }

    public static int getTimesThatTheAppWasOpened(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_TIMES_THAT_THE_APP_WAS_OPENED, 0);
    }

    public static void saveToPreferences(Context context, String preferenceName, int preferenceValue) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String getName(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USERNAME, "Anónimo");
    }

    public static String getPhoto(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USERPHOTO, "");
    }

    public static String getId(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USERID, "");
    }

    public static void setName(Context context, String username) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public static void setId(Context context, String userId) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERID, userId);
        editor.apply();
    }

    public static void saveFCMToken(Context context, FcmToken fcmToken) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit()
                .putString(TOKEN_KEY, fcmToken.toString())
                .apply();
    }

    public static FcmToken getFCMToken(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

    public static void setPhoto(Context context, String photo) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERPHOTO, photo);
        editor.apply();
    }

    public static String readString(Context context, String prefenrenceName) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(prefenrenceName, "");
    }

    public static boolean readBoolean(Context context, String prefenrenceName) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(prefenrenceName, false);
    }

    public static int readInt(Context context, String prefenrenceName) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(prefenrenceName, 0);
    }

    public static boolean isFirstTime(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_TIME, true);
    }

    public static void isFirstTime(Context context, boolean value) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_FIRST_TIME, value).apply();
    }

    public static void endSession(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //saveFCMToken(context, new FcmToken("", false, false));
        editor.remove(KEY_USERNAME)
                .remove(KEY_USERPHOTO)
                .remove(KEY_USERID)
                .apply();
    }
}
