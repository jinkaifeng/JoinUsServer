package com.northgatecode.joinus.auth;

import org.bson.types.ObjectId;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 18/2/2016.
 */
public class UserPrincipal implements Principal {
    private ObjectId id;
    private String name;
    private List<String> roles;

    public UserPrincipal() {
        roles = new ArrayList<>();
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }


}
