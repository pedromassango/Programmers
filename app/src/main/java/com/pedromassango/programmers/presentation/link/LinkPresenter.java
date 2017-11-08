package com.pedromassango.programmers.presentation.link;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.IntentUtils;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 15/06/2017 at 16:17.
 */

public class LinkPresenter implements LinkContract.PresenterActivity, LinkContract.PresenterAdapter {

    private LinkContract.ViewActivity viewActivity;
    private LinkContract.ViewAdapter viewAdapter;
    private LinkModel model;
    private Link link = new Link();
    private boolean update;

    public LinkPresenter(LinkContract.ViewActivity viewActivity) {

        this.viewActivity = viewActivity;
        model = new LinkModel(this);
    }

    public LinkPresenter(LinkContract.ViewAdapter viewAdapter) {

        this.viewAdapter = viewAdapter;
        model = new LinkModel(this);
    }

    @Override
    public void initialize(Intent intent) {
        if (intent != null && intent.hasExtra(Constants.EXTRA_LINK)) {
            link = intent.getParcelableExtra(Constants.EXTRA_LINK);
            viewActivity.bindViews(link);
            update = true;
        }
    }

    @Override
    public Activity getContext() {

        return (Activity) viewActivity;
    }

    public void onPublishClicked() {
        String category = viewActivity.getSelectedCategory();
        String description = viewActivity.getDescription();
        String url = viewActivity.getUrl();

        if (description.trim().length() == 0) {
            viewActivity.setDescriptionError(getContext().getString(R.string.empty_description));
            viewActivity.showToast(getContext().getString(R.string.empty_description));
            return;
        }

        if (description.length() < 10) {
            viewActivity.setDescriptionError(getContext().getString(R.string.description_less));
            return;
        }

        if (url.trim().length() == 0) {
            viewActivity.setURLError(getContext().getString(R.string.url_empty));
            return;
        }

        if (!Patterns.WEB_URL.matcher(url).matches()) {
            viewActivity.setURLError(getContext().getString(R.string.url_invalid));
            viewActivity.showToast(getContext().getString(R.string.url_invalid));
            return;
        }

        // If come from update, do not need to change the time
        if(!update) {
            link.setTimestamp(System.currentTimeMillis());
        }

        link.setDescription(description);
        link.setUrl(url);
        link.setViews(0);
        link.setCategory(category);
        link.setAuthor(model.getUserName());
        link.setAuthorId(model.getUserId());
        viewActivity.showPublishProgress();
        model.publishLink(link, update, this);
    }

    @Override
    public void onSendSuccess() {

        getContext().finish();
    }

    @Override
    public void onSendError() {

        viewActivity.showToast(getContext().getString(R.string.something_was_wrong));
    }


    /*
        Implementations for @LinkAdapter
     */
    @Override
    public void onLinkClicked(Activity activity, Link mLink) {

        model.increaseViewsCount(mLink,  this);
        IntentUtils.startBrowserIntent(activity, mLink.getUrl());
    }

    @Override
    public void onLinkLongClicked(Activity activity, Link mLink) {
        showLog("onLinkLongClicked()");

        if (model.getUserId().equals(mLink.getAuthorId())) {
            showLog("same user id - show dialog");
            CharSequence[] actions = {"Editar", "Eliminar"};
            viewAdapter.showDialogAction(mLink, actions);
        }
        showLog("diferent user id - do nothing");
    }

    @Override
    public void onDialogActionItemClicked(int actionPosition, Link mLink) {
        switch (actionPosition) {
            case 0:
                Bundle b = new Bundle();
                b.putParcelable(Constants.EXTRA_LINK, mLink);
                viewAdapter.startEdiLinkActivity(b);
                break;

            case 1:
                viewAdapter.showToastDeleteLink();
                model.deleteLink(mLink, this);
                break;
        }
    }

    @Override
    public void onError() {

        viewAdapter.showToastError();
    }
}
