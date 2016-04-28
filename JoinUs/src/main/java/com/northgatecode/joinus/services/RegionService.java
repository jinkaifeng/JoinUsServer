package com.northgatecode.joinus.services;

import com.northgatecode.joinus.dto.region.CityItem;
import com.northgatecode.joinus.dto.region.ProvinceItem;
import com.northgatecode.joinus.dto.region.ProvinceList;
import com.northgatecode.joinus.mongodb.City;
import com.northgatecode.joinus.mongodb.Province;
import com.northgatecode.joinus.utils.JedisHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 8/4/2016.
 */
public class RegionService {
    public static ProvinceList getProvinceList() {
        ProvinceList provinceList = JedisHelper.get("provinceList", ProvinceList.class);

        if (provinceList == null) {
            provinceList = new ProvinceList();
            provinceList.setProvinceItems(new ArrayList<ProvinceItem>());

            Datastore datastore = MorphiaHelper.getDatastore();
            List<Province> provinces = datastore.createQuery(Province.class).asList();
            for (Province province : provinces) {
                ProvinceItem provinceItem = new ProvinceItem(province);
                provinceItem.setCityItems(new ArrayList<CityItem>());
                List<City> cities = datastore.createQuery(City.class).field("provinceId").equal(province.getId()).asList();
                for (City city : cities) {
                    provinceItem.getCityItems().add(new CityItem(city));
                }
                provinceList.getProvinceItems().add(provinceItem);
            }

            JedisHelper.set("provinceList", provinceList, 60 * 60);
        }

        return provinceList;
    }
}
