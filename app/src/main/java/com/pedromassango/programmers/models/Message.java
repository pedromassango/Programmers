package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Created by JANU on 14/05/2017.
 */

@IgnoreExtraProperties
public class Message extends RealmObject implements Parcelable {

    private String id;
    private String author;
    private String authorId;
    private String receiverId;
    private String receiverName;
    private String text;
    private long timestamp;


    protected Message(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        receiverId = in.readString();
        receiverName = in.readString();
        text = in.readString();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(receiverId);
        dest.writeString(receiverName);
        dest.writeString(text);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("text", text);
        map.put("authorId", authorId);
        map.put("author", author);
        map.put("receiverId", receiverId);
        map.put("receiverName", receiverName);
        map.put("timestamp", timestamp);
        return map;
    }

    public Message() {

    }

    public Message(String id, String authorId, String author, String receiverId, String receiverName, String text, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

}
