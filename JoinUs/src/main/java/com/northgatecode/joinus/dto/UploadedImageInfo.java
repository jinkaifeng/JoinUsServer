package com.northgatecode.joinus.dto;

/**
 * Created by qianliang on 9/3/2016.
 */
public class UploadedImageInfo {
    private String fileName;
    private String path;

    public UploadedImageInfo(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
