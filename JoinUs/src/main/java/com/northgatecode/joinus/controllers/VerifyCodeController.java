package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.Message;
import com.northgatecode.joinus.dto.VerifyCode;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.JpaHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by qianliang on 8/4/2016.
 */
@Path("verifyCode")
public class VerifyCodeController {
    @GET
    @Authenticated
    public Response getMyCode(@Context SecurityContext securityContext) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();

        User user = datastore.find(User.class).field("id").equal(userId).get();
        VerifyCodeService.generateCodeAndSendSMS(user.getMobile(), "verifyMe");
        return Response.ok(new Message("验证码已发送, 有效期5分钟.")).build();
    }

    @POST
    @Path("verify")
    @Authenticated
    public Response verify(@Context SecurityContext securityContext, VerifyCode verifyCode){
        User user = UserService.getUserFromContext(securityContext);
        if (!VerifyCodeService.verify(user.getMobile(), "verifyMe", verifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误");
        }
        return Response.ok(new Message("验证成功")).build();
    }

    @GET
    @Path("updateMobile/{mobile}")
    @Authenticated
    public Response getVerifyCode(@PathParam("mobile") String mobile) {
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            // check is valid mobile number
            if (mobile.length() != 11) {
                new BadRequestException("无效的手机号码");
            }
            // check same number
            List<User> sameMobileUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                    .field("mobile").equal(mobile).asList();

            if (sameMobileUsers.size() > 0) {
                throw new BadRequestException("此号码已使用.");
            }

            VerifyCodeService.generateCodeAndSendSMS(mobile, "updateMobile");

        } finally {
            entityManager.close();
        }

        return Response.ok(new Message("验证码已发送")).build();
    }
}
