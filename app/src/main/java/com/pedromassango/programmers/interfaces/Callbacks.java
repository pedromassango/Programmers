package com.pedromassango.programmers.interfaces;

/**
 * Created by pedromassango on 11/8/17.
 */

import java.util.List;

/**
 * Contains generic callbacks.
 */
public class Callbacks {

    // For single result
    public interface IResultCallback<T> {
        void onSuccess(T result);
        void onDataUnavailable();
    }

    // For multiple itens results
    public interface IResultsCallback<T> {
        void onSuccess(List<T> results);
        void onDataUnavailable();
    }

    // For non result nedded
    public interface IRequestCallback{
        void onSuccess();
        void onError();
    }

    public interface IDeleteListener<T>{
        void delete(T item);
    }

}
