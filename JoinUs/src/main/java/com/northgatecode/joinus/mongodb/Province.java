package com.northgatecode.joinus.mongodb;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.Date;
import java.util.List;
/**
 * Created by qianliang on 25/3/2016.
 */
@Entity(noClassnameStored = true)
public class Province {
    @Id
    private int id;
    private String name;
    @Reference
    private transient List<City> cities;

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

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
