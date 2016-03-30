package com.northgatecode.joinus.dto;

import org.bson.types.ObjectId;

/**
 * Created by qianliang on 30/3/2016.
 */
public class UploadImage {
    private ObjectId imageId;
    private String name;

    public UploadImage() {
    }

    public UploadImage(ObjectId imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public ObjectId getImageId() {
        return imageId;
    }

    public void setImageId(ObjectId imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
