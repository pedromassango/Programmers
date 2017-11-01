package com.pedromassango.programmers.mvp.settings.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.XmlRes;

import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.mvp.base.BaseContract;

/**
 * Created by Pedro Massango on 25/05/2017.
 */

class Contract {

    interface Model {

        void logout();

        String getString(SettingsPreference settingsPreference);

        void setString(SettingsPreference settingsPreference, String newValue);

        boolean getBoolean(SettingsPreference settingsPreference);

        void setBoolean(SettingsPreference settingsPreference, boolean newValue);
    }

    interface View extends Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        void replaceFragment(@XmlRes int resid);

        Activity getContext();

        Preference getPreference(String key);

        void startDonateActivity();

        void startActivityToSendMail(String email);

        void showScreen(int xml);

        void startAboutAppActivity();

        void startBugRepportActivity();

        void startPrivacyPoliceActivity();

        void startShareAppIntent();

        void showResetPasswordFragment();
    }

    interface Presenter extends BaseContract.PresenterImpl{

        String TAG = "SettingsFragment";
        String CATEGORY = "category";

        // Categories
        String CATEGORY_NOTIFICATIONS = "pref_key_category_notifications";
        String CATEGORY_BUG_REPPORT = "pref_key_repport_bug";
        String CATEGORY_UPDATES = "pref_key_category_app_updates";
        String CATEGORY_PRIVACY_POLICE = "pref_key_category_privacy_police";
        String CATEGORY_CHANGE_PASSWORD = "pref_key_reset_password";
        String CATEGORY_ABOUT = "pref_key_category_about";
        String CATEGORY_SHARE_APP = "pref_key_share_app";
        String CATEGORY_QUIT = "pref_key_category_quit";

        // Sub-categories
        String VIBRATE = "pref_key_vibrate";
        String NOTIFY_ON_NEW_APP_VERSION = "notify_on_new_app_version_message";
        String CHECK_FOR_UPDATES_AUTOMATICALY = "pref_key_check_for_updates_automaticaly";
        String ON_FACEBOOK = "pref_key_page_on_facebook";
        String FEEDBACK = "pref_key_feedback";
        String DONATE = "pref_key_donate";
        String MORE_ABOUT_APP = "pref_key_more_about_app";

        String CATEGORY_TAG = "settings_category_fragment_tag";

        // For logout service
        Context getContext();

        // Actions on UI
        boolean onPreferenceClick(Preference preference);

        boolean onPreferenceChange(Preference preference, Object newValue);

        void handlePreferences();

        void getInitialize(Bundle arguments);
    }
}
