package com.pedromassango.programmers.mvp.main.fragments;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.mvp.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.mvp.conversations.chat.adapter.ChatAdapter;
import com.pedromassango.programmers.server.Library;

/**
 * Created by Pedro Massango on 23/06/2017 at 00:05.
 */

public class ChatsFragment extends BaseFragmentRecyclerView {

    @Override
    protected void setup(Bundle bundle) {

    }

    /**
     * This method will bind with the server, and
     * return a list of users states on the server
     * something like a CHAT list
     *
     * @return the {@link ChatAdapter} that will show the chat list.
     */
    @Override
    protected ChatAdapter adapter() {

        DatabaseReference allUsersChatsRef = Library.getAllUsersChatsRef();
        return (new ChatAdapter(getActivity(), allUsersChatsRef, this));
    }
}
