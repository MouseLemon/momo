package com.joysupply.controller;

import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.*;
import com.joysupply.util.AllTimetable;
import com.joysupply.util.MyHttpUtil;
import com.joysupply.util.ResultData;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author：WangHao
 * @Create：2018-11-12 16:10
 * @Description：课程表视图
 * @Program：byoa-service
 * @Version：1.0
 **/
@Controller
@RequestMapping(value = "/scheduleView")
public class ScheduleViewController extends BaseController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ScheduleViewService scheduleViewService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ClassManageService classManageService;

    @Autowired
    private AuthorityManageService authorityManageService;

    private Logger logger = Logger.getLogger(ScheduleViewController.class);
    private String teacherAuth = "13";

    @RequestMapping("/teacherView")
    public ModelAndView getProjectViewSchedule(@RequestParam Map map) {
        String userType = getUser().getUserType();
        String teacher_code = getUser().getPersonCode();

        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", user);

        // 查teacher列表
        List<Map<String, Object>> teacherList;

        Map temp = new HashMap();
        String person_code = user.getPersonCode();
        // 如果是老师权限进来的,只能显示当前老师的
        if (teacherAuth.equals(userType)) {
            teacherList = teacherService.getTeachers(person_code);
            resultMap.put("teacherList", teacherList);
            String myName = scheduleViewService.getMyName(teacher_code);
            resultMap.put("myName", myName);
        } else {
            temp.put("personCode", person_code);
            teacherList = authorityManageService.getTeachersAuth(temp);
            resultMap.put("teacherList", teacherList);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy/MM");
        Calendar calendar = Calendar.getInstance();
        Object classDate = map.get("classDate");
        try{
            // 如果有时间传过来
            if(classDate != null) {
                String replace = classDate.toString().replace("-", "/");
                calendar.setTime(sdf.parse(replace));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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
        resultMap.put("weekLable", "第" + week + "周");
        resultMap.put("userType", userType);
        resultMap.put("teacher_code", teacher_code);

        return new ModelAndView("scheduleView/teacherView", resultMap);
    }

    @RequestMapping(value = "/getSchedule", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getSchedule(@RequestBody Map<String, Object> map) {
        String userType = map.get("userType").toString();
        if (teacherAuth.equals(userType)){
            map.put("teacher_code", map.get("teacher_code1").toString());
        }

        Map<String, Object> resultMap = new HashMap();
        //各种判断初始值
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy/MM");
        SimpleDateFormat sdfs= new SimpleDateFormat("yyyy-MM-dd");
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
                // 判断week
                // 如果选择的是周日，则周减一
                day = calendar.get(Calendar.DAY_OF_MONTH);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                String startDate = sdfs.format(calendar.getTime());
                map.put("startDate", startDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String endDate = sdfs.format(calendar.getTime());
                map.put("endDate", endDate);
            }
            resultMap.put("weekLable", "第" + week + "周");
            resultMap.put("weekOrMonth", 1);
            resultMap.put("week", week);
            resultMap.put("year", year);
            resultMap.put("month", month);
            resultMap.put("day", day);
            try {
                resultMap.put("weekCalendar", MyCalendar.weekCalendar(year, month, day));
            } catch (ParseException ex) {
                logger.error("获取项目周信息" + ex.getMessage());
            }
            try {
                Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
                resultMap.put("timeTable", timeTable);
                // 根据老师查所有课
                List<Map<String, Object>> teacherViewList = scheduleViewService.getWeekSchedule(map);
                resultMap.put("teacherViewList", teacherViewList);
            } catch (BusinessLevelException ex) {
                logger.error("获取项目排课信息失败" + ex.getMessage());
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
            resultMap.put("weekLable", "第" + week + "周");
            resultMap.put("weekOrMonth", 2);
            resultMap.put("week", week);
            resultMap.put("year", year);
            resultMap.put("month", month);
            resultMap.put("day", day);
            try {
                resultMap.put("monthCalendar", MyCalendar.monthCalendar(year, month, day));
            } catch (ParseException ex) {
                logger.error("获取项目日历信息" + ex.getMessage());
            }
            try {
                List<Map<String, Object>> teacherViewList = scheduleViewService.getWeekSchedule(map);
                resultMap.put("teacherViewList", teacherViewList);
            } catch (BusinessLevelException ex) {
                logger.error("获取项目排课信息" + ex.getMessage());
            }
        }
        return resultMap;
    }

    /**
     * 课程详情
     *
     * @param schedule_id
     * @return
     */
    @RequestMapping(value = "teacherViewInfo", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView teacherViewInfo(@RequestParam String schedule_id) {
        Map result = classManageService.getProjectSchedulePlanSheet(schedule_id);
        String start_time = result.get("start_time").toString();
        String end_time = result.get("end_time").toString();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date stime = dateFormat.parse(start_time);
            Date etime = dateFormat.parse(end_time);
            // 这样得到的差值是微秒级别
            long diff = etime.getTime() - stime.getTime(); 
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            String longTime = hours + ":" + minutes;
            result.put("longTime", longTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ModelAndView("scheduleView/teacherViewInfo", result);
    }

    /**
     * 总课表视图
     * WangYueLei
     * @return
     */
    @RequestMapping(value = "/courseSearchView", method = RequestMethod.GET)
    public ModelAndView generalTimetable(String startTime,String endTime,String roomLoc, String roomNum, String roomName) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        resultMap.put("startTime", startTime);
        resultMap.put("endTime", endTime);
        resultMap.put("data", settingService.queryTabelAllHead());
        Map map = new HashMap();
        map.put("personCode",getUser().getPersonCode());

        List personBuildingAuth = authorityManageService.getPersonBuildingAuth(map);
        
        resultMap.put("roomLoc", personBuildingAuth);
        resultMap.put("roomLocSel", roomLoc);
        resultMap.put("roomNum", roomNum);
        resultMap.put("roomName", roomName);
        return new ModelAndView("scheduleView/courseSearch", resultMap);
    }

    /**
     * @Author Dreamer
     * @Description 导出总课表
     * @Date 上午 10:52 2018/12/15 0015
     * @Param [param, response]
     * @return void
     **/
    @RequestMapping(value = "/excel" , method = RequestMethod.GET)
    public void exportExcelCommon(@RequestParam Map<String,Object> param , HttpServletRequest request, HttpServletResponse response )throws IOException {
        String fileName = ""+param.get("startTime")+"到"+param.get("endTime")+"总课表.xls";
        List<Map<String,Object>> headList = settingService.queryTimeByTableAll();
        Map map= new HashMap();
        map.put("personCode",getUser().getPersonCode());
        List personBuildingAuth = authorityManageService.getPersonBuildingAuth(map);
        List<Map<String,Object>> bodyList = new ArrayList<>();
        if(personBuildingAuth != null && personBuildingAuth.size()>0){
            param.put("personBuildingAuth",personBuildingAuth);
            bodyList = settingService.queryAllTableBodyParam(param);
        }
        HSSFWorkbook wb = AllTimetable.table(headList,bodyList);
        if (MyHttpUtil.isMSBrowser(request)) {
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
        } else {
            response.setHeader("content-disposition", "attachment;filename="
                    + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        }
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "/queryCurseSearchData", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryCurseSearchData(@RequestParam Map<String, Object> map) {
        try {
            //看是否有楼群数据
            List<Map<String,Object>> personBuildingAuth = new ArrayList<Map<String,Object>>();
            if(map.get("roomLoc")==null||map.get("roomLoc").toString().equals("")){
                map.put("personCode",getUser().getPersonCode());
                personBuildingAuth = authorityManageService.getPersonBuildingAuth(map);
            }else{
                personBuildingAuth.add(new HashMap<String,Object>(){
                    {
                        put("dataCode",map.get("roomLoc").toString());
                    }
                });
            }
            if(personBuildingAuth.size()<1){
                return new ResultData<>();
            }
            map.put("personBuildingAuth",personBuildingAuth);
            ResultData result = settingService.queryTabelData(map);
            return result;
        } catch (BusinessLevelException ex) {
            return new ResultData<>(ex.getMessage());
        }

    }

    /**
     * 调课
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/changeClass", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView changeClass(@RequestParam Map map) {
        return null;
    }

}
