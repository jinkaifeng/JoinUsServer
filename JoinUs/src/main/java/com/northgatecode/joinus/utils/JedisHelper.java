package com.northgatecode.joinus.utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.*;

/**
 * Created by qianliang on 19/3/2016.
 */
public class JedisHelper {
    private static final JedisHelper jedisHelper;

    static {
        try {
            jedisHelper = new JedisHelper();
        } catch (Exception e) {
            throw e;
        }
    }

    private JedisPool jedisPool;

    private JedisHelper() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(2);
        config.setMinIdle(0);
        config.setBlockWhenExhausted(true);
        this.jedisPool = new JedisPool(config, "localhost", 6379, 2000, "joinUS123");
    }

    final public static Jedis getResource() {
        return jedisHelper.jedisPool.getResource();
    }

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                    .create();
        }
        return gson;
    }

    public static <T> T get(String key, Class<T> classOfT) {
        try (Jedis jedis = getResource()) {
            return getGson().fromJson(jedis.get(key), classOfT);
        }
    }

    public static String get(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.get(key);
        }
    }

    public static void set(String key, Object obj, int expireInSeconds) {
        try (Jedis jedis = getResource()) {
            jedis.set(key, getGson().toJson(obj));
            jedis.expire(key, expireInSeconds);
        }
    }

    public static void set(String key, String value, int expireInSeconds) {
        try (Jedis jedis = getResource()) {
            jedis.set(key, value);
            jedis.expire(key, expireInSeconds);
        }
    }

    public static void del(String key) {
        try (Jedis jedis = JedisHelper.getResource()) {
            jedis.del(key);
        }
    }

}