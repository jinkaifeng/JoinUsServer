package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dao.Province;
import com.northgatecode.joinus.services.ProvinceService;

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

        Province province = ProvinceService.getById(provinceId);

        if (province != null) {
            return Response.ok(province).build();
        } else {
            throw new NotFoundException();
        }
    }
}
