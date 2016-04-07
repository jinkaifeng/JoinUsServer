package com.northgatecode.joinus.mongodb;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.List;
/**
 * Created by qianliang on 25/3/2016.
 */
@Entity(noClassnameStored = true)
public class Role {
    @Id
    private int id;
    private String name;

    public Role() {
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
