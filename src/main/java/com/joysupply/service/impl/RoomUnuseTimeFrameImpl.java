package com.joysupply.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joysupply.dao.ProjectSchedulePlanDao;
import com.joysupply.dao.RoomUnuseTimeFrameDao;
import com.joysupply.entity.ProjectSchedulePlan;
import com.joysupply.entity.RoomUnuseTimeFrame;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.RoomUnuseTimeFrameService;
import com.joysupply.util.Constants;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;

@Service("roomUnuseTimeFrameService")
public class RoomUnuseTimeFrameImpl extends BaseService implements RoomUnuseTimeFrameService {
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private RoomUnuseTimeFrameDao roomUnuseTimeFrameDao;
	@Autowired
	private ProjectSchedulePlanDao projectSchedulePlanDao;
	@Override
	public List<RoomUnuseTimeFrame> getRoomUnuseTimeFrameListByRoomID(Map<String, Object> map) {
		if(map.get("roomId") == null || "".equals(map.get("roomId").toString())) {
			return new ArrayList<RoomUnuseTimeFrame>();
		}
		List<RoomUnuseTimeFrame> result = roomUnuseTimeFrameDao.getRoomUnuseTimeFrameListByRoomID(map);
		return result;
	}
	
	
	@Transactional
	@Override
	public OpResult updateRoomUnuseTimeFrame(Map<String, Object> map) throws BusinessLevelException {
		if(map.get("roomId").toString() == null || "".equals(map.get("roomId").toString())) {
			return new OpResult("传入的roomId为空");
		}
		if(map.get("roomDate").toString() == null || "".equals(map.get("roomDate").toString())) {
			return new OpResult("传入的日期为空");
		}
		if(map.get("startTime").toString() == null || "".equals(map.get("startTime").toString())) {
			return new OpResult("传入的开始时间为空");
		}
		if(map.get("endTime").toString() == null || "".equals(map.get("endTime").toString())) {
			return new OpResult("传入的结束时间为空");
		}
		try {
			if(map.get("frameId").toString() != null && !"".equals(map.get("frameId").toString())) {//不可用变为可用
				//1.判断是否是已排课的不可用
				ProjectSchedulePlan projectSchedulePlan = new ProjectSchedulePlan();
				projectSchedulePlan = projectSchedulePlanDao.getProjectSchedulePlanByScheduleId(map);
				if(projectSchedulePlan !=null) {
					//说明该教室在这个时段有课,则不能设置为可用
					return new OpResult("该教室在该时段已有课,不能设置为可用");
				}
				//2.设置为可用
				roomUnuseTimeFrameDao.deleteRoomUnuseTimeFram(map);
			}else {//可用变为不可用
				//1.根据日期和roomId判断该教室有没有课
				ProjectSchedulePlan projectSchedulePlan = new ProjectSchedulePlan();
				projectSchedulePlan = projectSchedulePlanDao.getProjectSchedulePlanByScheduleId(map);
				if(projectSchedulePlan !=null) {
					//说明有课
					return new OpResult("该教室在该时段已有课,不能设置为不可用");
				}
				//2.判断该时段是否有教室占用
				Calendar dateSplit =  DateUtil.dateSplit(map.get("roomDate").toString());
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("startTime", map.get("startTime").toString());
				paramMap.put("endTime", map.get("endTime").toString());
				paramMap.put("year",dateSplit.get(Calendar.YEAR));
				paramMap.put("month",dateSplit.get(Calendar.MONTH) + 1);
				paramMap.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
				List<RoomUnuseTimeFrame> roomIsAvailbleList = roomUnuseTimeFrameDao.getRoomUnuseTimeFrameListByRoomID(paramMap);
				if(roomIsAvailbleList !=null && roomIsAvailbleList.size()>0) {
					return new OpResult("该教室已被占用，请重新选择教室！");
				}
				//3.可以设置为不可用
				map.put("frameId",Constants.GUID());
				map.put("week",DateUtil.dateToWeek(map.get("roomDate").toString()));
				map.put("weekDay",DateUtil.dateToWeekDay(map.get("roomDate").toString()));
				map.put("year",dateSplit.get(Calendar.YEAR));
				map.put("month",dateSplit.get(Calendar.MONTH) + 1);
				map.put("day",dateSplit.get(Calendar.DAY_OF_MONTH));
				roomUnuseTimeFrameDao.saveRoomUnuseTimeFram(map);
			}
			return new OpResult();
		}catch (RuntimeException ex) {
			log.error(ex.getMessage());
			throw new BusinessLevelException("修改教室时间段可用或不可用失败",ex);
		}
	}
	
}
