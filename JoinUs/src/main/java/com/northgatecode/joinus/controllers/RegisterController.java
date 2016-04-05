package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dto.*;
import com.northgatecode.joinus.dto.user.MobileVerifyCode;
import com.northgatecode.joinus.dto.user.UserProfile;
import com.northgatecode.joinus.dto.user.UserProfileWithToken;
import com.northgatecode.joinus.dto.user.UserToken;
import com.northgatecode.joinus.mongodb.Gender;
import com.northgatecode.joinus.mongodb.Role;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.JpaHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.mongodb.morphia.Datastore;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
    @Path("{mobile}")
    public Response getVerifyCode(@PathParam("mobile") String mobile) {
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            // check is valid mobile number

            // check same number
            List<User> sameMobileUsers = MorphiaHelper.getDatastore().createQuery(User.class)
                    .field("mobile").equal(mobile).asList();

            if (sameMobileUsers.size() > 0) {
                throw new BadRequestException("此号码已注册, 请直接登陆.");
            }

            VerifyCodeService.generateCodeAndSendSMS(mobile, "register");

        } finally {
            entityManager.close();
        }

        return Response.ok(new Message("验证码已发送")).build();
    }

    @PUT
    public Response register(MobileVerifyCode mobileVerifyCode) {

        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), "register", mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误或者已经过期,请重新验证.");
        }

        Datastore datastore = MorphiaHelper.getDatastore();
        // check same number
        List<User> sameMobileUsers = datastore.createQuery(User.class)
                .field("mobile").equal(mobileVerifyCode.getMobile()).asList();

        if (sameMobileUsers.size() > 0) {
            throw new BadRequestException("此号码已注册, 请直接登陆.");
        }

        User user = new User();
        user.setMobile(mobileVerifyCode.getMobile());
        user.setName("用户" + RandomStringUtils.randomNumeric(6));

        Role registeredRole = datastore.find(Role.class).field("id").equal(2).get();
        user.setRoles(new ArrayList<Role>());
        user.getRoles().add(registeredRole);

        Gender unknownGender = datastore.find(Gender.class).field("id").equal(1).get();
        user.setGender(unknownGender);

        UserService.generateToken(user);

        user.setCreateDate(new Date());
        user.setLastUpdateDate(new Date());
        user.setRegisterDate(new Date());

        user.setLocked(false);

        datastore.save(user);


        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

}
