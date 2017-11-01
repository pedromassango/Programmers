package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.models.Post;

import java.util.ArrayList;

/**
 * Created by Pedro Massango on 07/06/2017 at 12:16.
 */

public interface ILoaderPostsListener {
    void onSuccess(ArrayList<Post> posts);

    void onError(String error);
}
