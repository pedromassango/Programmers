package com.pedromassango.programmers.presentation.comments.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.CommentsRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.server.Library;

import static com.pedromassango.programmers.extras.Constants.EXTRA_POST;

/**
 * Created by Pedro Massango on 22-02-2017 1:12.
 */

public class Presenter implements Contract.Presenter,Callbacks.IRequestCallback, Callbacks.IResultCallback<Post> {

    private Post post = null;
    private Contract.View view;
    private CommentsRepository model;

    public Presenter(Contract.View view) {
        this.view = view;
        this.model = RepositoryManager.getInstance().getCommentsRepository();
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
            Log.i("output", "received post id: " +postId);

            // Get the post data from server
            view.showTextLoading();
            RepositoryManager.getInstance()
                    .getPostsRepository()
                    .getById( postId, this);
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

        String userId = PrefsHelper.getId();
        String username = PrefsHelper.getName();
        String userPhoto = PrefsHelper.getPhoto();

        long currentTime = Util.getTime();

        Comment comment = new Comment();
        comment.setText(text);
        comment.setId( Library.generateId());
        comment.setPostId(post.getId());
        comment.setPostAuthorId(post.getAuthorId());
        comment.setPostCategory(post.getCategory());
        comment.setAuthorId( userId);
        comment.setAuthor( username);
        comment.setAuthorUrlPhoto( userPhoto);
        comment.setTimestamp(currentTime);

        Notification n = new Notification();
        n.setId( String.valueOf( Util.getTime()));
        n.setAuthor( username);
        n.setAuthorId( userId);
        n.setToUserId( post.getAuthorId());
        n.setPostId( post.getId());
        n.setTimestamp( currentTime);

        model.send(comment,n, this);
    }

    @Override
    public void onGetCommentsSuccessfull() {

        view.showRecyclerviewComments();
    }

    @Override
    public void showMessageLoading(String message) {

        view.showTextLoading();
    }

    @Override
    public void onSuccess(Post result) {
        // Will show the post info
        // And load all comments from this post
        showPostInfo(post);
    }

    @Override
    public void onDataUnavailable() {   // get post error

        view.showGetPostError();
    }

    @Override
    public void onSuccess() {   // send comment success
        view.onSendCommentSuccess();
    }

    @Override
    public void onError() { // send comment error
        view.onSendError();
    }
}
