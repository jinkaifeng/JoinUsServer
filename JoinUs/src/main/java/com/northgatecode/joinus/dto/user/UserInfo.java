package com.northgatecode.joinus.dto.user;

import com.northgatecode.joinus.dto.region.CityInfo;
import com.northgatecode.joinus.dto.region.ProvinceInfo;
import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.Date;

/**
 * Created by qianliang on 20/4/2016.
 */
public class UserInfo {
    private ObjectId id;
    private String name;
    private String photo;
    private Gender gender;
    private CityInfo city;
    private Date registerDate;

    public UserInfo() {}

    public UserInfo(User user) {
        Datastore datastore = MorphiaHelper.getDatastore();
        this.id = user.getId();
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
        this.registerDate = user.getRegisterDate();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public CityInfo getCity() {
        return city;
    }

    public void setCity(CityInfo city) {
        this.city = city;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
