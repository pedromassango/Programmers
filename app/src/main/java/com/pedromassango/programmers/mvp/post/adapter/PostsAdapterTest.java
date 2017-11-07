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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.mvp.adapters.ContextMenuAdapter;
import com.pedromassango.programmers.mvp.adapters.holders.PostVH;
import com.pedromassango.programmers.mvp.comments.activity.CommentsActivity;
import com.pedromassango.programmers.mvp.image.ViewImageDIalogFragment;
import com.pedromassango.programmers.mvp.post.edit.EditPostActivity;

import java.util.List;

/**
 * Created by JANU on 15/05/2017.
 */

public class PostsAdapterTest extends RecyclerView.Adapter<PostVH> implements Contract.View, ChildEventListener {

    public static final String TAG = "PostsAdapter";

    private final Activity activity;
    private final float scale;
    private List<Post> mPosts;
    private IGetDataCompleteListener getDataCompleteListener;
    private ProgressDialog progressDialog;
    private AdapterPresenter adapterPresenter;

    public PostsAdapterTest(Activity activity, DatabaseReference ref) {
        this.activity = activity;
        this.adapterPresenter = new AdapterPresenter(activity, this);
        this.scale = activity.getResources().getDisplayMetrics().density;

        //Listen on list of posts
        ref.addChildEventListener(this);
    }

    @Override
    public PostVH onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(activity).inflate(R.layout.row_post, parent, false);
        return (new PostVH(v));
    }

    @Override
    public void onBindViewHolder(PostVH holder, int position) {

        Post p = mPosts.get(position);
        holder.bindViews(adapterPresenter, p);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
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

    // Firebase
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Post p = dataSnapshot.getValue(Post.class);
        mPosts.add(p);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

//        Post p = dataSnapshot.getValue(Post.class);
  //      mPosts.add(p);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}

