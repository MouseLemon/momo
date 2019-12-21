package com.joysupply.controller;

import com.joysupply.util.FileUtil;
import com.joysupply.util.MyHttpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileController
 * @Author MaZhuli
 * @Date 2018/11/06 11:31
 * @Description 文件上传controller
 * @Version 1.0
 **/
@Controller
@RequestMapping("/file")
public class FileController {
    private Log log = LogFactory.getLog(getClass());

    @RequestMapping("/uploadSingleFile")
    @ResponseBody
    public Map<String, Object> uploadSingleFile(HttpServletRequest request) {
        String indexpath = request.getParameter("indexpath");
        String fileUrl = "";
        try {
            fileUrl = FileUtil.fileUploadSingle(request, "file", indexpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("state", "SUCCESS");
        map.put("url", fileUrl);
        return map;
    }

    @RequestMapping("/uploadMultipleFile")
    @ResponseBody
    public Map<String, Object> uploadMultipleFile(HttpServletRequest request) {
        String indexpath = request.getParameter("indexpath");
        String fileUrl = "";
        String filename = "";
        try {
            Map<String, String> urlsMap = FileUtil.fileUploadMultiple(request, indexpath);
            for (String url : urlsMap.keySet()) {
                fileUrl += (url + ",");
                filename += (urlsMap.get(url) + ",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("state", "SUCCESS");
        map.put("url", fileUrl.substring(0, fileUrl.length() - 1));
        map.put("name", filename.substring(0, filename.length() - 1));
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 下载附件
     * @Date 2018/11/6 9:49
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/downLoadAccessory", method = RequestMethod.GET)
    public void downLoadAccessory(@RequestParam Map map, HttpServletRequest request, HttpServletResponse response) {
        String url = map.get("url").toString();
        String addName = map.get("addName").toString();
        String root = System.getProperty("user.dir");
        File file = new File(root + url);
        if (!file.exists()) {
            log.error("文件未找到!");
        } else {
            try {
                if (MyHttpUtil.isMSBrowser(request)) {
                    response.setHeader("content-disposition", "attachment;filename="
                            + URLEncoder.encode(addName, "UTF-8"));
                } else {
                    response.setHeader("content-disposition", "attachment;filename="
                            + new String(addName.getBytes("UTF-8"), "ISO-8859-1"));
                }
                FileInputStream in = new FileInputStream(file);
                OutputStream out = response.getOutputStream();
                byte buffer[] = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                log.error("文件未找到!");
            }
        }
    }
}
