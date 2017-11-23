package com.pedromassango.programmers.presentation.post.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.PostsRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.ContextMenuItem;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.adapters.ContextMenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pedromassango.programmers.extras.Constants.EXTRA_POST;
import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 26-02-2017 16:35.
 */

public class AdapterPresenter implements Contract.Presenter {

    private Context context;
    private Contract.View view;
    private PostsRepository postsRepository;
    //private final String loggedUserId;

    public AdapterPresenter(Context context, Contract.View view) {
        this.view = view;
        this.context = context;
        postsRepository = RepositoryManager.getInstance().getPostsRepository();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String getLikesCount(HashMap<String, Boolean> likes) {
        return Util.join("(", likes.size(), ")");
    }

    @Override
    public String getViewsCount(int viewsCount) {

        return Util.join("(", viewsCount, ")");
    }

    @Override
    public String getCommentsCount(int commentsCount) {

        return Util.join("(", commentsCount, ")");
    }

    @Override
    public String getUserId() {
        return PrefsHelper.getId();
    }

    /*
            This method is to change the icon.
                it will check if the user arleady make some vote
                if have, set icon red.
         */
    @Override
    public int getLikeButtonTextColor(Post post) {

//        @ColorInt int color = getContext().getResources().getColor(R.color.primaryText);

        // A list of the post votes
        Map<String, Boolean> votes = post.getLikes();

//        if (votes.size() == 0)
//            return color;

        boolean participate = votes.containsKey(getUserId());
        return (participate) ? getContext().getResources().getColor(R.color.colorAccent)
                :
                getContext().getResources().getColor(R.color.primaryText);
    }

    /**
     * Swith the options to show on contextMenu
     * depending on authorId
     *
     * @param postAuthorId - The current Post author id to compare if is the same
     *                     whith current logged user ID.
     * @return the contextMenuItems to show up
     */
    @Override
    public List<ContextMenuItem> getContextMenuItems(String postAuthorId, boolean commentsActive) {
        ArrayList<ContextMenuItem> contextMenuItemList = new ArrayList<>();

        //TODO: make changes here

        boolean currentUserPost = postAuthorId.equals( getUserId());

        showLog(String.format("CURRENT_USER: %s  POST_USER: %s", getUserId(), postAuthorId));

        // If the current user is the author of the Post
        // The options is EDIT and DELETE
        if (currentUserPost) {
            int message = (commentsActive) ? R.string.action_deative_comments : R.string.action_ative_comments;
            @DrawableRes int icon = (commentsActive) ? R.drawable.ic_clear_black : R.drawable.ic_done_black;

            contextMenuItemList.add(new ContextMenuItem(icon, message));
            contextMenuItemList.add(new ContextMenuItem(R.drawable.ic_edit_dark, R.string.action_edit));
            contextMenuItemList.add(new ContextMenuItem(R.drawable.ic_delete_dark, R.string.action_delete));
        } else {
            contextMenuItemList.add(new ContextMenuItem(R.drawable.ic_vote_up_simple, R.string.up_vote));
            contextMenuItemList.add(new ContextMenuItem(R.drawable.ic_vote_down_simple, R.string.down_vote));
        }
        return contextMenuItemList;
    }

    @Override
    public void onContextMenuItemClicked(int position, Post post) {

        // If sender is'nt the loggedUser then return
        // Perform Edit and Delete click
        if (!(post.getAuthorId().equals(getUserId()))) {
            return;
        }

        switch (position) {

            case 0:
                postsRepository.handleCommentsPermission(post, postResultCallback(position));
                break;
            case 1: // Edit Option
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_POST, post);
                view.startEditPostActivity(bundle);
                break;

            case 2: // Delete Option
                view.showProgressDialog(getContext().getString(R.string.deleting_post));
                postsRepository.delete(post, postDeleteResultCallback( post, position));
                break;
        }
    }

    @Override
    public void onMenuOptionsClicked(View vOptions, Post post, int currentPostPosition) {

        List<ContextMenuItem> menuItems = getContextMenuItems(post.getAuthorId(), post.isCommentsActive());
        ContextMenuAdapter menuAdapter = new ContextMenuAdapter(getContext(), menuItems);

        view.openMenuOption(vOptions, post, currentPostPosition, menuAdapter);
    }

    @Override
    public void onLikeClicked(Post post, int position) {
        HashMap<String, Boolean> likes = post.getLikes();
        boolean arleadyLiked = likes.containsKey( getUserId());

        if (arleadyLiked)
            postsRepository.handleLikes(post, false, postResultCallback( position));
        else
            postsRepository.handleLikes(post, true, postResultCallback(position));
    }

    private Callbacks.IRequestCallback postDeleteResultCallback(final Post post, final int position) {
        return new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                view.delete(post, position);
            }

            @Override
            public void onError() {

                view.showToast(getContext().getString(R.string.something_was_wrong));
            }
        };
    }

    private Callbacks.IResultCallback<Post> postResultCallback(final int position) {
        return new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post result) {
                view.update(result, position);
            }

            @Override
            public void onDataUnavailable() {

                view.showToast(getContext().getString(R.string.something_was_wrong));
            }
        };
    }


    @Override
    public void onCommentClicked(Post post) {
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_POST, post);

        postsRepository.increasePostViews(post);
        view.startCommentsActivity(b);
    }

    @Override
    public void onPostImageClicked(String postImageUrl) {

        Bundle b = new Bundle();
        b.putString(Constants.EXTRA_IMAGE_URL, postImageUrl);
        view.onPostImageClicked(b);
    }

    @Override
    public void onFacebookShareButton(Post post) {

        //SharePhoto photo = new SharePhoto.Builder().setBitmap();

        // ShareContent content;

        // facebook share content

     /*   ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle( post.getTitle())
                .setContentDescription(post.getText())*/
    }

    @Override
    public void showToast(String s) {
        view.showToast(s);
    }

    @Override
    public void onSaveSuccess(String message) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onDeleteSuccess() {
        view.dismissProgressDialog();
        view.showToast(getContext().getString(R.string.post_deleted_successfull));
    }

    @Override
    public void onDeleteError(String error) {

        view.dismissProgressDialog();
        view.showAlertDialog(error);
    }
}
