package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.models.Post;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public interface IGetPostListener {
    void onGetPostSuccessfull(Post post);
    void onError(String error);
}
