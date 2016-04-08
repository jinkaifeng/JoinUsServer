package com.northgatecode.joinus.dto.region;

import com.northgatecode.joinus.mongodb.Province;

import java.util.List;

/**
 * Created by qianliang on 8/4/2016.
 */
public class ProvinceItem {
    private int id;
    private String name;
    private List<CityItem> cities;

    public ProvinceItem() {
    }

    public ProvinceItem(Province province) {
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

    public List<CityItem> getCities() {
        return cities;
    }

    public void setCities(List<CityItem> cities) {
        this.cities = cities;
    }
}
