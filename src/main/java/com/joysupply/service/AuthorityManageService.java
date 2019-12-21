package com.joysupply.service;

import com.github.pagehelper.PageInfo;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 权限管理业务层
 *
 * @author Administrator
 */
public interface AuthorityManageService {

    /**
     * 查询用户对应楼
     *
     * @param reqmap
     * @return
     */
    List<Map<String, Object>> getPersonBuildingAuth(Map<String, Object> reqmap) throws BusinessLevelException;

    /**
     * 查询用户对应项目
     *
     * @param reqmap
     * @return
     */
    List<Map<String, Object>> getPersonProjectAuth(Map<String, Object> reqmap) throws BusinessLevelException;

    /**
     * 删除用户对应项目权限
     *
     * @param string
     */
    OpResult deletePersonProjectAuth(String string) throws BusinessLevelException;

    /**
     * 删除教师对应项目权限
     *
     * @param string
     */
    OpResult deleteTeacherProjectAuth(String string) throws BusinessLevelException;

    /**
     * 删除用户和楼的权限
     *
     * @param building
     * @return
     * @throws BusinessLevelException
     */
    OpResult deletePersonBuildingAuth(String building) throws BusinessLevelException;

    /**
     * 插入老师项目权限
     *
     * @param map
     */
    OpResult insertTeacherProjectAuth(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 插入用户项目权限
     *
     * @param map
     */
    OpResult insertPersonProjectAuth(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 插入用户教学楼权限
     *
     * @param map
     * @return
     */
    OpResult insertPersonBuildingAuth(Map<String, Object> map) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 获取人员教师权限
     * @Date 2018/11/28 10:06
     * @Param [reqmap]
     * @Return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     **/
    List<Map<String, Object>> getPersonTeacherAuth(Map<String, Object> reqmap) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 添加人员教师权限
     * @Date 2018/11/28 10:08
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    OpResult insertPersonTeacherAuth(Map<String, Object> map) throws BusinessLevelException;

    /**
     * @return java.util.List<java.util.Map       <       java.lang.String       ,       java.lang.Object>>
     * @Author Dreamer
     * @Description 教师视图下拉选
     * @Date 下午 17:03 2018/12/18 0018
     * @Param [reqmap]
     **/
    List<Map<String, Object>> getTeachersAuth(Map<String, Object> reqmap) throws BusinessLevelException;

    PageInfo<Teacher> listTeacherAuthPageData(String personCode, int limit, int page, Teacher teacher, Integer type);

    List<Map> getTeachersAuthPersons(String personCode) throws BusinessLevelException;

    OpResult copyOath(Map map) throws BusinessLevelException;
}
