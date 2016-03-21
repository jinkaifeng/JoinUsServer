package com.northgatecode.joinus.dto;

import com.northgatecode.joinus.dao.City;
import com.northgatecode.joinus.dao.Gender;
import com.northgatecode.joinus.dao.Role;
import com.northgatecode.joinus.dao.User;

import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserProfile {
    private int id;
    private String mobile;
    private String email;
    private String name;
    private String photo;
    private Gender gender;
    private City city;
    private Date lastUpdateDate;
    private List<Role> roles;

    public UserProfile() {}

    public UserProfile(User user) {
        this.id = user.getId();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.name = user.getName();
        this.photo = user.getPhoto();
        this.city = user.getCity();
        this.gender = user.getGender();
        this.lastUpdateDate = user.getLastUpdateDate();
        this.roles = user.getRoles();
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
