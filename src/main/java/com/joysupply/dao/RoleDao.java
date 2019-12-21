package com.joysupply.dao;

import java.util.List;
import java.util.Map;


import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.Role;
import com.joysupply.entity.RoleMenu;
import com.joysupply.entity.dto.RoleMenuDto;

/**
 * 角色数据层接口
 *
 * @author Administrator
 */
public interface RoleDao {
    /**
     * 查询角色列表
     *
     * @return
     */
    List<Role> getRoleList();

    /**
     * 添加角色
     *
     * @param role
     * @return
     * @throws RuntimeException
     */
    int saveRole(Role role) throws RuntimeException;

    /**
     * 编辑角色
     *
     * @param role
     * @return
     * @throws RuntimeException
     */
    int updateRole(Role role) throws RuntimeException;

    /**
     * 根据roleCode查询menu_role表的list
     *
     * @param roleMenu
     * @return
     */
    List<RoleMenu> getRoleMenuList(RoleMenu roleMenu);

    /**
     * 插入menu_role表
     *
     * @param roleMenuDto
     * @return
     * @throws RuntimeException
     */
    int saveRoleMenu(RoleMenuDto roleMenuDto) throws RuntimeException;

    /**
     * 根据roleCode删除enu_role表对应记录
     *
     * @param String
     * @return
     * @throws RuntimeException
     */
    int deleteRoleMenuByRoleCode(String roleCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 为人员添加角色
     * @Date 2018/11/19 11:03
     * @Param [personRoleList]
     * @Return int
     **/
    int savePersonRole(List<Map<String, Object>> personRoleList) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除人员角色
     * @Date 2018/11/19 11:03
     * @Param [personRoleList]
     * @Return int
     **/
    int delPersonRole(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取人员角色
     * @Date 2018/12/14 14:04
     * @Param [personCode]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getPersonRoles(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 保存角色
     * @Date 2018/11/27 15:45
     * @Param [role]
     * @Return int
     **/
    int addRole(Role role) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改角色
     * @Date 2018/11/27 16:11
     * @Param [role]
     * @Return int
     **/
    int updRole(Role role) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取角色
     * @Date 2018/11/27 16:00
     * @Param [role]
     * @Return com.joysupply.entity.Role
     **/
    Role getRole(Role role) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除角色
     * @Date 2018/11/27 16:03
     * @Param [role]
     * @Return int
     **/
    int delRole(Role role) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取角色菜单
     * @Date 2018/11/27 17:53
     * @Param [role]
     * @Return java.util.List<com.joysupply.entity.RoleMenu>
     **/
    List<String> getRoleMenus(Role role) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据角色获取人员列表
     * @Date 2018/12/14 14:16
     * @Param [role]
     * @Return java.util.List<java.lang.String>
     **/
    List<OrganizePeople> getPersonByRole(Map map) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取其它财务人员列表
     * @Date 2018/12/22 12:57
     * @Param [map]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getOtherPersonHaveSameRole(Map map) throws RuntimeException;


}
