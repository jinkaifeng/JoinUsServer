package com.northgatecode.joinus.services;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.northgatecode.joinus.auth.UserPrincipal;
import com.northgatecode.joinus.dto.ImageInfo;
import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.utils.Config;
import com.northgatecode.joinus.utils.JerseyHelper;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by qianliang on 5/4/2016.
 */
public class ImageService {

    public static String getImageName(ObjectId imageId) {
        if (imageId == null) {
            return null;
        }
        Datastore datastore = MorphiaHelper.getDatastore();
        Image image = datastore.find(Image.class).field("id").equal(imageId).get();
        if (image != null) {
            return image.getName();
        }
        return null;
    }

    public static Image saveUploadImage(InputStream inputStream, ObjectId userId) {
        Logger logger = Logger.getLogger(EaseMobService.class.getName());

        String endpoint;
        if (SystemUtils.IS_OS_LINUX) {
            endpoint = "oss-cn-hangzhou-internal.aliyuncs.com";
        } else {
            endpoint = "oss-cn-hangzhou.aliyuncs.com";
        }


        String imageName = UUID.randomUUID().toString();

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public,max-age=" + 60*60*24*30);
        meta.setLastModified(new Date());
        String key = "joinus/" + imageName + ".jpg";

        OSSClient client = null;
        try {
            client = new OSSClient(endpoint, Config.getAliKey(), Config.getAliSecret());
            client.putObject("northgatecode", key, inputStream, meta);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

        Response response = JerseyHelper.getClient()
                .target("http://northgatecode.img-cn-hangzhou.aliyuncs.com/joinus/" + imageName + ".jpg@info")
                .request().get();
        if (response.getStatus() == 200) {
            ImageInfo imageInfo = response.readEntity(ImageInfo.class);

            Image image = new Image();
            image.setName(imageName);
            image.setExtension(".jpg");
            image.setWidth(imageInfo.getWidth());
            image.setHeight(imageInfo.getHeight());
            image.setSize(imageInfo.getSize());
            image.setUploadedBy(userId);
            image.setUploadDate(new Date());
            MorphiaHelper.getDatastore().save(image);

            return image;
        } else {
            throw new InternalServerErrorException("图片上传失败");
        }

    }

//
//    public static boolean addDimensions(ObjectId imageId, int[] dimensions) {
//        Datastore datastore = MorphiaHelper.getDatastore();
//        Image image = datastore.find(Image.class).field("id").equal(imageId).get();
//        if (image == null) {
//            return false;
//        }
//        File imageFile = new File(FilenameUtils.concat(Utils.getUploadFolder(), image.getName() + image.getExtension()));
//
//        float shorterSide = image.getHeight() > image.getWidth() ? image.getWidth() : image.getHeight();
//        if (image.getDimensions() == null) {
//            image.setDimensions(new ArrayList<String>());
//        }
//
//        for (int dimension : dimensions) {
//            boolean created = false;
//            for (String createdDimension : image.getDimensions()) {
//                if (createdDimension.equals("_" + dimension)) {
//                    created = true;
//                    break;
//                }
//            }
//            if (!created) {
//                try {
//                    Thumbnails.of(imageFile).scale(dimension / shorterSide).toFile(FilenameUtils.concat(Utils.getResizedFolder(),
//                            image.getName() + "_" + dimension + image.getExtension()));
//                } catch (IOException ioEx) {
//                    throw new InternalServerErrorException("生成缩略图异常");
//                }
//                image.getDimensions().add("_" + dimension);
//            }
//        }
//        datastore.save(image);
//
//        return true;
//    }
}
