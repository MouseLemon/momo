package com.joysupply.controller;

import com.joysupply.entity.Menu;
import com.joysupply.entity.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public abstract class BaseController {
    @Autowired
    private HttpSession session;

    protected SystemUser getUser() {
        return (SystemUser) session.getAttribute("systemUser");
    }

    protected List<Menu> getMenu() {
        return (List<Menu>) session.getAttribute("menuList");
    }

    protected List<String> getFunc() {
        return (List<String>) session.getAttribute("funcList");
    }

    protected Map<String, Object> getMenuMap() {
        return (Map<String, Object>) session.getAttribute("menuMap");
    }

}
