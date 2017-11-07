package com.pedromassango.programmers.mvp.post.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IPostDeleteListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.mvp.post.PostModel;
import com.pedromassango.programmers.extras.CategoriesUtils;

import static com.pedromassango.programmers.extras.Constants.EXTRA_POST;

/**
 * Created by Pedro Massango on 23-02-2017 11:15.
 */

public class Presenter implements Contract.Presenter, IPostDeleteListener {

    private Contract.View view;
    private PostModel postModel;
    private Post post;

    Presenter(Contract.View view) {
        this.view = view;
        this.postModel = new PostModel(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize(Intent intent, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            post = savedInstanceState.getParcelable(EXTRA_POST);
        } else {
            post = intent.getParcelableExtra(EXTRA_POST);
        }

        int cPosition = CategoriesUtils.getCPosition(getContext(), post.getCategory());
        view.fillViews(post, cPosition);
    }

    @Override
    public void updatePostClicked() {

        String pTitle = view.getPostTitle();
        String pDescription = view.getPostDescription();
        String pCategory = view.getPostCategory();

        if (pTitle.equals(post.getTitle()) &&
                pDescription.equals(post.getBody())) {
            view.showToast(R.string.no_changes_maked_in_edit_post);
            return;
        }

        post.setTitle(pTitle);
        post.setBody(pDescription);
        post.setCategory(pCategory);

        view.showProgress(R.string.updating_post);
        postModel.update(this, post);
    }

    @Override
    public void deletePostClicked() {

        //Show dialog, for user confirm delete action
        //If agree, delete post
        view.showDeletePostAlert(new Contract.OnUserPostDeleteChoseListener() {
            @Override
            public void deletePostConfirmed() {

                //Delete post on the server
                postModel.deletePost(Presenter.this, post);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(EXTRA_POST, post);
    }

    @Override
    public void updateSuccess() {
        view.dismissProgress();
        view.actionPostUpdated();
    }

    @Override
    public void updateError(String message) {
        view.dismissProgress();
        view.showFailDialog(message);
    }

    @Override
    public void onPostDeleteSuccess() {
        view.dismissProgress();
        view.postDeleted();
    }

    @Override
    public void onPostDeleteError(String error) {

        view.dismissProgress();
        view.showFailDialog(error);
    }
}
