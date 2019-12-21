package com.joysupply.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;











import com.joysupply.entity.SystemUser;
import com.joysupply.service.KcManagerTeacherService;
import com.joysupply.util.ExcelUtil_JXL;

@Controller
@RequestMapping(value="/kcManagerTeacher")
public class KcManagerTeacherController extends BaseController{
	
	@Autowired
	KcManagerTeacherService kcManagerTeacherService;
	
	//返回课酬统计页面
	@RequestMapping(value="/kcStatistics")
	public String kcStatistics(Model model) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		model.addAllAttributes(resultMap);
		return "kcManageMentTeacher/kcStatistics";
	}
	
	/**
	 * 获取教师课酬数据
	 * @param requset
	 * @return
	 */
	@RequestMapping("/getKCData")
	@ResponseBody
	public Map<String, Object> getKCData(@RequestParam Map<String, String> requset){
		Map<String, Object> map = new HashMap<String, Object>();
		SystemUser user = getUser();
		String personCode = user.getPersonCode();
		requset.put("personCode", personCode);
		List<Map<String, Object>> list = kcManagerTeacherService.getKCData(requset);
		map.put("data", list);
		map.put("count", list.size());
		return map;
	}
	
	/**
	 * 获取项目名称
	 */
	@RequestMapping(value="/getProductName")
	@ResponseBody
	public Map<String, Object> getProductName(){
		Map<String, Object> map = new HashMap<String, Object>();
		SystemUser user = getUser();
		String personCode = user.getPersonCode();
		List<Map<String, Object>> list = kcManagerTeacherService.getProductName(personCode);
		map.put("data",list);
		map.put("count", list.size());
		return map;
	}
	
	/**
	 * 获取教师个人信息
	 * @return
	 */
	@RequestMapping(value="/teacherInfo")
	public ModelAndView teacherInfo(Model model){
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		resultMap.put("teacherCode", getUser().getPersonCode());
		model.addAllAttributes(resultMap);
		return new ModelAndView("kcManageMentTeacher/teacherInfo");
	}
	
	
	/**
	 * 获取教师个人信息详情
	 * @return
	 */
	@RequestMapping(value="/getTeacherInfo")
	@ResponseBody
	public Map<String, Object> getTeacherInfo(@RequestParam String teacherCode){
		Map<String, Object> info = kcManagerTeacherService.getTeacherInfo(teacherCode);
		return info;
	}
	
	/**
	 * 课酬明细页面
	 * @param parMap
	 * @return
	 */
	@RequestMapping(value="/kcInfo")
	public ModelAndView kcInfo(@RequestParam Map<String, Object> parMap){
		parMap.putAll(getMenuMap());
		SystemUser user = getUser();
		parMap.put("user", user);
		return new ModelAndView("kcManageMentTeacher/kcInfo",parMap);
	}
	
	/**
	 * 查询课酬明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getKCInfo")
	@ResponseBody
	public Map<String, Object> getKCInfo(@RequestParam Map<String, Object> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		SystemUser user = getUser();
		request.put("personCode", user.getPersonCode());
		List<Map<String, Object>> list = kcManagerTeacherService.getKCInfo(request);
		map.put("data", list);
		return map;
	}
	
	/**
	 * 导出教师课酬统计
	 */
	@RequestMapping(value = "/exportTeacherKC")
	public void exportTeacherKC(HttpServletRequest request,HttpServletResponse response) {
		SystemUser user = getUser();
		List<Map<String, Object>> productName = kcManagerTeacherService.getProductName(user.getPersonCode());
		Map<String, String> parMap = new HashMap<String, String>();
		parMap.put("startTime", request.getParameter("startTime"));
		parMap.put("endTime", request.getParameter("endTime"));
		parMap.put("personCode", user.getPersonCode());
		List<Map<String, Object>> list = kcManagerTeacherService.getKCData(parMap);
		String name = "教师课酬统计";
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String fileName = time + "_" + name + "" + ".xls";
		String workbookName = "课酬统计";
		Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
		exportMap.put("序号", "px");
		exportMap.put("年份", "year");
		exportMap.put("月份", "month");
		exportMap.put("教师类别", "teacherType");
		exportMap.put("姓名", "name");
		exportMap.put("身份证号", "idCard");
		exportMap.put("银行卡号", "bankCode");
		exportMap.put("国籍", "guoji");
		for (Map<String, Object> map : productName) {
			exportMap.put((String) map.get("projectName"), map.get("projectName"));
		}
		exportMap.put("课酬合计", "totalFee");
		ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response,request);
	}
	
	/**
	 * 导出教师课酬明细表格
	 */
	@RequestMapping(value = "/exportTeacherKCInfo")
	public void exportTeacherKCInfo(HttpServletRequest request,HttpServletResponse response) {
		SystemUser user = getUser();
		Map<String, Object> parMap = new HashMap<String, Object>();
		parMap.put("year", request.getParameter("year"));
		parMap.put("month", request.getParameter("month"));
		if (request.getParameter("projectName") != null && !"".equals(request.getParameter("projectName"))) {
			parMap.put("projectName", request.getParameter("projectName"));
		}
		parMap.put("personCode", user.getPersonCode());
		List<Map<String, Object>> list = kcManagerTeacherService.getKCInfo(parMap);
		String name = "教师课酬明细";
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String fileName = time + "_" + name + "" + ".xls";
		String workbookName = "课酬明细";
		Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
		exportMap.put("序号", "px");
		exportMap.put("年份", "year");
		exportMap.put("月份", "month");
		exportMap.put("项目部名称", "dep_name");
		exportMap.put("项目名称", "project_name");
		exportMap.put("课程名称", "course_id");
		exportMap.put("上课日期", "class_date");
		exportMap.put("基础课酬", "base_fee");
		exportMap.put("课时", "hour_actual");
		exportMap.put("人数系数", "person_factor");
		exportMap.put("课程系数", "course_factor");
		exportMap.put("课酬", "fee_course");
		ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response,request);
	}

	/**
	 * 获取教师本周课酬
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/getThisWeekKc")
	@ResponseBody
	public Map<String, Object> getThisWeekKc(@RequestParam Map<String, Object> param){
		Map<String, Object> map = new HashMap<String, Object>();
		param.put("personCode", getUser().getPersonCode());
		List<Map<String, Object>> list = kcManagerTeacherService.getThisWeekKc(param);
		String[] xParam = new String[list.size()];
		String[] yParam = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);	
			xParam[i] = m.get("week").toString();
			yParam[i] = m.get("fee").toString();
		}
		map.put("week", xParam);
		map.put("fee", yParam);
		return map;
	}
	
	@RequestMapping(value="/getTodayKc")
	@ResponseBody
	public String getTodayKc(){
		Map<String, Object> param = new HashMap<String, Object>();
		SystemUser user = getUser();
		param.put("personCode", user.getPersonCode());
		String feeTatol = kcManagerTeacherService.getTodayKc(param);
		if(feeTatol == null || "".equals(feeTatol)) {
			return "0";
		}
		return feeTatol;
	}
}
