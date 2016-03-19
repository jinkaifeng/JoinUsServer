package com.northgatecode.joinus.dto;

import com.northgatecode.joinus.dao.User;

import java.util.Date;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserToken {
    private int id;
    private String token;
    private Date tokenExpDate;

    public UserToken() {}

    public UserToken(User user) {
        this.id = user.getId();
        this.token = user.getToken();
        this.tokenExpDate = user.getTokenExpDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenExpDate() {
        return tokenExpDate;
    }

    public void setTokenExpDate(Date tokenExpDate) {
        this.tokenExpDate = tokenExpDate;
    }
}
