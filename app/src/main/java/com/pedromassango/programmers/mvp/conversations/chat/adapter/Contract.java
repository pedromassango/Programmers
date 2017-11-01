package com.pedromassango.programmers.mvp.conversations.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.mvp.base.BaseContract;

/**
 * Created by JANU on 23/05/2017.
 */

public class Contract {

    public interface Model{

    }

    public interface View{

        void startMessageActivity(Bundle data);

        void startViewImageActivity(Bundle b);

        void startProfileActivity(Bundle b);
    }

    public interface Presenter extends BaseContract.PresenterImpl{

        void onContactClicked(Contact contact);

        void onImageUserClicked(String imageUrl);

        boolean isTheCurrentUser(String userId);
    }

    public interface OnContactClickdListener{
        void OnContactClick();
    }
}
