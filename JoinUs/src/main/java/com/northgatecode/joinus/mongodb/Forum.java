package com.northgatecode.joinus.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;
import org.mongodb.morphia.utils.IndexType;

import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 24/3/2016.
 */
@Entity(noClassnameStored = true)
@Indexes({
        @Index(fields = @Field(value = "name")),
        @Index(fields = @Field(value = "activity", type = IndexType.DESC)),
        @Index(fields = @Field(value = "createdByUserId")),
        @Index(fields = @Field(value = "createDate", type = IndexType.DESC)),
        @Index(fields = @Field(value = "$**", type = IndexType.TEXT))
})
public class Forum {
    @Id
    private ObjectId id;
    private String name;
    private String desc;
    private ObjectId iconImageId;
    private int posts;
    private int members;
    private int activity;
    private String categories;
    private boolean deleted;
    private ObjectId createdByUserId;
    private Date createDate;

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

    public ObjectId getIconImageId() {
        return iconImageId;
    }

    public void setIconImageId(ObjectId iconImageId) {
        this.iconImageId = iconImageId;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ObjectId getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(ObjectId createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
