package com.joysupply.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.joysupply.service.CourseFeeService;

/**
 * 项目教师-课酬controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/courseFee")
public class CourseFeeController extends BaseController {
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private CourseFeeService courseFeeService;
}
