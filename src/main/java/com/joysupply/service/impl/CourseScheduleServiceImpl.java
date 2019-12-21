package com.joysupply.service.impl;

 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.CourseScheduleDao;
import com.joysupply.service.CourseScheduleService;

@Service("courseScheduleService")
public class CourseScheduleServiceImpl extends BaseService implements CourseScheduleService {
	@Autowired
	private CourseScheduleDao courseScheduleDao;
	private Log log=LogFactory.getLog(getClass());
	
}
