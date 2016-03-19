package com.northgatecode.joinus.dao;

import javax.persistence.*;
import java.util.List;

/**
 * Created by qianliang on 21/2/2016.
 */
@Entity
@Table(name = "province")
public class Province {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 20)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    private transient List<City> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
