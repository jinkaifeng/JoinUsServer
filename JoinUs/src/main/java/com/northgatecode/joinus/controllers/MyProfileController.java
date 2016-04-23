package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.Message;
import com.northgatecode.joinus.dto.region.CityItem;
import com.northgatecode.joinus.dto.region.ProvinceItem;
import com.northgatecode.joinus.dto.region.ProvinceList;
import com.northgatecode.joinus.dto.user.*;
import com.northgatecode.joinus.mongodb.*;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.services.RegionService;
import com.northgatecode.joinus.services.UserService;
import com.northgatecode.joinus.services.VerifyCodeService;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.mongodb.morphia.Datastore;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qianliang on 28/3/2016.
 */
@Path("myProfile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MyProfileController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @GET
    @Authenticated
    public Response getMyProfile(@Context SecurityContext securityContext) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        User user = UserService.getById(userId);
        return Response.ok(new UserProfile(user)).build();
    }

    @POST
    @Path("photo")
    @Authenticated
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePhoto(@Context SecurityContext securityContext, @FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileMetaData) throws IOException {

        if (!FilenameUtils.getExtension(fileMetaData.getFileName()).toLowerCase().equals("jpg")) {
            throw new BadRequestException("JPG file only");
        }

        String imageName = UUID.randomUUID().toString();
        String fullPath = FilenameUtils.concat( Utils.getUploadFolder(), imageName + ".jpg");

        OutputStream out = new FileOutputStream(fullPath);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = fileInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();

        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Image image = ImageService.saveImage(imageName, userId);

        ImageService.addDimensions(image.getId(), new int[]{320, 160, 80});

        Datastore datastore = MorphiaHelper.getDatastore();
        User user = datastore.find(User.class).field("id").equal(userId).get();
        user.setPhotoImageId(image.getId());
        user.setLastUpdateDate(new Date());
        datastore.save(user);

        return Response.ok(new UserProfile(user)).build();
    }

    @POST
    @Path("mobile")
    @Authenticated
    public Response updateMobile(@Context SecurityContext securityContext, MobileVerifyCode mobileVerifyCode) {

        if (!VerifyCodeService.verify(mobileVerifyCode.getMobile(), "updateMobile", mobileVerifyCode.getVerifyCode())) {
            throw new BadRequestException("验证码错误");
        }

        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        Datastore datastore = MorphiaHelper.getDatastore();
        User user = datastore.find(User.class).field("id").equal(userId).get();

        user.setMobile(mobileVerifyCode.getMobile());
        user.setLastUpdateDate(new Date());
        datastore.save(user);

        return Response.ok(new UserProfile(user)).build();
    }

    @POST
    @Path("password")
    @Authenticated
    public Response updatePassword(@Context SecurityContext securityContext, UserPassword userPassword) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();

        User user = datastore.find(User.class).field("id").equal(userId).get();

        if (user.getPassword() != null && !UserService.verifyPassword(user, userPassword.getCurrentPassword())) {
            throw new BadRequestException("原密码错误");
        }
        // validate password
