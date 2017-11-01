package com.pedromassango.programmers.config;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.pedromassango.programmers.BuildConfig;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

public class PayPalSettings {

    private static final String PAYPAL_CLIENT_ID = "ARIogH6O17zZlEp0JYN-C4HaKOjNbyqXWFPzlJpX60t_IXVNNB-MIJ3v9oPLrtf-QCM_jA21zzGpWvH8";
    private static final String PAYPAL_ENVIRONMENT =
            BuildConfig.DEBUG ?
                    PayPalConfiguration.ENVIRONMENT_NO_NETWORK
                    :
                    PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    //Paypal Configuration Object
    public static PayPalConfiguration PAYPAL_CONFIG = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            //.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .environment(PAYPAL_ENVIRONMENT)
            .clientId(PayPalSettings.PAYPAL_CLIENT_ID);
}
