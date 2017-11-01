package com.pedromassango.programmers.mvp.job;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.TextUtils;

/**
 * Created by Pedro Massango on 19/06/2017 at 21:44.
 */

public class JobPresenter implements JobContract.Presenter, IErrorListener {

    private JobContract.ViewActivity viewActivity;
    private JobContract.ViewAdapter viewAdapter;
    private JobModel model;
    private Job job;
    private Context context;
    private boolean edited = false;

    public JobPresenter(JobContract.ViewActivity viewActivity) {
        this.viewActivity = viewActivity;
        this.model = new JobModel(this);
    }

    public JobPresenter(JobContract.ViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
        this.model = new JobModel(this);
    }


    @Override
    public Activity getContext() {

        return ((Activity) context);
    }

    @Override
    public void setContext(Context context) {

        this.context = context;
    }

    @Override
    public void initialize(Intent intent, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            job = savedInstanceState.getParcelable(Constants.EXTRA_JOB);
            viewActivity.bindViews(job, CategoriesUtils.getCPosition(getContext(), job.getCategory()));
            return;
        }

        if (intent != null && intent.hasExtra(Constants.EXTRA_JOB)) {
            edited = true;
            job = intent.getParcelableExtra(Constants.EXTRA_JOB);
            viewActivity.bindViews(job, CategoriesUtils.getCPosition(getContext(), job.getCategory()));
            viewActivity.changeButtonText(R.string.update);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.EXTRA_JOB, job);
    }



    @Override
    public void onPublishClicked() {
        String categoria = viewActivity.getCategoria();
        String empregador = viewActivity.getEmpregador();
        String areaDeTrabalho = viewActivity.getAreaDeTrabalho();
        String anosDeExperiencia = viewActivity.getAnosDeExperiencia();
        String email = viewActivity.getEmail();
        boolean remote = viewActivity.getRemote();
        String pais = viewActivity.getPais();
        String cidade = viewActivity.geCidade();

        if (TextUtils.isEmpty(empregador)) {
            viewActivity.setEmpregadorError(getContext().getString(R.string.empty_empregador));
            return;
        }
        viewActivity.setEmpregadorError(null);

        if (TextUtils.lessOrEqualTwoDigits(empregador)) {
            viewActivity.setEmpregadorError(getContext().getString(R.string.name_too_short));
            return;
        }
        viewActivity.setEmpregadorError(null);

        if (TextUtils.isEmpty(areaDeTrabalho)) {
            viewActivity.setAreaDeTrabalhoError(getContext().getString(R.string.empty_work_zone));
            return;
        }
        viewActivity.setAreaDeTrabalhoError(null);

        if (anosDeExperiencia.trim().length() > 1 || Integer.valueOf(anosDeExperiencia) > 8) {
            viewActivity.setExperienceYearsError(getContext().getString(R.string.year_must_be_less_than_eight));
            viewActivity.showToast(R.string.year_must_be_less_than_eight);
            return;
        }
        viewActivity.setExperienceYearsError(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            viewActivity.setEmailError(getContext().getString(R.string.email_invalido));
            return;
        }
        viewActivity.setEmailError(null);

        if (TextUtils.isEmpty(pais)) {
            viewActivity.setCountryError(getContext().getString(R.string.empty_country));
            return;
        }
        viewActivity.setCountryError(null);

        if (TextUtils.isEmpty(cidade)) {
            viewActivity.setCityError(getContext().getString(R.string.empty_city));
            return;
        }
        viewActivity.setCityError(null);

        Job job = new Job();
        job.setAuthor(model.getUsername());
        job.setAuthorId(model.getUserId());
        job.setCategory(categoria);
        job.setCity(cidade);
        job.setCountry(pais);
        job.setEmail(email);
        job.setEmployer(empregador);
        job.setRemote(remote);
        job.setWorkType(areaDeTrabalho);
        job.setYearsOfExperience(anosDeExperiencia);
        job.setTimestamp(System.currentTimeMillis());

        viewActivity.showProgress(R.string.publishing);
        model.saveJob(job, edited, this);
    }

    @Override
    public void onSendSuccess() {

        viewActivity.dismissProgress();
        viewActivity.showToast(R.string.done);
        viewActivity.closeActivity();
    }

    @Override
    public void onSendError() {
        viewActivity.dismissProgress();
        viewActivity.showToast(R.string.something_was_wrong);
    }

    // Adapter

    @Override
    public void onSendCandidatureClicked(Job job) {

        // If the click is of the author, ignore the click
        if (job.getAuthorId().equals(model.getUserId())) {
            return;
        }

        String jobEmail = job.getEmail();
        viewAdapter.startSendCandidatureByEmail(jobEmail);
    }

    @Override
    public void onJobLongClick(Job job) {
        CharSequence[] actions;

        // If was the author that click on it
        // they options is, Edit or Delete this job
        if (job.getAuthorId().equals(model.getUserId())) {
            actions = new CharSequence[2];
            actions[0] = viewAdapter.getContext().getString(R.string.action_edit);
            actions[1] = viewAdapter.getContext().getString(R.string.action_delete);

            viewAdapter.showDialogAction(actions, job);
        }
    }

    @Override
    public void onActionChosed(int position, Job job) {
        switch (position) {
            case 0: //Edit
                Bundle b = new Bundle();
                b.putParcelable(Constants.EXTRA_JOB, job);
                viewAdapter.startEditJobActivity(b);
                break;

            case 1: //Delete
                viewAdapter.showToast(R.string.deleting);
                model.deleteJob(job, this);
                break;
        }
    }

    @Override
    public void onError() {
        if (viewAdapter != null) {
            viewAdapter.showToast(R.string.something_was_wrong);
        }
    }
}
