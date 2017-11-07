package com.pedromassango.programmers.mvp.donate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.PayPalSettings;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.interfaces.IDialogInfoOkClickedListener;
import com.pedromassango.programmers.models.Payment;
import com.pedromassango.programmers.ui.dialogs.InfoDialogCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

class Presenter implements Contract.Presenter {

    //PAYPAL request code, to track on ActivityREsult
    private static final int PAYPAL_REQUEST_CODE = 101;

    private Contract.View view;
    private Model model;

    Presenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this);
    }

    @Override
    public Context getContext() {

        return (Context) view;
    }

    @Override
    public void startPayPalService() {

        Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalSettings.PAYPAL_CONFIG);

        view.startPayPalService(intent);
    }

    @Override
    public void stopPaypalService() {

        IntentUtils.stopService(getContext(), new Intent(getContext(), PayPalService.class));
    }

    @Override
    public void donatePayPalClicked() {
        //Getting the amount from editText
        String paymentAmount = view.getPaymentAmount();

        //Check if it is empty
        if (paymentAmount.isEmpty()) {
            view.setPaymentAmountError(getContext().getString(R.string.empty_payment_amount));
            return;
        }

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount))
                , "USD", getContext().getString(R.string.paiment_app_detail),
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(getContext(), PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalSettings.PAYPAL_CONFIG);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        view.startActivityWithResult(intent, PAYPAL_REQUEST_CODE);
        view.setBtnPayEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                //If the result is OK i.e. user has not canceled the payment
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        JSONObject paypalDetailsJson = confirm.toJSONObject();
                        JSONObject paypalResponseJson = paypalDetailsJson.getJSONObject("response");

                        Payment payment = new Payment();
                        payment.setPaymentId(paypalResponseJson.getString("id"));
                        payment.setAmount(view.getPaymentAmount());
                        payment.setTimestamp(System.currentTimeMillis());

                        //Sending payment info on Firebase
                        //Registering an payment on server
                        model.savePaymentInfo(payment);

                        showLog("PAYPAL: SUCCESS");
                        showLog("PAYPAL: state -> " + paypalResponseJson.getString("state"));
                        showLog("PAYPAL: id -> " + paypalResponseJson.getString("id"));

                        //Show dialog <Payment Success>
                        new InfoDialogCustom()
                                .create(getContext(), getContext().getString(R.string.payment_success_title_info),
                                        getContext().getString(R.string.payment_success_description_info))
                                .setOnOkClickedListener(new IDialogInfoOkClickedListener() {
                                    @Override
                                    public void onOkClick() {

                                        view.clearPaymentAmount();
                                        view.setBtnPayEnabled(true);
                                    }
                                })
                                .show(600);

                    } catch (JSONException e) {
                        showLog("an extremely unlikely failure occurred: " + e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showLog("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                showLog("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
}
