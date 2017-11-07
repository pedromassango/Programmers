package com.pedromassango.programmers.mvp.job;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.ISendDataListener;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.mvp.base.BaseContract;

/**
 * Created by Pedro Massango on 19/06/2017 at 21:44.
 */

public class JobContract {

    interface Model {
        String getUsername();

        String getUserId();

        void saveJob(Job job, boolean edited, ISendDataListener sendDataListener);

        void deleteJob(Job job, IErrorListener errorListener);
    }


    public interface ViewAdapter {

        Context getContext();

        void startSendCandidatureByEmail(String email);

        void showDialogAction(CharSequence[] actions, Job job);

        void startEditJobActivity(Bundle b);

        void showToast(int message);
    }

    public interface ViewActivity {

        String getEmpregador();

        String getAreaDeTrabalho();

        String getAnosDeExperiencia();

        String getPais();

        String geCidade();

        boolean getRemote();

        void setEmpregadorError(String error);

        void setAreaDeTrabalhoError(String empty_work_zone);

        void setExperienceYearsError(String error);

        String getEmail();

        void setEmailError(String error);

        void setCountryError(String error);

        void setCityError(String error);

        String getCategoria();

        void closeActivity();

        void showProgress(int publishing);

        void dismissProgress();

        void showToast(int message);

        void bindViews(Job job, int cPosition);

        void changeButtonText(int update);
    }


    interface Presenter extends BaseContract.PresenterImpl, ISendDataListener {

        void setContext(Context context);

        void onPublishClicked();

        void initialize(Intent intent, Bundle savedInstanceState);

        void onSaveInstanceState(Bundle outState);

        void onSendCandidatureClicked(Job job);

        void onJobLongClick(Job job);

    void onActionChosed(int position, Job job);
    }
}
