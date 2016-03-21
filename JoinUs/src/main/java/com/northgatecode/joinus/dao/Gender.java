package com.northgatecode.joinus.dao;

import javax.persistence.*;
import java.util.List;

/**
 * Created by qianliang on 21/3/2016.
 */
@Entity
@Table(name = "gender")
public class Gender {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 20)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gender")
    private transient List<User> users;

    public Gender() {}

    public Gender(int id, String name) {
        this.id = id;
        this.name = name;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
