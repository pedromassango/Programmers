package com.pedromassango.programmers.mvp.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Message;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class MessageVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    private boolean tvDateVisible = false;
    private TextView tvMessage;
    private TextView tvDate;

    public MessageVH(View itemView) {
        super(itemView);

        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvMessage.setOnClickListener(this);

        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
    }

    public void bindViews(Message message) {

        tvMessage.setText(message.getText());
        String date = Util.getTimeAgo(message.getTimestamp());
        tvDate.setText(date);
    }

    /**
     * Handle click on Message TextView
     * to hide/show the TextView date
     * just for fun xD
     *
     * @param v the view that was clicked { tvMessage }
     */
    @Override
    public void onClick(View v) {

        tvDate.setVisibility(tvDateVisible ? View.GONE : View.VISIBLE);
        tvDateVisible = !tvDateVisible;
    }
}
