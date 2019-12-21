package com.joysupply.service.impl;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.joysupply.dao.AuthorityManageDao;
import com.joysupply.dao.UnSendTeacherKcDao;
import com.joysupply.util.ResultData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.poifs.storage.ListManagedBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.KcTeacherDao;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.KcTeacherService;
import com.joysupply.util.Amount;
import com.joysupply.util.PageHelperUtil;

import javax.jws.Oneway;

/**
 * 
 * @author Administrator
 *
 */
@Service("kcTeacherService")
public class KcTeacherServiceImpl extends BaseService implements KcTeacherService {
 	
	@Autowired
	private KcTeacherDao kcTeacherDao;
	@Autowired
	private UnSendTeacherKcDao unSendTeacherKcDao;
	private Log log=LogFactory.getLog(getClass());
	/**
	 * 已发教师类别实现层
	 */
	@Override
	public List getkcTeacher(Map<String, Object> params) {
		String time = params.get("time").toString();
		if(!"".equals(time)) {
			params.put("year", time.split("-")[0]);
			params.put("month", time.split("-")[1]);
		}
		int px = 1;
		List<Map<String,Object>> dataMap = this.kcTeacherDao.getKcTeacher(params);
		
		Map<String,List<Map<String,Object>>> teacherTypeGroup = dataMap.stream().collect(Collectors.groupingBy(j -> j.get("teacherType").toString()));
		Iterator<Map.Entry<String, List<Map<String,Object>>>> iterTeacherType = teacherTypeGroup.entrySet().iterator();
		//返回结果集
		List<List<Map<String,Object>>> result = new ArrayList<>();
		
		while(iterTeacherType.hasNext()) {
			List<Map<String,Object>> teacherValue = iterTeacherType.next().getValue();
			//教师类型list
			List<Map<String,Object>> teacherListGroup = new ArrayList<>();
			//项目课酬小计map
			Map<String,Object> projectList = new HashMap<>();
			
			Map<String,List<Map<String,Object>>> teacherNameGroup = teacherValue.stream().collect(Collectors.groupingBy(j -> j.get("teacherName").toString()));
			Iterator<Map.Entry<String, List<Map<String,Object>>>> iterTeacherName = teacherNameGroup.entrySet().iterator();
			
			while(iterTeacherName.hasNext()) {
				String totalCount="0.00";
				List<Map<String,Object>> nameValue = iterTeacherName.next().getValue();
				Map<String,Object> groupResult = nameValue.get(0);
				for(int i=0;i<nameValue.size();i++) {
					Map<String,Object> value1 = nameValue.get(i);
					String projectId = value1.get("projectId").toString();
					String feeCourse = value1.get("feeCourse").toString();
					groupResult.put(projectId, feeCourse);
					if(!projectList.containsKey(projectId)) {
						projectList.put(projectId,"0.00");
					}
					if(projectId.equals("1901")) {
						totalCount = Amount.sub(totalCount, feeCourse);
					}else {
						totalCount = Amount.add(totalCount, feeCourse);
					}
				}
				groupResult.put("totalCount", totalCount);
				groupResult.put("px", px);
				px++;
				teacherListGroup.add(groupResult);
			}
			Map<String,Object> xiaoji = new HashMap<>();
			String xiaojiTotalCount="0.00";
			for(int i=0;i<teacherListGroup.size();i++) {
				Map<String,Object> map = teacherListGroup.get(i);
				if(xiaoji.isEmpty()) {
					xiaoji.put("px", "小计");
					xiaoji.put("month", map.get("month"));
					xiaoji.put("year", map.get("year"));
					xiaoji.put("teacherType", map.get("teacherType"));
					xiaoji.put("totalCount", "0.00");
				}
				xiaojiTotalCount = Amount.add(xiaojiTotalCount, map.get("totalCount").toString());
				projectList.forEach((k,v)->{
					if(map.containsKey(k)) {
						projectList.put(k, Amount.add(v.toString(), map.get(k).toString()));
					}
				});
			}
			projectList.forEach((k,v)->{
				xiaoji.put(k, v);
			});
			xiaoji.put("totalCount", xiaojiTotalCount);
			teacherListGroup.add(xiaoji);
			result.add(teacherListGroup);
		}
		List<Map<String,Object>> heji = new ArrayList<>();
		Map<String,Object> hejiMap = new HashMap<>();
		String hjTotalCount = "0.00";
		hejiMap.put("px","合计");
		for(int i=0;i<result.size();i++) {
			List<Map<String,Object>> value = result.get(i);
			for(int j=0;j<value.size();j++) {
				Map<String,Object> map = value.get(j);
				if(map.get("px").equals("小计")) {
					hjTotalCount = Amount.add(hjTotalCount, map.get("totalCount").toString());
				}
			}
		}
		hejiMap.put("totalCount", hjTotalCount);
		heji.add(hejiMap);
		result.add(heji);
		return result;
	}
	
