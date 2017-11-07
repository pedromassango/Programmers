package com.pedromassango.programmers.mvp.main.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.Query;
import com.pedromassango.programmers.mvp.adapters.users.UsersAdapter;
import com.pedromassango.programmers.mvp.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.TextUtils;

import static com.pedromassango.programmers.extras.Constants.EXTRA_CATEGORY;

/**
 * Created by Pedro Massango on 13/06/2017 at 01:56.
 */

public class UsersFragment extends BaseFragmentRecyclerView {

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected RecyclerView.Adapter adapter() {

        String category = getArguments().getString(EXTRA_CATEGORY);
        Query usersRef = TextUtils.isEmpty(category) ?
                Library.getUsersRef()
                :
                Library.getUsersRef().equalTo("programmingLanguage", category);
        return new UsersAdapter(getActivity(), usersRef, this);
    }
}
