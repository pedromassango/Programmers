package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 14/06/2017 at 19:57.
 */
@IgnoreExtraProperties
public class Notification implements Parcelable {

    private String id;
    private String author;
    private String authorId;
    private String postId;
    private String description;
    private long timestamp;


    protected Notification(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        postId = in.readString();
        description = in.readString();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(postId);
        dest.writeString(description);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("authorId", authorId);
        map.put("author", author);
        map.put("description", description);
        map.put("timestamp", timestamp);
        return map;
    }

    public Notification() {

    }


    public Notification(String id, String authorId, String author, String postId, String description, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.postId = postId;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
