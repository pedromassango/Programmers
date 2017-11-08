package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.presentation.post.adapter.PostsAdapter;
import com.pedromassango.programmers.presentation.post.adapter.PostsTests;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.TextUtils;
import com.pedromassango.programmers.extras.Util;

import static com.pedromassango.programmers.extras.Constants.EXTRA_CATEGORY;

/**
 * Created by Pedro Massango on 13/06/2017 at 01:05.
 */

public class PostsFragment extends BaseFragmentRecyclerView {

    private static final String TAG = "PostsFragment";

    @Override
    protected void setup(Bundle bundle) {
        Log.v(TAG, "setup");
    }

    @Override
    protected RecyclerView.Adapter adapter() {

        if (Constants._DEVELOP_MODE) {
            return new PostsTests(getActivity(), Util.getPosts(), this);
        }

        String category = getArguments().getString(EXTRA_CATEGORY);
        DatabaseReference postsRef = TextUtils.isEmpty(category) ?
                Library.getAllPostsRef()
                :
                Library.getPostsByCategoryRef(category);
        return new PostsAdapter(getActivity(), postsRef, this);
    }
}
