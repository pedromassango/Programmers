package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.presentation.adapters.NotificationAdapter;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.TextUtils;

/**
 * Created by Pedro Massango on 14/06/2017 at 18:57.
 */

public class NotificationsFragment extends BaseFragmentRecyclerView {

    private NotificationAdapter notificationAdapter;

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected RecyclerView.Adapter adapter() {

        //setting separator in recyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL));

        notificationAdapter = new NotificationAdapter(getActivity());
        return (notificationAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: get notifications
    }

    @Override
    public void reloadData(String category) {
        // Ignored on Notifications Fragment,
        // We do not need this here.
    }
}
