package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pedro Massango on 03/06/2017.
 */
@IgnoreExtraProperties
public class Job implements Parcelable {

    private String id;
    private String author;
    private String authorId;
    private String category;
    private String employer;
    private String workType;
    private String country;
    private String city;
    private String email;
    private String yearsOfExperience;
    private boolean remote;
    private long timestamp;


    protected Job(Parcel in) {
        id = in.readString();
        author = in.readString();
        authorId = in.readString();
        category = in.readString();
        employer = in.readString();
        workType = in.readString();
        country = in.readString();
        city = in.readString();
        email = in.readString();
        yearsOfExperience = in.readString();
        remote = in.readByte() != 0;
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(authorId);
        dest.writeString(category);
        dest.writeString(employer);
        dest.writeString(workType);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(email);
        dest.writeString(yearsOfExperience);
        dest.writeByte((byte) (remote ? 1 : 0));
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("authorId", authorId);
        map.put("author", author);
        map.put("category", category);
        map.put("employer", employer);
        map.put("workType", workType);
        map.put("country", country);
        map.put("city", city);
        map.put("email", email);
        map.put("yearsOfExperience", yearsOfExperience);
        map.put("remote", remote);
        map.put("timestamp", timestamp);
        return map;
    }

    public Job() {

    }

    public Job(String id, String authorId, String author, String category, String employer, String workType,
               String country, String city, String email, String yearsOfExperience, boolean remote, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.category = category;
        this.employer = employer;
        this.workType = workType;
        this.country = country;
        this.city = city;

        this.email = email;
        this.yearsOfExperience = yearsOfExperience;
        this.remote = remote;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
