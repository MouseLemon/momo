package com.joysupply.controller;

import com.joysupply.entity.Menu;
import com.joysupply.entity.SystemUser;
import com.joysupply.service.LoginService;
import com.joysupply.service.MenuService;
import com.joysupply.util.HttpUtill;
import com.joysupply.util.JWTUtils;
import com.joysupply.util.SsoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SsoController
 * @Description 一期二期切换
 * @Author ZhangXingTong
 * @Date 2019/2/27 11:03
 * @Version 1.0
 **/
@RequestMapping("/sso")
@RestController
public class SsoController extends BaseController {

    @Value("${hasRoleUrl}")
    private String hasRoleUrl;
    @Value("${bmIndexUrl}")
    private String bmIndexUrl;
    @Autowired
    private MenuService menuService;
    @Autowired
    private LoginService loginService;


    @RequestMapping("/jumpToBm")
    public Map<String, Object> jumpToBm() {
        // 先判断是否有权限
        Map<String, Object> map = SsoUtils.hasRole(getUser().getPersonCode(), hasRoleUrl);
        if((boolean)map.get("success")) {
            // 生成token
            Date isDate = new Date();
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MINUTE, 10);
            try {
                String token = JWTUtils.createToken(getUser(), isDate, instance.getTime());
                map.put("url",bmIndexUrl+"?token="+token    );
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @RequestMapping("/online")
    public Map<String, Object> online(String personCode) {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("success",true);
        SystemUser user = loginService.queryUserByPersonCode(personCode);
        List<Menu> menuListByUser = menuService.getMenuListByUser(user);
        if(menuListByUser.size() <= 0) {
            resultMap.put("success", false);
            resultMap.put("msg", "您没有权限，请联系管理员");
        }
        return resultMap;
    }
}
