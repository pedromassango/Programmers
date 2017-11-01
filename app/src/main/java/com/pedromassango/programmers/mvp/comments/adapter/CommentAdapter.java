package com.pedromassango.programmers.mvp.comments.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.mvp.adapters.holders.CommentVH;
import com.pedromassango.programmers.mvp.profile.profile.ProfileActivity;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 22-11-2016.
 */

public class CommentAdapter extends FirebaseRecyclerAdapter<Comment,CommentVH> implements Contract.View {

    private final Activity activity;
    private ProgressDialog progressDialog;
    private Presenter presenter;
    private boolean notified;
    private com.pedromassango.programmers.mvp.comments.activity.Presenter activityPresenter;

    public CommentAdapter(Activity activity, DatabaseReference ref, com.pedromassango.programmers.mvp.comments.activity.Presenter activityPresenter) {
        super(Comment.class, R.layout.row_comment, CommentVH.class, ref);
        this.activity = activity;
        this.presenter = new Presenter(activity, this);
        this.activityPresenter = activityPresenter;
    }

    public CommentAdapter(Activity activity, Query ref, com.pedromassango.programmers.mvp.comments.activity.Presenter activityPresenter) {
        super(Comment.class, R.layout.row_comment, CommentVH.class, ref);
        this.activity = activity;
        this.presenter = new Presenter(activity, this);
        this.activityPresenter = activityPresenter;
    }

    public CommentAdapter(Activity activity, Query ref) {
        super(Comment.class, R.layout.row_comment, CommentVH.class, ref);
        this.activity = activity;
        this.presenter = new Presenter(activity, this);
    }

    @Override
    protected void populateViewHolder(CommentVH viewHolder, Comment model, int position) {

        viewHolder.bindViews(activity, presenter, model);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);

        if (null != activityPresenter && !notified) {
            activityPresenter.onGetCommentsSuccessfull();
            notified = true;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgessDialog(@StringRes int message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getString(message));
        progressDialog.show();
    }

    @Override
    public void showDialogAction(final int adapterPosition, final Comment comment, CharSequence[] actions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true)
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        showLog("sendComment actionselected: " + i);
                        presenter.onDialogActionItemClicked(adapterPosition, comment);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void startProfileActivity(Bundle b) {

        IntentUtils.startActivity(activity, b, ProfileActivity.class);
    }

    @Override
    public void dismissProgessDialog() {
        progressDialog.dismiss();
    }
}
