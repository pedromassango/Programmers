package com.pedromassango.programmers.presentation.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.Settings;
import com.pedromassango.programmers.config.SettingsPreference;
import com.pedromassango.programmers.presentation.login.LoginActivity;
import com.pedromassango.programmers.services.GoogleServices;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.PrefsUtil;

public class IntroActivity extends AppIntro {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(ProgrammersSlide.newInstance(R.layout.app_intro3));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_one_title),
                getString(R.string.intro_one_description),
                R.drawable.intro1, Color.parseColor("#f43640")));

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_two_title),
                getString(R.string.intro_two_description),
                0, Color.parseColor("#0594c4")));

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_tree_title),
                getString(R.string.intro_tree_description),
                0, Color.parseColor("#0594c4")));

         addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_four_title),
                getString(R.string.intro_four_description),
                R.drawable.intro3, Color.parseColor("#f43640")));


        // This will check if the device have
        // the apropriated Play Service version
        // If not, the app will be closed
        GoogleServices googleServices = new GoogleServices(this);
        if (!googleServices.isGooglePlayServicesAvailable()) {
            return;
        }

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Enable fade animation
        setFadeAnimation();

        // Turn vibration on and set intensity.
        boolean vibrateEnabled = new Settings(this).getBoolean(SettingsPreference.VIBRATE);
        setVibrate(vibrateEnabled);
        setVibrateIntensity(35);


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        // Save the show splash state. that, the splash activity
        // will read it, to switch witch activity will open
        // betwen IntroActivity OR LoginActivity
        PrefsUtil.isFirstTime(this, false);

        // Do something when users tap on Done button.
        IntentUtils.startActivity(this, LoginActivity.class);
        finish();
    }
}
