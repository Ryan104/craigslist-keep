package com.example.ryanelliott.mycraigsearch;

/**
 * Created by ryand on 10/4/2016.
 */

public class Listing {
    private String title;
    private String datePosted;
    private String description;
    private String postLink;
    private String imageLink;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Title: " + this.getTitle() + " | Date: " + this.getDatePosted() + " | Link: " +
                this.getPostLink() + " | Description: " + this.getDescription();
    }
}
