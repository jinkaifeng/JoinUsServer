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
        @Index(fields = @Field(value = "onTop", type = IndexType.DESC)),
        @Index(fields = @Field(value = "lastPostDate", type = IndexType.DESC)),
})
public class Topic {
    @Id
    private ObjectId id;
    private ObjectId forumId;
    private String title;
    private ObjectId postedByUserId;
    private int posts;
    private int views;
    private ObjectId firstPostId;
    private Date firstPostDate;
    private ObjectId lastPostId;
    private Date lastPostDate;
    private boolean onTop;
    private boolean deleted;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
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

    public boolean isOnTop() {
        return onTop;
    }

    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
