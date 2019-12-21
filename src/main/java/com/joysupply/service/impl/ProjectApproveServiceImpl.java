package com.joysupply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.MessageDao;
import com.joysupply.dao.ProjectApproveDao;
import com.joysupply.dao.ProjectDao;
import com.joysupply.dao.ProjectSchedulePlanDao;
import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.Project;
import com.joysupply.service.MessageService;
import com.joysupply.service.ProjectApproveService;
import com.joysupply.util.Amount;
import com.joysupply.util.Constants;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;
import com.joysupply.util.PageHelperUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.internal.util.logging.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectApproveServiceImpl extends BaseService implements ProjectApproveService{

	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ProjectApproveDao projectApproveDao;
	@Autowired 
	private ProjectDao ProjectDao;
	@Autowired
	private ProjectSchedulePlanDao projectSchedulePlanDao;
	@Autowired
	MessageService messageService;
	@Autowired
	MessageDao messageDao;


	@Override
	public Map<String, Object> getKcList(Map<String, Object> params) {
        //获取未审批课酬数据
        List<Map<String, Object>> list = projectApproveDao.getKcList(params);
        List<Map<String, Object>> listResult = new ArrayList<>();
        Map<String,List<Map<String,Object>>> serialNumberGroup = list.stream()
        			.collect(Collectors.groupingBy(j -> j.get("serialNumber").toString()));
        serialNumberGroup.forEach((k,v)->{
        	String ksCount = "0";
        	String kcCount = "0.00";
        	Map<String,Object> map = new HashMap<>();
        	for(int i=0;i<v.size();i++) {
        		Map<String,Object> v1 = v.get(i);
        		if(map.isEmpty()) {
        			map.put("approveOrder", v1.get("approveOrder"));
        			map.put("depType", v1.get("depType"));
        			map.put("serialNumber", v1.get("serialNumber"));
        			map.put("year", v1.get("year"));
        			map.put("month", v1.get("month"));
            		String startTime = v1.get("startTime").toString().replace("-","/");
                	String endTime = v1.get("endTime").toString().replace("-","/");
                	map.put("timeSolt", startTime+"-"+endTime);
            		map.put("createTime", v1.get("createTime"));
            		map.put("status", v1.get("status"));
        		}
        		if(v1.get("projectId").equals("1901")) {
        			ksCount = Amount.sub(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.sub(kcCount, v1.get("feeCourse").toString());
        		}else {
        			ksCount = Amount.add(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.add(kcCount, v1.get("feeCourse").toString());
        		}
        	}
        	ksCount = ksCount.substring(0,ksCount.indexOf("."));
        	map.put("hourActual", ksCount);
        	map.put("feeCourse", kcCount);
        	listResult.add(map);
        });
      /*  PageInfo page = new PageInfo(listResult);
        long totalNum = page.getTotal();*/
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("count", listResult.size());
        map.put("data", listResult);
        map.put("code", 0);
        map.put("msg", "");
        return map;
	}
	@Override
	public Map<String, Object> kcSystemList(Map<String, Object> params) {
		
		//Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(params);
        //PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        
        //获取未审批课酬数据
        List<Map<String, Object>> list = projectApproveDao.kcSystemList(params);
        if(list.size()==0) {
        	Map<String,Object> map = new HashMap<String,Object>();
            map.put("count", list.size());
            map.put("data", list);
            map.put("code", 0);
            map.put("msg", "");
            return map;
        }
        //获取项目部名称
        List<Map<String, Object>> listResult = new ArrayList<>();
        Map<String,List<Map<String,Object>>> serialNumberGroup = list.stream()
        			.collect(Collectors.groupingBy(j -> j.get("serialNumber").toString()));
        serialNumberGroup.forEach((k,v)->{
        	String ksCount = "0";
        	String kcCount = "0.00";
        	Map<String,Object> map = new HashMap<>();
        	for(int i=0;i<v.size();i++) {
        		Map<String,Object> v1 = v.get(i);
        		if(map.isEmpty()) {
        			
        			map.put("approveOrder", v1.get("approveOrder"));
        			map.put("depType", v1.get("depType"));
        			map.put("serialNumber", v1.get("serialNumber"));
        			map.put("year", v1.get("year"));
        			map.put("month", v1.get("month"));
            		String startTime = v1.get("startTime").toString().replace("-","/");
                	String endTime = v1.get("endTime").toString().replace("-","/");
                	map.put("timeSolt", startTime+"-"+endTime);
            		map.put("createTime", v1.get("createTime"));
            		map.put("status", v1.get("status"));
            		map.put("memo", v1.get("memo"));
        		}
        		if(v1.get("projectId").equals("1901")) {
        			ksCount = Amount.sub(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.sub(kcCount, v1.get("feeCourse").toString());
        		}else {
        			ksCount = Amount.add(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.add(kcCount, v1.get("feeCourse").toString());
        		}
        		String projectId = v1.get("projectId").toString();
    			if(projectId.length() > 4){
    				 String depName = projectApproveDao.getDepNameByProjectId(projectId);
    				 map.put("depName", depName);
    			}
        	}
        	ksCount = ksCount.substring(0,ksCount.indexOf("."));
        	map.put("hourActual", ksCount);
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
	@Transactional
	public OpResult kcPass(Map<String, Object> params) {
		try {
			//验证是否是最后以一位审核
			int count = this.projectApproveDao.queryListCheckPerson(params);
			if(count==0) {
				this.projectApproveDao.projectApproveKc(params);
				this.projectApproveDao.feeDeductKc(params);
			}else {
				if("-1".equals(params.get("pass").toString())){
					this.projectApproveDao.projectApproveKc(params);
					this.projectApproveDao.feeDeductKc(params);
				}else{
					Map<String, Object> list = (Map<String, Object>)params.get("listArr");
					String serialNumber = list.get("serialNumber").toString();
					String personCode = projectApproveDao.getNextPersonCode(params);
					sendProjectMsg(personCode,serialNumber);
					this.projectApproveDao.projectApproveOrder(params);
				}
			}
			this.projectApproveDao.updateApproveStatus(params);
			//新增信息记录表
			this.projectApproveDao.insertApprove(params);
			if(params.get("pass").equals("-1")) {
				JSONObject jo = JSON.parseObject(params.get("listArr").toString());
				jo.put("rowId", Constants.GUID());
				params.put("listArr", jo);
				this.projectApproveDao.insertApproveDoc(params);
			}
		} catch (Exception e) {
			log.debug("审批失败："+e.getMessage());
			e.printStackTrace();
			return new OpResult("审批失败："+e.getMessage());
		}
		return new OpResult();
	}
	
	@Override
	@Transactional
	public OpResult cwkcApproval(Map<String, Object> params) {
		List<String> serialNumber = Arrays.asList(params.get("serialNumber").toString().split(","));
		List<String> projectIds = Arrays.asList(params.get("projectIds").toString().split(","));
		params.put("serialNumber",serialNumber);
		params.put("projectIds",projectIds);
		try {
			if(params.get("pass").equals("1")){
				//获取需要更行的课酬列表
				List<Map<String,Object>> daKc = this.projectApproveDao.getGrantKC(params);
				//更新课酬列表
				this.projectApproveDao.cwApproveKc(params);
				//更改教师工作量扣除
				this.projectApproveDao.cwFeeDeductKc(params);
				//更新 项目排课计划表
				this.projectSchedulePlanDao.upIsSend(daKc);
				
				//添加记录
				List<Map<String,Object>> pam = new ArrayList<>();
				for (String str : serialNumber ) {
					Map<String,Object> m = new HashMap<>();
					m.put("serialNumber",str);
					m.put("depType",params.get("depType").toString());
					m.put("personCode",params.get("personCode").toString());
					m.put("status",params.get("status").toString());
					m.put("approveOption",params.get("approveOption").toString());
					m.put("approveOrder",params.get("approveOrder").toString());
					m.put("id",params.get("id").toString());
					pam.add(m);
				}
				
				//新增信息记录表
				this.projectApproveDao.insertApproves(pam);
//				sendTeacherMsg(params);
			}
			if(params.get("pass").equals("-1")) {
				List<Map<String,Object>> p = this.projectApproveDao.objByMergeSerialNumber(params);
				if(p.size()>0){
					return new OpResult("该流水号已有教师发放课酬，不允许退回！");
				}else{
					//更新课酬列表
					this.projectApproveDao.cwApproveKcRetu(params);
					//更改教师工作量扣除
					this.projectApproveDao.cwFeeDeductKcRetu(params);
					//新增信息记录表
					List<Map<String,Object>> pam = new ArrayList<>();
					for (String str : serialNumber ) {
						Map<String,Object> m = new HashMap<>(5);
						m.put("serialNumber",str);
						m.put("depType",params.get("depType").toString());
						m.put("personCode",params.get("personCode").toString());
						m.put("status",params.get("status").toString());
						m.put("approveOption",params.get("approveOption").toString());
						m.put("approveOrder",params.get("approveOrder").toString());
						m.put("id",params.get("id").toString());
						pam.add(m);
					}
					this.projectApproveDao.insertApproves(pam);
					//日志
					List<Map<String,Object>> lpam = new ArrayList<Map<String,Object>>();
					for (String str : serialNumber) {
						Map<String,Object> map = new HashMap<>(5);
						map.put("rowId", Constants.GUID());
						map.put("id",params.get("id").toString());
						map.put("serialNumber",str);
						lpam.add(map);
					}
					this.projectApproveDao.insertApproveDocs(lpam);
				}
				return new OpResult();
			}
//			List<String> projectPay = Arrays.asList(params.get("projectIds").toString().split(","));
			//查询需要每个项目需要修改的数据
			List<Map<String,Object>> projectCKCSs = queryProjectKCKSs(projectIds);
			//修改项目的已发课酬已发课时projectId,payHour,payed
			for (Map<String,Object> proPay: projectCKCSs) {
				Project pro = new Project(){
					{
						setProjectId(proPay.get("projectId")+"");
						setPayHour(proPay.get("payHour")+"");
						setPayed(proPay.get("payed")+"");
					}
				};
				ProjectDao.updProjectKC(pro);
			}
		} catch (Exception e) {
			log.debug("审批失败："+e.getMessage());
			e.printStackTrace();
			return new OpResult("审批失败");
		}
		return new OpResult();
	}
	
	//查询课酬表已发课酬数据
	private List<Map<String,Object>> queryProjectKCKSs(List<String> list){
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for (String e: list ) {
			Map<String,Object> kc = projectApproveDao.getProjectKC(e);
			Map<String,Object> fc = projectApproveDao.getProjectFC(e);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("projectId",e);
			m.put("fcFeeCourse",kc==null?"0":kc.get("fcFeeCourse"));
			m.put("fcHourActual",kc==null?"0":kc.get("fcHourActual"));
			m.put("fdFeeCountCat",fc==null?"0":fc.get("fdFeeCountCat"));
			m.put("fdHourActualCat",fc==null?"0":fc.get("fdHourActualCat"));
			data.add(m);
		}
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

		for (Map<String,Object> e: data) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("projectId",e.get("projectId")+"");
			//计算课酬
			BigDecimal fcFeeCourse = BigDecimal.valueOf(0);
			BigDecimal fdFeeCountCat = BigDecimal.valueOf(0);
			
			if(e.get("fcFeeCourse")!=null){
				fcFeeCourse = new BigDecimal(e.get("fcFeeCourse")==null?"0":e.get("fcFeeCourse").toString());
			}
			if (e.get("fdFeeCountCat") != null){
				fdFeeCountCat = new BigDecimal(e.get("fdFeeCountCat")==null?"0":e.get("fdFeeCountCat").toString());
			} 
			BigDecimal payed = fcFeeCourse.subtract(fdFeeCountCat).setScale(2,BigDecimal.ROUND_HALF_UP);
			map.put("payed",payed);
			//计算课时
			BigDecimal fcHourActual = BigDecimal.valueOf(0);
			BigDecimal fdHourActualCat = BigDecimal.valueOf(0);
			if(e.get("fcHourActual")!=null){
				fcHourActual = new BigDecimal(e.get("fcHourActual")==null?"0":e.get("fcHourActual").toString());
			}
			/*if (e.get("fdHourActualCat")!=null){
				fdHourActualCat = new BigDecimal(e.get("fdHourActualCat")==null?"0":e.get("fdHourActualCat").toString());
			}*/
			BigDecimal payHour = fcHourActual.subtract(fdHourActualCat).setScale(0,BigDecimal.ROUND_HALF_UP);
			map.put("payHour",payHour);
			result.add(map);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> kcReadyList(Map<String, Object> params) {
        //获取已审批记录
        List<Map<String, Object>> list = projectApproveDao.kcReadyList(params);
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
        		map.put("apprvoeTime", v1.get("apprvoeTime"));
        		map.put("status", v1.get("status"));
        		map.put("year", v1.get("year"));
        		map.put("month", v1.get("month"));
        		if(v1.get("projectId").equals("1901")) {
        			ksCount = Amount.sub(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.sub(kcCount, v1.get("feeCourse").toString());
        		}else {
        			ksCount = Amount.add(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.add(kcCount, v1.get("feeCourse").toString());
        		}
        	}
        	map.put("hourActual", ksCount.substring(0,ksCount.indexOf(".")));
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
	public OpResult managerSave(Map<String, Object> params) {
		try {
			String[] serialNumberList = params.get("serialNumber").toString().split(",");
			params.put("serialNumber", serialNumberList);
			this.projectApproveDao.managerSave(params);
		} catch (Exception e) {
			log.debug("年月保存失败:"+e.getMessage());
			return new OpResult("年月保存失败："+e.getMessage());
		}
		return new OpResult();
	}
	
	public void sendProjectMsg(String personCode,String serialNumber){
		String name = messageService.getPersonNameByPersonCode(personCode);
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("personCode", personCode);
		msg.put("teacherName", name);
		msg.put("type", 2);
		msg.put("projectName", "流水号为"+serialNumber+"课酬");
		messageService.addMessage(msg);
	}
	
	public void sendTeacherMsg(Map<String, Object> map){
		String personCode = map.get("teacherCode").toString();
		String name = messageService.getTeacherNameByPersonCode(personCode);
		Map<String, Object> msg = new HashMap<String, Object>();
		String projectId = map.get("projectIds").toString();
		String[] ids = projectId.split(",");
		String kcName = "";
		for (String id : ids) {
			kcName = kcName + messageService.getProjectNameById(id) + ",";
		}
		msg.put("personCode", personCode);
		msg.put("name", name);
		msg.put("type", 3);
		msg.put("kcName", kcName.substring(0,kcName.length()-1));
		messageService.addMessage(msg);
	}
	@Override
	public Map<String, Object> systemKcReadyList(Map<String, Object> params) {
		//获取已审批记录
        List<Map<String, Object>> list = projectApproveDao.systemKcReadyList(params);
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
        		map.put("apprvoeTime", v1.get("apprvoeTime"));
        		map.put("status", v1.get("status"));
        		map.put("year", v1.get("year"));
        		map.put("month", v1.get("month"));
        		if(v1.get("projectId").equals("1901")) {
        			ksCount = Amount.sub(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.sub(kcCount, v1.get("feeCourse").toString());
        		}else {
        			ksCount = Amount.add(ksCount, v1.get("hourActual").toString());
        			kcCount = Amount.add(kcCount, v1.get("feeCourse").toString());
        		}
        	}
        	map.put("hourActual", ksCount.substring(0,ksCount.indexOf(".")));
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
	public void sendMsg(Map<String, Object> params) {
		String personIds = params.get("personIds").toString();
		List<String> ids = JSON.parseArray(personIds,String.class);
		List<Map<String, Object>> msgList = new ArrayList<Map<String,Object>>();
		String msgTemplate = messageDao.getContent("7");
		String data = params.get("data").toString();
		List<Map> list = JSON.parseArray(data,Map.class);
		String teacherName = "";
		for (Map<String, Object> map : list) {
			String year = map.get("year").toString();
			String month = map.get("month").toString();
			String fee = map.get("totalCount").toString();
			for(String id:ids) {
				Map<String, Object> m = new HashMap<>();
				String content = msgTemplate.replace("{year}", year).replace("{month}",month).replace("{fee}",fee);
				m.put("content", content);
				m.put("personCode", id);
				m.put("messageTime", DateUtil.ToDateTimeString());
				m.put("rowId", Constants.GUID());
				m.put("isReading", 0);
				m.put("type", "7");
				msgList.add(m);
			}
			/*String teacherCode = map.get("teacherCode").toString();
			teacherName = messageService.getTeacherNameByPersonCode(teacherCode);*/
		}
		/*for(String id:ids){
            Map<String, Object> map = new HashMap<>();
            String content = msgTemplate.replace("{teacherName}", teacherName);
            map.put("content", content);
            map.put("personCode", id);
            map.put("messageTime", DateUtil.ToDateTimeString());
            map.put("rowId", Constants.GUID());
            map.put("isReading", 0);
            map.put("type", "7");
            msgList.add(map);
        }*/
		messageDao.addMessages(msgList);
	}
}
