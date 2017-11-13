package com.pedromassango.programmers.presentation.job;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.data.prefs.PrefsUtil;
import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.ISendDataListener;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.server.Library;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 19/06/2017 at 16:10.
 */

public class JobModel implements JobContract.Model {

    private BaseContract.PresenterImpl presenter;

    public JobModel(BaseContract.PresenterImpl presenter){

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
    public void saveJob(Job job, boolean edited, final ISendDataListener sendDataListener) {

        // Generate a new id if is not edited
        DatabaseReference jobsRef = Library.getJobsRef();
        String jobId = edited ? job.getId() : jobsRef.push().getKey();
        job.setId(jobId);

        Map<String, Object> jobValue = job.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(AppRules.getAllJobsRef(jobId), jobValue);
        childUpdates.put(AppRules.getJobsCategoryRef(job.getCategory(), jobId), jobValue);
        childUpdates.put(AppRules.getJobsUserRef(job.getAuthorId(), jobId), jobValue);

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        sendDataListener.onSendError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        sendDataListener.onSendSuccess();
                    }
                });

    }

    @Override
    public void deleteJob(Job job, final IErrorListener errorListener) {
        String jobId = job.getId();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(AppRules.getAllJobsRef(jobId), null);
        childUpdates.put(AppRules.getJobsCategoryRef(job.getCategory(), jobId), null);
        childUpdates.put(AppRules.getJobsUserRef(job.getAuthorId(), jobId), null);

        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        errorListener.onError();
                    }
                });
    }
}
