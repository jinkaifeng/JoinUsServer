package com.northgatecode.joinus.dto;

import com.northgatecode.joinus.dao.User;

import java.util.Date;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserToken {
    private int userId;
    private String securityToken;
    private Date experiationDate;

    public UserToken() {}

    public UserToken(User user) {
        this.userId = user.getId();
        this.securityToken = user.getToken();
        this.experiationDate = user.getTokenExpDate();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public Date getExperiationDate() {
        return experiationDate;
    }

    public void setExperiationDate(Date experiationDate) {
        this.experiationDate = experiationDate;
    }
}
