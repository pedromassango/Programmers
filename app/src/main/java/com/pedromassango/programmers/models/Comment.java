package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 15-11-2016.
 */
@IgnoreExtraProperties
public class Comment implements Parcelable {

    private String id;
    private String author;
    private String authorId;
    private String authorUrlPhoto;
    private String postId;
    private String text;
    private Map<String, Boolean> votes;
    private long timestamp;
    private String postCategory;
    private String postAuthorId;

    protected Comment(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        authorUrlPhoto = in.readString();
        postId = in.readString();
        text = in.readString();
        timestamp = in.readLong();
        postCategory = in.readString();
        postAuthorId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(authorUrlPhoto);
        dest.writeString(postId);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeString(postCategory);
        dest.writeString(postAuthorId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getPostAuthorId() {
        return postAuthorId;
    }

    public void setPostAuthorId(String postAuthorId) {
        this.postAuthorId = postAuthorId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("author", author);
        map.put("authorUrlPhoto", authorUrlPhoto);
        map.put("authorId", authorId);
        map.put("postId", postId);
        map.put("postCategory", postCategory);
        map.put("postAuthorId", postAuthorId);
        map.put("text", text);
        map.put("votes", votes);
        map.put("timestamp", timestamp);
        return map;
    }

    @Override
    public String toString() {

        return (new Gson().toJson(this));
    }


    public Comment() {
    }

    public Comment(String id, String senderName, String authorUrlPhoto, String postId, String text, Map<String, Boolean> votes, long timestamp, String postCategory) {
        this.id = id;
        this.author = senderName;
        this.authorUrlPhoto = authorUrlPhoto;
        this.postId = postId;
        this.text = text;
        this.votes = votes;
        this.timestamp = timestamp;
        this.postCategory = postCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrlPhoto() {
        return authorUrlPhoto;
    }

    public void setAuthorUrlPhoto(String authorUrlPhoto) {
        this.authorUrlPhoto = authorUrlPhoto;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Boolean> getVotes() {
        if (null == votes) {
            votes = new HashMap<>();
        }
        return votes;
    }

    public void setVotes(Map<String, Boolean> votes) {
        this.votes = votes;
    }

    //EXTRA METHODOS

    public void addVote(String userId) {
        if (null == votes) {
            votes = new HashMap<>();
        }

        votes.put(userId, true);
    }

    public void removeVote(String userId) {
        votes.remove(userId);
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostCategory() {
        return postCategory;
    }
}
