package com.pedromassango.programmers.presentation.conversations.messages.adapter;

import android.content.Context;

import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.models.Message;

import static com.pedromassango.programmers.presentation.conversations.messages.adapter.MessageAdapter.IN_MSG;
import static com.pedromassango.programmers.presentation.conversations.messages.adapter.MessageAdapter.OUT_MSG;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

class Presenter implements Contract.Presenter {

    private Context context;
    private Contract.View view;

    Presenter(Context context, Contract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = view.getMessageItem(position);
        String userId = PrefsHelper.getId();

        return (msg.getAuthorId().equals(userId)) ? OUT_MSG : IN_MSG;
    }
}
