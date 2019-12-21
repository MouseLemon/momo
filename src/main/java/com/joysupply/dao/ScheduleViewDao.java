package com.joysupply.dao;

import java.util.List;
import java.util.Map;

/**
 * 教室课表视图dao
 * @author zxt
 *
 * 2018年11月14日-下午2:10:29
 */
public interface ScheduleViewDao {

	/**
	 * 查询周视图
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getWeekSchedule(Map<String, Object> map) throws RuntimeException;

	/**
	 * @Author Dreamer
	 * @Description 获取教师名称
	 * @Date 上午 10:54 2018/12/15 0015
	 * @Param [teacher_name]
	 * @return java.lang.String
	 **/
    String getMyName(String teacher_name) throws RuntimeException;
}
