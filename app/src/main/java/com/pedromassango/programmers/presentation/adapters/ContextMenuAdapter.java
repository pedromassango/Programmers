package com.pedromassango.programmers.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.ContextMenuItem;
import com.pedromassango.programmers.presentation.adapters.holders.ContextMenuVH;

import java.util.List;

/**
 * Created by Pedro Massango on 05-02-2017 20:11.
 */

public class ContextMenuAdapter extends BaseAdapter {

    private final List<ContextMenuItem> items;
    private final LayoutInflater inflater;

    public ContextMenuAdapter(Context context, List<ContextMenuItem> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContextMenuItem menuItem = items.get(i);

        ContextMenuVH rowHolder;
        if (view == null) {
            rowHolder = new ContextMenuVH();
            view = inflater.inflate(R.layout.row_context_menu, viewGroup, false);

            rowHolder.imageView = view.findViewById(R.id.img_icon);
            rowHolder.textView = view.findViewById(R.id.tv_label);

            view.setTag(rowHolder);
        } else {
            rowHolder = (ContextMenuVH) view.getTag();
        }

        rowHolder.imageView.setImageResource(menuItem.getIcon());
        rowHolder.textView.setText(menuItem.getLabel());

        return view;
    }
}
