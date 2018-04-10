package com.example.ssurendran.bakingapp.models;

/**
 * Created by ssurendran on 4/10/18.
 */

public class StepModel {

    private String id;
    private String shortDescription;
    private String description;
    private String videoUrlString;
    private String thumbnailUrlString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrlString() {
        return videoUrlString;
    }

    public void setVideoUrlString(String videoUrlString) {
        this.videoUrlString = videoUrlString;
    }

    public String getThumbnailUrlString() {
        return thumbnailUrlString;
    }

    public void setThumbnailUrlString(String thumbnailUrlString) {
        this.thumbnailUrlString = thumbnailUrlString;
    }
}
