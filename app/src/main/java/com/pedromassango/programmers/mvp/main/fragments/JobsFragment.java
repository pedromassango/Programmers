package com.pedromassango.programmers.mvp.main.fragments;

import android.os.Bundle;

import com.google.firebase.database.Query;
import com.pedromassango.programmers.mvp.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.mvp.job.adapter.JobsAdapter;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.TextUtils;

/**
 * Created by Pedro Massango on 14/06/2017 at 03:15.
 */

public class JobsFragment extends BaseFragmentRecyclerView {
    @Override
    protected void setup(Bundle bundle) {

    }


    @Override
    protected JobsAdapter adapter() {
        String category = getArguments().getString(Constants.EXTRA_CATEGORY, "");
        Query jobsRef = TextUtils.isEmpty(category) ?
                Library.getJobsRef()
                :
                Library.getJobsByCategoryRef(category);
        return new JobsAdapter(getActivity(), jobsRef, this);
    }

}
