package com.northgatecode.joinus.exceptions;

import com.northgatecode.joinus.dto.Message;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by qianliang on 12/3/2016.
 */
//@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Message(throwable.getMessage())).build();
    }
}
