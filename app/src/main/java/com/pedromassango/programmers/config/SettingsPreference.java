package com.pedromassango.programmers.config;

import com.pedromassango.programmers.BuildConfig;

/**
 * Created by Pedro Massango on 26/05/2017.
 * <p>
 * This class save the default app settings
 */

public enum SettingsPreference {

    // for Version Controll
    VERSION_KEY("version", BuildConfig.VERSION_CODE),
    VIBRATE("pref_key_vibrate", true),
    NOTIFY_ON_NEW_APP_VERSION("notify_on_new_app_version_message", true),
    CHECK_FOR_UPDATES_AUTOMATICALY("pref_key_check_for_updates_automaticaly", true);

    private String key;
    private Object defaultValue;

    SettingsPreference(String key) {
        this.key = key;
    }

    SettingsPreference(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}