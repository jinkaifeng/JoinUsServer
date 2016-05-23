package com.northgatecode.joinus.dto;

import com.northgatecode.joinus.mongodb.Image;
import org.bson.types.ObjectId;

/**
 * Created by qianliang on 20/5/2016.
 */
public class ImageItem {
    private String name;
    private int width;
    private int height;

    public ImageItem() {
    }

    public ImageItem(Image image) {
        this.name = image.getName();
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
