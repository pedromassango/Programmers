package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Pedro Massango on 14/06/2017 at 19:57.
 */
@IgnoreExtraProperties
public class Notification extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id;
    private String author;
    private String authorId;
    private String toUserId;
    private String postId;
    private long timestamp;


    protected Notification(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        toUserId = in.readString();
        postId = in.readString();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(toUserId);
        dest.writeString(postId);
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
        map.put("toUserId", toUserId);
        map.put("postId", postId);
        map.put("timestamp", timestamp);
        return map;
    }


    public Notification() {

    }


    public Notification(String id, String authorId, String author, String toUserId, String postId, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.toUserId = toUserId;
        this.postId = postId;
        this.timestamp = timestamp;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
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
