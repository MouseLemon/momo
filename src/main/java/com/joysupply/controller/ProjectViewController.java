package com.joysupply.controller;

import com.alibaba.fastjson.JSONObject;
import com.joysupply.entity.Project;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ProjectService;
import com.joysupply.service.ProjectViewService;
import com.joysupply.service.SettingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ProjectViewController
 * @Author MaZhuli
 * @Date 2018/11/14 15:18
 * @Description 项目视图Controller
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/scheduleView")
public class ProjectViewController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ProjectViewService projectViewService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SettingService settingService;

    @RequestMapping("/projectView")
    public ModelAndView getProjectViewSchedule(@RequestParam Map map) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", user);
        List<Project> projectList = projectService.getProjectListNoPage(user);
        resultMap.put("projectList", projectList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy/MM");
        Calendar calendar = Calendar.getInstance();
        String currentDate = sdf.format(calendar.getTime());
        String currentMonth = sdfm.format(calendar.getTime());
        resultMap.put("currentDate", currentDate);
        resultMap.put("currentMonth", currentMonth);
        Integer week = calendar.get(Calendar.WEEK_OF_YEAR);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        resultMap.put("weekOrMonth", 1);
        resultMap.put("week", week);
        resultMap.put("year", year);
        resultMap.put("month", month);
        resultMap.put("day", day);
        resultMap.put("weekLable", /*year + "/" + (month + 1) + "/" + day + */"第" + week + "周");
        return new ModelAndView("scheduleView/projectView/projectView", resultMap);
    }

    @RequestMapping(value = "/projectViewList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> projectViewList(@RequestBody Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap();
        //各种判断初始值
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy/MM");
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        Integer weekOrMonth = Integer.parseInt(map.get("weekOrMonth").toString());
        Integer beforeOrAfter = Integer.parseInt(map.get("beforeOrAfter").toString());
        Integer week = Integer.parseInt(map.get("week").toString());
        Integer year = Integer.parseInt(map.get("year").toString());
        Integer month = Integer.parseInt(map.get("month").toString());
        Integer day = Integer.parseInt(map.get("day").toString());
        if (weekOrMonth == 1) {
            //周
            if (beforeOrAfter == -1) {
                //上周
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                resultMap.put("currentDate", sdf.format(calendar.getTime()));
                resultMap.put("currentMonth", sdfm.format(calendar.getTime()));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            } else if (beforeOrAfter == 1) {
                //下周
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                resultMap.put("currentDate", sdf.format(calendar.getTime()));
                resultMap.put("currentMonth", sdfm.format(calendar.getTime()));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            } else {
                //本周
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                resultMap.put("currentDate", sdf.format(calendar.getTime()));
                resultMap.put("currentMonth", sdfm.format(calendar.getTime()));
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            }
            resultMap.put("weekLable", /*year + "/" + (month + 1) + "/" + day + */"第" + week + "周");
            resultMap.put("weekOrMonth", 1);
            resultMap.put("week", week);
            resultMap.put("year", year);
            resultMap.put("month", month);
            resultMap.put("day", day);
            try {
                resultMap.put("weekCalendar", MyCalendar.weekCalendar(year, month, day));
            } catch (ParseException ex) {
                log.error("获取项目周信息" + ex.getMessage());
            }
            try {
                Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
                resultMap.put("timeTable", timeTable);
                List<Map<String, Object>> projectViewList = projectViewService.getProjectView(map);
                resultMap.put("projectViewList", projectViewList);
            } catch (BusinessLevelException ex) {
                log.error("获取项目排课信息失败" + ex.getMessage());
            }
        } else {
            //月
            if (beforeOrAfter == -1) {
                //上月
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                resultMap.put("currentDate", sdf.format(calendar.getTime()));
                resultMap.put("currentMonth", sdfm.format(calendar.getTime()));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            } else if (beforeOrAfter == 1) {
                //下月
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                resultMap.put("currentDate", sdf.format(calendar.getTime()));
                resultMap.put("currentMonth", sdfm.format(calendar.getTime()));
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            } else {
                //本月
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                resultMap.put("currentDate", sdf.format(calendar.getTime()));
                resultMap.put("currentMonth", sdfm.format(calendar.getTime()));
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            }
            resultMap.put("weekLable", /*year + "/" + (month + 1) + "/" + day + */"第" + week + "周");
            resultMap.put("weekOrMonth", 2);
            resultMap.put("week", week);
            resultMap.put("year", year);
            resultMap.put("month", month);
            resultMap.put("day", day);
            try {
                resultMap.put("monthCalendar", MyCalendar.monthCalendar(year, month, day));
            } catch (ParseException ex) {
                log.error("获取项目日历信息" + ex.getMessage());
            }
            try {
                List<Map<String, Object>> projectViewList = projectViewService.getProjectViewB(map);
                resultMap.put("projectViewList", projectViewList);
            } catch (BusinessLevelException ex) {
                log.error("获取项目排课信息" + ex.getMessage());
            }
        }
        return resultMap;
    }

    @RequestMapping(value = "/getCalendar", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getCalendar(@RequestBody Map<String, Object> map) throws ParseException {
        Map resultMap = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy/MM");
        Date currentDate = new Date();
        try {
            Integer weekOrMonth = Integer.parseInt(map.get("weekOrMonth").toString());
            if (weekOrMonth == 1) {
                currentDate = sdf.parse(map.get("currentDate").toString());
            }else{
                currentDate = sdfm.parse(map.get("currentDate").toString());
            }
        } catch (ParseException ex) {
            log.error("日期解析错误" + ex.getMessage());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        String currentDatestr = sdf.format(calendar.getTime());
        String currentMonthstr = sdfm.format(calendar.getTime());
        Integer week = calendar.get(Calendar.WEEK_OF_YEAR);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        resultMap.put("week", week);
        resultMap.put("year", year);
        resultMap.put("month", month);
        resultMap.put("day", day);
        resultMap.put("weekLable", /*year + "/" + (month + 1) + "/" + day + */"第" + week + "周");
        resultMap.put("currentDate", currentDatestr);
        resultMap.put("currentMonth", currentMonthstr);
        return resultMap;
    }
}
