package com.pedromassango.programmers.mvp.link;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.ISendDataListener;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;

import java.util.HashMap;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 15/06/2017 at 16:16.
 */

public class LinkModel implements LinkContract.Model {

    private BaseContract.PresenterImpl presenter;

    public LinkModel(BaseContract.PresenterImpl presenter) {

        this.presenter = presenter;
    }

    @Override
    public String getUserName() {
        return PrefsUtil.getName(presenter.getContext());
    }

    @Override
    public String getUserId() {
        return PrefsUtil.getId(presenter.getContext());
    }

    @Override
    public void publishLink(Link link, boolean update, final ISendDataListener sendDataListener) {

        DatabaseReference linksRef = Library.getLinksRef();

        // If is for update the link,
        // we dont need to generate a new id.
        String linkId = update ?
                link.getId()
                :
                linksRef.push().getKey();

        // Update/Insert the ID
        link.setId(linkId);

        Map<String, Object> linkData = link.toMap();
        Map<String, Object> childUpdades = new HashMap<>();

        // Child by category
        childUpdades.put(AppRules.getAllLinksRef(linkId), linkData);
        childUpdades.put(AppRules.getLinksCategoryRef(link.getCategory(), linkId), linkData);
        childUpdades.put(AppRules.getLinksUserRef(link.getAuthorId(), linkId), linkData);

        Library.getRootReference()
                .updateChildren(childUpdades)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        sendDataListener.onSendError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        sendDataListener.onSendSuccess();
                    }
                });
    }

    @Override
    public void increaseViewsCount(Link link, IErrorListener errorListener) {

        String linkId = link.getId();

        DatabaseReference allLinksRef = Library.getRootReference().child(AppRules.getAllLinksRef(linkId)).getRef();
        showLog("linkRef: " +allLinksRef.getKey());
        DatabaseReference linkByCategory = Library.getRootReference().child(AppRules.getLinksCategoryRef(link.getCategory(), linkId)).getRef();
        showLog("linkRef: " +linkByCategory.getRef());

        DatabaseReference linkByUser = Library.getRootReference().child(AppRules.getLinksUserRef(link.getAuthorId(), linkId)).getRef();

        Worker.runLinkViewsCountTransation(allLinksRef);
        Worker.runLinkViewsCountTransation(linkByCategory);
        Worker.runLinkViewsCountTransation(linkByUser);
    }

    @Override
    public void deleteLink(Link link, final IErrorListener errorListener) {

        Map<String, Object> childUpdades = new HashMap<>();

        // Child by category
        childUpdades.put(AppRules.getAllLinksRef(link.getId()), null);
        childUpdades.put(AppRules.getLinksCategoryRef(link.getCategory(), link.getId()), null);
        childUpdades.put(AppRules.getLinksUserRef(link.getAuthorId(), link.getId()), null);

        Library.getRootReference()
                .updateChildren(childUpdades)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        errorListener.onError();
                    }
                });
    }
}
