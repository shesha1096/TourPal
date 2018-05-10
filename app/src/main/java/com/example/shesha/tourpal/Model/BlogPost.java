package com.example.shesha.tourpal.Model;

import com.example.shesha.tourpal.BlogPostID;

public class BlogPost extends com.example.shesha.tourpal.BlogPostID {
    public String emailid;
    public String blogimage;
    public String description;
    public String timestamp;
    public String imageuri;

    public BlogPost(String emaiilid, String blogimage, String description, String timestamp) {
        this.emailid = emaiilid;
        this.blogimage = blogimage;
        this.description = description;
        this.timestamp = timestamp;
    }

    public BlogPost() {
    }

    public String getEmailid() {
        return emailid;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getBlogimage() {
        return blogimage;
    }

    public void setBlogimage(String blogimage) {
        this.blogimage = blogimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
