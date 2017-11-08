package com.pedromassango.programmers.presentation.post.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.pedromassango.programmers.interfaces.IGetPostsListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.BaseContract;

import java.util.ArrayList;

/**
 * Created by Pedro Massango on 23-03-2017 20:01.
 */

class Contract {

    interface View {

        void showToast(String query);

        void setRecyclerViewVisibility(int visible);

        void showResults(ArrayList<Post> posts);

        void changeToolbarTitle(String query);

        void notifyAdapterDataSetChanged();

        void setTvEmptyVisibility(int gone);

        void setTvEmptyMessage(String string);
    }

    interface Presenter extends BaseContract.PresenterImpl, IGetPostsListener {

        void handlerSearch(Intent intent);

        void filterPostsService(String query);

        void onOptionsItemSelected(MenuItem item);

        void initialize(Intent intent, Bundle savedInstanceState);

        void onSaveInstanceState(Bundle outState);
    }

    interface OnResultListener {
        void onSuccess(ArrayList<Post> posts);

        void onError(String error);
    }
}
