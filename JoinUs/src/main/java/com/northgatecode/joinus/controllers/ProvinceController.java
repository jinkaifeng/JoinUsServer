package com.northgatecode.joinus.controllers;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by qianliang on 2/3/2016.
 */
@Path("province")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProvinceController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @GET
    @Path("{provinceId}")
    public Response getProvince(@PathParam("provinceId") int provinceId) {

        return Response.ok().build();
    }
}
