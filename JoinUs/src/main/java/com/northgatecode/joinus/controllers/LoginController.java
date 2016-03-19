package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dao.User;
import com.northgatecode.joinus.dto.*;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.JpaHelper;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {

            TypedQuery<Long> query = entityManager.createQuery("select count(u) from User as u where u.mobile like '" + mobile + "'", Long.class);
            long count = query.getSingleResult();

            if (count == 0) {
                throw new BadRequestException("此号码还没有注册, 请注册.");
            }
        } finally {
            entityManager.close();
        }
        VerifyCodeService.generateCodeAndSendSMS(mobile);
        return Response.ok(new Message("验证码已发送, 有效期5分钟.")).build();
    }

    @POST
    @Path("verify-code")
    public Response verifyCode (MobileVerifyCode mobileVerifyCode) {
        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误");
        }
        User user = UserService.getByMobile(mobileVerifyCode.getMobile());
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

    @POST
    @Path("password")
    public Response password (MobilePassword mobilePassword) {
        User user = UserService.getByMobile(mobilePassword.getMobile());
        if (user == null) {
            new BadRequestException("手机号码或者密码错误");
        }
        String hashedPassword = DigestUtils.md5Hex(mobilePassword.getPassword() + user.getSalt());
        if (!hashedPassword.equals(user.getPassword())) {
            new BadRequestException("手机号码或者密码错误");
        }
        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }
}
