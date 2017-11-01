package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.models.Post;

import java.util.ArrayList;

/**
 * Created by Pedro Massango on 07/06/2017 at 12:57.
 */

public interface IGetPostsListener {
    void onGetPostSuccessfull(ArrayList<Post> post);
    void onError(String error);
}
