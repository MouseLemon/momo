package com.joysupply.controller;

import com.joysupply.entity.JobType;
import com.joysupply.entity.Menu;
import com.joysupply.entity.Role;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.OrganizePeopleService;
import com.joysupply.service.RoleService;
import com.joysupply.service.SystemUserService;
import com.joysupply.util.MapObj;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SystemController
 * @Author MaZhuli
 * @Date 2018/11/20 14:33
 * @Description 用户Controller
 * @Version 1.0
 **/
@Controller
@RequestMapping("/systemUser")
public class SystemController extends BaseController {
    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private OrganizePeopleService organizePeopleService;

    /**
     * @Author MaZhuli
     * @Description 跳转创建账户页面
     * @Date 2018/11/20 14:47
     * @Param [map]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/addSystemUserPage", method = RequestMethod.GET)
    public ModelAndView addSystemUserPage(@RequestParam Map map) {
        return new ModelAndView("system/systemUser/addSystemUserPage", map);
    }

    @RequestMapping(value = "/modifyPwdPage", method = RequestMethod.GET)
    public ModelAndView modifyPwdPage(@RequestParam Map map) {
        return new ModelAndView("system/systemUser/modifyPwdPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 跳转修改账户页面
     * @Date 2018/11/20 14:48
     * @Param [map]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/updSystemUserPage", method = RequestMethod.GET)
    public ModelAndView updSystemUserPage(@RequestParam String personCode) {
        SystemUser systemUser = systemUserService.getSystemUser(personCode);
        Map<String, Object> map = MapObj.objectToMap(systemUser);
        return new ModelAndView("system/systemUser/editSystemUserPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 快捷菜单管理
     * @Date 2018/12/13 11:21
     * @Param [personCode]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/addShortCutMenu", method = RequestMethod.GET)
    public ModelAndView addShortCutMenu() {
        Map map = new HashMap();
        try {
            List<Menu> shortCutMenuList = organizePeopleService.getShortCutMenuByUser(getUser());
            List<Menu> allShortCutMenusList = organizePeopleService.getAllShortCutMenusByUser(getUser());
            List<String> shortCutList = new ArrayList<>();
            for (Menu menu1 : shortCutMenuList) {
                if (!shortCutList.contains(menu1.getMenuCode())) {
                    shortCutList.add(menu1.getMenuCode());
                }
            }
            for (Menu menu : allShortCutMenusList) {
                if (shortCutList.contains(menu.getMenuCode())) {
                    menu.setIsSelected(1);
                }
            }
            map.put("shortCutList", shortCutList);
            map.put("allShortCutMenusList", allShortCutMenusList);
        } catch (
                BusinessLevelException ex)

        {
            log.error("获取快捷菜单失败" + ex.getMessage());
            map.put("allShortCutMenusList", null);
        }
        return new

                ModelAndView("system/systemUser/addShortCutMenu", map);

    }

    @RequestMapping(value = "/updShortCutMenus", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updShortCutMenus(@RequestBody Map map) {
        map.put("personCode", getUser().getPersonCode());
        try {
            return organizePeopleService.updShortCutMenus(map);
        } catch (BusinessLevelException ex) {
            log.error("更新快捷菜单失败" + ex.getMessage());
            return new OpResult("更新快捷菜单失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 添加账户
     * @Date 2018/11/20 14:43
     * @Param [user]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/addSystemUser", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addSystemUser(@RequestBody SystemUser user) {
        try {
            if (user.getUserType() == null || "".equals(user.getUserType())) {

                user.setUserType("1");
            }
        	/*
            Wangyuelei 用户名是否重复
             */
            String userName = user.getUserName();
            SystemUser systemUser = systemUserService.getUserByUserName(userName);
            if (systemUser != null) {
                return new OpResult("用户名已经存在");
            }
            return systemUserService.addSystemUser(user);
        } catch (BusinessLevelException ex) {
            log.error("添加账户失败" + ex.getMessage());
            return new OpResult("添加账户失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 修改账户
     * @Date 2018/11/20 14:43
     * @Param [user]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updSystemUser", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updSystemUser(@RequestBody SystemUser user) {
        try {
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            SystemUser systemUser = systemUserService.getUserByUserName(userName);
            if (systemUser != null && !user.getPersonCode().equals(systemUser.getPersonCode())) {
                if (!systemUser.getPersonCode().equals(personCode)) {
                    if (systemUser != null) {
                        return new OpResult("用户名已经存在");
                    }
                }
            }
            return systemUserService.updSystemUser(user);
        } catch (BusinessLevelException ex) {
            log.error("修改账户失败" + ex.getMessage());
            return new OpResult("修改账户失败");
        }
    }

    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ResponseBody
    public OpResult modifyPwd(@RequestBody Map map) {
        try {
            SystemUser user = getUser();
            String personCode = user.getPersonCode();
            String password = user.getPassword();
            String oldPassword = map.get("oldPassword").toString();
            if (!password.equals(oldPassword)) {
                return new OpResult("原始密码错误,请重新输入!");
            }
            String newPassword = map.get("newPassword").toString();
            String checkPassword = map.get("checkPassword").toString();
            if (!newPassword.equals(checkPassword)) {
                return new OpResult("两次输入密码不一致,请重新输入!");
            }
            SystemUser newUser = new SystemUser();
            newUser.setPersonCode(personCode);
            newUser.setPassword(newPassword);
            return systemUserService.modifyPwd(newUser);
        } catch (BusinessLevelException ex) {
            log.error("修改账户失败" + ex.getMessage());
            return new OpResult("修改密码失败");
        }
    }

}
