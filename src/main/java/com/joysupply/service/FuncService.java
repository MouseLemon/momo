package com.joysupply.service;

import java.util.List;

import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;

/**
 * 功能数据层接口
 *
 * @author Administrator
 */
public interface FuncService {

    /**
     * @Author MaZhuli
     * @Description 获取用户按钮列表
     * @Date 2018/12/7 15:14
     * @Param [user]
     * @Return java.util.List<Func>
     **/
    List<String> getFuncListByUser(SystemUser user) throws BusinessLevelException;
    /**
     * @Author MaZhuli
     * @Description 获取角色按钮列表
     * @Date 2018/12/8 9:44
     * @Param [role]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getFuncListByRole(Role role) throws BusinessLevelException;
    /**
     * @Author MaZhuli
     * @Description 获取所有按钮列表
     * @Date 2018/12/7 18:41
     * @Param []
     * @Return java.util.List<com.joysupply.entity.Func>
     **/
    List<Func> getAllFuncList() throws BusinessLevelException;
    /**
     * @Author MaZhuli
     * @Description 根据菜单获取按钮列表
     * @Date 2018/12/8 15:40
     * @Param [menuCode]
     * @Return java.util.List<com.joysupply.entity.Func>
     **/
    List<Func> getFuncListByMenu(Menu menu);
}
