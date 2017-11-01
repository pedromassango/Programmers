package com.pedromassango.programmers.mvp.profile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.mvp.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.mvp.post.adapter.PostsAdapter;
import com.pedromassango.programmers.server.Library;

/**
 * Created by JM on 21/09/2017.
 */

public class UserPostsFragment extends BaseFragmentRecyclerView {

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected RecyclerView.Adapter adapter() {

        String userId = getArguments().getString(Constants.EXTRA_USER_ID);
        return (new PostsAdapter(getActivity(), Library.getUserPostsRef(userId), this));
    }
}
