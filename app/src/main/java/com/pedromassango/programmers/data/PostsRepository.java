package com.pedromassango.programmers.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.pedromassango.programmers.AppRules;
import com.pedromassango.programmers.config.ReputationConfigs;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.server.Library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by pedromassango on 11/12/17.
 */

/**
 * A class to manage Posts. Use to retrieve and save data.
 */
public class PostsRepository implements PostsDataSource {

    private static PostsRepository INSTANCE = null;
    private final PostsDataSource remoteSource;
    private final PostsDataSource localSource;

    private PostsRepository(PostsDataSource remoteSource, PostsDataSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }

    public static PostsRepository getInstance(PostsDataSource remoteSource, PostsDataSource localSource) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepository(remoteSource, localSource);
        }
        return INSTANCE;
    }

    @Override
    public void getAll(final Callbacks.IResultsCallback<Post> callback) {
        localSource.getAll(new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                getFromRemoteAndUpdateLocalSource(callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteAndUpdateLocalSource(callback);
            }
        });
    }

    @Override
    public void getByUser(final String authorId, final Callbacks.IResultsCallback<Post> callback) {
        localSource.getByUser(authorId, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                getFromRemoteByAuthorId(authorId, callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteByAuthorId(authorId, callback);
            }
        });
    }

    @Override
    public void getAll(final String category, final Callbacks.IResultsCallback<Post> callback) {
        localSource.getAll(category, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                getFromRemoteByCategory(category, callback);
            }

            @Override
            public void onDataUnavailable() {
                getFromRemoteByCategory(category, callback);
            }
        });
    }

    private void getFromRemoteAndUpdateLocalSource(final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.getAll(new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {

                updateLocalDataSource(results);
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void getFromRemoteByAuthorId(String authorId, final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.getByUser(authorId, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {

                updateLocalDataSource(results);
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void getFromRemoteByCategory(String category, final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.getAll(category, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {

                updateLocalDataSource(results);
                callback.onSuccess(results);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void getFromRemoteAndUpdateLocalSource(String postId, final Callbacks.IResultCallback<Post> callback) {
        remoteSource.getById(postId, new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post result) {

                localSource.save(result, null);
                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void updateLocalDataSource(List<Post> results) {
        for (Post p : results) {
            localSource.save(p, null);
        }
    }

    public void increasePostViews(Post post, Callbacks.IRequestCallback callback) {
        String postId = post.getId();

        DatabaseReference allPostRef = Library.getRootReference().child(AppRules.getAllPostsRef(postId));
        DatabaseReference PostByCategoryRef = Library.getRootReference().child(AppRules.getPostsCategoryRef(post.getCategory(), postId));
        DatabaseReference postByUserRef = Library.getRootReference().child(AppRules.getPostUserRef(post.getAuthorId(), postId));

        //TODO: work here.
        //Worker.runPostViewsCountTransition(allPostRef, callback);
        //Worker.runPostViewsCountTransition(PostByCategoryRef, callback);
        //Worker.runPostViewsCountTransition(postByUserRef, callback);

        // Update the local post data
        localSource.update(post, null);
    }

    @Override
    public void handleCommentsPermission(final String authorId, final String postId, final String category, final boolean commentsActive, final Callbacks.IRequestCallback callback) {
        remoteSource.handleCommentsPermission(authorId, postId, category, commentsActive, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                localSource.handleCommentsPermission(authorId, postId, category, commentsActive, null);

                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    public void handleLikes(final Callbacks.IRequestCallback callback, Post post, boolean like) {

        final String loggedUserId = PrefsHelper.getId();
        final String senderId = post.getAuthorId();
        String postId = post.getId();
        String postCategory = post.getCategory();

        Log.v("output", "handleLikes:\n L_ID: " + loggedUserId + "\n S_ID: " + senderId);

        // Firebase rules
        String category = CategoriesUtils.getCategory(postCategory);

        //The vote data
        final Map<String, Object> likeValue = new HashMap<>();
        if (like) {
            likeValue.put(loggedUserId, Boolean.TRUE);
        } else {
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

                        callback.onError();
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
                        runReputationCountTransition(senderPostRef, true, true);
                    }
                });
    }

    public static void runReputationCountTransition(DatabaseReference userRef,
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


    @Override
    public void search(final String query, final Callbacks.IResultsCallback<Post> callback) {
        localSource.search(query, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                // get from remote but not notify if have error
                searchOnRemote(false, query, callback);
            }

            @Override
            public void onDataUnavailable() {
                searchOnRemote(true, query, callback);
            }
        });
    }

    private void searchOnRemote(final boolean notifyError, String query, final Callbacks.IResultsCallback<Post> callback) {
        remoteSource.search(query, new Callbacks.IResultsCallback<Post>() {
            @Override
            public void onSuccess(List<Post> results) {
                callback.onSuccess(results);

                updateLocalDataSource(results);
            }

            @Override
            public void onDataUnavailable() {
                if (notifyError)
                    callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void getById(final String postId, final Callbacks.IResultCallback<Post> callback) {
        localSource.getById(postId, new Callbacks.IResultCallback<Post>() {
            @Override
            public void onSuccess(Post result) {

                getFromRemoteAndUpdateLocalSource(postId, callback);

                callback.onSuccess(result);
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    @Override
    public void save(final Post post, final Callbacks.IRequestCallback callback) {
        remoteSource.save(post, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                //TODO: Send an notification for all users

                //TODO: this post does not have an ID
                localSource.save(post, null);

                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void update(final Post post, final Callbacks.IRequestCallback callback) {
        remoteSource.update(post, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {
                localSource.update(post, null);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void delete(final Post post, final Callbacks.IRequestCallback callback) {
        remoteSource.delete(post, new Callbacks.IRequestCallback() {
            @Override
            public void onSuccess() {

                localSource.delete(post, callback);
                //callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}
