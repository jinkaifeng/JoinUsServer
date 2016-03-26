package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dto.*;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.JpaHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by qianliang on 2/3/2016.
 */
@Path("login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginController {
    @GET
    @Path("{mobile}")
    public Response getVerifyCode(@PathParam("mobile") String mobile) {

        List<User> existingUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                .field("mobile").equal(mobile).asList();

            if (existingUsers.size() == 0) {
                throw new BadRequestException("此号码还没有注册, 请注册.");
            }

        VerifyCodeService.generateCodeAndSendSMS(mobile);
        return Response.ok(new Message("验证码已发送, 有效期5分钟.")).build();
    }

    @POST
    @Path("verifyCode")
    public Response verifyCode (MobileVerifyCode mobileVerifyCode) {
        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误");
        }
        User user = UserService.getByMobile(mobileVerifyCode.getMobile());
        UserService.refreshToken(user);
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

    @POST
    @Path("password")
    public Response password (MobilePassword mobilePassword) {
        User user = UserService.getByMobile(mobilePassword.getMobile());
        if (user == null) {
            throw new BadRequestException("此号码还没有注册,请先注册");
        }

        if (user.getPassword() == null || user.getPassword().length() == 0) {
            throw new BadRequestException("您还没有设置密码");
        }

        if (!UserService.verifyPassword(user, mobilePassword.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        UserService.refreshToken(user);
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }
}
