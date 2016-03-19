package com.northgatecode.joinus.providers;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
/**
 * Created by qianliang on 19/3/2016.
 */
@Provider
public class PoweredByResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        responseContext.getHeaders().add("X-Powered-By", "North Gate Code");
    }
}
