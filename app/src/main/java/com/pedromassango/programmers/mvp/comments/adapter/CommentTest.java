package com.pedromassango.programmers.mvp.comments.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.mvp.profile.profile.ProfileActivity;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 22-11-2016.
 */

public class CommentTest extends RecyclerView.Adapter<CommentTest.CommentVH> implements Contract.View {

    private final Activity activity;
    private ProgressDialog progressDialog;
    private Presenter presenter;
    private ArrayList<Comment> comments;
    private com.pedromassango.programmers.mvp.comments.activity.Presenter activityPresenter;


    public CommentTest(Activity activity, ArrayList<Comment> comments) {
        this.comments = comments;
        this.activity = activity;
        this.presenter = new Presenter(activity, this);
        this.comments = comments;
    }

    @Override
    public CommentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_comment, parent, false);
        return (new CommentVH(v));
    }

    @Override
    public void onBindViewHolder(CommentVH holder, int position) {

        holder.bindViews(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
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

    final class CommentVH extends RecyclerView.ViewHolder {
         TextView tvText;
         TextView tvDate;
         TextView tvVotes;
         TextView tvSenderName;
         CircleImageView imgUser;

        CommentVH(View itemView) {
            super(itemView);
            tvSenderName = (TextView) itemView.findViewById(R.id.row_comment_sender_name);
            tvText = (TextView) itemView.findViewById(R.id.row_comment_text);
            tvDate = (TextView) itemView.findViewById(R.id.row_comment_date);
            tvVotes = (TextView) itemView.findViewById(R.id.tv_votes);
            tvVotes.setText("");
            imgUser = (CircleImageView) itemView.findViewById(R.id.row_comment_sender_image);
        }

        void bindViews(final Comment comment) {

            String time = presenter.geTime(comment.getTimestamp());
            String votesSize = presenter.getVotesSize(comment.getVotes().size());

            tvSenderName.setText(comment.getAuthor());
            tvText.setText(comment.getText());
            tvDate.setText(time);

            if (votesSize.trim().length() > 0) {
                tvVotes.setText(votesSize);
                tvVotes.setVisibility(View.VISIBLE);
            } else {
                tvVotes.setVisibility(View.GONE);
            }

            ImageUtils.loadImageUser(activity, comment.getAuthorUrlPhoto(), imgUser);

            imgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    presenter.onImageUserClicked(comment.getAuthorId());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    presenter.onCommentLongClicked(getAdapterPosition(), comment);
                    return false;
                }
            });
        }
    }
}
