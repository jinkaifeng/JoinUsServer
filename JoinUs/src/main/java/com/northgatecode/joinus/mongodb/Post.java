package com.northgatecode.joinus.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 24/3/2016.
 */
@Entity(noClassnameStored = true)
@Indexes({
        @Index(fields = @Field(value = "topicId")),
        @Index(fields = @Field(value = "postDate", type = IndexType.DESC))
})
public class Post {
    @Id
    private ObjectId id;
    private ObjectId topicId;
    private ObjectId postedByUserId;
    private String content;
    private Date postDate;
    private boolean deleted;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getTopicId() {
        return topicId;
    }

    public void setTopicId(ObjectId topicId) {
        this.topicId = topicId;
    }

    public ObjectId getPostedByUserId() {
        return postedByUserId;
    }

    public void setPostedByUserId(ObjectId postedByUserId) {
        this.postedByUserId = postedByUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
