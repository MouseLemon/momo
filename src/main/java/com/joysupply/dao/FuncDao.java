package com.joysupply.dao;

import java.util.List;

import com.joysupply.entity.*;

/**
 * 功能数据层接口
 *
 * @author Administrator
 */
public interface FuncDao {
    /**
     * 查询功能列表数据
     *
     * @return
     */
    List<Func> getFuncList();

    /**
     * 根据菜单编码menu_code查询对应的功能列表数据
     *
     * @param menuFunc
     * @return
     */
    List<Func> getMenuFuncListByMenuCode(MenuFunc menuFunc);

    /**
     * @Author MaZhuli
     * @Description 获取用户按钮列表
     * @Date 2018/12/7 15:18
     * @Param [user]
     * @Return java.util.List<com.joysupply.entity.Func>
     **/
    List<String> getFuncListByUser(SystemUser user);

    /**
     * @Author MaZhuli
     * @Description 获取所有按钮列表
     * @Date 2018/12/7 18:43
     * @Param []
     * @Return java.util.List<com.joysupply.entity.Func>
     **/
    List<Func> getAllFuncList() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取角色按钮列表
     * @Date 2018/12/8 11:27
     * @Param [role]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getFuncListByRole(Role role) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据菜单获取按钮
     * @Date 2018/12/8 15:43
     * @Param [menu]
     * @Return java.util.List<com.joysupply.entity.Func>
     **/
    List<Func> getFuncListByMenu(Menu menu) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据角色删除按钮
     * @Date 2018/12/8 16:20
     * @Param [roleCode]
     * @Return int
     **/
    int delFuncByRoleCode(String roleCode) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 保存角色按钮
     * @Date 2018/12/8 16:22
     * @Param [list]
     * @Return int
     **/
    int saveRoleFunc(List<Func> list) throws RuntimeException;
}
