package com.pedromassango.programmers.presentation.comments.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 22-02-2017 1:11.
 */

class Contract {

    interface View {

        String getComment();

        void setCommentError(@StringRes int empty_text);

        void showTextLoading(String message);

        void onSendCommentSuccess();

        void showToast(String error);

        void fetchComments(String postId);

        void showRecyclerviewComments();

        void setEditTextVisibility(int visibility);

        void bindViews(String postAuthor, String author, String category, String title, String body, String likes, String date);
    }

    interface Presenter extends BaseContract.PresenterImpl{

        Context getContext();

        void initialize(Intent intent, Bundle savedState);

        void onSendCommentClicked();

        void onGetCommentsSuccessfull();

        void showMessageLoading(String message);
    }
}