	/**
	 * 获取项目名称
	 */
	@Override
	public List<Map<String, Object>> getProductName(String personCode) {
		
		return kcTeacherDao.getProductName(personCode);
	}

	
	/**
	 * 教师类别课酬实现层
	 */
	@Override
	public List<Map<String, Object>> getKclbTeacher(String teacherType, String year, String month) {
		
		List<Map<String, Object>> kcLb= kcTeacherDao.getKclbTeacher(teacherType,year,month);
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, List<Map<String, Object>>> xmfz = null;
		// 按照教师类型
		int num =1 ;
		Map<String, List<Map<String, Object>>> jslx = kcLb.stream().collect(Collectors.groupingBy(m -> m.get("teacherType").toString()));
		//按照项目
		Iterator<Map.Entry<String, List<Map<String,Object>>>> iterProject = jslx.entrySet().iterator();
		while(iterProject.hasNext()) {
			Map.Entry<String, List<Map<String,Object>>> entryTeacher = iterProject.next();
			List<Map<String,Object>> valueTeacher = entryTeacher.getValue();
			xmfz = valueTeacher.stream().collect(Collectors.groupingBy(m -> m.get("project_name").toString()));
		}
		Iterator<Map.Entry<String, List<Map<String,Object>>>> iterTableMap = xmfz.entrySet().iterator();
		while(iterTableMap.hasNext()) {
			Map.Entry<String, List<Map<String,Object>>> entryTeacher = iterTableMap.next();
			List<Map<String,Object>> valueTeacher = entryTeacher.getValue();
			for(int i=0;i<valueTeacher.size();i++) {
				Map<String,Object> map = valueTeacher.get(i);
				result.add(map); 
			}
		} 
		return result;
	}
	
