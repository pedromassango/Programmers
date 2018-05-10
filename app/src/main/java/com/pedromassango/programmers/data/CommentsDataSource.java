package com.pedromassango.programmers.data;

import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Notification;

/**
 * Created by Pedro Massango on 11/22/17.
 */

public interface CommentsDataSource {

    void send(Comment comment, Notification notification, Callbacks.IRequestCallback callback);

    void markAsUtil(Comment comment, boolean mark, Callbacks.IResultCallback<Comment> callback);

    void delete(Comment comment, Callbacks.IRequestCallback callback);

    void getAll(String postId, Callbacks.IResultsCallback<Comment> callback);
}
