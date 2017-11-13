package com.pedromassango.programmers.extras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pedromassango.programmers.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    /**
     * Load User images only
     *
     * @param context
     * @param urlPhoto
     * @param imageView
     */
    public static void loadImageUser(Context context, String urlPhoto, ImageView imageView) {
        Log.v(TAG, "loadImageUser: " + urlPhoto);
        if (!Util.validImageUrl(urlPhoto)) {
            imageView.setImageResource(R.drawable.ic_user);
            return;
        }
        Picasso.with(context)
                .load(urlPhoto)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.error)
                .into(imageView);
    }

    public static void loadCoverImage(Context context, String urlPhoto, ImageView imageView) {
        Log.v(TAG, "loadImageUser: " + urlPhoto);
        if (Util.validImageUrl(urlPhoto))
            Picasso.with(context)
                    .load(urlPhoto)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.error)
                    .into(imageView);
    }

    public static Bitmap loadImageUser(final Context activity, final String urlPhoto) throws IOException {
        Log.v(TAG, "loadImageUser: " + urlPhoto);
        if (!Util.validImageUrl(urlPhoto)) {
            return BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_user);
        }

        return Picasso.with(activity)
                .load(urlPhoto)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .get();
    }

    /**
     * Load post's image only
     *
     * @param activity  the context to show the image
     * @param urlPhoto  the url to load
     * @param imageView the view to set the image when ready
     */

    public static void loadPostImage(final Context activity, final String urlPhoto, final ImageView imageView) {
        Log.v(TAG, "loadPostImage: " + urlPhoto);
        if (!Util.validImageUrl(urlPhoto)) {
            imageView.setVisibility(View.GONE);
            return;
        }

        Picasso.with(activity)
                .load(urlPhoto)
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(imageView);
    }

    public static void loadPostImage(final Activity activity, Uri urlPhoto, final ImageView imageView) {
        Log.v(TAG, "loadPostImage: " + urlPhoto);

        Picasso.with(activity)
                .load(urlPhoto)
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(imageView);
    }

    public static Bitmap loadPostImage(Context context, String url) throws IOException {
        Log.v(TAG, "loadPostImage: " + url);

        return Picasso
                .with(context)
                .load(url)
                .get();
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Log.v(TAG, "loadImage: " + url);

        if (!Util.validImageUrl(url)) {
            return;
        }

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.error)
                .into(imageView);
    }
}
