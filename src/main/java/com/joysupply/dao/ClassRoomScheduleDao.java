package com.joysupply.dao;

import java.util.List;
import java.util.Map;

/**
 * 教室课表视图dao
 * @author zxt
 *
 * 2018年11月14日-下午2:10:29
 */
public interface ClassRoomScheduleDao {
	
	
	/**
	 * 查询周视图
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getWeekSchedule(Map<String, Object> param);

}
