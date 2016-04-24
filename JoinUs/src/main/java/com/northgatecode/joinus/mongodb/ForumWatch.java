package com.northgatecode.joinus.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 24/3/2016.
 */
@Indexes({
        @Index(fields = @Field(value = "forumId")),
        @Index(fields = @Field(value = "userId")),
        @Index(fields = @Field(value = "lastPostDate", type = IndexType.DESC))
})
@Entity(noClassnameStored = true)
public class ForumWatch {
    @Id
    private ObjectId id;
    private ObjectId forumId;
    private ObjectId userId;
    private int level;
    private int posts;
    private int score;
    private boolean isAdmin;
    private Date joinDate;
    private Date lastPostDate;
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

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
