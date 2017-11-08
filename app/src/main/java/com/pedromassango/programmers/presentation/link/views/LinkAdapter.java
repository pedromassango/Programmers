package com.pedromassango.programmers.presentation.link.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.presentation.adapters.holders.LinkVH;
import com.pedromassango.programmers.presentation.link.LinkContract;
import com.pedromassango.programmers.presentation.link.LinkPresenter;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;

import static com.pedromassango.programmers.extras.Util.showLog;

public class LinkAdapter extends FirebaseRecyclerAdapter<Link, LinkVH> implements LinkContract.ViewAdapter {

    private Activity activity;
    private boolean notified;
    private LinkPresenter presenter;
    private IGetDataCompleteListener getDataCompleteListener;

    public LinkAdapter(Activity actvivity, DatabaseReference linksRef, IGetDataCompleteListener getDataCompleteListener) {
        super(Link.class, R.layout.row_link, LinkVH.class, linksRef);
        this.activity = actvivity;
        this.presenter = new LinkPresenter(this);
        this.getDataCompleteListener = getDataCompleteListener;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);

        if (null != getDataCompleteListener && !notified) {
            getDataCompleteListener.onGetDataSuccess();
            notified = true;
        }
    }

    @Override
    protected void populateViewHolder(LinkVH viewHolder, final Link model, int position) {

        // Fill all views
        viewHolder.bindViews(model);

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onLinkClicked(activity, model);
            }
        });

        viewHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                presenter.onLinkLongClicked(activity, model);
                return false;
            }
        });
    }

    @Override
    public void showDialogAction(final Link mLink, CharSequence[] actions) {
        showLog("showDialogAction()");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true)
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        showLog("showDialogAction actionselected: " + i);
                        presenter.onDialogActionItemClicked(i, mLink);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void startEdiLinkActivity(Bundle b) {

        IntentUtils.startActivity(activity, b, LinkActivity.class);
    }

    @Override
    public void showToastError() {

        Util.showToast(activity, R.string.something_was_wrong);
    }

    @Override
    public void showToastDeleteLink() {

        Util.showToast(activity, R.string.deleting);
    }
}
