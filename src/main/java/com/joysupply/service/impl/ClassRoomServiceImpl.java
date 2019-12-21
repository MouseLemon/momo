package com.joysupply.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.joysupply.dao.*;
import com.joysupply.service.ClassManageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.entity.ClassRoom;
import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.ProjectSchedulePlan;
import com.joysupply.entity.RoomUnuseTimeFrame;
import com.joysupply.entity.dto.ClassRoomDisableTimeDto;
import com.joysupply.entity.dto.StartTimeAndEndTimeDto;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ClassRoomService;
import com.joysupply.service.DataDictionaryService;
import com.joysupply.util.Constants;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;

/**
 * 教室管理实现类
 * @author zxt
 *
 * 2018年11月3日-下午2:14:02
 */
@Service("classRoomService")
public class ClassRoomServiceImpl extends BaseService implements ClassRoomService {
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private ClassRoomDao classRoomDao;
	@Autowired
	private ProjectSchedulePlanDao projectSchedulePlanDao;
	@Autowired
	private RoomUnuseTimeFrameDao roomUnuseTimeFrameDao;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private ClassManageService classManageService;
	@Autowired
	private AuthorityManageDao authorityManageDao;
	@Autowired
	private MessageDao messageDao;

	
	private final static String[] WEEK = {"一","二","三","四","五","六","日"};
	@Override
	public Map<String, Object> getClassRooms(ClassRoom classRoom,Integer page, Integer limit) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(page == null || page == 0) {
			page = 1;
		}
		if(limit == null || limit == 0) {
			limit = 10;
		}
		PageHelper.startPage(page,limit);
		List<ClassRoom> listData = classRoomDao.getClassRooms(classRoom);
		setUseSeason(listData);
		
