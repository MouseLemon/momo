package com.joysupply.controller;

import com.joysupply.entity.TeachResearchRoom;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.MapObj;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.joysupply.service.TeachResearchRoomService;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Dreamer
 * @Description 教研室
 * @Date 上午 10:38 2018/12/15 0015
 * @Param
 * @return
 **/
@Controller
@RequestMapping(value = "/api/teachResearchRoom")
public class TeachResearchRoomController extends BaseController {
    @Autowired
    private TeachResearchRoomService teachResearchRoomService;
    private Log log = LogFactory.getLog(getClass());

    /**
     * @Author Dreamer
     * @Description 教研室首页
     * @Date 上午 10:38 2018/12/15 0015
     * @Param []
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("index/classroom", resultMap);
    }

    /**
     * @Author MaZhuli
     * @Description 教研室列表页面
     * @Date 2018/11/3 14:30
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/researchOfficeListPage")
    public ModelAndView researchOfficeListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("researchOffice/researchOfficeList", resultMap);
    }

    @RequestMapping(value = "/getResearchOfficeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getResearchOfficeList(@RequestParam Map map) {
        try {
            return teachResearchRoomService.getResearchOfficeList(map);
        } catch (BusinessLevelException e) {
            log.error("获取教研室列表:" + e.getMessage());
            return null;
        }
    }
    @RequestMapping(value = "/addResearchOfficePage", method = RequestMethod.GET)
    public ModelAndView addResearchOfficePage(@RequestParam Map map) {
        return new ModelAndView("researchOffice/addResearchOfficePage", map);
    }

    @RequestMapping(value = "/editResearchOfficePage", method = RequestMethod.GET)
    public ModelAndView editResearchOfficePage(@RequestParam String officeCode) {
        TeachResearchRoom teachResearchRoom = teachResearchRoomService.getResearchOffice(officeCode);
        return new ModelAndView("researchOffice/editResearchOfficePage", MapObj.objectToMap(teachResearchRoom));
    }

    @RequestMapping(value = "/addResearchOffice", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addResearchOffice(@RequestBody TeachResearchRoom teachResearchRoom) {
        try {
            teachResearchRoom.setCreater(getUser().getUserName());
            log.debug(teachResearchRoom);
            String office_name = teachResearchRoom.getOfficeName();
            Map map = teachResearchRoomService.selectChecking(office_name);
            if (map != null){
                // 存在该名称的教研室
                return new OpResult("教研室名称已存在");
            } else {
                return teachResearchRoomService.addResearchOffice(teachResearchRoom);
            }
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    @RequestMapping(value = "/updResearchOffice", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updResearchOffice(@RequestBody TeachResearchRoom teachResearchRoom) {
        try {
            log.debug(teachResearchRoom);
            String office_name = teachResearchRoom.getOfficeName();
            Map map = teachResearchRoomService.selectChecking(office_name);
            if (map != null){
                // 存在该名称的教研室
                return new OpResult("教研室名称已存在");
            } else {
                return teachResearchRoomService.updResearchOffice(teachResearchRoom);
            }
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    @RequestMapping(value = "/delResearchOffice", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delResearchOffice(@RequestBody TeachResearchRoom teachResearchRoom) {
        try {
            log.debug(teachResearchRoom);
            return teachResearchRoomService.delResearchOffice(teachResearchRoom);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

}
