package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.*;

/**
 * 菜单数据层接口
 *
 * @author Administrator
 */
public interface MenuDao {
    /**
     * 根据角色查询对应的菜单列表数据
     *
     * @return
     */
    List<Menu> getMenuListByRoleCode(RoleMenu roleMenu);

    /**
     * 根据menuCode查询menu_func表的list是否有记录
     *
     * @return
     */
    List<MenuFunc> getMenuFuncList(MenuFunc menuFunc);

    /**
     * 插入menu_func表
     *
     * @param map
     * @return
     * @throws RuntimeException
     */
    int saveMenusFunc(Map<String, Object> map) throws RuntimeException;

    /**
     * 根据menuCode删除menu_func表对应记录
     *
     * @param menuCode
     * @return
     * @throws RuntimeException
     */
    int deleteMenuFuncByMenuCode(String menuCode) throws RuntimeException;

    /**
     * 根据用户查询菜单列表
     */
    List<Menu> getMenuListByUser(SystemUser user) throws RuntimeException;

    /**
     * 查询教师的菜单列表
     */
    List<Menu> getMenuListByTeacher(SystemUser user) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取所有菜单列表
     * @Date 2018/11/27 17:43
     * @Param []
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    List<Menu> getAllMenus() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取用户当前可用菜单
     * @Date 2018/12/13 9:25
     * @Param [user]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    List<Menu> getShortCutMenuByUser(SystemUser user) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除快捷菜单
     * @Date 2018/12/13 14:50
     * @Param [map]
     * @Return int
     **/
    int delShortCutMenuByUser(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 保存快捷菜单
     * @Date 2018/12/13 14:50
     * @Param [map]
     * @Return int
     **/
    int addShortCutMenuByUser(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取导航链接
     * @Date 2018/12/13 17:30
     * @Param [menu]
     * @Return java.lang.String
     **/
    String getMenuIndexUrl(Menu menu) throws RuntimeException;
}
