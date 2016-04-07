package com.northgatecode.joinus.services;

import com.northgatecode.joinus.mongodb.Image;
import com.northgatecode.joinus.utils.MorphiaHelper;
import com.northgatecode.joinus.utils.Utils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static Image saveImage(String imageName, ObjectId userId) throws IOException {
        String fullPath = FilenameUtils.concat( Utils.getUploadFolder(), imageName + ".jpg");
        File imageFile = new File(fullPath);
        BufferedImage originalImage = ImageIO.read(imageFile);

        Datastore datastore = MorphiaHelper.getDatastore();

        Image image = new Image();
        image.setName(imageName);
        image.setExtension(".jpg");
        image.setSize(imageFile.length());

        image.setWidth(originalImage.getWidth());
        image.setHeight(originalImage.getHeight());

        image.setUploadedBy(userId);
        image.setUploadDate(new Date());

        image.setDimensions(new ArrayList<String>());

        datastore.save(image);
        return image;
    }

    public static void addDimensions(ObjectId imageId, int[] dimensions) throws IOException {
        Datastore datastore = MorphiaHelper.getDatastore();
        Image image = datastore.find(Image.class).field("id").equal(imageId).get();
        File imageFile = new File(FilenameUtils.concat(Utils.getUploadFolder(), image.getName() + image.getExtension()));

        float shorterSide = image.getHeight() > image.getWidth() ? image.getWidth() : image.getHeight();
        if (image.getDimensions() == null) {
            image.setDimensions(new ArrayList<String>());
        }

        for (int dimension : dimensions) {
            boolean created = false;
            for (String createdDimension : image.getDimensions()) {
                if (createdDimension.equals("_" + dimension)) {
                    created = true;
                    break;
                }
            }
            if (!created) {
                Thumbnails.of(imageFile).scale(dimension / shorterSide).toFile(FilenameUtils.concat(Utils.getResizedFolder(),
                        image.getName() + "_" + dimension + image.getExtension()));
                image.getDimensions().add("_" + dimension);
            }
        }
        datastore.save(image);
    }
}
