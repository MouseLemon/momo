package com.joysupply.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PageHelperUtil
 * @Author ChenYang
 * @Date 2018/10/11 11:20
 * @Description 分页辅助
 * @Version 1.0
 **/

public class PageHelperUtil {
    /**
     * @Author ChenYang
     * @Description 分页辅助
     * @Date 2018/10/11 14:31
     * @Param [map]
     * @Return java.util.Map<java.lang.String,java.lang.Integer>
     **/
    
    public static Map<String, Integer> getPageInfo(Map<String, Object> map) {
        Map<String, Integer> pageMap = new HashMap<>();
        int page = 1;
        int limit = 10;
        if (map.get("page") != null) {
            page = Integer.parseInt(String.valueOf(map.get("page")));
        }
        if (map.get("limit") != null) {
            limit = Integer.parseInt(String.valueOf(map.get("limit")));
        }
        pageMap.put("page", page);
        pageMap.put("limit", limit);
        return pageMap;
    }
}

