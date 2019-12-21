package com.joysupply.service;

import com.joysupply.entity.ClassRoom;
import com.joysupply.entity.ProjectInfo;
import com.joysupply.entity.ProjectSchedulePlan;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 课程管理业务层
 *
 * @author Administrator
 */
public interface ClassManageService {

    /**
     * 课程管理列表
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    Map<String, Object> getClassManageList(Map map) throws BusinessLevelException;

    Map<String, Object> getClassManageListA(Map map) throws BusinessLevelException;

    Map<String, Object> getGradeListA(Map<String, Object> map) throws BusinessLevelException;

    Map<String, Object> getGradeListB(Map<String, Object> map) throws BusinessLevelException;

    Map<String, Object> getGradeListAPjs(Map<String, Object> map) throws BusinessLevelException;

    Map<String, Object> getGradeListBPjs(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 根据id查询项目
     * @param project_id
     * @return
     */
    ProjectInfo getProjectInfo(String project_id) throws BusinessLevelException;

    /**
     * 查询已存排课内容
     * @param map
     * @return
     */
    List<Map<String,String>> getSavedCourseByProjectId(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 根据schedule_id查询当前课程信息
     * @param schedule_id
     * @return
     */
    Map<String, Object> getProjectSchedulePlanSheet(String schedule_id) throws BusinessLevelException;

    /**
     * 排教室列表
     * @param map
     * @return
     */
    Map<String, Object> classRoomPlanList(Map map) throws BusinessLevelException;

    /**
     * 退回排课
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    OpResult returnPK(Map map) throws BusinessLevelException;

    OpResult returnPKA(Map map) throws BusinessLevelException;

    /**
     * 修改课程信息页面《保存》
     * @param projectSchedulePlan
     * @return
     * @throws BusinessLevelException
     */
    OpResult saveEdit(Map projectSchedulePlan) throws BusinessLevelException;

    /**
     * 修改课程信息页面《删除》
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    OpResult deleteEdit(Map map) throws BusinessLevelException;

    /**
     * 添加课程信息页面《保存》
     * @param projectSchedulePlan
     * @return
     * @throws BusinessLevelException
     */
    OpResult saveAdd(Map projectSchedulePlan) throws BusinessLevelException;

    /**
     * 拖动复制《添加课程》
     * @param projectSchedulePlan
     * @return
     * @throws BusinessLevelException
     */
    OpResult saveCopyAdd(Map projectSchedulePlan) throws BusinessLevelException;

    /**
     * 《排课》页面-提交按钮
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    OpResult submitBtn(Map map) throws BusinessLevelException;

    /**
     * 空闲时间搜索列表
     * @param classRoom
     * @param page
     * @param limit
     * @param classDate
     * @return
     */
    Map<String, Object> getClassRooms(ClassRoom classRoom, Integer page, Integer limit, String classDate) throws BusinessLevelException;
    
    /**
     * // 根据排课计划id查询该计划的教室信息
     * @param scheduleId
     * @return
     */
    ProjectSchedulePlan getClassRoomInfoByScheduleId(String scheduleId) throws BusinessLevelException;
    

    /**
     * 保存教室排课
     * @param scheduleId
     * @param roomId
     * @param class_date
     * @param endTime
     * @param starTime
     * @param year
     * @return
     */
    OpResult saveRoomPlan(String scheduleId, String roomId, String starTime, String endTime, String class_date, String year,String classRoomType) throws BusinessLevelException;

    /**
     * 复制教室排课
     * @param ids
     * @param startTime
     * @param endTime
     * @param projectId
     * @return
     */
    OpResult copyRoomPlan(String ids, String startTime, String endTime, String projectId) throws BusinessLevelException;

    /**
     * 复制教室排课2
     * @param ids
     * @param startTime
     * @param endTime
     * @param projectId
     * @return
     */
    OpResult copyClassPlan(String ids, String startTime, String endTime, String projectId, String projectStartTime, String projectEndTime, String weekStartDate, String gradeId) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 提交所有选中（修改已排课程状态）
     * @Date 上午 9:57 2018/12/15 0015
     * @Param [map]
     * @return com.joysupply.util.OpResult
     **/
    OpResult submitHadPk(Map map) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 回显系数
     * @Date 上午 9:57 2018/12/15 0015
     * @Param [map]
     * @return java.util.Map
     **/
    Map backCoefficient(Map map) throws BusinessLevelException;
    
    /**
     * 查询教师课表日历
     * @param teacherCode
     * @param dateTime
     * @return
     */
	Map<String, Object> getPlanByTeacher(String teacherCode, String dateTime) throws BusinessLevelException;

	/**
	 * @Author Dreamer
	 * @Description 查询该项目已排课数量
	 * @Date 上午 9:58 2018/12/15 0015
	 * @Param [project_id]
	 * @return int
	 **/
    int getPkCount(String project_id) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 根据教师code查询排课数量
     * @Date 上午 9:59 2018/12/15 0015
     * @Param [teacherCode]
     * @return int
     **/
    int getPkCountByTeacherCode(String teacherCode) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 查看已排课酬时间
     * @Date 上午 9:59 2018/12/15 0015
     * @Param [project_id]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String, String>> getHadCountKcTime(String project_id) throws BusinessLevelException;

    /**
     * 查询临时排课内容
     * @param param
     * @return
     */
    List<Map<String,String>> getTempPlan(Map<String,Object> param) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 查看系数
     * @Date 上午 10:00 2018/12/15 0015
     * @Param [map]
     * @return java.util.Map
     **/
    Map selectBlank(Map map) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 查看系数2
     * @Date 上午 10:00 2018/12/15 0015
     * @Param [map]
     * @return java.util.Map
     **/
    Map selectBlank2(Map map) throws BusinessLevelException;

    String isPlan(Map<String,Object> param) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 查看当前项目是否有课程
     * @Date 上午 10:00 2018/12/15 0015
     * @Param [param]
     * @return java.util.Map
     **/
    Map getNowScheduleOrNot(Map param) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 获取当前教师的课程
     * @Date 上午 10:01 2018/12/15 0015
     * @Param [param]
     * @return java.util.Map
     **/
    Map getTeacherSchedule(Map param) throws BusinessLevelException;

    int getProjectStartPersonCount(String project_id) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 获取教师列表
     * @Date 上午 10:01 2018/12/15 0015
     * @Param [map]
     * @return java.util.List
     **/
    List selectTeacher(Map map) throws BusinessLevelException;

    Map getYstartAndYend(String selectStartTime) throws BusinessLevelException;

    OpResult validate(String projectId) throws BusinessLevelException;

    List<Map<String, Object>> getGradeClassA(Map<String, Object> map) throws BusinessLevelException;

    List<Map<String, Object>> getGradeClassB(Map<String, Object> map) throws BusinessLevelException;

    OpResult fenban() throws BusinessLevelException;

    Map<String, Object> getAllGradeInfo(Map resultMap) throws BusinessLevelException;
}
