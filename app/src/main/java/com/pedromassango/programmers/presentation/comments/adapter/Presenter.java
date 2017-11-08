package com.pedromassango.programmers.presentation.comments.adapter;

import android.content.Context;
import android.os.Bundle;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.presentation.comments.CommentsContract;
import com.pedromassango.programmers.presentation.comments.CommentsModel;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 26-02-2017 22:27.
 */

public class Presenter implements Contract.Presenter, CommentsContract.OnDeleteCommentListener, CommentsContract.OnSetUtilCommentListener {

    private Contract.View view;
    private CommentsModel model;
    private Context context;

    Presenter(Context context, Contract.View view) {
        this.view = view;
        this.context = context;
        this.model = new CommentsModel(this);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onCommentLongClicked(int adapterPosition, Comment comment) {

        CharSequence[] actions = new CharSequence[1];

        //Can't mark your own sendComment
        if (comment.getAuthorId().equals(model.getUserId())) {
            actions[0] = getContext().getString(R.string.action_delete_comment);
            view.showDialogAction(adapterPosition, comment, actions);
            return;
        }

        //Show unmark sendComment
        //else
        //Show mark sendComment

        if (comment.getVotes().containsKey(model.getUserId())) {
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
        if (comment.getAuthorId().equals(model.getUserId())) {

            view.showProgessDialog(R.string.deleting_comment);
            model.deleteComment(this, comment);
            return;
        }

        //Show unmark sendComment
        if (comment.getVotes().containsKey(model.getUserId())) {

            view.showToast(getContext().getString(R.string.comment_unmarking));
            model.unMarkCommentUtil(this, comment);
            return;
        }

        //Show mark sendComment
        if (!comment.getVotes().containsKey(model.getUserId())) {
            view.showToast(getContext().getString(R.string.comment_marking));
            model.markCommentUtil(this, comment);
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
    public void onError(String error) {
        view.showToast(error);
    }

    //Delete result
    @Override
    public void onDeleteSuccess() {
        //view.updateRecyclerView(adapterPosition);
        view.dismissProgessDialog();
        view.showToast(getContext().getString(R.string.comment_deleted_successfull));
    }

    @Override
    public void onDeleteError() {
        view.dismissProgessDialog();

        view.showToast(getContext().getString(R.string.something_was_wrong));
    }

}
