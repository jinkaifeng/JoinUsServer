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
    private Date lastUpdateDate;
    private boolean deleted;

}
