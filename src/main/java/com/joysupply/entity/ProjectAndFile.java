package com.joysupply.entity;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @ClassName ProjectAndFile
 * @Author MaZhuli
 * @Date 2018/11/7 21:32
 * @Description 项目和附件
 * @Version 1.0
 **/
public class ProjectAndFile extends Project {
    private String fileNames;
    private String fileUrls;

    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }

    public String getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(String fileUrls) {
        this.fileUrls = fileUrls;
    }

    @Override
    public String toString() {
        return "ProjectAndFile{" +
                "fileNames='" + fileNames + '\'' +
                ", fileUrls='" + fileUrls + '\'' +
                '}';
    }
}
