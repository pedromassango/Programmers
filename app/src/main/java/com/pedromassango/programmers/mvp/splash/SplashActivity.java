package com.pedromassango.programmers.mvp.splash;

import android.app.Activity;
import android.os.Bundle;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.mvp.intro.IntroActivity;
import com.pedromassango.programmers.mvp.login.LoginActivity;
import com.pedromassango.programmers.mvp.main.activity.MainActivity;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.IntentUtils;

public class SplashActivity extends Activity implements Contract.View {

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        presenter = new Presenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void startMainActivity() {

        IntentUtils.startActivity(this, MainActivity.class);
    }

    @Override
    public void startLoginActivity() {

        // For free tests
        if (Constants._DEVELOP_MODE) {
            IntentUtils.startActivity(this, MainActivity.class);
            return;
        }

        IntentUtils.startActivity(this, LoginActivity.class);
    }

    @Override
    public void startIntroActivity() {

        IntentUtils.startActivity(this, IntroActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
