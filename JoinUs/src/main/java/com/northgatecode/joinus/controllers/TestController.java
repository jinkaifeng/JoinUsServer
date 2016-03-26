package com.northgatecode.joinus.controllers;

import com.mongodb.MongoClient;
import com.northgatecode.joinus.auth.Authenticated;
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

        return "Database successfully initialized.";
    }

    @GET
    @Path("mongoDB")
    @Produces(MediaType.TEXT_PLAIN)
    public String mongoDB() {

        Post post = new Post();
        post.setContent("test");
        post.setPostDate(new Date());
        post.setLastUpdateDate(new Date());
        post.setDeleted(false);

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
