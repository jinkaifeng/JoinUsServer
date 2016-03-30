package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.UserName;
import com.northgatecode.joinus.dto.UserPassword;
import com.northgatecode.joinus.dto.UserProfile;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.utils.JpaHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by qianliang on 2/3/2016.
 */
@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

}
