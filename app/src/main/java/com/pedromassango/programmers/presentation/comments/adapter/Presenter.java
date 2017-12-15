package com.pedromassango.programmers.presentation.comments.adapter;

import android.content.Context;
import android.os.Bundle;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.CommentsRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 26-02-2017 22:27.
 */

public class Presenter implements Contract.Presenter, Callbacks.IResultCallback<Comment> {

    private Contract.View view;
    private CommentsRepository model;
    private Context context;

    Presenter(Context context, Contract.View view) {
        this.view = view;
        this.context = context;
        this.model = RepositoryManager.getInstance().getCommentsRepository();
    }

    // Return the current user id
    private String getUserId(){ return PrefsHelper.getId();}

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onCommentLongClicked(int adapterPosition, Comment comment) {

        CharSequence[] actions = new CharSequence[1];

        //Can't mark your own sendComment
        if (comment.getAuthorId().equals(getUserId())) {
            actions[0] = getContext().getString(R.string.action_delete_comment);
            view.showDialogAction(adapterPosition, comment, actions);
            return;
        }

        //Show unmark sendComment
        //else
        //Show mark sendComment

        if (comment.getVotes().containsKey(getUserId())) {
            actions[0] = getContext().getString(R.string.action_unmark_util);
            view.showDialogAction(adapterPosition, comment, actions);
        } else {
            actions[0] = getContext().getString(R.string.action_mark_util);
            view.showDialogAction(adapterPosition, comment, actions);
        }
    }

    @Override
    public void onDialogActionItemClicked(int adapterPosition, Comment comment) {

        //Can't mark your own sendComment
        if (comment.getAuthorId().equals(getUserId())) {

            view.showProgessDialog(R.string.deleting_comment);
            model.delete(comment, new Callbacks.IRequestCallback() {
                @Override
                public void onSuccess() {
                    view.dismissProgessDialog();
                }

                @Override
                public void onError() {
                    view.dismissProgessDialog();
                    view.showToastError();
                }
            });
            return;
        }

        //Show unmark sendComment
        if (comment.getVotes().containsKey(getUserId())) {
            model.markAsUtil(comment, false, this);
            return;
        }

        //Show mark sendComment
        if (!comment.getVotes().containsKey(getUserId())) {
            view.showToast(getContext().getString(R.string.comment_marking));
            model.markAsUtil(comment, true, this);
        }
    }

    @Override
    public void onImageUserClicked(String senderId) {

        Bundle b = new Bundle();
        b.putString(Constants.EXTRA_USER_ID, senderId);

        view.startProfileActivity(b);
    }

    @Override
    public String geTime(long timestamp) {

        return Util.getTimeAgo(timestamp);
    }

    @Override
    public String getVotesSize(int votesSize) {
        if (votesSize <= 0) {
            return "";
        }

        return String.valueOf(votesSize);
    }

    @Override
    public void onSuccess(Comment result) {
        //view.updateRecyclerView(adapterPosition);
        view.dismissProgessDialog();
        view.showToast(getContext().getString(R.string.comment_deleted_successfull));
    }

    @Override
    public void onDataUnavailable() {
        view.showToastError();
    }
}
