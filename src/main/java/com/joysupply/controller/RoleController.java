package com.joysupply.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joysupply.entity.*;
import com.joysupply.service.FuncService;
import com.joysupply.util.MapObj;
import com.joysupply.util.PageMenuUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.joysupply.entity.dto.RoleMenuDto;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.MenuService;
import com.joysupply.service.RoleService;
import com.joysupply.util.OpResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * 角色管理
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private FuncService funcService;

    /**
     * @Author MaZhuli
     * @Description 跳转角色列表页面
     * @Date 2018/11/27 15:43
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/roleListPage", method = RequestMethod.GET)
    public ModelAndView roleListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", getUser());
        return new ModelAndView("system/roleList", resultMap);
    }

    /**
     * @Author MaZhuli
     * @Description 获取角色列表
     * @Date 2018/11/27 13:55
     * @Param []
     * @Return java.util.List<com.joysupply.entity.Role>
     **/
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getRoleList(@RequestParam Map map) {
        try {
            return roleService.getRoleListInPage(map);
        } catch (BusinessLevelException ex) {
            log.error("获取角色列表失败:" + ex.getMessage());
            return null;
        }
    }

    /**
     * @Author MaZhuli
     * @Description 调整添加角色页面
     * @Date 2018/11/27 15:42
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/addRolePage", method = RequestMethod.GET)
    public ModelAndView addRolePage() {
        Map map = new HashMap();
        try {
            List<Role> roleList = roleService.getRoleList();
            map.put("roleList", roleList);
        } catch (BusinessLevelException ex) {
            log.error("获取角色列表失败:" + ex.getMessage());
            return null;
        }
        return new ModelAndView("system/addRolePage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 调整编辑角色页面
     * @Date 2018/11/27 15:42
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editRolePage", method = RequestMethod.GET)
    public ModelAndView editRolePage(@RequestParam String roleCode) {
        Role role = new Role();
        role.setRoleCode(roleCode);
        Map map = new HashMap();
        try {
            role = roleService.getRole(role);
            map.putAll(MapObj.objectToMap(role));
            List<Role> roleList = roleService.getRoleList();
            map.put("roleList", roleList);
        } catch (BusinessLevelException ex) {
            log.error("获取角色列表失败:" + ex.getMessage());
        }
        return new ModelAndView("system/editRolePage", map);
    }
    /**
     * @Author MaZhuli
     * @Description 编辑角色菜单按钮
     * @Date 2018/12/10 14:09
     * @Param [roleCode]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editRoleMenus", method = RequestMethod.GET)
    public ModelAndView editRoleMenus(@RequestParam String roleCode) {
        Role role = new Role();
        role.setRoleCode(roleCode);
        Map map = new HashMap();
        map.put("roleCode", roleCode);
        try {
            List<String> list = roleService.getRoleMenus(role);
            map.put("roleMenuList", list);
            List<Menu> allMenus = menuService.getAllMenus();
            List<Menu> indexMenu = PageMenuUtil.getIndexMenu(allMenus);
            List<Menu> menuDir = new ArrayList<>();
            List<Menu> menuList = new ArrayList<>();
            List<String> funcList = funcService.getFuncListByRole(role);
            for (Menu menu : indexMenu) {
                List<Menu> menuDirItem = PageMenuUtil.getMenuDir(allMenus, menu.getMenuCode());
                int countDir = 0;
                for (Menu menu1 : menuDirItem) {
                    List<Menu> menuListItem = PageMenuUtil.getMenuListByDir(allMenus, menu1);
                    int count = 0;
                    for (Menu menu2 : menuListItem) {
                        if (list.contains(menu2.getMenuCode())) {
                            count++;
                            menu2.setIsSelected(1);
                        } else {
                            menu2.setIsSelected(0);
                        }
                        List<Func> funcDirList = funcService.getFuncListByMenu(menu2);
                        int funcCount = 0;
                        for (Func func : funcDirList) {
                            if (funcList.contains(func.getFuncCode())) {
                                func.setIsSelected(1);
                                funcCount++;
                            } else {
                                func.setIsSelected(0);
                            }
                        }
                        if (funcCount == funcDirList.size()) {
                            menu2.setAllFuncSelected(1);
                        } else {
                            menu2.setAllFuncSelected(0);
                        }
                        menu2.setFuncList(funcDirList);
                    }
                    if (count == menuListItem.size()) {
                        menu1.setIsSelected(1);
                        countDir++;
                    } else {
                        menu1.setIsSelected(0);
                    }
                    menuList.addAll(menuListItem);
                }
                if (countDir == menuDirItem.size()) {
                    menu.setIsSelected(1);
                } else {
                    menu.setIsSelected(0);
                }
                menuDir.addAll(menuDirItem);
            }
            map.put("indexMenu", indexMenu);
            map.put("menuDir", menuDir);
            map.put("menuList", menuList);
        } catch (BusinessLevelException ex) {
            log.error("获取角色列表失败:" + ex.getMessage());
        }
        return new ModelAndView("system/editRoleMenusPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 保存角色
     * @Date 2018/11/27 15:42
     * @Param [Role]
     * @Return java.util.Map<java.lang.String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addRole(@RequestBody Role role) {
        try {
            return roleService.addRole(role);
        } catch (BusinessLevelException ex) {
            log.error("保存角色失败:" + ex.getMessage());
            return new OpResult("保存角色失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 修改角色
     * @Date 2018/11/27 16:12
     * @Param [role]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updRole", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updRole(@RequestBody Role role) {
        try {
            return roleService.updRole(role);
        } catch (BusinessLevelException ex) {
            log.error("保存角色失败:" + ex.getMessage());
            return new OpResult("保存角色失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除角色
     * @Date 2018/11/27 16:11
     * @Param [role]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delRole(@RequestBody Role role) {
        try {
            return roleService.delRole(role);
        } catch (BusinessLevelException ex) {
            log.error("删除角色失败:" + ex.getMessage());
            return new OpResult("删除角色失败");
        }
    }
    /**
     * @Author MaZhuli
     * @Description 修改角色菜单配置
     * @Date 2018/12/10 15:11
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updateMenuByRole", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateMenuByRole(@RequestBody Map map) {
        try {
            log.debug(map);
            return roleService.updateMenuByRole(map);
        } catch (BusinessLevelException ex) {
            log.error("修改角色菜单配置失败:" + ex.getMessage());
            return new OpResult("修改角色菜单配置失败");
        }
    }
}
