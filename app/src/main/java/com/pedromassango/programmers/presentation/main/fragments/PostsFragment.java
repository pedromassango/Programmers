package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.PostsRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.presentation.post.adapter.PostsAdapterSimple;
import com.pedromassango.programmers.extras.TextUtils;

import java.util.List;

/**
 * Created by Pedro Massango on 13/06/2017 at 01:05.
 */

public class PostsFragment extends BaseFragmentRecyclerView implements Callbacks.IResultsCallback<Post> {

    private static final String TAG = "PostsFragment";
    private boolean filterAction = false;
    private PostsAdapterSimple postsAdapterSimple;

    @Override
    protected void setup(Bundle bundle) {
        Log.v(TAG, "setup");
    }

    @Override
    protected RecyclerView.Adapter adapter() {

      /*  String category = getArguments().getString(EXTRA_CATEGORY);
        DatabaseReference postsRef = TextUtils.isEmpty(category) ?
                Library.getAllPostsRef()
                :
                Library.getPostsByCategoryRef(category);*/

        postsAdapterSimple = new PostsAdapterSimple(getActivity());
        return postsAdapterSimple;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Passing empty string, to fetch all posts.
        reloadData("");
    }

    @Override
    public void reloadData(String category) {
        PostsRepository repo = RepositoryManager.getInstance().getPostsRepository();

        if(TextUtils.isEmpty(category)){
            filterAction = false;
            repo.getAll(this);
        }else{
            filterAction = true;
            repo.getAll(category, this);
        }
    }

    @Override
    public void onSuccess(List<Post> results) {
        postsAdapterSimple.add(results);

        showRecyclerView();
    }

    @Override
    public void onDataUnavailable() {
        if(filterAction){
            showToast(getString(R.string.nothing_to_show));
        }else {
            showTextError(getString(R.string.something_was_wrong));
        }
    }
}
