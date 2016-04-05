package com.northgatecode.joinus.dto.user;

import org.bson.types.ObjectId;

/**
 * Created by qianliang on 28/3/2016.
 */
public class UserPhoto {
    private ObjectId imageId;

    public ObjectId getImageId() {
        return imageId;
    }

    public void setImageId(ObjectId imageId) {
        this.imageId = imageId;
    }
}
