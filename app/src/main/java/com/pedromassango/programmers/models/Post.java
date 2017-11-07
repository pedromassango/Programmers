package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 15-11-2016 at 16:59 at 2:20.
 */
@IgnoreExtraProperties

public class Post implements Parcelable {

    private String id;
    private String author;
    private String authorId;
    private String title;
    private String body;
    private String imgUrl;
    private String category;
    private int commentsCount;
    private boolean commentsActive;
    private HashMap<String, Boolean> likes;
    private int views;
    private long timestamp;

    protected Post(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        title = in.readString();
        body = in.readString();
        imgUrl = in.readString();
        category = in.readString();
        commentsCount = in.readInt();
        views = in.readInt();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(imgUrl);
        dest.writeString(category);
        dest.writeInt(commentsCount);
        dest.writeInt(views);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("authorId", authorId);
        map.put("author", author);
        map.put("title", title);
        map.put("body", body);
        map.put("imgUrl", imgUrl);
        map.put("category", category);
        map.put("commentsCount", commentsCount);
        map.put("commentsActive", commentsActive);
        map.put("likes", likes);
        map.put("views", views);
        map.put("timestamp", timestamp);
        return map;
    }


    public Post() {
    }

    public Post(String id, String authorId, String author, String title, String body, String imgUrl, String category, boolean commentsActive, HashMap<String, Boolean> likes, long timestamp, int commentsCount, int views) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.title = title;
        this.body = body;
        this.imgUrl = imgUrl;
        this.category = category;
        this.commentsActive = commentsActive;
        this.likes = likes;
        this.timestamp = timestamp;
        this.commentsCount = commentsCount;
        this.views = views;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public boolean isCommentsActive() {
        return commentsActive;
    }

    public void setCommentsActive(boolean commentsActive) {
        this.commentsActive = commentsActive;
    }

    @Exclude
    @Override
    public String toString() {

        return (new Gson().toJson(this));
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public HashMap<String, Boolean> getLikes() {
        if (likes == null) {
            likes = new HashMap<>();
        }
        return likes;
    }

    public void setLikes(HashMap<String, Boolean> likes) {
        this.likes = likes;
    }
}
