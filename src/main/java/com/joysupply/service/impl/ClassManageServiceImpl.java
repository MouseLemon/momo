package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.ClassManageDao;
import com.joysupply.dao.ClassRoomDao;
import com.joysupply.dao.MessageDao;
import com.joysupply.entity.ClassRoom;
import com.joysupply.entity.ProjectInfo;
import com.joysupply.entity.ProjectSchedulePlan;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.*;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;
import com.joysupply.util.PageHelperUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Dreamer
 * @Description ClassManageServiceImpl
 * @Date 上午 10:06 2018/12/15 0015
 * @Param
 * @return
 **/
@Service("classManageService")
public class ClassManageServiceImpl extends BaseService implements ClassManageService {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ClassManageDao classManageDao;

    @Autowired
    private ClassRoomService classRoomService;
    
    @Autowired
    private SettingService settingService;
    
    @Autowired
    private ScheduleViewService scheduleViewService;

    @Autowired
    private KcManagerService kcManagerService;

    @Autowired
    private ClassRoomDao classRoomDao;

    @Autowired
    private MessageDao messageDao;

    /**
     * 获取课程管理列表
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public Map<String, Object> getClassManageList(Map map) throws BusinessLevelException {
        List<Map<String, Object>> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        try {
            list = classManageDao.getClassManageList(map);
        } catch (RuntimeException e) {
            log.error("获取课程管理列表:" + e.getMessage());
            throw new BusinessLevelException("获取课程管理列表", e);
        }
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public Map<String, Object> getClassManageListA(Map map) throws BusinessLevelException {
        List<Map<String, Object>> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        try {
            list = classManageDao.getClassManageListA(map);
        } catch (RuntimeException e) {
            log.error("获取课程管理列表:" + e.getMessage());
            throw new BusinessLevelException("获取课程管理列表", e);
        }
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * 提交已排课程
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult submitHadPk(Map map) throws BusinessLevelException {
        try {
            String project_ids = map.get("project_ids").toString();
            String project_names = map.get("project_names").toString();
            // 将所有project_id存入idsList
            List idsList = Arrays.asList(project_ids.split(","));
            List nameList = Arrays.asList(project_names.split(","));

            // 传入所有projectids
            Map iMap = new HashMap();
            iMap.put("list", idsList);
            // 查询给哪些用户发消息
            List<Map<String, Object>> manySendMessagePersonList = classManageDao.selectManySendMessagePerson(iMap);
            List<Map<String, Object>> classManagePerson = classManageDao.selectClassManagePerson();
            for (int i = 0; i < classManagePerson.size(); i++) {
                manySendMessagePersonList.add(classManagePerson.get(i));
            }
            List messageList = new ArrayList();
            String classroomContentTemp = messageDao.getContent("5");
            // 几个人
            for (int i = 0; i < manySendMessagePersonList.size(); i++){
                // 每人几条消息
                for (int j = 0; j < idsList.size(); j++) {
                    Map result3 = new HashMap();
                    String person_code = manySendMessagePersonList.get(i).get("person_code").toString();
                    String name = manySendMessagePersonList.get(i).get("name").toString();
                    String classroomContent = classroomContentTemp.replace("{name}", name).replace("{projectName}", nameList.get(j).toString());
                    result3.put("row_id", UUID.randomUUID().toString());
                    result3.put("person_code", person_code);
                    result3.put("message_content", classroomContent);
                    result3.put("type", "5");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String message_time = simpleDateFormat.format(new Date());
                    result3.put("message_time", message_time);
                    result3.put("is_reading", 0);
                    messageList.add(result3);
                }
            }
            if (messageList.size() > 0){
                // 修改所有课程的状态
                classManageDao.submitHadPk1(idsList);
                // 插入pm表消息(多条)
                classManageDao.insertPersonMessage(messageList);
                return new OpResult();
            } else {
                return new OpResult("请联系管理员,赋予安排教室的账号!");
            }
        }catch (RuntimeException e){
            log.error("提交课程管理列表:" + e.getMessage());
            throw new BusinessLevelException("提交课程管理列表", e);
        }
    }

    /**
     * 根据项目id查询
     * @param project_id
     * @return
     */
    @Override
    public ProjectInfo getProjectInfo(String project_id) {
        return classManageDao.getProjectInfo(project_id);
    }

    /**
     * 查询已存排课内容
     * @param map
     * @return
     */
    @Override
    public List<Map<String,String>> getSavedCourseByProjectId(Map<String, Object> map) {
        List<Map<String,String>> result = classManageDao.getSavedCourseByProjectId(map);
        return result;
    }

    /**
     * 查询已存排课内容,复制专用
     * @param map
     * @return
     */
    public List<Map<String,String>> getSavedCourseByProjectIdCopy(Map<String, Object> map) {
        if(map.get("project_id") == null || "".equals(map.get("project_id").toString())) {
            return new ArrayList<Map<String,String>>();
        }
        List<Map<String,String>> result = classManageDao.getSavedCourseByProjectIdCopy(map);
        return result;
    }

    public List<Map<String,String>> getSelectWeekSchedules(Map<String, Object> map) {
        List<Map<String,String>> result = classManageDao.selectTargetDateSchedules(map);
        return result;
    }

