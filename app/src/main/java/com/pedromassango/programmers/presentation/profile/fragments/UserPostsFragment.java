package com.pedromassango.programmers.presentation.profile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.presentation.post.adapter.PostsAdapter;
import com.pedromassango.programmers.presentation.post.adapter.PostsAdapterSimple;
import com.pedromassango.programmers.server.Library;

import java.util.List;

/**
 * Created by JM on 21/09/2017.
 */

public class UserPostsFragment extends BaseFragmentRecyclerView implements Callbacks.IResultsCallback<Post> {

    private PostsAdapterSimple adapterSimple;

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected RecyclerView.Adapter adapter() {
        adapterSimple = new PostsAdapterSimple(getActivity());
        return adapterSimple;
        //return (new PostsAdapter(getActivity(), Library.getUserPostsRef(userId), this));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userId = getArguments().getString(Constants.EXTRA_USER_ID);

        RepositoryManager.getInstance()
                .getPostsRepository()
                .getByUser(userId, this);
    }

    @Override
    public void onSuccess(List<Post> results) {
        showRecyclerView();
        adapterSimple.add( results);
    }

    @Override
    public void reloadData(String category) {
        // Ignored here on Profile activity
    }

    @Override
    public void onDataUnavailable() {
        showTextError(getString(R.string.something_was_wrong));
    }
}
