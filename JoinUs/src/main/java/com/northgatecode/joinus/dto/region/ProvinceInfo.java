package com.northgatecode.joinus.dto.region;

import com.northgatecode.joinus.mongodb.Province;

/**
 * Created by qianliang on 16/4/2016.
 */
public class ProvinceInfo {
    private int id;
    private String name;

    public ProvinceInfo(Province province) {
        this.id = province.getId();
        this.name = province.getName();
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
