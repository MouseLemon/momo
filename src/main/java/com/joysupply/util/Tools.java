package com.joysupply.util;

import com.joysupply.entity.DataDictionary;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {
	
	public static String getUploadDir(String moduleName) {
		SimpleDateFormat sdfFolderName = new SimpleDateFormat("yyyyMMdd");
		String newFolderName = sdfFolderName.format(new Date());
		String userDir = System.getProperty("user.dir");
		String path = "";
		if(moduleName != null && !"".equals(moduleName)) {
			
			path = userDir + "\\upload\\" + moduleName + "\\" + newFolderName + "\\";
		}else {
			path = userDir + "\\upload\\"  + newFolderName + "\\";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static String encode(String fileName, HttpServletRequest request) {
		try {
			//获得请求头中的User-Agent
			String agent = request.getHeader("User-Agent");
			//根据不同浏览器进行不同的编码
			if (agent.contains("MSIE")) {
				// IE浏览器
				fileName = URLEncoder.encode(fileName, "utf-8");
				fileName = fileName.replace("+", " ");
			} else if (agent.contains("Firefox")) {
				// 火狐浏览器
				BASE64Encoder base64Encoder = new BASE64Encoder();

				fileName = "=?utf-8?B?"
						+ base64Encoder.encode(fileName.getBytes("utf-8")) + "?=";
			} else {
				// 其它浏览器
				fileName = URLEncoder.encode(fileName, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 获取未有的字典项
	 *
	 * @param list1
	 * @param teacherType
	 * @param noTeacherType
	 * @param key
	 */
	public static void getDataDic(List<Map<String, Object>> list1, List<DataDictionary> teacherType, List<String> noTeacherType, String key) {
		for (Map<String, Object> item : list1) {
			Object type = item.get(key);
			boolean flag = true;
			if (type != null && !"".equals(type.toString())) {
				for (DataDictionary data : teacherType) {
					String name = data.getName();
					if (name.equals(type.toString())) {
						flag = false;
						break;
					}
				}
				if (flag) {
					noTeacherType.add(type.toString());
				}
			}else {
				noTeacherType.add("none");
			}
		}
	}

	/**
	 * 获取异常消息
	 * @param msg
	 * @param noTeacherType
	 * @param source
	 */
	public static void getMsg(List<Map<String, Object>> msg, List<String> noTeacherType, String source) {
		for (String teacherTypeStr : noTeacherType) {
			Map<String, Object> option = new HashMap<>();
			option.put("source", source);
			if(teacherTypeStr.equals("none")){

				//option.put("description", "未填写数据");
			}else {
				option.put("description", "系统中：“" + teacherTypeStr + "”不存在");
				msg.add(option);
			}
		}
	}

	public static void setDataDic(List<Map<String, Object>> list1, List<DataDictionary> teacherType, String key) {
		for (Map<String, Object> item : list1) {
			Object type = item.get(key);
			boolean flag = true;
			if (type != null && !"".equals(type.toString())) {
				for (DataDictionary data : teacherType) {
					String name = data.getName();
					if (name.equals(type.toString())) {
						item.put(key,data.getCode());
					}
				}
			}
		}
	}
}
