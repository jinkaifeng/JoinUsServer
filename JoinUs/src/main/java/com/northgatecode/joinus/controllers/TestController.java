package com.northgatecode.joinus.controllers;

import com.mongodb.MongoClient;
import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.mongodb.Category;
import com.northgatecode.joinus.mongodb.Gender;
import com.northgatecode.joinus.mongodb.Post;
import com.northgatecode.joinus.mongodb.Role;
import com.northgatecode.joinus.utils.JpaHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;

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
        Datastore datastore = MorphiaHelper.getDatastore();

        datastore.save(new Role(1, "临时用户"));
        datastore.save(new Role(2, "注册用户"));
        datastore.save(new Role(3, "付费用户"));
        datastore.save(new Role(8, "VIP用户"));

        datastore.save(new Gender(1, "保密"));
        datastore.save(new Gender(2, "男"));
        datastore.save(new Gender(3, "女"));

        datastore.save(new Category(1, "手机数码"));
        datastore.save(new Category(2, "计算机技术"));
        datastore.save(new Category(3, "黑科技"));
        datastore.save(new Category(4, "娱乐明星"));
        datastore.save(new Category(5, "电影电视"));
        datastore.save(new Category(6, "体育"));
        datastore.save(new Category(7, "游戏"));
        datastore.save(new Category(8, "动漫"));
        datastore.save(new Category(9, "高校"));
        datastore.save(new Category(100, "其他"));


        return "Database successfully initialized.";
    }

    @GET
    @Path("mongoDB")
    @Produces(MediaType.TEXT_PLAIN)
    public String mongoDB() {

        Post post = new Post();

        final Morphia morphia = new Morphia();
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("com.northgatecode.joinus.mongodb");
        // create the Datastore connecting to the default port on the local host
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "joinus");
        datastore.ensureIndexes();

        datastore.save(post);


        return "OK";
    }
}
