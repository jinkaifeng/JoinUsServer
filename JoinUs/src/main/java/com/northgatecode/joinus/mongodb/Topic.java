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
        @Index(fields = @Field(value = "forumId")),
        @Index(fields = @Field(value = "lastPostDate", type = IndexType.DESC)),
})
public class Topic {
    @Id
    private int id;
    private ObjectId forumId;
    private String title;
    private ObjectId postedByUserId;
    private int views;
    private ObjectId firstPostId;
    private Date firstPostDate;
    private ObjectId lastPostId;
    private Date lastPostDate;
    private List<String> images;
    private boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObjectId getForumId() {
        return forumId;
    }

    public void setForumId(ObjectId forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ObjectId getPostedByUserId() {
        return postedByUserId;
    }

    public void setPostedByUserId(ObjectId postedByUserId) {
        this.postedByUserId = postedByUserId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public ObjectId getFirstPostId() {
        return firstPostId;
    }

    public void setFirstPostId(ObjectId firstPostId) {
        this.firstPostId = firstPostId;
    }

    public Date getFirstPostDate() {
        return firstPostDate;
    }

    public void setFirstPostDate(Date firstPostDate) {
        this.firstPostDate = firstPostDate;
    }

    public ObjectId getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(ObjectId lastPostId) {
        this.lastPostId = lastPostId;
    }

    public Date getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
