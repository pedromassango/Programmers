package com.pedromassango.programmers.presentation.conversations.chat.adapter;

import android.os.Bundle;

import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.presentation.base.BaseContract;

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
