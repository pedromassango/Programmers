package com.pedromassango.programmers.mvp.base;

import android.content.Context;

/**
 * Created by Pedro Massango on 27/06/2017 at 14:53.
 */

public class BaseContract {

    public interface ModelImpl{

        String getUserId();

        String getUsername();
    }

    public interface PresenterImpl{

        Context getContext();
    }
}
