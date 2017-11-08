package com.pedromassango.programmers.presentation.donate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.extras.IntentUtils;

public class DonateActivity extends BaseActivity implements Contract.View {

    private Presenter presenter;
    private Button btnPay;

    //Views
    private EditText edtPaymentAmount;

    @Override
    protected int layoutResource() {

        return R.layout.activity_donate;
    }

    @Override
    protected void initializeViews() {

        edtPaymentAmount = findViewById(R.id.edt_payment_amount);

        btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.donatePayPalClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.startPayPalService();
    }

    @Override
    public void setBtnPayEnabled(boolean clicable) {

        btnPay.setEnabled(clicable);
    }

    @Override
    public void clearPaymentAmount() {

        edtPaymentAmount.setText("");
    }

    @Override
    public String getPaymentAmount() {

        return edtPaymentAmount.getText().toString();
    }

    @Override
    public void setPaymentAmountError(String string) {

        edtPaymentAmount.setError(string);
    }

    @Override
    public void startPayPalService(Intent payPalIntentService) {

        IntentUtils.startService(this, payPalIntentService);
    }

    @Override
    public void startActivityWithResult(Intent intent, int requestCode) {

        IntentUtils.startActivityForResult(this, intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        presenter.stopPaypalService();
        super.onDestroy();
    }
}
