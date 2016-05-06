package com.northgatecode.joinus.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidMobile(String mobile) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
}
