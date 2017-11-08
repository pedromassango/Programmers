package com.pedromassango.programmers.presentation.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Job;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 03/06/2017.
 */

public class JobVH extends RecyclerView.ViewHolder {

    public TextView tvWorkType;
    public TextView tvCategory;
    public TextView tvAuthor;
    public TextView tvEmployer;
    public TextView tvRemote;
    public TextView tvCountry;
    public TextView tvCity;
    public TextView tvYearsOfExperience;
    public TextView tvDate;
    public Button btnSend;

    public JobVH(View view) {
        super(view);
        tvWorkType = view.findViewById(R.id.tv_funcional_area);
        tvCategory = view.findViewById(R.id.tv_category);
        tvAuthor = view.findViewById(R.id.tv_author);
        tvEmployer = view.findViewById(R.id.tv_empregador_name);
        tvYearsOfExperience = view.findViewById(R.id.tv_needed_experience);
        tvRemote = view.findViewById(R.id.tv_remote);
        tvCountry = view.findViewById(R.id.tv_country);
        tvCity = view.findViewById(R.id.tv_city);
        tvDate = view.findViewById(R.id.tv_date);
        btnSend = view.findViewById(R.id.btn_send_curriculum);
    }

    public void bindViews(Job job){

        tvWorkType.setText( job.getWorkType());
        tvCategory.setText( job.getCategory());
        tvAuthor.setText(job.getAuthor());
        tvEmployer.setText(job.getEmployer());
        tvCountry.setText(job.getCountry());
        tvCity.setText(job.getCity());
        tvYearsOfExperience.setText(job.getYearsOfExperience());
        tvDate.setText(Util.getTimeAgo( job.getTimestamp()));

        String remote = job.isRemote() ?
                itemView.getContext().getString(R.string.str_yes)
                :
                itemView.getContext().getString(R.string.str_no);
        tvRemote.setText(remote);
    }
}
