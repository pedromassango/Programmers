package com.pedromassango.programmers.presentation.post.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.presentation.adapters.ContextMenuAdapter;
import com.pedromassango.programmers.presentation.adapters.holders.PostVH;
import com.pedromassango.programmers.presentation.comments.activity.CommentsActivity;
import com.pedromassango.programmers.presentation.image.ViewImageDIalogFragment;
import com.pedromassango.programmers.presentation.post.edit.EditPostActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JANU on 15/05/2017.
 */

public class PostsAdapterSimple extends RecyclerView.Adapter<PostVH> implements Contract.View {

    public static final String TAG = "PostsAdapterSimple";

    private List<Post> posts;

    private final Activity activity;
    private final float scale;
    // Save the notify status of IGetDataCompleteListener.
    private ProgressDialog progressDialog;
    private AdapterPresenter adapterPresenter;

    public PostsAdapterSimple(Activity activity) {
        this.activity = activity;
        this.posts = new ArrayList<>();
        this.adapterPresenter = new AdapterPresenter(activity, this);
        this.scale = activity.getResources().getDisplayMetrics().density;
    }

    @Override
    public void update(Post result, int position) {
        synchronized (PostsAdapterSimple.class){
            posts.remove(result);
            posts.add(position, result);
            notifyDataSetChanged();
        }
    }

    @Override
    public void delete(Post result, int position) {
        synchronized (PostsAdapterSimple.class){
            posts.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public PostVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_post, viewGroup, false);
        return (new PostVH(v));
    }

    @Override
    public void onBindViewHolder(PostVH postVH, int i) {
        Post p = posts.get(i);
        postVH.bindViews(adapterPresenter, p);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void add(List<Post> posts){
        synchronized (PostsAdapterSimple.class) {
            this.posts.clear();
            this.posts.addAll( posts);

            notifyDataSetChanged();
        }
    }

    @Override
    public void showToast(String message) {

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog(String string) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(string);
        progressDialog.show();
    }

    @Override
    public void showAlertDialog(String error) {

        Util.showAlertDialog(activity, activity.getString(R.string.error), error);
    }

    @Override
    public void dismissProgressDialog() {

        progressDialog.dismiss();
    }

    //EVENT CLICKS

    @Override
    public void startCommentsActivity(Bundle b) {

        IntentUtils.startActivity(activity, b, CommentsActivity.class);
    }

    @Override
    public void onPostImageClicked(Bundle b) {

        IntentUtils.showFragment(activity, b, new ViewImageDIalogFragment());
    }

    @Override
    public void openMenuOption(View vOptions, final Post post, final int currentPostPosition, ContextMenuAdapter menuAdapter) {

        final ListPopupWindow popupWindow = new ListPopupWindow(activity);
        popupWindow.setAnchorView(vOptions);
        popupWindow.setWidth((int) (240 * scale + 0.5f));
        popupWindow.setAdapter(menuAdapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemClickedPosition, long l) {

                adapterPresenter.onContextMenuItemClicked(itemClickedPosition, post);
                popupWindow.dismiss();
            }
        });

        popupWindow.setModal(true);
        popupWindow.show();
    }

    @Override
    public void startEditPostActivity(Bundle bundle) {

        IntentUtils.startActivity(activity, bundle, EditPostActivity.class);
    }
}

