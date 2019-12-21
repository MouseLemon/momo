package com.joysupply.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joysupply.service.CourseScheduleService;

/**
 * 项目排课controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/courseSchedule")
public class CourseScheduleController extends BaseController implements CourseScheduleService {
	@Autowired
	private CourseScheduleService courseScheduleService;
	private Log log =LogFactory.getLog(getClass());
}
