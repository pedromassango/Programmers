package com.pedromassango.programmers.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.app.Programmers;
import com.pedromassango.programmers.config.ReputationConfigs;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IPresenceLIstener;
import com.pedromassango.programmers.interfaces.ISubscriptionCompleteListener;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.services.firebase.NotificationSender;

import java.util.HashMap;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 11/22/17.
 */

/**
 * Contains all APP transations to remote server.
 */
public class Transations {

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

    public static void handleUserSubscriptionInCategory(String mCategory, final boolean subscribe, final int position, final ISubscriptionCompleteListener completeListener) {

        // Just to be sure that the category is realy formated, to be subscribed
        final String category = CategoriesUtils.getCategoryTopic(mCategory);

        final Map<String, Boolean> subscriptionValue = new HashMap<>();
        subscriptionValue.put(category, true);

        Object value = subscribe ? subscriptionValue : null;

        Library.getCurrentUserRef()
                .child("favoritesCategory")
                .setValue(value)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showLog("handleUserSubscriptionInCategory: fail");

                        // sent fail
                        if (completeListener != null) {
                            completeListener.onError();
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showLog("handleUserSubscriptionInCategory: sucess");
                        if (subscribe) {
                            NotificationSender.subscribe(category);
                            if (completeListener != null)
                                completeListener.onComplete(category, true, position);
                        } else {
                            NotificationSender.unsubscribe(category);
                            if (completeListener != null)
                                completeListener.onComplete(category, false, position);
                        }
                    }
                });
    }

    public static void runReputationCountTransition(String userId, final boolean increment, final boolean isPost) {

        Library.getUserRef(userId).runTransaction(new Transaction.Handler() {
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
                if (reputationCount < 385) { // user help less than 25 peoples
                    userCodeLevel = Programmers.getContext().getString(R.string.beginner);
                } else if (reputationCount >= 385 && reputationCount < 770) { // user help 25 peoples
                    userCodeLevel = Programmers.getContext().getString(R.string.amauter);
                } else if (reputationCount >= 770 && reputationCount < 1540) { // user help 50 peoples
                    userCodeLevel = Programmers.getContext().getString(R.string.professional);
                } else if (reputationCount >= 3080) { // user help more than 100 or more  peoples
                    userCodeLevel = Programmers.getContext().getString(R.string.expert);
                }

                user.setReputation(reputationCount);
                user.setCodeLevel(userCodeLevel);

                //update user data localy
                RepositoryManager.getInstance().getUsersRepository()
                        .updateLoggedUser(user);

                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                showLog("update reputations complete: " + (databaseError != null));
            }
        });
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

                //update localy
                RepositoryManager.getInstance()
                        .getPostsRepository()
                        .updateLocaly(post);

                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                showLog("runCommentsCountTransition: " + (databaseError != null));
            }
        });
    }
}
