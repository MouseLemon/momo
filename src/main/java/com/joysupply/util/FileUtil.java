package com.joysupply.util;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @ClassName FileUtil
 * @Author LiZhuang
 * @Date 2018/10/10 14:31
 * @Description 上传文件的相关方法
 * @Version 1.0
 **/
public class FileUtil {
    //定义允许上传的文件扩展名
    static Map<String, String> extMap = new HashMap<String, String>() {{
        put("image", "gif,jpg,jpeg,png,bmp");
        put("video", "mp4");
        put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        put("file", "doc,docx,xls,xlsx,ppt,htm,html,xml,txt,zip,rar,gz,bz2");
    }};

    /**
     * @Author LiZhuang
     * @Description 上传单文件的方法，返回相对路径
     * @Date 2018/10/10 15:42
     * @Param [request 上传文件的请求, inputName 上传文件时input的name, parentFloder 文件所属模块名（如Banner）]
     * @Return java.lang.String
     **/
    public static String fileUploadSingle(HttpServletRequest request, String inputName, String parentFloder) throws Exception {
        String filePath = null;
        String root = System.getProperty("user.dir");
        String path = "/upload/" + parentFloder + "/";

        //获取解析器
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断是否是文件
        if (resolver.isMultipart(request)) {
            // 进行转换
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) (request);
            // 获取MultipartFile文件
            MultipartFile file = multiRequest.getFile(inputName);
            if (file != null) {
                if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")) {
                    // 获取屏保文件在数据库中保存的路径
                    if (filePath == null) {
                        //上传文件并返回路径
                        filePath = uploadFile(root, path, file);
                    }
                }
            }
        }
        return filePath;
    }

    /**
     * @Author LiZhuang
     * @Description 同一个input中出入多张图片，返回一个list集合
     * @Date 2018/10/11 9:27
     * @Param [request, parentFloder]
     * @Return java.util.List<java.lang.String>
     **/
    public static List<String> filesUploadMultiple(HttpServletRequest request, String parentFloder) throws Exception {
        List<String> filePaths = new ArrayList<>();
        String filePath = null;
        String root = System.getProperty("user.dir");
        String path = "/upload/" + parentFloder + "/";

        //获取解析器
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断是否是文件
        if (resolver.isMultipart(request)) {
            // 进行转换
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) (request);
            // 获取所有文件名称
            Iterator<String> it = multiRequest.getFileNames();
            while (it.hasNext()) {
                String inputName = it.next();
                MultipartFile file = multiRequest.getFile(inputName);
                filePath = uploadFile(root, path, file);
                filePaths.add(filePath);
            }
        }
        return filePaths;
    }

    /**
     * @Author LiZhuang
     * @Description 上传多文件的方法，返回存有相对路径的map{input的名称:文件上传后的路径}（也适用于单文件上传）
     * @Date 2018/10/10 14:53
     * @Param [request 上传文件的请求, parentFloder 文件所属模块名（如Banner）]
     * @Return java.lang.String
     **/
    public static Map<String, String> fileUploadMultiple(HttpServletRequest request, String parentFloder) throws Exception {

        Map<String, String> filePathMap = new HashMap<>();
        String filePath = null;
        String root = System.getProperty("user.dir");
        String path = "/upload/" + parentFloder + "/";

        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) (request);
            Iterator<String> it = multiRequest.getFileNames();
            while (it.hasNext()) {
                String inputName = it.next();
                MultipartFile file = multiRequest.getFile(inputName);
                String originalName = file.getOriginalFilename();
                filePath = uploadFile(root, path, file);
                filePathMap.put(filePath, originalName);
            }
        }
        return filePathMap;
    }

    /**
     * @param root 一级目录：项目所在的系统路径
     * @param path 二级目录：项目同级目录下/upload/../..
     * @param file 获取到的MultipartFile
     * @return
     * @Author LiZhuang
     * @Description 封装的上传文件方法，上传文件到指定目录，并返回存入数据的地址
     * @Date 2018/10/10 15:53
     */
    public static String uploadFile(String root, String path, MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String fileName = Constants.GUID() + originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        String dataFloder = DateUtil.getDateDir();
        path += (dataFloder + "/");
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        path += (fileExt + "/");
        String filePath = path + fileName;
        String finalPath = root + filePath;
        File newFile = new File(finalPath);
        File localhost = new File(root + path);
        localhost.mkdirs();
        file.transferTo(newFile);
        return filePath;
    }

}
