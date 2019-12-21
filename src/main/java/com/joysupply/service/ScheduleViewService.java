package com.joysupply.service;

import com.joysupply.exception.BusinessLevelException;

import java.util.List;
import java.util.Map;

/**
 * 教室课表视图service
 * @author zxt
 *
 * 2018年11月14日-下午2:08:25
 */
public interface ScheduleViewService {
	
	/**
	 * 获取视图
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getWeekSchedule(Map<String, Object> param) throws BusinessLevelException;

	/**
	 * @Author Dreamer
	 * @Description 获取教师名称
	 * @Date 上午 10:54 2018/12/15 0015
	 * @Param [teacher_name]
	 * @return java.lang.String
	 **/
	String getMyName(String teacher_name) throws BusinessLevelException;

}
