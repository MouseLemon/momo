package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.SystemUser;
import com.joysupply.entity.Teacher;

/**
 * 教师数据层
 * @author Administrator
 *
 */
public interface TeacherDao {
	
	/**
	 * 保存教师信息
	 * @param teacher
	 * @return
	 * @throws RuntimeException
	 */
	int addTeacher(Teacher teacher) throws RuntimeException; 
	
	/**
	 * 修改教师信息
	 * @param teacher
	 * @return
	 * @throws RuntimeException
	 */
	int updateTeacher(Teacher teacher) throws RuntimeException;
	
	/**
	 * 删除教师
	 * @param teacherCode
	 * @return
	 * @throws RuntimeException
	 */
	int deleteTeacher(String teacherCode) throws RuntimeException;
	
	/**
	 * 查询教师列表
	 * @param teacher
	 * @return
	 * @throws RuntimeException
	 */
	List<Teacher> listData(Teacher teacher) throws RuntimeException;
	
	Teacher getTeacherByCode(String teacherCode) throws RuntimeException;

	String existTeacher(String idCard) throws RuntimeException;

	/**
	 * 无分页查询教师列表
	 * @return
	 * @throws RuntimeException
	 */
	List<Teacher> getTeacherListNoPage() throws RuntimeException;

	/**
	 * 根据教师姓名查询教师编码
	 * @return
	 * @throws RuntimeException
	 */
	Map<String, Object> getTeacherInCode(String teacher_name) throws RuntimeException;

	/**
	 * 根据教师查询所有课程
	 * @param teacher_name
	 * @return
	 * @throws RuntimeException
	 */
	List<Map<String, Object>> getCourseByTeacherName(String teacher_name) throws RuntimeException;

	/**
	 * 根据教师名称查询课程
	 * @param param
	 * @return
	 */
	List getSavedCourseByTeacherName(Map param) throws RuntimeException;
	
	/**
	 * 获取教室详细信息
	 * @param personCode
	 * @return
	 */
	Teacher getTeacherInfoByCode(String personCode) throws RuntimeException;

	/**
	 * 查询教师首页今日课程及课时数
	 */
	List<Map<String, Object>> getTodayClass(Map<String,Object> param) throws RuntimeException;

	int getTodayClassCount(Map<String, Object> param) throws RuntimeException;

	List<Teacher> getCurrentTeacher(String person_code) throws RuntimeException;

    List<Teacher> getTeacherListByProjectId(String project_id);

	/**
	 * 批导入教师
	 * @param list
	 */
	void importTeacher(List<Map<String,Object>> list);

	/**
	 * 批导入教师用户
	 * @param list
	 */
	void importUserByTeacher(List<Map<String,Object>> list);

	/**
	 * 批导入教师，查询教师是否存在
	 * @param list
	 * @return
	 */
	List<Teacher> existTeachers(List<Map<String,Object>> list);

	/**
	 * 批创建教师用户，查询用户是否存在
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

	void updateSigna(Map<String,String> param) throws RuntimeException;

	void addSigna(Map<String,String> param) throws RuntimeException;

    List<Map<String,Object>> listDataNoPage(Teacher teacher);

    void deleteUser(String teacherCode);

	List<Map<String, Object>> getTeachers(String person_code);
}
