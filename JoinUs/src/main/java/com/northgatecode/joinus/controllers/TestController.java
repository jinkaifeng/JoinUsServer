package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.dao.Gender;
import com.northgatecode.joinus.dao.Role;
import com.northgatecode.joinus.utils.JpaHelper;
import com.sun.tools.javac.jvm.Gen;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by qianliang on 19/3/2016.
 */
@Path("test")
public class TestController {
    @Path("hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello, Jersey is working.";
    }

    @Path("query")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String query(@QueryParam("name") String name) {
        return "Query String: name=" + name;
    }

    @GET
    @Path("auth")
    @Authenticated
    @Produces(MediaType.TEXT_PLAIN)
    public String auth(@Context SecurityContext sc) {
        return "User Name:" + sc.getUserPrincipal().getName();
    }

    @GET
    @Path("initDB")
    @Produces(MediaType.TEXT_PLAIN)
    public String initdb() {
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(new Role(1, "临时用户"));
            entityManager.merge(new Role(2, "注册用户"));
            entityManager.merge(new Role(3, "付费用户"));
            entityManager.merge(new Role(8, "VIP用户"));

            entityManager.merge(new Gender(1, "保密"));
            entityManager.merge(new Gender(2, "男"));
            entityManager.merge(new Gender(3, "女"));

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw ex;
        } finally {
            entityManager.close();
        }
        return "Database successfully initialized.";
    }
}
