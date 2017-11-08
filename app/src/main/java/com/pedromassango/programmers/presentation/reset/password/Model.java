package com.pedromassango.programmers.presentation.reset.password;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.pedromassango.programmers.server.Library;

/**
 * Created by Pedro Massango on 07-03-2017 10:45.
 */

class Model implements Contract.Model {


    @Override
    public void sendEmailVerification(final Contract.OnSendResultListener listener, final String email) {

        FirebaseAuth auth = Library.getFirebaseAuth();

        auth.sendPasswordResetEmail(email)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onSendError(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        listener.onSendSuccess();
                    }
                });
    }
}
