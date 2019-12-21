package com.joysupply.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.joysupply.dao.AuthorityManageDao;

import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.KcManagerDao;
import com.joysupply.service.KcManagerService;
import com.joysupply.service.MessageService;

@Service("kcManagerService")
public class KcManagerServiceImpl extends BaseService implements KcManagerService{

	private Log log = LogFactory.getLog(getClass());
	@Autowired
	private KcManagerDao kcManagerDao;
	@Autowired
    private AuthorityManageDao authorityManageDao;
	@Autowired
	private MessageService messageService;
	
	@Override
	public List<Map<String, Object>> queryKcInfo(Map<String, Object> params) {

		List<Map<String,Object>> kcMapdata = this.kcManagerDao.queryKcInfo(params);
        
		List teacherProjectGroup = new ArrayList<>();
		for(Map<String,Object> map : kcMapdata) {
			String baseFee = map.get("baseFee").toString();
			String personFactor = map.get("personFactor").toString();
			String courseFactor = map.get("courseFactor").toString();
			String hourActual = map.get("hourActual").toString();
			map.put("classFee", Amount.mul(hourActual,Amount.mul(Amount.mul(baseFee, personFactor),courseFactor)));
		}
		int num=1;
		int groupId=0;
		//第一级分组  教师名，项目名，语种，教师类别，课程名称
		Map<String,List<Map<String,Object>>> teacherGroup = kcMapdata.stream().collect(Collectors.groupingBy(j -> j.get(params.get("selectParam")).toString()));
		Iterator<Map.Entry<String, List<Map<String,Object>>>> iterTeacher = teacherGroup.entrySet().iterator();
		while(iterTeacher.hasNext()) {
			Map.Entry<String, List<Map<String,Object>>> entryTeacher = iterTeacher.next();
			List<Map<String,Object>> valueTeacher = entryTeacher.getValue();
			//System.out.println(valueTeacher);
			
			//按照项目分组
			Map<String,List<Map<String,Object>>> projectGroup = valueTeacher.stream().collect(Collectors.groupingBy(j -> j.get("projectId").toString()));
			
			List<Map<String,Object>> teacherCourseGroup = new ArrayList<>();
			
			Iterator<Map.Entry<String, List<Map<String,Object>>>> iterProject = projectGroup.entrySet().iterator();
			
			while(iterProject.hasNext()) {
				Map.Entry<String, List<Map<String,Object>>> entryProject = iterProject.next();
				List<Map<String,Object>> valueProject = entryProject.getValue();
				//按照项目课程分组
				Map<String,Object> projectMap = new HashMap<>();
				
				Map<String,List<Map<String,Object>>> courseGroup = valueProject.stream().collect(Collectors.groupingBy(j -> j.get("courseName").toString()));
				Iterator<Map.Entry<String, List<Map<String,Object>>>> iterCourse = courseGroup.entrySet().iterator();
				
				while(iterCourse.hasNext()) {
					Map.Entry<String, List<Map<String,Object>>> entryCourse = iterCourse.next();
					List<Map<String,Object>> valueCourse = collection(entryCourse.getValue());
					Map<String,Object> paMap = new HashMap<>();
					String kcCount="0.00";
					String ksCount="0";
					for(Map<String,Object> map : valueCourse) {
						kcCount = Amount.add(kcCount, map.get("classFee").toString());
						ksCount = Amount.add(ksCount, map.get("hourActual").toString());
						if(paMap.isEmpty()) {
							paMap = map;
						}
					}
					paMap.put("px",num);
					paMap.put("timeSolt", params.get("startTime").toString().replace("-","/")+"-"+
							params.get("endTime").toString().replace("-","/"));
					paMap.put("kcCount", kcCount);
					paMap.put("ksCount", ksCount.substring(0,ksCount.indexOf(".")));
					paMap.put("groupId", groupId);
					num++;
					teacherCourseGroup.add(paMap);
				}
			}
			Map<String,Object> maphj = new HashMap<>();
			String ksCount = "0";
			String kcCount = "0.00";
			for(Map<String,Object> hj : teacherCourseGroup) {
				if(hj.get("projectId").equals("1901")) {
					kcCount = Amount.sub(kcCount, hj.get("kcCount").toString());
					ksCount = Amount.sub(ksCount, hj.get("ksCount").toString());
				}else {
					kcCount = Amount.add(kcCount, hj.get("kcCount").toString());
					ksCount = Amount.add(ksCount, hj.get("ksCount").toString());
				}
				ksCount = ksCount.substring(0,ksCount.indexOf("."));
				if(maphj.isEmpty()) {
					maphj.put("timeSolt",hj.get("timeSolt").toString());
					if("projectId".equals(params.get("selectParam").toString())) {
						maphj.put("projectName",hj.get("projectName").toString());
					}else {
						maphj.put(params.get("selectParam").toString(),hj.get(params.get("selectParam")).toString().toString());
					}
				}
			}
			maphj.put("px", "小计");
			maphj.put("ksCount",ksCount);
			maphj.put("kcCount",kcCount);
			teacherCourseGroup.add(maphj);
			groupId++;
			teacherProjectGroup.add(teacherCourseGroup);
		}
		if(teacherProjectGroup.size() == 0){
			return teacherProjectGroup;
		}
		String kcCount = "0.00";
		String ksCount="0";
		Map<String,Object> hjMap = new HashMap<String,Object>();
		for(int i=0;i<teacherProjectGroup.size();i++) {
			List<Map<String,Object>> list = (List<Map<String, Object>>) teacherProjectGroup.get(i);
			for(int j=0;j<list.size();j++) {
				Map<String,Object> map = list.get(j);
				if(map.get("px").equals("小计")) {
					kcCount = Amount.add(kcCount, map.get("kcCount").toString());
					ksCount = Amount.add(ksCount, map.get("ksCount").toString());
				}
			}
		}
		ksCount = ksCount.substring(0,ksCount.indexOf("."));
		hjMap.put("px", "合计");
		hjMap.put("timeSolt", params.get("startTime").toString().replace("-","/")+"-"+
		params.get("endTime").toString().replace("-","/"));
		hjMap.put("kcCount", kcCount);
		hjMap.put("ksCount", ksCount);
		List<Map<String,Object>> hjList = new ArrayList<>();
		hjList.add(hjMap);
		teacherProjectGroup.add(hjList);
		
		return teacherProjectGroup;
	}
	
