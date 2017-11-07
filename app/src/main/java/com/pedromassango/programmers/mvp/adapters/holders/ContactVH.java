package com.pedromassango.programmers.mvp.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JANU on 23/05/2017.
 */

public class ContactVH extends RecyclerView.ViewHolder {

    public final CircleImageView imgUser;
    public final TextView tvUsername;
    public final TextView tvLastOnline;
    public final View onlineStatus;

    public ContactVH(View itemView) {
        super(itemView);
        imgUser = itemView.findViewById(R.id.img_user);
        tvUsername = itemView.findViewById(R.id.tv_author);
        tvLastOnline = itemView.findViewById(R.id.tv_last_online_status);
        onlineStatus = itemView.findViewById(R.id.view_online_status);
    }
}
