package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.Role;
import com.joysupply.entity.RoleMenu;
import com.joysupply.entity.dto.RoleMenuDto;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

/**
 * 角色数据层接口
 *
 * @author Administrator
 */
public interface RoleService {
    List<Role> getRoleList() throws BusinessLevelException;

    OpResult updateMenuByRole(Map map) throws BusinessLevelException;

    List<String> getPersonRoles(String personCode) throws BusinessLevelException;

    Map<String, Object> getRoleListInPage(Map map) throws BusinessLevelException;

    OpResult addRole(Role role) throws BusinessLevelException;

    Role getRole(Role role) throws BusinessLevelException;

    OpResult updRole(Role role) throws BusinessLevelException;

    OpResult delRole(Role role) throws RuntimeException;

    List<String> getRoleMenus(Role role) throws RuntimeException;
}
