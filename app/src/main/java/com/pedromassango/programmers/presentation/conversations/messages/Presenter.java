package com.pedromassango.programmers.presentation.conversations.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.MessageRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.Transations;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.server.Library;

import static com.pedromassango.programmers.extras.Constants.EXTRA_FRIEND_CONTACT;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class Presenter implements Contract.Presenter, IPresenceLIstener, Callbacks.IRequestCallback {

    private Contract.View view;
    private MessageRepository repository;
    private Contact friendContact;

    public Presenter(Contract.View view) {
        this.view = view;
        this.repository = RepositoryManager.getInstance()
                .getMessageRepository();
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize(Intent intent, Bundle savedInstanceState) {

        friendContact = intent.getParcelableExtra(EXTRA_FRIEND_CONTACT);

        view.setActivityTitle(friendContact.getUsername());
        view.handleMessages(friendContact.getUserId());

        Transations.handleUserPresence(friendContact.getUserId(), this);
    }

    @Override
    public void onSendMessageClicked() {
        String text = view.getMessage();

        if (text.trim().length() < 0) {
            view.showToast(getContext().getString(R.string.empty_message));
            return;
        }

        Message message = new Message();
        message.setText(text);
        message.setId(Library.generateId());
        message.setAuthorId(PrefsHelper.getId());
        message.setAuthor(PrefsHelper.getName());
        message.setReceiverId(friendContact.getUserId());
        message.setReceiverName(friendContact.getUsername());
        message.setTimestamp(System.currentTimeMillis());

        repository.send(message, this);
    }


    @Override
    public void onSuccess() {

        // Clear the current message
        view.clearTypedText();
    }

    @Override
    public void onError() {

        view.showToast(getContext().getString(R.string.something_was_wrong));
    }


    @Override
    public void onFriendOnline(boolean online) {

        int visibility = online ? View.VISIBLE : View.GONE;
        view.setFriendOnlineIconVisibility(visibility);
    }

    @Override
    public void onFriendOffline(String lastOnline) {

        view.setActivitySubtitle(lastOnline);
    }
}
