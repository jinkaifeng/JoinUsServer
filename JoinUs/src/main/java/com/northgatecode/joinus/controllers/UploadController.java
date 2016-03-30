package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.UploadImage;
import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.utils.MorphiaHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
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
import java.util.UUID;

/**
 * Created by qianliang on 9/3/2016.
 */
@Path("upload")
public class UploadController {
    @POST
    @Path("image")
    @Authenticated
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImage(@Context SecurityContext securityContext,@FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception
    {
        String upload_path = "/var/www/joinus/images/upload";
        if (SystemUtils.IS_OS_MAC)
            upload_path = "/Users/qianliang/Projects/www/joinus/images/upload";

        int read;
        byte[] bytes = new byte[1024];

        if (!FilenameUtils.getExtension(fileMetaData.getFileName()).toLowerCase().equals("jpg")) {
            throw new BadRequestException("JPG file only");
        }
        String name = UUID.randomUUID().toString();
        String fileName =  name + ".jpg";
        String fullPath = FilenameUtils.concat(upload_path, fileName);

        OutputStream out = new FileOutputStream(fullPath);
        while ((read = fileInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();

        Datastore datastore = MorphiaHelper.getDatastore();

        Image image = new Image();
        image.setName(name);
        image.setExtension(".jpg");

        File imageFile = new File(fullPath);
        image.setSize(imageFile.length());

        BufferedImage originalImage = ImageIO.read(imageFile);
        image.setWidth(originalImage.getWidth());
        image.setHeight(originalImage.getHeight());

        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();
        User user = datastore.find(User.class).field("id").equal(userId).get();
        image.setUploadedBy(user);
        image.setUploadDate(new Date());

        image.setDimensions(new ArrayList<String>());

        datastore.save(image);

        return Response.ok(new UploadImage(image.getId(), image.getName())).build();
    }
}
