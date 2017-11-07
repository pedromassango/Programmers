package com.pedromassango.programmers.server;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.config.ReputationConfigs;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.PrefsUtil;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IErrorListener;
import com.pedromassango.programmers.interfaces.IPostDeleteListener;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.interfaces.ISubscriptionCompleteListener;
import com.pedromassango.programmers.models.FcmToken;
import com.pedromassango.programmers.models.Link;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.base.BaseContract;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import java.util.HashMap;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 06/06/2017 at 02:19.
 */

/**
 * This class is responsable to do Any operation in server
 * that will be called more than one.
 */
public class Worker {

    public static void handleLikes(final BaseContract.PresenterImpl presenter, final String loggedUserId,
                                   boolean like, String postId, String postCategory,
                                   final String senderId, final IErrorListener listener) {

        Log.v("output", "handleLikes:\n L_ID: " + loggedUserId + "\n S_ID: " + senderId);

        // Firebase rules
        String  category = CategoriesUtils.getCategory(postCategory);

        //The vote data
        final Map<String, Object> likeValue = new HashMap<>();
        if(like) {
            likeValue.put(loggedUserId, Boolean.TRUE);
        }else{
            likeValue.put(loggedUserId, null);
        }

        // All posts reference
        String allPostsLikesRef = AppRules.getAllPostsLikesRef(postId);
        // Posts by category reference
        String postsCategoryLikesRef = AppRules.getPostsCategoryLikesRef(category, postId);
        // Posts by user reference
        String postUserLikesRef = AppRules.getPostUserLikesRef(senderId, postId);

        Log.v("output", "handleLikes - path: " + allPostsLikesRef);

        //The references to update
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(allPostsLikesRef, likeValue);
        childUpdates.put(postsCategoryLikesRef, likeValue);
        childUpdates.put(postUserLikesRef, likeValue);

        //Start the LIKE work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // If is the sender that like they own post
                        // we do not need to increment the skills
                        if (loggedUserId.equals(senderId))
                            return;

                        // increment or decrement the senderPost skill
                        DatabaseReference senderPostRef = Library.getUserRef(senderId);
                        runReputationCountTransition(presenter, senderPostRef, true, true);

                    }
                });
    }

    public static void deletePost(String postId, String category, String senderId, final IPostDeleteListener listener) {

        //All posts reference
        String allPostsRef = AppRules.getAllPostsRef(postId);
        //Posts by category reference
        String postsCategoryRef = AppRules.getPostsCategoryRef(category, postId);
        //Posts by user reference
        String postUserRef = AppRules.getPostUserRef(senderId, postId);

        //The references to update
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(allPostsRef, null);
        childUpdates.put(postsCategoryRef, null);
        childUpdates.put(postUserRef, null);

        //Start the Post publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        listener.onPostDeleteError(Util.getError(e));
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        listener.onPostDeleteSuccess();
                    }
                });
    }

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

    /**
     * Decrement or increment commentsCount
     *
     * @param postRef   the Post reference to run transaction
     * @param increment if TRUE it wil increment, if FALSE it will decrement.
     */
    public static void runCommentsCountTransition(DatabaseReference postRef, final boolean increment) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }

                int commentsCount = post.getCommentsCount();
                int data = increment ? commentsCount + 1 : commentsCount - 1;

                post.setCommentsCount(data);
                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                showLog("runCommentsCountTransition: " + (databaseError != null));
            }
        });
    }

    public static void runPostViewsCountTransition(DatabaseReference postRef, final IErrorListener errorListener) {
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

    public static void runLinkViewsCountTransation(DatabaseReference linkRef) {
        linkRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                showLog("increaseViewsCount: running ");
                Link link = mutableData.getValue(Link.class);
                if (link == null) {
                    return Transaction.success(mutableData);
                }

                int viewsCount = link.getViews() + 1;

                link.setViews(viewsCount);
                showLog("increaseViewsCount: sucess ");
                mutableData.setValue(link);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                showLog("increaseViewsCount: " + (databaseError != null));
            }
        });
    }

    public static void runReputationCountTransition(final BaseContract.PresenterImpl presenter, DatabaseReference userRef,
                                                    final boolean increment, final boolean isPost) {
        userRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Usuario user = mutableData.getValue(Usuario.class);
                if (user == null) {
                    return Transaction.success(mutableData);
                }
                int reputationCount = user.getReputation();
                if (increment && isPost) {
                    reputationCount += ReputationConfigs.POST_INCREMENT;
                } else if (!increment && isPost) {
                    reputationCount -= ReputationConfigs.POST_DECREMENT;
                } else if (increment) {
                    reputationCount += ReputationConfigs.COMMENT_INCREMENT;
                } else {
                    reputationCount -= ReputationConfigs.COMMENT_DECREMENT;
                }

                String userCodeLevel = user.getCodeLevel();

                if (reputationCount < 375) { // user help less than 25 peoples
                    userCodeLevel = presenter.getContext().getString(R.string.beginner);
                } else if (reputationCount >= 375 && reputationCount < 750) { // user help 25 peoples
                    userCodeLevel = presenter.getContext().getString(R.string.amauter);
                } else if (reputationCount >= 750 && reputationCount < 1500) { // user help 50 peoples
                    userCodeLevel = presenter.getContext().getString(R.string.professional);
                } else if (reputationCount >= 1500) { // user help more than 100 or more  peoples
                    userCodeLevel = presenter.getContext().getString(R.string.expert);
                }

                user.setReputation(reputationCount);
                user.setCodeLevel(userCodeLevel);

                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                showLog("update reputations complete: " + (databaseError != null));
            }
        });
    }

    public static void handleUserSubscriptionInCategory(String mCategory, final boolean subscribe, final ISubscriptionCompleteListener completeListener) {

      /*  // Just to be sure that the category is realy formated, to be subscribed
        final String category = CategoriesUtils.getCategoryTopic(mCategory);

        final Map<String, Boolean> subscriptionValue = new HashMap<>();
        subscriptionValue.put(category, true);

        Object value = subscribe ? subscriptionValue : null;

        Library.getCurrentUserRef()
                .child("favoritesCategory")
                .setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            // sent fail
                            if (completeListener != null)
                                completeListener.onError();
                            showLog("handleUserSubscriptionInCategory: fail");
                            return;
                        }

                        showLog("handleUserSubscriptionInCategory: sucess");

                        if (subscribe) {
                            NotificationSender.subscribe(category);
                            if (completeListener != null)
                                completeListener.onComplete(category, true);
                        } else {
                            NotificationSender.unsubscribe(category);
                            if (completeListener != null)
                                completeListener.onComplete(category, false);
                        }
                    }
                });*/
    }
}
