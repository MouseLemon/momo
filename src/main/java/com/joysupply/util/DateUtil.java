package com.joysupply.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.util.StringUtils;

public class DateUtil {
	
	private final static String DEFAULT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	private final static String[] WEEKS = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
	/**
	 * 获取日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String ToDateTimeString() {
		return ToDateString(new Date(), DEFAULT_DATETIME);
	}
	/**
	 * 获取日期 yyyyMMddHHmm
	 * @return
	 */
	public static String ToDateTString() {
		return ToDateString(new Date(), "yyyyMMddHHmm");
	}
	/**
	 * 获取日期 yyyyMMdd
	 * 
	 * @return
	 */
	public static String toShortDate() {
		return ToDateString(new Date(), "yyyyMMdd");
	}
	/**
	 * 获取当前时间 单位(秒)
	 * @return
	 */
	public static long getCurrentTime(){
		return (new Date()).getTime();
	}
	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式化的格式 默: yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String ToDateString(Date date, String format) {
		if (date == null) {
			date = new Date();
		}
		if (format == null || StringUtils.isEmpty(format)) {
			format = DEFAULT_DATETIME;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);
	}

	public static String getEnglishDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy",Locale.ENGLISH);
		return dateFormat.format(date);
	}
	/**
	 * 年月日分割
	 * @param dateParam
	 * @return
	 */
	public static Calendar dateSplit(String dateParam) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
        try {
            Date date = sf.parse(dateParam);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return calendar;
	}
	/**
	 * 获取Calendar
	 * @param dateParam
	 * @return
	 */
	public static Calendar getCalendar(String format, String dateParam) {
		if(format == null ||  StringUtils.isEmpty(format)) {
			format = DEFAULT_DATETIME;
		}
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = sf.parse(dateParam);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}
	/**
	 * 根据日期计算星期几
	 * @param datetime
	 * @return
	 */
	public static String dateToWeekDay(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	/**
	 * 根据日期计算星期几
	 * @param datetime
	 * @return
	 */
	public static String dateToWeekDay(Date datetime) {
        String[] weekDays = { "6", "0", "1", "2", "3", "4", "5" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.setTime(datetime);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	/**
	 * 日期转某年的第几周
	 * @param datetime
	 * @return
	 */
	public static int dateToWeek(String datetime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(datetime));
			cal.setFirstDayOfWeek(Calendar.MONDAY);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		return week;
    }

	/**
	 * 获取指定日期的年/月/日/周/星期
	 * @param dateTime
	 * @return
	 */
	public static Map getCulm(String dateTime,String next, String up,String monthNextOrUp) {
		Map result = new HashMap();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		if(dateTime != null && !"".equals(dateTime)) {
			try {
				c.setTime(sdf.parse(dateTime));
				if(next != null && !"".equals(next)) {
					if(StringUtils.isEmpty(monthNextOrUp)) {
						c.add(Calendar.DAY_OF_MONTH, 7);
					}else {
						c.add(Calendar.MONTH, 1);
					}
				}
				if(up != null && !"".equals(up)) {
					if(StringUtils.isEmpty(monthNextOrUp)) {
						c.add(Calendar.DAY_OF_MONTH, -7);
					}else {
						c.add(Calendar.MONTH, -1);
					}

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 当前日期是本月第几周
		int weeks = c.get(Calendar.WEEK_OF_YEAR);
		
		String date = ToDateString(c.getTime(),"yyyy-MM-dd");
		result.put("dateString", date);
		String weekString = "第"+weeks +"周";
		result.put("weekString", weekString);
		int mo = c.get(Calendar.MONTH)+1;
		String monthString = mo +"月";
		result.put("monthString", monthString);
		result.put("weeks", weeks);
		// 当前是星期几 java中一周第一天为星期天，所以6代表星期日，0代表星期一，以此类推
		int week = c.get(Calendar.DAY_OF_WEEK)-1;
		result.put("year", c.get(Calendar.YEAR));
		if(week == 0) { // 为0时
			c.add(Calendar.DATE, -6);
		}else {

			c.add(Calendar.DATE, -week+1);
		}
		List<String> culm = new ArrayList<String>();
		List<String> months = new ArrayList<String>();
		List<String> years = new ArrayList<String>();
		List<String> days = new ArrayList<String>();
		List<String> weekDay = new ArrayList<String>();

		result.put("xyStart", sdf.format(c.getTime()));

		for (int i = 0; i < 7; i++) {
			int month = c.get(Calendar.MONTH)+1; // 月份加1
			int day = c.get(Calendar.DAY_OF_MONTH);
			int year = c.get(Calendar.YEAR);
			String cul = WEEKS[i]+"("+month+"/"+day+")";
			culm.add(cul);
			years.add((year+"").length() ==1 ?("0"+year):year+""); // 年
			months.add((month+"").length() ==1 ?("0"+month):month+""); // 月
			days.add((day+"").length() ==1 ?("0"+day):day+"");  // 月中天
			weekDay.add(i+""); // 周几 0 周一 1 周二 -----
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		c.add(Calendar.DAY_OF_MONTH, -1);
		result.put("cul", culm);

		result.put("xyEnd", sdf.format(c.getTime()));
		result.put("months", months);
		result.put("days", days);
		result.put("years", years);
		result.put("weekDay", weekDay);

		if(dateTime != null && !"".equals(dateTime)) {
			try {
				c.setTime(sdf.parse(dateTime));
				if(next != null && !"".equals(next)) {
					c.add(Calendar.DAY_OF_MONTH, 7);
				}
				if(up != null && !"".equals(up)) {
					c.add(Calendar.DAY_OF_MONTH, -7);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		result.put("fristDay", sdf.format(c.getTime()));
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		result.put("lastDay", sdf.format(c.getTime()));

		return result;
	}


	public static int getDays(String format, String startTime, String endTime) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(endTime);
			Date date2 = sdf.parse(startTime);
			int a = (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
//			calendar.setTime(sdf.parse(startTime));
//			int startDay = calendar.get(Calendar.DAY_OF_YEAR);
//			calendar.setTime(sdf.parse(endTime));
//			int endDay = calendar.get(Calendar.DAY_OF_YEAR);
//			return endDay - startDay;
			return a;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	
	
	public static Date getDate(int hour){
		Date date = null;
		Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.HOUR, hour);
        date = cal.getTime();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        date = cal.getTime();
        return date;
    }

    public static String getDateDir() {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter1.format(new Date());
		return dateString;
	}

    public static String getStringAllDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = formatter.format(new Date());
        return dateString;
    }
    public static List<String> sortDate(List<String> list){
		List<Calendar> datas = strToDate(list);
		List<String> listStr = new ArrayList<String>();
		for(int i = 0 ; i < datas.size() ; i ++){
			String month = datas.get(i).get(Calendar.MONTH )+1+"";//当前月
			String day = datas.get(i).get(Calendar.DAY_OF_MONTH)+"";//当前日
			Calendar yesterday = Calendar.getInstance();
			Calendar upData = Calendar.getInstance();
			if(i>0){
				upData = datas.get(i-1);
				yesterday = datas.get(i);
				yesterday.add(Calendar.DATE, -1);
			}
			String yesMonth = yesterday.get(Calendar.MONTH )+1+"";//当前数据的昨天月
			String yesDay = yesterday.get(Calendar.DAY_OF_MONTH)+"";//当前数据的昨天日
			yesterday.add(Calendar.DATE, 1);
			String upMonth = upData.get(Calendar.MONTH)+1+"";//上一个数据的月
			String upDay = upData.get(Calendar.DAY_OF_MONTH)+"";//上一个数据的日
			boolean flg = false;
			if(i == 0){
				listStr.add(month+"."+day);
			}else{
				if(!(yesMonth+"."+yesDay).equals(upMonth+"."+upDay)){
					String a = listStr.get(listStr.size()-1);
					if(!a.equals(upMonth+"."+upDay)){
						listStr.set(listStr.size()-1,a+"-"+upMonth+"."+upDay);
					}
					listStr.add(month+"."+day);
					continue ;
				}else{
					flg = true;
					if(i!=(datas.size()-1)){
						continue ;
					}
				}
			}
			if(flg){
				String a = listStr.get(listStr.size()-1);
				listStr.set(listStr.size()-1,a+"-"+month+"."+day);
			}
		}
		return listStr;
	}
	public static List<Calendar> strToDate(List<String> list) {
		if(list.size()<1){
			return null;
		}
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		List<String> data = strToListStr(list);
		List<Calendar> dataDate = new ArrayList<>();
		for (String str: data) {
			Date date = null;
			try{
				date=sdf.parse(str);
			}catch ( Exception ex){
				System.out.println(ex);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			dataDate.add(calendar);
		}
		return dataDate;
	}
	public static List<String> strToListStr(List<String> list){
		list = duplicate(list);
		Collections.sort(list,new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				String str1=(String) o1;
				String str2=(String) o2;
				if (str1.compareToIgnoreCase(str2)<0){
					return -1;
				}
				return 1;
			}
		});
		return list;
	}
	public static List<String> duplicate(List<String> list){
		Set<String> set = new HashSet<String>(list);
		List<String> result = new ArrayList<>(set);
		return result;
	}

	public static boolean dataStrTime(String start , String end) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
		try {
			Date dt1 = df.parse(start);//将字符串转换为date类型
			Date dt2 = df.parse(end);
			return dt1.compareTo(dt2)==1?false:true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

}
