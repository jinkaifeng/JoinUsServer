package com.northgatecode.joinus.dto;

import com.northgatecode.joinus.dao.Role;

/**
 * Created by qianliang on 22/2/2016.
 */
public class RoleInfo {
    private int id;
    private String name;

    public RoleInfo(){}

    public RoleInfo(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public RoleInfo(int id, String name) {
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
}