	/**
	 * 部门维度课酬
	 */
	/*@Override
	public List<Map<String, Object>> getVdoingInfo(Map<String, Object> map){
		if(map.containsKey("startTime") && map.get("startTime") != null && !"".equals(map.get("startTime").toString()) ){
			String startTime = map.get("startTime").toString();
			map.put("startTime",startTime+"-01");
			map.put("startYear",startTime.split("-")[0]);
			int startMonth = Integer.parseInt(startTime.split("-")[1]);
			map.put("startMonth",startMonth);
		}
		if(map.containsKey("endTime") && map.get("endTime") != null && !"".equals(map.get("endTime").toString())){
			String endTime = map.get("endTime").toString();
			map.put("endTime",endTime+"-31");
			map.put("endYear",endTime.split("-")[0]);
			int endMonth = Integer.parseInt(endTime.split("-")[1]);
			map.put("endMonth",endMonth);
		}
	 	List<Map<String,Object>> list = kcTeacherDao.getVdoingInfo(map);
	 	if(!list.isEmpty()){
	 		for (Map<String, Object> map2 : list) {
				if(map2.get("payHour") == null || "".equals(map2.get("payHour").toString())){
					map2.put("payHour", "0");
				}
				if(map2.get("countHour") == null || "".equals(map2.get("countHour").toString())){
					map2.put("countHour", "0");
				}
				if(map2.get("payed") == null || "".equals(map2.get("payed").toString())){
					map2.put("payed", "0.00");
				}
				if(map2.get("totalFee") == null || "".equals(map2.get("totalFee").toString())){
					map2.put("totalFee", "0.00");
				}
				if(map2.containsKey("deductKs")){
					String deductKs = map2.get("deductKs").toString();
					String payHour1 = map2.get("payHour").toString();
					map2.put("payHour",Amount.sub(payHour1,deductKs));
				}
				DecimalFormat df = new DecimalFormat("0.00");
				DecimalFormat df1 = new DecimalFormat("0");
				double payed = Double.valueOf(map2.get("payed").toString());
				double payHour = Double.valueOf(map2.get("payHour").toString());
				String payed2 = df.format(payed);
				String payHour2 = df1.format(payHour);
				map2.put("payed", payed2);
				map2.put("payHour", payHour2);
				double totalFee =  Double.valueOf(map2.get("totalFee").toString());
				String totalFee2 = df.format(totalFee);
				map2.put("totalFee", totalFee2);
				double countHour =  Double.valueOf(map2.get("countHour").toString());
				String countHour2 = df1.format(countHour);
				map2.put("countHour", countHour2);
				if(map2.containsKey("deductFee")){
					String deductFee = map2.get("deductFee").toString();
					String payed1 = map2.get("payed").toString();
					map2.put("payed",Amount.sub(payed1,deductFee));
				}
			}
	 	}
	 	
		return list;
	}*/

	/**
	 * 部门维度课酬
	 */
	@Override
	public List<Map<String, Object>> getVdoingInfo(Map<String, Object> map){
		if(map.containsKey("startTime") && map.get("startTime") != null && !"".equals(map.get("startTime").toString()) ){
			String startTime = map.get("startTime").toString();
			map.put("startTime",startTime+"-01");
			map.put("startYear",startTime.split("-")[0]);
			int startMonth = Integer.parseInt(startTime.split("-")[1]);
			map.put("startMonth",startMonth);
		}
		if(map.containsKey("endTime") && map.get("endTime") != null && !"".equals(map.get("endTime").toString())){
			String endTime = map.get("endTime").toString();
			map.put("endTime",endTime+"-31");
			map.put("endYear",endTime.split("-")[0]);
			int endMonth = Integer.parseInt(endTime.split("-")[1]);
			map.put("endMonth",endMonth);
		}
		List<Map<String,Object>> list = kcTeacherDao.getVdoingInfo(map);
		List<Map<String, Object>> alreadyKc = kcTeacherDao.getAlreadyKc(map);
		List<Map<String,Object>> result = new ArrayList<>();
		DecimalFormat df1 = new DecimalFormat("0");
		if(!alreadyKc.isEmpty()){
			Map<String, List<Map<String, Object>>> dep_name = alreadyKc.stream().collect(Collectors.groupingBy(m -> m.get("dep_name").toString()));
			Iterator<Map.Entry<String, List<Map<String, Object>>>> iterator = dep_name.entrySet().iterator();
			while (iterator.hasNext()){
				String hourActual = "0.00";
				String feeCourse = "0.00";
				Map.Entry<String, List<Map<String, Object>>> next = iterator.next();
				List<Map<String, Object>> value = next.getValue();
				Map<String, Object> map2 = new HashMap<>();
				for (Map<String, Object> m : value) {
					String deductType = m.get("deductType").toString();
					if("0".equals(deductType)){
						hourActual = Amount.add(hourActual,m.get("hourActual").toString());
						feeCourse = Amount.add(feeCourse,m.get("feeCourse").toString());
					}else{
						hourActual = Amount.sub(hourActual,m.get("hourActual").toString());
						feeCourse = Amount.sub(feeCourse,m.get("feeCourse").toString());
					}
				}
				double v = Double.parseDouble(hourActual);
				map2.put("hourActual",df1.format(v));
				map2.put("feeCourse",feeCourse);
				map2.put("depName",next.getKey());
				result.add(map2);
			}
		}
		for (int i = 0;i < list.size();i++){
			Map<String, Object> listMap = list.get(i);
			for (int j = 0;j < result.size();j++){
				Map<String, Object> resultMap = result.get(j);
				String organizeCode = listMap.get("organizeCode").toString();
				String depName = resultMap.get("depName").toString();
				if(organizeCode.equals(depName)){
					listMap.put("hourActual",resultMap.get("hourActual"));
					listMap.put("feeCourse",resultMap.get("feeCourse"));
				}
			}
			if(!listMap.containsKey("countHour") || listMap.get("countHour") == null || "".equals(listMap.get("countHour").toString())){
				listMap.put("countHour","0");
			}
			if(!listMap.containsKey("totalFee") || listMap.get("totalFee") == null || "".equals(listMap.get("totalFee").toString())){
				listMap.put("totalFee","0.00");
			}else{
                String totalFee = listMap.get("totalFee").toString();
                String add = Amount.add(totalFee, "0");
                listMap.put("totalFee",totalFee);
            }
			if(!listMap.containsKey("hourActual") || listMap.get("hourActual") == null || "".equals(listMap.get("hourActual").toString())){
				listMap.put("hourActual","0");
			}
			if(!listMap.containsKey("feeCourse") || listMap.get("feeCourse") == null || "".equals(listMap.get("feeCourse").toString())){
				listMap.put("feeCourse","0.00");
			}
		}
		return list;
	}

