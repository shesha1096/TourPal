package com.example.shesha.tourpal.Model;

public class Place {
    String description;
    String image;

    public Place(String description, String image) {
        this.description = description;
        this.image = image;
    }

    public Place() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
