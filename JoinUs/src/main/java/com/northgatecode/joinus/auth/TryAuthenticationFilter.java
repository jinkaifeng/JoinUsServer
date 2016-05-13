package com.northgatecode.joinus.auth;

import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;

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
 * Created by qianliang on 10/5/2016.
 */
@Provider
@TryAuthenticate
@Priority(Priorities.AUTHENTICATION)
public class TryAuthenticationFilter implements ContainerRequestFilter{
    private Logger logger = Logger.getLogger(this.getClass().toString());
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        if (!containerRequestContext.getHeaders().containsKey("user-id")
                || !containerRequestContext.getHeaders().containsKey("security-token")) {
            return;
        }

        String userId = containerRequestContext.getHeaderString("user-id");
        String token = containerRequestContext.getHeaderString("security-token");
        if (userId == null || userId.length() != 24 || token == null || token.length() != 64) {
            return;
        }

        User user = MorphiaHelper.getDatastore().find(User.class).field("id").equal(new ObjectId(userId)).get();

        if (user == null) {
            return;
        }

        if (user.isLocked()) {
            return;
        }

        if (user.getTokenExpDate().compareTo(new Date()) < 0 || !user.getToken().equals(token)) {
            return;
        }

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(user.getId());
        userPrincipal.setName(user.getName());


        userPrincipal.addRole(Integer.toString(user.getRoleId()));

        RoleBasedSecurityContext securityContext = new RoleBasedSecurityContext(userPrincipal);

        containerRequestContext.setSecurityContext(securityContext);
    }
}
