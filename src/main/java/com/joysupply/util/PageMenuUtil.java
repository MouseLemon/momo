package com.joysupply.util;

import com.joysupply.entity.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PageMenuUtil
 * @Author MaZhuli
 * @Date 2018/10/27 15:10
 * @Description 页面菜单工具类-
 * @Version 1.0
 **/
public class PageMenuUtil {
    /**
     * @Author MaZhuli
     * @Description 获取导航栏按钮
     * @Date 2018/10/29 13:40
     * @Param [allMenus]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    public static List<Menu> getIndexMenu(List<Menu> allMenus) {
        List<Menu> indexMenu = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (menu.getParentCode() == null || menu.getParentCode().equals("")) {
                indexMenu.add(menu);
            }
        }
        return indexMenu;
    }

    /**
     * @Author MaZhuli
     * @Description 获取某个导航栏按钮下所有菜单夹
     * @Date 2018/10/29 13:41
     * @Param [allMenus, parentCode]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    public static List<Menu> getMenuDir(List<Menu> allMenus, String parentCode) {
        List<Menu> menuDir = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (menu.getParentCode().equals(parentCode)) {
                menuDir.add(menu);
            }
        }
        return menuDir;
    }

    /**
     * @Description:  获取某个url下的menus
     * @Param: [allMenus, url]
     * @return: java.util.List<com.joysupply.entity.Menu>
     * @Author: ZhangXingTong
     * @Date: 2019/2/27 14:46
     */
    public static List<Menu> getMenuDirByUrl(List<Menu> allMenus, String url) {
        String parentCode = "";
        for (Menu menu : allMenus) {
            if (menu.getMenuUrl().equals(url)) {
                parentCode = menu.getParentCode();
                // 查询是否还有上级
                for (Menu menu1 : allMenus) {
                    if(parentCode.equals(menu1.getMenuCode())) {
                        parentCode = menu1.getParentCode();
                        break;
                    }
                }

                break;
            }
        }
        return getMenuDir(allMenus,parentCode);
    }

    /**
     * @Author MaZhuli
     * @Description 获取菜单夹下所有菜单按钮
     * @Date 2018/10/29 13:42
     * @Param [allMenus, menuDir]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    public static List<Menu> getMenuList(List<Menu> allMenus, List<Menu> menuDir) {
        List<Menu> menuList = new ArrayList<>();
        for (Menu menu : menuDir) {
            List<Menu> menuListItem = getMenuDir(allMenus, menu.getMenuCode());
            menuList.addAll(menuListItem);
        }
        return menuList;
    }

    /**
     * @Author MaZhuli
     * @Description 获取当个菜单夹下所有菜单
     * @Date 2018/11/29 13:45
     * @Param [allMenus, menuDir]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    public static List<Menu> getMenuListByDir(List<Menu> allMenus, Menu menu) {
        List<Menu> menuList = new ArrayList<>();
        List<Menu> menuListItem = getMenuDir(allMenus, menu.getMenuCode());
        menuList.addAll(menuListItem);
        return menuList;
    }
}
