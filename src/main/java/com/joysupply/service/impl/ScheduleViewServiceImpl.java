package com.joysupply.service.impl;

import com.joysupply.dao.ScheduleViewDao;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ScheduleViewService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author Dreamer
 * @Description 教师视图ServiceImpl
 * @Date 上午 10:53 2018/12/15 0015
 * @Param 
 * @return 
 **/
@Service
public class ScheduleViewServiceImpl extends BaseService implements ScheduleViewService {

	@Autowired
	private ScheduleViewDao scheduleViewDao;

	private Log log = LogFactory.getLog(getClass());
	/**
	 * 查询周视图
	 */
	@Override
	public List<Map<String, Object>> getWeekSchedule(Map<String, Object> map) {
		try {
			return scheduleViewDao.getWeekSchedule(map);
		} catch (RuntimeException e) {
			e.printStackTrace();
			log.error("获取教室视图记录:" + e.getMessage());
			throw new BusinessLevelException("获取教室视图记录", e);
		}
	}

	@Override
	public String getMyName(String teacher_name) {
		try {
			return scheduleViewDao.getMyName(teacher_name);
		} catch (RuntimeException e) {
			e.printStackTrace();
			log.error("获取教室视图记录:" + e.getMessage());
			throw new BusinessLevelException("获取教室视图记录", e);
		}
	}

}
