package com.pedromassango.programmers.presentation.post.adapter;

import android.content.Context;
import android.os.Bundle;

import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IPostDeleteListener;
import com.pedromassango.programmers.interfaces.ISaveListener;
import com.pedromassango.programmers.models.ContextMenuItem;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.adapters.ContextMenuAdapter;
import com.pedromassango.programmers.presentation.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Pedro Massango on 26-02-2017 16:35.
 */

public class Contract {

    interface View {

        void showToast(String message);

        void openMenuOption(android.view.View view, Post post, int currentPostPosition, ContextMenuAdapter menuAdapter);

        void onPostImageClicked(Bundle b);

        void startEditPostActivity(Bundle bundle);

        void showProgressDialog(String string);

        void dismissProgressDialog();

        void showAlertDialog(String error);

        void startCommentsActivity(Bundle bundle);

        void update(Post result, int position);

        void delete(Post result, int position);
    }

    public interface Presenter extends BaseContract.PresenterImpl, IErrorListener, IPostDeleteListener, ISaveListener {

        String getLikesCount(HashMap<String, Boolean> votes);

        String getViewsCount(int viewsCount);

        String getCommentsCount(int commentsCount);

        int getLikeButtonTextColor(Post post);

        List<ContextMenuItem> getContextMenuItems(String authorId, boolean commentsActive);

        void onMenuOptionsClicked(android.view.View vOptions, Post post, int position);

        void onPostImageClicked(String postImageUrl);

        void showToast(String s);

        void onFacebookShareButton(Post post);

        Context getContext();

        void onContextMenuItemClicked(int position, Post post);

        void onCommentClicked(Post post);

        void onLikeClicked(Post post, int position);

        String getUserId();
    }
}
