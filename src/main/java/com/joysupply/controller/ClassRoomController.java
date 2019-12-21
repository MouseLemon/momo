package com.joysupply.controller;

import java.util.*;

import com.joysupply.service.*;
import com.joysupply.util.*;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.joysupply.entity.ClassRoom;
import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.RoomUnuseTimeFrame;
import com.joysupply.exception.BusinessLevelException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 教室管理
 * @author zxt
 *
 * 2018年11月3日-下午2:19:34
 */
@Controller
@RequestMapping(value = "/classRoom")
public class ClassRoomController extends BaseController {
	@Autowired
	private ClassRoomService classRoomService;
	@Autowired
	private RoomUnuseTimeFrameService roomUnuseTimeFrameService;
	@Autowired
	private ProjectSchedulePlanService projectSchedulePlanService;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private HintInfoService hintInfoService;
	@Autowired
	private ClassManageService classManageService;
    @Autowired
    private HttpSession session;

	private Log log=LogFactory.getLog(getClass());

	@Autowired
	private AuthorityManageService authorityManageService;

	
	@RequestMapping(value="/classRoom")
	public String classRoom(Model model){
		Map resultMap = new HashMap();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
        model.addAllAttributes(resultMap);
        List<DataDictionary> roomType = dataDictionaryService.getDicItemListNoPage("12"); // 教室类型
        List<DataDictionary> useSeason = dataDictionaryService.getDicItemListNoPage("20"); // 可用季节
		Map<String, Object> reqmap = new HashMap<String, Object>();
		reqmap.put("personCode", getUser().getPersonCode());
		List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
//        List<DataDictionary> roomLoc = dataDictionaryService.getDicItemListNoPage("21"); // 所属位置
        model.addAttribute("roomType", roomType);

        model.addAttribute("useSeason", useSeason);
        model.addAttribute("roomLoc", buildingAuth);
        return "classRoom/classRoom";
    }
	
	/**
	 * 添加或修改教室页面
	 * @param roomId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addClassRoom")
	public ModelAndView addClassRoom(String roomId) {
		List<DataDictionary> roomType = dataDictionaryService.getDicItemListNoPage("12"); // 教室类型
		List<DataDictionary> useSeason = dataDictionaryService.getDicItemListNoPage("20"); // 可用季节
//		List<DataDictionary> roomLoc = dataDictionaryService.getDicItemListNoPage("21"); // 所属位置
		Map result = new HashMap();
		Map<String, Object> reqmap = new HashMap<String, Object>();
		reqmap.put("personCode", getUser().getPersonCode());
		List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
		if(roomId != null && !"".equals(roomId)) { // 编辑教室
			ClassRoom classRoom = classRoomService.getClassRoomByRoomId(roomId);
			result.put("classRoom", classRoom);
		}
		result.put("roomType", roomType);
		result.put("useSeason", useSeason);
		result.put("roomLoc", buildingAuth);
	
	
		return new ModelAndView("classRoom/addClassRoom",result);
	}
	
	
	
	/**
	 * 设置教室不可用时间
	 * @param roomId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/setUnUseTime")
	public ModelAndView setUnUseTime(String roomId,String date,String next, String up, String roomLocName) {
		Map resultMap = new HashMap();
		getUnUserTime(roomId, date, next, up, roomLocName, resultMap);
		
		return new ModelAndView("classRoom/unUseTime",resultMap);
	}
	/**
	 * 空闲详情
	 * @param roomId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/idleRoomDetail")
	public ModelAndView idleRoomDetail(String roomId,String date,String next, String up, String roomLocName) {
		Map resultMap = new HashMap();
		// 查询不可用时间
		getUnUserTime(roomId, date, next, up, roomLocName, resultMap);
		// 查询已排课时间
		Map<String,Object> param = new HashMap<>();
		param.put("xyStart", resultMap.get("xyStart").toString());
		param.put("xyEnd", resultMap.get("xyEnd").toString());
		param.put("roomId", roomId);
		List<Map<String,String>> list =  classManageService.getSavedCourseByProjectId(param);
		resultMap.put("classRoomPlan",list);
		// 查询临时排课占用的
		List<Map<String,String>> tempPlan =  classManageService.getTempPlan(param);
		resultMap.put("tempPlan",tempPlan);
		return new ModelAndView("pkManagement/idleRoomDetail",resultMap);
	}

	/**
	 * 获取不可用时间
	 * @param roomId
	 * @param date
	 * @param next
	 * @param up
	 * @param roomLocName
	 * @param resultMap
	 */
	private void getUnUserTime(String roomId, String date, String next, String up, String roomLocName, Map resultMap) {
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		if(roomId != null && !"".equals(roomId)) { // 查询教室
			ClassRoom classRoom = classRoomService.getClassRoomByRoomId(roomId);
			resultMap.put("classRoom", classRoom);
		}
		OpResult opResult = hintInfoService.getHintInfoByPerson("01", getUser().getPersonCode());
		if("OK".equals(opResult.getResult())) {
			resultMap.put("flag", false);
		}else {
			resultMap.put("flag", true);
		}
		Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
		Map culm = DateUtil.getCulm(date,next,up,"");
		resultMap.putAll(culm);
		resultMap.putAll(timeTable);
		Map param = new HashMap();
		param.put("years", resultMap.get("years"));
		param.put("week", resultMap.get("weeks"));
		param.put("roomId", roomId);
		List<RoomUnuseTimeFrame> list = roomUnuseTimeFrameService.getRoomUnuseTimeFrameListByRoomID(param);
		resultMap.put("list", list);
		resultMap.put("roomLocName", roomLocName);
	}