//        String regex = "^[a-zA-Z0-9-_]{4,16}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(user.getPassword());
        if (userPassword.getPassword().length() < 4) {
            throw new BadRequestException("无效的新密码");
        }

        String salt = RandomStringUtils.randomAlphanumeric(4);
        String hashedPassword = DigestUtils.md5Hex(userPassword.getPassword() + salt);

        user.setPassword(hashedPassword);
        user.setSalt(salt);
        user.setLastUpdateDate(new Date());

        datastore.save(user);


        return Response.ok().build();
    }

    @POST
    @Path("email")
    @Authenticated
    public Response updateEmail(@Context SecurityContext securityContext, UserEmail userEmail) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userEmail.getEmail());
        if (!matcher.matches()) {
            throw new BadRequestException("无效的邮箱地址");
        }

        Datastore datastore = MorphiaHelper.getDatastore();

        User user = datastore.find(User.class).field("id").equal(userId).get();
        user.setEmail(userEmail.getEmail());
        user.setLastUpdateDate(new Date());

        datastore.save(user);

        return Response.ok(new UserProfile(user)).build();
    }

    private String nameRegx = "^[0-9a-zA-Z\u4E00-\u9FA5]{2,10}$";

    @POST
    @Path("validateName")
    public Response validateName(UserName userName) {

//        try {
//            Thread.sleep(RandomUtils.nextInt(1000, 3000)); //1000 milliseconds is one second.
//        } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }

        if (userName.getName().length() < 2) {
            throw new NotAcceptableException("用户名过短,用户名应不少于2个字符.");
        }

        if (userName.getName().length() > 10) {
            throw new NotAcceptableException("用户名过长,用户名不应超过10个字符.");
        }

        Pattern pattern = Pattern.compile(nameRegx);
        Matcher matcher = pattern.matcher(userName.getName());
        if (!matcher.matches()) {
            throw new NotAcceptableException("用户名含有非法字符,用户名只可以使用中文或英文字母。");
        }

        Datastore datastore = MorphiaHelper.getDatastore();
        List<User> sameNameUsers = datastore.createQuery(User.class).field("name").equalIgnoreCase(userName.getName()).asList();
        if (sameNameUsers.size() > 0) {
            throw new NotAcceptableException("已存在同名用户,请重新选择用户名.");
        }

        return Response.ok(userName).build();
    }

    @POST
    @Path("name")
    @Authenticated
    public Response updateName(@Context SecurityContext securityContext, UserName userName) {
        if (userName.getName().length() < 2) {
            throw new BadRequestException("用户名过短,用户名应不少于2个字符.");
        }

        if (userName.getName().length() > 10) {
            throw new BadRequestException("用户名过长,用户名不应超过10个字符.");
        }

        Pattern pattern = Pattern.compile(nameRegx);
        Matcher matcher = pattern.matcher(userName.getName());
        if (!matcher.matches()) {
            throw new BadRequestException("用户名含有非法字符,用户名只可以使用中文或英文字母。");
        }

        Datastore datastore = MorphiaHelper.getDatastore();
        List<User> sameNameUsers = datastore.createQuery(User.class).field("name").equalIgnoreCase(userName.getName()).asList();
        if (sameNameUsers.size() > 0) {
            throw new BadRequestException("已存在同名用户,请重新选择用户名.");
        }

        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        User user = datastore.find(User.class).field("id").equal(userId).get();
        user.setName(userName.getName());
        user.setLastUpdateDate(new Date());

        datastore.save(user);

        return Response.ok(new UserProfile(user)).build();
    }


    @POST
    @Path("gender")
    @Authenticated
    public Response updatePhoto(@Context SecurityContext securityContext, UserGender userGender) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();

        User user = datastore.find(User.class).field("id").equal(userId).get();
        Gender gender = datastore.find(Gender.class).field("id").equal(userGender.getGenderId()).get();
        if (gender == null) {
            throw new BadRequestException("无效的性别代码");
        }
        user.setGenderId(userGender.getGenderId());
        user.setLastUpdateDate(new Date());

        datastore.save(user);

        return Response.ok(new UserProfile(user)).build();
    }

    @GET
    @Path("provinces")
    public Response getProvinceList() {
//        try {
//            Thread.sleep(RandomUtils.nextInt(500, 2000)); //1000 milliseconds is one second.
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }

        ProvinceList provinceList = RegionService.getProvinceList();
        return Response.ok(provinceList).build();
    }

    @POST
    @Path("city")
    @Authenticated
    public Response updatePhoto(@Context SecurityContext securityContext, UserCity userCity) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Datastore datastore = MorphiaHelper.getDatastore();

        User user = datastore.find(User.class).field("id").equal(userId).get();
        City city = datastore.find(City.class).field("id").equal(userCity.getCityId()).get();
        if (city == null) {
            throw new BadRequestException("无效的城市代码");
        }
        user.setCityId(city.getId());
        user.setLastUpdateDate(new Date());

        datastore.save(user);

        return Response.ok(new UserProfile(user)).build();
    }
}
