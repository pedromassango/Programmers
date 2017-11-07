package com.pedromassango.programmers.mvp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IRecyclerViewCategoryClickListener;
import com.pedromassango.programmers.models.Category;

import java.util.List;

/**
 * Created by Pedro Massango on 07-01-2017 at 13:19.
 */

public class CategoryAdapterListView extends ArrayAdapter<Category> {

    private Context context;
    private final List<Category> drawerItems;
    private IRecyclerViewCategoryClickListener clickListener;
    private LayoutInflater inflater;

    public CategoryAdapterListView(Context context, List<Category> drawerItems, IRecyclerViewCategoryClickListener clickListener) {
        super(context, R.layout.row_category);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.drawerItems = drawerItems;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @NonNull
    @Override
    public View getView(int mPosition, View convertView, ViewGroup parent) {
        final int position = mPosition;
        final Category category = drawerItems.get(position);

        CategoryVH holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_category, null);
            holder = new CategoryVH(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CategoryVH) convertView.getTag();
        }

        if (position == 0 || position == 1) {
            holder.tvIcon.setText("");
            holder.tvIcon.setBackgroundResource(category.getIcon());
            return convertView;
        }

        if (category.getTitle().equalsIgnoreCase(context.getString(R.string.terminar_sess_o))) {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.white));
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            return convertView;
        }

        holder.tvIcon.setText(category.getSimpleName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListener.onCategoryClicked(position, category);
            }
        });

        return convertView;
    }

    class CategoryVH {
        public View itemView;
        public TextView tvTitle;
        public TextView tvIcon;

        CategoryVH(View v) {
            itemView = v;
            tvIcon = v.findViewById(R.id.tv_category);
            tvTitle = v.findViewById(R.id.tv_title);
        }
    }
}
