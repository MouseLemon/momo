package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.Menu;
import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

/**
 * 组织结构人的数据接口层
 *
 * @author dagu
 */
public interface OrganizePeopleService {
    OpResult addOrganizePeople(OrganizePeople organizePeople, String[] roleArr) throws BusinessLevelException;

    OpResult updOrganizePeople(OrganizePeople organizePeople, String[] roleArr) throws BusinessLevelException;

    OpResult delOrganizePeople(OrganizePeople organizePeople) throws BusinessLevelException;

    OrganizePeople getOrganizePeople(String personCode) throws BusinessLevelException;

    List<OrganizePeople> getOrganizePeopleListNoPage(OrganizeStruct organizeStruct) throws BusinessLevelException;

    List<OrganizePeople> getOrganizePeopleHaveAuditRole(Map map) throws BusinessLevelException;

    boolean isTelephoneExist(String telphone) throws BusinessLevelException;

    OrganizePeople getOrganizePeopleByTelephone(String telphone) throws BusinessLevelException;

    List<Menu> getAllShortCutMenusByUser(SystemUser user) throws BusinessLevelException;

    List<Menu> getShortCutMenuByUser(SystemUser user) throws BusinessLevelException;

    OpResult updShortCutMenus(Map map) throws BusinessLevelException;

	List<Map<String, Object>> getAllPersonUser();

    int getSystemUserByTel(String telphone);
}
