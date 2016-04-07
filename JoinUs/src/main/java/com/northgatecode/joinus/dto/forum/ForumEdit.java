package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.UploadImage;
import com.northgatecode.joinus.mongodb.Category;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 2/4/2016.
 */
public class ForumEdit {

    private ObjectId id;
    private String name;
    private String desc;
    private UploadImage icon;
    private List<Category> categories;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UploadImage getIcon() {
        return icon;
    }

    public void setIcon(UploadImage icon) {
        this.icon = icon;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
