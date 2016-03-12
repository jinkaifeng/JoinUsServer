package com.northgatecode.joinus.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by qianliang on 12/3/2016.
 */
public class JpaHelper {
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("com.northgatecode.joinus.jpa");
        } catch (ExceptionInInitializerError e) {
            throw e;
        }
    }

    public static EntityManagerFactory getFactory() {
        return entityManagerFactory;
    }
}