    /**
     * 回显系数
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public Map<String, Object> backCoefficient(Map map) throws BusinessLevelException{
        try{
            log.debug(map);
            Map result = new HashMap();
            Map map2 = classManageDao.backCoefficient(map);
            if (map2 == null){
                result.put("base_fee", "");
                result.put("person_factor", "1.00");
                result.put("course_factor", "1.00");
                return result;
            }
            return map2;
        }catch (RuntimeException e){
            log.error("无回显系数:" + e.getMessage());
            throw new BusinessLevelException("无回显系数", e);
        }
    }

    /**
     * 根据schedule_id获取当前课程信息
     * @param schedule_id
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public Map<String, Object> getProjectSchedulePlanSheet(String schedule_id) throws BusinessLevelException{
        try{
            Map<String, Object> classInfo = classManageDao.getProjectSchedulePlanSheet(schedule_id);
            return classInfo;
        }catch (RuntimeException e){
            log.error("获取当前课程信息:" + e.getMessage());
            throw new BusinessLevelException("获取当前课程信息失败", e);
        }
    }

    /**
     * 修改课程信息《保存》
     * @param projectSchedulePlan
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult saveEdit(Map projectSchedulePlan) throws BusinessLevelException {
        try {
            // 先查询 当前日期,当前时间段,是否存在当前老师的课程
            List<Map<String, Object>> sList = classManageDao.selectScheduleOrNotEdit(projectSchedulePlan);
            if (sList.size() > 0) {
                int count1 = sList.size();
                for (int i = 0; i < sList.size(); i++) {
                    Map<String, Object> sMap = sList.get(i);
                    if (count1 > 0 && sMap.get("teacher_code").toString().equals(projectSchedulePlan.get("teacher_code").toString())){
                        return new OpResult("当前时间该教师已排课！请重新选择");
                    }
                }
            }
            // 将3个系数 更新到所有同project_id,teacher_code 的课程下
            Map result = new HashMap();
            result.put("project_id", projectSchedulePlan.get("project_id").toString());
            result.put("course_name", projectSchedulePlan.get("course_name").toString());
            result.put("teacher_code", projectSchedulePlan.get("teacher_code").toString());
            result.put("base_fee", projectSchedulePlan.get("base_fee").toString());
            result.put("person_factor", projectSchedulePlan.get("person_factor").toString());
            result.put("course_factor", projectSchedulePlan.get("course_factor").toString());
            // 查询教师的3系数
            List ptkfList = classManageDao.selectFromPTKF(result);
            if (ptkfList.size() == 0){
                classManageDao.insertPtkf(result);
            } else {
                classManageDao.updatePtkf(result);
                // 将所有 当前项目，当前课程，当前老师 的系数都改变
            }

            // 查询课程名字courseInput,如果数据字典中不存在,则创建
            String courseInput = projectSchedulePlan.get("courseInput").toString();
            Map courseMap = classManageDao.selectCourseNameFromDataDic(courseInput);
            if (courseMap == null){
                // 创建课, 并插入当前课程信息
                Map course = new HashMap();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                course.put("code", UUID.randomUUID().toString());
                course.put("name", courseInput);
                course.put("parent_code", "25");
                course.put("note", courseInput);
                course.put("createTime", df.format(new Date()));
                course.put("isfixed", 0);
                // 插入课程
                classManageDao.createCourse(course);

                // 替换psp的map
                projectSchedulePlan.remove("course_name");
                projectSchedulePlan.put("course_name", course.get("code").toString());
            }
            // 将所有信息保存到psp表了
            int i = classManageDao.saveEdit(projectSchedulePlan);
            // 判断saveEdit是否执行成功
            if (i == -2147482646) {
                return new OpResult();
            } else {
                return new OpResult("修改失败, 请联系管理员查看情况!");
            }
        } catch (RuntimeException e) {
            log.error("修改课程信息:" + e.getMessage());
            throw new BusinessLevelException("修改课程信息", e);
        }
    }

    /**
     * 修改课程信息《删除》
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult deleteEdit(Map map) throws BusinessLevelException {
        try {
            classManageDao.deleteEdit(map);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("删除课程信息:" + e.getMessage());
            throw new BusinessLevelException("删除课程信息", e);
        }
    }

    /**
     * 添加课程信息《保存》
     * @param projectSchedulePlan
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult saveAdd(Map projectSchedulePlan) throws BusinessLevelException {
        try {
            // 先查询 当前日期,当前时间段,是否存在当前老师的课程
            Map sMap = classManageDao.selectScheduleOrNotAdd(projectSchedulePlan);
            if (sMap != null) {
                String count = sMap.get("count(0)").toString();
                int count1 = Integer.parseInt(count);
                if (count1 > 0){
                    return new OpResult("当前时间该教师已排课！请重新选择");
                }
            }

            String teacher_code = projectSchedulePlan.get("teacher_code").toString();
            projectSchedulePlan.put("teacher_code", teacher_code);

            String project_id = projectSchedulePlan.get("project_id").toString();
            String course_name = projectSchedulePlan.get("course_name").toString();
            if ("".equals(course_name)) {
                // 创建这个课
                // 查询课程名字courseInput,如果数据字典中不存在,则创建
                String courseInput = projectSchedulePlan.get("courseInput").toString();
                Map courseMap = classManageDao.selectCourseNameFromDataDic(courseInput);
                if (courseMap == null){
                    // 创建课, 并插入当前课程信息
                    Map course = new HashMap();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    course.put("code", UUID.randomUUID().toString());
                    course.put("name", courseInput);
                    course.put("parent_code", "25");
                    course.put("note", courseInput);
                    course.put("createTime", df.format(new Date()));
                    course.put("isfixed", 0);
                    // 插入课程
                    classManageDao.createCourse(course);

                    // 替换psp的map
                    projectSchedulePlan.remove("course_name");
                    projectSchedulePlan.put("course_name", course.get("code").toString());
                }
            }

            // 创建UUID schedule_id
            String schedule_id = UUID.randomUUID().toString();
            projectSchedulePlan.put("schedule_id", schedule_id);
            projectSchedulePlan.put("status", 0);

            // 将新输入的三个系数， 保存起来
            Map result = new HashMap();
            result.put("project_id", project_id);
            result.put("course_name", projectSchedulePlan.get("course_name"));
            result.put("teacher_code", teacher_code);
            result.put("base_fee", projectSchedulePlan.get("base_fee").toString());
            result.put("person_factor", projectSchedulePlan.get("person_factor").toString());
            result.put("course_factor", projectSchedulePlan.get("course_factor").toString());
            // 先查询是不是有project_teacher_kc_factor表里是否有 当前项目 和 当前课程 和 当前老师
            List ptkfList = classManageDao.selectFromPTKF(result);
            if (ptkfList.size() <= 0){
                classManageDao.insertPtkf(result);
            } else {
                classManageDao.updatePtkf(result);
            }

            classManageDao.saveAdd(projectSchedulePlan);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("添加课程信息:" + e.getMessage());
            throw new BusinessLevelException("添加课程信息", e);
        }
    }

    /**
     * 添加课程信息《保存》
     * @param projectSchedulePlan
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult saveCopyAdd(Map projectSchedulePlan) throws BusinessLevelException {
        try {
            // 先查询 当前日期,当前时间段,是否存在当前老师的课程
            Map sMap = classManageDao.selectScheduleOrNotAdd(projectSchedulePlan);
            if (sMap != null) {
                String count = sMap.get("count(0)").toString();
                int count1 = Integer.parseInt(count);
                if (count1 > 0){
                    return new OpResult("当前时间该教师已排课！请重新选择");
                }
            }

            String teacher_code = projectSchedulePlan.get("teacher_code").toString();
            projectSchedulePlan.put("teacher_code", teacher_code);

            String project_id = projectSchedulePlan.get("project_id").toString();
            String course_name = projectSchedulePlan.get("course_name").toString();

            // 将新输入的三个系数， 保存起来
            Map result = new HashMap();
            result.put("project_id", project_id);
            result.put("course_name", course_name);
            result.put("teacher_code", teacher_code);
            result.put("base_fee", projectSchedulePlan.get("base_fee").toString());
            result.put("person_factor", projectSchedulePlan.get("person_factor").toString());
            result.put("course_factor", projectSchedulePlan.get("course_factor").toString());
            // 先查询是不是有project_teacher_kc_factor表里是否有 当前项目 和 当前课程 和 当前老师
            List ptkfList = classManageDao.selectFromPTKF(result);
            if (ptkfList.size() <= 0){
                classManageDao.insertPtkf(result);
            } else {
                classManageDao.updatePtkf(result);
            }

            // 判断insert后的返回值
            classManageDao.saveAdd(projectSchedulePlan);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("添加课程信息:" + e.getMessage());
            throw new BusinessLevelException("添加课程信息", e);
        }
    }

    /**
     * 排教室数据列表
     */
    @Override
    public Map<String, Object> classRoomPlanList(Map map) throws BusinessLevelException {
        List<Map<String, Object>> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        try {
            list = classManageDao.classRoomPlanList(map);
        } catch (RuntimeException e) {
            log.error("获取排教室列表:" + e.getMessage());
            throw new BusinessLevelException("获取排教室列表失败", e);
        }

        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * 退回排课
     */
    @Override
    public OpResult returnPK(Map map) throws BusinessLevelException {

        if(map.get("projectId") == null || "".equals(map.get("projectId").toString())) {
            return new OpResult("请选择项目");
        }

        try {
            String idStr = map.get("projectId").toString();
            String project_names = map.get("projectName").toString();
            // 将所有project_id存入idsList
            List idsList = Arrays.asList(idStr.split(","));
            List nameList = Arrays.asList(project_names.split(","));

            String[] ids = idStr.split(",");
            map.put("ids", ids);
            Map iMap = new HashMap();
            iMap.put("list", idsList);

            // 查询给哪些用户发消息
            List<Map<String, Object>> manySendMessagePersonList = classManageDao.selectManySendMessagePersonPro(iMap);
            List<Map<String, Object>> manySendMessagePersonList2 = classManageDao.selectManySendMessagePersonPro2(iMap);
            for (int i = 0; i < manySendMessagePersonList2.size(); i++) {
                manySendMessagePersonList.add(manySendMessagePersonList2.get(i));
            }
            List messageList = new ArrayList();
            String classroomContentTemp = messageDao.getContent("6");
            // 几个人
            for (int i = 0; i < manySendMessagePersonList.size(); i++){
                // 每人几条消息
                for (int j = 0; j < idsList.size(); j++) {
                    Map result3 = new HashMap();
                    String person_code = manySendMessagePersonList.get(i).get("person_code").toString();
                    String name = manySendMessagePersonList.get(i).get("name").toString();
                    String classroomContent = classroomContentTemp.replace("{name}", name).replace("{projectName}", nameList.get(j).toString());
                    result3.put("row_id", UUID.randomUUID().toString());
                    result3.put("person_code", person_code);
                    result3.put("message_content", classroomContent);
                    result3.put("type", "6");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String message_time = simpleDateFormat.format(new Date());
                    result3.put("message_time", message_time);
                    result3.put("is_reading", 0);
                    messageList.add(result3);
                }
            }

            classManageDao.returnPK(map);
            if (messageList.size() > 0){
                // 插入pm表消息(多条)
                classManageDao.insertPersonMessage(messageList);
            }
            return new OpResult();
        } catch (Exception e) {
            throw new BusinessLevelException("退回排课失败", e);
        }

    }

    /**
     * 退回排课
     */
    @Override
    public OpResult returnPKA(Map map) throws BusinessLevelException {

        if(map.get("projectId") == null || "".equals(map.get("projectId").toString())) {
            return new OpResult("请选择项目");
        }

        try {
            String idStr = map.get("projectId").toString();
            String project_names = map.get("projectName").toString();
            // 将所有project_id存入idsList
            List idsList = Arrays.asList(idStr.split(","));
            List nameList = Arrays.asList(project_names.split(","));

            String[] ids = idStr.split(",");
            map.put("ids", ids);
            Map iMap = new HashMap();
            iMap.put("list", idsList);

            // 查询给哪些用户发消息
            List<Map<String, Object>> manySendMessagePersonList = classManageDao.selectManySendMessagePersonPro(iMap);
            List<Map<String, Object>> manySendMessagePersonList2 = classManageDao.selectManySendMessagePersonPro2(iMap);
            for (int i = 0; i < manySendMessagePersonList2.size(); i++) {
                manySendMessagePersonList.add(manySendMessagePersonList2.get(i));
            }
            List messageList = new ArrayList();
            String classroomContentTemp = messageDao.getContent("6");
            // 几个人
            for (int i = 0; i < manySendMessagePersonList.size(); i++){
                // 每人几条消息
                for (int j = 0; j < idsList.size(); j++) {
                    Map result3 = new HashMap();
                    String person_code = manySendMessagePersonList.get(i).get("person_code").toString();
                    String name = manySendMessagePersonList.get(i).get("name").toString();
                    String classroomContent = classroomContentTemp.replace("{name}", name).replace("{projectName}", nameList.get(j).toString());
                    result3.put("row_id", UUID.randomUUID().toString());
                    result3.put("person_code", person_code);
                    result3.put("message_content", classroomContent);
                    result3.put("type", "6");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String message_time = simpleDateFormat.format(new Date());
                    result3.put("message_time", message_time);
                    result3.put("is_reading", 0);
                    messageList.add(result3);
                }
            }

            classManageDao.returnPKA(map);
            if (messageList.size() > 0){
                // 插入pm表消息(多条)
                classManageDao.insertPersonMessage(messageList);
            }
            return new OpResult();
        } catch (Exception e) {
            throw new BusinessLevelException("退回排课失败", e);
        }

    }

    /**
     * 《排课》页面-提交按钮
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    @Override
    public OpResult submitBtn(Map map) throws BusinessLevelException {
        // 排课页面【提交】按钮仅修改psp表状态
        try {
            String project_id = map.get("project_id").toString();
            String grade_id = map.get("grade_id").toString();
            List list = new ArrayList();
            Map param = new HashMap();
            param.put("status", 1);
            param.put("project_id", project_id);
            param.put("grade_id", grade_id);
            list.add(param);
            Map result = new HashMap();
            result.put("list", list);

            // 查询给哪些用户发消息
            List<Map<String, Object>> sendMessagePersonList = classManageDao.selectSendMessagePerson(project_id);
            List<Map<String, Object>> classManagePerson = classManageDao.selectClassManagePerson();
            for (int i = 0; i < classManagePerson.size(); i++) {
                sendMessagePersonList.add(classManagePerson.get(i));
            }
            List messageList = new ArrayList();
            String classroomContentTemp = messageDao.getContent("5");
            for (int i = 0; i < sendMessagePersonList.size(); i++){
                Map result3 = new HashMap();
                result3.put("row_id", UUID.randomUUID().toString());
                String person_code = sendMessagePersonList.get(i).get("person_code").toString();
                String name = sendMessagePersonList.get(i).get("name").toString();
                String classroomContent = classroomContentTemp.replace("{name}", name).replace("{projectName}", map.get("project_name").toString());
                result3.put("person_code", person_code);
                result3.put("message_content", classroomContent);
                result3.put("type", "5");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String message_time = simpleDateFormat.format(new Date());
                result3.put("message_time", message_time);
                result3.put("is_reading", 0);

                messageList.add(result3);
            }
            if (messageList.size() > 0){
                // 修改psp表状态
                classManageDao.submitBtn(result);
                // 插入pm表消息(多条)
                classManageDao.insertPersonMessage(messageList);
                return new OpResult();
            } else {
                return new OpResult("请联系管理员,赋予安排教室的账号!");
            }
        } catch (RuntimeException e) {
            log.error("提交排课表失败:" + e.getMessage());
            throw new BusinessLevelException("提交排课表失败", e);
        }
    }

    /**
     * 根据teacher_name查询已存排课内容
     * @param classRoom
     * @param page
     * @param limit
     * @param classDate
     * @return
     */
    @Override
    public Map<String, Object> getClassRooms(ClassRoom classRoom, Integer page, Integer limit, String classDate) {
    	classRoom.setStatus(1);
        Map<String,Object> classRooms = new HashMap<>();
        List<ClassRoom> roomList = classRoomDao.getClassRooms(classRoom);
        List<ClassRoom> resultList = new ArrayList<ClassRoom>();
        classRooms.put("code", 0);
        classRooms.put("msg", "");
        if(roomList.size() <= 0) {
            classRooms.put("count",0);
        	classRooms.put("data", resultList);
        	return classRooms;
        }
        // 查询时间表
        Map<String, Object> timeTable = settingService.timeTable(new HashMap<String, Object>());
        List<Map<String,Object>> data = (List<Map<String, Object>>) timeTable.get("data");
        // 查询不可用时间
        List<Map<String,Object>> unTimeData = classRoomService.getUnTimeData(roomList,classDate);
//        List<Map<String,Object>> getUnTimeDataPsp = classRoomService.getUnTimeDataPsp(roomList,classDate);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        // 循环查出来的教室表
        for (ClassRoom classRoom2 : roomList) {
            List<String> result = new ArrayList<>();
            result.add(data.get(0).get("start_time").toString());
            String endTime = data.get(data.size()-1).get("end_time").toString();
            String roomId = classRoom2.getRoomId();
            int count = 0;
            int index = 0;
            for(int i = 0; i < unTimeData.size(); i++) {
                Map<String, Object> map = unTimeData.get(i);
                String room_id = map.get("room_id").toString();
                if(roomId.equals(room_id)) {
                    String start = map.get("start_time").toString();
                    String end = map.get("end_time").toString();
                    String lastTime = result.get(result.size() - 1);
                    try {
                        long startLong = format.parse(start).getTime();
                        long endLong = format.parse(end).getTime();
                        long endLong1 = format.parse(lastTime).getTime();
//                        boolean flag = (start.equals(lastTime)) || (endLong1 <= endLong && endLong1 >= startLong);
                        boolean flag = start.equals(lastTime);
                        if(flag) {
                            result.set(result.size() - 1,end);
                        }else {
                            result.add(start);
                            result.add(end);
                        }
                    }catch (Exception e) {

                    }

                }
            }
            if(result.contains(endTime)) {
                result.remove(result.size() - 1);
            }else {
                result.add(endTime);
            }
            for (int i = 0; i < result.size(); i = i + 2) {
//                s = s + result.get(i) + "-" + result.get(i + 1)+",";
                ClassRoom classRoom1 = new ClassRoom();
                classRoom1.setRoomLoc(classRoom2.getRoomLoc());
                classRoom1.setRoomNum(classRoom2.getRoomNum());
                classRoom1.setRoomType(classRoom2.getRoomType());
                classRoom1.setCreateTime(result.get(i) + "-" + result.get(i + 1));
                resultList.add(classRoom1);
            }




        }

//        if(getUnTimeDataPsp.size() > 0) {
//                for (int j = 0; j < resultList1.size(); j++) {
//                    String roomId1 = resultList1.get(j).getRoomId();
//                    String[] times = resultList1.get(j).getCreateTime().split("-");
//                    String start = times[0];
//                    String end = times[1];
//                        // roomId相等
//                        for (int i = 0; i < getUnTimeDataPsp.size(); i++) {
//                            Map map = getUnTimeDataPsp.get(i);
//                            String roomId = map.get("room_id").toString();
//                            if(roomId.equals(roomId1)) {
//                                // 已排时间
//                                String endTimeUnUse1 = map.get("e1").toString();
//                                String startTimeUnUse1 = map.get("t1").toString();
//                                try {
//                                    long startLong = format.parse(startTimeUnUse1).getTime();
//                                    long startLong1 = format.parse(start).getTime();
//                                    long endLong1 = format.parse(end).getTime();
//                                    if(startLong >= startLong1 && startLong <= endLong1) {
//
//                                    }
//                                }catch (Exception e) {
//
//                                }
//
//                            }
//                        }
//                    }
//
//                }


        com.joysupply.util.PageInfo pageInfo = new com.joysupply.util.PageInfo(page,limit);
        List<ClassRoom> list = new ArrayList<ClassRoom>();
        if(resultList.size() > 0) {
            if(resultList.size() < page*limit) {
                list = resultList.subList(pageInfo.getStart(), resultList.size());
            }else {
                list = resultList.subList(pageInfo.getStart(), page*limit);
            }
        }
        classRooms.put("data", list);
        classRooms.put("count", resultList.size());
        return classRooms;
    }

	private ClassRoom accept(ClassRoom classRoom2) {
		ClassRoom classRoom3 = new ClassRoom();
		classRoom3.setRoomLoc(classRoom2.getRoomLoc());
		classRoom3.setRoomId(classRoom2.getRoomId());
		classRoom3.setRoomType(classRoom2.getRoomType());
		classRoom3.setRoomNum(classRoom2.getRoomNum());
		return classRoom3;
	}

	   /**
     * 根据排课计划id查询该计划的教室信息
     * @param scheduleId
     * @return
     */
    @Override
    public ProjectSchedulePlan getClassRoomInfoByScheduleId(String scheduleId) {
    	ProjectSchedulePlan classRoomInfoByScheduleId = classManageDao.getClassRoomInfoByScheduleId(scheduleId);
        return classRoomInfoByScheduleId;
    }

    /**
     * 保存教室排课
     */
    @Transactional
    @Override
    public OpResult saveRoomPlan(String scheduleId,String roomId,String starTime,
                                 String endTime, String class_date,String year, String classRoomType)  throws BusinessLevelException{
        try {
            Map<String,Object> param = new HashMap<String, Object>();

            if(class_date != null && !"".equals(class_date)) {
                param.put("month", class_date.split("-")[1]);
                param.put("day", class_date.split("-")[2]);
            }
            param.put("starTime", starTime);
            param.put("endTime", endTime);
            param.put("year", year);
            param.put("roomId", roomId);
            param.put("classRoomType", classRoomType);
            Integer count = classManageDao.getUnUseCountBy(param);
            if(count != null && count > 0) {
                return new OpResult("当前时间教室不可用");
            }
            param.put("scheduleId", scheduleId);
            param.put("class_date", class_date);
            classManageDao.saveRoomPlan(param);
            return new OpResult();
        } catch (Exception e) {
            throw new BusinessLevelException("保存排教室失败", e);
        }

    }

    @Transactional
    @Override
    public OpResult copyRoomPlan(String ids, String startTime, String endTime, String projectId) {
        if(projectId == null || "".equals(projectId)) {
            return new OpResult("未找到项目");
        }
        String[] idsArr = null;
        if(ids != null && !"".equals(ids)) {
            idsArr = ids.split(",");
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("xyStart", startTime);
        param.put("xyEnd", endTime);
        param.put("project_id", projectId);
        param.put("ids", idsArr);
        param.put("nowPlan", "");
        param.put("isPlan", "1");
        Integer count = classManageDao.listIsPlanByProject(param);
        if(count > 0) {
            return new OpResult("您选择的日期内已经有安排教室<br>请重新选择日期范围");
        }
        // 查询选择时间段内的排课信息
        List<Map<String,String>> list1 = getSavedCourseByProjectId(param);
        param.clear();
        param.put("project_id", projectId);
        param.put("ids", idsArr);
        param.put("nowPlan", "1");
        // 查询当前周课程的排教室情况
        List<Map<String,String>> list2 = getSavedCourseByProjectId(param);
        List<Map<String,String>> params = new ArrayList<Map<String,String>>();
        for (Map<String,String> map1 : list2) {
            String weekDay = map1.get("week_day");
            String start = map1.get("start_time");
            String end = map1.get("end_time");
            String classRoomType = map1.get("class_room_type");
            for (Map<String,String> map2 : list1) {
                String weekDay1 = map2.get("week_day");
                String start2 = map2.get("start_time");
                String end2 = map2.get("end_time");
                String classRoomType1 = map2.get("class_room_type");
                // 如果时间相对，教室类型相等，保存
                if(weekDay.equals(weekDay1) && start.equals(start2) && end.equals(end2) && classRoomType.equals(classRoomType1)) {
                    // 查询是否已排课或设置不可用
                    String classDate = map2.get("class_date").toString();
                    param.clear();
                    param.put("startTime",classDate);
                    param.put("endTime",classDate);
                    param.put("start",start2);
                    param.put("end",end2);
                    param.put("roomNum",map1.get("class_room_id"));
                    List<String> weeks = new ArrayList<>();
                    weeks.add(weekDay1);
                    param.put("weeks",weeks);
                    Map<String,String> map  = new HashMap<String, String>();
                    map.put("schedule_id", map2.get("schedule_id"));
                    map.put("class_room_id",  map1.get("class_room_id"));
                    params.add(map);
                }
            }
        }
        try {
            if(params.size() <=0) {
                return new OpResult("没有需要排教室的课程");
            }
            classManageDao.copyRoomPlan(params);
            return new OpResult();
        } catch (Exception e) {
            throw new BusinessLevelException("复制课表失败", e);
        }
    }

    /**
     * 复制排课2
     * @param ids 所有schedule_id
     * @param startTime 选择的开始时间
     * @param endTime 选择的结束时间
     * @param projectId 当前项目的id
     * @return
     */
    @Transactional
    @Override
    public OpResult copyClassPlan(String ids, String startTime, String endTime, String projectId, String projectStartTime, String projectEndTime, String weekStartDate, String gradeId) {
        // 定义ids的数组(ids是所有schedule_id)
        String[] idsArr = null;
        if(ids != null && !"".equals(ids)) {
            idsArr = ids.split(",");
        }
        Map<String, Object> param = new HashMap<String, Object>();

        // 传入当前周所有课程
        param.put("ids", idsArr);
        param.put("project_id", projectId);
        param.put("grade_id", gradeId);
        // 查询当前周课程的排课情况
        List<Map<String,String>> nowWeekList = getSavedCourseByProjectIdCopy(param);

        param.clear();

        Set teacherSet = new HashSet();
        // 遍历出当前周的所有教师
        for (int i = 0; i < nowWeekList.size(); i++) {
            Map<String, String> teacherMap = nowWeekList.get(i);
            String teacherCode = teacherMap.get("teacher_code").toString();
            teacherSet.add(teacherCode);
        }
        List teacherList = new ArrayList(teacherSet);

        param.put("teacherList", teacherList);
        // 选择的开始时间
        param.put("xyStart", startTime);
        // 选择的结束时间
        param.put("xyEnd", endTime);
//        param.put("project_id", projectId);
        // 查询选择时间段内, 所有项目的排课信息
        List<Map<String,String>> selectTimeList = getSelectWeekSchedules(param);

        List<Map<String, Object>> params = new ArrayList<>();
        int days = DateUtil.getDays("yyyy-MM-dd", startTime, endTime);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        // 获得一个日历
        Calendar cal = Calendar.getInstance();
        try {
        	for (int i = 0; i <= days; i++) {
				cal.setTime(f.parse(startTime));
				cal.add(Calendar.DAY_OF_MONTH, i);
                // 目标日期
				String today = f.format(cal.getTime());
                // 目标星期
				String weekDay = DateUtil.dateToWeekDay(cal.getTime());
				for(int j = 0; j < nowWeekList.size(); j++){
				    Map result = new HashMap();
                    Map pice = new HashMap();
                    String week_day1 = nowWeekList.get(j).get("week_day");
                    String start_time1 = nowWeekList.get(j).get("start_time");
                    int flagStartTime1 = Integer.parseInt(start_time1.replace(":", ""));
                    String end_time1 = nowWeekList.get(j).get("end_time");
                    int flagEndTime1 = Integer.parseInt(end_time1.replace(":", ""));
                    String classDate1 = nowWeekList.get(j).get("class_date");
                    String schedule_id = nowWeekList.get(j).get("schedule_id");
                    String project_id = nowWeekList.get(j).get("project_id");
                    String teacher_code = nowWeekList.get(j).get("teacher_code");
                    String course_name = nowWeekList.get(j).get("course_name_fz");
                    String grade_id1 = nowWeekList.get(j).get("grade_id");
                    String base_fee = nowWeekList.get(j).get("base_fee");
                    String person_count = String.valueOf(nowWeekList.get(j).get("person_count"));
                    String person_factor = nowWeekList.get(j).get("person_factor");
                    String course_factor = nowWeekList.get(j).get("course_factor");
                    String fee_total = nowWeekList.get(j).get("fee_total");
                    String class_room_type = nowWeekList.get(j).get("class_room_type");
                    // 第一对比标志
                    String templetA = teacher_code + week_day1 + start_time1 + end_time1 + grade_id1;
                    String dayTempletA = teacher_code + week_day1;
//                    if(weekDay.equals(week_day1) && !classDate.equals(today)) {
                    if(weekDay.equals(week_day1)) {
                        boolean flag = false;
                        if (classDate1.equals(today)) {

                        } else {
                            flag = true;
                            for (int k = 0; k < selectTimeList.size(); k++){
                                String course_name2 = selectTimeList.get(k).get("course_name_fz");
                                String teacher_code2 = selectTimeList.get(k).get("teacher_code");
                                String grade_id2 = selectTimeList.get(k).get("grade_id");
                                String week_day2 = selectTimeList.get(k).get("week_day");
                                String start_time2 = selectTimeList.get(k).get("start_time");
                                int flagStartTime2 = Integer.parseInt(start_time2.replace(":", ""));
                                String end_time2 = selectTimeList.get(k).get("end_time");
                                int flagEndTime2 = Integer.parseInt(end_time2.replace(":", ""));
                                String classDate2 = selectTimeList.get(k).get("class_date");
                                // 第一对比标志
                                // 课程名称+老师名称+日期+开始时间+结束时间
                                String templetB = teacher_code2 + week_day2 + start_time2 + end_time2 + grade_id2;
                                String dayTempletB = teacher_code2 + week_day2;
                                // 可复制的条件:
                                if (templetA.equals(templetB)){
                                    if (today.equals(classDate2)) {
                                        flag = false;
                                        break;
                                    }
                                }

                                // 课程名称+老师名称+日期 都相等的情况下
                                if (dayTempletA.equals(dayTempletB)) {
                                    if (today.equals(classDate2)) {
                                        // 看看长时段是否包含
                                        // flagStartTime1 : 当前周的课的开始时间
                                        // flagEndTime1 : 当前周的课的结束时间
                                        // flagStartTime2 : 所选时间的开始时间
                                        // flagEndTime2 : 所选时间的结束时间
                                        if (flagStartTime1 > flagStartTime2 && flagStartTime1 < flagEndTime2) {
                                            flag = false;
                                            break;
                                        } else if (flagStartTime1 == flagStartTime2) {
                                            flag = false;
                                            break;
                                        } else if (flagStartTime1 < flagStartTime2 && flagEndTime1 < flagEndTime2 && flagEndTime1 > flagStartTime2) {
                                            flag = false;
                                            break;
                                        } else if (flagStartTime1 < flagStartTime2 && flagEndTime1 > flagEndTime2) {
                                            flag = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        // 满足条件，封装数据
                        if(flag) {
                            // 转换时间
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date projectStart = simpleDateFormat.parse(projectStartTime);
                                Date projectEnd = simpleDateFormat.parse(projectEndTime);
                                Date todayDate = simpleDateFormat.parse(today);
                                if (projectStart.getTime() <= todayDate.getTime() && projectEnd.getTime() >= todayDate.getTime()){
                                    System.out.println("满足条件"+today);
                                    result.put("schedule_id", UUID.randomUUID().toString());
                                    result.put("project_id", project_id);
                                    result.put("grade_id", grade_id1);
                                    result.put("course_name", course_name);
                                    result.put("teacher_code", teacher_code);
                                    result.put("class_room_type", class_room_type);
                                    result.put("class_date", today);
                                    result.put("start_time", start_time1);
                                    result.put("end_time", end_time1);
                                    result.put("base_fee", base_fee);
                                    result.put("person_count", person_count);
                                    result.put("person_factor", person_factor);
                                    result.put("course_factor", course_factor);
                                    result.put("fee_total", fee_total);
                                    result.put("week_day", weekDay);
                                    result.put("status", "0");
                                    pice.putAll(result);
                                    params.add(pice);
                                } else {
                                    log.error("不在项目时间范围内！");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
				}
        	}
        } catch (ParseException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        try {
            if (params.size() == 0) {
                return new OpResult("复制课表失败 ! 请检查日期或其他项目 !");
            }else {
                classManageDao.copyClassPlan(params);
                return new OpResult();
            }
        } catch (Exception e) {
            throw new BusinessLevelException("复制课表失败", e);
        }
    }

	@Override
	public Map<String, Object> getPlanByTeacher(String teacherCode,
			String dateTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("teacher_code", teacherCode);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			c.setTime(sdf.parse(dateTime));
			c.set(Calendar.DAY_OF_MONTH, 1);
			param.put("xyStart", sdf.format(c.getTime()));
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			param.put("xyEnd", sdf.format(c.getTime()));
			param.put("myClass", "1");
			param.put("myClass1", "1");
			List<Map<String,Object>> weekSchedule = scheduleViewService.getWeekSchedule(param);
			param.clear();
			param.put("schedule", weekSchedule);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return param;
	}

    @Override
    public int getPkCount(String project_id) {
        return classManageDao.getPkCount(project_id);
    }

    /**
     * 根据教师查询是否已排课
     * @param teacherCode
     * @return
     */
    @Override
    public int getPkCountByTeacherCode(String teacherCode) {
        return classManageDao.getPkCountByTeacherCode(teacherCode);
    }

    @Override
    public List<Map<String,String>> getHadCountKcTime(String project_id) {
        if(project_id == null || "".equals(project_id)) {
            return new ArrayList<Map<String,String>>();
        }
        List<Map<String,String>> result = classManageDao.getHadCountKcTime(project_id);
        return result;
    }

    @Override
    public List<Map<String, String>> getTempPlan(Map<String, Object> param) {
        return classManageDao.getTempPlan(param);
    }

    @Override
    public Map selectBlank(Map map) {
        try{
            log.debug(map);
            Map result = classManageDao.selectBlank(map);
            return result;
        } catch (Exception e) {
            throw new BusinessLevelException("复制课表失败", e);
        }
    }

    @Override
    public Map selectBlank2(Map map) {
        try{
            log.debug(map);
            Map result = classManageDao.selectBlank2(map);
            return result;
        } catch (Exception e) {
            throw new BusinessLevelException("复制课表失败", e);
        }
    }

    @Override
    public String isPlan(Map<String, Object> param) {
        return classManageDao.isPlan(param);
    }

    @Override
    public Map getNowScheduleOrNot(Map param) {
        try {
            log.debug(param);
            Map nowMap = classManageDao.getNowScheduleOrNot(param);
            return nowMap;
        } catch (Exception e) {
            throw new BusinessLevelException("失败", e);
        }
    }

    @Override
    public Map getTeacherSchedule(Map param) {
        try {
            log.debug(param);
            Map tMap = classManageDao.getTeacherSchedule(param);
            return tMap;
        } catch (Exception e) {
            throw new BusinessLevelException("失败", e);
        }
    }

    @Override
    public int getProjectStartPersonCount(String project_id) {
        try {
            log.debug(project_id);
            int start_person_count = classManageDao.getProjectStartPersonCount(project_id);
            return start_person_count;
        } catch (Exception e) {
            throw new BusinessLevelException("失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> selectTeacher(Map map) {
        List<Map<String, Object>> selectTeacherList = classManageDao.selectTeacher(map);
        return selectTeacherList;
    }

    @Override
    public Map getYstartAndYend(String selectStartTime) throws BusinessLevelException {
        try {
            log.debug(selectStartTime);
            Map map = classManageDao.getYstartAndYend(selectStartTime);
            return map;
        } catch (Exception e) {
            throw new BusinessLevelException("失败", e);
        }
    }

    @Override
    public OpResult validate(String projectId) throws BusinessLevelException {
        try {
            log.debug(projectId);
            List<Map<String, Object>> result = classManageDao.validate(projectId);
            if (result.size() == 1) {
                String status = result.get(0).get("status").toString();
                if ("1".equals(status)) {
                    // 已提交
                    return new OpResult();
                } else {
                    return new OpResult("notSubmit");
                }
            } else {
                return new OpResult("notSubmit");
            }
        } catch (Exception e) {
            throw new BusinessLevelException("失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getGradeClassA(Map<String, Object> map) {
        List<Map<String, Object>> gradeClassList = classManageDao.getGradeClassA(map);
        return gradeClassList;
    }

    @Override
    public List<Map<String, Object>> getGradeClassB(Map<String, Object> map) {
        List<Map<String, Object>> gradeClassList = classManageDao.getGradeClassB(map);
        return gradeClassList;
    }

    @Override
    public Map<String, Object> getGradeListA(Map<String, Object> map) {
        List<Map<String, Object>> list = null;
        List gradeIdList = new ArrayList();
        Map gradeIdMap = new HashMap();
        try {
            list = classManageDao.getGradeListA(map);

            Map<String, Object> gMap = list.get(0);
            gMap.put("projectId", map.get("projectId").toString());
            String gradeId = gMap.get("gradeId").toString();
            gradeIdMap.put("gradeId", gradeId);
            gradeIdList.add(gradeIdMap);

            List gradePersonCount = classManageDao.getGradePersonCount(gradeIdList);
            a : for (int i = 0; i < gradePersonCount.size(); i++) {
                Map gpcMap = (Map) gradePersonCount.get(i);

                b : for (int j = i; j < list.size(); j++) {
                    Map<String, Object> finalMap = list.get(i);
                    finalMap.put("pkNum", gpcMap.get("pkNum").toString());
                    finalMap.put("pjsNum", gpcMap.get("pjsNum").toString());
                    finalMap.put("status", gpcMap.get("status").toString());
                    break b;
                }
            }

        } catch (RuntimeException e) {
            log.error("获取班级管理列表:" + e.getMessage());
            throw new BusinessLevelException("获取课程管理列表", e);
        }
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public Map<String, Object> getGradeListB(Map<String, Object> map) {
        List<Map<String, Object>> list = null;
        List gradeIdList = new ArrayList();

        try {
            list = classManageDao.getGradeListB(map);

            for (int i = 0; i < list.size(); i++) {
                Map gradeIdMap = new HashMap();
                Map<String, Object> gMap = list.get(i);
                gMap.put("projectId", map.get("projectId").toString());

                String gradeId = gMap.get("gradeId").toString();
                gradeIdMap.put("gradeId", gradeId);
                gradeIdList.add(i, gradeIdMap);
            }

            List gradePersonCount = classManageDao.getGradePersonCount(gradeIdList);
//            for (int i = 0; i < gradePersonCount.size(); i++) {
//                Map tMap = (Map) gradePersonCount.get(i);
//                String status = tMap.get("status").toString();
//                if (!"1".equals(status)) {
//                    gradePersonCount.remove(i);
//                }
//            }

            a : for (int i = 0; i < list.size(); i++) {
                Map gpcMap = list.get(i);
                String gProjectId = gpcMap.get("gradeId").toString();
                b : for (int j = 0; j < gradePersonCount.size(); j++) {
                    Map fMap = (Map) gradePersonCount.get(j);
                    String fProjectId = fMap.get("grade_id").toString();
                    if (gProjectId.equals(fProjectId)) {
                        gpcMap.put("pkNum", fMap.get("pkNum").toString());
                        gpcMap.put("pjsNum", fMap.get("pjsNum").toString());
                        gpcMap.put("status", fMap.get("status").toString());
                        break b;
                    }
                }
            }
        } catch (RuntimeException e) {
            log.error("获取班级管理列表:" + e.getMessage());
            throw new BusinessLevelException("获取课程管理列表", e);
        }
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public Map<String, Object> getGradeListAPjs(Map<String, Object> map) {
        List<Map<String, Object>> list = null;
        List gradeIdList = new ArrayList();
        Map gradeIdMap = new HashMap();
        try {
            list = classManageDao.getGradeListA(map);

            Map<String, Object> gMap = list.get(0);
            gMap.put("projectId", map.get("projectId").toString());
            String gradeId = gMap.get("gradeId").toString();
            gradeIdMap.put("gradeId", gradeId);
            gradeIdList.add(gradeIdMap);

            List gradePersonCount = classManageDao.getGradePersonCountPjs(gradeIdList);
            a : for (int i = 0; i < gradePersonCount.size(); i++) {
                Map gpcMap = (Map) gradePersonCount.get(i);

                b : for (int j = i; j < list.size(); j++) {
                    Map<String, Object> finalMap = list.get(i);
                    finalMap.put("pkNum", gpcMap.get("pkNum").toString());
                    finalMap.put("pjsNum", gpcMap.get("pjsNum").toString());
                    finalMap.put("status", gpcMap.get("status").toString());
                    break b;
                }
            }

        } catch (RuntimeException e) {
            log.error("获取班级管理列表:" + e.getMessage());
            throw new BusinessLevelException("获取课程管理列表", e);
        }
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public Map<String, Object> getGradeListBPjs(Map<String, Object> map) {
        List<Map<String, Object>> list = null;
        List gradeIdList = new ArrayList();

        List resultList = new ArrayList();

        try {
            list = classManageDao.getGradeListB(map);

            for (int i = 0; i < list.size(); i++) {
                Map gradeIdMap = new HashMap();
                Map<String, Object> gMap = list.get(i);
                gMap.put("projectId", map.get("projectId").toString());

                String gradeId = gMap.get("gradeId").toString();
                gradeIdMap.put("gradeId", gradeId);
                gradeIdList.add(i, gradeIdMap);
            }

            List gradePersonCount = classManageDao.getGradePersonCountPjs(gradeIdList);

            a : for (int i = 0; i < list.size(); i++) {
                Map gpcMap = list.get(i);
                String gProjectId = gpcMap.get("gradeId").toString();
                b : for (int j = 0; j < gradePersonCount.size(); j++) {
                    Map fMap = (Map) gradePersonCount.get(j);
                    String fProjectId = fMap.get("grade_id").toString();
                    if (gProjectId.equals(fProjectId)) {
                        gpcMap.put("pkNum", fMap.get("pkNum").toString());
                        gpcMap.put("pjsNum", fMap.get("pjsNum").toString());
                        gpcMap.put("status", fMap.get("status").toString());
                        resultList.add(gpcMap);
                        break b;
                    }
                }
            }

        } catch (RuntimeException e) {
            log.error("获取班级管理列表:" + e.getMessage());
            throw new BusinessLevelException("获取课程管理列表", e);
        }
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageInfo page = new PageInfo(resultList);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", resultList);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public Map<String, Object> getAllGradeInfo(Map resultMap) {
        List<Map<String, Object>> allGradeInfo = classManageDao.getAllGradeInfo(resultMap);

        List<Map<String, Object>> bmGradeInfo = classManageDao.getBmGradeInfo(resultMap);
        if (allGradeInfo.size() > 0) {
            Map<String, Object> gradeMap = allGradeInfo.get(0);
            return gradeMap;
        }

        if (bmGradeInfo.size() > 0) {
            Map<String, Object> gradeMap = bmGradeInfo.get(0);
            return gradeMap;
        }
        return resultMap;
    }

    @Override
    public OpResult fenban() {
        Map map = new HashMap();
        // 获取所有grade_info项目
        List<Map<String, Object>> allGradeInfo = classManageDao.getAllGradeInfo(map);
        // 获取所有一期项目
        List<Map<String, Object>> allKcInfo = classManageDao.getAllKcInfo();
        // 获取所有二期项目
        List<Map<String, Object>> allBmInfo = classManageDao.getAllBmInfo();

        // 去除一期中, 与grade_info重复的项目
        a: for (int i = 0; i < allGradeInfo.size(); i++) {
            Map aMap = allGradeInfo.get(i);
            String projectIdA = aMap.get("project_id").toString();
            b : for (int j = 0; i < allKcInfo.size(); j++) {
                Map bMap = allKcInfo.get(j);
                String projectIdB = bMap.get("project_id").toString();
                if (projectIdA.equals(projectIdB)) {
                    allKcInfo.remove(j);
                    break b;
                }
            }
        }

        // 去除一期中, 与二期project_code相同的项目
        c : for (int i = 0; i < allBmInfo.size(); i++) {
            Map cMap = allBmInfo.get(i);
            String projectCodeA = cMap.get("project_code").toString();
            d : for (int j = 0; i < allKcInfo.size(); j++) {
                Map dMap = allKcInfo.get(j);
                String projectCodeB = dMap.get("project_code").toString();
                if (projectCodeA.equals(projectCodeB)) {
                    allKcInfo.remove(j);
                    break d;
                }
            }
        }

        List gradeList = new ArrayList();
        for (int i = 0; i < allKcInfo.size(); i++) {
            Map gradeMap = allKcInfo.get(i);
            Map gMap = new HashMap();
            gMap.put("grade_id", UUID.randomUUID().toString());
            gMap.put("grade_name", "");
            gMap.put("grade_code", gradeMap.get("project_code").toString() + "1");
            gMap.put("project_id", gradeMap.get("project_id").toString());
            gMap.put("person_count", gradeMap.get("start_person_count").toString());
            gradeList.add(gMap);
        }

        classManageDao.insertGradeInfo(gradeList);
        return null;
    }

}
