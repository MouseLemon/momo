package com.joysupply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.ResultData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joysupply.service.AuthorityManageService;
import com.joysupply.util.OpResult;

/**
 * 权限管理controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/authority")
public class AuthorityManageController extends BaseController{
	
	@Autowired
	AuthorityManageService authorityManageService;
	private Log log = LogFactory.getLog(getClass());
	private String getUserCode(){
		return getUser().getPersonCode();
	}
	
	/**
	 * 查询用户项目权限
	 * @param reqmap  persionCode String
	 * @return
	 */
	@RequestMapping(value="/getPersonProjectAuth")
	@ResponseBody
	public ResultData<List<Map<String,Object>>> getPersonProjectAuth(@RequestParam Map<String, Object> reqmap){
		try{
			if(reqmap!=null && (reqmap.get("personCode") == null || reqmap.get("personCode").toString().equals(""))){
				reqmap.put("personCode",getUserCode());
			}
			List<Map<String,Object>> data = authorityManageService.getPersonProjectAuth(reqmap);
			return new ResultData<>(data.size(),data);
		}catch (BusinessLevelException ex){
			ex.printStackTrace();
			return new ResultData<>(ex.getMessage());
		}
	}
	/**
	 * 查询用户楼权限
	 * @param reqmap persionCode String
	 * @return
	 */
	@RequestMapping(value="/getPersonBuildingAuth")
	@ResponseBody
	public ResultData<List<Map<String,Object>>> getPersonBuildingAuth(@RequestParam Map<String, Object> reqmap){
		try{
			if(reqmap!=null && (reqmap.get("personCode") == null || reqmap.get("personCode").toString().equals(""))){
				reqmap.put("personCode",getUserCode());
			}
			List<Map<String,Object>> data = authorityManageService.getPersonBuildingAuth(reqmap);
			return new ResultData<>(data.size(),data);
		}catch (BusinessLevelException ex){
			ex.printStackTrace();
			return new ResultData<>(ex.getMessage());
		}
	}

	/**
	 * 添加修改老师项目权限 
	 * teacherCode String
	 * ids String x,x,x
	 */
	@RequestMapping(value="/insUpTeacherProjectAuth")
	@ResponseBody
	public OpResult insUpTeacherProjectAuth(@RequestParam Map<String, Object> map){
		try {
			log.debug(map);
			authorityManageService.insertTeacherProjectAuth(map);
			return new OpResult();
		} catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}

	/**
	 * 添加修改用户教学楼权限 
	 * personCode String
	 * ids String x,x,x
	 */
	@RequestMapping(value="/insUpPersonBuildingAuth")
	@ResponseBody
	public OpResult insUpPersonBuildingAuth(@RequestParam Map<String, Object> map){
		try {
			log.debug(map);
			authorityManageService.insertPersonBuildingAuth(map);
			return new OpResult();
		} catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	/**
	 * 添加修改用户教师权限
	 * personCode String
	 * ids String x,x,x
	 */
	@RequestMapping(value="/insUpPersonTeacherAuth")
	@ResponseBody
	public OpResult insUpPersonTeacherAuth(@RequestParam Map<String, Object> map){
		try {
			log.debug(map);
			authorityManageService.insertPersonTeacherAuth(map);
			return new OpResult();
		} catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	/**
	 * 添加修改用户项目权限 
	 * personCode String
	 * ids String x,x,x
	 */
	@RequestMapping(value="/insUpPersonProjectAuth")
	@ResponseBody
	public OpResult insUpPersonProjectAuth(@RequestParam Map<String, Object> map){
		try {
			log.debug(map);
			authorityManageService.insertPersonProjectAuth(map);
			return new OpResult();
		} catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
}
