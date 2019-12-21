package com.joysupply.controller;

import java.util.*;

import com.auth0.jwt.interfaces.Claim;
import com.joysupply.entity.Menu;
import com.joysupply.service.LoginService;
import com.joysupply.service.MenuService;
import com.joysupply.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.joysupply.entity.SystemUser;
import com.joysupply.service.HintInfoService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 公用类
 * @author zxt
 *
 * 2018年10月27日-上午10:06:32
 */
@RequestMapping("/common")
@RestController
public class CommonController extends BaseController{
	
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private HttpSession session;

	
	/**
	 * 多文件上传
	 * @param fileDir   文件保存目录/upload/fileDir/xxx
	 * @param files
	 * @return key:name    文件名称
	 * 			key :url   文件保存路径  
	 */
	@RequestMapping(value="/uploadFiles",method=RequestMethod.POST)
	public ResultData<List<Map<String,Object>>> uploadFiles(String fileDir, @RequestParam("files") MultipartFile [] files) {
		
		if(files != null || files.length <= 0) {
			return new ResultData<>("上传的文件为空");
		}else if(files.length>10) {
			return new ResultData<>("上传的文件不能超多10个");
		}
		try {
			return new ResultData<List<Map<String,Object>>>(files.length,UpLoadFile.uploadImg(files,fileDir));
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			return new ResultData<>(ex.getMessage());
		}
		
	}
	
	/**
	 * 单文件上传
	 * @param fileDir      文件保存目录/upload/fileDir/xxx
	 * @param file
	 * @return key:name    文件名称
	 * 			key :url   文件保存路径  
	 */
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST)
	public ResultData<Map<String,Object>> uploadFile(String fileDir, @RequestParam("file") MultipartFile file) {
		
		try {
			return new ResultData<Map<String,Object>>(UpLoadFile.uploadFile(file,fileDir));
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			return new ResultData<>(ex.getMessage());
		}
	}
	

	
	
	/**
	 * 获取当前日期的周，天
	 * @param next
	 * @param up
	 * @param dateTime
	 * @return
	 */
	@RequestMapping("/getWeekDay")
	public Map<String, Object> getWeekDay(String next, String up, String dateTime,String month) {
		Map culm = DateUtil.getCulm(dateTime,next,up,month);
		return culm;
	}

	/**
	 * 验签
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/verifyToken", method = RequestMethod.GET)
	public boolean verifyToken(String token) {
		try {
			JWTUtils.verifyToken(token);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	/**
	 * 解析token
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/decryptToken", method = RequestMethod.GET)
	public Map<String, Object> decryptToken(String token) {
        Map<String, Object> result = new HashMap<>();
		try {
            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);
            String userName = claimMap.get("userName").asString();
            String personCode = claimMap.get("personCode").asString();
            result.put("success",true);
            result.put("userName",userName);
            result.put("personCode",personCode);
            return result;
		}catch (Exception e) {
            result.put("success",false);
		    return result;
		}
	}

	@RequestMapping(value = "/createToken", method = RequestMethod.GET)
	public Map<String, Object> createToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            Object systemUser = session.getAttribute("systemUser");
            if(systemUser == null || !(systemUser instanceof SystemUser)) {
                result.put("success",false);
                return result;
            }
            SystemUser user = (SystemUser) systemUser;
            Date isDate = new Date();
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MINUTE, 1);
            String token = JWTUtils.createToken(user, isDate, instance.getTime());
            result.put("success",true);
            result.put("token",token);
            return result;
        }catch (Exception e) {
            result.put("success",false);
            return result;
		}
	}

}