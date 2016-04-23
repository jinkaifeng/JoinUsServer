package com.northgatecode.joinus.dto.user;

import com.northgatecode.joinus.dto.region.CityInfo;
import com.northgatecode.joinus.dto.region.ProvinceInfo;
import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserProfile {
    private ObjectId id;
    private String mobile;
    private String email;
    private String name;
    private String photo;
    private Gender gender;
    private Role role;
    private CityInfo city;
    private Date lastUpdateDate;
    private Date registerDate;

    public UserProfile() {}

    public UserProfile(User user) {
        Datastore datastore = MorphiaHelper.getDatastore();
        this.id = user.getId();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.name = user.getName();
        this.photo = ImageService.getImageName(user.getPhotoImageId());
        if (user.getCityId() != 0) {
            City city = datastore.find(City.class).field("id").equal(user.getCityId()).get();
            this.city = new CityInfo(city);
            this.city.setProvince(new ProvinceInfo(datastore.find(Province.class).field("id").equal(city.getProvinceId()).get()));
        }
        if (user.getGenderId() != 0) {
            this.gender = datastore.find(Gender.class).field("id").equal(user.getGenderId()).get();
        }
        if (user.getRoleId() != 0) {
            this.role = datastore.find(Role.class).field("id").equal(user.getRoleId()).get();
        }
        this.lastUpdateDate = user.getLastUpdateDate();
        this.registerDate = user.getRegisterDate();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public CityInfo getCity() {
        return city;
    }

    public void setCity(CityInfo city) {
        this.city = city;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
