package com.joysupply.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SsoUtils
 * @Description 单点登录工具类
 * @Author ZhangXingTong
 * @Date 2019/2/27 10:41
 * @Version 1.0
 **/
public class SsoUtils {

    /**
     * @Description: 判断是否具有二期登录权限
     * @Param: [personCode]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: ZhangXingTong
     * @Date: 2019/2/27 10:42
     */
    public static Map<String, Object> hasRole(String personCode, String url) {
        Map<String, Object> resultMap = new HashMap<>(2);
        String result = HttpUtill.getRequest(url+"?personCode="+personCode);
        if(StringUtils.isEmpty(result)) {
            resultMap.put("success",false);
            resultMap.put("msg","报名系统未启动，请联系管理员");
            return resultMap;
        }
        JSONObject jsonObject1 = JSONUtil.toJsonObject(result);
        boolean flag = (boolean)jsonObject1.get("success");
        if(flag) {
            resultMap.put("success",true);
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","您不具备登录权限，请联系管理员");
        }
        return resultMap;
    }
}
