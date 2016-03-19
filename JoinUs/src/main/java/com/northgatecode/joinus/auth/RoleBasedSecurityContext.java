package com.northgatecode.joinus.auth;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by qianliang on 18/2/2016.
 */
public class RoleBasedSecurityContext implements SecurityContext {
    private UserPrincipal user;

    public RoleBasedSecurityContext(UserPrincipal user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {return this.user;}

    @Override
    public boolean isUserInRole(String s) {
        if (user.getRoles() != null) {
            return user.getRoles().contains(s);
        }
        return false;
    }

    @Override
    public boolean isSecure() {return false;}

    @Override
    public String getAuthenticationScheme() {
        return "custom";
    }
}
