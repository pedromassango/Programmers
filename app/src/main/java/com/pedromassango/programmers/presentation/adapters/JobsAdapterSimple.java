package com.pedromassango.programmers.presentation.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.presentation.adapters.holders.JobVH;
import com.pedromassango.programmers.presentation.job.JobContract;
import com.pedromassango.programmers.presentation.job.JobPresenter;
import com.pedromassango.programmers.presentation.job.activity.JobActivity;

import java.util.ArrayList;

/**
 * Created by Pedro Massango on 03/06/2017.
 */

public class JobsAdapterSimple extends RecyclerView.Adapter<JobVH> implements JobContract.ViewAdapter {

    private Activity activity;
    private boolean notified;
    private JobPresenter presenter;
    private ArrayList<Job> jobs;

    public JobsAdapterSimple(Activity activity) {

        this.activity = activity;
        this.presenter = new JobPresenter(this);
        this.presenter.setContext(activity);
        this.jobs = new ArrayList<>();
    }

    @Override
    public JobVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_job, viewGroup, false);
        return (new JobVH(v));
    }

    @Override
    public void onBindViewHolder(JobVH viewHolder, int i) {
        final Job mJob = jobs.get(i);

        viewHolder.bindViews(mJob);
        viewHolder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendCandidatureClicked(mJob);
            }
        });

        viewHolder.itemView.findViewById(R.id.root_view)
                .setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        presenter.onJobLongClick(mJob);
                        return false;
                    }
                });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
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
