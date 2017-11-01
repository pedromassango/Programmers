package com.pedromassango.programmers.mvp.settings.fragment;

import com.pedromassango.programmers.config.Settings;
import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.server.logout.LogoutHadler;

/**
 * Created by Pedro Massango on 25/05/2017.
 */

class Model implements Contract.Model {

    // To logout service
    private LogoutHadler logoutHadler;

    // To store settings data
    private static Settings settings;

    public Model(BaseContract.PresenterImpl presenter) {

        logoutHadler = new LogoutHadler(presenter);
        settings = new Settings(presenter.getContext());
    }

    public String getString(SettingsPreference settingsPreference) {

        return settings.getString(settingsPreference);
    }

    @Override
    public void setString(SettingsPreference settingsPreference, String newValue) {

        settings.setString(settingsPreference, newValue);
    }

    @Override
    public boolean getBoolean(SettingsPreference settingsPreference) {

        return settings.getBoolean(settingsPreference);
    }

    @Override
    public void setBoolean(SettingsPreference settingsPreference, boolean newValue) {

        settings.setBoolean(settingsPreference, newValue);
    }

    /**
     * Method to logout the user
     */
    @Override
    public void logout() {

        logoutHadler.showAlertDialogLogout();
    }
}
