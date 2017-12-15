package com.pedromassango.programmers.presentation.comments.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 26-02-2017 22:27.
 */

public class Contract {

    interface View {
        void showToast(String message);

        void showDialogAction(int adapterPosition, Comment comment, CharSequence[] actions);

        void showProgessDialog(@StringRes int message);

        void dismissProgessDialog();

        void startProfileActivity(Bundle b);

        void showToastError();
    }

    interface Presenter extends BaseContract.PresenterImpl{

        Context getContext();

        String geTime(long comment);

        String getVotesSize(int votesSize);

        void onCommentLongClicked(int adapterPosition, Comment comment);

        void onDialogActionItemClicked(int adapterPosition, Comment comment);

        void onImageUserClicked(String senderId);
    }
}
