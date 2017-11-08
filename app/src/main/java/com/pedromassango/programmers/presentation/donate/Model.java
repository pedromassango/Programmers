package com.pedromassango.programmers.presentation.donate;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.models.Payment;
import com.pedromassango.programmers.server.Library;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

class Model implements Contract.Model, OnFailureListener, OnSuccessListener<Void> {

    private String loggedUsername,
            loggedUserId;

    Model(Presenter presenter) {

        this.loggedUsername = PrefsUtil.getName(presenter.getContext());
        this.loggedUserId = PrefsUtil.getId(presenter.getContext());
    }

    @Override
    public void savePaymentInfo(Payment payment) {

        //setting the author info
        payment.setAuthor(loggedUsername);
        payment.setAuthorId(loggedUserId);

        DatabaseReference donationsRef = Library.getPaymentsRef();

        //setting an firebase id, on Payment object
        String firebasePaymentId = donationsRef.push().getKey();
        payment.setId(firebasePaymentId);

        //Sending the payment info on server
        donationsRef
                .child(payment.getId())
                .setValue(payment)
                .addOnFailureListener(this)
                .addOnSuccessListener(this);

    }

    //Failed to send Payment info on server
    @Override
    public void onFailure(@NonNull Exception e) {

        showLog("SEND PAYMENT INFO FAILED: " + e.getMessage());
    }

    //Success, payment info saved
    @Override
    public void onSuccess(Void aVoid) {

        showLog("SEND PAYMENT INFO SUCCESS");
    }
}
