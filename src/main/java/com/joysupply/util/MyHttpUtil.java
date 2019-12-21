package com.joysupply.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MyHttpUtil
 * @Author MaZhuli
 * @Date 2018/12/14 10:31
 * @Description 请求工具类
 * @Version 1.0
 **/
public class MyHttpUtil {
    private static String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};

    public static boolean isMSBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal))
                return true;
        }
        return false;
    }

}
