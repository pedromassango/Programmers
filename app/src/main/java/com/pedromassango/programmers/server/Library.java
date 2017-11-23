package com.pedromassango.programmers.server;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.CategoriesUtils;

/**
 * Created by Pedro Massango on 13-04-2017 at 22:35.
 */

public class Library {

    private static StorageReference firebaseStorage;
    private static DatabaseReference bugsRef;
    private static DatabaseReference postsDatabase;
    private static FirebaseStorage imagePostsStorage;
    private static DatabaseReference rootReference;
    private static DatabaseReference notificationsRef;
    private static DatabaseReference linksRef;

    public static FirebaseStorage getImageProfilesStorage() {
        if (imagePostsStorage == null) {
            imagePostsStorage = getFirebaseStorage().child("photos").getStorage();
        }
        return imagePostsStorage;
    }

    public static DatabaseReference getRootReference() {
        if (null == rootReference) {
            rootReference = FirebaseDatabase.getInstance().getReference();
        }
        return rootReference;
    }

    public static FirebaseMessaging getFirebaseMessaging() {

        return FirebaseMessaging.getInstance();
    }

    public static FirebaseAuth getFirebaseAuth() {

        return FirebaseAuth.getInstance();
    }

    private static StorageReference getFirebaseStorage() {
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return firebaseStorage;
    }


    // User
    public static String getUserId() {

        return getFirebaseAuth().getCurrentUser().getUid();
    }

    public static DatabaseReference getCurrentUserRef() {

        return getUserRef(getUserId());
    }

    public static DatabaseReference getUserRef(String userUId) {

        return getUsersRef().child(userUId);
    }

    public static DatabaseReference getAllUsersChatsRef() {

        return getRootReference().child(AppRules.USERS_ONLINE).getRef();
    }

    public static DatabaseReference getUsersRef() {

        return getRootReference().child(AppRules.USERS);
    }
    // User


    // Bugs
    public static DatabaseReference getBugsref() {
        if (null == bugsRef) {
            bugsRef = getRootReference().child(AppRules.BUGS);
        }
        return bugsRef;
    }


    // Posts
    public static DatabaseReference getAllPostsRef() {
        if (postsDatabase == null) {
            postsDatabase = getRootReference().child(AppRules.POSTS);
        }
        return postsDatabase;
    }

    public static DatabaseReference getPostsByCategoryRef(String category) {
        category = CategoriesUtils.getCategory(category);
        return getRootReference().child(AppRules.POSTS_CATEGORY + category);
    }

    public static DatabaseReference getCommentsRef(String postId) {

        return getRootReference().child(AppRules.COMMENTS_POST).child(postId);
    }

    public static DatabaseReference getUserPostsRef(String userUId) {

        return getRootReference().child(AppRules.USER_POSTS).child(userUId);
    }

    public static DatabaseReference getPostRef(String postId) {

        return getAllPostsRef().child(postId);
    }
    // Posts


    // Messages
    public static DatabaseReference getMessagesRef(String friendId) {

        return getRootReference()
                .child(AppRules.USER_MESSAGES)
                .child(PrefsHelper.getId())
                .child(friendId);
    }
    // Messages


    // Payments
    public static DatabaseReference getPaymentsRef() {

        return getRootReference().child(AppRules.DONATIONS);
    }
    // Payments


    // Jobs
    public static DatabaseReference getJobsRef() {

        return getRootReference().child(AppRules.JOBS);
    }

    public static DatabaseReference getJobsByCategoryRef(String category) {

        return getRootReference().child(AppRules.JOBS_CATEGORY + category);
    }
    // Jobs


    // Links
    public static DatabaseReference getLinksByCategoryRef(String category) {

        return getRootReference().child(AppRules.LINKS_CATEGORY + category);
    }

    public static DatabaseReference getUserLinksRef(String userUId) {

        return getRootReference().child(AppRules.USER_LINKS).child(userUId);
    }

    public static DatabaseReference getLinksRef() {
        if (linksRef == null) {
            linksRef = getRootReference().child(AppRules.LINKS);
        }
        return linksRef;
    }

    public static DatabaseReference getNotificationsRef(String userId) {
        if (notificationsRef == null) {
            notificationsRef = getRootReference().child(AppRules.NOTIFICATIONS)
            .child(userId);
        }
        return notificationsRef;
    }

    public static String generateId() {
        return getRootReference().push().getKey();
    }
}
