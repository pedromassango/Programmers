package com.pedromassango.programmers.presentation.comments.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetPostListener;
import com.pedromassango.programmers.interfaces.ISendCommentListener;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.comments.CommentsModel;

import static com.pedromassango.programmers.extras.Constants.EXTRA_POST;

/**
 * Created by Pedro Massango on 22-02-2017 1:12.
 */

public class Presenter implements Contract.Presenter, ISendCommentListener, IGetPostListener {

    private Post post = null;
    private Contract.View view;
    private CommentsModel model;

    public Presenter(Contract.View view) {
        this.view = view;
        this.model = new CommentsModel(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize(Intent intent, Bundle savedState) {

        // If we have an instance saved
        // we show the Post info, and do not need to load the post Info.
        if (savedState != null) {
            post = savedState.getParcelable(EXTRA_POST);
            showPostInfo(post);
            return;
        }

        // If we do not have an instance saved
        // We check if the intent give us an Post ata
        // If yes, we just show the Post Info.
        if (intent.hasExtra(Constants.EXTRA_POST)) {
            post = intent.getParcelableExtra(EXTRA_POST);
            showPostInfo(post);
            return;
        }

        // If we do not have an instance saved
        // And do not receive an Post data
        // So we receive just the Post ID, and we need to load Post data.
        if (intent.hasExtra(Constants.EXTRA_POST_ID)) {
            String postId = intent.getStringExtra(Constants.EXTRA_POST_ID);

            // Get the post data from server
            view.showTextLoading("Carregando publicação...");
            model.getPost(postId, this);
        }
    }

    // LOCAL
    private void showPostInfo(Post post) {
        String date = Util.getTimeAgo(post.getTimestamp());
        String likes = String.valueOf(post.getLikes().size());
        String views = String.valueOf(post.getViews());

        view.bindViews(
                post.getAuthor(), post.getCategory(),
                post.getTitle(), post.getBody(),
                likes, views, date);

        view.showTextLoading("Carregando comentários...");
        view.fetchComments(post.getId());

//        if (!post.isCommentsActive()) {
//            view.setEditTextVisibility(View.GONE);
//        }
    }

    @Override
    public void onSendCommentClicked() {
        String text = view.getComment();

        if (text.isEmpty()) {
            view.setCommentError(R.string.empty_text);
            return;
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setId( model.generateId());
        comment.setPostId(post.getId());
        comment.setPostAuthorId(post.getAuthorId());
        comment.setPostCategory(post.getCategory());
        comment.setAuthorId(model.getUserId());
        comment.setAuthor(model.getUsername());
        comment.setAuthorUrlPhoto(model.getUserUrlPhoto());
        comment.setTimestamp(System.currentTimeMillis());

        model.sendComment(this, comment);
    }

    @Override
    public void onGetPostSuccessfull(Post post) {

        // Will show the post info
        // And load all comments from this post
        showPostInfo(post);
    }

    @Override
    public void onGetCommentsSuccessfull() {

        view.showRecyclerviewComments();
    }

    @Override
    public void onCommentSentSuccess() {

        view.onSendCommentSuccess();
    }

    @Override
    public void showMessageLoading(String message) {

        view.showTextLoading(message);
    }

    @Override
    public void onError(String error) {

        view.showToast(error);
    }
}
