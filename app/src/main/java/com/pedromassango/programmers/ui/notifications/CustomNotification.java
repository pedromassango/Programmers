package com.pedromassango.programmers.ui.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Comment;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.comments.activity.CommentsActivity;
import com.pedromassango.programmers.presentation.main.activity.MainActivity;

import java.io.IOException;

/**
 * Created by Pedro Massango on 04/06/2017.
 */

public class CustomNotification {


    private Context context;
    private int id = (int) System.currentTimeMillis() / 100;
    private NotificationCompat.Builder builder;


    public CustomNotification create(Context context) {
        this.context = context;
        builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        return this;
    }

    public CustomNotification setType(Post post) {
        String title = context.getString(R.string.posts);
        String body = Util.concat(post.getAuthor(), context.getString(R.string.published), post.getBody());

        setTitleAndText(title, body, true);

        // Intent that will open MainActivity when tha Notification
        // is clicked
        Intent intent = IntentUtils.getIntent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Button Comment

        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_POST, post);
        pendingIntent = getCommentPendingIntent(b);
        builder.addAction(R.drawable.ic_comment, context.getString(R.string.comment), pendingIntent);
        return this;
    }

    public CustomNotification setType(Comment comment) {

        String author = Util.getFirstName(comment.getAuthor());
        String title = context.getString(R.string.comments);

        // Check if the post author is the
        String currentUserId = PrefsHelper.getName();
        String text = "";
        /*String text = comment.getPostAuthorId().equals(currentUserId) ?
                Util.concat(author, context.getString(R.string.notification_current_user_text))
                :
                Util.concat(author, context.getString(R.string.notification_non_current_user_text));
*/
        setTitleAndText(title, text, false);

        // Loading the sender image and set it as LargeIcon Notification
        loadSenderImage(comment.getAuthorUrlPhoto());

        Bundle b = new Bundle();
        b.putString(Constants.EXTRA_POST_ID, comment.getPostId());
        PendingIntent pendingIntent = getCommentPendingIntent(b);
        builder.setContentIntent(pendingIntent);
        return this;
    }

    private void loadSenderImage(String urlPhoto) {
        Bitmap bmp = null;
        try {
            bmp = ImageUtils.loadImageUser(context, urlPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.setLargeIcon(bmp);
    }

    private void setTitleAndText(String title, String body, boolean isPost) {
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setTicker(body);

        if (isPost) {
            builder.setSmallIcon(R.drawable.ic_post);
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(builder);
            bigTextStyle.setBigContentTitle(title);
            bigTextStyle.setSummaryText(body);
            builder.setStyle(bigTextStyle);
        } else {
            builder.setSmallIcon(R.drawable.ic_comment);
        }
    }

    private PendingIntent getCommentPendingIntent(Bundle b) {
        // Intent to open Comments Activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(CommentsActivity.class);

        Intent commentsIntent = IntentUtils.getIntent(context, CommentsActivity.class);
        commentsIntent.putExtras(b);
        stackBuilder.addNextIntent(commentsIntent);

        // Set the pendingIntent on Notification.Builder
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, id, commentsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void show() {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}
