package com.northgatecode.joinus.services;

import com.google.gson.Gson;
import com.northgatecode.joinus.dao.Province;
import com.northgatecode.joinus.utils.JedisHelper;
import com.northgatecode.joinus.utils.JpaHelper;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;

/**
 * Created by qianliang on 24/2/2016.
 */
public class ProvinceService {

    public static Province getById(int id) {
        String key = Province.class.getName() + ":" + id;

        try (Jedis jedis = JedisHelper.getResource()) {
            if (jedis.exists(key))
            {
                Gson gson = new Gson();
                return gson.fromJson(jedis.get(key), Province.class);
            }
        }

        Province province;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            province = entityManager.find(Province.class, id);
        }
        finally {
            entityManager.close();
        }

        if (province != null) {
            try (Jedis jedis = JedisHelper.getResource()) {

                Gson gson = new Gson();
                jedis.set(key, gson.toJson(province));
                jedis.expire(key, 60 * 1);
            }
        }

        return province;
    }
}
