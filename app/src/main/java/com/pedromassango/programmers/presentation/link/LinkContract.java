package com.pedromassango.programmers.presentation.link;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.ISendDataListener;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.presentation.base.BaseContract;

/**
 * Created by Pedro Massango on 15/06/2017 at 16:18.
 */

public class LinkContract {

    interface Model {

        String getUserName();

        String getUserId();

        void publishLink(Link link, boolean update, ISendDataListener sendDataListener);

        void deleteLink(Link mLink, IErrorListener errorListener);

        void increaseViewsCount(Link mLink, IErrorListener errorListener);
    }

    public interface ViewAdapter {

        void showDialogAction(Link mLink, CharSequence[] actions);

        void startEdiLinkActivity(Bundle b);

        void showToastError();

        void showToastDeleteLink();
    }

    public interface PresenterAdapter extends BaseContract.PresenterImpl, IErrorListener {

        void onLinkClicked(Activity activity, Link link);

        void onLinkLongClicked(Activity activity, Link mLink);


        void onDialogActionItemClicked(int actionPosition, Link mLink);
    }

    public interface ViewActivity {

        String getSelectedCategory();

        String getDescription();

        String getUrl();

        void setDescriptionError(String error);

        void setURLError(String error);

        void showPublishProgress();

        void showToast(String message);

        void bindViews(Link link);
    }

    public interface PresenterActivity extends ISendDataListener {

        void initialize(Intent intent);

        Activity getContext();
    }

}
