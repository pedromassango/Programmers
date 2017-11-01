package com.pedromassango.programmers.models;

import com.google.gson.Gson;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public class FcmToken {

    private String token;
    private boolean sent;
    private boolean saved;

    public FcmToken(){

    }

    public FcmToken(String token, boolean sent, boolean saved) {
        this.token = token;
        this.sent = sent;
        this.saved = saved;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }


    // Will return an JSON from this current instance class
    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
