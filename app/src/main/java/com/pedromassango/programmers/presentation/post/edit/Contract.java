package com.pedromassango.programmers.presentation.post.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.interfaces.IUpdateListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 03-03-2017 15:33.
 */

class Contract {

    interface View {
        String getPostTitle();

        String getPostDescription();

        String getPostCategory();

        void fillViews(Post post, int categoryPosition);

        void showFailDialog(@StringRes String message);

        void showProgress(@StringRes int message);

        void dismissProgress();

        void actionPostUpdated();

        void showDeletePostAlert(OnUserPostDeleteChoseListener listener);

        void postDeleted();

        void showToast(@StringRes int message);
    }

    interface Presenter extends BaseContract.PresenterImpl, IUpdateListener {

        void initialize(Intent intent, Bundle savedInstanceState);

        void updatePostClicked();

        void deletePostClicked();

        void onSaveInstanceState(Bundle outState);
    }

    public interface OnUserPostDeleteChoseListener {

        void deletePostConfirmed();
    }
}