	/*@Override
	public List getDepartDetail(Map<String, String> params) {
		List<Map<String,Object>> kcDetailsMap = this.kcTeacherDao.getDepartDetail(params);
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
		ksCountTotal = ksCountTotal.substring(0,ksCountTotal.indexOf("."));
		hjMap.put("feeCourse", kcCountTotal);
		hjMap.put("hourActual", ksCountTotal);
		List<Map<String,Object>> hjList = new ArrayList<>();
		hjList.add(hjMap);
		result.add(hjList);
		return result;
	}*/
	@Override
	public List getDepartDetail(Map<String, Object> params) {
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
		String type = params.get("type").toString();
		List<Map<String,Object>> kcDetailsMap = null;
		if(type.equals("0")){
			kcDetailsMap = this.kcTeacherDao.getDepartDetail(params);
		}else {
			kcDetailsMap = kcTeacherDao.getShouleDepartDetail(params);
		}
		List<List<Map<String,Object>>> result = new ArrayList<>();
		if(kcDetailsMap != null && kcDetailsMap.size() > 0){
			Map<String,List<Map<String,Object>>> teacherGroup = kcDetailsMap.stream().collect(Collectors.groupingBy(j -> j.get("teacherCode").toString()));
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
					String deductType = one.get("deductType").toString();
					one.put("px", num);
					if(subTotal.isEmpty()) {
						subTotal.put("px", "小计");
					}
					//ksCount = Amount.add(ksCount, one.get("hourActual").toString());
					//kcCount = Amount.add(kcCount, one.get("feeCourse").toString());
					
					/*if("1901".equals(one.get("projectId"))) {
						projectSub = Amount.add(projectSub, one.get("feeCourse").toString());
						ksCount = Amount.sub(ksCount, one.get("hourActual").toString());
						kcCount = Amount.sub(kcCount, one.get("feeCourse").toString());
					}else {
						ksCount = Amount.add(ksCount, one.get("hourActual").toString());
						kcCount = Amount.add(kcCount, one.get("feeCourse").toString());
					}*/
					if("0".equals(deductType)) {
						ksCount = Amount.add(ksCount, one.get("hourActual").toString());
						kcCount = Amount.add(kcCount, one.get("feeCourse").toString());
					}else {
						if("1901".equals(deductType)) {
							ksCount = Amount.sub(ksCount, one.get("hourActual").toString());
							kcCount = Amount.sub(kcCount, one.get("feeCourse").toString());
						}
					}
					if(one.containsKey("startTime")){
						one.put("timeSolt", one.get("startTime").toString().replace("-", "/")+"-"+one.get("endTime").toString().replace("-", "/"));
						one.put("projectSub", "");
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
						if(hjMap.isEmpty()) {
							hjMap.put("timeSolt", map.get("timeSolt"));
						}
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

	@Override
	public ResultData<List<String>> getYearMonth() throws BusinessLevelException {
		try {
			List<String> data = retParamName(kcTeacherDao.getYearMonth());
			return new ResultData<>(data.size(),data);
		}catch (RuntimeException e){
			throw new BusinessLevelException("获取已发课酬表头",e);
		}
	}

	@Override
	public List<ResultData> selRemTeacher(Map<String, Object> map) throws BusinessLevelException {
		try {
			Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
			PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
			List<Map<String,Object>> list = kcTeacherDao.selRemTeacher(map);
			com.github.pagehelper.PageInfo page = new com.github.pagehelper.PageInfo(list);
			long totalNum = page.getTotal();
			if(list.size() == 0 ){
				return new ArrayList<ResultData>(){
					{
						add(new ResultData<>(0,null));
						add(new ResultData<>(0,null));
					}
				};
			}
			List<Map<String, Object>> data = getTeaserData(list); 
			List<Map<String, Object>> head = getHeadData2(list);
			PageHelper.clearPage();
			return new ArrayList<ResultData>(){
				{
					add(new ResultData<List<Map<String,Object>>>((int) totalNum,data));
					add(new ResultData<List<Map<String,Object>>>(head.size(),head));
				}
			};
		}catch (RuntimeException e){
			e.printStackTrace();
			throw new BusinessLevelException("获取已发课酬数据",e);
		}
	}

	@Override
	public Map<String, Object> selRemTeacherNoPage(Map<String, Object> map) throws BusinessLevelException {
		Map<String,Object> result = new HashMap<String,Object>(3);
		List<Map<String,Object>> list = kcTeacherDao.selRemTeacher(map);
		result.put("data",getTeaserData(list));
		result.put("head",getHeadData(list));
		return result;
	}

	public List<Map<String,Object>> getTeaserData(List<Map<String,Object>> list){
	    List<Map<String,Object>> head = getHeadData(list);
	    if(head.size()<1){
	        return null;
        }
        //遍历数据
        for (int i = 0 ; i < list.size() ; i ++ ){
            Map<String,Object> map = list.get(i);
			List<String> deplist = new ArrayList<String>();
            if(map != null && !map.isEmpty()){
            	//遍历头部表头
                for (Map<String,Object> map1: head) {
                    double deduction = 0d; //工作量扣除
                    double lecture = 0d; //讲座
                    double replacement = 0d; // 补发
                    if(map1 != null && !map.isEmpty()){
                    	if(map.get("information") != null){
							List<String> list1 = Arrays.asList(map.get("information").toString().split(","));
							List<String> depl = Arrays.asList(map.get("depCodes").toString().split(","));
							if(list1.size()>0){
								boolean flg = false;
								for (int k = 0 ; k < list1.size() ; k ++ ) {
									String str1 = list1.get(k).toString();
									if(str1!=null && !str1.equals("")){
										List<String> list2 = Arrays.asList(str1.split("--"));
										if(list2.size()>0){
											if(map1.get("pName").toString().equals(list2.get(1)) && !list2.get(1).equals("x")
													&& !list2.get(1).equals("y") && !list2.get(1).equals("z")){
												double m = 0d;
												if(map.get(map1.get("name")+"")!=null){
													m = Double.valueOf(map.get(map1.get("name")+"").toString());
												}else if(depl.size()>k){
													flg = true;
													deplist.add(depl.get(k));
												}
												map.put(map1.get("name")+"",Double.valueOf(list2.get(0).toString())+m);
											}else if (list2.get(1).equals("x")){
												deduction += Double.valueOf(list2.get(0)+"");
											}else if (list2.get(1).equals("y")){
												lecture += Double.valueOf(list2.get(0)+"");
											}else if (list2.get(1).equals("z")){
												replacement += Double.valueOf(list2.get(0)+"");
											}
										}
									}
								}
								if(!flg){
									deplist.add(map1.get("dep").toString());
								}
							}
						}
                    }
                    map.put("deduction",deduction);
					map.put("lecture",lecture);
					map.put("replacement",replacement);
                }
            }
            List<String> values = new ArrayList<>();
			for (Map<String,Object> projectName: head) {
				String value = map.get(projectName.get("name"))+"";
				values.add(value);
			}
			map.put("valName",values);
			
			//部门维度计算
			List<Map<String,Object>> deps = getDepByProjectIds(head);
			List<String> val = new ArrayList<>();
			for (int j = 0 ; j < deps.size() ; j ++){
				Map<String,Object> d = deps.get(j);
				String sum = "0";
                for (int n = 0 ; n < deplist.size() ; n ++) {
                    String dep = deplist.get(n);
                    if(d.get("depCode").toString().equals(dep)){
                        if(values.get(n)!= null && !values.get(n).equals("null")){
                            sum = Amount.add(sum, values.get(n));
                        }
                    }
                }
				val.add(sum);
			}
			map.put("depVal",val);
		}
		return list;
	}
	
	public List<Map<String,Object>> getDepByProjectIds(List<Map<String,Object>> list){
		return unSendTeacherKcDao.getDepList(list);
	}
	public List<Map<String, Object>> getHeadData2(List<Map<String,Object>> list){
		List<Map<String,Object>> head = getHeadData(list);
		return getDepByProjectIds(head);
	}
	public List<Map<String, Object>> getHeadData(List<Map<String,Object>> list){
        List<String> da = new ArrayList<>();
        List<String> deps = new ArrayList<>();
        List<String> projectIds = new ArrayList<>();
        for (Map<String,Object> map:list) {
        	if(map.get("projectName")!=null){
				List<String> li = Arrays.asList(map.get("projectName").toString().split(","));
				List<String> dep = Arrays.asList(map.get("depCodes").toString().split(","));
				List<String> projectId = Arrays.asList(map.get("projectId").toString().split(","));
				for (int i = 0 ; i < li.size() ; i ++ ){
					String str = li.get(i);
					if(!da.contains(str)){
						da.add(str);
						deps.add(dep.get(i));
						projectIds.add(projectId.get(i));
					}
				}
			}
		}
		if(da.size() < 1){
		    return null;
        }
        List<String> l = da;
        List<Map<String,Object>> dataList = new ArrayList<>();
		for (int i = 0 ; i < l.size() ; i ++ ){
		    if(l.get(i) != null && !l.get(i).equals("")){
		        Map<String,Object> map = new HashMap<>();
		        map.put("name","projectName"+i);
		        map.put("pName",l.get(i));
		        map.put("dep",deps.get(i));
		        map.put("projectId",projectIds.get(i));
		        dataList.add(map);
            }
        }
		return dataList;
	}
	
	public List<String> retParamName (List<String> data){
		List<String> listName = new ArrayList<String>();
		Function<List<String>,List<String>> list = (x) -> {
			for (String str:x) {
				List<String> lis = Arrays.asList(str.split(","));
				for (String str1:lis) {
					if(lis.size()>0){
						listName.add(str1);
						break;
					}
				}
			}
			return  listName;
		};
		return list.apply(data);
	}

	@Override
	public long getVdoingCount(Map<String, Object> param) {
		
		return kcTeacherDao.getVdoingCount(param);
	}

}