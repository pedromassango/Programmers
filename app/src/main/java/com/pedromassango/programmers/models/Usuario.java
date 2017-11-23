package com.pedromassango.programmers.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 15-11-2016.
 */
@IgnoreExtraProperties
public class Usuario extends RealmObject implements Parcelable {

    //PROPERTIES
    @PrimaryKey
    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String urlPhoto;
    private String fcmToken;
    private String programmingLanguage;
    private int reputation;
    private String codeLevel;
    private String platform;
    private String age;
    private String gender;
    private String country;
    private String city;

    @Ignore
    private Map<String, Boolean> favoritesCategory;
    private int accountComplete;


    public Usuario() {
    }

    public Usuario(String id, String username, String password, String email, String phone,
                   String urlPhoto, String fcmToken, String programmingLanguage,
                   int reputation, String codeLevel, String platform,
                   String age, String gender, String country,
                   String city, Map<String, Boolean> favoritesCategory, int accountComplete) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.urlPhoto = urlPhoto;
        this.fcmToken = fcmToken;
        this.programmingLanguage = programmingLanguage;
        this.reputation = reputation;
        this.codeLevel = codeLevel;
        this.platform = platform;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.favoritesCategory = favoritesCategory;
        this.accountComplete = accountComplete;
    }

    protected Usuario(Parcel in) {
        id = in.readString();
        username = in.readString();
        password = in.readString();
        email = in.readString();
        phone = in.readString();
        urlPhoto = in.readString();
        fcmToken = in.readString();
        programmingLanguage = in.readString();
        reputation = in.readInt();
        codeLevel = in.readString();
        platform = in.readString();
        age = in.readString();
        gender = in.readString();
        country = in.readString();
        city = in.readString();
        accountComplete = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(urlPhoto);
        dest.writeString(fcmToken);
        dest.writeString(programmingLanguage);
        dest.writeInt(reputation);
        dest.writeString(codeLevel);
        dest.writeString(platform);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeInt(accountComplete);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String token) {
        this.fcmToken = token;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(String codeLevel) {
        this.codeLevel = codeLevel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public int getAccountComplete() {
        return accountComplete;
    }

    public void setAccountComplete(int accountComplete) {
        this.accountComplete = accountComplete;
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

    public Contact toContact() {
        Contact c = new Contact();
        c.setUserId(id);
        c.setUsername(username);
        c.setUserUrlPhoto(urlPhoto);
        return c;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public Map<String, Boolean> getFavoritesCategory() {
        if (favoritesCategory == null) {
            favoritesCategory = new HashMap<>();
            return favoritesCategory;
        }
        return favoritesCategory;
    }

    public void addCategory(String category) {
        favoritesCategory.put(category, true);
    }

    public void removeCategory(String category) {
        favoritesCategory.remove(category);
    }
}
