package com.joysupply.service;

import java.util.List;
import java.util.Map;

/**
 * 教室课表视图service
 * @author zxt
 *
 * 2018年11月14日-下午2:08:25
 */
public interface ClassRoomScheduleService {
	
	/**
	 * 获取视图
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getWeekSchedule(Map<String, Object> param);

}
