package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dto.UploadedImageInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.UUID;

/**
 * Created by qianliang on 9/3/2016.
 */
@Path("upload")
public class UploadController {
    @POST
    @Path("image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImage(@FormDataParam("file") InputStream fileInputStream,
                                  @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception
    {
        String upload_path = "/var/www/joinus/images/upload";
        if (SystemUtils.IS_OS_MAC)
            upload_path = "/Users/qianliang/Projects/www/joinus/images/upload";

        int read = 0;
        byte[] bytes = new byte[1024];

        if (!FilenameUtils.getExtension(fileMetaData.getFileName()).toLowerCase().equals("jpg")) {
            throw new BadRequestException("JPG file only");
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String fullPath = FilenameUtils.concat(upload_path, fileName);

        OutputStream out = new FileOutputStream(fullPath);
        while ((read = fileInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();

        return Response.ok(new UploadedImageInfo(fileName, "images")).build();
    }
}
