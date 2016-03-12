package com.northgatecode.joinus.exceptions;

import com.northgatecode.joinus.models.Message;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by qianliang on 12/3/2016.
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException e) {
        return Response.status(e.getResponse().getStatus()).entity(new Message(e.getMessage())).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}