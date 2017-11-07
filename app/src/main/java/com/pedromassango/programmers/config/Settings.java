package com.pedromassango.programmers.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pedromassango.programmers.config.SettingsPreference;

/**
 * Created by Pedro Massango on 26/05/2017.
 * <p>
 * This class is responsable to read/write settigs.
 * save/read user preferences
 */

public class Settings {

    // To store settings data
    private static SharedPreferences preferences;

    public Settings(Context context) {

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getInt(SettingsPreference settingsPreference) {

        return preferences.getInt(settingsPreference.getKey(), (Integer) settingsPreference.getDefaultValue());
    }

    public void setInt(SettingsPreference settingsPreference, int newValue){

        preferences.edit().putInt(settingsPreference.getKey(), newValue).apply();
    }

    public String getString(SettingsPreference settingsPreference) {

        return preferences.getString(settingsPreference.getKey(), (String) settingsPreference.getDefaultValue());
    }

    public void setString(SettingsPreference settingsPreference, String newValue) {

        preferences.edit().putString(settingsPreference.getKey(), newValue).apply();
    }

    public boolean getBoolean(SettingsPreference settingsPreference) {

        return preferences.getBoolean(settingsPreference.getKey(), (Boolean) settingsPreference.getDefaultValue());
    }

    public void setBoolean(SettingsPreference settingsPreference, boolean newValue) {

        preferences.edit().putBoolean(settingsPreference.getKey(), newValue).apply();
    }

}
