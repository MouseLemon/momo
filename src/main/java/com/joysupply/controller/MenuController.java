package com.joysupply.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joysupply.entity.Func;
import com.joysupply.entity.Menu;
import com.joysupply.entity.MenuFunc;
import com.joysupply.entity.RoleMenu;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.FuncService;
import com.joysupply.service.MenuService;
import com.joysupply.util.OpResult;

/**
 * 菜单管理
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/api/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private FuncService funcService;
    private Log log = LogFactory.getLog(getClass());
}
