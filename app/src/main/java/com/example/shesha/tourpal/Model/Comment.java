package com.example.shesha.tourpal.Model;

import java.util.Date;

public class Comment {
    private String message,userid;
    private Date timestamp;
    private String imageuri;

    public Comment() {
    }

    public Comment(String message, String userid, Date timestamp) {
        this.message = message;
        this.userid = userid;
        this.timestamp = timestamp;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
