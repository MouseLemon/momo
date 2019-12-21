package com.joysupply.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.github.pagehelper.util.StringUtil;
import com.joysupply.entity.Menu;
import com.joysupply.entity.SystemUser;
import com.joysupply.service.FuncService;
import com.joysupply.service.LoginService;
import com.joysupply.service.MenuService;
import com.joysupply.util.JWTUtils;
import com.joysupply.util.PageMenuUtil;
import com.joysupply.util.SsoUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Component
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

    @Value("${ssoUrl}")
    private String ssoUrl;
    @Value("${hasRoleUrl}")
    private String hasRoleUrl;
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private FuncService funcService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -1);
        response.setHeader("Pragma", "no-cache");
        String currentUri = request.getRequestURI().toString();
        String type = request.getHeader("X-Requested-With");
        if (currentUri.equals("/") || currentUri.equals("/login/logout") || currentUri.equals("/login/login")) {
            return true;
        }
        HttpSession session = request.getSession();
        SystemUser systemUser = (SystemUser) session.getAttribute("systemUser");
        String serviceUrl = request.getParameter("serviceUrl");
        String token  = request.getParameter("token");
         if (null == systemUser) {
            if(serviceUrl == null || "".equals(serviceUrl)) {
                // 判断二期是否启动
                Map<String, Object> map = SsoUtils.hasRole(null, hasRoleUrl);
                if(!(boolean)map.get("success")) {
                    return setHeader(response, type);
                }
                // 二期重定向携带参数，不为空表示二期也没登陆
                String noToken = request.getParameter("noToken");
                // 第一次，未重定向到二期，重定向到二期判断登录
                if(StringUtil.isEmpty(noToken) && StringUtil.isEmpty(token)) {
                    String requestURL = request.getRequestURL().toString();
                    String queryString = request.getQueryString();
                    if(StringUtil.isEmpty(queryString)) {
                        response.sendRedirect(ssoUrl+"?serviceUrl="+requestURL);
                    }else {
                        response.sendRedirect(ssoUrl+"?serviceUrl="+requestURL+"?"+queryString);
                    }
                    return false;
                }
            }else {
                if(serviceUrl.contains("?")) {
                    response.sendRedirect(serviceUrl+"&noToken=no");
                }else {
                    response.sendRedirect(serviceUrl+"?noToken=no");
                }
                return false;
            }
        }

        // 如果是二期请求过来的
        if(serviceUrl != null && !"".equals(serviceUrl)) {
            // 生产token
            Date isDate = new Date();
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MINUTE, 1);
            String thisToken = JWTUtils.createToken(systemUser, isDate, instance.getTime());
            if(serviceUrl.contains("?")) {
                response.sendRedirect(serviceUrl+"&token="+thisToken);
            }else {
                response.sendRedirect(serviceUrl+"?token="+thisToken);
            }
            return false;
        }else if(!StringUtil.isEmpty(token)) {
            // 封装参数到session中
            return setParameters(response, type, session, token, request);

        }else if(systemUser == null){
            return setHeader(response, type);
        }else {
            return true;
        }
    }

    private boolean setParameters(HttpServletResponse response, String type, HttpSession session, String token, HttpServletRequest request) throws IOException {
        SystemUser systemUser;
        Object isDecrypt = session.getAttribute(token);
        if(isDecrypt != null) {
            return true;
        }
        try {
            Map<String, Claim> verifyToken = JWTUtils.verifyToken(token);
            String personCode = verifyToken.get("personCode").asString();
            session.setAttribute(token,1);
            systemUser = loginService.queryUserByPersonCode(personCode);
            List<Menu> menuList = menuService.getMenuListByUser(systemUser);
            if(menuList.size() <= 0) {
                return setHeader(response,type);
            }
            List<String> funcList = funcService.getFuncListByUser(systemUser);
            session.setAttribute("menuList", menuList);
            session.setAttribute("funcList", funcList);
            session.setAttribute("systemUser", systemUser);
            List<Menu> indexMenu = PageMenuUtil.getIndexMenu(menuList);
            List<Menu> menuDir = PageMenuUtil.getMenuDir(menuList, request.getRequestURI());
            List<Menu> menuListChild = PageMenuUtil.getMenuList(menuList, menuDir);
            Map menuMap = new HashMap();
            menuMap.put("indexMenu", indexMenu);
            menuMap.put("menuDir", menuDir);
            menuMap.put("menuList", menuListChild);
            session.setAttribute("menuMap", menuMap);
            return true;
        }catch (Exception e) {
            return setHeader(response, type);
        }
    }

    private boolean setHeader(HttpServletResponse response, String type) throws IOException {
        if (StringUtils.equals("XMLHttpRequest", type)) {
            response.setHeader("SESSIONSTATUS", "TIMEOUT");
            response.setHeader("CONTEXTPATH", "/");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        } else {
            response.sendRedirect("/");
            return false;
        }
    }

}
