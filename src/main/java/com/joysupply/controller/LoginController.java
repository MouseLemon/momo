package com.joysupply.controller;

import com.joysupply.entity.Menu;
import com.joysupply.entity.SystemUser;
import com.joysupply.service.FuncService;
import com.joysupply.service.LoginService;
import com.joysupply.service.MenuService;
import com.joysupply.service.MessageService;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController extends BaseController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private FuncService funcService;
    @Autowired
    private MessageService messageService;

    private Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("login/index");
    }

    @RequestMapping(value = "/login/login", method = RequestMethod.POST)
    @ResponseBody
    public OpResult queryUserByParam(SystemUser systemUser, HttpServletRequest request) {
        log.debug(systemUser);
        SystemUser user = loginService.queryUserByParam(systemUser);

        if (user == null) {
            return new OpResult("账户名或密码不正确");
        }
        /*List<Map<String, Object>> msgList = messageService.getUnreadMessageList(user.getPersonCode());
        user.setMsgCount(msgList.size());
        user.setMsgList(msgList);*/
        HttpSession session = request.getSession();
        List<Menu> menuList = menuService.getMenuListByUser(user);
        if(menuList.size() <= 0) {
            return new OpResult("您没有权限，请联系管理员") ;
        }
        List<String> funcList = funcService.getFuncListByUser(user);
        session.setAttribute("menuList", menuList);
        session.setAttribute("funcList", funcList);
        session.setAttribute("systemUser", user);
        return new OpResult() {
            {
                setMessage("登录成功");
            }
        };
    }

    @RequestMapping(value = "/login/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("systemUser");
        session.removeAttribute("menuList");
        session.removeAttribute("funcList");
        session.removeAttribute("token");
        return "redirect:/";
    }

}
