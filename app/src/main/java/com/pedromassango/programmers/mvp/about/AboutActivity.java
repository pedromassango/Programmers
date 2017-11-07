package com.pedromassango.programmers.mvp.about;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.mvp.base.activity.BaseActivity;
import com.pedromassango.programmers.mvp.donate.DonateActivity;
import com.pedromassango.programmers.mvp.policy.PrivacyPolicyDialogFragment;
import com.pedromassango.programmers.extras.IntentUtils;

public class AboutActivity extends BaseActivity implements Contract.View {

    private Presenter presenter;

    @Override
    protected int layoutResource() {

        return R.layout.activity_about;
    }

    @Override
    protected void initializeViews() {

        Button btnDonate = findViewById(R.id.btn_donate);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.donateClicked();
            }
        });

        findViewById(R.id.link_terms_and_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.termsAndConditionsClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
    }

    @Override
    public void startDonateActivity() {

        IntentUtils.startActivity(this, DonateActivity.class);
    }

    @Override
    public void showTermsAndConditions() {

        IntentUtils.showFragment(this, new PrivacyPolicyDialogFragment());
    }
}
