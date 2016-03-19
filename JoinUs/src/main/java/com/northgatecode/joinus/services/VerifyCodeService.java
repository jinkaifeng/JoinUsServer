package com.northgatecode.joinus.services;

import com.northgatecode.joinus.utils.JedisHelper;
import org.apache.commons.lang3.RandomStringUtils;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by qianliang on 2/3/2016.
 */
public class VerifyCodeService {

    private static Logger logger = Logger.getLogger(VerifyCodeService.class.getName());

    public static String getKey(String mobile) {
        return "JoinUs.VerifyCode:" + mobile;
    }

    public static String generateCodeAndSendSMS(String mobile) {
        //String code = RandomStringUtils.randomNumeric(6);
        String code = "123456";
        logger.log(Level.INFO, "Verify Code Generated for mobile: " + mobile + " code: " + code);

        try(Jedis jedis = JedisHelper.getResource()) {
            String key = getKey(mobile);
            jedis.set(key, code);
            jedis.expire(key, 60 * 5);
        }

        logger.log(Level.INFO, "Sending Verify Code to mobile: " + mobile + " code: " + code);
        return code;
    }

    public static Boolean verify(String mobile, String code) {

        try(Jedis jedis = JedisHelper.getResource()) {
            String key = getKey(mobile);
            if (!jedis.exists(key)) {
                return false;
            }

            if (jedis.get(key).equals(code)){
                jedis.del(key);
                return true;
            }
        }

        return false;
    }
}
