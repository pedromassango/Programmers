package com.pedromassango.programmers.mvp.adapters.holders;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.mvp.comments.adapter.Presenter;
import com.pedromassango.programmers.extras.ImageUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pedro Massango on 24/06/2017 at 00:19.
 */

public class CommentVH extends RecyclerView.ViewHolder {
    TextView tvText;
    TextView tvDate;
    TextView tvVotes;
    TextView tvSenderName;
    CircleImageView imgUser;

    public CommentVH(View itemView) {
        super(itemView);

        tvSenderName = (TextView) itemView.findViewById(R.id.row_comment_sender_name);
        tvText = (TextView) itemView.findViewById(R.id.row_comment_text);
        tvDate = (TextView) itemView.findViewById(R.id.row_comment_date);
        tvVotes = (TextView) itemView.findViewById(R.id.tv_votes);
        imgUser = (CircleImageView) itemView.findViewById(R.id.row_comment_sender_image);
    }

    public void bindViews(Activity activity, final Presenter presenter, final Comment comment) {

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
