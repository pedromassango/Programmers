package com.pedromassango.programmers.data;

import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;

/**
 * Created by pedromassango on 11/12/17.
 */

public interface PostsDataSource {

    void getAll(Callbacks.IResultsCallback<Post> callback);

    void getByUser(String authorId, Callbacks.IResultsCallback<Post> callback);

    void getAll(String category, Callbacks.IResultsCallback<Post> callback);

    void getById(String postId, Callbacks.IResultCallback<Post> callback);

    void save(Post post, Callbacks.IRequestCallback callback);

    void update(Post post, Callbacks.IRequestCallback callback);

    void search(String query, Callbacks.IResultsCallback<Post> callback);

    void delete(Post post, Callbacks.IRequestCallback callback);

    void handleCommentsPermission(Post post, final Callbacks.IResultCallback<Post> callback);
}
