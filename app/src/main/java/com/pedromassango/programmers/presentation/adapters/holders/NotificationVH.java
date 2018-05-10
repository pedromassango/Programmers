package com.pedromassango.programmers.presentation.adapters.holders;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.NotificationRepository;
import com.pedromassango.programmers.data.Transations;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.interfaces.IDeleteListener;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.presentation.comments.activity.CommentsActivity;

/**
 * Created by Pedro Massango on 14/06/2017 at 18:58.
 */

public class NotificationVH extends RecyclerView.ViewHolder {

    private final TextView tvDate;
    private final TextView tvTitle;
    private final TextView tvDescription;

    public NotificationVH(View view) {
        super(view);
        tvDate = view.findViewById(R.id.tv_date);
        tvTitle = view.findViewById(R.id.tv_title);
        tvDescription = view.findViewById(R.id.tv_description);
    }

    public void bindViews(final Notification notification,
                          final Activity activity,
                          final Callbacks.IDeleteListener<Notification> iDeleteListener) {

        tvDate.setText(Util.getTimeAgo(notification.getTimestamp()));

        final String notificationDescription =
                String.format("%s %s", notification.getAuthor(), activity.getString(R.string.notification_current_user_text));

        tvTitle.setText( notification.getPostTitle());
        tvDescription.setText(String.valueOf(notificationDescription));

        // For delete action
        itemView.findViewById(R.id.root_view)
                .setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogActions( activity,notification, iDeleteListener);
                return false;
            }
        });

        // Notification clicked
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString(Constants.EXTRA_POST_ID, notification.getPostId());

                IntentUtils.startActivity(activity, b, CommentsActivity.class);
            }
        });
    }

    private void showDialogActions(final Context context, final Notification notification,
                                   final Callbacks.IDeleteListener<Notification> deleteListener) {

        // Check to handle witch message we need to show
        CharSequence[] actions = {context.getString(R.string.action_delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true)
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteListener.delete(notification);
                    }
                });

        builder.create()
                .show();
    }
}
