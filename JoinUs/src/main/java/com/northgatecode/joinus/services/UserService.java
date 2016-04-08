package com.northgatecode.joinus.services;

import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.JedisHelper;
import com.northgatecode.joinus.utils.JpaHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserService {

    public static User getById(ObjectId id) {
        return MorphiaHelper.getDatastore().find(User.class).field("id").equal(id).get();
    }

    public static User getByMobile(String mobile) {

        return MorphiaHelper.getDatastore().find(User.class).field("mobile").equal(mobile).get();
    }

    public static User getUserFromContext(SecurityContext securityContext) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();
        User user = datastore.find(User.class).field("id").equal(userId).get();

        return user;
    }

    public static void refreshToken(User user) {
        if (user != null && (user.getTokenExpDate() == null || user.getTokenExpDate().compareTo(new Date()) < 0)) {
            generateToken(user);
            MorphiaHelper.getDatastore().save(user);
        }
    }

    public static void generateToken(User user) {
        user.setToken(RandomStringUtils.randomAlphanumeric(64));
        user.setTokenExpDate(DateUtils.addDays(new Date(), 7));
    }

    public static Boolean verifyPassword(User user, String password) {
        String hashedPassword = DigestUtils.md5Hex(password + user.getSalt());
        if (hashedPassword.equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}
