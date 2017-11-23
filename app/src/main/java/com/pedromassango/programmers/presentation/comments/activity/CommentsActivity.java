package com.pedromassango.programmers.presentation.comments.activity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.presentation.comments.adapter.CommentAdapter;
import com.pedromassango.programmers.server.Library;

public class CommentsActivity extends BaseActivity implements Contract.View {

    private EditText edtText;
    private TextView tvEmpty;
    private TextView tvViews, tvDate, tvBody, tvLikes, tvCategory, tvTitle;
    private RecyclerView recyclerView;

    private CommentAdapter adapter;
    private Presenter presenter;

    @Override
    public int layoutResource() {
        return R.layout.activity_comments;
    }

    @Override
    public void initializeViews() {

         FrameLayout frameLayout = findViewById(R.id.frame_info_container);

        tvTitle = findViewById(R.id.tv_title);
        tvBody = findViewById(R.id.tv_body);
        tvDate = findViewById(R.id.tv_date);
        tvLikes = findViewById(R.id.tv_up_votes);
        tvViews = findViewById(R.id.tv_views);
        tvEmpty = frameLayout.findViewById(R.id.tv_empty);
        tvCategory = findViewById(R.id.tv_category);

        recyclerView = frameLayout.findViewById(R.id.recycler_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        edtText = findViewById(R.id.edt_comment);
        edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    presenter.onSendCommentClicked();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new Presenter(this);
        presenter.initialize(getIntent(), savedInstanceState);
    }

    @Override
    public void bindViews(String author, String category, String title, String body, String likes, String views, String date) {

        //this.setTitle(author);
        tvTitle.setText(title);
        tvBody.setText(body);
        tvLikes.setText(likes);
        tvViews.setText(views);
        tvCategory.setText(category);
        tvDate.setText(date);
    }

    @Override
    public void fetchComments(String postId) {
        tvEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter = new CommentAdapter(this, Library.getCommentsRef(postId), presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setEditTextVisibility(int visibility) {

        edtText.setVisibility(visibility);
    }

    @Override
    public void showGetPostError() {
        showAlertDialog(R.string.ops, R.string.post_not_found_message);
    }

    @Override
    public String getComment() {
        return edtText.getText().toString();
    }

    @Override
    public void setCommentError(@StringRes int empty_text) {

        edtText.setError(getString(empty_text));
    }

    @Override
    public void showTextLoading() {
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText(getString(R.string.a_carregar));
    }

    @Override
    public void showRecyclerviewComments() {

        tvEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.scrollToPosition( adapter.getItemCount()-1);
    }

    @Override
    public void onSendError() {

    }

    @Override
    public void onSendCommentSuccess() {

        edtText.setText("");
        recyclerView.scrollToPosition( adapter.getItemCount()-1);
    }
}
