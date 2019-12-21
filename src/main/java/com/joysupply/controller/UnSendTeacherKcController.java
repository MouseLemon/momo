package com.joysupply.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joysupply.entity.OrganizeStruct;
import com.joysupply.service.OrganizeStructService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.joysupply.service.UnSendTeacherKcService;
import com.joysupply.util.AllTimetable;
import com.joysupply.util.ExcelUtil_JXL;

/**
 * 未发教师课酬
 * */
@Controller
@RequestMapping("/unSend")
public class UnSendTeacherKcController extends BaseController {

	@Autowired
	private UnSendTeacherKcService unSendTeacherKcService;
	@Autowired
	private OrganizeStructService organizeStructService;

	//未发教师课酬页
	@RequestMapping("/unSendPage")
	public String unSendPage(Model model) {
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		model.addAllAttributes(resultMap);
		return "kcManagement/teacherKcUnSend";
	}

	//已发教师课酬页
	@RequestMapping("/sendPage")
	public String sendPage(Model model) {
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		model.addAllAttributes(resultMap);
		return "kcManagement/teacherKcSend";
	}

	/**
	 * 获得项目
	 * */
	@RequestMapping(value="/getProductName",method=RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> projectName(@RequestParam Map<String, Object> map){
		String personCode = getUser().getPersonCode();
		map.put("personCode",personCode);
		//List<Map<String,Object>> projectName = unSendTeacherKcService.getProjectName(personCode);
		List<Map<String, Object>> projectName = unSendTeacherKcService.getProjectName(map);
		return projectName;
	}

	/**
	 * 获取教师未发已发课酬列表(根据status判断)
	 * */
	@RequestMapping(value="/getUnsendData",method=RequestMethod.GET)
	@ResponseBody
	public List getUnsendData(@RequestParam Map<String,Object> params){
		params.put("personCode",getUser().getPersonCode());
		List data = this.unSendTeacherKcService.getUnsendData(params);
		return data;
	}

	@RequestMapping(value="/teacherInfo")
	public ModelAndView teacherInfo(@RequestParam Map<String, Object> map){
		map.putAll(getMenuMap());
		map.put("user", getUser());
		return new ModelAndView("kcManageMentTeacher/teacherInfo",map);
	}

	/**
     * 导出未发教师课酬
     * @param request
     * @param response
     */
	@RequestMapping(value="/exportUnSendKc")
	@ResponseBody
    public void exportUnSendKc(HttpServletRequest request,HttpServletResponse response){
		//wangyuelei加权限
		Map<String, Object> parMap = new HashMap<String, Object>();
		String teacherName = request.getParameter("teacherName");
		String tme = request.getParameter("time");
        String note = request.getParameter("note");
		parMap.put("teacherName", teacherName);
		parMap.put("note", note);
		parMap.put("personCode",getUser().getPersonCode());
		parMap.put("time", tme);
		parMap.put("status", 2);
		List<List<Map<String, Object>>> unsendData = unSendTeacherKcService.getUnsendData(parMap);
		List<Map<String,Object>> projectName = unSendTeacherKcService.getProjectName(parMap);
        List<Map<String, Object>> list = new ArrayList<>();
		if(unsendData.size() > 1){
			for (List<Map<String, Object>> list2 : unsendData) {
				for (Map<String, Object> map : list2) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("px", map.get("px"));
					m.put("year", map.get("year"));
					m.put("month", map.get("month"));
					m.put("teacherType", map.get("teacherType"));
					m.put("cardNo", map.get("cardNo"));
					m.put("nationality", map.get("nationality"));
					if(map.get("sex") != null && !"".equals(map.get("sex"))){
						String sex = map.get("sex").toString();
						if(sex.equals("1")){
							m.put("sex", "男");
						}else if(sex.equals("2")){
							m.put("sex", "女");
						}else{
							m.put("sex", "");
						}
					}else{
						m.put("sex","");
					}
					m.put("birthDay", map.get("birthDay"));
					m.put("entryDate", map.get("entryDate"));
					m.put("prvOutDate", map.get("prvOutDate"));
					m.put("joinTime", map.get("joinTime"));
					m.put("teleNum", map.get("teleNum"));

					m.put("teacherName", map.get("accountName"));
					m.put("idCard", map.get("idCard"));
					m.put("bankCode", map.get("bankCode"));
					m.put("nationality", map.get("nationality"));
					m.put("projectTeacher", map.get("projectTeacher"));
					if(projectName.size() > 0){
						if(map.containsKey("depCode")){
							for (Map<String,Object> map1: projectName) {
                                String depCode = map1.get("depCode").toString();
                                String depName = map1.get("depName").toString();
                                if(map.containsKey(depCode)){
                                    m.put(depName,map.get(depCode));
                                }else {
                                    m.put(depName,map.get("0.0"));
                                }
							}
						}
					}
					m.put("1901", map.get("1901"));
					m.put("1902", map.get("1902"));
					m.put("1903", map.get("1903"));
					m.put("totalCount", map.get("totalCount"));
					m.put("memo", map.get("memo"));
					list.add(m);
				}
			}
		}
		String name = teacherName + "教师未发课酬";
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String fileName = time + "_" + name + ".xls";
		String workbookName = "教师课酬";
		Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
		exportMap.put("序号", "px");
		exportMap.put("年份", "year");
		exportMap.put("月份", "month");
		exportMap.put("教师类别", "teacherType");
		exportMap.put("一卡通号", "cardNo");
		exportMap.put("国籍", "nationality");
		exportMap.put("性别", "sex");
		exportMap.put("出生日期", "birthDay");
		exportMap.put("入境日期", "entryDate");
		exportMap.put("预计出境日期", "prvOutDate");
		exportMap.put("入职日期", "joinTime");
		exportMap.put("联系电话", "teleNum");
		exportMap.put("开户姓名", "teacherName");
		exportMap.put("身份证号", "idCard");
		exportMap.put("银行卡号", "bankCode");
		exportMap.put("所得项目", "projectTeacher");
		if(projectName.size() > 0){
            for (Map<String,Object> map1: projectName) {
                exportMap.put(map1.get("depName").toString(),map1.get("depName"));
            }
        }

		exportMap.put("工作量扣除", "1901");
		exportMap.put("讲座", "1902");
		exportMap.put("补发课酬", "1903");
		exportMap.put("课酬合计", "totalCount");
		exportMap.put("备注", "memo");
		ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response, request);
    }
	/**
	 * 导出已发教师课酬
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/exportSendTeacherKC")
	@ResponseBody
	public void exportSendTeacherKC(HttpServletRequest request,HttpServletResponse response){
		String personCode = getUser().getPersonCode();
		Map<String, Object> parMap = new HashMap<String, Object>();
		String teacherName = request.getParameter("teacherName");
		String tme = request.getParameter("time");
		String note = request.getParameter("note");
		parMap.put("teacherName", teacherName);
		parMap.put("note", note);
		parMap.put("personCode",personCode);
		parMap.put("time", tme);
		parMap.put("status", 3);
		List<List<Map<String, Object>>> unsendData = unSendTeacherKcService.getUnsendData(parMap);
        List<Map<String, Object>> projectName = unSendTeacherKcService.getProjectName(parMap);
		List<Map<String, Object>> list = new ArrayList<>();
		if(unsendData.size() > 0){
			for (List<Map<String, Object>> list2 : unsendData) {
				for (Map<String, Object> map : list2) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("px", map.get("px"));
					m.put("year", map.get("year"));
					m.put("month", map.get("month"));
					m.put("teacherType", map.get("teacherType"));
					m.put("cardNo", map.get("cardNo"));
					m.put("nationality", map.get("nationality"));
					if(map.get("sex") != null && !"".equals(map.get("sex"))){
						String sex = map.get("sex").toString();
						if(sex.equals("1")){
							m.put("sex", "男");
						}else if(sex.equals("2")){
							m.put("sex", "女");
						}else{
							m.put("sex", "");
						}
					}else{
						m.put("sex","");
					}
					m.put("birthDay", map.get("birthDay"));
					m.put("entryDate", map.get("entryDate"));
					m.put("prvOutDate", map.get("prvOutDate"));
					m.put("joinTime", map.get("joinTime"));
					m.put("teleNum", map.get("teleNum"));

					m.put("teacherName", map.get("accountName"));
					m.put("idCard", map.get("idCard"));
					m.put("bankCode", map.get("bankCode"));
					m.put("nationality", map.get("nationality"));
					m.put("projectTeacher", map.get("projectTeacher"));
                    if(projectName.size() > 0){
						if(map.containsKey("depCode")){
							for (Map<String,Object> map1: projectName) {
								String depCode = map1.get("depCode").toString();
								String depName = map1.get("depName").toString();
								if(map.containsKey(depCode)){
									m.put(depName,map.get(depCode));
								}else {
									m.put(depName,map.get("0.0"));
								}
							}
						}
                    }
					m.put("1901", map.get("1901"));
					m.put("1902", map.get("1902"));
					m.put("1903", map.get("1903"));
					m.put("totalCount", map.get("totalCount"));
					m.put("memo", map.get("memo"));
					list.add(m);
				}
			}
		}
		String name = teacherName + "教师已发课酬";
		String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String fileName = time + "_" + name + ".xls";
		String workbookName = "教师课酬";
		Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
		exportMap.put("序号", "px");
		exportMap.put("年份", "year");
		exportMap.put("月份", "month");
		exportMap.put("教师类别", "teacherType");
		exportMap.put("一卡通号", "cardNo");
		exportMap.put("国籍", "nationality");
		exportMap.put("性别", "sex");
		exportMap.put("出生日期", "birthDay");
		exportMap.put("入境日期", "entryDate");
		exportMap.put("预计出境日期", "prvOutDate");
		exportMap.put("入职日期", "joinTime");
		exportMap.put("联系电话", "teleNum");
		exportMap.put("开户姓名", "teacherName");
		exportMap.put("身份证号", "idCard");
		exportMap.put("银行卡号", "bankCode");
		exportMap.put("国籍", "nationality");
		exportMap.put("所得项目", "projectTeacher");
        if(projectName.size() > 0){
            for (Map<String,Object> map1: projectName) {
                exportMap.put(map1.get("depName").toString(),map1.get("depName"));
            }
        }
		exportMap.put("工作量扣除", "1901");
        exportMap.put("讲座", "1902");
        exportMap.put("补发课酬", "1903");
		exportMap.put("课酬合计", "totalCount");
		exportMap.put("备注", "memo");
		ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response,request);
	}


}
