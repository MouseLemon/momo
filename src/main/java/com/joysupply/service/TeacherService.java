package com.joysupply.service;

import com.github.pagehelper.PageInfo;
import com.joysupply.entity.ProjectSchedulePlan;
import com.joysupply.entity.SystemUser;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

public interface TeacherService {
	
	/**
	 * 添加教师信息
	 * @param teacher
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult addTeacher(Teacher teacher) throws BusinessLevelException;
	
	
	/**
	 * 修改教师信息
	 * @param teacher
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult updateTeacher(Teacher teacher) throws BusinessLevelException;
	
	/**
	 * 删除教师信息
	 * @param teacherCode
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult deleteTeacher(String teacherCode) throws BusinessLevelException;

	/**
	 * 查询教师列表
	 * @param teacher
	 * @param limit 
	 * @param page 
	 * @return
	 * @throws BusinessLevelException
	 */
	PageInfo<Teacher> listData(Teacher teacher, int page, int limit) throws BusinessLevelException;

	/**
	 * 根据code查询教师详细信息
	 * @param teacherCode
	 * @return
	 */
	Teacher getTeacherByCode(String teacherCode) throws BusinessLevelException;

	/**
	 * 无分页查询教师列表
	 * @return
	 * @throws BusinessLevelException
	 */
	List<Teacher> getTeacherListNoPage() throws BusinessLevelException;

	/**
	 * 根据教师查询所有课程
	 * @param teacher_name
	 * @return
	 * @throws BusinessLevelException
	 */
	Map<String, Object> getCourseByTeacherName(String teacher_name) throws BusinessLevelException;

	/**
	 * 根据教师名称查询课程
	 * @param param
	 * @return
	 * @throws BusinessLevelException
	 */
	List<ProjectSchedulePlan> getSavedCourseByTeacherName(Map param) throws BusinessLevelException;

	
	/**
	 * 获取教室个人信息。个人中心展示
	 * @param personCode
	 * @return
	 */
	Teacher getTeacherInfoByCode(String personCode);

	/**
	 * 查询教师首页的今日课程与课时
	 * @return
	 */
	Map<String, Object> getTodayClass(String teacherCode);
	
	/**
	 * 根据日期获取今日课时
	 * @param dateTime
	 * @return
	 */
	int getTodayClassCount(String teacherCode, String dateTime);

	List<Teacher> getCurrentTeacher(String person_code);

	List<Teacher> getTeacherListByProjectId(String project_id);

	/**
	 * 批导入教师信息
	 * @param list
	 * @throws BusinessLevelException
	 */
	void importTeacher(List<Map<String,Object>> list) throws RuntimeException;

	/**
	 * 批创建教师用户
	 * @param list
	 * @throws BusinessLevelException
	 */
	void importUserByTeacher(List<Map<String,Object>> list) throws RuntimeException;

	/**
	 * 批添加。查询教师是否存在
	 * @param list
	 * @return
	 */
	List<Teacher> existTeacher(List<Map<String,Object>> list);

	/**
	 * 批添加，查询用户是否存在
	 * @param list
	 * @return
	 */
	List<SystemUser> existUser(List<Map<String,Object>> list);

	/**
	 * 查询个性签名
	 * @param personCode
	 * @return
	 */
    Map<String,String> getSignaTrue(String personCode);

	/**
	 * 修改签名
	 * @param personCode
	 */
	void updateSigna(String personCode,String signature) throws BusinessLevelException;

	void addSigna(String personCode, String signature) throws BusinessLevelException;

    List<Map<String,Object>> listDataNoPage(Teacher teacher);

	List<Map<String, Object>> getTeachers(String person_code);
}
