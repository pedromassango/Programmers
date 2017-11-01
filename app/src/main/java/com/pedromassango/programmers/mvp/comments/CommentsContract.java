package com.pedromassango.programmers.mvp.comments;

import com.pedromassango.programmers.interfaces.IGetPostListener;
import com.pedromassango.programmers.interfaces.ISendCommentListener;
import com.pedromassango.programmers.models.Comment;

/**
 * Created by Pedro Massango on 11/06/2017 at 00:37.
 */

public class CommentsContract {

    public interface CommentsModelImpl{

        void markCommentUtil( OnSetUtilCommentListener listener, final Comment comment);

        void unMarkCommentUtil(OnSetUtilCommentListener listener, Comment comment);

        String getUserId();

        void deleteComment(OnDeleteCommentListener listener, Comment comment);

        String getUsername();

        String getUserUrlPhoto();

        void sendComment(ISendCommentListener listener, Comment comment);

        void getPost(String postId, IGetPostListener getPostListener);

        String generateId();
    }

    public interface OnSetUtilCommentListener {

        void onError(String error);
    }

    public interface OnDeleteCommentListener {
        void onDeleteSuccess();

        void onDeleteError();
    }
}
