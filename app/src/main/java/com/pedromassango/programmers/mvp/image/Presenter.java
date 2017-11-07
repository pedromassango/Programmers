package com.pedromassango.programmers.mvp.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.pedromassango.programmers.extras.Constants;

/**
 * Created by Pedro Massango on 23-02-2017 11:22.
 */

class Presenter implements Contract.Presenter {
    private Contract.View view;

    public Presenter(Contract.View view) {
        this.view = view;
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize(Bundle intent, Bundle bundle) {

        // If we have an instance saved, just get it, and show again
        if (bundle != null) {
            Bitmap bmp = bundle.getParcelable(Constants.EXTRA_IMAGE);
            view.showImage(bmp);
            return;
        }

        // If we received an URL, load the image
        if (intent.containsKey(Constants.EXTRA_IMAGE_URL)) {
            String imageUrl = intent.getString(Constants.EXTRA_IMAGE_URL);
            view.showImage(imageUrl);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Bitmap bmp = view.getBitmapImage();
        outState.putParcelable(Constants.EXTRA_IMAGE, bmp);
    }


}
