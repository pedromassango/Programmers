package com.pedromassango.programmers.mvp.post.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.mvp.adapters.ContextMenuAdapter;
import com.pedromassango.programmers.mvp.adapters.holders.PostVH;
import com.pedromassango.programmers.mvp.comments.activity.CommentsActivity;
import com.pedromassango.programmers.mvp.image.ViewImageDIalogFragment;
import com.pedromassango.programmers.mvp.post.edit.EditPostActivity;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;

import java.util.ArrayList;

/**
 * Created by JANU on 15/05/2017.
 */

public class PostsTests extends RecyclerView.Adapter<PostVH> implements Contract.View {

    public static final String TAG = "PostsAdapter";
    private ArrayList<Post> posts;

    private final Activity activity;
    private final float scale;
    private boolean notified;
    private IGetDataCompleteListener getDataCompleteListener;
    private ProgressDialog progressDialog;
    private AdapterPresenter adapterPresenter;

    public PostsTests(Activity activity, ArrayList<Post> posts, IGetDataCompleteListener getDataCompleteListener) {
        this.posts = posts;
        this.activity = activity;
        this.adapterPresenter = new AdapterPresenter(activity, this);
        this.scale = activity.getResources().getDisplayMetrics().density;
        this.getDataCompleteListener = getDataCompleteListener;
    }

    @Override
    public PostVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_post, parent, false);
        return (new PostVH(v));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onBindViewHolder(PostVH mHolder, int position) {

        mHolder.bindViews(adapterPresenter, posts.get(position));
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        Log.v(TAG, "registerAdapterDataObserver");
        if (null != getDataCompleteListener && !notified) {
            getDataCompleteListener.onGetDataSuccess();
            Log.v(TAG, "registerAdapterDataObserver - released");
            notified = true;
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

        IntentUtils.showFragment(activity,b,  new ViewImageDIalogFragment());
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

