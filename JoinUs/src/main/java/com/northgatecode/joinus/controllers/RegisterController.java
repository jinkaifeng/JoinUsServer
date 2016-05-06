package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dto.*;
import com.northgatecode.joinus.dto.user.*;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by qianliang on 2/3/2016.
 */

@Path("register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegisterController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @GET
    @Path("mobile/{mobile}")
    public Response getVerifyCode(@PathParam("mobile") String mobile) {

        // check is valid mobile number
        if (!Utils.isValidMobile(mobile)) {
            throw new BadRequestException("无效的手机号码");
        }

        // check same number
        List<User> sameMobileUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                .field("mobile").equalIgnoreCase(mobile).asList();
        if (sameMobileUsers.size() > 0) {
            throw new BadRequestException("此号码已注册, 请直接登陆.");
        }

        VerifyCodeService.generateCodeAndSendSMS(mobile, "register");

        return Response.ok(new Message("验证码已发送")).build();
    }

    @GET
    @Path("email/{email}")
    public Response getEmailVerifyCode(@PathParam("email") String email) {

        if (!Utils.isValidEmail(email)) {
            throw new BadRequestException("无效的邮箱地址");
        }

        List<User> sameEmailUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                .field("email").equalIgnoreCase(email).asList();
        if (sameEmailUsers.size() > 0) {
            throw new BadRequestException("此邮箱已注册, 请直接登陆.");
        }

        VerifyCodeService.generateCodeAndSendEmail(email, "register");

        return Response.ok(new Message("验证码已发送")).build();
    }

    @PUT
    @Path("mobile")
    public Response register(MobileVerifyCode mobileVerifyCode) {

        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), "register", mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误或者已经过期,请重新验证.");
        }

        Datastore datastore = MorphiaHelper.getDatastore();
        // check same number
        List<User> sameMobileUsers = datastore.createQuery(User.class)
                .field("mobile").equalIgnoreCase(mobileVerifyCode.getMobile()).asList();

        if (sameMobileUsers.size() > 0) {
            throw new BadRequestException("此号码已注册, 请直接登陆.");
        }

        User user = new User();
        user.setMobile(mobileVerifyCode.getMobile());
        user.setName("用户" + RandomStringUtils.randomNumeric(6));

        user.setRoleId(2); // 注册用户
        user.setGenderId(1); // 保密

        UserService.generateToken(user);

        user.setCreateDate(new Date());
        user.setLastUpdateDate(new Date());
        user.setRegisterDate(new Date());

        user.setLocked(false);

        datastore.save(user);


        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

    @PUT
    @Path("email")
    public Response emailRegister(EmailVerifyCode emailVerifyCode) {

        if (!VerifyCodeService.verify(emailVerifyCode.getEmail(), "register", emailVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误或者已经过期,请重新验证.");
        }

        Datastore datastore = MorphiaHelper.getDatastore();
        // check same number
        List<User> sameEmailUsers = datastore.createQuery(User.class)
                .field("email").equalIgnoreCase(emailVerifyCode.getEmail()).asList();

        if (sameEmailUsers.size() > 0) {
            throw new BadRequestException("此邮箱已注册, 请直接登陆.");
        }

        User user = new User();
        user.setEmail(emailVerifyCode.getEmail().toLowerCase());
        user.setName("用户" + RandomStringUtils.randomNumeric(6));

        user.setRoleId(2); // 注册用户
        user.setGenderId(1); // 保密

        UserService.generateToken(user);

        user.setCreateDate(new Date());
        user.setLastUpdateDate(new Date());
        user.setRegisterDate(new Date());

        user.setLocked(false);

        datastore.save(user);


        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

}
