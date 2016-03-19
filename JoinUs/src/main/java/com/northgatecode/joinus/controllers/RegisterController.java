package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dao.User;
import com.northgatecode.joinus.dto.*;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.JpaHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
    @Path ("{mobile}")
    public Response getVerifyCode(@PathParam("mobile") String mobile) {
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            // check is valid mobile number

            // check same number
            TypedQuery<User> query = entityManager.createQuery(
                    "select u from User as u where u.mobile " +
                    "like '" + mobile + "'", User.class);
            List<User> sameMobileUsers = query.getResultList();
            if (sameMobileUsers.size() > 0) {
                throw new BadRequestException("此号码已注册, 请直接登陆.");
            }

            VerifyCodeService.generateCodeAndSendSMS(mobile);

        } finally {
            entityManager.close();
        }

        return Response.ok(new Message("验证码已发送")).build();
    }

    @PUT
    public Response register(MobileVerifyCode mobileVerifyCode) {

        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误或者已经过期,请重新验证.");
        }

        User user;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            // check mobile
            TypedQuery<User> query = entityManager.createQuery("select u from User as u where u.mobile like '"
                    + mobileVerifyCode.getMobile() + "'", User.class);
            List<User> sameMobileUsers = query.getResultList();
            if (sameMobileUsers.size() > 0) {
                throw new BadRequestException("此手机号码已注册");
            }

            user = new User();
            user.setMobile(mobileVerifyCode.getMobile());
            user.setName("用户" + RandomStringUtils.randomNumeric(6));

            UserService.refreshToken(user);

            user.setCreateDate(new Date());
            user.setLastUpdateDate(new Date());

            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }

        return Response.ok(new UserProfileWithToken(new UserProfile(user), new UserToken(user))).build();
    }

}
