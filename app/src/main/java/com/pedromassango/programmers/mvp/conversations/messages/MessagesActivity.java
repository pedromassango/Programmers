package com.pedromassango.programmers.mvp.conversations.messages;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.mvp.base.activity.BaseActivity;
import com.pedromassango.programmers.mvp.conversations.messages.adapter.MessageAdapter;
import com.pedromassango.programmers.server.Library;

public class MessagesActivity extends BaseActivity implements Contract.View {

    // MVP
    private Presenter presenter;

    // Adapter
    private MessageAdapter messageAdapter;

    // Views
    private RecyclerView recyclerView;
    private EditText edtMessage;
    private View vFriendOnlineStatus;

    @Override
    protected int layoutResource() {
        return R.layout.activity_messages;
    }

    @Override
    protected void initializeViews() {

        //RecyclerView
        vFriendOnlineStatus = findViewById(R.id.view_online_status);
        recyclerView = findViewById(R.id.recycler_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        edtMessage = findViewById(R.id.edt_message);
        edtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    presenter.onSendMessageClicked();
                    handled = true;
                }
                return handled;
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
    public String getMessage() {

        return edtMessage.getText().toString();
    }

    @Override
    public void clearTypedText() {

        edtMessage.setText("");
    }

    @Override
    public void setFriendOnlineIconVisibility(int visibility) {

        vFriendOnlineStatus.setVisibility(visibility);
    }

    @Override
    public void setActivityTitle(String friendUsername) {

        toolbar.setTitle(friendUsername);
    }

    @Override
    public void setActivitySubtitle(String lastOnline) {

        toolbar.setSubtitle(lastOnline);
    }

    @Override
    public void showToast(String message) {

        super.showToastMessage(message);
    }

    @Override
    public void handleMessages(String friendId) {

        DatabaseReference userAndFriendMessagesRef = Library.getMessagesRef(friendId);
        messageAdapter = new MessageAdapter(this, userAndFriendMessagesRef);
        recyclerView.setAdapter(messageAdapter);
    }
}