		PageInfo<ClassRoom> pageInfo = new PageInfo<ClassRoom>(listData);
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", pageInfo.getTotal());
		result.put("data", pageInfo.getList());
		return result;
	}

	/**
	 * 封装可用季节
	 * @param listData
	 */
	public void setUseSeason(List<ClassRoom> listData) {
		List<DataDictionary> useSeason = dataDictionaryService.getDicItemListNoPage("20"); // 可用季节
		for (ClassRoom classRoom2 : listData) {
			if(classRoom2.getUseSeason() != null && !"".equals(classRoom2.getUseSeason())) {
				String useSeasonStr = "";
				String useSeason2 = classRoom2.getUseSeason();
				String[] seasonArr = useSeason2.split(",");
				for (String item : seasonArr) {
					for (DataDictionary dataDictionary : useSeason) {
						if(item.equals(dataDictionary.getCode())) {
							useSeasonStr+=dataDictionary.getName()+", ";
						}
					}
				}
				useSeasonStr = useSeasonStr.substring(0,useSeasonStr.lastIndexOf(","));
				classRoom2.setUseSeason(useSeasonStr);
			}
			
		}
	}
	
	@Override
	@Transactional
	public OpResult saveNewClassRoom(Map<String, Object> map) throws BusinessLevelException {
		//TODO 参数判断
		try {

			String toDateTimeString = DateUtil.ToDateTimeString();
			map.put("createTime", toDateTimeString);
			if(map.get("roomId") !=null && !"".equals(map.get("roomId").toString())) {
				classRoomDao.updateClassRoom(map);
			}else {
				int row = classRoomDao.getRoomByLocAndNum(map);
				if(row > 0) {
					return new OpResult("请勿重复添加");
				}
				map.put("roomId", Constants.GUID());
				classRoomDao.saveNewClassRoom(map);
			}
			return new OpResult();
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			throw new BusinessLevelException("新建或修改教室失败",ex);
		}
	}
	
	/**
	 * 批量停用或启用
	 */
	@Override
	@Transactional
	public OpResult updateProhibitionStatus(Map<String, Object> map) throws BusinessLevelException {
		try {
			String ids = map.get("roomIdList").toString();
			String[] idsArr = ids.split(",");
			map.put("roomIdList", idsArr);
			int updateProhibitionStatus = classRoomDao.updateProhibitionStatus(map);
			return new OpResult();
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			throw new BusinessLevelException("批量停用或启用教室失败",ex);
		}
	}
	
	/**
	 * 删除
	 */
	@Override
	@Transactional
	public OpResult deleteClassRoom(Map<String, Object> map) throws BusinessLevelException {
		if(map.get("roomId") == null || "".equals(map.get("roomId").toString())) {
			return new OpResult("传入的roomId为空");
		}
		try {
			ProjectSchedulePlan projectSchedulePlan = new ProjectSchedulePlan();
			projectSchedulePlan.setClass_room_id(map.get("roomId").toString());
			//1.如果排课已经排过该教室，则不能删除，只能停用
			Integer row = projectSchedulePlanDao.getProjectSchedulePlanByRooId(map.get("roomId").toString());
			//该教室已经排过课,不能删除,只能停用
			if(row != null && row > 0  ) {
				return new OpResult("该教室已经排过课,不能删除,只能停用");
			}
			//2.删除
			classRoomDao.deleteClassRoom(map);
			return new OpResult();
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			throw new BusinessLevelException("删除教室失败",ex);
		}
	}
	
	
	@Override
	public List<Map<String, Object>> getMactchingRooms(Map<String, Object> map) {
		if(map.get("scheduleId").toString() == null || "".equals(map.get("scheduleId").toString())) {
			return new ArrayList<Map<String, Object>>();
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		//1.根据项目排课计划表主键查询记录
		ProjectSchedulePlan projectSchedulePlan = new ProjectSchedulePlan();
		projectSchedulePlan = projectSchedulePlanDao.getProjectSchedulePlanByScheduleId(map);
		if(projectSchedulePlan !=null) {
			Calendar dateSplit =  DateUtil.dateSplit(projectSchedulePlan.getClass_date());
			//2.根据人数、教室类型和可用时间，筛选出符合的教室
			paramMap.put("personCount",projectSchedulePlan.getPerson_count());
			paramMap.put("roomType",projectSchedulePlan.getClass_room_type());
			paramMap.put("year",dateSplit.get(Calendar.YEAR));
			paramMap.put("month",dateSplit.get(Calendar.MONTH) + 1);
			paramMap.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
			paramMap.put("startTime",projectSchedulePlan.getStart_time());
			paramMap.put("endTime",projectSchedulePlan.getEnd_time());
			resultMap = classRoomDao.getMactchingRooms(paramMap);
		}
		return resultMap;
	}
	
	@Transactional
	@Override
	public OpResult saveUsableRoom(Map<String, Object> map) throws BusinessLevelException {
		if(map.get("roomId") == null || "".equals(map.get("roomId").toString())) {
			return new OpResult("传入的roomId为空");
		}
		if(map.get("scheduleId") == null || "".equals(map.get("scheduleId").toString())) {
			return new OpResult("传入的scheduleId为空");
		}
		Map<String, Object> scheduleMap = new HashMap<String, Object>();
		scheduleMap.put("scheduleId",map.get("scheduleId").toString());
		Map<String, Object> unuseTimeMap = new HashMap<String, Object>();
		try {
			//1.根据scheduleId查询该课是否已经排过教室,
			ProjectSchedulePlan projectSchedulePlan = new ProjectSchedulePlan();
			projectSchedulePlan = projectSchedulePlanDao.getProjectSchedulePlanByScheduleId(scheduleMap);
			if(projectSchedulePlan !=null) {
				if(projectSchedulePlan.getClass_room_id() == null || "".equals(projectSchedulePlan.getClass_room_id())) {//未排过教室
					//1.1查询在该时间段内是否被其他教室占用
					Calendar dateSplit =  DateUtil.dateSplit(projectSchedulePlan.getClass_date());
					unuseTimeMap.put("year",dateSplit.get(Calendar.YEAR));
					unuseTimeMap.put("month",dateSplit.get(Calendar.MONTH) + 1);
					unuseTimeMap.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
					unuseTimeMap.put("startTime",projectSchedulePlan.getStart_time());
					unuseTimeMap.put("endTime",projectSchedulePlan.getEnd_time());
					List<RoomUnuseTimeFrame> roomIsAvailbleList = roomUnuseTimeFrameDao.getRoomUnuseTimeFrameListByRoomID(unuseTimeMap);
					if(roomIsAvailbleList !=null && roomIsAvailbleList.size()>0) {
						return new OpResult("该教室已被占用，请重新选择教室！");
					}
					//1.2修改项目排课计划表
					projectSchedulePlanDao.updateProjectSchedulePlan(map);
					//1.3插入不可用时间表
					map.put("week",DateUtil.dateToWeek(projectSchedulePlan.getClass_date()));
					map.put("weekDay",DateUtil.dateToWeekDay(projectSchedulePlan.getClass_date()));
					map.put("frameId",Constants.GUID());
					map.put("year",dateSplit.get(Calendar.YEAR));
					map.put("month",dateSplit.get(Calendar.MONTH) + 1);
					map.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
					map.put("startTime",projectSchedulePlan.getStart_time());
					map.put("endTime",projectSchedulePlan.getEnd_time());
					roomUnuseTimeFrameDao.saveRoomUnuseTimeFram(map);
				}else {
					//已排过教室
					//1.1查询是否已经上过课,未上课,可以更改
					Date now = new Date();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
					Date classDate = new Date();
					try {
						 classDate = sdf.parse(projectSchedulePlan.getClass_date());
						 if(classDate.after(now)) {//可以修改该教室
							 //1.2修改项目排课计划表
							 projectSchedulePlanDao.updateProjectSchedulePlan(map);
							 //1.3修改不可用时间表
							 Calendar dateSplit =  DateUtil.dateSplit(projectSchedulePlan.getClass_date());
							 map.put("year",dateSplit.get(Calendar.YEAR));
							 map.put("month",dateSplit.get(Calendar.MONTH) + 1);
							 map.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
							 map.put("startTime",projectSchedulePlan.getStart_time());
							 map.put("endTime",projectSchedulePlan.getEnd_time());
							 roomUnuseTimeFrameDao.updateRoomUnuseTimeFram(map);
						 }else {
							 return new OpResult("该上课时间不能进行修改教室");
						 }
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			return new OpResult();
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			throw new BusinessLevelException("保存教室",ex);
		}
	}
	@Override
	public List<Map<String, Object>> getClassRoomSpareTime(Map<String, Object> map) {
		if(map.get("classDate") == null || "".equals(map.get("classDate").toString())) {
			return null;
		}
		 Calendar dateSplit =  DateUtil.dateSplit(map.get("classDate").toString());
		 map.put("year",dateSplit.get(Calendar.YEAR));
		 map.put("month",dateSplit.get(Calendar.MONTH) + 1);
		 map.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
		 List<ClassRoomDisableTimeDto> classRoomDisableTimeList = new ArrayList<ClassRoomDisableTimeDto>();
		 List<String> startOrEndTime = new ArrayList<String>();
		 List<StartTimeAndEndTimeDto> startTimeAndEndTimeDto = new ArrayList<StartTimeAndEndTimeDto>();
		 SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");
		//1.查看教室的id和对应的不可用时间段
		List<Map<String, Object>> disableQuantumList = classRoomDao.getClassRoomSpareTime(map);
		String disabletime = "";
		if(disableQuantumList !=null && disableQuantumList.size()>0) {
			for (int i = 0; i < disableQuantumList.size(); i++) {
				classRoomDisableTimeList.get(i).setRoomId(disableQuantumList.get(i).get("roomId").toString());
				disabletime = disableQuantumList.get(i).get("disabletime").toString();
				//逗号分隔时间段(8:00-9:00)
				List<String> result = Arrays.asList(StringUtils.split(disabletime,","));
				//时间段按照'-'分隔开始时间和结束时间
				if(result !=null && result.size()>0) {
					for(int j = 0; j<result.size();j++) {
						startOrEndTime = Arrays.asList(StringUtils.split(result.get(j),"-"));
						//区分开始时间和结束时间
						try {
							if(dateFormat.parse(startOrEndTime.get(0)).before(dateFormat.parse(startOrEndTime.get(1)))) {
								startTimeAndEndTimeDto.get(j);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		
		return null;
	}
	
	
	@Override
	public List<String> getClassRoomAlreadyClasses(Map<String, Object> map) {
		if(map.get("classDate") == null || "".equals(map.get("classDate").toString())) {
			return null;
		}
		if(map.get("roomId") == null || "".equals(map.get("roomId").toString())) {
			return null;
		}
		List<String> alreadyClassTimeList = new ArrayList<String>();
		List<String> noRepeateClassTimeList = new ArrayList<String>();
		String alreadyClassTime = "";
		 Calendar dateSplit =  DateUtil.dateSplit(map.get("classDate").toString());
		 map.put("year",dateSplit.get(Calendar.YEAR));
		 map.put("month",dateSplit.get(Calendar.MONTH) + 1);
		 map.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
		//1.查看教室的id和对应的不可用时间段
		List<Map<String, Object>> disableQuantumList = classRoomDao.getClassRoomSpareTime(map);
		if(disableQuantumList !=null && disableQuantumList.size()>0) {
			alreadyClassTime = disableQuantumList.get(0).get("disabletime").toString();
			alreadyClassTimeList = Arrays.asList(StringUtils.split(alreadyClassTime,","));
			//去重
			noRepeateClassTimeList = new ArrayList<>(new HashSet<>(alreadyClassTimeList));
		}
		return noRepeateClassTimeList;
	}
	
	
	/**
	 * 退回排课
	 */
	@Override
	@Transactional
	public OpResult updateReturnToClass(Map<String, Object> map) throws BusinessLevelException  {
		if(map.get("scheduleId") == null || "".equals(map.get("scheduleId").toString())) {
			return new OpResult("传入的scheduleId为空");
		}
		try {
			//state修改为-1(教研室排课退回)状态.
			map.put("status", -1);
			projectSchedulePlanDao.updateProjectSchedulePlan(map);
			return new OpResult();
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			throw new BusinessLevelException("退回排课",ex);
		}
	}




	@Override
	public ClassRoom getClassRoomByRoomId(String roomId) {
		return classRoomDao.getClassRoomByRoomId(roomId);
	}



	/**
	 * 设置教室不可用时间段
	 */
	@Override
	@Transactional
	public OpResult saveUnUseTime(String option, String year, String week,
			String roomId) throws BusinessLevelException {
		// 先删除当前年当前周的数据
		deleUnUseTime(year,week,roomId);
		// 解析字符串
		List<RoomUnuseTimeFrame> frames = new ArrayList<RoomUnuseTimeFrame>();
		if(option != null && option != "" && option != ",") {
			getRoomUnUseList(option, year, week, roomId, frames);
			try {
				roomUnuseTimeFrameDao.saveUnUseTime(frames);
				return new OpResult();
			} catch (Exception e) {
				// TODO: handle exception
				throw new BusinessLevelException("添加不可用时间段失败",e);
			}
		}
		
		return new OpResult();
		
	}



	/**
	 * 封装本周不可用时间段数据
	 * @param option
	 * @param year
	 * @param week
	 * @param roomId
	 * @param frames
	 */
	private void getRoomUnUseList(String option, String year, String week,
			String roomId, List<RoomUnuseTimeFrame> frames) {
		if(option != null && !"".equals(option)) {
			String[] options = option.split(",");
			for (String item : options) { // 所有记录
				String[] items = item.split("-"); // 每一天记录
				RoomUnuseTimeFrame frame = new RoomUnuseTimeFrame();
				frame.setFrameId(Constants.GUID());
				frame.setWeek(week);
				frame.setRoomId(roomId);
				frame.setMonth(items[1]);
				frame.setDay(items[2]);
				frame.setWeekDay(items[3]);
				frame.setStartTime(items[4]);
				frame.setEndTime(items[5]);
				frame.setYear(items[0]);
				frames.add(frame);
			}
		}
	}




	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void deleUnUseTime(String year, String week, String roomId)
			throws BusinessLevelException {
		Map params = new HashMap();
		params.put("year", year);
		params.put("week", week);
		params.put("roomId", roomId);
		try {
			roomUnuseTimeFrameDao.deleteRoomUnuseTime(params);
		} catch (Exception e) {
			throw new BusinessLevelException("删除不可用时间段失败",e);
		}
	}



	
	@Override
	@Transactional
	public OpResult copyUnUseTime(String option, String year, String week,
			String roomId, Map data) throws BusinessLevelException {
		if(data.get("startTime") == null || "".equals(data.get("startTime").toString()) || data.get("endTime") == null || "".equals(data.get("endTime").toString())) {
			return new OpResult("请选择复制的时间段");
		}
		String[] weekDays = { "6", "0", "1", "2", "3", "4", "5" };
		String startTime = data.get("startTime").toString();
		String endTime = data.get("endTime").toString();
		List<RoomUnuseTimeFrame> frames = new ArrayList<RoomUnuseTimeFrame>();
		List<RoomUnuseTimeFrame> others = new ArrayList<RoomUnuseTimeFrame>();
		getRoomUnUseList(option, year, week, roomId, frames);
		if(frames.size() <= 0) {
			return new OpResult("当前没有可复制的内容");
		}
		int days = DateUtil.getDays("yyyy-MM-dd", startTime, endTime);
		if(days <= 0) {
			return new OpResult("请正确选择开始与结束时间");
		}
		
		// 封装数据
		Calendar calendar = DateUtil.getCalendar("yyyy-MM-dd", data.get("startTime").toString());
		int nowYear = calendar.get(Calendar.YEAR);
		for (int i = 0; i <= days; i++) {
			int month = calendar.get(Calendar.MONTH)+1; // 月份加1
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int weeks = calendar.get(Calendar.WEEK_OF_YEAR);
			String monthStr = (month+"").length() ==1 ?("0"+month):month+"";
			String dayStr = (day+"").length() ==1 ?("0"+day):day+"";
			int index = calendar.get(Calendar.DAY_OF_WEEK)-1;
			String weekDay2 = weekDays[index];
			if(index == 0) {
                weeks = weeks -1;
            }
			if(weekDay2.equals("0")) {
				nowYear = calendar.get(Calendar.YEAR);
			}
			for (int j = 0; j < frames.size(); j++) {
				String weekDay = frames.get(j).getWeekDay();
				String day2 = frames.get(j).getDay();
				String month2 = frames.get(j).getMonth();
				
				if(day2.equals(dayStr) && month2.equals(monthStr) && frames.get(j).getYear().equals(nowYear)) {
					break;
				}
				if(frames.get(j).getWeekDay().equals(weekDay2)) { // 星期相同
					RoomUnuseTimeFrame frame = new RoomUnuseTimeFrame();
					frame.setWeek(weeks+"");
					frame.setRoomId(roomId);
					frame.setMonth(monthStr);
					frame.setDay(dayStr);
					frame.setWeekDay(weekDay2);

					frame.setYear(nowYear+"");

					frame.setFrameId(Constants.GUID());
					frame.setStartTime(frames.get(j).getStartTime());
					frame.setEndTime(frames.get(j).getEndTime());
					others.add(frame);
//					break;
				}
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		try {
			// 查询是否有时间已经设置
			Map<String,Object> params = new HashMap<>();
			params.put("others",others);
			params.put("frames",frames);
			params.put("roomId",roomId);
			List<String> noCopyDateTimes = roomUnuseTimeFrameDao.selIsSet(params);
			if(noCopyDateTimes.size() > 0 && !"null".equals(noCopyDateTimes.get(0)) && noCopyDateTimes.get(0) != null) {
				String msg = "以下日期已设置，不能复制：";
				for (String string : noCopyDateTimes) {
					msg+= "<br>"+string;
				}
				return new OpResult(msg);
			}
//			roomUnuseTimeFrameDao.deleteIsSet(others);
			roomUnuseTimeFrameDao.saveUnUseTime(others);
			return new OpResult();
		} catch (Exception e) {
			throw new BusinessLevelException("添加不可用时间段失败",e);
		}
	}



	/**
	 * 查询空闲时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIdleRoom(String roomLoc,
			String class_room_type, String class_date, String person_count,
			String starTime, String endTime, String year, String scheduleId,String roomId) {
		Map result = new HashMap();
		Map params = new HashMap();
		params.put("roomLoc", roomLoc);
		if(class_room_type != null) {

			params.put("class_room_type", class_room_type);
			params.put("class_date", class_date);

			if(class_date == null || "".equals(class_date)) {
				class_date = DateUtil.ToDateString(new Date(),"yyyy-MM-dd");
			}
			params.put("month", class_date.split("-")[1]);
			params.put("day", class_date.split("-")[2]);
			//
			params.put("person_count", person_count);
			params.put("starTime", starTime);
			params.put("endTime", endTime);
			params.put("year", class_date.split("-")[0]);
			params.put("roomId", roomId);
			// 可用季节
//			String season = getNowSeason(params.get("month").toString());
//			params.put("season",season);
		}
        List<ClassRoom> idleRoom = classRoomDao.getIdleRoom(params);
        // 查询已排
		if(idleRoom == null || idleRoom.size() <= 0 || idleRoom.get(0) == null) {
            result.put("success", false);
            result.put("msg", "未匹配到合适的教室，请重新选择所属位置");
        }else {
            params.put("idleRoom", idleRoom);
            List<Map<String,String>> roomids = classRoomDao.getIsPlanRoomId(params);
            Iterator<ClassRoom> iterator = idleRoom.iterator();
            while (iterator.hasNext()) {
                ClassRoom classRoom = iterator.next();
                for (Map<String,String> id : roomids) {
                    if(id.get("roomId").equals(classRoom.getRoomId()) && scheduleId != null && !id.get("scheduleId").equals(scheduleId)) {
                        iterator.remove();
                    }
                }
            }
            if(idleRoom.size() <= 0 || idleRoom.get(0) == null) {
                result.put("success", false);
                result.put("msg", "未匹配到合适的教室，请重新选择所属位置");
            }else {
                result.put("success", true);
                result.put("idleRoom", idleRoom);
            }
		}
		return result;
	}


	
	/**
	 * 查看教室已排课详情
	 */
	@Override
	public Map<String, Object> getRoomPlan(String roomId, String class_date, String starTime, String endTime, String year) {
		Map<String,Object> params = new HashMap<String, Object>(); 
		/*if(class_date != null && !"".equals(class_date)) {
			params.put("month", class_date.split("-")[1]);
			params.put("day", class_date.split("-")[2]);
		}*/
		params.put("class_date", class_date);
		params.put("starTime", starTime);
		params.put("endTime", endTime);
		params.put("year", year);
		params.put("roomId", roomId);
		// 查询已排课情况
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> isPlan = classRoomDao.getRoomPlan(params);
		if(isPlan == null) {
			result.put("isPlan",isPlan);
		}else {
			result.putAll(isPlan);
		}
		Map<String,Object> room = classRoomDao.getRoom(roomId);
		result.putAll(room);
		return result;
	} 




	@Override
	public List<Map<String, Object>> getUnTimeData(List<ClassRoom> roomList, String classDate) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("roomList", roomList);
		param.put("classDate", classDate);
		if(classDate != null && !"".equals(classDate)) {
			param.put("month", classDate.split("-")[1]);
			param.put("day", classDate.split("-")[2]);
			param.put("year", classDate.split("-")[0]); // 年
		}
		
		return classRoomDao.getUnTimeData(param);
	}
	@Override
	public List<Map<String, Object>> getUnTimeDataPsp(List<ClassRoom> roomList, String classDate) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("roomList", roomList);
		param.put("classDate", classDate);

		return classRoomDao.getUnTimeDataPsp(param);
	}



	/**
	 * 空闲教室查询
	 */
	@Override
	public Map<String, Object> idleRoomListData(String startTime,
                                                String endTime, String time, String end, String roomLoc,
                                                String roomNum, String weekDay, int limit, int page, List<Map<String, Object>> buildingAuth) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(startTime == null || "".equals(startTime) || time == null || "".equals(time) || endTime == null || "".equals(endTime) || end == null || "".equals(end)) {
			result.put("code", 0);
			result.put("msg", "");
			result.put("count", 0);
			result.put("data", new ArrayList<Map<String,Object>>());
			return result;
		}
		
		ClassRoom classRoom = new ClassRoom();
		if(buildingAuth.size() <= 0) {
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", 0);
            result.put("data", new ArrayList<>());
            return result;
        }
        if(roomLoc != null && !"".equals(roomLoc)) {

            classRoom.setRoomLoc(roomLoc);
        }else {
            classRoom.setRoomLocList(buildingAuth);
        }
		classRoom.setRoomNum(roomNum);
		classRoom.setStatus(1);
		// 可用季节？是有季节可用还是全部季节
//        int startMonth = Integer.parseInt(startTime.split("-")[1]);
//        int endMonth = Integer.parseInt(endTime.split("-")[1]);
//        // 获取起始与结束的月份
//        List<String> months = new ArrayList<>();
//        if(startMonth > endMonth) {
//            for (int i = startMonth; i <= 12; i++) {
//                months.add(i+"");
//            }
//            for (int i = 1; i <= endMonth; i++) {
//                months.add(i+"");
//            }
//
//        }else {
//            for (int i = startMonth; i <= endMonth; i++) {
//                months.add(i+"");
//            }
//        }
//        String season = getSeasonByMonths(months);
//        String[] seasons = season.split(",");
//        classRoom.setUseSeason(season); // 设置可用季节
		List<ClassRoom> rooms = classRoomDao.getClassRooms(classRoom); // 查出所有教室
//        Iterator<ClassRoom> iterator = rooms.iterator();
//        while (iterator.hasNext()) {
//            ClassRoom next = iterator.next();
//            String thisSeason = next.getUseSeason();
//            String[] thisSeasons = thisSeason.split(",");
//            if(thisSeasons.length >= seasons.length) {
//
//                for (String item: seasons) {
//                    boolean flag = false;
//                    for (String thisItem: thisSeasons) {
//                        if(item.equals(thisItem)) {
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if(!flag) {
//                        iterator.remove();
//                        break;
//                    }
//                }
//            }
//
//        }


		// 查出当前教室的不可用时间和已占用的时间
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("startTime", startTime);
        int startIndex = -1;
        int endIndex = -1;
        param.put("endTime", endTime);
        List<String> weeks = new ArrayList<String>();
//        if(weekDay == null || "".equals(weekDay)) {
            int days = DateUtil.getDays("yyyy-MM-dd", startTime, endTime);
            if(days < 7) {
                startIndex = Integer.parseInt(DateUtil.dateToWeekDay(startTime))-1;
                endIndex = Integer.parseInt(DateUtil.dateToWeekDay(endTime))-1;
                if(startIndex > endIndex) { // 如果起始星期大于姐是星期
                    for (int i = startIndex; i < 7; i++){
						weeks.add(i+"");
                    }
                    for (int i = 0; i <= endIndex; i++){
						weeks.add(i+"");
                    }
                }else{ // 如果不小于，则按顺序排序
                    for (int i = startIndex; i <= endIndex; i++) {
						weeks.add(i+"");
                    }
                }
            }else { // 如果大于一周，计算一周的
                startIndex = 0;
                endIndex = 6;
                for (int i = startIndex; i <= endIndex; i++) {
					weeks.add(i+"");
                }
            }
//        }else {
//            String[] weekArr = weekDay.split(",");
//            for (int i = 0; i < weekArr.length; i++) {
//                weeks.add(weekArr[i]);
//            }
//        }
		if(weekDay != null && !"".equals(weekDay)) {
			String[] weekArr = weekDay.split(",");
			List<String> list = new ArrayList<>();
			for (int i = 0; i < weekArr.length; i++) {
				if(weeks.contains(weekArr[i])) {
					list.add(weekArr[i]);
				}
			}
			weeks = list;
		}
		if(weeks.size() <= 0) {
			result.put("code", 0);
			result.put("msg", "");
			result.put("count", 0);
			result.put("data", 0);
			return result;
		}
        param.put("start", time);
		param.put("end", end);
		param.put("roomNum", roomNum);

//		if(weekDay == null || "".equals(weekDay)) {
//			for (int i = startIndex; i <= endIndex; i++) {
//				weeks.add(i+"");
//			}
//		}else {
//			String[] weekArr = weekDay.split(",");
//			for (int i = 0; i < weekArr.length; i++) {
//				weeks.add(weekArr[i]);
//			}
//		}
		param.put("weeks", weeks);
		// 查询不可用时间
		List<Map<String,Object>> list = classRoomDao.getIdleRoomListData(param);
		List<ClassRoom> roomResult = new ArrayList<ClassRoom>(); //总集合
        // 筛选符合条件的教室
		for (ClassRoom classRoom2 : rooms) {
			String roomId = classRoom2.getRoomId();
			List<String> weekTemp = new ArrayList<String>();
			weekTemp.addAll(weeks);
			for (Map<String,Object> map : list) {
				String roomId2 = map.get("roomId").toString();
				if(roomId2.equals(roomId) && map.get("weekDay") != null) {
					String week = map.get("weekDay").toString(); 
					
					weekTemp.remove(week);
				}
			}
			
			if(weekTemp.size() != 0) {
				String weekString = "";
				for (String index : weekTemp) {
					weekString += WEEK[Integer.parseInt(index)]+", ";
				}
				weekString = weekString.substring(0,weekString.lastIndexOf(","));
				classRoom2.setCreateTime(weekString);
				roomResult.add(classRoom2);
			}else {
				continue;
			}
		}
		com.joysupply.util.PageInfo pageInfo = new com.joysupply.util.PageInfo(page,limit);
        List<ClassRoom> classRooms = new ArrayList<ClassRoom>();
		if(roomResult.size() > 0) {
			if(roomResult.size() < page*limit) {
				classRooms = roomResult.subList(pageInfo.getStart(), roomResult.size());
			}else {
				classRooms = roomResult.subList(pageInfo.getStart(), page*limit);
			}
		}
        
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", roomResult.size());
		result.put("data", classRooms);
		return result;
	}

	/**
	 * 查询教师首页今日课程及课时数
	 */
	@Override
	public List<Map<String, Object>> getProjectInfo(String personCode) {
		return messageDao.getUnreadMessageList(personCode);

//		Map<String,Object> param = new HashMap<>();
//		param.put("personCode", teacherCode);
		// 查看当前登录的人，有什么项目
//		List projectAuth = authorityManageDao.getPersonProjectAuth(teacherCode);
		/**
		 * select DISTINCT a.pCode, a.projectId, a.authType, a.projectName, pm.message_content from
		 * (SELECT pda.p_code pCode, pda.data_code projectId , pda.auth_type authType , pi.project_name projectName
		 * FROM person_data_auth pda
		 * LEFT JOIN project_info pi ON pda.data_code = pi.project_id WHERE pda.auth_type = 2) a
		 * left join person_message pm on a.pCode = pm.person_code
		 * WHERE a.pCode = ''
		 */

		// 正在进行的项目数量
		// 已完成的项目数量
		// 接收到到消息

//		String today = DateUtil.ToDateString(new Date(), "yyyy-MM-dd");
//		Map<String,Object> param = new HashMap<String, Object>();
//		param.put("classDate", today);
//		param.put("teacherCode", personCode);
//		List<Map<String, Object>> list = classRoomDao.getProjectInfo(param);
//
//		int onWayProjectCount = classRoomDao.getOnWayProjectCount(today); // 正在进行的项目数量
//		int finishedProjectCount = classRoomDao.getFinishedProjectCount(today); // 已完成的项目数量
//		int notBeginProjectCount = classRoomDao.getNotBeginProjectCount(today); // 未开始的项目数量
//
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("list", list);
//
//		result.put("onWayProjectCount", onWayProjectCount);
//		result.put("finishedProjectCount", finishedProjectCount);
//		result.put("notBeginProjectCount", notBeginProjectCount);
//
//		return result;
	}



	@Override
	public List<Map<String, String>> getRoomLocByAuth(Map<String, Object> reqmap) {
		
		return classRoomDao.getRoomLocByAuth(reqmap);
	}

//	@Override
//	public String getNowSeason(String month) {
//
//		return classRoomDao.getNowSeason(month);
//	}

//    @Override
//    public String getSeasonByMonths(List<String> months) {
//        return classRoomDao.getSeasonByMonths(months);
//    }

    @Override
    @Transactional
    public Map<String, Object> saveTempPlan(Map<String, Object> map) throws BusinessLevelException {
		try {
			String roomId = map.get("roomId").toString();
			Map<String, Object> room = classRoomDao.getRoom(roomId);
			// 编辑
			if(map.get("id") != null && !"".equals(map.get("id").toString())) {
				classRoomDao.updateTempPlan(map);
				room.put("scheduleId",map.get("id"));
			}else {
				// 新建
				map.put("scheduleId",Constants.GUID());
				map.put("status",1);
				map.put("scheduleType",2);
				map.put("year",map.get("classdate").toString().split("-")[0]);
				room.put("scheduleId",map.get("scheduleId"));
				map.put("gradeId",1);
				classRoomDao.saveTempPlan(map);
			}

            return room;
        }catch (Exception e){
            System.out.println(e.toString());
            throw  new  BusinessLevelException(e.toString());

        }
    }

    @Override
    public Integer getNowTimeIsAble(String classdate, String startTime) {
	    Map<String,Object> param = new HashMap<>();
	    param.put("classdate",classdate);
	    param.put("startTime",startTime);
        String id = classManageService.isPlan(param);
        if(id != null && !"".equals(id)) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<String> getIsPlanTeacherCode(Map<String, Object> param) {
        return classRoomDao.getIsPlanTeacherCode(param);
    }

	@Override
	public List<ClassRoom> existList(List<Map<String, Object>> list) {
		return classRoomDao.existList(list);
	}

	@Override
	public void importClassRoom(List<Map<String, Object>> list) {
		// 封装数据
		classRoomDao.importClassRoom(list);
	}

	@Override
	public int getOnWayProjectCount(Map doMap) {
		try {
            int doingNum = classRoomDao.getOnWayProjectCount(doMap);
            return doingNum;
        } catch (Exception e) {
			throw new BusinessLevelException(e.toString());
		}
	}

    @Override
    public int getFinishedProjectCount(Map doMap) {
        try {
            int doneNum = classRoomDao.getFinishedProjectCount(doMap);
            return doneNum;
        } catch (Exception e) {
            throw new BusinessLevelException(e.toString());
        }
    }

	@Override
	public Map<String, Object> getTempPlan(String id) {

		return classRoomDao.getTempPlan(id);
	}
}
