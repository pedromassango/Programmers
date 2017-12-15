package com.pedromassango.programmers.presentation.post.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.presentation.post.adapter.PostsAdapterSimple;

import java.util.ArrayList;

public class SearchPostActivity extends BaseActivity implements Contract.View {

    private RecyclerView recyclerView;
    private PostsAdapterSimple postsAdapter;
    private TextView tvEmpty;

    private Presenter presenter;

    @Override
    protected int layoutResource() {
        return R.layout.activity_search;
    }

    @Override
    protected void initializeViews() {

        recyclerView = findViewById(R.id.recycler_posts);
        tvEmpty = findViewById(R.id.tv_empty);

        postsAdapter = new PostsAdapterSimple(this);
        recyclerView.setAdapter( postsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.initialize(getIntent(), savedInstanceState);
        presenter.handlerSearch(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        this.setIntent(intent);
        presenter.handlerSearch(intent);
    }

    @Override
    public void changeToolbarTitle(String query) {

        this.setTitle(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem itemSearch = menu.findItem(R.id.action_search_post);

        searchView = (SearchView) itemSearch.getActionView();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//        } else {
//            searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
//        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        presenter.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void setTvEmptyVisibility(int visibility) {

        tvEmpty.setVisibility(visibility);
    }

    @Override
    public void setTvEmptyMessage(String message) {

        tvEmpty.setText(message);
    }

    @Override
    public void setRecyclerViewVisibility(int visibility) {

        recyclerView.setVisibility(visibility);
    }

    @Override
    public void showResults(ArrayList<Post> result) {
        postsAdapter.add(result);
    }

    @Override
    public void notifyAdapterDataSetChanged() {

        postsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNotFOundMessage() {
        setTvEmptyMessage( getString(R.string.posts_not_foud));
    }

    @Override
    public void showToast(String message) {

        super.showToastMessage(message);
    }
}
