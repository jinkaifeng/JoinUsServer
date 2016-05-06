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

    public static String getKey(String mobile, String type) {
        return "JoinUs.VerifyCode:" + mobile + ":" + type;
    }

    public static String generateCodeAndSendSMS(String mobile, String type) {
        String code = RandomStringUtils.randomNumeric(6);
//        String code = "123456";
        logger.log(Level.INFO, "Verify Code Generated for mobile: " + mobile + " code: " + code);

        logger.log(Level.INFO, "Sending Verify Code to mobile: " + mobile + " code: " + code);
        SMSService.sendVerifyCodeSMS(mobile, code);

        try(Jedis jedis = JedisHelper.getResource()) {
            String key = getKey(mobile, type);
            jedis.set(key, code);
            jedis.expire(key, 60 * 5);
        }


        return code;
    }

    public static String generateCodeAndSendEmail(String email, String type) {
        String code = RandomStringUtils.randomNumeric(6);
//        String code = "123456";
        logger.log(Level.INFO, "Verify Code Generated for email: " + email + " code: " + code);

        try(Jedis jedis = JedisHelper.getResource()) {
            String key = getKey(email, type);
            jedis.set(key, code);
            jedis.expire(key, 60 * 5);
        }

        logger.log(Level.INFO, "Sending Verify Code to mail: " + email + " code: " + code);
        MailService.sendSingleMail(email, "Join Us 验证码", "尊敬的会员, 您好! \n"
                + "您申请的验证码是: " + code + ", 验证码五分钟内有效, \n"
                + "为保证您的帐户安全请勿泄漏！");
        return code;
    }

    public static Boolean verify(String mobile, String type, String code) {

        try(Jedis jedis = JedisHelper.getResource()) {
            String key = getKey(mobile, type);
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
