package com.joysupply.dao;

import com.joysupply.entity.Teacher;

import java.util.List;
import java.util.Map;

public interface AuthorityManageDao {

    /**
     * 查询用户对应项目权限
     *
     * @param personCode
     * @return
     */
    List<Map<String, Object>> getPersonProjectAuth(String personCode) throws RuntimeException;

	List<Map<String, Object>> getClassManageProject()throws RuntimeException;

	/**
	 * 查询用户教学楼权限
	 * @param personCode
	 * @return
	 */
	List<Map<String, Object>> getPersonBuildingAuth(String personCode)throws RuntimeException;

    /**
     * 删除权限
     *
     * @param map
     */
    void deleteData(Map<String, Object> map) throws RuntimeException;

    /**
     * 插入权限
     *
     * @param map
     */
    void insertData(Map<String, Object> map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取人员教师权限
     * @Date 2018/11/28 10:10
     * @Param [personCode]
     * @Return java.util.List<java.util.Map       <       java.lang.String       ,       java.lang.Object>>
     **/
    List<Map<String, Object>> getPersonTeacherAuth(String personCode) throws RuntimeException;

    List<Map<String, Object>> getPersonTeacherAuthA(String personCode) throws RuntimeException;

    List<Map<String, Object>> getTeachersAuth(String personCode) throws RuntimeException;

    List<Map<String, Object>> getTeachersAuthA(String organizeCode) throws RuntimeException;

    Map<String, Object> getOrganizeCode(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除人员项目权限
     * @Date 2018/12/19 15:08
     * @Param [personCode]
     * @Return int
     **/
    int delPersonprojectAuth(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除人员教师权限
     * @Date 2019/1/25 18:25
     * @Param [personCode]
     * @Return void
     **/
    void delPersonTeacherAuth(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取所有财务人员项目列表
     * @Date 2018/12/22 13:00
     * @Param [personList]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getFeePersonProjectAuth(List<String> personList);

    List<Teacher> listTeacherAuthPageData(Teacher teacher);

    /**
     * @Author MaZhuli
     * @Description 获取拥有教师权限人员
     * @Date 2019/1/25 17:52
     * @Param [personCode]
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> getTeachersAuthPersons(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 批量插入数据权限
     * @Date 2019/1/25 18:47
     * @Param [sourceList]
     * @Return void
     **/
    void insertAuthDataInBatch(List<Map<String, Object>> sourceList);
    /**
     * @Author MaZhuli
     * @Description 删除人员所有权限
     * @Date 2019/2/21 10:33
     * @Param [personCode]
     * @Return void
     **/
    void delPersonAuth(String personCode)throws RuntimeException;
}
