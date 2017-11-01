package com.pedromassango.programmers.mvp.settings.activity;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.mvp.base.activity.BaseActivityFragmented;
import com.pedromassango.programmers.mvp.settings.fragment.SettingsFragment;

/**
 * A {@link SettingsActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends BaseActivityFragmented {

    private static final String TAG = "SettingsActivity";
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate: ");

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        settingsFragment = (SettingsFragment) fragmentManager.findFragmentByTag(TAG);

        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance(R.xml.settings_main);

            replaceFragment(settingsFragment);
        } else {
            showFragment(settingsFragment);
        }
    }
}
