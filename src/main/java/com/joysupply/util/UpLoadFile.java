package com.joysupply.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;


public class UpLoadFile {
	//多文件上传
	public static List<Map<String,Object>>  uploadImg(MultipartFile [] files , String fileDir) {
		List<Map<String,Object>> nameAndUrls = new ArrayList<Map<String,Object>>(); 
		for (MultipartFile file : files) {
			Map<String, Object> nameAndUrl = getFileUrlAndName(fileDir, file);
			if(nameAndUrl!=null&&!nameAndUrl.isEmpty()) {
				nameAndUrls.add(nameAndUrl);
			}
		}
		return nameAndUrls;
	}
	
	
	/**
	 * 单文件上传
	 * @param file
	 * @param fileDir
	 * @return
	 */
	public static Map<String,Object>  uploadFile(MultipartFile file , String fileDir) {
		Map<String, Object> nameAndUrl = getFileUrlAndName(fileDir, file);
		return nameAndUrl;
	}
	
	

	private static Map<String, Object> getFileUrlAndName(String fileDir,MultipartFile file) {
		Map<String,Object> nameAndUrl = new HashMap<String,Object>();
		nameAndUrl.put("fileName", file.getOriginalFilename());
		String fileUrl = ""; // 上传图片
		String filePath = Tools.getUploadDir(fileDir); // 上传文件保存路径
		String pathSub = filePath.substring(filePath.indexOf("upload")).replace("\\", "/");// 获取路径
		if (file.isEmpty()) {
			fileUrl = "";
		} else {
			String Suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			String newFileName = Constants.GUID().replaceAll("-","")+ "." + Suffix;
			nameAndUrl.put("name", file.getOriginalFilename()); //把  name 添加到map中
			String newFilePath = filePath + newFileName;// 新路径
			File newFile = new File(newFilePath);
			try {
				file.transferTo(newFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileUrl = "/" + pathSub + newFileName;// 保存到数据库的路径
			nameAndUrl.put("url", fileUrl); //把  rul 添加到map中
		}
		return nameAndUrl;
	}
	
}
