package com.pedromassango.programmers.interfaces;

/**
 * Created by Pedro Massango on 09/06/2017 at 10:35.
 */

/**
 * Class to handle friend presence in {@link com.pedromassango.programmers.mvp.conversations.messages.MessagesActivity}
 *
 */
public interface IPresenceLIstener {

    void onFriendOnline(boolean state);

    void onFriendOffline(String lastOnline);
}
