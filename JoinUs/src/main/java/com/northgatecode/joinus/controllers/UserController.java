package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dao.User;
import com.northgatecode.joinus.dto.UserPassword;
import com.northgatecode.joinus.dto.UserProfile;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.utils.JpaHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by qianliang on 2/3/2016.
 */
@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @GET
    @Path("myProfile")
    @Authenticated
    public Response getMyProfile(@Context SecurityContext securityContext) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        int userId = userPrincipal.getId();
        User user = UserService.getById(userId);
        return Response.ok(new UserProfile(user)).build();
    }

    @POST
    @Path("password")
    @Authenticated
    public Response updatePassword(@Context SecurityContext securityContext, UserPassword userPassword) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        int userId = userPrincipal.getId();

        User user;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            user = entityManager.find(User.class, userId);

            if (user.getPassword() != null && !UserService.verifyPassword(user, userPassword.getOldPassword()))
            {
                throw new BadRequestException("原密码错误");
            }
            // validate password
            if (userPassword.getNewPassword().length() < 6) {
                throw new BadRequestException("无效的新密码");
            }

            String salt = RandomStringUtils.randomAlphanumeric(4);
            String hashedPassword = DigestUtils.md5Hex(userPassword.getNewPassword() + salt);

            entityManager.getTransaction().begin();
            user.setPassword(hashedPassword);
            user.setSalt(salt);
            user.setLastUpdateDate(new Date());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }
        UserService.cacheData(user);
        return Response.ok().build();
    }
}
