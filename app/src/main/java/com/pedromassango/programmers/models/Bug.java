package com.pedromassango.programmers.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.OkHttpDownloader;

/**
 * Created by Pedro Massango on 07-03-2017 9:47.
 */
@IgnoreExtraProperties

public class Bug {

    private String id;
    private String author;
    private String authorId;
    private String description;
    private long timestamp;

    public Bug() {
    }

    public Bug(String id, String authorId, String author, String description, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.description = description;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
