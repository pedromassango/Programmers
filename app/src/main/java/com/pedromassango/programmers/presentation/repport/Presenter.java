package com.pedromassango.programmers.presentation.repport;

import android.content.Context;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Bug;

/**
 * Created by Pedro Massango on 05-03-2017 20:06.
 */

class Presenter implements Contract.Presenter, Contract.OnSendBugListener {

    private Contract.View view;
    private Model model;
    private Bug bugToSend;


    Presenter(Contract.View view) {
        this.view = view;
        this.model = new Model(this);
    }

    public Context getContext() {

        return (Context) view;
    }

    @Override
    public void sendBugClicked() {

        String description = view.getDescription();
        boolean attachEmail = view.getAttachEmail();

        if (description.trim().isEmpty()) {
            view.setDescriptionError(R.string.empty_bug_description);
            return;
        }

        if (description.trim().length() < 15) {
            view.setDescriptionError(R.string.less_description);
            return;
        }

        bugToSend = new Bug();

        if (attachEmail) {
            String userId = model.getUserId();
            bugToSend.setAuthorId(userId);
        }

        bugToSend.setAuthor(model.getUsername());
        bugToSend.setDescription(description);
        bugToSend.setTimestamp(System.currentTimeMillis());

        //Sending bug...
        view.showProgess(R.string.sending_bug);
        model.sendBug(this, bugToSend);
    }

    @Override
    public void onSuccess() {
        view.dismissProgress();
        view.onResultSuccess();
    }

    @Override
    public void onError(String error) {
        view.dismissProgress();
        view.showFailDialog(error, this);
    }

    @Override
    public void onRetry() {
        //Sending bug...
        view.showProgess(R.string.sending_bug);
        model.sendBug(this, bugToSend);
    }
}
