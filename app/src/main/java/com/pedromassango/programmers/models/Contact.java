package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Pedro Massango on 14/05/2017.
 */
@IgnoreExtraProperties
public class Contact implements Parcelable {

    private String id;
    private String username;
    private String userId;
    private String userUrlPhoto;
    private boolean online;
    private long lastOnline;

    public Contact() {

    }

    public Contact(String id, String username, String userId, String userUrlPhoto, boolean online, long lastOnline) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.userUrlPhoto = userUrlPhoto;
        this.online = online;
        this.lastOnline = lastOnline;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        username = in.readString();
        userId = in.readString();
        userUrlPhoto = in.readString();
        online = in.readByte() != 0;
        lastOnline = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(userId);
        dest.writeString(userUrlPhoto);
        dest.writeByte((byte) (online ? 1 : 0));
        dest.writeLong(lastOnline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getUserUrlPhoto() {
        return userUrlPhoto;
    }

    public void setUserUrlPhoto(String userUrlPhoto) {
        this.userUrlPhoto = userUrlPhoto;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
