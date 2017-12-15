package com.pedromassango.programmers.presentation.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.Transations;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IRecyclerViewCategoryClickListener;
import com.pedromassango.programmers.interfaces.ISubscriptionCompleteListener;
import com.pedromassango.programmers.models.Category;
import com.pedromassango.programmers.models.Usuario;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;
import java.util.Map;

/**
 * Created by Pedro Massango on 07-01-2017 at 13:19.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> implements ISubscriptionCompleteListener {

    private Context context;
    private Usuario currentUser;
    private List<Category> drawerItems;
    private IRecyclerViewCategoryClickListener clickListener;
    private LayoutInflater inflater;
    private CategoryVH vh;

    public CategoryAdapter(Context context, Usuario currentUser, List<Category> drawerItems, IRecyclerViewCategoryClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.currentUser = currentUser;
        this.drawerItems = drawerItems;
        this.clickListener = clickListener;
    }

    @Override
    public CategoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_category, parent, false);
        vh = new CategoryVH(view);
        return (vh);
    }

    @Override
    public void onBindViewHolder(CategoryVH holder, int position) {
        Category category = drawerItems.get(position);

        holder.tvTitle.setText(category.getTitle());
        holder.tvIcon.setText(category.getSimpleName());
        holder.checkNotifyStatus( position);
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    class CategoryVH extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvIcon;
        private ImageView img;

        CategoryVH(View v) {
            super(v);
            tvIcon = v.findViewById(R.id.tv_category);
            tvIcon.setTypeface(EasyFonts.caviarDreamsBold(context));
            tvTitle = v.findViewById(R.id.tv_title);
            img = v.findViewById(R.id.img_icon);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getAdapterPosition() != 0) {
                        // Handle user to subscribe to topic with their categories selected
                        showDialogActions();
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onCategoryClicked(getAdapterPosition(), drawerItems.get(getAdapterPosition()));
                }
            });
        }

        private void showDialogActions() {
            final int position = getAdapterPosition();
            Category oCategory = drawerItems.get(position);
            final String category = CategoriesUtils.getCategoryTopic(oCategory.getTitle());
            final boolean canNotify = canNotify(position);

            // Check to handle witch message we need to show
            CharSequence[] actions = {canNotify ?
                    context.getString(R.string.not_receive_notifications)
                    :
                    context.getString(R.string.receive_notifications)};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true)
                    .setTitle(tvTitle.getText())
                    .setItems(actions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (canNotify) {
                                Transations.handleUserSubscriptionInCategory(category, false, position, CategoryAdapter.this);
                            } else {
                                Transations.handleUserSubscriptionInCategory(category, true,position, CategoryAdapter.this);
                            }

                            // This will update the current state of the category
                            checkNotifyStatus(position);
                        }
                    });

            builder.create()
                    .show();
        }

        boolean canNotify(int position) {
            Category category = drawerItems.get(position);
            Map<String, Boolean> userFavoritesCategory = currentUser.getFavoritesCategory();
            String categoryName = CategoriesUtils.getCategoryTopic(category.getTitle());

            return userFavoritesCategory.containsKey(categoryName);
        }

        void checkNotifyStatus(int position) {
            if (canNotify( position)) {
                img.setVisibility(View.VISIBLE);
                tvIcon.setVisibility(View.GONE);
            } else {
                img.setVisibility(View.GONE);
                tvIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError() {

        Util.showToast(context, R.string.something_was_wrong);
    }

    @Override
    public void onComplete(String category, boolean subscribed, int position) {
        if (subscribed)
            currentUser.addCategory(category);
        else
            currentUser.removeCategory(category);

        vh.checkNotifyStatus( position);

        // update user data localy
        RepositoryManager.getInstance()
                .getUsersRepository()
                .updateLoggedUser(currentUser);
    }
}