package com.northgatecode.joinus.dao;

import javax.persistence.*;
import java.util.List;

/**
 * Created by qianliang on 21/2/2016.
 */
@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;

    @Column(name = "name", length = 20)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    private transient List<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
