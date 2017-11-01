package com.pedromassango.programmers.mvp.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by Pedro Massango on 23-02-2017 11:16.
 */

class Contract {

    interface View {

        void showImage(Bitmap bitmap);

        void showImage(String imgURL);

        Bitmap getBitmapImage();
    }

    interface Presenter {
        Context getContext();

        void initialize(Bundle intent, Bundle bundle);

        void onSaveInstanceState(Bundle outState);
    }
}
