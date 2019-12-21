package com.joysupply.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.CourseFeeDao;
import com.joysupply.service.CourseFeeService;

@Service("courseFeeService")
public class CourseFeeServiceImpl extends BaseService implements CourseFeeService {
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private CourseFeeDao courseFeeDao;
}
