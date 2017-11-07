package com.pedromassango.programmers.mvp.conversations.messages.adapter;

import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.mvp.conversations.ConversationContract;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

class Contract {

    interface View{

        Message getMessageItem(int position);
    }

    interface Presenter extends BaseContract.PresenterImpl{

        int getItemViewType(int position);
    }
}
