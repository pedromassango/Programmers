package com.pedromassango.programmers.mvp.repport;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.models.Bug;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 05-03-2017 20:07.
 */

class Model implements Contract.Model {

    private Presenter presenter;

    Model(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getUsername() {

        return PrefsUtil.getName(presenter.getContext());
    }

    @Override
    public String getUserId() {

        return PrefsUtil.getId(presenter.getContext());
    }

    @Override
    public void sendBug(final Contract.OnSendBugListener listener, final Bug bug) {

        DatabaseReference bugsDb = Library.getBugsref();
        String bugId = bugsDb.push().getKey();
        DatabaseReference bugRef = bugsDb.child(bugId).getRef();

        bug.setId(bugId);
        bugRef.setValue(bug)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onError(Util.getError(e));
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        listener.onSuccess();
                    }
                });
    }
}
