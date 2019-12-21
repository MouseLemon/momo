package com.joysupply.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joysupply.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.joysupply.entity.DataDictionary;
import com.joysupply.util.DateUtil;

/**
 * 教室课表视图
 * @author zxt
 *
 * 2018年11月14日-下午2:08:04
 */
@RequestMapping("/classRoomSchedule")
@Controller
public class ClassRoomScheduleController extends BaseController{
	
	@Autowired
    private SettingService settingService;

    @Autowired
    private DataDictionaryService dataDictionaryService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private ClassRoomScheduleService classRoomScheduleService;

    @Autowired
    private AuthorityManageService authorityManageService;
	
	@RequestMapping("/scheduleView")
	public ModelAndView scheduleView(String date, String roomLoc, String roomNum, String next, String up) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = getMenuMap();
		result.put("user", getUser());
		result.putAll(map);
        // 查询时间表
        Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
        result.putAll(timeTable);

        // 数据字典room_loc：21
        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", getUser().getPersonCode());
        List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
//        List<DataDictionary> room_locList = dataDictionaryService.getDicItemListNoPage("21");
        result.put("roomLoc", buildingAuth);
        // 获取指定的年月日周期
        Map culm = DateUtil.getCulm(date,next,up,"");
        result.putAll(culm);
        
        
		return new ModelAndView("scheduleView/classRoomScheduleView", result);
	}
	
	
	@RequestMapping(value = "/getSchedule" ,method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getSchedule(String roomLoc, String roomNum, String date, String next, String up) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map culm = DateUtil.getCulm(date,next,up,"");
		result.putAll(culm);
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("year", culm.get("year"));
        param.put("week", culm.get("weeks"));
        param.put("xyStart", culm.get("xyStart"));
        param.put("xyEnd", culm.get("xyEnd"));
        param.put("roomLoc", roomLoc);
        param.put("roomNum", roomNum);
        // 获取周视图
        List<Map<String,Object>> weekSchedule = classRoomScheduleService.getWeekSchedule(param);
        String[] dates = date.split("-");
        // 获取月视图
        param.put("xyStart", culm.get("fristDay"));
        param.put("xyEnd", culm.get("lastDay"));
        List<Map<String,Object>> monthSchedule = classRoomScheduleService.getWeekSchedule(param);
        // 查询时间表
        Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
        result.putAll(timeTable);
        try {
			result.put("monthCalendar", MyCalendar.monthCalendar(Integer.parseInt(dates[0]), Integer.parseInt(dates[1])-1, Integer.parseInt(dates[2])));
		} catch (NumberFormatException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        result.put("weekSchedule", weekSchedule);
        result.put("monthSchedule", monthSchedule);
        
        
        return result;
        
        
	}
	
}
