package com.joysupply.dao;

import com.joysupply.entity.ProjectInfo;
import com.joysupply.entity.ProjectSchedulePlan;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 课程管理数据层
 *
 * @author Administrator
 */
public interface ClassManageDao {

	/**
	 * 获取课程管理列表
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	List<Map<String, Object>> getClassManageList(Map map) throws RuntimeException;

	List<Map<String, Object>> getClassManageListA(Map map) throws RuntimeException;

	List<Map<String, Object>> getGradeListA(Map<String, Object> map) throws RuntimeException;

	List<Map<String, Object>> getGradeListB(Map<String, Object> map) throws RuntimeException;

	/**
	 * 根据项目id查询
	 * @param project_id
	 * @return
	 * @throws RuntimeException
	 */
	ProjectInfo getProjectInfo(String project_id) throws RuntimeException;


	/**
	 * 查询已存排课内容
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getSavedCourseByProjectId(Map<String, Object> map) throws RuntimeException;

	/**
	 * 查询已存排课内容，复制专用
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getSavedCourseByProjectIdCopy(Map<String, Object> map) throws RuntimeException;

	List<Map<String,String>> selectTargetDateSchedules(Map<String, Object> map) throws RuntimeException;

	/**
	 * @Author Dreamer
	 * @Description 获取课表计划
	 * @Date 上午 10:07 2018/12/15 0015
	 * @Param [schedule_id]
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	Map<String, Object> getProjectSchedulePlanSheet(String schedule_id) throws RuntimeException;

	/**
	 * 排教室列表
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	List<Map<String, Object>> classRoomPlanList(Map map) throws RuntimeException;

	/**
	 * 退回排课
	 * @param map
	 * @throws RuntimeException
	 */
	void returnPK(Map map) throws RuntimeException;

	void returnPKA(Map map) throws RuntimeException;

	/**
	 * 修改课程信息《保存》
	 * @param projectSchedulePlan
	 * @return
	 * @throws RuntimeException
	 */
	int saveEdit(Map projectSchedulePlan) throws RuntimeException;

	/**
	 * 修改课程信息《删除》
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	int deleteEdit(Map map) throws RuntimeException;

	/**
	 * 添加课程信息《保存》
	 * @param projectSchedulePlan
	 * @return
	 * @throws RuntimeException
	 */
	int saveAdd(Map projectSchedulePlan) throws RuntimeException;

	/**
	 * 《排课》页面-提交按钮
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	int submitBtn(Map map) throws RuntimeException;

	/**
	 * 修改列表状态
	 * @param result2
	 * @return
	 */
	int updateListStatus(Map result2) throws RuntimeException;

	/**
	 * 根据教师姓名，获取课程信息
	 * @param teacher_name
	 * @return
	 */
	ProjectSchedulePlan getCourseByTeacherName(String teacher_name) throws RuntimeException;

	/**
	 * 根据teacher_name查询已存排课内容
	 * @param map
	 * @return
	 */
	List<ProjectSchedulePlan> getSavedCourseByTeacherName(Map<String, Object> map) throws RuntimeException;
	

	/**
	 * 根据排课计划id查询该计划的教室信息
	 * @param scheduleId
	 * @return
	 */
	ProjectSchedulePlan getClassRoomInfoByScheduleId(String scheduleId) throws RuntimeException;
	
	/**
	 * 根据roomid和时间段查询不可用教室数量
	 * @param param
	 * @return
	 */
	Integer getUnUseCountBy(Map<String, Object> param) throws RuntimeException;
	
	/**
	 * 查询该教室是否已排
	 * @param param
	 * @return
	 */
	String isPlan(Map<String, Object> param) throws RuntimeException;
	
	/**
	 * 保存课程教室排课
	 * @param param
	 */
	void saveRoomPlan(Map<String, Object> param) throws RuntimeException;
	

	/**
	 * 教室复制排课
	 * @param params
	 */
	void copyRoomPlan(List<Map<String, String>> params) throws RuntimeException;

	/**
	 * 复制排课2
     * @param params
     */
	int copyClassPlan(List<Map<String, Object>> params) throws RuntimeException;

	Map backCoefficient(Map map) throws RuntimeException;

    int getPkCount(String project_id) throws RuntimeException;

	List selectFromPTKF(Map result) throws RuntimeException;

	int insertPtkf(Map result) throws RuntimeException;

	int updatePtkf(Map result) throws RuntimeException;

    int submitHadPk1(List idsList) throws RuntimeException;

	/**
	 * 查询教师是否已排课
	 * @param teacherCode
	 * @return
	 */
	int getPkCountByTeacherCode(String teacherCode) throws RuntimeException;

    Map selectScheduleOrNotAdd(Map projectSchedulePlan) throws RuntimeException;

	List<Map<String, Object>> selectScheduleOrNotEdit(Map<String, Object> projectSchedulePlan) throws RuntimeException;

	List<Map<String, String>> getHadCountKcTime(String project_id) throws RuntimeException;

	/**
	 * 查询临时排课计划
	 * @param param
	 * @return
	 */
    List<Map<String,String>> getTempPlan(Map<String,Object> param) throws RuntimeException;

	Map selectBlank(Map map) throws RuntimeException;

	Map selectBlank2(Map map) throws RuntimeException;

    Map getNowScheduleOrNot(Map param) throws RuntimeException;

	Map getTeacherSchedule(Map param) throws RuntimeException;

    int getProjectStartPersonCount(String project_id) throws RuntimeException;

    List<Map<String, Object>> selectSendMessagePerson(String project_id) throws RuntimeException;

	int insertPersonMessage(List messageList) throws RuntimeException;

	List<Map<String, Object>> selectManySendMessagePerson(Map iMap) throws RuntimeException;

	List<Map<String, Object>> selectClassManagePerson() throws RuntimeException;

	List<Map<String, Object>> selectManySendMessagePersonPro(Map iMap) throws RuntimeException;

	List<Map<String, Object>> selectManySendMessagePersonPro2(Map iMap) throws RuntimeException;

    Map selectCourseNameFromDataDic(String courseInput) throws RuntimeException;

	int createCourse(Map course) throws RuntimeException;

	List<Map<String, Object>> selectTeacher(Map map) throws RuntimeException;

	List<Map<String, String>> selectAllProject(Map tMap) throws RuntimeException;

	Map getYstartAndYend(String selectStartTime) throws RuntimeException;

	Integer listIsPlanByProject(Map<String,Object> param) throws RuntimeException;

    List<Map<String, Object>> validate(String projectId) throws RuntimeException;

	List<Map<String, Object>> getGradeClassA(Map map) throws RuntimeException;

	List<Map<String, Object>> getGradeClassB(Map map) throws RuntimeException;

    List<Map<String, Object>> getAllGradeInfo(Map map) throws RuntimeException;

    List<Map<String, Object>> getAllKcInfo() throws RuntimeException;

	List<Map<String, Object>> getAllBmInfo() throws RuntimeException;

	List<Map<String, Object>> getBmGradeInfo(Map resultMap) throws RuntimeException;

	int insertGradeInfo(List list) throws RuntimeException;

	List getGradePersonCount(List list) throws RuntimeException;

	List getGradePersonCountPjs(List list) throws RuntimeException;
}
