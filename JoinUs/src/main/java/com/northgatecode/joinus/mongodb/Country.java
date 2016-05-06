package com.northgatecode.joinus.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

/**
 * Created by qianliang on 30/4/2016.
 */

@Entity(noClassnameStored = true)
@Indexes({
        @Index(fields = @Field(value = "name")),
        @Index(fields = @Field(value = "$**", type = IndexType.TEXT))
})
public class Country {
    @Id
    private ObjectId id;
    private String name;
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
