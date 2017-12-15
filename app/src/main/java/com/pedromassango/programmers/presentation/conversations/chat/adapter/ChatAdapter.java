package com.pedromassango.programmers.presentation.conversations.chat.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.presentation.adapters.holders.ContactVH;
import com.pedromassango.programmers.presentation.conversations.messages.MessagesActivity;
import com.pedromassango.programmers.presentation.image.ViewImageDIalogFragment;
import com.pedromassango.programmers.presentation.profile.profile.ProfileActivity;

/**
 * Created by JANU on 23/05/2017.
 */

public class ChatAdapter extends FirebaseRecyclerAdapter<Contact, ContactVH> implements Contract.View {

    private Activity activity;
    private Presenter presenter;
    private boolean notified;
    private IGetDataCompleteListener getPostsCompleteListener;

    public ChatAdapter(Activity activity, DatabaseReference ref) {
        super(Contact.class, R.layout.row_chat, ContactVH.class, ref);
        this.activity = activity;
        this.presenter = new Presenter(this, activity);
    }

    public ChatAdapter(Activity activity, Query ref, IGetDataCompleteListener getPostsCompleteListener) {
        super(Contact.class, R.layout.row_chat, ContactVH.class, ref);

        this.activity = activity;
        this.getPostsCompleteListener = getPostsCompleteListener;
        this.presenter = new Presenter(this, activity);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);

        if (notified)
            return;

        getPostsCompleteListener.onGetDataSuccess();
        notified = true;
    }

    @Override
    protected void populateViewHolder(ContactVH cv, Contact mContact, int position) {
        final ContactVH h = cv;
        final Contact contact = mContact;

        h.tvUsername.setText(contact.getUsername());

        if (contact.isOnline()) {
            if (presenter.isTheCurrentUser(contact.getUserId())) {
                h.tvLastOnline.setVisibility(View.VISIBLE);
                h.tvLastOnline.setText(R.string.you);
                h.onlineStatus.setVisibility(View.GONE);
            } else {
                h.tvLastOnline.setVisibility(View.GONE);
                h.onlineStatus.setVisibility(View.VISIBLE);
            }
        } else {
            h.onlineStatus.setVisibility(View.GONE);
            h.tvLastOnline.setVisibility(View.VISIBLE);
            h.tvLastOnline.setText(Util.getTimeAgo(contact.getLastOnline()));
        }

        //Load User image
        ImageUtils.loadImageUser(activity, contact.getUserUrlPhoto(), h.imgUser);

        h.imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onImageUserClicked(contact.getUserUrlPhoto());
            }
        });

        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onContactClicked(contact);
            }
        });
    }

    // Contact clicked
    @Override
    public void startMessageActivity(Bundle data) {

        IntentUtils.startActivity(activity, data, MessagesActivity.class);
        //Toast.makeText(activity, R.string.funtionality_off, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startProfileActivity(Bundle b) {

        IntentUtils.startActivity(activity, b, ProfileActivity.class);
    }

    @Override
    public void startViewImageActivity(Bundle b) {

        IntentUtils.showFragment(activity, b, new ViewImageDIalogFragment());
    }
}
