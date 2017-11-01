package com.pedromassango.programmers.mvp.donate;

import android.content.Context;
import android.content.Intent;

import com.pedromassango.programmers.models.Payment;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

public class Contract {

    interface Model {

        void savePaymentInfo(Payment payment);
    }

    interface View {

        void startPayPalService(Intent PayPalIntentService);

        String getPaymentAmount();

        void setPaymentAmountError(String string);

        void startActivityWithResult(Intent intent, int requestCode);

        void setBtnPayEnabled(boolean clicacle);

        void clearPaymentAmount();
    }

    interface Presenter {

        Context getContext();

        void donatePayPalClicked();

        void startPayPalService();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void stopPaypalService();
    }
}
