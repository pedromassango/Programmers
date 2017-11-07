package com.pedromassango.programmers.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

@IgnoreExtraProperties
public class Payment {

    private String id;
    private String paymentId;
    private String author;
    private String authorId;
    private String amount;
    private long timestamp;

    //For Firebase
    public Payment(){

    }

    public Payment(String id, String paymentId, String authorId, String author, String amount, long timestamp) {
        this.id = id;
        this.paymentId = paymentId;
        this.authorId = authorId;
        this.author = author;
        this.amount = amount;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
