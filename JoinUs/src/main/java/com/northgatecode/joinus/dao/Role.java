package com.northgatecode.joinus.dao;

import javax.persistence.*;
import java.util.List;

/**
 * Created by qianliang on 20/2/2016.
 */
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private transient List<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
