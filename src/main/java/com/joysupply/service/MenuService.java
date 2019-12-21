package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.Menu;
import com.joysupply.entity.RoleMenu;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

/**
 * 菜单数据层接口
 *
 * @author Administrator
 */
public interface MenuService {
    /**
     * @Author MaZhuli
     * @Description 根据用户获取菜单列表
     * @Date 2018/11/19 16:28
     * @Param [user]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    List<Menu> getMenuListByUser(SystemUser user)throws BusinessLevelException;
    /**
     * @Author MaZhuli
     * @Description 获取所有菜单列表
     * @Date 2018/11/27 19:31
     * @Param []
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    List<Menu> getAllMenus() throws BusinessLevelException;
}
