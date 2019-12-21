package com.joysupply.service.impl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.ProjectSchedulePlanDao;
import com.joysupply.service.ProjectSchedulePlanService;

@Service("projectSchedulePlanService")
public class ProjectSchedulePlanImpl extends BaseService implements ProjectSchedulePlanService {
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private ProjectSchedulePlanDao projectSchedulePlanDao;
	
}
