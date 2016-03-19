package com.northgatecode.joinus.services;

import com.northgatecode.joinus.dao.User;
import com.northgatecode.joinus.utils.JedisHelper;
import com.northgatecode.joinus.utils.JpaHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserService {
    public static String getKey(int id) {
        return User.class.getName() + ":" + id;
    }
    public static String getMobileKey(String mobile) {
        return User.class.getName() + ":mobile:" + mobile;
    }
    public static int expireInSeconds = 60 * 30;

    public static User getById(int id) {
        String key = getKey(id);

        User user = JedisHelper.get(key, User.class);

        if (user != null) {
            return user;
        }

        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            user = entityManager.find(User.class, id);
        } finally {
            entityManager.close();
        }

        if (user != null) {
            cacheData(user);
        }

        return user;
    }

    public static User getByMobile(String mobile) {
        User user = null;

        String mobileKey = getMobileKey(mobile);
        String key = JedisHelper.get(mobileKey);
        if (key != null && key.length() > 0) {
            user = JedisHelper.get(key, User.class);
        }

        if (user != null) {
            return user;
        }

        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery("select u from User as u where u.mobile like '" + mobile + "'", User.class);
            List<User> users = query.getResultList();
            if (users.size() > 0) {
                user = users.get(0);
            }
        } finally {
            entityManager.close();
        }

        if (user != null) {
            cacheData(user);
        }

        return user;
    }

    public static User reloadById(int id) {
        String key = getKey(id);

        JedisHelper.del(key);

        User user = null;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            user = entityManager.find(User.class, id);
        } finally {
            entityManager.close();
        }

        if (user != null) {
            cacheData(user);
        }
        return user;
    }

    public static void refreshToken(User user) {
        if (user != null && (user.getTokenExpDate() == null || user.getTokenExpDate().compareTo(new Date()) < 0)) {
            user.setToken(RandomStringUtils.randomAlphanumeric(64));
            user.setTokenExpDate(DateUtils.addMinutes(new Date(), 30)); // expire after 30 minutes
        }
    }

    public static Boolean verifyPassword(User user, String password) {
        String hashedPassword = DigestUtils.md5Hex(password + user.getSalt());
        if (hashedPassword.equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    private static void cacheData(User user) {
        String key = getKey(user.getId());
        String mobileKey = getMobileKey(user.getMobile());

        try (Jedis jedis = JedisHelper.getResource()) {
            jedis.set(key, JedisHelper.getGson().toJson(user));
            jedis.expire(key, expireInSeconds);
            jedis.set(mobileKey, key);
            jedis.expire(mobileKey, expireInSeconds);
        }
    }
}
