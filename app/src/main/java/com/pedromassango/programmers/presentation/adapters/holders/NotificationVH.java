package com.pedromassango.programmers.presentation.adapters.holders;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.models.Notification;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.presentation.comments.activity.CommentsActivity;

/**
 * Created by Pedro Massango on 14/06/2017 at 18:58.
 */

public class NotificationVH extends RecyclerView.ViewHolder {

    private final TextView tvDate;
    private final TextView tvDescription;

    public NotificationVH(View view) {
        super(view);
        tvDate = view.findViewById(R.id.tv_date);
        tvDescription = view.findViewById(R.id.tv_description);
    }

    public void bindViews(final Notification notification, final Activity activity) {
        tvDate.setText(Util.getTimeAgo(notification.getTimestamp()));

        String notificationDescription =
                String.format("%s %s", notification.getAuthor(), activity.getString(R.string.notification_current_user_text));
        tvDescription.setText(String.valueOf(notificationDescription));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("output", "sending post id: " +notification.getPostId());

                Bundle b = new Bundle();
                b.putString(Constants.EXTRA_POST_ID, notification.getPostId());

                IntentUtils.startActivity(activity, b, CommentsActivity.class);
            }
        });
    }
}
