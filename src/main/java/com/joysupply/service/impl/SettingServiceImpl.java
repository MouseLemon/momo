package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.joysupply.dao.SettingDao;
import com.joysupply.entity.TimeSheet;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.SettingService;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;
import com.joysupply.util.PageHelperUtil;
import com.joysupply.util.ResultData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Dreamer
 * @Description 时间设置ServiceImpl
 * @Date 上午 10:45 2018/12/15 0015
 * @Param 
 * @return 
 **/
@Service("settingService")
public class SettingServiceImpl extends BaseService implements SettingService {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	SettingDao settingDao;

	/**
	 * 查询时间表
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> timeTable(Map<String, Object> map) {
		Map<String,Object> result = new HashMap<String,Object>(){
			{
				put("message","获取失败");
				put("state",false);
			}
		};
		// 设置日期格式
		SimpleDateFormat now = new SimpleDateFormat("yyyy");
		map.put("year", now.format(new Date()));
		int count = settingDao.getTimeTableCount(map);
		List<Map<String,Object>> data = settingDao.getTimeTableInfo(map);
		result.put("code", 0);
		result.put("data", data);
		result.put("count",count);
		result.put("message", "获取成功！");
		result.put("state", true);
		return result;
	}

	/**
	 * 修改所有，点击《保存》按钮
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public OpResult saveTimeTable(Map<String, Object> map) throws BusinessLevelException {
		try{
			// 设置日期格式
			SimpleDateFormat now = new SimpleDateFormat("yyyy");
			map.put("year", now.format(new Date()));
			// 获取条数
			int count = settingDao.getTimeTableCount(map);
			List list = new ArrayList();
			for (int i = 0; i < count; i++){
				Map mmaapp = new HashMap();
				// map中每个section
				String eachSection = "data[" + i + "][section]";
				// map中每个start_time
				String eachStartTime = "data[" + i + "][start_time]";
				// map中每个end_time
				String eachEndTime = "data[" + i + "][end_time]";

				String sectionResult = map.get(eachSection).toString();
				String start_timeResult = map.get(eachStartTime).toString();
				String end_timeResult = map.get(eachEndTime).toString();

				mmaapp.put("section", sectionResult);
				mmaapp.put("start_time", start_timeResult);
				mmaapp.put("end_time", end_timeResult);
				list.add(mmaapp);
			}
			Map newMap = new HashMap();
			newMap.put("list",list);
			settingDao.saveTimeTable(newMap);
			return new OpResult();
		}catch (RuntimeException ex){
			log.error("修改时间表" + ex.getMessage());
			throw new BusinessLevelException("修改时间表",ex);
		}
	}

	/**
	 * 编辑单个时间段
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public OpResult editTime(Map<String, Object> map) throws BusinessLevelException {
		String start_time = map.get("start_time").toString();
		String end_time = map.get("end_time").toString();
		String beginStartTime = map.get("beginStartTime").toString();
		String beginEndTime = map.get("beginEndTime").toString();

		// 查询上一条数据（根据传入的beginStartTime查），上一条数据的结束时间就是  开始时间
		Map bMap = settingDao.selectBeginStartTime(beginStartTime);
		if (bMap != null){
			// 最大开始时间
			int maxStartTime = Integer.parseInt(bMap.get("end_time").toString().replace(":", ""));
			// 修改的开始时间
			int primaryStartTime = Integer.parseInt(start_time.replace(":", ""));
			if (primaryStartTime < maxStartTime) {
				return new OpResult("cuole");
			}
		}

		// 查询下一条数据（根据传入的beginEndTime查），下一条数据的开始时间 就是  结束时间
		Map cMap = settingDao.selectBeginEndTime(beginEndTime);
		if (cMap != null){
			int maxEndTime = Integer.parseInt(cMap.get("start_time").toString().replace(":", ""));
			int primaryEndTime = Integer.parseInt(end_time.replace(":", ""));
			if (primaryEndTime > maxEndTime){
				return new OpResult("cuole");
			}
		}

		// ---------------------------------------------------------
		String e1 = end_time.substring(0,2);
		String e2 = end_time.substring(3,5);
		int et1 = Integer.parseInt(e1);

		if (et1 <= 12) {
			map.put("step", "1");
		} else if(et1 > 12 && et1 <= 17 && "00".equals(e2)){
			map.put("step", "2");
		} else if(et1 > 17){
			map.put("step", "3");
		}

		List listMap = new ArrayList();
		for (int i = 2015; i < 2026; i++){
			Map eMap = new HashMap();
			eMap.put("step", map.get("step").toString());
			eMap.put("start_time", map.get("start_time").toString());
			eMap.put("end_time", map.get("end_time").toString());
			eMap.put("year", i);
			eMap.put("section", map.get("section").toString());
			listMap.add(eMap);
		}

		Map param = new HashMap();
		param.put("list", listMap);
		param.put("sectionName", map.get("sectionName").toString());

		try{
			settingDao.editTime(param);
			return new OpResult();
		}catch (RuntimeException e){
			log.error("修改课表时间:" + e.getMessage());
			throw new BusinessLevelException("修改课表时间", e);
		}
	}

	/**
	 * 添加课表时间
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public OpResult addTime(Map<String, Object> map) throws BusinessLevelException{

		String start_time = map.get("start_time").toString();
		String end_time = map.get("end_time").toString();

		// 此时获取end_time，这个时间就是 当前设置开始时间的上限
		Map startMap = settingDao.selectStart(start_time);
		// 如果为空，则开始时间无上限
		if (startMap != null) {
			return new OpResult("开始时间不在空闲时间范围");
		}

		// 此时获取start_time，这个时间就是 当前设置结束时间的上限
		Map endMap = settingDao.selectEnd(end_time);
		// 如果为空，则结束时间无下限
		if (endMap != null){
			return new OpResult("结束时间不在空闲范围");
		}

		Map nextMap = settingDao.selectStartNext(start_time);
		String st = nextMap.get("start_time").toString().replace(":", "");
		String et = end_time.replace(":", "");
		int start = Integer.parseInt(st);
		int end = Integer.parseInt(et);
		if (end > start){
			return new OpResult("时间段不符合");
		}

		String e1 = end_time.substring(0,2);
		String e2 = end_time.substring(3,5);
		int et1 = Integer.parseInt(e1);

		if (et1 <= 12){
			map.put("step", "1");
		} else if(et1 > 12 && et1 <= 17 && "00".equals(e2)){
			map.put("step", "2");
		} else if(et1 > 17){
			map.put("step", "3");
		}

		List listMap = new ArrayList();
		for (int i = 2015; i < 2026; i++){
			Map eMap = new HashMap();
			eMap.put("step", map.get("step").toString());
			eMap.put("start_time", map.get("start_time").toString());
			eMap.put("end_time", map.get("end_time").toString());
			eMap.put("year", i);
			eMap.put("section", map.get("section").toString());
			listMap.add(eMap);
		}

		try{
			settingDao.addTime(listMap);
			return new OpResult();
		}catch (RuntimeException e){
			log.error("添加课表时间:" + e.getMessage());
			throw new BusinessLevelException("添加课表时间", e);
		}
	}

    /**
     * 删除时间段
     * @param timeSheet
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult deleteTime(TimeSheet timeSheet) throws BusinessLevelException {
        try {
            settingDao.deleteTime(timeSheet);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("删除时间:" + e.getMessage());
            throw new BusinessLevelException("删除时间", e);
        }
    }

	/**
	 * 获取所有时间
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public List<TimeSheet> getTimeSheet(String year) throws BusinessLevelException {
		try {
			return settingDao.getTimeSheet(year);
		} catch (RuntimeException e) {
			log.error("获取所有时间:" + e.getMessage());
			throw new BusinessLevelException("获取所有时间", e);
		}
	}

	@Override
	public Map<String,Object> queryTabelAllHead(){
		try{
			Map<String,Object> result = new HashMap<String,Object>();
			List<Map<String,Object>> data = settingDao.queryTimeByTableAll();
			result.put("time",data);
			List<Map<String,Object>> step = new ArrayList<Map<String, Object>>();
			for (Map<String,Object> map:data) {
				if(step.size()<1){
					step.add(map);
				}
				boolean flg = false;
				for (Map<String,Object> map2:step) {
					if(map2!=null&&step.size()>0){
						if(!map.get("step").toString().equals(map2.get("step")+"")){
							flg = true;
						}else{
							flg = false;
							break;
						}
					}else{
						step.add(map);
					}
				}
				if(flg){
					step.add(map);
				}
			}
			result.put("step",step);
			return result;
		}catch (RuntimeException e){
			log.error("获取总课表的表头"+e.getMessage());
			throw new BusinessLevelException("获取总课表的表头",e);
		}
	}

	@Override
	public ResultData<List<Map<String, Object>>> queryTabelData(Map<String, Object> map) throws BusinessLevelException {
		List<Map<String,Object>> tableHead = settingDao.queryTimeByTableAll();
		Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
		PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
		List<Map<String,Object>> tableContent = new ArrayList<>();
		try {
			tableContent = settingDao.queryTableData(map);
		}catch (RuntimeException ex){
			ex.printStackTrace();
			throw new BusinessLevelException("cad asdfasdf");
		}
		List<Map<String,Object>> tableData = allCourseData(tableContent,tableHead);
		com.github.pagehelper.PageInfo page = new com.github.pagehelper.PageInfo(tableContent);
		long totalNum = page.getTotal();
		if(tableContent.size() == 0 ){
			return new ResultData(0, null);
		}
		return new ResultData<>((int) totalNum, tableData);
	}

	@Override
	public List<Map<String, Object>> queryTimeByTableAll() throws BusinessLevelException {
		return settingDao.queryTimeByTableAll();
	}

	@Override
	public List<Map<String, Object>> queryAllTableBodyParam(Map<String, Object> map) throws BusinessLevelException {
		List<Map<String,Object>> tableHead = settingDao.queryTimeByTableAll();

		//查询出每周的数据
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		//weekDay 0-6 周一到周日
		for( int i = 0 ; i <= 6 ; i ++ ){
		    Map<String,Object> m = new HashMap<String,Object>();
			map.put("weekDay",i);
			List<Map<String,Object>> tableContent = settingDao.queryTableData(map);
			List<Map<String,Object>> tableData = allCourseData(tableContent,tableHead);
			m.put("data",tableData);
			m.put("weekDay",weekDay(i));
			result.add(m);
		}
		return result;
	}
	
	public String weekDay(int num){
		if(num == 0){
			return "星期一";
		}else if (num == 1){
			return "星期二";
		}else if (num == 2){
			return "星期三";
		}else if (num == 3){
			return "星期四";
		}else if (num == 4){
			return "星期五";
		}else if (num == 5 ){
			return "星期六";
		}else if (num == 6){
			return "星期日";
		}else{
			return null;
		}
	}

	public List<Map<String,Object>> allCourseData(List<Map<String,Object>> list,List<Map<String,Object>> head){
		List<Map<String,Object>> data = getData(list);
		for(int i = 0 ; i < head.size() ; i++){
			if(head.get(i).toString().isEmpty()){
				continue;
			}
			for (Map<String, Object> map : data) {
				if (map != null && map.get("goClassTime") != null) {
					List<Map<String,Object>> list1 = (List<Map<String, Object>>) map.get("data");
					if(list1.size()<1){
						continue;
					}
					for (Map<String,Object> map1:list1) {
						if(map1!=null && map1.get("time")!=null){
							List<Map<String,Object>> list2 = (List<Map<String, Object>>) map1.get("data");
							if(list2.size()<1){
								continue;
							}
							if(map1.get("time").toString().equals(head.get(i).get("time")+"")){
								String text = "";
								for (Map<String,Object> map2:list2) {
									text += "<div class='viewStyle'>"+map2.get("projectName")+"</div><div class='viewStyle'>" + map2.get("date")+"</div>";
								}
								map.put(head.get(i).get("time")+"",text);
							}else{
                                boolean flg =  comparisonTime(map1.get("time").toString(),head.get(i).get("time")+"");
                                if(flg){
                                    String text = "";
                                    for (Map<String,Object> map2:list2) {
                                        text += "<div class='viewStyle'>"+ map2.get("projectName")+"</div>" +
                                                "<div class='viewStyle'>" + map2.get("date")+"</div>" +
                                                "<div class='viewStyle'>"+map1.get("time").toString()+"</div>";
                                    }
                                    map.put(head.get(i).get("time")+"",text);
                                }
                            }
						}
					}
				}
			}
		}
		return data;
	}

	public List<Map<String,Object>> getData(List<Map<String,Object>> list){
		
		for (Map<String,Object> map:list) {
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			if(map!=null && !map.isEmpty() && map.get("goClassProjectName")!=null
					&& map.get("goClassTime")!=null && map.get("goClassDate")!=null){
				String goClassProjectName = map.get("goClassProjectName")+"";
				String goClassDate = map.get("goClassDate")+"";
				String goClassTime = map.get("goClassTime")+"";
				List<String> proName = Arrays.asList(goClassProjectName.split(","));
				List<String> date = Arrays.asList(goClassDate.split(","));
				List<String> time = Arrays.asList(goClassTime.split(","));
				for(int i = 0 ; i < proName.size() ; i ++ ){
					Map<String,Object> da = new HashMap<String,Object>();
					if(i == 0 ) {
						da.put("projectName", proName.get(i));
						da.put("date", date.get(i));
						da.put("time", time.get(i));
						data.add(da);
					}else{
						boolean flg = false;
						for (Map<String,Object> map2:data) {
							if(map2 != null && map2.get("time")!=null){
								if(time.get(i).toString().equals(map2.get("time"))){
									String d = map2.get("date")+"";
									String t = map2.get("projectName")+"";
									map2.put("date",d+","+date.get(i));
									map2.put("projectName",t+","+proName.get(i));
									flg = false;
									break;
								}else{
									flg = true;
								}
							}
						}
						if(flg){
							da.put("projectName", proName.get(i));
							da.put("date", date.get(i));
							da.put("time", time.get(i));
							data.add(da);
						}
					}
				}
			}
			data = projectName(data);
			map.put("data",data);
		}
		return list; 
	}

	public List<Map<String,Object>> projectName( List<Map<String,Object>> list){
		for (Map<String, Object> map : list) {
			List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
			if (map != null && !map.isEmpty() && map.get("projectName") != null
					&& map.get("date") != null && map.get("time") != null) {
				String goClassDate = map.get("date") + "";
				String goProName = map.get("projectName") + "";
				List<String> date = Arrays.asList(goClassDate.split(","));
				List<String> proName = Arrays.asList(goProName.split(","));
				for(int i = 0 ; i < proName.size() ; i ++){
					Map<String,Object> da = new HashMap<String,Object>();
					if(i == 0 ) {
						da.put("projectName", proName.get(i));
						da.put("date", date.get(i));
						l.add(da);
					}else{
						boolean flg = false;
						for (Map<String,Object> map2:l) {
							if(map2 != null && map2.get("projectName")!=null){
								if(proName.get(i).toString().equals(map2.get("projectName"))){
									String d = map2.get("date")+"";
									map2.put("date",d+","+date.get(i));
									flg = false;
									break;
								}else{
									flg = true;
								}
							}
						}
						if(flg){
							da.put("projectName", proName.get(i));
							da.put("date", date.get(i));
							l.add(da);
						}
					}
				}
				for (Map<String,Object> m: l) {
					if(m!=null&&m.get("date")!=null){
						String strDay = m.get("date").toString();
						List<String> upDay = Arrays.asList(m.get("date").toString().split(","));
						List<String> day = DateUtil.sortDate(upDay);
						String str = day.toString();
						m.put("date","("+str.substring(1, str.length()-1)+")");
					}
				}
			}
			map.put("data",l);
		}
		return list;
	}

	public boolean comparisonTime (String starts , String ends ){
        List<String> headTime = Arrays.asList(starts.split("-"));
        List<String> dataTime = Arrays.asList(ends.split("-"));
        String headStartTime = headTime.get(0);
        String headendTime = headTime.get(1);
        String dataStartTime = dataTime.get(0);
        if(DateUtil.dataStrTime(headStartTime,dataStartTime)
            && DateUtil.dataStrTime(dataStartTime,headendTime)){
            return true;
        }
        return false;
    }

    @Override
	public List<Map<String,String>> getTimeSheetList(Map<String, Object> map) {
		if(map.get("project_id") == null || "".equals(map.get("project_id").toString())) {
			return new ArrayList<Map<String,String>>();
		}
		List<Map<String,String>> result = settingDao.getTimeSheetList(map);
		return result;
	}

	@Override
	public Map getTimes(Map timeResult) {
		return settingDao.getTimes(timeResult);
	}

}

