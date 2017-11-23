package com.pedromassango.programmers.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.pedromassango.programmers.config.ReputationConfigs;
import com.pedromassango.programmers.extras.CategoriesUtils;
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

    public static void handleUserSubscriptionInCategory(String mCategory, final boolean subscribe, final ISubscriptionCompleteListener completeListener) {

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
                                completeListener.onComplete(category, true);
                        } else {
                            NotificationSender.unsubscribe(category);
                            if (completeListener != null)
                                completeListener.onComplete(category, false);
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
//TODO: verify this code
                if (reputationCount < 375) { // user help less than 25 peoples
                    //userCodeLevel = presenter.getContext().getString(R.string.beginner);
                } else if (reputationCount >= 375 && reputationCount < 750) { // user help 25 peoples
                    //userCodeLevel = presenter.getContext().getString(R.string.amauter);
                } else if (reputationCount >= 750 && reputationCount < 1500) { // user help 50 peoples
                    //userCodeLevel = presenter.getContext().getString(R.string.professional);
                } else if (reputationCount >= 1500) { // user help more than 100 or more  peoples
                    //userCodeLevel = presenter.getContext().getString(R.string.expert);
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
}
