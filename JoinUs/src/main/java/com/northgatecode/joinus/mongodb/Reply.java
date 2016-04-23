package com.northgatecode.joinus.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * Created by qianliang on 21/4/2016.
 */
public class Reply {
    @Id
    private ObjectId id;
    private ObjectId postId;
    private ObjectId repliedByUserId;
    private String content;
    private Date replyDate;
    private boolean deleted;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getPostId() {
        return postId;
    }

    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }

    public ObjectId getRepliedByUserId() {
        return repliedByUserId;
    }

    public void setRepliedByUserId(ObjectId repliedByUserId) {
        this.repliedByUserId = repliedByUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
