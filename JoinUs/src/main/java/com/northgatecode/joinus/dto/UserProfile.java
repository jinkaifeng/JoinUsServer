package com.northgatecode.joinus.dto;

import com.northgatecode.joinus.dao.City;
import com.northgatecode.joinus.dao.User;

import java.util.Date;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserProfile {
    private int id;
    private String mobile;
    private String email;
    private String name;
    private String photo;
    private City city;
    private Boolean acceptNotification;
    private Date lastUpdateDate;

    public UserProfile() {}

    public UserProfile(User user) {
        this.id = user.getId();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.name = user.getName();
        this.photo = user.getPhoto();
        this.city = user.getCity();
        this.acceptNotification = user.getAcceptNotification();
        this.lastUpdateDate = user.getLastUpdateDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Boolean getAcceptNotification() {
        return acceptNotification;
    }

    public void setAcceptNotification(Boolean acceptNotification) {
        this.acceptNotification = acceptNotification;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
