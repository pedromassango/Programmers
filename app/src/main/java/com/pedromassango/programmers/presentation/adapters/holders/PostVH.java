package com.pedromassango.programmers.presentation.adapters.holders;

import android.support.v4.text.util.LinkifyCompat;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.post.adapter.AdapterPresenter;


/**
 * Created by JANU on 15/05/2017.
 */

public class PostVH extends RecyclerView.ViewHolder {

    private final TextView mUsername;
    private final TextView mTitle;
    private final TextView mBody;
    private final TextView mCategory;
    private final TextView mLikes;
    private final TextView mViews;
    private final TextView mComments;
    private final ImageView mPostImage;
    private final ImageView mOptions;
    private final Button btnComment;
    private final Button btnLike;
    //public ShareButton shareButton;

    public PostVH(View itemView) {
        super(itemView);

        mTitle = itemView.findViewById(R.id.tv_title);
        mBody = itemView.findViewById(R.id.tv_text);
        mUsername = itemView.findViewById(R.id.tv_author);
        mPostImage = itemView.findViewById(R.id.row_post_image);

        mOptions = itemView.findViewById(R.id.img_options);
        mCategory = itemView.findViewById(R.id.tv_category);
        mLikes = itemView.findViewById(R.id.tv_up_votes);
        mViews = itemView.findViewById(R.id.tv_views);
        mComments = itemView.findViewById(R.id.tv_comments);

        btnComment = itemView.findViewById(R.id.btn_comment);
        btnLike = itemView.findViewById(R.id.btn_like);
        //shareButton = (ShareButton)itemView.findViewById(R.id.share_btn);
    }

    // To bind views whit data
    public void bindViews(final AdapterPresenter adapterPresenter, final Post post) {

        String likesCount = adapterPresenter.getLikesCount(post.getLikes());
        String viewsCount = adapterPresenter.getViewsCount(post.getViews());
        String commentsCount = adapterPresenter.getCommentsCount(post.getCommentsCount());
        int likeButtonTextColor = adapterPresenter.getLikeButtonTextColor(post);

        String time = Util.getTimeAgo(post.getTimestamp());
        String timeAndName = Util.concat(time, "-", post.getAuthor());

        mTitle.setText(post.getTitle());
        mBody.setText(post.getBody());
        mUsername.setText(timeAndName);
        mCategory.setText(post.getCategory());
        mLikes.setText(likesCount);
        mComments.setText(commentsCount);
        mViews.setText(viewsCount);
        btnLike.setTextColor(likeButtonTextColor);

        //Detect links, to perfomr actions
        LinkifyCompat.addLinks(mBody, Linkify.ALL);

        // Breaking MVP patterns
        if (!post.isCommentsActive()) {
            btnComment.setVisibility(View.GONE);
        }

        if (!post.getAuthorId().equals(adapterPresenter.getUserId())) {
            mOptions.setVisibility(View.GONE);
        }

        ImageUtils.loadPostImage(itemView.getContext(), post.getImgUrl(), mPostImage);

        itemView.findViewById(R.id.linearLayout1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapterPresenter.onCommentClicked(post);
                    }
                });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapterPresenter.onLikeClicked(post, getAdapterPosition());
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapterPresenter.onCommentClicked(post);
            }
        });

        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapterPresenter.onMenuOptionsClicked(view, post, getAdapterPosition());
            }
        });

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapterPresenter.onPostImageClicked(post.getImgUrl());
            }
        });

    }
}
