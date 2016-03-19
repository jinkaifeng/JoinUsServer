package com.northgatecode.joinus.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by qianliang on 20/2/2016.
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

    public static void persist(Object obj) {
        EntityManager entityManager = getFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(obj);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw ex;
        } finally {
            entityManager.close();
        }
    }

    public static <T> T find(Class<T> model, Object id) {
        EntityManager entityManager = getFactory().createEntityManager();
        try {
            return entityManager.find(model, id);
        } finally {
            entityManager.close();
        }
    }

    public static <T> T remove(Class<T> model, int id) {
        EntityManager entityManager = getFactory().createEntityManager();
        T obj;
        try {
            obj = entityManager.find(model, id);
            if (obj != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(obj);
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw ex;
        } finally {
            entityManager.close();
        }
        return obj;
    }


}
