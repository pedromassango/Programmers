package com.pedromassango.programmers;

import com.pedromassango.programmers.extras.CategoriesUtils;

/**
 * Created by Pedro Massango on 01/05/17.
 */

/**
 * This class have all references for Firebase operation
 */
public class AppRules {

    // Topic for receive notifications
    public static final String NEWS = "news";
    public static final String NOTIFICATIONS = "notifications";

    // Votes
    private static final String LIKES = "likes";

    // Posts
    public static final String POSTS = "posts";
    public static final String POSTS_CATEGORY = "posts-";
    public static final String USER_POSTS = "user-posts";

    // Jobs
    public static final String JOBS = "jobs";
    public static final String JOBS_CATEGORY = "jobs-";
    private static final String USER_JOBS = "user-jobs";

    // Links
    public static final String LINKS = "links";
    public static final String LINKS_CATEGORY = "links-";
    public static final String USER_LINKS = "user-links";

    // User
    public static final String USERS = "users";
    public static final String USERS_ONLINE = "users-online";
    private static final String USERS_MESSAGES = "users-messages";
    public static final String USER_MESSAGES = "users-messages";

    public static final String DONATIONS = "donations";
    public static final String COMMENTS_POST = "comments-post";
    public static final String BUGS = "bugs";
    /*Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.email_to), null));
    contactIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
    startActivity(Intent.createChooser(contactIntent, getString(R.string.email_chooser)));*/

    /*
    1--->If the post arleady have more than @10 suggests, the Post will be
         deleted imediatily.

    */


    // Post RULES
    public static String getAllPostsRef(String postId) {

        //childUpdates.put("/posts/" + postId+"/likes", likeValue);
        return String.format("/%s/%s", POSTS, postId);
    }

    public static String getPostsCategoryRef(String mCategory, String mPostId) {

        String category = CategoriesUtils.getCategory(mCategory);
        return String.format("/%s%s/%s", POSTS_CATEGORY, category, mPostId);
    }

    public static String getPostUserRef(String senderId, String postId) {

        return String.format("/%s/%s/%s/", USER_POSTS, senderId, postId);
    }
    //END publish/delete Post RULES


    // voteUp/voteDown RULES
    public static String getAllPostsLikesRef(String postId) {
        //childUpdates.put("/posts/" + postId+"/likes", likeValue);
        return String.format("/%s/%s/%s", POSTS, postId, LIKES);
    }

    public static String getPostsCategoryLikesRef(String category, String postId) {
        category = CategoriesUtils.getCategory(category);
        return String.format("/%s%s/%s/%s", POSTS_CATEGORY, category, postId, LIKES);
    }

    public static String getPostUserLikesRef(String senderId, String postId) {

        return String.format("%s/%s/%s/%s", USER_POSTS, senderId, postId, LIKES);
    }
    //END voteUp/voteDown RULES


    // GET USER REFERENCE Rule
    public static String getUserRef(String userId) {
        return String.format("/%s/%s", USERS, userId);
    }

    // List all users, with online status
    public static String allUsersOnlineRef(String userId) {
        return String.format("%s/%s", USERS_ONLINE, userId);
    }


    // Messages Rules
    public static String getMessagesRef(String userId, String friendId) {

        return String.format("/%s/%s/%s", USERS_MESSAGES, userId, friendId);
    }
    // Publish update an Post


    // Publish/delete Jobs RULES
    public static String getAllJobsRef(String jobId) {

        return String.format("/%s/%s", JOBS, jobId);
    }

    public static String getJobsCategoryRef(String mCategory, String mJobId) {

        String category = CategoriesUtils.getCategory(mCategory);
        return String.format("/%s%s/%s", JOBS_CATEGORY, category, mJobId);
    }

    public static String getJobsUserRef(String senderId, String jobId) {

        return String.format("/%s/%s/%s/", USER_JOBS, senderId, jobId);
    }
    //END publish/delete Jobs RULES


    // Publish/delete Links RULES
    public static String getAllLinksRef(String linkId) {

        //childUpdates.put("/posts/" + postId+"/likes", likeValue);
        return String.format("/%s/%s", LINKS, linkId);
    }

    public static String getLinksCategoryRef(String mCategory, String mLinkId) {

        String category = CategoriesUtils.getCategory(mCategory);
        return String.format("/%s%s/%s", LINKS_CATEGORY, category, mLinkId);
    }

    public static String getLinksUserRef(String senderId, String linkId) {

        return String.format("/%s/%s/%s/", USER_LINKS, senderId, linkId);
    }
    //END publish/delete LInks RULES

    //The post to publish
  /*  Map<String, Object> postValues = post.toMap();

        showLog("The sender ID - " +post.getAuthorId());

        Map<String, Object> childUpdates = new HashMap<>();
        String caregory = Util.getCategory(post.getCategory());

        //All posts reference
        childUpdates.put("/posts/" + postId, postValues);

        //Posts by category reference
        childUpdates.put( String.format("/posts-%s/", caregory) + "/" + postId, postValues);

        //Posts by user reference
        childUpdates.put("/user-posts/" + senderId + "/" + postId, postValues);

        //Start the publish work
        Library.getRootReference()
                .updateChildren(childUpdates)
                .addOnCompleteListener(this);*/


    // Publish update an Comment
/*
    Map<String, Object> commentValues = sendComment.toMap();

    Map<String, Object> childUpdates = new HashMap<>();

    //All Comments of the Current Post reference
    childUpdates.put("/comments-post/" + postId +"/"+commentId, commentValues);

    //Start the publish work
            Library.getRootReference()
                    .updateChildren(childUpdates)
        .addOnCompleteListener(this);*/
}

// Handler user online status

 /*   // since I can connect from multiple devices, we store each connection instance separately
// any time that connectionsRef's value is null (i.e. has no children) I am offline
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myConnectionsRef = database.getReference("users/joe/connections");

    // stores the timestamp of my last disconnect (the last time I was seen online)
    final DatabaseReference lastOnlineRef = database.getReference("/users/joe/lastOnline");

    final DatabaseReference connectedRef = database.getReference(".info/connected");
connectedRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot snapshot) {
        boolean connected = snapshot.getValue(Boolean.class);
        if (connected) {
        // add this device to my connections list
        // this value could contain info about the device or a timestamp too
        DatabaseReference con = myConnectionsRef.push();
        con.setValue(Boolean.TRUE);

        // when this device disconnects, remove it
        con.onDisconnect().removeValue();

        // when I disconnect, update the last time I was seen online
        lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
        }
        }

@Override
public void onCancelled(DatabaseError error) {
        System.err.println("Listener was cancelled at .info/connected");
        }
        });*/