	/**
	 * 查询教室信息列表
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getClassRooms" , method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getClassRooms(ClassRoom classRoom,Integer page, Integer limit){
		try {
		    if(classRoom.getUseSeason() != null && !"".equals(classRoom.getUseSeason())) {
		        classRoom.setSeason(classRoom.getUseSeason().split(","));
            }
            // 查询楼群权限
            Map<String, Object> reqmap = new HashMap<String, Object>();
            reqmap.put("personCode", getUser().getPersonCode());
            List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap);
            if(buildingAuth.size() <= 0) {
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("code", 0);
				result.put("msg", "");
				result.put("count", 0);
				result.put("data", new ArrayList<>());
				return result;
			}
            classRoom.setRoomLocList(buildingAuth);
			return classRoomService.getClassRooms(classRoom,page,limit);
		}catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 编辑教室
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveNewClassRoom" , method=RequestMethod.POST)
	@ResponseBody
	public OpResult saveNewClassRoom(@RequestParam Map<String,Object> map) {
		try {
			log.debug(map);
			return classRoomService.saveNewClassRoom(map);
		}catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	
	
	/**
	 * 批量停用或启用
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateStatus" , method=RequestMethod.POST)
	@ResponseBody
	public OpResult updateProhibitionStatus(@RequestParam Map<String,Object> map) {
		try {
			log.debug(map);
			return classRoomService.updateProhibitionStatus(map);
//			System.out.println("=================");
		}catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	
	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteClassRoom" , method=RequestMethod.POST)
	@ResponseBody
	public OpResult deleteClassRoom(@RequestParam Map<String,Object> map) {
		try {
			log.debug(map);
			return classRoomService.deleteClassRoom(map);
		}catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	
	/**
	 * 根据roomId查询不可用时间段列表
	 * @return
	 */
	@RequestMapping(value="/getRoomUnuseTimeFrameListByRoomID", method=RequestMethod.GET) 
	@ResponseBody
	public List<RoomUnuseTimeFrame> getRoomUnuseTimeFrameListByRoomID(@RequestParam Map<String,Object> map){ 
		return this.roomUnuseTimeFrameService.getRoomUnuseTimeFrameListByRoomID(map);
	}
	
	
	/**
	 * 添加不可用时间段记录
	 * @param option
	 * @param year
	 * @param week
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value="/saveUnUseTime", method=RequestMethod.POST )
	@ResponseBody
	public OpResult saveUnUseTime(String option, String year, String week, String roomId) {
		if(roomId == null || "".equals(roomId)) {
			return new OpResult("请重新选择教室");
		}
		try {
			
			return classRoomService.saveUnUseTime(option, year, week, roomId);
			
		} catch (BusinessLevelException e) {
			log.debug(e);
			return new OpResult("设置失败");
		}
		
		
	}
	
	@RequestMapping(value="/copyUnUseTime" ,method=RequestMethod.POST)
	@ResponseBody
	public OpResult copyUnUseTime(String option, String year, String week, String roomId,String startTime, String endTime) {
		try {
			
			Map formdata = new HashMap();
			formdata.put("startTime", startTime);
			formdata.put("endTime", endTime);
			return classRoomService.copyUnUseTime(option, year, week, roomId,formdata );
			
		} catch (BusinessLevelException e) {
			log.debug(e);
			return new OpResult("设置失败");
		}
		
		
	}
	
	/**
	 * 复制页面
	 * @return
	 */
	@RequestMapping(value="/planItem", method=RequestMethod.GET )
	public ModelAndView planItem(String roomId,String startTime, String endTime) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		reMap.put("roomId", roomId);
		reMap.put("startTime", startTime);
		reMap.put("endTime", endTime);
		return new ModelAndView("classRoom/planItem",reMap);
	}
	
	/**
	 * 查询教室空闲时间
	 * @param roomLoc
	 * @param class_room_type
	 * @param class_date
	 * @param person_count
	 * @param starTime
	 * @param endTime
	 * @param year
	 * @return
	 */
	@RequestMapping("/getIdleRoom")
	@ResponseBody
	public Map<String, Object> getIdleRoom(String roomLoc, String class_room_type, String class_date, String person_count, String starTime, String endTime, String  year,String scheduleId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = classRoomService.getIdleRoom(roomLoc, class_room_type, class_date, person_count, starTime, endTime,year,scheduleId,"");
		
		return result;
	}
	
	/**
	 * 查询教室已排课详情
	 * @param roomId
	 * @return
	 */
	@RequestMapping("/getRoomPlan")
	@ResponseBody
	public Map<String, Object> getRoomPlan(String roomId, String class_date, String starTime, String endTime, String  year) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = classRoomService.getRoomPlan(roomId,class_date,starTime,endTime,year);
		
		return result;
	}
	
	
	/**
	 * 修改教室时间段可用或不可用 ysb
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateRoomUnuseTimeFrame" , method=RequestMethod.POST)
	@ResponseBody
	public OpResult updateRoomUnuseTimeFrame(@RequestParam Map<String,Object> map) {
		try {
			log.debug(map);
			return roomUnuseTimeFrameService.updateRoomUnuseTimeFrame(map);
		}catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getMactchingRooms" , method=RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getMactchingRooms(@RequestParam Map<String,Object> map) {
		return classRoomService.getMactchingRooms(map);
	}
	
	
	@RequestMapping(value = "/saveUsableRoom" , method=RequestMethod.POST)
	@ResponseBody
	public OpResult saveUsableRoom(@RequestParam Map<String,Object> map) {
		try {
			log.debug(map);
			return classRoomService.saveUsableRoom(map);
		}catch (BusinessLevelException ex) {
			ex.printStackTrace();
			return new OpResult(ex.getMessage());
		}
	}
	
	
//	@ApiOperation(value="查看教室空闲时间")
//	@ApiImplicitParams({
//        @ApiImplicitParam(name = "classDate",value = "上课日期",required = true,dataType = "String"),
//        @ApiImplicitParam(name = "roomLoc",value = "所属位置",required = false,dataType = "String"),
//        @ApiImplicitParam(name = "roomNum",value = "教室号",required = false,dataType = "String")
//	})
//	@RequestMapping(value = "/getClassRoomSpareTime" , method=RequestMethod.GET)
//	@ResponseBody
//	public List<Map<String, Object>> getClassRoomSpareTime(@RequestParam Map<String,Object> map) {
//		return classRoomService.getClassRoomSpareTime(map);
//	}
	
	

	
	
	
	@RequestMapping(value = "/getClassRoomSpareTime" , method=RequestMethod.GET)
	@ResponseBody
	public List<String> getClassRoomAlreadyClasses(@RequestParam Map<String,Object> map) {
		return classRoomService.getClassRoomAlreadyClasses(map);
	}
	
	@RequestMapping(value = "/updateReturnToClass" , method=RequestMethod.POST)
	@ResponseBody
	public OpResult updateReturnToClass(@RequestParam Map<String,Object> map) {
		return classRoomService.updateReturnToClass(map);
	}
	
	
	/**
	 * 空闲时间查询
	 * @return
	 */
	@RequestMapping(value="/idleRoomList" ,method=RequestMethod.GET)
	public ModelAndView idleRoomList() {
		Map resultMap = new HashMap();
		resultMap.putAll(getMenuMap());
		resultMap.put("user", getUser());
		//List<DataDictionary> roomLoc = dataDictionaryService.getDicItemListNoPage("21"); // 所属位置
		Map<String, Object> reqmap = new HashMap<String, Object>();
		reqmap.put("personCode", getUser().getPersonCode());
		List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
		resultMap.put("roomLoc", buildingAuth);
		Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
		resultMap.put("timeTable", timeTable.get("data"));
		return new ModelAndView("pkManagement/idleRoomList",resultMap);
			
	}


	/**
	 * 空闲教室查询
	 * @param startTime
	 * @param endTime
	 * @param start
	 * @param end
	 * @param roomLoc
	 * @param roomNum
	 * @param weekDay
	 * @param limit
	 * @param page
	 * @return
	 */
	@RequestMapping("/idleRoomListData")
	@ResponseBody
	public Map<String,Object> idleRoomListData(String startTime, String endTime, String start, String end, String roomLoc, String roomNum, String weekDay, int limit, int page) {
		Map<String, Object> reqmap = new HashMap<String, Object>();
		reqmap.put("personCode", getUser().getPersonCode());
		List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
		return classRoomService.idleRoomListData(startTime,endTime,start,end,roomLoc,roomNum,weekDay,limit,page,buildingAuth);
	}

	@RequestMapping("/setTempPlanPage")
	public ModelAndView setTempPlan(String date, String id) {
	    // 编辑
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> tempPlan = null;
        if(!StringUtils.isEmpty(id)) {
            tempPlan = classRoomService.getTempPlan(id);
        }
		String[] data = date.split("-");
		String classdate = data[0] + "-" + data[1] + "-" + data[2];
		String weekDay = data[3];
		String startTime = data[4];
		String endTime = data[5];
//		List<TimeSheet> timeList = settingService.getTimeSheet(data[0]);// 所有时间
		Map reqmap = new HashMap();
        reqmap.put("personCode",getUser().getPersonCode());
		List<Map<String, Object>> teacherList = authorityManageService.getPersonTeacherAuth(reqmap); // 教师权限
		if(teacherList.size() > 0) {
			// 查询已排教师
			Map<String,Object> param = new HashMap<>();
			param.put("class_date",classdate);
			param.put("teacherList",teacherList);
			param.put("startTime",startTime);
			param.put("endTime",endTime);
			List<String> teacherCodes = classRoomService.getIsPlanTeacherCode(param);
			Iterator<Map<String, Object>> iterator = teacherList.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> map = iterator.next();
				for (String tid : teacherCodes) {
					if(tid.equals(map.get("teacher_code").toString())) {
						iterator.remove();
					}
				}
			}
		}
		result.put("classdate",classdate);
		result.put("weekDay",weekDay);
