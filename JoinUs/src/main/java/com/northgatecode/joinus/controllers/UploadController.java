package com.northgatecode.joinus.controllers;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.northgatecode.joinus.auth.Authenticated;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.ImageInfo;
import com.northgatecode.joinus.dto.UploadImage;
import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.mongodb.User;
import com.northgatecode.joinus.services.ImageService;
import com.northgatecode.joinus.utils.JerseyHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;
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
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

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
    public Response uploadJPG(@Context SecurityContext securityContext, @FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
        if (!FilenameUtils.getExtension(fileMetaData.getFileName()).toLowerCase().equals("jpg")) {
            throw new BadRequestException("JPG file only");
        }

        UserPrincipal userPrincipal = (UserPrincipal)securityContext.getUserPrincipal();
        ObjectId userId = userPrincipal.getId();

        Image image = ImageService.saveUploadImage(fileInputStream, userId);

        return Response.ok(new UploadImage(image.getId(), image.getName())).build();
    }

    @POST
    @Path("sync")
    public Response syncImageInfo() {
        Datastore datastore = MorphiaHelper.getDatastore();
        List<Image> images = datastore.find(Image.class).field("size").doesNotExist().limit(100).asList();
        for (Image image : images) {
            Response response = JerseyHelper.getClient()
                    .target("http://northgatecode.img-cn-hangzhou.aliyuncs.com/joinus/" + image.getName() + ".jpg@info")
                    .request().get();
            if (response.getStatus() == 200) {
                ImageInfo imageInfo = response.readEntity(ImageInfo.class);

                image.setWidth(imageInfo.getWidth());
                image.setHeight(imageInfo.getHeight());
                image.setSize(imageInfo.getSize());
            }

            datastore.save(image);
        }

        return Response.ok().build();
    }
}
