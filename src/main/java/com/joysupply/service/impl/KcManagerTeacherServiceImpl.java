package com.joysupply.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.KcManagerTeacherDao;
import com.joysupply.service.KcManagerTeacherService;
import com.joysupply.util.Amount;

/**
 * 教师课酬管理Service实现类
 */
@Service("KcManagerTeacherService")
public class KcManagerTeacherServiceImpl extends BaseService implements KcManagerTeacherService {

	@Autowired
	KcManagerTeacherDao kcManagerTeacherDao;
	
	@Override
	public List<Map<String, Object>> getKCData(Map<String, String> request) {
		List<Map<String,Object>> list = kcManagerTeacherDao.getKCData(request);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		// 按年分组
		Map<String, List<Map<String, Object>>> year = list.stream().collect(Collectors.groupingBy(m -> m.get("year").toString()));
		// 2017
		year.forEach((yk,yv) -> {
			Map<String, List<Map<String, Object>>> month = yv.stream().collect(Collectors.groupingBy(m -> m.get("month").toString()));
			month.forEach((mk,mv) -> {
				Map<String, Object> map = new HashMap<>();
				map.put("year", yk);
				map.put("month", mk);
				result.add(map);
			});
		});
		int num=1;
		//按月分组
		Map<String, List<Map<String, Object>>> month2 = list.stream().collect(Collectors.groupingBy(m -> m.get("month").toString()));
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> map = result.get(i); // i=0
			String yearStr = map.get("year").toString(); // 2017
			String monthStr = map.get("month").toString(); // 1
			List<Map<String, Object>> list2 = month2.get(monthStr); // 集合 2017  2018  ....
			String totalFee = "0.00";
			for (int j = 0; j < list2.size(); j++) { // j=0
				Map<String, Object> map2 = list2.get(j); // 1
				if (yearStr.equals(map2.get("year").toString())){ // 2017
					map.put("px", num);
					map.put("teacherType", map2.get("teacherType"));
					map.put("name", map2.get("name"));
					map.put("idCard", map2.get("idCard"));
					map.put("bankCode", map2.get("bankCode"));
					map.put("guoji", map2.get("guoji"));
					String fee_course = map2.get("fee_course").toString();
					if("1901".equals(map2.get("projectId").toString())){
						totalFee = Amount.sub(totalFee, fee_course);
					}else{
						totalFee = Amount.add(totalFee, fee_course);
					}
					if(map.containsKey(map2.get("projectName").toString())){
						String a = map.get(map2.get("projectName").toString()).toString();
						String b = map2.get("fee_course").toString();
						String add = Amount.add(a, b);
						map.put(map2.get("projectName").toString(),add);
					}else{
						map.put(map2.get("projectName").toString(),map2.get("fee_course"));
					}
					num++;
				}
			}
			map.put("totalFee", totalFee);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getProductName(String personCode) {
		List<Map<String, Object>> productName = kcManagerTeacherDao.getProductName(personCode);

		return productName;
	}

	@Override
	public Map<String, Object> getTeacherInfo(String personCode) {
		
		return kcManagerTeacherDao.getTeacherInfo(personCode);
	}

	@Override
	public List<Map<String, Object>> getKCInfo(Map<String, Object> request) {
		List<Map<String, Object>> kcInfo = kcManagerTeacherDao.getKCInfo(request);
		int num=1;
		String totalFee = "0.00";
		int hour_actual = 0;
		if (kcInfo != null && kcInfo.size() > 0) {
			for (Map<String, Object> map : kcInfo) {
				map.put("px", num);
				if("工作量扣除".equals(map.get("project_name").toString())){
					totalFee = Amount.sub(totalFee, map.get("fee_course").toString());
					hour_actual =hour_actual - Integer.valueOf(map.get("hour_actual").toString());
				}else{
					totalFee = Amount.add(totalFee, map.get("fee_course").toString());
					hour_actual =hour_actual + Integer.valueOf(map.get("hour_actual").toString());
				}
				
				num++;
			}
		}
		Map<String, Object> total = new HashMap<String, Object>();
		total.put("px", "合计");
		total.put("fee_course", totalFee);
		total.put("hour_actual", hour_actual);
		kcInfo.add(total);
		return kcInfo;
	}

	@Override
	public List<Map<String, Object>> getThisWeekKc(Map<String, Object> param) {
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		try {
			String timeInterval = getTimeInterval();
			String[] split = timeInterval.split(",");
			String monday = split[0];
			String sunday = split[1];
			param.put("monday", monday);
			param.put("sunday", sunday);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			Date dBegin = sdf.parse(monday);
			Date dEnd = sdf.parse(sunday);
			List<Date> weekList = findDates(dBegin, dEnd);//获取这周所有date
			list = kcManagerTeacherDao.getThisWeekKc(param);
			for (int i = 0; i < weekList.size(); i++) {
				Map<String, Object> data = new HashMap<String, Object>();
				String time = sdf.format(weekList.get(i)); 
				data.put("week", getWeekName(i));
				data.put("fee", "0");
				for (Map<String, Object> map : list) {
					String endTime = map.get("classDate").toString();
					if(time.equals(endTime)){
						data.put("fee", map.get("feeTotal"));
					}
				}
				result.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getWeekName(int i){
		if(i == 0){
			return "周一";
		}else if(i == 1){
			return "周二";
		}else if(i == 2){
			return "周三";
		}else if(i == 3){
			return "周四";
		}else if(i == 4){
			return "周五";
		}else if(i == 5){
			return "周六";
		}else if(i == 6){
			return "周日";
		}
		return "";
	}
	
	public String getTimeInterval() {  
		Date date = new Date();
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
        if (1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
        String imptimeBegin = sdf.format(cal.getTime());  
        // System.out.println("所在周星期一的日期：" + imptimeBegin);  
        cal.add(Calendar.DATE, 6);  
        String imptimeEnd = sdf.format(cal.getTime());  
        // System.out.println("所在周星期日的日期：" + imptimeEnd);  
        return imptimeBegin + "," + imptimeEnd;  
   }
	
	/**
	 * 获取本周开始到结束的日期集合
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static List<Date> findDates(Date dBegin, Date dEnd)  
    {  
     List lDate = new ArrayList();  
     lDate.add(dBegin);  
     Calendar calBegin = Calendar.getInstance();  
     // 使用给定的 Date 设置此 Calendar 的时间  
     calBegin.setTime(dBegin);  
     Calendar calEnd = Calendar.getInstance();  
     // 使用给定的 Date 设置此 Calendar 的时间  
     calEnd.setTime(dEnd);  
     // 测试此日期是否在指定日期之后  
     while (dEnd.after(calBegin.getTime()))  
     {  
      // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
      calBegin.add(Calendar.DAY_OF_MONTH, 1);  
      lDate.add(calBegin.getTime());  
     }  
     return lDate;  
    }

	@Override
	public String getTodayKc(Map<String, Object> param) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(date);
		param.put("todayDate", format);
		String todayKc = kcManagerTeacherDao.getTodayKc(param);
		return todayKc;
	}

}
