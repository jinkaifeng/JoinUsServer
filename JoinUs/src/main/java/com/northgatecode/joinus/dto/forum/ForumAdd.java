package com.northgatecode.joinus.dto.forum;

import com.northgatecode.joinus.dto.UploadImage;
import com.northgatecode.joinus.mongodb.Category;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 2/4/2016.
 */
public class ForumAdd {
    private String name;
    private String desc;
    private ObjectId iconImageId;
    private List<Integer> categoryIds;

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

    public ObjectId getIconImageId() {
        return iconImageId;
    }

    public void setIconImageId(ObjectId iconImageId) {
        this.iconImageId = iconImageId;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
