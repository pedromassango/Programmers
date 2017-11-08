package com.pedromassango.programmers.presentation.post.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.MenuItem;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.post.PostContract;
import com.pedromassango.programmers.presentation.post.PostModel;
import com.pedromassango.programmers.provider.SearchableProvider;

import java.util.ArrayList;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-03-2017 20:01.
 */

class Presenter implements Contract.Presenter {

    private static final String SEARCHED_POSTS = "com.pedromassango.programmers.mvp.post.search.SEARCHED_POSTS";
    private Contract.View view;
    private PostContract.ModelImpl model;
    private ArrayList<Post> searchedPosts = new ArrayList<>();

    public Presenter(Contract.View view) {
        this.view = view;
        this.model = new PostModel(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize(Intent intent, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            searchedPosts = savedInstanceState.getParcelableArrayList(SEARCHED_POSTS);
        }
    }

    @Override
    public void handlerSearch(Intent intent) {

        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);

            view.changeToolbarTitle(query);

            filterPostsService(query);

            SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(getContext(),
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);

            recentSuggestions.saveRecentQuery(query, null);
        }
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete) {

            SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(getContext(),
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);

            recentSuggestions.clearHistory();

            view.showToast(getContext().getString(R.string.search_history_deleted));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(SEARCHED_POSTS, searchedPosts);
    }

    @Override
    public void filterPostsService(String query) {

        view.setTvEmptyVisibility(View.VISIBLE);
        view.setTvEmptyMessage(getContext().getString(R.string.searching));

        model.searchPosts(query, this);
    }

    @Override
    public void onGetPostSuccessfull(ArrayList<Post> result) {
        showLog("onGetPostSuccessfull: "+result.size());
        searchedPosts.clear();
        searchedPosts.addAll(result);

        view.setTvEmptyVisibility(View.GONE);
        view.setRecyclerViewVisibility(View.VISIBLE);
        view.showResults(searchedPosts);
        view.notifyAdapterDataSetChanged();
    }

    @Override
    public void onError(String error) {
        showLog("onGetPost-Error: "+error);
        view.setTvEmptyMessage(error);
        //view.showToastMessage(getContext().getString(error));
    }
}
