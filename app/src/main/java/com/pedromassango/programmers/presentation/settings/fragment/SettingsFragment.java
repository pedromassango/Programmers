package com.pedromassango.programmers.presentation.settings.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.presentation.about.AboutActivity;
import com.pedromassango.programmers.presentation.donate.DonateActivity;
import com.pedromassango.programmers.presentation.policy.PrivacyPolicyDialogFragment;
import com.pedromassango.programmers.presentation.reset.password.ResetPasswordDialogFragment;
import com.pedromassango.programmers.server.logout.LogoutHadler;

import java.util.Stack;

/**
 * Created by Pedro Massango on 25/05/2017.
 */

public class SettingsFragment extends PreferenceFragment implements Contract.View {

    private Contract.Presenter presenter;

    /**
     * Method do get an instance of the Fragment
     * Needed to reuse.
     *
     * @param category_xml the XML file to load in UI
     * @return SettingsFragment
     */
    public static SettingsFragment newInstance(@XmlRes int category_xml) {
        SettingsFragment instance = new SettingsFragment();
        Bundle b = new Bundle();
        b.putInt(Presenter.CATEGORY, category_xml);
        instance.setArguments(b);
        return instance;
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind the view with presenter
        presenter = new Presenter(this);
        presenter.getInitialize(getArguments());

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        //START loop to set listener on Preferences
        // Set "this" to listen for all preference click/change listener for all preferences
        for (int i = 0, size = preferenceScreen.getPreferenceCount(); i < size; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            preference.setOnPreferenceClickListener(this);
            preference.setOnPreferenceChangeListener(this);

            // If this is a preference category, make sure to go through all the subpreferences as well.
            if (preference instanceof PreferenceCategory) {
                Stack<PreferenceCategory> stack = new Stack<>();
                stack.push((PreferenceCategory) preference);

                do {
                    PreferenceCategory category = stack.pop();
                    for (int x = 0, xSize = category.getPreferenceCount(); x < xSize; x++) {
                        Preference subPref = category.getPreference(x);
                        subPref.setOnPreferenceChangeListener(this);
                        subPref.setOnPreferenceClickListener(this);

                        if (subPref instanceof PreferenceCategory) {
                            stack.push((PreferenceCategory) subPref);
                        }
                    }
                }
                while (!stack.isEmpty());
            }
        } //END loop to set listener on Preferences

        //This will update the state of the Settings view
        presenter.handlePreferences();
    }

    @Override
    public void showScreen(int xmlResId) {

        addPreferencesFromResource(xmlResId);
    }

    @Override
    public Preference getPreference(String key) {

        return findPreference(key);
    }

    /**
     * This method will be called when an preference value change
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        return presenter.onPreferenceChange(preference, newValue);
    }

    /**
     * This method will be called when an preference was clicked
     *
     * @param preference the preference info that was clicked
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {

        return presenter.onPreferenceClick(preference);
    }

    /**
     * To replace the current fragment, with new xml to show.
     *
     * @param resid the xml UI to show in the fragment
     */
    @Override
    public void replaceFragment(@XmlRes int resid) {

        Fragment fragment = SettingsFragment.newInstance(resid);
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_layout, fragment, Contract.Presenter.CATEGORY_TAG)
                .commit();
    }

    @Override
    public void logout() {
        new LogoutHadler(getContext())
                .showAlertDialogLogout();
    }

    @Override
    public void startDonateActivity() {

        IntentUtils.startActivity(getActivity(), DonateActivity.class);
    }

    @Override
    public void startActivityToSendMail(String email) {

        IntentUtils.startSendEmailIntent(getActivity(), email);
    }

    @Override
    public void startShareAppIntent() {

        IntentUtils.startShareApp(getActivity());
    }

    @Override
    public void startAboutAppActivity() {

        IntentUtils.startActivity(getActivity(), AboutActivity.class);
    }

    @Override
    public void startPrivacyPoliceActivity() {

        IntentUtils.showFragment(getActivity(), new PrivacyPolicyDialogFragment());
    }

    @Override
    public void showResetPasswordFragment() {

        IntentUtils.showFragment(getActivity(), new ResetPasswordDialogFragment());
    }
}
