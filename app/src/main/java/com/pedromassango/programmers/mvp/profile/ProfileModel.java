package com.pedromassango.programmers.mvp.profile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetUserListener;
import com.pedromassango.programmers.models.Contact;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.mvp.profile.edit.Contract;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 27/06/2017 at 14:04.
 */

public class ProfileModel implements ProfileContract.ModelImpl {

    private BaseContract.PresenterImpl profilePresenter;
    private Contract.OnEditListener listener;

    public ProfileModel(BaseContract.PresenterImpl profilePresenter) {

        this.profilePresenter = profilePresenter;
    }


    @Override
    public String getUserId() {

        return PrefsUtil.getId(profilePresenter.getContext());
    }

    @Override
    public String getUsername() {
        return PrefsUtil.getName(profilePresenter.getContext());
    }

    @Override
    public void saveProfile(final Contract.Presenter presenter, final Contract.OnEditListener listener,
                            Uri imagePicked, final Usuario mUsuario) {
        this.listener = listener;
        this.listener = listener;
        mUsuario.setId(getUserId());

        if (null != imagePicked) {

            //Uploading Image
            FirebaseStorage imageProfilesStorage = Library.getImageProfilesStorage();
            StorageReference imageProfilePath = imageProfilesStorage.getReference().child(imagePicked.getLastPathSegment());
            imageProfilePath.putFile(imagePicked)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            listener.onError(Util.getError(e));
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String string_dwload = downloadUrl.toString();

                            mUsuario.setUrlPhoto(string_dwload);

                            //Save the user information
                            updateProfile(mUsuario);
                        }
                    });
            return;
        }

        //Proced without image
        updateProfile(mUsuario);
    }

    private String getFcmToken() {
        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        String token = instanceId.getToken();
        token = (token != null) ? token : "";
        return token;
    }

    @Override
    public void updateProfile(final Usuario usuario) {
        boolean isFirstTime = ((Contract.Presenter) profilePresenter).isFirsttime();
        if (isFirstTime) {
            usuario.setFcmToken(getFcmToken());
        }

        //Path to update the profile
        String userRef = AppRules.getUserRef(getUserId());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(userRef, usuario);

        //Start the save work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onError(Util.getError(e));
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Saving user info
                        PrefsUtil.setId(profilePresenter.getContext(), usuario.getId());
                        PrefsUtil.setName(profilePresenter.getContext(), usuario.getUsername());
                        PrefsUtil.setPhoto(profilePresenter.getContext(), usuario.getUrlPhoto());

                        // Subscribe the user to their favorite category, to receive notifications
                        Worker.handleUserSubscriptionInCategory(usuario.getProgrammingLanguage(), true, null);

                        listener.onSuccess(usuario);
                    }
                });
    }

    @Override
    public void getUser(String userId, final IGetUserListener getUserListener) {

        Library.getUserRef(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        getUserListener.onGetUserSuccess(usuario);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        getUserListener.onGetUserError(databaseError.getMessage());
                    }
                });
    }
}