	private List<Map<String,Object>> collection(List<Map<String,Object>> listMap){
		Collections.sort(listMap,new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return o1.get("classDate").toString().compareTo(o2.get("classDate").toString());
			}
		});
		return listMap;
	}

	@Override
	@Transactional
	public String saveKcList(Map<String, Object> params) {
		List<JSONObject> listMap = new ArrayList<JSONObject>();
		List<JSONObject> deductListMap = new ArrayList<JSONObject>();
		JSONObject jo = JSON.parseObject(params.get("kcInfo").toString());
		JSONArray jsaTotal = jo.getJSONArray("data");
		String startTime = params.get("startTime").toString();
		String endTime = params.get("endTime").toString();
		String serialNumber="";
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			String curDate = df.format(new Date()).replace("-","");
			params.put("yearMonth", curDate);
			//查询有没有流水号记录
			String lastSerialNumber= this.kcManagerDao.queryExamineData(params);
			//保存之前清除上次保存记录
			if(!lastSerialNumber.equals("0")) {
				params.put("serialNumber",lastSerialNumber);
				this.kcManagerDao.deleteLastSave(params);
				serialNumber = lastSerialNumber;
			}else{
				String maxSerialNum = kcManagerDao.getMaxSerialNum(params);
				if(maxSerialNum == null){
					serialNumber = curDate+"0001";
				}else{
					serialNumber = String.valueOf(Integer.parseInt(maxSerialNum) + 1 );
				}
			}
			params.put("serialNumber", serialNumber);
			
			for(int i=0;i<jsaTotal.size();i++) {
				JSONArray jsaList = jsaTotal.getJSONArray(i);
				for(int j=0;j<jsaList.size();j++) {
					JSONObject joList = jsaList.getJSONObject(j);
					if(!joList.get("px").toString().equals("小计") && !joList.get("px").toString().equals("合计")) {
						joList.put("rowId",Constants.GUID());
						joList.put("serialNumber",serialNumber);
						joList.put("startTime", startTime);
						joList.put("endTime", endTime);
						listMap.add(joList);
						if(joList.get("projectId").equals("1901") || joList.get("projectId").equals("1902")||joList.get("projectId").equals("1903")) {
							deductListMap.add(joList);
						}
					}
				}
			}
			this.kcManagerDao.saveKcList(listMap);
			if(!deductListMap.isEmpty()) {
				//保存老师的历史记录
				this.kcManagerDao.saveDeList(deductListMap);
				//保存流水号-项目-教师的工作量扣除
				String finalSerialNumber = serialNumber;
				deductListMap.forEach(e->{
					e.put("rowId",Constants.GUID()+"");
					e.put("status",2);
					e.put("serialNumber", finalSerialNumber);
				});
				this.kcManagerDao.saveFeeDeduct(deductListMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "新增课酬失败！";
		}
		return serialNumber;
	}


	@Override
	public Map<String, Object> queryLastProject(Map<String, String> params) {
		return kcManagerDao.queryLastProject(params);
	}

	@Override
	public OpResult checkSave(Map<String, Object> params) {
		List<Map<String, Object>> count = kcManagerDao.checkTimeSolt(params);
		if(count.size()>0) {
			return new OpResult("当前时间段存在已计算的数据，请重新选择日期！");
		}
		return new OpResult();
	}

	@Override
	@Transactional
	public OpResult commit(Map<String,Object> params) {
		try {
			//查询需要保存的数据
			String serialNumber=""; 
			List<Map<String,String>> listmap= new ArrayList<>();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			String curDate = df.format(new Date()).replace("-","");
			params.put("yearMonth", curDate);
			serialNumber= this.kcManagerDao.querySerialNum(params);
			//保存待审批记录
			if(!"".equals(params.get("personCode1"))) {
				Map<String,String> map = new HashMap<>();
				map.put("serialNumber", serialNumber);
				map.put("personCode", params.get("personCode1").toString());
				map.put("approveOrder", "1");
				map.put("depType", "1");
				map.put("rowId", Constants.GUID());
				addMsg(params);
				listmap.add(map);
			}
			if(!"".equals(params.get("personCode2"))) {
				Map<String,String> map = new HashMap<>();
				map.put("serialNumber", serialNumber);
				map.put("personCode", params.get("personCode2").toString());
				map.put("approveOrder", "2");
				map.put("depType", "1");
				map.put("rowId", Constants.GUID());
				listmap.add(map);
			}
			if(!"".equals(params.get("personCode3"))) {
				Map<String,String> map = new HashMap<>();
				map.put("serialNumber", serialNumber);
				map.put("personCode", params.get("personCode3").toString());
				map.put("approveOrder", "3");
				map.put("depType", "1");
				map.put("rowId", Constants.GUID());
				listmap.add(map);
			}
			this.kcManagerDao.saveExamineData(listmap);
			
			//修改课酬表状态为提交未审核
			this.kcManagerDao.updatefeeStatus(serialNumber);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new OpResult("提交审核失败："+e.getMessage());
		}
		return new OpResult();
	}

	private void addMsg(Map<String, Object> params) {
		String personCode = params.get("personCode1").toString();
		String name = messageService.getPersonNameByPersonCode(personCode);
		String curDate = params.get("yearMonth").toString();
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("personCode", personCode);
		msg.put("type", 2);
		msg.put("teacherName", name);
		msg.put("projectName", curDate+"月课酬");
		messageService.addMessage(msg);
	}

	@Override
	public List queryKcDetatils(Map<String,String> params) {
		List<Map<String,Object>> kcDetailsMap = this.kcManagerDao.queryKcDetatils(params);
		String selectParam = params.get("selectParam");
		Map<String,List<Map<String,Object>>> teacherGroup = kcDetailsMap.stream().collect(Collectors.groupingBy(j -> j.get(selectParam).toString()));
		List<List<Map<String,Object>>> result = new ArrayList<>();
		Iterator<Map.Entry<String, List<Map<String,Object>>>> teacherGroupIterator = teacherGroup.entrySet().iterator();
		int num = 1;
		int groupNum = 1;
		while(teacherGroupIterator.hasNext()) {
			List<Map<String,Object>> v = teacherGroupIterator.next().getValue();
			String ksCount="0";
			String kcCount="0.00";
			String projectSub="0.00";
			Map<String,Object> subTotal = new HashMap<>();
			for(int i=0;i<v.size();i++) {
				Map<String,Object> one = v.get(i);
				one.put("px", num);
				if(subTotal.isEmpty()) {
					subTotal.put("px", "小计");
					subTotal.put("timeSolt", one.get("startTime").toString().replace("-", "/")+"-"+one.get("endTime").toString().replace("-", "/"));
					if(selectParam.equals("teacherCode")) {
						subTotal.put("teacherName", one.get("teacherName"));
					}else {
						subTotal.put(selectParam, one.get(selectParam));
					}
				}
				if(one.get("projectId").equals("1901")) {
					projectSub = Amount.add(projectSub, one.get("feeCourse").toString());
					ksCount = Amount.sub(ksCount, one.get("hourActual").toString());
					kcCount = Amount.sub(kcCount, one.get("feeCourse").toString());
				}else {
					ksCount = Amount.add(ksCount, one.get("hourActual").toString());
					kcCount = Amount.add(kcCount, one.get("feeCourse").toString());
				}
				one.put("timeSolt", one.get("startTime").toString().replace("-", "/")+"-"+one.get("endTime").toString().replace("-", "/"));
				one.put("projectSub", "");
				num++;
			}
			ksCount = ksCount.substring(0,ksCount.indexOf("."));
			subTotal.put("hourActual", ksCount);
			subTotal.put("feeCourse", kcCount);
			subTotal.put("projectSub", projectSub);
			v.add(subTotal);
			result.add(v);
		}
		
		String kcCountTotal = "0.00";
		String ksCountTotal = "0";
		Map<String,Object> hjMap = new HashMap<String,Object>();
		for(int i=0;i<result.size();i++) {
			List<Map<String,Object>> list = (List<Map<String, Object>>) result.get(i);
			for(int j=0;j<list.size();j++) {
				Map<String,Object> map = list.get(j);
				if(map.get("px").equals("小计")) {
					if(hjMap.isEmpty()) {
						hjMap.put("timeSolt", map.get("timeSolt"));
					}
					kcCountTotal = Amount.add(kcCountTotal, map.get("feeCourse").toString());
					ksCountTotal = Amount.add(ksCountTotal,map.get("hourActual").toString());
				}
			}
		}
		hjMap.put("px", "合计");
		if(ksCountTotal.indexOf(".") >= 0){
			ksCountTotal = ksCountTotal.substring(0,ksCountTotal.indexOf("."));
		}
		hjMap.put("feeCourse", kcCountTotal);
		hjMap.put("hourActual", ksCountTotal);
		List<Map<String,Object>> hjList = new ArrayList<>();
		hjList.add(hjMap);
		result.add(hjList);
		return result;
	}

	@Override
	public Map<String, Object> projectKcQuery(Map<String, Object> params) {
		/*Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(params);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));*/
        //获取已审批记录
        List<Map<String, Object>> list = this.kcManagerDao.projectKcQuery(params);
        List<Map<String, Object>> listResult = new ArrayList<>();
        Map<String,List<Map<String,Object>>> serialNumberGroup = list.stream()
        			.collect(Collectors.groupingBy(j -> j.get("serialNumber").toString()));

        serialNumberGroup.forEach((k,v)->{
        	String ksCount = "0";
        	String kcCount = "0.00";
        	Map<String,Object> map = new HashMap<>();
        	for(int i=0;i<v.size();i++) {
        		Map<String,Object> v1 = v.get(i);
        		map.put("serialNumber", v1.get("serialNumber"));
        		String startTime = v1.get("startTime").toString().replace("-","/");
            	String endTime = v1.get("endTime").toString().replace("-","/");
            	map.put("timeSolt", startTime+"-"+endTime);
        		map.put("createTime", v1.get("createTime"));
        		map.put("year", v1.get("year"));
        		map.put("month", v1.get("month"));
        		map.put("status", v1.get("status"));
        		map.put("memo", v1.get("memo"));
        		if(v1.get("projectId").equals("1901")) {
        			ksCount = Amount.sub(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.sub(kcCount, v1.get("feeCourse").toString());
        		}else {
        			ksCount = Amount.add(ksCount,v1.get("hourActual").toString());
        			kcCount = Amount.add(kcCount, v1.get("feeCourse").toString());
        		}
        	}
        	map.put("hourActual", ksCount.substring(0, ksCount.indexOf(".")));
        	map.put("feeCourse", kcCount);
        	listResult.add(map);
        });
        //PageInfo page = new PageInfo(listResult);
        //long totalNum = page.getTotal();
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("count", listResult.size());
        map.put("data", listResult);
        map.put("code", 0);
        map.put("msg", "");
        return map;
	}

	@Override
	public Map<String, Object> projectKcQueryA(Map<String, Object> params) {
		//获取已审批记录
		List<Map<String, Object>> list = this.kcManagerDao.projectKcQuery(params);
		List<Map<String, Object>> listResult = new ArrayList<>();
		Map<String,List<Map<String,Object>>> serialNumberGroup = list.stream()
				.collect(Collectors.groupingBy(j -> j.get("serialNumber").toString()));

		serialNumberGroup.forEach((k,v)->{
			String ksCount = "0";
			String kcCount = "0.00";
			Map<String,Object> map = new HashMap<>();
			for(int i=0;i<v.size();i++) {
				Map<String,Object> v1 = v.get(i);
				map.put("serialNumber", v1.get("serialNumber"));
				String startTime = v1.get("startTime").toString();
				String endTime = v1.get("endTime").toString();
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				map.put("createTime", v1.get("createTime"));
				map.put("year", v1.get("year"));
				map.put("month", v1.get("month"));
				map.put("status", v1.get("status"));
				map.put("memo", v1.get("memo"));
				if(v1.get("projectId").equals("1901")) {
					ksCount = Amount.sub(ksCount, v1.get("hourActual").toString());
					kcCount = Amount.sub(kcCount, v1.get("feeCourse").toString());
				}else {
					ksCount = Amount.add(ksCount,v1.get("hourActual").toString());
					kcCount = Amount.add(kcCount, v1.get("feeCourse").toString());
				}
			}
			map.put("hourActual", ksCount.substring(0, ksCount.indexOf(".")));
			map.put("feeCourse", kcCount);
			listResult.add(map);
		});

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("count", listResult.size());
		map.put("data", listResult);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}

	@Override
	public List<Map<String, Object>> getKCDetail(Map<String, Object> param) {
		//wangyuelei增加权限
		/*List<Map<String,Object>> myAuth = authorityManageDao.getPersonProjectAuth(param.get("personCode")+"");
		param.put("myAuth",myAuth);
		List<String> serialNumbers = kcManagerDao.getSerialNumbers(param);
		param.put("serialNumbers",serialNumbers);*/
		//查询课酬明细数据
		List<Map<String, Object>> list = kcManagerDao.getKCDetail(param);
		int px = 1;
		String feeTotal = "0.00";
		String hourActual = "0";
		if(list.isEmpty()){
			return list;
		}
		for (Map<String, Object> map : list) {
			String deductType = map.get("deductType").toString();
			if("0".equals(deductType)){
				feeTotal = Amount.add(feeTotal, map.get("feeCourse").toString());
				hourActual = Amount.add(hourActual, map.get("hourActual").toString());
			}else{
				if("1901".equals(deductType)){
					feeTotal = Amount.sub(feeTotal, map.get("feeCourse").toString());
					hourActual = Amount.sub(hourActual, map.get("hourActual").toString());
				}else{
					feeTotal = Amount.add(feeTotal, map.get("feeCourse").toString());
					hourActual = Amount.add(hourActual, map.get("hourActual").toString());
				}
			}
			map.put("px", px);
			px++;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("px", "合计");
		map.put("feeCourse", feeTotal);
		//hourActual = hourActual.substring(0,hourActual.indexOf("."));
		map.put("hourActual", hourActual.substring(0,hourActual.length()-3));
		list.add(map);
		return list;
	}

	@Override
	@Transactional
	public void deleteCourseFeeBySerialNum(Map<String, Object> map) {
		kcManagerDao.deleteCourseFeeBySerialNum(map);
		kcManagerDao.deleteCourseFeeApproveBySerialNum(map);
		kcManagerDao.deleteCourseFeeBackBySerialNum(map);
		kcManagerDao.deleteFeeDeduct(map);
	}

	@Override
	public List<Map<String, String>> existFee(Map map) {
		return kcManagerDao.existFee(map);
	}

    @Override
    public List<String> getSerialNumbers(Map<String, Object> params) {
        return kcManagerDao.getSerialNumbers(params);
    }

	@Override
	public List<Map<String, Object>> getQueryKCDetail(Map<String, Object> param) {
		List<Map<String, Object>> list = kcManagerDao.getQueryKCDetail(param);
		return list;
	}

	@Override
	public ResultData<List<Map<String, Object>>> classInformationPorjectData(Map<String, Object> map) throws BusinessLevelException {
		if(map.get("startTime") != null && !"".equals(map.get("startTime"))){
			map.put("startTime",map.get("startTime").toString() + "-00");
		}
		if(map.get("startTime") != null && !"".equals(map.get("endTime"))){
			map.put("endTime",map.get("endTime").toString() + "-32");
		}
		Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
		PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
		List<Map<String,Object>> list = null;
		try {
			list = kcManagerDao.classInformationPorjectData(map);
			//获取项目部个数
			List<String> depName = new ArrayList<String>();
			if(list.size()>0){
				for ( Map<String,Object> m : list ) {
					String name = m.get("depName").toString();
					if(!depName.contains(name)){
						depName.add(name);
					}
				}
				list.get(0).put("depNameNumber",depName.size());
			}
		} catch (RuntimeException e) {
			log.error("获取项目列表:" + e.getMessage());
			throw new BusinessLevelException("获取项目列表失败", e);
		}
		com.github.pagehelper.PageInfo page = new PageInfo(list);
		long totalNum = page.getTotal();
		return new ResultData<>((int) totalNum , list);
	}

	@Override
	public ResultData<List<Map<String, Object>>> classInformationPorjectDataNoPage(Map<String, Object> map) throws BusinessLevelException {
		List<Map<String,Object>> list = kcManagerDao.classInformationPorjectData(map);
		return new ResultData<>(list.size(),list);
	}

	@Override
	public ResultData<List<Map<String, Object>>> classInformationTeacherData(Map<String, Object> map) throws BusinessLevelException {
		if(map.get("startTime") != null && !"".equals(map.get("startTime"))){
			map.put("startTime",map.get("startTime").toString() + "-00");
		}
		if(map.get("startTime") != null && !"".equals(map.get("endTime"))){
			map.put("endTime",map.get("endTime").toString() + "-32");
		}
		Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
		PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
		List<Map<String,Object>> list = null;
		try {
			list = kcManagerDao.classInformationTeacherData(map);
			List<String> projectNames = new ArrayList<String>();
			List<String> teacherNames = new ArrayList<String>();
			if(list.size()>0){
				for ( Map<String,Object> m : list ) {
					String projectName = m.get("projectName").toString();
					if(!projectNames.contains(projectName)){
						projectNames.add(projectName);
					}
					String teacherName = m.get("teacherName").toString();
					if(!teacherNames.contains(teacherName)){
						teacherNames.add(teacherName);
					}
				}
				list.get(0).put("projectNames",projectNames.size());
				list.get(0).put("teacherNames",teacherNames.size());
			}
		} catch (RuntimeException e) {
			log.error("获取老师列表:" + e.getMessage());
			throw new BusinessLevelException("获取老师列表失败", e);
		}
		com.github.pagehelper.PageInfo page = new PageInfo(list);
		long totalNum = page.getTotal();
		return new ResultData<>((int) totalNum , list);
	}

	@Override
	public ResultData<List<Map<String, Object>>> classInformationTeacherDataNoPage(Map<String, Object> map) throws BusinessLevelException {
		List<Map<String,Object>> list = kcManagerDao.classInformationTeacherData(map);
		return new ResultData<>(list.size(),list);
	}

	@Override
	public List<List<Map<String,Object>>> getResearchKc(Map<String, Object> params) {
		if (params.containsKey("startTime") && !"".equals(params.get("startTime"))){
			String startTime = params.get("startTime").toString();
			params.put("startTime",startTime + "-01");
			int startYear = Integer.parseInt(startTime.split("-")[0]);
			params.put("startYear",startYear);
			int startMonth = Integer.parseInt(startTime.split("-")[1]);
			params.put("startMonth",startMonth);
		}
		if (params.containsKey("endTime") && !"".equals(params.get("endTime"))){
			String endTime = params.get("endTime").toString();
			params.put("endTime",endTime + "-31");
			int endYear = Integer.parseInt(endTime.split("-")[0]);
			params.put("endYear",endYear);
			int endMonth = Integer.parseInt(endTime.split("-")[1]);
			params.put("endMonth",endMonth);
		}

		List<Map<String,Object>> kcDetailsMap =  kcManagerDao.getResearchKc(params);
		List<List<Map<String,Object>>> result = new ArrayList<>();
		if(kcDetailsMap != null && kcDetailsMap.size() > 0){
			Map<String,List<Map<String,Object>>> teacherGroup = kcDetailsMap.stream().collect(Collectors.groupingBy(j -> j.get("teacherCode").toString()));
			Iterator<Map.Entry<String, List<Map<String,Object>>>> teacherGroupIterator = teacherGroup.entrySet().iterator();
			int num = 1;
			while(teacherGroupIterator.hasNext()) {
				List<Map<String,Object>> v = teacherGroupIterator.next().getValue();
				String ksCount="0";
				String kcCount="0.00";
				String projectSub="0.00";
				Map<String,Object> subTotal = new HashMap<>();
				for(int i=0;i<v.size();i++) {
					Map<String,Object> one = v.get(i);
					String deductType = one.get("deductType").toString();
					one.put("px", num);
					if(subTotal.isEmpty()) {
						subTotal.put("px", "小计");
					}
					if("0".equals(deductType)) {
						ksCount = Amount.add(ksCount, one.get("hourActual").toString());
						kcCount = Amount.add(kcCount, one.get("feeCourse").toString());
					}else {
						if("1901".equals(deductType)) {
							ksCount = Amount.sub(ksCount, one.get("hourActual").toString());
							kcCount = Amount.sub(kcCount, one.get("feeCourse").toString());
						}
					}
					num++;
				}
				ksCount = ksCount.substring(0,ksCount.indexOf("."));
				subTotal.put("hourActual", ksCount);
				subTotal.put("feeCourse", kcCount);
				subTotal.put("projectSub", projectSub);
				v.add(subTotal);
				result.add(v);
			}

			String kcCountTotal = "0.00";
			String ksCountTotal = "0";
			Map<String,Object> hjMap = new HashMap<String,Object>();
			for(int i=0;i<result.size();i++) {
				List<Map<String,Object>> list = (List<Map<String, Object>>) result.get(i);
				for(int j=0;j<list.size();j++) {
					Map<String,Object> map = list.get(j);
					if(map.get("px").equals("小计")) {
						kcCountTotal = Amount.add(kcCountTotal, map.get("feeCourse").toString());
						ksCountTotal = Amount.add(ksCountTotal,map.get("hourActual").toString());
					}
				}
			}
			hjMap.put("px", "合计");
			ksCountTotal = ksCountTotal.substring(0,ksCountTotal.indexOf("."));
			hjMap.put("feeCourse", kcCountTotal);
			hjMap.put("hourActual", ksCountTotal);
			List<Map<String,Object>> hjList = new ArrayList<>();
			hjList.add(hjMap);
			result.add(hjList);
		}

		return result;

	}

}
