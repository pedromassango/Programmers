package com.pedromassango.programmers.data.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.data.UserDataSource;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.server.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pedromassango on 11/8/17.
 */

/**
 * retrieve the user data from remote source (Firebase)
 */
public class UserRemoteDataSource implements UserDataSource {

    // Store the instance
    private static UserRemoteDataSource INSTANCE;

    private UserRemoteDataSource(){

    }

    // prevent multiple instances of this class.
    public static UserRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(final Callbacks.IResultsCallback<Usuario> callback) {

        Library.getUsersRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Check if it not exist
                        if(!dataSnapshot.exists()){
                            callback.onDataUnavailable();
                            return;
                        }

                        // Temp list to store data from queu
                        List<Usuario> usuarios = new ArrayList<Usuario>();

                        for(DataSnapshot it : dataSnapshot.getChildren()){
                            Usuario user = it.getValue(Usuario.class);

                            // store on temp list
                            usuarios.add(user);
                        }

                        // Notify the UI with the result.
                        callback.onSuccess(usuarios);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();

                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void getUserById(String userId, final Callbacks.IResultCallback<Usuario> callback) {

        Library.getUserRef(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            callback.onDataUnavailable();
                            return;
                        }

                        // convert snapshot to Usuario model
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        // return the data
                        callback.onSuccess( usuario);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();

                        // notify the UI that some error occours.
                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void saveUser(final Usuario usuario, final Callbacks.IRequestCallback callback) {

        // Check if there is image to save
        if(usuario.getUrlPhoto() != null){

            // Convert back to Uri
            String uriString = usuario.getUrlPhoto();
            Uri uri = new Gson().fromJson(uriString, Uri.class);

            uploadImageUser(uri, new Callbacks.IResultCallback<String>() {
                @Override
                public void onSuccess(String imageUrl) {
                    // Image uploaded success
                    usuario.setUrlPhoto( imageUrl);

                    saveOrUpdateUserProfile( usuario, callback);
                }

                @Override
                public void onDataUnavailable() {
                    // unable to upload image
                    callback.onError();
                }
            });
        }
    }

    // Method to upload image
    private void uploadImageUser(Uri photoUri, final Callbacks.IResultCallback<String> callback){

        //Uploading Image
        FirebaseStorage imageProfilesStorage = Library.getImageProfilesStorage();
        StorageReference imageProfilePath = imageProfilesStorage.getReference().child(photoUri.getLastPathSegment());
        imageProfilePath.putFile(photoUri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Notify error
                        callback.onDataUnavailable();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String string_dwload = downloadUrl.toString();

                        // return the photo download url
                        callback.onSuccess(string_dwload);
                    }
                });
    }

    private void saveOrUpdateUserProfile(Usuario usuario, final Callbacks.IRequestCallback callback){
        //Path to update the profile
        String userRef = AppRules.getUserRef(usuario.getId());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(userRef, usuario);

        //Start the save work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // some errors occours
                        callback.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Success!
                        callback.onSuccess();
                    }
                });
    }

    @Override
    public void logout(final Callbacks.IRequestCallback callback) {

        // Logout user in Firebase.
        Library.getFirebaseAuth().signOut();

        // Listen when it done
        Library.getFirebaseAuth()
                .addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        // If sucess, currentUser shold be null
                        if(firebaseAuth.getCurrentUser() != null){
                            // An error occour when trie to logout
                            callback.onError();
                            return;
                        }

                        // Logout success
                        callback.onSuccess();
                    }
                });
    }
}
