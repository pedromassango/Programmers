package com.pedromassango.programmers.mvp.conversations.messages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.mvp.adapters.holders.MessageVH;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageVH> implements Contract.View {

    static final int OUT_MSG = 0;
    static final int IN_MSG = 1;
    private Presenter presenter;

    public MessageAdapter(Context context, DatabaseReference messagesRef) {
        super(Message.class, R.layout.row_message_in, MessageVH.class, messagesRef);
        presenter = new Presenter(context, this);
    }

    @Override
    public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = (viewType == OUT_MSG) ?
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message_out, parent, false)
                :
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message_in, parent, false);

        return (new MessageVH(view));
    }

    @Override
    public int getItemViewType(int position) {

        return presenter.getItemViewType(position);
    }

    @Override
    public Message getMessageItem(int position) {

        //TODO: bug here
        return getItem(position);
    }

    @Override
    protected void populateViewHolder(MessageVH viewHolder, Message model, int position) {

        viewHolder.bindViews(model);
    }
}
