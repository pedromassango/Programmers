package com.pedromassango.programmers.presentation.main.drawer;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.presentation.conversations.chat.adapter.ChatAdapter;
import com.pedromassango.programmers.server.Library;

/**
 * Created by Pedro Massango on 23/06/2017 at 00:05.
 */

public class NavigationChat extends BaseFragmentRecyclerView {

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

        // adding divider on list
        recyclerView.addItemDecoration(
                new DividerItemDecoration((getContext()),
                DividerItemDecoration.VERTICAL));

        DatabaseReference allUsersChatsRef = Library.getAllUsersChatsRef();
        return (new ChatAdapter(getActivity(), allUsersChatsRef, this));
    }

    @Override
    public void reloadData(String category) {
        // Ignored on chat fragment
    }
}
