package com.northgatecode.joinus.dto.user;

/**
 * Created by qianliang on 2/3/2016.
 */
public class UserPassword {
    private String currentPassword;
    private String password;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
