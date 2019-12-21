package com.joysupply.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.ClassRoomScheduleDao;
import com.joysupply.service.ClassRoomScheduleService;

/**
 * 教室课表视图service实现
 * @author zxt
 *
 * 2018年11月14日-下午2:09:37
 */
@Service
public class ClassRoomScheduleServiceImpl extends BaseService implements ClassRoomScheduleService {

	@Autowired
	private ClassRoomScheduleDao classRoomScheduleDao;
	
	/**
	 * 查询周视图
	 */
	@Override
	public List<Map<String, Object>> getWeekSchedule(Map<String, Object> param) {
		
		
		return classRoomScheduleDao.getWeekSchedule(param);
	}

}
