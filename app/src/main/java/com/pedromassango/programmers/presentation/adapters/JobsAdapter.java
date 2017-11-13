package com.pedromassango.programmers.presentation.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.presentation.adapters.holders.JobVH;
import com.pedromassango.programmers.presentation.job.JobContract;
import com.pedromassango.programmers.presentation.job.JobPresenter;
import com.pedromassango.programmers.presentation.job.activity.JobActivity;

/**
 * Created by Pedro Massango on 03/06/2017.
 */

public class JobsAdapter extends FirebaseRecyclerAdapter<Job, JobVH> implements JobContract.ViewAdapter {

    private Activity activity;
    private boolean notified;
    private JobPresenter presenter;
    private IGetDataCompleteListener getDataCompleteListener;

    public JobsAdapter(Activity activity, DatabaseReference jobsRef, IGetDataCompleteListener getDataCompleteListener) {
        super(Job.class, R.layout.row_job, JobVH.class, jobsRef);
        this.activity = activity;
        this.presenter = new JobPresenter(this);
        this.presenter.setContext(activity);
        this.getDataCompleteListener = getDataCompleteListener;
    }

    public JobsAdapter(Activity activity, Query jobsRef, IGetDataCompleteListener getDataCompleteListener) {
        super(Job.class, R.layout.row_job, JobVH.class, jobsRef);
        this.activity = activity;
        this.presenter = new JobPresenter(this);
        this.presenter.setContext(activity);
        this.getDataCompleteListener = getDataCompleteListener;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);

        if (null != getDataCompleteListener && !notified) {
            getDataCompleteListener.onGetDataSuccess();
            notified = true;
        }
    }

    @Override
    protected void populateViewHolder(JobVH viewHolder, final Job model, int position) {

        viewHolder.bindViews(model);
        viewHolder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendCandidatureClicked(model);
            }
        });

        viewHolder.itemView.findViewById(R.id.root_view)
                .setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        presenter.onJobLongClick(model);
                        return false;
                    }
                });
    }

    @Override
    public void startSendCandidatureByEmail(String email) {

        IntentUtils.startSendEmailIntent(activity, email);
    }

    @Override
    public void showToast(int message) {

        Util.showToast(activity, message);
    }

    @Override
    public Context getContext() {

        return activity;
    }

    @Override
    public void showDialogAction(CharSequence[] actions, final Job job) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itemPosition) {

                presenter.onActionChosed(itemPosition, job);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void startEditJobActivity(Bundle b) {

        IntentUtils.startActivity(activity, b, JobActivity.class);
    }
}
