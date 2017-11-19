package com.pedromassango.programmers.data.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.data.UserDataSource;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.server.Library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

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
    public void checkLoggedInStatus(final Callbacks.IRequestCallback callback) {
        Library.getFirebaseAuth()
                .addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if(user == null){   // Not logged in
                            callback.onError();
                        }else { //Logged in
                            callback.onSuccess();
                        }
                    }
                });
    }

    // Login with email & password
    @Override
    public void login(String email, String password, final Callbacks.IResultCallback<Usuario> callback) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnFailureListener( reportError(callback))
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                showLog("signIn:success");
                final FirebaseUser user = authResult.getUser();
                showLog("signIn - UID: " + user.getUid());

                loginFlow(user, callback);
            }
        });
    }

    private OnFailureListener reportError(final Callbacks.IRequestCallback callback){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError();
            }
        };
    }
    private OnFailureListener reportError(final Callbacks.IResultCallback<Usuario> callback){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onDataUnavailable();
            }
        };
    }

    // Login with Google
    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account,
                                       final Callbacks.IResultCallback<Usuario> callback) {
        showLog("firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnFailureListener( reportError(callback))
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        loginFlow( user, callback);
                    }
                });
    }

    private void loginFlow(final FirebaseUser user, final Callbacks.IResultCallback<Usuario> callback) {

        Library.getUsersRef()
                .child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showLog("onDataChange - checking user info");

                        Usuario usuario;

                        // CHeck if the user data arleady exist
                        if(!dataSnapshot.exists()){
                            String photo = user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString();
                            usuario = new Usuario();
                            usuario.setEmail( user.getEmail());
                            usuario.setUrlPhoto(  photo);

                            callback.onSuccess(usuario);
                            return;
                        }

                        usuario = dataSnapshot.getValue(Usuario.class);
                        callback.onSuccess(usuario);
                    }

                    //Some error
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        showLog("LOGIN WAS CANCELLED: " + databaseError.getMessage());
                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void signup(Usuario usuario, final Callbacks.IRequestCallback callback) {
        FirebaseAuth auth = Library.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getPassword())
                .addOnFailureListener(reportError(callback))
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
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
                            reportError(callback);
                            return;
                        }

                        // Logout success
                        callback.onSuccess();
                    }
                });
    }
}
