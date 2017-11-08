package com.pedromassango.programmers.presentation.settings.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.XmlRes;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.extras.IntentUtils;

/**
 * Created by Pedro Massango on 25/05/2017.
 */

class Presenter implements Contract.Presenter {

    private Contract.View view;
    private Model model;

    Presenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this);
    }

    /**
     * Used to instatiate a AppPreferences, to save settings data
     *
     * @return
     * @see Model
     */
    @Override
    public Activity getContext() {
        return view.getContext();
    }

    /**
     * @param arguments - the data passed on {@link SettingsFragment}
     *                  we found the XML passed on Fragment.
     */
    @Override
    public void getInitialize(Bundle arguments) {

        int xml = arguments.getInt(Presenter.CATEGORY, R.xml.settings_main);
        view.showScreen(xml);
    }

    /**
     * This method update views states
     * when SettingsActivity e shown
     */
    @Override
    public void handlePreferences() {

        SwitchPreference vibratePref = (SwitchPreference) view.getPreference(VIBRATE);
        if (vibratePref != null) {
            vibratePref.setChecked(model.getBoolean(SettingsPreference.VIBRATE));
        }

        CheckBoxPreference autoUpdate = (CheckBoxPreference) view.getPreference(CHECK_FOR_UPDATES_AUTOMATICALY);
        if (autoUpdate != null) {
            autoUpdate.setChecked(model.getBoolean(SettingsPreference.CHECK_FOR_UPDATES_AUTOMATICALY));
        }

        SwitchPreference notifyOnNewAppVersion = (SwitchPreference) view.getPreference(NOTIFY_ON_NEW_APP_VERSION);
        if (notifyOnNewAppVersion != null) {
            notifyOnNewAppVersion.setChecked(model.getBoolean(SettingsPreference.NOTIFY_ON_NEW_APP_VERSION));
        }
    }

    /**
     * Hadle save settings, here when an Preference change
     *
     * @param preference the preference tha change
     * @param newValue   the new preference value
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        switch (key) {

            case VIBRATE:
                if (newValue instanceof Boolean) {
                    model.setBoolean(SettingsPreference.VIBRATE, (Boolean) newValue);
                }
                break;

            case NOTIFY_ON_NEW_APP_VERSION:
                if (newValue instanceof Boolean) {
                    model.setBoolean(SettingsPreference.NOTIFY_ON_NEW_APP_VERSION, (Boolean) newValue);
                }
                break;

            case CHECK_FOR_UPDATES_AUTOMATICALY:
                if (newValue instanceof Boolean) {
                    model.setBoolean(SettingsPreference.CHECK_FOR_UPDATES_AUTOMATICALY, (Boolean) newValue);
                }
        }

        return true;
    }

    /**
     * Hadle here Preference Item click
     *
     * @param preference the preference that was clicked
     * @return true if the click was handled
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey() != null ? preference.getKey() : "";

        // Categories click
        @XmlRes int resid = 0;

        switch (key) {

            case CATEGORY_NOTIFICATIONS:
                resid = R.xml.settings_notifications;
                break;

            case CATEGORY_SHARE_APP:
                view.startShareAppIntent();
                break;

            case CATEGORY_BUG_REPPORT:
                view.startBugRepportActivity();
                break;

            case CATEGORY_CHANGE_PASSWORD:
                view.showResetPasswordFragment();
                break;

            case CATEGORY_UPDATES:
                resid = R.xml.settings_updates;
                break;

            case CATEGORY_PRIVACY_POLICE:
                view.startPrivacyPoliceActivity();
                break;

            case CATEGORY_ABOUT:
                resid = R.xml.settings_about;
                break;

            case CATEGORY_QUIT:
                // Logout user
                model.logout();
                break;
        }

        if (resid != 0) {
            view.replaceFragment(resid);
        }

        // Sub- categories click
        switch (key) {
            case FEEDBACK:

                String appEmail = getContext().getString(R.string.app_email);
                view.startActivityToSendMail(appEmail);
                break;

            case ON_FACEBOOK:
                IntentUtils.startAppOnFacebookIntent(getContext());
                break;

            case DONATE:
                view.startDonateActivity();
                break;

            case MORE_ABOUT_APP:
                view.startAboutAppActivity();
                break;
        }

        return false;
    }
}
