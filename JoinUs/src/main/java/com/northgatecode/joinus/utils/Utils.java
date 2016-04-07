package com.northgatecode.joinus.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Created by qianliang on 30/3/2016.
 */
public class Utils {

    public static String getUploadFolder(){
        if (SystemUtils.IS_OS_MAC)
            return "/Users/qianliang/Projects/www/joinus/images/upload";
        return "/var/www/joinus/images/upload/";
    }

    public static String getResizedFolder() {
        if (SystemUtils.IS_OS_MAC)
            return "/Users/qianliang/Projects/www/joinus/images/resized";
        return "/var/www/joinus/images/resized/";
    }
}
