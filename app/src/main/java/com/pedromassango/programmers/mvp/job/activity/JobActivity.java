package com.pedromassango.programmers.mvp.job.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.mvp.base.activity.BaseActivity;
import com.pedromassango.programmers.mvp.job.JobContract;
import com.pedromassango.programmers.mvp.job.JobPresenter;

public class JobActivity extends BaseActivity implements JobContract.ViewActivity {

    //MVP
    private JobPresenter presenter;

    // Views
    private TextInputLayout tiEpregador, tiWorkZone, tiExperience, tiEmail, tiCountry, tiCity;
    private CheckBox cbRemote;
    private Spinner spCategory;
    private Button btnPublish;

    @Override
    protected int layoutResource() {

        return R.layout.activity_job;
    }

    @Override
    protected void initializeViews() {

        tiEpregador = (TextInputLayout) findViewById(R.id.input_empregador);
        tiWorkZone = (TextInputLayout) findViewById(R.id.input_functional_area);
        tiExperience = (TextInputLayout) findViewById(R.id.input_experience_years);
        tiCountry = (TextInputLayout) findViewById(R.id.input_country);
        tiEmail = (TextInputLayout) findViewById(R.id.input_email);
        tiCity = (TextInputLayout) findViewById(R.id.input_city);

        spCategory = (Spinner) findViewById(R.id.spinner_language);

        cbRemote = (CheckBox) findViewById(R.id.cb_remote);

        btnPublish = (Button) findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onPublishClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new JobPresenter(this);
        presenter.setContext(this);
        presenter.initialize(getIntent(), savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void changeButtonText(int update) {

        btnPublish.setText(update);
    }

    @Override
    public void bindViews(Job job, int cPosition) {

        tiWorkZone.getEditText().setText(job.getWorkType());
        spCategory.setSelection(cPosition);
        tiEpregador.getEditText().setText(job.getEmployer());
        tiCountry.getEditText().setText(job.getCountry());
        tiEmail.getEditText().setText(job.getEmail());
        tiCity.getEditText().setText(job.getCity());
        tiExperience.getEditText().setText(job.getYearsOfExperience());
        cbRemote.setChecked(job.isRemote());
    }

    @Override
    public String getCategoria() {

        return spCategory.getSelectedItem().toString();
    }

    @Override
    public String getEmpregador() {
        return tiEpregador.getEditText().getText().toString();
    }

    @Override
    public String getAreaDeTrabalho() {
        return tiWorkZone.getEditText().getText().toString();
    }

    @Override
    public String getAnosDeExperiencia() {
        return tiExperience.getEditText().getText().toString();
    }

    @Override
    public String getPais() {
        return tiCountry.getEditText().getText().toString();
    }

    @Override
    public String geCidade() {
        return tiCity.getEditText().getText().toString();
    }

    @Override
    public boolean getRemote() {
        return cbRemote.isChecked();
    }

    @Override
    public void setEmpregadorError(String error) {

        tiEpregador.setError(error);
        tiEpregador.requestFocus();
    }

    @Override
    public void setAreaDeTrabalhoError(String empty_work_zone) {

        tiWorkZone.setError(empty_work_zone);
        tiWorkZone.requestFocus();
    }

    @Override
    public void setExperienceYearsError(String error) {

        tiExperience.setError(error);
        tiExperience.requestFocus();
    }

    @Override
    public String getEmail() {
        return tiEmail.getEditText().getText().toString();
    }

    @Override
    public void setEmailError(String error) {

        tiEmail.setError(error);
        tiEmail.requestFocus();
    }

    @Override
    public void setCountryError(String error) {

        tiCountry.setError(error);
        tiCountry.requestFocus();
    }

    @Override
    public void setCityError(String error) {

        tiCity.setError(error);
        tiCity.requestFocus();
    }

    @Override
    public void closeActivity() {

        this.finish();
    }

    @Override
    public void showProgress(int publishing) {

        super.showProgressDialog(publishing);
    }

    @Override
    public void dismissProgress() {

        super.dismissProgressDialog();
    }

    @Override
    public void showToast(int message) {

        super.showToastMessage(message);
    }
}
