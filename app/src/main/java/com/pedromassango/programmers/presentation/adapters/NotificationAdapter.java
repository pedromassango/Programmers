package com.pedromassango.programmers.presentation.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.NotificationRepository;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.interfaces.IDeleteListener;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.presentation.adapters.holders.NotificationVH;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationVH> {

    private ArrayList<Notification> notifications;
    private Activity activity;
    private Callbacks.IDeleteListener<Notification> deleteListener;

    public NotificationAdapter(Activity actvivity, Callbacks.IDeleteListener<Notification> deleteListener) {
        this.activity = actvivity;
        this.deleteListener = deleteListener;
        this.notifications = new ArrayList<>();
    }

    @Override
    public NotificationVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_notification, viewGroup, false);
        return new NotificationVH(v);
    }

    @Override
    public void onBindViewHolder(NotificationVH notificationVH, int i) {
        Notification notification = notifications.get(i);

        notificationVH.bindViews(notification, activity, deleteListener);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void add(Notification notification) {
        notifications.add(notification);
        notifyDataSetChanged();
    }

    public void add(List<Notification> mNotifications) {
        notifications.clear();
        notifications.addAll(mNotifications);
        notifyDataSetChanged();
    }

    public void remove(Notification item) {
        notifications.remove(item);
        notifyDataSetChanged();
    }
}
