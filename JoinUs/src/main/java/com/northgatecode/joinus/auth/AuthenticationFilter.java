package com.northgatecode.joinus.auth;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by qianliang on 19/3/2016.
 */

@Provider
@Authenticated
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        if (!containerRequestContext.getHeaders().containsKey("user_id")
                || !containerRequestContext.getHeaders().containsKey("security_token")) {
            throw new NotAuthorizedException("invalid user_id or security_token", Response.noContent());
        }
        String userId = containerRequestContext.getHeaderString("user_id");
        String token = containerRequestContext.getHeaderString("security_token");
        if (userId == null || userId.length() == 0 || token == null || token.length() == 0) {
            throw new NotAuthorizedException("invalid user_id or security_token", Response.noContent());
        }

//        User user = UserService.getById(Integer.parseInt(userId));
//
//        if (user.getTokenExpDate().compareTo(new Date()) < 0 || !user.getToken().equals(token)) {
//            throw new NotAuthorizedException("invalid user_id or security_token", Response.noContent());
//        }
//
//        UserPrincipal userPrincipal = new UserPrincipal();
//        userPrincipal.setId(user.getId());
//        userPrincipal.setName(user.getName());
//        for (Role role : user.getRoles()) {
//            userPrincipal.addRole(role.getName());
//        }
//
//        RoleBasedSecurityContext securityContext = new RoleBasedSecurityContext(userPrincipal);
//
//        containerRequestContext.setSecurityContext(securityContext);
    }

}

