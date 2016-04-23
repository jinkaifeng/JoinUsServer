package com.northgatecode.joinus.dto.region;

import com.northgatecode.joinus.mongodb.City;
import com.northgatecode.joinus.mongodb.Province;

/**
 * Created by qianliang on 16/4/2016.
 */
public class CityInfo {
    private int id;
    private String name;
    private ProvinceInfo province;

    public CityInfo() {
    }

    public CityInfo(City city) {
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

    public ProvinceInfo getProvince() {
        return province;
    }

    public void setProvince(ProvinceInfo province) {
        this.province = province;
    }
}
