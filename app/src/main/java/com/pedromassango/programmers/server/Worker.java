package com.pedromassango.programmers.server;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.models.Post;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 06/06/2017 at 02:19.
 */

/**
 * This class is responsable to do Any operation in server
 * that will be called more than one.
 */
public class Worker {
    public static void handleUserPresence(String userId, final IPresenceLIstener presenceLIstener) {

        final DatabaseReference userChatRef = Library.getAllUsersChatsRef().child(userId);
        final DatabaseReference onlineRef = userChatRef.child("online");
        final DatabaseReference lastOnlineRef = userChatRef.child("lastOnline");

        lastOnlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long timestamp;
                String lastOnline;

                if (dataSnapshot == null) {
                    timestamp = System.currentTimeMillis();
                    lastOnline = Util.getTimeAgo(timestamp);
                    presenceLIstener.onFriendOffline(lastOnline);
                    return;
                }

                timestamp = dataSnapshot.getValue(Long.class);
                lastOnline = Util.getTimeAgo(timestamp);
                presenceLIstener.onFriendOffline(lastOnline);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    presenceLIstener.onFriendOnline(false);
                    return;
                }

                presenceLIstener.onFriendOnline(dataSnapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                showLog("FRIEND Presence Listener was disconnected");
            }
        });
    }

    public static void sendCurrentUserFCMToken(final Context context) {

//        // Current user id
//        String userId = PrefsUtil.getId(context);
//
//        // Getting FcmToken from PrefsUtil
//        final FcmToken fcmToken = PrefsUtil.getFCMToken(context);
//        String token = FirebaseInstanceId.getInstance().getToken();
//
//        if (fcmToken.getToken() == null || fcmToken.getToken().isEmpty()) {
//            showLog("FCM TOKEN: getting token manualy");
//
//            fcmToken.setToken(token);
//        }
//
//        // If NULL, we are not connected
//        // So we do not need to continue
//        if (userId == null || userId.isEmpty()) {
//            showLog("FCM TOKEN no user found");
//            return;
//        }
//
//        if (!fcmToken.isSaved()) {
//            showLog("FCM TOKEN have not seved");
//            return;
//        }
//
//        if (fcmToken.isSent()) {
//            showLog("FCM TOKEN arleady sent");
//            return;
//        }
//
//        // Sending the current token on server
//        Library.getCurrentUserRef()
//                .child("fcmToken")
//                .setValue(fcmToken.getToken())
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        fcmToken.setSent(false);
//                        PrefsUtil.saveFCMToken(context, fcmToken);
//                        showLog("ERROR IN UPDATE TOKEN ON SERVER");
//
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        fcmToken.setSent(true);
//                        PrefsUtil.saveFCMToken(context, fcmToken);
//                        showLog("TOKEN UPDATED ON SERVER");
//                    }
//                });
    }

    public static void sendEmailVerification(final Callbacks.IRequestCallback callback, final String email) {

        FirebaseAuth auth = Library.getFirebaseAuth();

        auth.sendPasswordResetEmail(email)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        callback.onSuccess();
                    }
                });
    }

    public static void runPostViewsCountTransition(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }

                int viewsCount = post.getViews() + 1;

                post.setViews(viewsCount);
                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                showLog("runCommentsCountTransition " + (databaseError != null));

                        /* if (databaseError == null) {
                            errorListener.onError();
                        }*/
            }
        });
    }
}
