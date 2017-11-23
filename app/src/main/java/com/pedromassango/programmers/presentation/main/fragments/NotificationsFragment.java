package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.NotificationRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.presentation.adapters.NotificationAdapter;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;

import java.util.List;

/**
 * Created by Pedro Massango on 14/06/2017 at 18:57.
 */

public class NotificationsFragment extends BaseFragmentRecyclerView implements Callbacks.IResultsCallback<Notification>,Callbacks.IDeleteListener<Notification> {

    private NotificationAdapter notificationAdapter;
    private NotificationRepository notificationRepository;

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected RecyclerView.Adapter adapter() {

        //setting separator in recyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL));

        notificationAdapter = new NotificationAdapter(getActivity(), this);
        return (notificationAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showTextError(R.string.a_carregar);

        notificationRepository = RepositoryManager.getInstance()
                .getNotificationRepository();

        // Fetch all notifications
        notificationRepository.get(PrefsHelper.getId(), this);
    }

    @Override
    public void reloadData(String category) {
        // Ignored on Notifications Fragment,
        // We do not need this here.
    }

    @Override
    public void onSuccess(List<Notification> results) {
        notificationAdapter.add( results);

        showRecyclerView();
    }

    @Override
    public void onDataUnavailable() {
        showTextError(R.string.nothing_to_show);
    }

    @Override
    public void delete(final Notification item) {
        notificationRepository.delete(item, new Callbacks.IResultCallback<Notification>() {
            @Override
            public void onSuccess(Notification result) {

                notificationAdapter.remove(item);
            }

            @Override
            public void onDataUnavailable() {
                showToast(getString(R.string.something_was_wrong));
            }
        });
    }
}