//		result.put("timeList",timeList);
        if (tempPlan != null) {
            result.put("startTime",tempPlan.get("startTime"));
            result.put("endTime",tempPlan.get("endTime"));
            result.put("teacherCode",tempPlan.get("teacherCode"));
            result.put("teacherName",tempPlan.get("teacherName"));
            result.put("courseName",tempPlan.get("courseName"));
        }else {
            result.put("startTime",startTime);
            result.put("endTime",endTime);
        }
		result.put("teacherList",teacherList);
		return new ModelAndView("pkManagement/addTempPlan",result);
	}

	/**
	 * 获取项目相关
	 * @return
	 */
	@RequestMapping(value="/getProjectInfo", method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getProjectInfo(String rowId) {
//		Map<String,Object> result = new HashMap<String, Object>();
//		result = classRoomService.getProjectInfo(getUser().getPersonCode()); // 查询项目
//		result.put("teacher_name",getUser().getUserName());
//		return result;
		List<Map<String, Object>> listMap = classRoomService.getProjectInfo(getUser().getPersonCode());
		return listMap;
	}

	@RequestMapping(value="/saveTempPlan", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveTempPlan(@RequestParam Map<String,Object> map) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			result = classRoomService.saveTempPlan(map);
			result.put("result","OK");
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.put("result","");
			result.put("message","添加失败");
			return result;
		}
	}

	/**
	 * 判断当前时间是否可用
	 * @param date
	 * @return
	 */
	@RequestMapping("/getNowTimeIsAble")
	@ResponseBody
	public OpResult getNowTimeIsAble(String date,String roomId) {
		String[] data = date.split("-");
 		String classdate = data[0] + "-" + data[1] + "-" + data[2];
		String weekDay = data[3];
		String startTime = data[4];
		String endTime = data[5];
		Map<String, Object> param = new HashMap<>();
		param.put("roomId",roomId);
		param.put("class_date",classdate);
		param.put("startTime",startTime);
		param.put("endTime",endTime);
		String id = classManageService.isPlan(param);
		if(id != null && !"".equals(id)) {
			return new OpResult("当前时间不可用");
		}
		return new OpResult();
	}

	@RequestMapping(value = "/importClassRoom")
	@ResponseBody
	public OpResult importClassRoom(@RequestParam("file") MultipartFile multipartFile) {

		try {
			String originalFilename = multipartFile.getOriginalFilename();
			String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
			String[] titles = {"教室号", "所属位置", "座位数", "教室类型","可用季节"};
			String[] titles_en = {"roomNum", "roomLoc", "seating", "roomType", "useSeason"};
            Map<String, Object> readExcel = ExcelUtil_JXL.readExcel(multipartFile.getInputStream(), fileType, titles, titles_en);
            List<Map<String, Object>> list = (List<Map<String, Object>>) readExcel.get("list");// 读取excel
            if(list.size() > 0) {
                // 异常消息集合
                List<Map<String, Object>> msg = new ArrayList<>();
                // 1 判断数据字典是否存在
                List<DataDictionary> roomLoc = dataDictionaryService.getDicItemListNoPage("21"); // 所属位置
                List<String> noRoomLoc = new ArrayList<>();
                String key = "roomLoc";
                Tools.getDataDic(list,roomLoc,noRoomLoc,key);
                List<DataDictionary> roomType = dataDictionaryService.getDicItemListNoPage("12"); // 教室类型
                List<String> noRoomType = new ArrayList<>();
                key = "roomType";
                Tools.getDataDic(list,roomType,noRoomType,key);
                List<DataDictionary> useSeason = dataDictionaryService.getDicItemListNoPage("20"); // 可用季节
                List<String> noSeason = new ArrayList<>();
                getDataDic(list, useSeason, noSeason);
                // 封装msg
                Tools.getMsg(msg, noRoomLoc, "所属位置");
                Tools.getMsg(msg, noRoomType, "教室类型");
                Tools.getMsg(msg, noSeason, "可用季节");
                // 查重
                List<ClassRoom> existList = classRoomService.existList(list);
                for (ClassRoom classRoom:existList) {
                    Map<String, Object> option = new HashMap<>();
                    option.put("source","重复数据");
                    option.put("description","系统中"+classRoom.getRoomLocName() + "   " + classRoom.getRoomNum()+"已存在");
					msg.add(option);
                }
                if(msg.size() > 0) {
                    session.setAttribute("classRoomImportMsg",msg);
                    return  new OpResult("导入失败，请点击确定下载异常信息");
                }
                // 导入数据
                Tools.setDataDic(list,roomLoc,"roomLoc");
                Tools.setDataDic(list,roomType,"roomType");
                setDataDic(list,useSeason);
                classRoomService.importClassRoom(list);
                return new OpResult();
            }else {
                return new OpResult("excel中无数据");
            }

        }catch (Exception e) {
			e.printStackTrace();
            return new OpResult("系统异常");
        }
	}

	@RequestMapping("/templateDown")
	@ResponseBody
	public void templateDown(HttpServletResponse response, HttpServletRequest request) {
		String fileName = "教室导入模板.xls";
		String workbookName = "教室信息";
		Map<String, Object> exportMap = new LinkedMap<>();
		String[] titles = {"教室号", "所属位置", "座位数", "教室类型","可用季节"};
		String[] titles_en = {"*roomNum", "*roomLoc", "*seating", "*roomType", "*useSeason"};
		List<Map<String, Object>> list =  new ArrayList<>();
		Map<String, Object> temp = new HashMap<>();
		temp.put("*roomNum","4A01");
		temp.put("*roomLoc","光华楼");
		temp.put("*seating","50");
		temp.put("*roomType","语音教室");
		temp.put("*useSeason","冬季,秋季 多个季节用英文逗号分隔");
		list.add(temp);
		for (int i = 0; i < titles.length; i++) {
			exportMap.put(titles[i],titles_en[i]);
		}
		String result = ExcelUtil_JXL.exportExcel(fileName,workbookName,exportMap,list,response,request);
	}


    /**
     * 判断excel中可用季节是否存在
     * @param list
     * @param useSeason
     * @param noSeason
     */
    private void getDataDic(List<Map<String, Object>> list, List<DataDictionary> useSeason, List<String> noSeason) {
        for (Map<String, Object> map: list) {
            Object useSeason1 = map.get("useSeason");
            map.put("roomId",Constants.GUID());
            map.put("createTime",DateUtil.ToDateTimeString());
            if(useSeason1 != null && !"".equals(useSeason1.toString())) {
                String[] seasons = useSeason1.toString().split(",");
                for (String season: seasons) {
                    boolean flag = true;
                    for (DataDictionary data:useSeason) {
                        String name = data.getName();
                        if(season.trim().equals(name)) {
                            flag = false;
                            break;
                        }
                    }
                    if(flag) {
                        noSeason.add(season);
                    }
                }

            }else {
                noSeason.add("none");
            }
        }
    }
    /**
     * 判断excel中可用季节是否存在
     * @param list
     * @param useSeason
     */
    private void setDataDic(List<Map<String, Object>> list, List<DataDictionary> useSeason) {
        for (Map<String, Object> map: list) {
            Object useSeason1 = map.get("useSeason");
            if(useSeason1 != null && !"".equals(useSeason1.toString())) {
                String[] seasons = useSeason1.toString().split(",");
                String seasonStr = "";
                for (String season: seasons) {
                    for (DataDictionary data:useSeason) {
                        String name = data.getName();
                        if(season.trim().equals(name)) {
                            seasonStr+=data.getCode()+",";
                        }
                    }

                }
                map.put("useSeason",seasonStr.substring(0,seasonStr.lastIndexOf(",")));

            }
        }
    }

}
