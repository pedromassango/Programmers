package com.pedromassango.programmers.presentation.post;

import android.content.Context;
import android.net.Uri;

import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IGetPostsListener;
import com.pedromassango.programmers.interfaces.INewPostListener;
import com.pedromassango.programmers.interfaces.IPostDeleteListener;
import com.pedromassango.programmers.interfaces.ISaveListener;
import com.pedromassango.programmers.interfaces.IUpdateListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 07/06/2017 at 12:49.
 */

public class PostContract {

    public interface ModelImpl extends BaseContract.ModelImpl {


        void deletePost(IPostDeleteListener postsPresenter, Post post);

        void searchPosts(String query, IGetPostsListener listener);

        void handleLikes(IErrorListener errorListener, Post post, boolean like);

        void publishPost(Context context, INewPostListener listener, Uri imageUri, Post post);

        void update(IUpdateListener listener, Post post);

        void increasePostViews(Post post, IErrorListener errorListener);

        void handleCommentsPermission(String author, String postId, String postCategory, boolean commentsActive, ISaveListener iSaveListener);
    }
}
