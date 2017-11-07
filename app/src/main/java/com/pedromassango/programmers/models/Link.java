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
public class Link implements Parcelable {

    private String id;
    private String author;
    private String authorId;
    private String description;
    private String category;
    private int views;
    private String url;
    private long timestamp;


    protected Link(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        description = in.readString();
        category = in.readString();
        views = in.readInt();
        url = in.readString();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeInt(views);
        dest.writeString(url);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Link> CREATOR = new Creator<Link>() {
        @Override
        public Link createFromParcel(Parcel in) {
            return new Link(in);
        }

        @Override
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("authorId", authorId);
        map.put("author", author);
        map.put("description", description);
        map.put("category", category);
        map.put("views", views);
        map.put("url", url);
        map.put("timestamp", timestamp);
        return map;
    }

    public Link() {

    }


    public Link(String id, String authorId, String author, String description, String category, int views, String url, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.description = description;
        this.category = category;
        this.views = views;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
