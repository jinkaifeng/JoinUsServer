package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dto.*;
import com.northgatecode.joinus.dto.user.*;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;
import org.mongodb.morphia.Datastore;

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
    @Path("mobile/{mobile}")
    public Response getVerifyCode(@PathParam("mobile") String mobile) {

        if (!Utils.isValidMobile(mobile)) {
            throw new BadRequestException("无效的手机号码");
        }

        List<User> existingUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                .field("mobile").equal(mobile).asList();

        if (existingUsers.size() == 0) {
            throw new BadRequestException("此号码还没有注册, 请注册.");
        }

        if (existingUsers.get(0).isLocked()) {
            throw new BadRequestException("此账号已被锁定.");
        }

        VerifyCodeService.generateCodeAndSendSMS(mobile, "login");
        return Response.ok(new Message("验证码已发送, 有效期5分钟.")).build();
    }

    @POST
    @Path("mobile/verifyCode")
    public Response verifyCode (MobileVerifyCode mobileVerifyCode) {
        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), "login", mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误");
        }
        User user = MorphiaHelper.getDatastore().find(User.class).field("mobile").equal(mobileVerifyCode.getMobile()).get();
        if (user == null) {
            throw new BadRequestException("此号码还没有注册, 请注册");
        }
        if (user.isLocked()) {
            throw new BadRequestException("此账号已被锁定");
        }
        UserService.refreshToken(user);
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

    @GET
    @Path("email/{email}")
    public Response getEmailVerifyCode(@PathParam("email") String email) {

        if (!Utils.isValidEmail(email)) {
            throw new BadRequestException("无效的邮箱地址");
        }

        List<User> existingUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                .field("email").equalIgnoreCase(email).asList();

        if (existingUsers.size() == 0) {
            throw new BadRequestException("此邮箱还没有注册, 请注册.");
        }

        if (existingUsers.get(0).isLocked()) {
            throw new BadRequestException("此账号已被锁定.");
        }

        VerifyCodeService.generateCodeAndSendEmail(email, "login");
        return Response.ok(new Message("验证码已发送, 有效期5分钟.")).build();
    }

    @POST
    @Path("email/verifyCode")
    public Response emailVerifyCode (EmailVerifyCode emailVerifyCode) {
        Datastore datastore = MorphiaHelper.getDatastore();
        if (!VerifyCodeService.verify(emailVerifyCode.getEmail(), "login", emailVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误");
        }
        User user = datastore.find(User.class).field("email").equalIgnoreCase(emailVerifyCode.getEmail()).get();
        if (user == null) {
            throw new BadRequestException("此邮箱还没有注册, 请注册.");
        }
        if (user.isLocked()) {
            throw new BadRequestException("此账号已被锁定.");
        }
        UserService.refreshToken(user);
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

    @POST
    @Path("password")
    public Response password (AccountPassword accountPassword) {
        Datastore datastore = MorphiaHelper.getDatastore();
        User user;
        if (Utils.isValidMobile(accountPassword.getAccount())) {
            user = datastore.find(User.class).field("mobile").equalIgnoreCase(accountPassword.getAccount()).get();

            if (user == null) {
                throw new BadRequestException("此手机号码还没有注册,请先注册");
            }
        } else if (Utils.isValidEmail(accountPassword.getAccount())) {
            user = datastore.find(User.class).field("email").equalIgnoreCase(accountPassword.getAccount()).get();

            if (user == null) {
                throw new BadRequestException("此邮箱还没有注册,请先注册");
            }
        } else {
            throw new BadRequestException("无效的手机号码或邮箱");
        }



        if (user.isLocked()) {
            throw new BadRequestException("此账号已被锁定.");
        }

        if (user.getPassword() == null || user.getPassword().length() == 0) {
            throw new BadRequestException("您还没有设置密码");
        }

        if (!UserService.verifyPassword(user, accountPassword.getPassword())) {
            throw new BadRequestException("密码错误");
        }

        UserService.refreshToken(user);
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }
}
