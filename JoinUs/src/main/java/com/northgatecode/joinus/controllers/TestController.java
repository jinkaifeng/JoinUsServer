package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;

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
}
