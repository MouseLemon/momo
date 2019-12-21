package com.joysupply.controller;

import com.joysupply.entity.Menu;
import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.SystemUser;
import com.joysupply.service.*;
import com.joysupply.util.PageMenuUtil;
import com.joysupply.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PageController
 * @Author MaZhuli
 * @Date 2018/10/27 17:10
 * @Description 页面跳转controller
 * @Version 1.0
 **/
@Controller
public class PageController extends BaseController {
    @Autowired
    HttpSession session;
    @Autowired
    private OrganizePeopleService organizePeopleService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ClassRoomService classRoomService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/page/{url}")
    public String hello(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String defaultUrl = "";
        //导航栏菜单
        List<Menu> indexMenu = PageMenuUtil.getIndexMenu(getMenu());
        Menu currentMenu = null;
        for (Menu menu : indexMenu) {
            if (menu.getMenuUrl().equals(requestURI)) {
                currentMenu = menu;
                break;
            }
        }
        List<Menu> menuDir = PageMenuUtil.getMenuDir(getMenu(), currentMenu.getMenuCode());
        List<Menu> menuList = PageMenuUtil.getMenuList(getMenu(), menuDir);
        Map menuMap = new HashMap();
        menuMap.put("indexMenu", indexMenu);
        menuMap.put("menuDir", menuDir);
        menuMap.put("menuList", menuList);
        session.setAttribute("menuMap", menuMap);
        if (request.getParameter("defaultUrl") != null && !request.getParameter("defaultUrl").equals("")) {
            defaultUrl = request.getParameter("defaultUrl");
        } else {
            defaultUrl = menuList.get(0).getMenuUrl();
        }
        return "redirect:" + defaultUrl;
    }

    @RequestMapping(value = "/page")
    public String hello() {
        List<Menu> indexMenu = PageMenuUtil.getIndexMenu(getMenu());
        List<Menu> menuDir = PageMenuUtil.getMenuDir(getMenu(), indexMenu.get(0).getMenuCode());
        List<Menu> menuList = PageMenuUtil.getMenuList(getMenu(), menuDir);
        Map menuMap = new HashMap();
        menuMap.put("indexMenu", indexMenu);
        menuMap.put("menuDir", menuDir);
        menuMap.put("menuList", menuList);
        session.setAttribute("menuMap", menuMap);
        String defaultUrl = "";
        defaultUrl = menuList.get(0).getMenuUrl();
        return "redirect:" + defaultUrl;
    }

    @RequestMapping(value = "/index/projectAudit", method = RequestMethod.GET)
    public ModelAndView projectAuditHomePage() {
        Map resultMap = new HashMap();
        SystemUser user = getUser();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", user);
        OrganizePeople organizePeople = organizePeopleService.getOrganizePeople(user.getPersonCode());
        List<Menu> shortCutList = organizePeopleService.getShortCutMenuByUser(user);
        resultMap.put("shortCutList", shortCutList);
        String name = organizePeople.getName();
        resultMap.put("name", name);
        List<Map<String, Object>> unreadMessageList = messageService.getAllMessageList(user.getPersonCode());
        resultMap.put("unreadMessageList", unreadMessageList);
        List<Menu> menuList = getMenu();
        Integer hasSendMenu = 0;
        for (int i = 0;i<menuList.size();i++) {
            if (menuList.get(i).getMenuUrl() != null && menuList.get(i).getMenuUrl().equals("/unSend/sendPage")){
                hasSendMenu = 1;
                break;
            }
        }
        resultMap.put("hasSendMenu",hasSendMenu);
        return new ModelAndView("index/projectAudit", resultMap);
    }

    @RequestMapping(value = "/index/classroom", method = RequestMethod.GET)
    public ModelAndView classroomHomePage() {
        Map resultMap = new HashMap();
        SystemUser user = getUser();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", user);
        OrganizePeople organizePeople = organizePeopleService.getOrganizePeople(user.getPersonCode());
        String name = organizePeople.getName();
        resultMap.put("name", name);
        List<Menu> shortCutList = organizePeopleService.getShortCutMenuByUser(user);
        resultMap.put("shortCutList", shortCutList);
        List<Map<String, Object>> unreadMessageList = messageService.getAllMessageList(user.getPersonCode());
        resultMap.put("unreadMessageList", unreadMessageList);
        // 项目数量
        String personCode = getUser().getPersonCode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        Map doMap = new HashMap();
        doMap.put("personCode", personCode);
        doMap.put("today", today);
        // 正在进行的项目数量 : 当前personcode进行的数量
        /*int doingNum = classRoomService.getOnWayProjectCount(doMap);
        resultMap.put("doingNum", doingNum);
        // 已完成的项目数量 : 当前personcode完成的数量
        int doneNum = classRoomService.getFinishedProjectCount(doMap);
        resultMap.put("doneNum", doneNum);*/
        List<Menu> menuList = getMenu();
        Integer hasSendMenu = 0;
        for (int i = 0;i<menuList.size();i++) {
            if (menuList.get(i).getMenuUrl() != null && menuList.get(i).getMenuUrl().equals("/unSend/sendPage")){
                hasSendMenu = 1;
                break;
            }
        }
        resultMap.put("hasSendMenu",hasSendMenu);
        return new ModelAndView("index/classroom", resultMap);
    }

    @RequestMapping(value = "/index/teacher", method = RequestMethod.GET)
    public ModelAndView teacherHomePage() {
        Map resultMap = new HashMap();
        SystemUser user = getUser();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", user);
        List<Menu> shortCutList = organizePeopleService.getShortCutMenuByUser(user);
        resultMap.put("shortCutList", shortCutList);
        return new ModelAndView("index/teacher", resultMap);
    }

    @RequestMapping(value = "/index/project", method = RequestMethod.GET)
    public ModelAndView projectHomePage() {
        Map resultMap = new HashMap();
        SystemUser user = getUser();
        OrganizePeople organizePeople = organizePeopleService.getOrganizePeople(user.getPersonCode());
        String name = organizePeople.getName();
        resultMap.put("name", name);
        Map map = new HashMap();
        List<String> personRoles = roleService.getPersonRoles(user.getPersonCode());
        if (!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") && !personRoles.contains("1902")) {
            map.put("personCode", user.getPersonCode());
        }
        map.put("isOver", 1);
        Map<String, Object> overList = projectService.getProjectList(map);
        resultMap.put("overSize", overList.get("count").toString());
        if (!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") && !personRoles.contains("1902")) {
            map.put("personCode", user.getPersonCode());
        }
        map.put("isOver", 0);
        Map<String, Object> notOverList = projectService.getProjectList(map);
        resultMap.put("notOverSize", notOverList.get("count").toString());
        resultMap.putAll(getMenuMap());
        resultMap.put("user", user);
        List<Menu> shortCutList = organizePeopleService.getShortCutMenuByUser(user);
        resultMap.put("shortCutList", shortCutList);
        List<Map<String, Object>> unreadMessageList = messageService.getAllMessageList(user.getPersonCode());
        resultMap.put("unreadMessageList", unreadMessageList);
        List<Menu> menuList = getMenu();
        Integer hasSendMenu = 0;
        for (int i = 0;i<menuList.size();i++) {
            if (menuList.get(i).getMenuUrl() != null && menuList.get(i).getMenuUrl().equals("/unSend/sendPage")){
                hasSendMenu = 1;
                break;
            }
        }
        resultMap.put("hasSendMenu",hasSendMenu);
        return new ModelAndView("index/project", resultMap);
    }
}
