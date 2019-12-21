package com.joysupply.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joysupply.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.github.pagehelper.PageInfo;
import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.SystemUser;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ClassManageService;
import com.joysupply.service.DataDictionaryService;
import com.joysupply.service.KcManagerService;
import com.joysupply.service.KcManagerTeacherService;
import com.joysupply.service.KcTeacherService;

import net.sf.json.JSONArray;

/**
 *已发教师课酬
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/teacherKc")
public class KcTeacherController extends BaseController {
	@Autowired
	private KcTeacherService kcTeacherService;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	private Log log=LogFactory.getLog(getClass());
	
	/**
	 * 已发教师页面表
	 * @param model
	 * @return
	 */
	@RequestMapping("/kcTeacher")
	public String unSendPage(Model model) {
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		model.addAllAttributes(resultMap);
		return "kcyfTeacher/yfTeacher";
	}

	/*@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value="/kcTeacher")
	public ModelAndView kcTeacher(Model model) {
		@SuppressWarnings("rawtypes")
		Map resultMap = new HashMap();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		List<DataDictionary> teacherType = dataDictionaryService.getDicItemListNoPage("13"); // 教师类型
		//计算年月
		List<String> yearAndMonth1= getYearAndMonth1(5);
		
		List<Map<String, Object>> list = kcTeacherService.getProductName(getUser().getPersonCode());
		resultMap.put("teacherType", teacherType);
		resultMap.put("yearAndMonth1", yearAndMonth1);
		return new ModelAndView("kcyfTeacher/yfTeacher",resultMap);
	}
	
	private List<String> getYearAndMonth1(int i) {
		List<String> yearAndMonth1=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");		
		Calendar c = Calendar.getInstance();		
		for (int j = 0; j < i; j++) {
			c.setTime(new Date());
			c.add(Calendar.MONTH, -j);		
			Date m = c.getTime();		
			String data = sdf.format(m);
			yearAndMonth1.add(data);
		}
		return yearAndMonth1;
	}*/
	
	
	/**
	 * 已发人员课酬信息表
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/getkcTeacher",method=RequestMethod.GET)
	@ResponseBody
	public List getkcTeacher(@RequestParam Map<String,Object> params){
		String personCode = getUser().getPersonCode();
		params.put("personCode", personCode);
		return this.kcTeacherService.getkcTeacher(params);
	}
	/*@SuppressWarnings("unused")
	@RequestMapping(value="/getkcTeacher",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getkcTeacher(@RequestParam Map<String,String> params ,String yearAndMonth1){
		String year="";
		String month="";
		if(yearAndMonth1!=null&&!"".equals(yearAndMonth1)){
			String[] split = yearAndMonth1.split("-");
			year=split[0];
			if(split[1].startsWith("0")){
				month=split[1].substring(1);
			}else{
				month=split[1];
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> data = kcTeacherService.getkcTeacher(params);
		map.put("data", data);
		map.put("count", data.size());
		return map;
	}*/
	
	/**
	 * 部门维度课酬页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/kcWdDepartment")
	public String kcStatistics(Model model) {
		Map resultMap = new HashMap();
			resultMap.putAll(getMenuMap());
			resultMap.put("user", getUser());
			model.addAllAttributes(resultMap);
		return "kcDepartment/wdDepartment";
	}
	 /**
      * 部门维度课酬页面信息表
      * @param
      * @return
      */
    @RequestMapping(value = "/getVdoingInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getVdoingInfo(@RequestParam Map<String,Object> param) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	int pageNow = Integer.parseInt(param.get("page").toString());
    	int pageSize = Integer.parseInt(param.get("limit").toString());
    	int start = ( pageNow - 1 ) * pageSize;
    	param.put("start", start);
    	param.put("pageSize", pageSize);
        List<Map<String, Object>> list = kcTeacherService.getVdoingInfo(param);
		int sum = list.size();
		if(list != null){
			if (start+pageSize > sum) {
				list = list.subList(start,sum);
			}else {
				list = list.subList(start,start+pageSize);
			}
		}

        //long totalCount = kcTeacherService.getVdoingCount(param);
        result.put("data", list);
		result.put("count", sum);
        result.put("code", 0);
        return result;
    }

    //返回课酬明细页面
    @RequestMapping(value="/departDetail")
    public ModelAndView DepartDetail(@RequestParam Map<String, Object> map) {
    	map.putAll(getMenuMap());
    	map.put("user", getUser());
    	return new ModelAndView("kcDepartment/mxDepartment",map);
    } 
    
    //查询课酬明细数据
    @RequestMapping(value="/getDepartDetail",method=RequestMethod.GET)
    @ResponseBody
    public List queryKcDetatils(@RequestParam Map<String,Object> params){
    	return kcTeacherService.getDepartDetail(params);
    }

	/**
	 *教师类别课酬 王越雷修改，不允许删除
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/kcTeacherType")
	public ModelAndView kcTeacherType(Model model) {
		Map resultMap = new HashMap();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		List<DataDictionary> teacherType = dataDictionaryService.getDicItemListNoPage("13"); // 教师类型
		//计算年月
		List<String> yearAndMonth= kcTeacherService.getYearMonth().getData();
		//获取工程名
		List<Map<String, Object>> list = kcTeacherService.getProductName(getUser().getPersonCode());
		resultMap.put("teacherType", teacherType);
		resultMap.put("yearAndMonth", yearAndMonth);
		resultMap.put("productName", list);
		return new ModelAndView("kcLbTeacher/LbTeacher",resultMap);
	}

	//WangYuelei 教师类别课酬
	@RequestMapping(value = "/queryKcTeacherData",method = RequestMethod.GET)
	@ResponseBody
	public List<ResultData> queryKcTeacherData(@RequestParam Map<String,Object> map){
		try{
			log.debug(map);
			List<ResultData> data = kcTeacherService.selRemTeacher(map);
			return data;
		}catch (BusinessLevelException e){
			e.printStackTrace();
			return new ArrayList<ResultData>(){
				{
					new ResultData<>(e.getMessage());
				}
			};
		}
	}
	
	//WangYuelei  教师类别导出
    @RequestMapping(value="/exportTeacher")
	public void exportTeacher(@RequestParam String data , HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String,Object> param = new ObjectMapper().readValue(data,HashMap.class);
        String year = "",month="";
        if(param.get("year")!=null){
        	year = param.get("year").toString();
		}
		if(param.get("month")!=null){
			month = param.get("month").toString();
			
		}
		String fileName = year+month+" 教师类别课酬.xls";
		/*param.put("page",1);
		param.put("limit",10000);

		List<ResultData> list = kcTeacherService.selRemTeacher(param);
		List<Map<String, Object>> nameList = (List<Map<String,Object>>)list.get(1).getData();
		List<Map<String, Object>> dataList = (List<Map<String,Object>>)list.get(0).getData();
		if(dataList.size() > 0){
			for (Map<String, Object> map : dataList) {
				List depVal = (List)map.get("depVal");
				Double total = 0d;
				for (int i = 0; i < depVal.size(); i++) {
					String fee = depVal.get(i).toString();
					total += Double.valueOf(fee);
				}
				map.put("total",total);
			}
		}
		Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
		exportMap.put("年份", "year");
		exportMap.put("月份", "month");
		exportMap.put("教师类别", "teacherType");
		if(nameList.size() > 0){
			for (Map<String,Object> map1: nameList) {
				exportMap.put(map1.get("depName").toString(),map1.get("depName"));
			}
		}
		exportMap.put("工作量扣除", "deduction");
		exportMap.put("课酬合计", "totalCount");
		List<Map<String, Object>> list1 = new ArrayList<>();
		if(dataList.size() > 0){
			for (Map<String, Object> map : dataList) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("year",map.get("year"));
				m.put("month",map.get("month"));
				m.put("teacherType",map.get("teacherType"));
				m.put("deduction",map.get("deduction"));
				m.put("totalCount",map.get("total"));
				list1.add(m);
			}
		}
		String workbookName = "教师类别课酬";*/
		//ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list1, response,request);
		//获取没有分页的数据
		Map<String,Object> mapData =  kcTeacherService.selRemTeacherNoPage(param);
		List<Map> dataList = (List<Map>)mapData.get("data");
		if(dataList.size() > 0){
			for (Map<String, Object> map : dataList) {
				List depVal = (List)map.get("depVal");
				Double total = 0d;
				for (int i = 0; i < depVal.size(); i++) {
					String fee = depVal.get(i).toString();
					total += Double.valueOf(fee);
				}
				List<String> depName = (List<String>)map.get("valName");
				for(int x = 0; x < depName.size(); x++){
					
				}
				map.put("total",total);
			}
		}
		if(dataList.size() > 0){
			Map<String,Object> map = new HashMap<>();
			Double deduction = 0d;
			List<String> valName = new ArrayList<>();
			Double total = 0d;
			for (int i = 0; i < dataList.size(); i++) {
				Double depTotal = 0d;
				Map map1 = dataList.get(i);
				deduction += Double.valueOf(map1.get("deduction").toString());
				total += Double.valueOf(map1.get("total").toString());
				//List<String> valName1 = (List<String>) map1.get("valName");
				//map.put("valName",valName1);
			}
			map.put("year","合计");
			map.put("valName",valName);
			map.put("month","");
			map.put("teacherType","");
			map.put("deduction",deduction);
			map.put("total",total);
			dataList.add(map);
		}
        HSSFWorkbook wb = AllTimetable.exportTeacher(mapData);
		if (MyHttpUtil.isMSBrowser(request)) {
			response.setHeader("content-disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
		} else {
			response.setHeader("content-disposition", "attachment;filename="
					+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		}
		OutputStream outputStream = response.getOutputStream();
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}

    private List<String> getYearAndMonth(int i) {
		List<String> yearAndMonth=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();		
		for (int j = 0; j < i; j++) {
			c.setTime(new Date());
			c.add(Calendar.MONTH, -j);		
			Date m = c.getTime();		
			String data = sdf.format(m);
			yearAndMonth.add(data);
		}
		return yearAndMonth;
	}

	/**
	 * 教师类别信息表
	 * @param
	 * @return
	 */
	@RequestMapping("/getTeacherType")
	@ResponseBody
	public Map<String, Object> getKclbTeacher(String yearAndMonth,String teacherType, String project_name){
		String year="";
		String month="";
		if(yearAndMonth!=null && !"".equals(yearAndMonth)){
			String[] split = yearAndMonth.split("-");
			year=split[0];
			if(split[1].startsWith("0")){
				month=split[1].substring(1);
			}else{
				month=split[1];
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = kcTeacherService.getKclbTeacher(teacherType,year,month);
		map.put("data", list);
		return map;
	}
	
	/**
	 * 获取项目名称
	 * @return
	 */
	@RequestMapping(value="/getProductName")
	
	@ResponseBody
	public Map<String, Object> getProductName(){
		Map<String, Object> map = new HashMap<String, Object>();
		SystemUser user = getUser();
		String personCode = user.getPersonCode();
		List<Map<String, Object>> list = kcTeacherService.getProductName(personCode);
		map.put("data",list);
		return map;
	}
	/**
	 * 导出部门维度课酬
	 */
	@RequestMapping(value = "/exportTeacherKC")
	public void exportTeacherKC(HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> parMap = new HashMap<String, Object>();
			parMap.put("start", 0);
			parMap.put("pageSize", 10000);
			parMap.put("startTime", request.getParameter("startTime"));
			parMap.put("endTime", request.getParameter("endTime"));
			List<Map<String, Object>> list = kcTeacherService.getVdoingInfo(parMap);
			//添加序号
			int num = 1;
			int countHour = 0;
			String totalFee = "0.00";
			int hourActual = 0;
			String feeCourse = "0.00";
			if (list != null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					map.put("px", num);
					num++;
					countHour += Integer.parseInt(map.get("countHour").toString());
					totalFee = Amount.add(totalFee, map.get("totalFee").toString());
					hourActual += Integer.parseInt(map.get("hourActual").toString());
					feeCourse = Amount.add(feeCourse, map.get("feeCourse").toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("px", "合计");
				map.put("countHour", countHour);
				map.put("totalFee", totalFee);
				map.put("hourActual", hourActual);
				map.put("feeCourse", feeCourse);
				list.add(map);
			}
			String name = "部门维度课酬";
			String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			String fileName = time + "_" + name + "" + ".xls";
			String workbookName = "维度课酬";
			Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
			exportMap.put("序号", "px");
			exportMap.put("项目部名称", "organizeName");
			exportMap.put("总课时", "countHour");
			exportMap.put("应付课酬(元)", "totalFee");
			exportMap.put("已付课时", "hourActual");
			exportMap.put("已付课酬(元)", "feeCourse");
			ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response, request);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 部门维度课酬柱状图
	 * @return
	 */
	@RequestMapping(value="/histogram")
	public ModelAndView histogram(@RequestParam Map<String,Object> map){
		return new ModelAndView("kcDepartment/histogram",map);
	}
	
	@RequestMapping(value="/getDimensionHistogram")
	@ResponseBody
	public Map<String, Object> getDimensionHistogram(@RequestParam Map<String, Object> param){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = kcTeacherService.getVdoingInfo(param);
		String[] xParam = new String[list.size()];
		String[] yParam = new String[list.size()];
		if(list != null && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);
				xParam[i] = m.get("organizeName").toString();
				yParam[i] = m.get("totalFee").toString();
			}
		}
		map.put("name", xParam);
		map.put("fee", yParam);
		return map;
	}
	
}
