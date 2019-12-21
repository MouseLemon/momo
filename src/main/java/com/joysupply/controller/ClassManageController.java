package com.joysupply.controller;

import com.github.pagehelper.PageInfo;
import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.*;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;

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
 * 课程管理controller
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/classManage")
public class ClassManageController extends BaseController {

    @Autowired
    ClassManageService classManageService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ClassRoomService classRoomService;

    @Autowired
    private AuthorityManageService authorityManageService;

    @Autowired
    private OrganizeStructService organizeStructService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private HintInfoService hintInfoService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private KcManagerService kcManagerService;

    private Log log = LogFactory.getLog(getClass());

    private String ok = "OK";
    private String undefined = "undefined";

    private String getUserCode() {
        SystemUser user = getUser();
        return user.getPersonCode();
    }

    /**
     * 课程管理页面
     * @return
     */
    @RequestMapping(value = "/classManagePage")
    public ModelAndView classManagePage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("pkManagement/classManage", resultMap);
    }

    /**
     * 课程管理列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/getClassManageList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getClassManageList(@RequestParam Map map) {
        try {
            // 获取当前用户personcode
            String personCode = getUser().getPersonCode();
            List<String> personRoles = roleService.getPersonRoles(personCode);
            // 如果是系统管理员, 则显示所有项目
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                return classManageService.getClassManageList(map);
            } else if (personRoles.contains("10") || personRoles.contains("1001") ||personRoles.contains("1002")) {
                map.put("personCode",getUser().getPersonCode());
                return classManageService.getClassManageListA(map);
            } else {
                map.put("personCode",getUser().getPersonCode());
                return classManageService.getClassManageList(map);
            }
        } catch (BusinessLevelException e) {
            log.error("获取课程管理列表:" + e.getMessage());
            return null;
        }
    }

    /**
     * 提交所有选中（修改已排课程状态）
     * @param map
     * @return
     */
    @RequestMapping(value = "/submitHadPk", method = RequestMethod.POST)
    @ResponseBody
    public OpResult submitHadPk(@RequestParam Map map){
        try{
            log.debug(map);
            OpResult opResult = classManageService.submitHadPk(map);
            if (ok.equals(opResult.getResult())) {
                return new OpResult();
            } else {
                return new OpResult(opResult.getMessage());
            }
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 跳转至排课页面
     * @param
     * @return
     */
    @RequestMapping(value = "/pkPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView pkPage(String project_id, String timeStamp, String date, String next,
                               String up, String count_hour, @RequestParam(value="status",required=false) String status, String grade_id) throws ParseException {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        resultMap.put("grade_id", grade_id);

        Map<String, Object> gradeInfo = classManageService.getAllGradeInfo(resultMap);
        resultMap.put("grade_code", gradeInfo.get("grade_code").toString());

        Map<String, Object> temp = new HashMap<>();

        // 第一次登陆
        OpResult opResult = hintInfoService.getHintInfoByPerson("00", getUser().getPersonCode());
        if(ok.equals(opResult.getResult())) {
            resultMap.put("flag", false);
        }else {
            resultMap.put("flag", true);
        }

        if (project_id != null && !"".equals(project_id)){
            // 根据id查询项目，获取项目信息
            ProjectInfo projectInfo = classManageService.getProjectInfo(project_id);
            resultMap.put("projectInfo", projectInfo);
            // ---------------------------------------------------------------------------------
            Map<String,Object> param = new HashMap<>();
            Map<String,Object> params = new HashMap<>();
            param.put("personCode",getUserCode());
            List<Map<String,Object>> myAuth = authorityManageService.getPersonProjectAuth(param);
            if(myAuth.size() <= 0) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("count", 0);
                map.put("data", new ArrayList<>());
                map.put("code", 0);
                map.put("msg", "");
            }
            params.put("personCode", getUserCode());
            params.put("myAuth",myAuth);

            if (myAuth.size() > 0) {
                // 查询项目对应的流水号
                List<String> serialNumbers = kcManagerService.getSerialNumbers(params);
                params.put("serialNumbers",serialNumbers);
                Map aaa = kcManagerService.projectKcQueryA(params);
                List daList = (List) aaa.get("data");
                List endTimeList = new ArrayList();

                for (int i = 0; i < daList.size(); i++) {
                    Map aMap = (Map) daList.get(i);
                    String endTime = aMap.get("endTime").toString();
                    endTimeList.add(endTime);
                }

                if (endTimeList.size() > 0) {
                    Comparable max = Collections.max(endTimeList);
                    String kcEndTime = (String) max; // 最后一次统计课酬的时间
                    // 判断结算课酬日期是否在项目日期内
                    resultMap.put("kcEndTime", kcEndTime);
                } else {
                    resultMap.put("kcEndTime", 0);
                }
            }
        }

        // 查询时间表
        Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
        resultMap.putAll(timeTable);

        // 获取指定的年月日周期
        Map culm = DateUtil.getCulm(date,next,up,"");
        resultMap.putAll(culm);

        Map param = new HashMap();
        param.put("year", resultMap.get("year"));
        param.put("week", resultMap.get("weeks"));
        param.put("xyStart", resultMap.get("xyStart"));
        param.put("xyEnd", resultMap.get("xyEnd"));
        param.put("project_id", project_id);
        param.put("grade_id", grade_id);

        // 查询已存排课内容
        List<Map<String,String>> list = classManageService.getSavedCourseByProjectId(param);
        // 查询该项目已排课数量
        int pkCountNow = classManageService.getPkCount(project_id);
        resultMap.put("pkCount", pkCountNow);
        resultMap.put("count_hour", count_hour);

        List<Map<String,String>> timeSheetList = settingService.getTimeSheetList(param);
        resultMap.put("list", list);
        resultMap.put("timeSheetList", timeSheetList);
        resultMap.put("timeStamp", timeStamp);
        if (status == null || undefined.equals(status) || "".equals(status)){
            status = "0";
        }
        resultMap.put("status", status);
        return new ModelAndView("pkManagement/pkPage", resultMap);
    }

    /**
     * 排教室列表
     * @return
     */
    @RequestMapping("/setClassRoomPlan")
    public ModelAndView setClassRoomPlan() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("pkManagement/classRoomPlanList", resultMap);
    }

    /**
     * 排教室列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/classRoomPlanList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> classRoomPlanList(@RequestParam Map map) {
        try {
            Map<String,Object> param = new HashMap<>();
            param.put("personCode",getUser().getPersonCode());
            List projectAuth = authorityManageService.getPersonProjectAuth(param);
            if(projectAuth.size() <= 0) {
                map.put("count", 0);
                map.put("data", projectAuth);
                map.put("code", 0);
                map.put("msg", "返回数据成功");
                return map;
            }
            map.put("projectAuth",projectAuth);
            return classManageService.classRoomPlanList(map);
        } catch (BusinessLevelException e) {
            log.error("获取课程管理列表:" + e.getMessage());
            return null;
        }
    }

    /**
     * 批量退回排课
     * @param map
     * @return
     */
    @RequestMapping(value = "/returnPK", method = RequestMethod.POST)
    @ResponseBody
    public OpResult returnPK(@RequestParam Map map) {
        try {
            return classManageService.returnPK(map);
        } catch (BusinessLevelException e) {
            return new OpResult("退回排课失败");
        }
    }

    @RequestMapping(value = "/returnPKA", method = RequestMethod.POST)
    @ResponseBody
    public OpResult returnPKA(@RequestParam Map map) {
        try {
            return classManageService.returnPKA(map);
        } catch (BusinessLevelException e) {
            return new OpResult("退回排课失败");
        }
    }

    /**
     * 点击表格，弹出《修改课程信息》
     * @param map
     * @return
     */
    @RequestMapping(value = "/editClassInfoPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView editClassInfoPage(@RequestParam Map map){ // map:schedule_id, year
        String year = map.get("year").toString();
        List<TimeSheet> timeList = settingService.getTimeSheet(year);// 所有时间
        List<DataDictionary> class_room_typeList = dataDictionaryService.getDicItemListNoPage("12"); // 教室类型
        List<DataDictionary> courseList = dataDictionaryService.getDicItemListNoPage("25"); // 课名
        // 根据project_id查询在该项目下的老师
        Map reqmap = new HashMap();
        reqmap.put("personCode",getUser().getPersonCode());
        List teacherList = authorityManageService.getPersonTeacherAuth(reqmap); // 教师权限

        String project_id = map.get("project_id").toString();
        int startPersonCount = classManageService.getProjectStartPersonCount(project_id);
        String start_person_count = startPersonCount + "";

        String schedule_id = map.get("schedule_id").toString();
        if (undefined.equals(schedule_id) || "".equals(schedule_id)){
            schedule_id = "";
        }

        Map<String, Object> classInfo = classManageService.getProjectSchedulePlanSheet(schedule_id);// 所有课程
        if ("".equals(classInfo.get("base_fee").toString()) || classInfo.get("base_fee") == null){
            classInfo.put("base_fee", "0.00");
            classInfo.put("person_factor", "0.00");
            classInfo.put("course_factor", "0.00");
        }

        classInfo.put("start_person_count", start_person_count);
        classInfo.put("class_room_typeList", class_room_typeList);
        classInfo.put("teacherList", teacherList);
        classInfo.put("timeList", timeList);
        classInfo.put("courseList", courseList);
        classInfo.put("yStartTime", map.get("start_time").toString());
        classInfo.put("yEndTime", map.get("end_time").toString());
        return new ModelAndView("pkManagement/editClassInfoPage", classInfo);
    }

    /**
     * 修改课程信息《保存》
     * @param projectSchedulePlan
     * @return
     */
    @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
    @ResponseBody
    public OpResult saveEdit(@RequestBody Map projectSchedulePlan){
        try{
            log.debug(projectSchedulePlan);
            String selectStartTime = projectSchedulePlan.get("start_time").toString(); // 选择的开始时间
            String selectEndTime = projectSchedulePlan.get("end_time").toString(); // 选择的结束时间
            Map result = classManageService.selectBlank2(projectSchedulePlan);
            if (result == null){ // 可以排课
                return classManageService.saveEdit(projectSchedulePlan);
            } else {
                String pScheduleId = projectSchedulePlan.get("schedule_id").toString();
                String rScheduleId = result.get("schedule_id").toString();
                if (pScheduleId.equals(rScheduleId)) {
                    // 课程相同, 执行修改
                    classManageService.saveEdit(projectSchedulePlan);
                    return new OpResult();
                }

                // 查询所选开始时间和结束时间, 再当前日期, 当前开始时间和结束时间, 是否有可课
                String st = result.get("start_time").toString().replace(":", "");
                System.out.println(st);
                String et = result.get("end_time").toString().replace(":", "");
                String set = selectEndTime.replace(":", "");

                int start_time = Integer.parseInt(st);
                int end_time = Integer.parseInt(et);
                int selectEndTime1 = Integer.parseInt(set);

                if (selectEndTime1 <= start_time){
                    OpResult opResult = classManageService.saveEdit(projectSchedulePlan);
                    if (ok.equals(opResult.getResult())) {
                        return new OpResult();
                    } else {
                        return new OpResult(opResult.getMessage());
                    }
                } else if (selectEndTime1 > start_time && selectEndTime1 <= end_time){
                    return new OpResult("结束时间所在时段已排课，请重新选择！");
                } else {
                    return new OpResult("结束时间所在时段已排课，请重新选择！");
                }
            }
        } catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 修改课程信息《删除》
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteEdit")
    @ResponseBody
    public OpResult deleteEdit(@RequestParam Map map){
        try{
            log.debug(map);
            return classManageService.deleteEdit(map);
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 添加课程信息《保存》
     * @param projectSchedulePlan
     * @return
     */
    @RequestMapping(value = "/saveAdd")
    @ResponseBody
    public OpResult saveAdd(@RequestParam Map projectSchedulePlan){
        try{
            log.debug(projectSchedulePlan);
            String selectEndTime = projectSchedulePlan.get("end_time").toString(); // 选择的结束时间
            Map result = classManageService.selectBlank2(projectSchedulePlan);
            if (result == null){ // 可以排课
                return classManageService.saveAdd(projectSchedulePlan);
            } else {
                // 取查出来的开始时间：为最大结束时间
                String st = result.get("start_time").toString().replace(":", "");
                String et = result.get("end_time").toString().replace(":", "");
                String set = selectEndTime.replace(":", "");

                int start_time = Integer.parseInt(st);
                int end_time = Integer.parseInt(et);
                int selectEndTime1 = Integer.parseInt(set);

                String alert = result.get("start_time").toString();

                if (selectEndTime1 <= start_time){
                    classManageService.saveAdd(projectSchedulePlan);
                    return new OpResult();
                } else if (selectEndTime1 >= start_time && selectEndTime1 <= end_time){
                    return new OpResult("所选时间已有课程安排，请选择" + alert + "之前的时间！");
                } else {
                    return new OpResult("所选时间已有课程安排，请选择" + alert + "之前的时间！");
                }
            }
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 点击表格，弹出《添加课程信息》
     * @param map
     * @return
     */
    @RequestMapping(value = "/addClassInfoPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView addClassInfoPage(@RequestParam Map map){
        String year = map.get("year").toString();
        List<TimeSheet> timeList = settingService.getTimeSheet(year);// 所有时间
        List<DataDictionary> class_room_typeList = dataDictionaryService.getDicItemListNoPage("12"); // 教室类型
        List<DataDictionary> courseList = dataDictionaryService.getDicItemListNoPage("25"); // 课名
        Map reqmap = new HashMap();
        reqmap.put("personCode",getUser().getPersonCode());
        List teacherList = authorityManageService.getPersonTeacherAuth(reqmap); // 教师权限

        String project_id = map.get("project_id").toString();
        int startPersonCount = classManageService.getProjectStartPersonCount(project_id);
        String start_person_count = startPersonCount + "";

        Map<String, Object> classInfo = new HashMap<>();
        classInfo.put("timeList", timeList);
        classInfo.put("class_room_typeList", class_room_typeList);
        classInfo.put("teacherList", teacherList);
        classInfo.put("courseList", courseList);

        classInfo.put("project_id", map.get("project_id").toString());
        classInfo.put("start_person_count", start_person_count);
        classInfo.put("grade_id", map.get("grade_id").toString());
        classInfo.put("week_day", map.get("week_day").toString());
        classInfo.put("ymd", map.get("ymd").toString());
        classInfo.put("start_time", map.get("start_time").toString());
        classInfo.put("end_time", map.get("end_time").toString());
        classInfo.put("projectStartTime", map.get("projectStartTime").toString());
        classInfo.put("projectEndTime", map.get("projectEndTime").toString());
        return new ModelAndView("pkManagement/addClassInfoPage", classInfo);
    }

    /**
     * 排教室页面
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/classRoomPlan", method = RequestMethod.GET)
    public ModelAndView classRoomPlan(String currentCount,String startTime, String date, String next, String up, String projectId, String endTime, String projectName, String gradeId) {
        try {
            // 教室类型
            List<DataDictionary> roomType = dataDictionaryService.getDicItemListNoPage("12");
            Map result = getMenuMap();
            result.put("grade_id", gradeId);
            Map<String, Object> gradeInfo = classManageService.getAllGradeInfo(result);
            result.put("grade_code", gradeInfo.get("grade_code").toString());

            result.put("user", getUser());
            Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
            Map culm = DateUtil.getCulm(date,next,up,"");
            result.putAll(culm);
            result.putAll(timeTable);
            result.put("endTime", endTime);
            result.put("projectId", projectId);
            result.put("projectName", projectName);

            Map param = new HashMap();
            param.put("year", result.get("year"));
            param.put("week", result.get("weeks"));
            param.put("project_id", projectId);
            param.put("grade_id", gradeId);

            param.put("xyStart", result.get("xyStart"));
            param.put("xyEnd", result.get("xyEnd"));
            // 查询已存排课内容
            param.put("roomPlan","1");
            List<Map<String,String>> list = classManageService.getSavedCourseByProjectId(param);
            result.put("list", list);
            result.put("startTime", startTime);
            result.put("roomType", roomType);
            return new ModelAndView("pkManagement/setClassRoomPlan",result);
        } catch (BusinessLevelException e) {
            return null;
        }
    }

    /**
     * 《排课》页面-提交按钮
     * @param map
     * @return
     */
    @RequestMapping(value = "/submitBtn", method = RequestMethod.POST)
    @ResponseBody
    public OpResult submitBtn(@RequestParam Map map){
        try{
            SystemUser user = getUser();
            String person_code = user.getPersonCode();
            map.put("person_code", person_code);
            OpResult opResult = classManageService.submitBtn(map);
            if (ok.equals(opResult.getResult())) {
                return new OpResult();
            } else {
                return new OpResult(opResult.getMessage());
            }
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 回显系数
     * @param map
     * @return
     */
    @RequestMapping(value = "/backCoefficient", method = RequestMethod.POST)
    @ResponseBody
    public Map backCoefficient(@RequestParam Map map) {
        try{
            log.debug(map);
            return classManageService.backCoefficient(map);
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 根据排课计划id查询该计划的教室信息
     * @param scheduleId
     * @return
     */
    @RequestMapping("/getClassRoomInfoByScheduleId")
    @ResponseBody
    public Map getClassRoomInfoByScheduleId(String scheduleId, String year,String classRoomType) {
        Map<String, Object> result = new HashMap<String, Object>();
        ProjectSchedulePlan classRoomInfoByScheduleId = classManageService.getClassRoomInfoByScheduleId(scheduleId);
        // 如果已排，查询教室信息用于回显
        if(classRoomInfoByScheduleId.getClass_room_id() != null && !"".equals(classRoomInfoByScheduleId.getClass_room_id())) {
            Map<String, Object> roomPlan = classRoomService.getRoomPlan(classRoomInfoByScheduleId.getClass_room_id(), classRoomInfoByScheduleId.getClass_date(), "", "", year);
            // 查询当前楼的教室
            if(roomPlan != null) {
                Map<String, Object> idleRoom = classRoomService.getIdleRoom(roomPlan.get("room_loc").toString(),
                        classRoomInfoByScheduleId.getClass_room_type().toString(),
                        classRoomInfoByScheduleId.getClass_date().toString(),
                        classRoomInfoByScheduleId.getPerson_count().toString(),
                        classRoomInfoByScheduleId.getStart_time().toString(),
                        classRoomInfoByScheduleId.getEnd_time(), year,scheduleId,classRoomInfoByScheduleId.getClass_room_id());
                result.putAll(idleRoom);
            }
            result.put("roomPlan",roomPlan);
        }

        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", getUser().getPersonCode());
        List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
        if(buildingAuth.size() > 0) { // 如果该老师有楼群
            // 根据老师和教室类型查询楼群
            reqmap.put("roomType", classRoomType);
            reqmap.put("buildingAuth", buildingAuth);
            reqmap.put("seating",  classRoomInfoByScheduleId.getPerson_count());
            List<Map<String,String>> roomLocByAuth = classRoomService.getRoomLocByAuth(reqmap);
            result.put("roomLocList", roomLocByAuth);
        }
        result.put("schedulePlan", classRoomInfoByScheduleId);
        return result;
    }

    /**
     * 保存教室排课
     * @param scheduleId
     * @return
     */
    @RequestMapping("/saveRoomPlan")
    @ResponseBody
    public OpResult saveRoomPlan(String scheduleId, String roomId, String starTime,String endTime,String class_date,String year, String classRoomType) {
        try{
            return classManageService.saveRoomPlan(scheduleId,roomId,starTime,endTime,class_date,year,classRoomType);
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 查询空闲教室页面
     * @param classDate
     * @param seating
     * @param roomType
     * @return
     */
    @RequestMapping("/idleRoom")
    public ModelAndView idleRoom(String classDate, String seating, String roomType) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("classDate", classDate);
        param.put("seating", seating);
        param.put("roomType", roomType);
        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", getUser().getPersonCode());
        List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
        param.put("roomLoc", buildingAuth);
        return new ModelAndView("pkManagement/openIdleRoomList",param);
    }

    /**
     * 空闲教室列表
     * @param classRoom
     * @param page
     * @param limit
     * @param classDate
     * @return
     */
    @RequestMapping("/idleRoomListData")
    @ResponseBody
    public Map<String, Object> idleRoomListData(ClassRoom classRoom, Integer page, Integer limit, String classDate) {
        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", getUser().getPersonCode());
        List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
        if(buildingAuth.size() <= 0) {
            reqmap.put("code", 0);
            reqmap.put("msg", "");
            reqmap.put("count", 0);
            reqmap.put("data", new ArrayList<>());
            return reqmap;
        }
        classRoom.setRoomLocList(buildingAuth);
        return classManageService.getClassRooms(classRoom, page, limit,classDate);
    }

    /**
     * 复制排课
     * @param ids
     * @param startTime
     * @param endTime
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/copyRoomPlan", method = RequestMethod.POST)
    @ResponseBody
    public OpResult copyRoomPlan(String ids, String startTime, String endTime,String projectId){
        try{
            return classManageService.copyRoomPlan(ids,startTime,endTime,projectId);
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 跳转到复制页面
     * @return
     */
    @RequestMapping(value="/copyClassPage", method=RequestMethod.GET )
    public ModelAndView planItem(String roomId,String startTime, String endTime) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        reMap.put("roomId", roomId);
        reMap.put("startTime", startTime);
        reMap.put("endTime", endTime);
        return new ModelAndView("pkManagement/copyClassPage",reMap);
    }

    /**
     * 复制排课2
     * @param ids
     * @param startTime
     * @param endTime
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/copyClassPlan", method = RequestMethod.POST)
    @ResponseBody
    public OpResult copyClassPlan(String ids, String startTime, String endTime,String projectId, String projectStartTime, String projectEndTime,String weekStartDate, String gradeId){
        try{
            return classManageService.copyClassPlan(ids,startTime,endTime,projectId, projectStartTime, projectEndTime, weekStartDate, gradeId);
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }


    /**
     * 查询教师课表日历
     * @param teacherCode
     * @param dateTime
     * @return
     */
    @RequestMapping(value = "/getPlanByTeacher", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getPlanByTeacher(String teacherCode, String dateTime) {
        return classManageService.getPlanByTeacher(teacherCode, dateTime);
    }

    /**
     * 拖动复制
     * @param map
     * @return
     */
    @RequestMapping(value = "/copyClass", method = RequestMethod.POST)
    @ResponseBody
    public Map copyClass(@RequestBody Map map) { // 复制-新插入课程，并将显示在表格的内容修改
        String targetStartTime = map.get("targetStartTime").toString();
        String start_time = targetStartTime.substring(0,2) + ":" + targetStartTime.substring(2,4);
        String targetWeek = map.get("targetWeek").toString();
        String scheduleId = map.get("scheduleId").toString();
        String status = map.get("status").toString();

        // 根据targetTime查time_sheet结束时间，2018
        SimpleDateFormat sim = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String y = sim.format(date);

        Map timeResult = new HashMap();
        timeResult.put("start_time", start_time);
        timeResult.put("year", y);
        Map result1 = settingService.getTimes(timeResult);

        Map scheduleMap = classManageService.getProjectSchedulePlanSheet(scheduleId);

        Map param = new HashMap();
        String schedule_id = UUID.randomUUID().toString();
        param.put("start_time", start_time);
        param.put("end_time", result1.get("end_time").toString());
        param.put("week_day", targetWeek);
        param.put("class_date", map.get("ymd").toString());
        param.put("status", status);
        param.put("schedule_id", schedule_id);
        param.put("project_id", scheduleMap.get("project_id").toString());
        param.put("course_name", scheduleMap.get("course_name").toString());
        param.put("teacher_code", scheduleMap.get("teacher_code").toString());
        param.put("grade_id", scheduleMap.get("grade_id").toString());
        param.put("class_room_type", scheduleMap.get("class_room_type").toString());
        param.put("base_fee", scheduleMap.get("base_fee").toString());
        param.put("person_factor", scheduleMap.get("person_factor").toString());
        param.put("course_factor", scheduleMap.get("course_factor").toString());
        param.put("person_count", scheduleMap.get("person_count").toString());
        param.put("fee_total", scheduleMap.get("fee_total").toString());

        Map kickMap = new HashMap();
        kickMap.put("nowStartTime", param.get("start_time"));
        kickMap.put("nowEndTime", param.get("end_time"));
        kickMap.put("nowDate", param.get("class_date"));
        kickMap.put("project_id", param.get("project_id"));

        // 如果当前项目，当前时间有课程，则不执行插入
        Map nowMap = classManageService.getNowScheduleOrNot(param);
        // 如果当前教师，当前时间有课，则不执行插入
        Map tMap = classManageService.getTeacherSchedule(param);

        // 当前格子没课
        if (nowMap == null){
            // 当前日期，该教师没课(当前格子)
            if (tMap == null){
                OpResult opResult = selectBlank(kickMap);
                if (ok.equals(opResult.getResult())){
                    OpResult opResult1 = classManageService.saveCopyAdd(param);
                    if (ok.equals(opResult1.getResult())) {
                        String schedule_id2 = param.get("schedule_id").toString();
                        Map result = classManageService.getProjectSchedulePlanSheet(schedule_id2);
                        return result;
                    } else {
                        Map pkMap = new HashMap();
                        pkMap.put("status", "teacherNoTime");
                        return pkMap;
                    }
                } else {
                    kickMap.put("status", "noLangTime");
                    return kickMap;
                }
            } else {
                tMap.put("status", "teacherNoTime");
                return tMap;
            }
        } else {
            nowMap.put("status", "scheduleInTime");
            return nowMap;
        }
    }

    @RequestMapping(value = "/selectBlank", method = RequestMethod.POST)
    @ResponseBody
    public OpResult selectBlank(@RequestParam Map map) {
        try {
            log.debug(map);
            Map result = classManageService.selectBlank(map);
            if (result == null){
                return new OpResult();
            } else {
                String end_time = result.get("end_time").toString();
                String nowEndTime = map.get("nowEndTime").toString();
                String nowStartTime = map.get("nowStartTime").toString();

                String end_time1 = end_time.replace(":","");
                String nowEndTime1 = nowEndTime.replace(":","");
                String nowStartTime1 = nowStartTime.replace(":","");

                int end_time2 = Integer.parseInt(end_time1);
                int nowEndTime2 = Integer.parseInt(nowEndTime1);
                int nowStartTime2 = Integer.parseInt(nowStartTime1);

                // 两个结束时间进行比较，如果查出来的结束时间 大于等于 传过来的结束时间，不可排课
                // 条件：nowStartTime2 <= end_time2 <= nowEndTime2
                if (end_time2 >= nowEndTime2 || end_time2 >= nowStartTime2){
                    return new OpResult("当前时段已安排课程，不可排课！");
                } else {
                    return new OpResult();
                }
            }
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 模糊查询教师
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectTeacher", method = RequestMethod.POST)
    @ResponseBody
    public Map selectTeacher(@RequestParam Map map) {
        try {
            log.debug(map);
            List<String> personRoles = roleService.getPersonRoles(getUser().getPersonCode());
            if(!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") &&!personRoles.contains("1902")){
                map.put("personCode", getUser().getPersonCode());
            }
            List selectTeacherList = classManageService.selectTeacher(map);
            Map param = new HashMap();
            param.put("selectTeacherList", selectTeacherList);
            return param;
        } catch (BusinessLevelException ex){
            ex.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    public OpResult validate(String projectId) {
        try{
            OpResult opResult = classManageService.validate(projectId);
            if ("OK".equals(opResult.getResult())) {
                return new OpResult();
            } else {
                return new OpResult("notSubmit");
            }
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    @RequestMapping(value = "/gradeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> gradeList(@RequestParam Map<String, Object> map) {
        try {
            if (map.size() > 2) {
                map.put("grade_code", map.get("gradeCode").toString());

                // 查询一期grade
                List<Map<String, Object>> gradeClassListA = classManageService.getGradeClassA(map);
                // 查询二期grade
                List<Map<String, Object>> gradeClassListB = classManageService.getGradeClassB(map);

                if (gradeClassListA.size() >= 1) {
                    Map<String, Object> gradeList = classManageService.getGradeListA(map);
                    return gradeList;
                }

                if (gradeClassListB.size() >= 1) {
                    Map<String, Object> gradeList = classManageService.getGradeListB(map);
                    return gradeList;
                }
            }
            return map;
        } catch (BusinessLevelException e) {
            log.error("获取班级管理列表:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/gradeListPjs", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> gradeListPjs(@RequestParam Map<String, Object> map) {
        try {
            if (map.size() > 2) {
                map.put("grade_code", map.get("gradeCode").toString());

                // 查询一期grade
                List<Map<String, Object>> gradeClassListA = classManageService.getGradeClassA(map);
                // 查询二期grade
                List<Map<String, Object>> gradeClassListB = classManageService.getGradeClassB(map);

                if (gradeClassListA.size() >= 1) {
                    Map<String, Object> gradeList = classManageService.getGradeListAPjs(map);
                    return gradeList;
                }

                if (gradeClassListB.size() >= 1) {
                    Map<String, Object> gradeList = classManageService.getGradeListBPjs(map);
                    return gradeList;
                }
            }
            return map;
        } catch (BusinessLevelException e) {
            log.error("获取班级管理列表:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/fenban", method = RequestMethod.POST)
    @ResponseBody
    public OpResult fenban(Map<String, Object> map) {
        classManageService.fenban();
        return new OpResult();
    }

}