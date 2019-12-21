package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.JobType;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.OrganizeType;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

/**
 * 组织结构数据层接口
 *
 * @author Administrator
 */
public interface OrganizeStructService {
    List<Map> queryFirstOrganizeList() throws BusinessLevelException;

    List<Map> queryOrganizeTree() throws BusinessLevelException;

    OpResult addOrganizeStruct(OrganizeStruct organizeStruct) throws BusinessLevelException;

    OpResult delOrganizeStruct(OrganizeStruct organizeStruct) throws BusinessLevelException;

    OpResult updOrganizeStruct(OrganizeStruct organizeStruct) throws BusinessLevelException;

    OpResult updateDepartment(OrganizeStruct organizeStruct) throws BusinessLevelException;

    List<JobType> getJobTypeList() throws BusinessLevelException;

    List<OrganizeType> getOrganizeTypeList() throws BusinessLevelException;

    OrganizeStruct getOrganizeStruct(String organizeCode) throws BusinessLevelException;

    Map<String, Object> getOrganizePeopleList(Map map) throws BusinessLevelException;

    List<OrganizeStruct> getSon(OrganizeStruct organizeStruct) throws BusinessLevelException;

    OrganizeStruct getParent(OrganizeStruct organizeStruct) throws BusinessLevelException;

    OrganizeStruct getOrganizeStructByPersonCode(String personCode) throws BusinessLevelException;

    List<OrganizeStruct> getAll() throws BusinessLevelException;

    List<OrganizeStruct> getProjectDepList() throws BusinessLevelException;

    /**
     * 根据当前用户获取项目部
     * @param personCode
     * @return
     */
    List<OrganizeStruct> listOrganizeByPersonCode(String personCode);
}
