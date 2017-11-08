package com.pedromassango.programmers.presentation.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.extras.Util;

/**
 * Created by Pedro Massango on 14/06/2017 at 18:58.
 */

public class LinkVH extends RecyclerView.ViewHolder {

    private final TextView tvTitle;
    private final TextView tvAuthor;
    private final TextView tvDate;
    public final View rootView;
    private final TextView tvCategory;
    private final TextView tvViews;
    private final TextView tvLink;

    public LinkVH(View view) {
        super(view);
        rootView = view.findViewById(R.id.root_view);
        tvTitle = view.findViewById(R.id.tv_title);
        tvAuthor = view.findViewById(R.id.tv_author);
        tvDate = view.findViewById(R.id.tv_date);
        tvLink = view.findViewById(R.id.tv_link);
        tvCategory = view.findViewById(R.id.tv_category);
        tvViews = view.findViewById(R.id.tv_views);
    }

    public void bindViews(Link link) {
        tvTitle.setText(link.getDescription());
        tvAuthor.setText(link.getAuthor());
        tvLink.setText(link.getUrl());
        tvCategory.setText(link.getCategory());
        tvDate.setText(Util.getTimeAgo(link.getTimestamp()));
        tvViews.setText(String.valueOf(link.getViews()));
    }
}
