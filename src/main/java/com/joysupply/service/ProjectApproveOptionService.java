package com.joysupply.service;

import com.joysupply.entity.ProjectApproveOption;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;
import com.joysupply.util.ResultData;

import java.util.List;
import java.util.Map;

/**
 * 项目业务层
 */
public interface ProjectApproveOptionService {


    ResultData<List<Map<String, Object>>> preApproved(Map<String, Object> map) throws BusinessLevelException;
    ResultData<List<Map<String, Object>>> afterApproved(Map<String, Object> map) throws BusinessLevelException;

    ResultData<List<Map<String, Object>>> queryOpioninList(Map<String, Object> map) throws BusinessLevelException;

    OpResult auditProject(List<ProjectApproveOption> projectList,List<ProjectApproveOption> changeList,boolean isApproved) throws BusinessLevelException;
}
