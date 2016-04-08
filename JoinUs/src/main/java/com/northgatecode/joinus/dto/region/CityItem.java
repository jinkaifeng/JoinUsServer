package com.northgatecode.joinus.dto.region;

import com.northgatecode.joinus.mongodb.City;

/**
 * Created by qianliang on 8/4/2016.
 */
public class CityItem {
    private int id;
    private String name;

    public CityItem() {
    }

    public CityItem(City city) {
        this.id = city.getId();
        this.name = city.getName();
    }

    public int getId() {
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
