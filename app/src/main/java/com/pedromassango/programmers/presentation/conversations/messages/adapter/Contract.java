package com.pedromassango.programmers.presentation.conversations.messages.adapter;

import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.presentation.base.BaseContract;

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
