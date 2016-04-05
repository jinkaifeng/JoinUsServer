package com.northgatecode.joinus.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;

/**
 * Created by qianliang on 1/4/2016.
 */
@Entity(noClassnameStored = true)
@Indexes({
        @Index(fields = @Field(value = "forumId")),
        @Index(fields = @Field(value = "categoryId"))
})
public class ForumCategory {
    @Id
    private ObjectId id;
    private ObjectId forumId;
    private long categoryId;

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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
