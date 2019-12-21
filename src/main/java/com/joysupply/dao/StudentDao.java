package com.joysupply.dao;

import com.joysupply.entity.ClassScore;
import com.joysupply.entity.Project;
import com.joysupply.entity.Student;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;
import com.joysupply.util.ResultData;

import java.util.List;
import java.util.Map;

public interface StudentDao {
    //学生信息

    /**
     * 保存学生信息
     *
     * @param student
     * @return
     * @throws RuntimeException
     */
    int saveStudent(Student student) throws RuntimeException;

    /**
     * 批量导入学员信息
     *
     * @param list
     * @return
     * @throws RuntimeException
     */
    int saveAllStudent(List<Student> list) throws RuntimeException;

    /**
     * 修改学生信息
     *
     * @param student
     * @return
     * @throws RuntimeException
     */
    int updateStudent(Student student) throws RuntimeException;

    /**
     * @param map status studentCode
     * @return
     * @throws RuntimeException
     */
    int updateStudentStatus(Map<String, Object> map) throws RuntimeException;

    /**
     * 通过学生编码删除学生
     *
     * @param studentCode
     * @return
     * @throws RuntimeException
     */
    int deleteStudentByCode(String studentCode) throws RuntimeException;

    /**
     * 删除学生和项目的链接表
     * @param studentCode
     * @return
     * @throws RuntimeException
     */
    int deleteProjectStudentByCode(String studentCode) throws RuntimeException;

    /**
     * 条件查询数量
     *
     * @param map
     * @return
     * @throws RuntimeException
     */
    List<String> queryStudentCodes(Map<String, Object> map) throws RuntimeException;

    /**
     * 通过学生编码查询对应的学生信息
     *
     * @param studentCode
     * @return
     * @throws RuntimeException
     */
    Map<String, Object> selectStudentByStrCode(String studentCode) throws RuntimeException;

    /**
     * 通过学生学号去重
     *
     * @param serial
     * @return
     * @throws RuntimeException
     */
    Student selectStudentBySerial(String serial) throws RuntimeException;

    /**
     * 条件查询学生列表
     *
     * @param list
     * @return
     * @throws RuntimeException
     */
    List<Map<String, Object>> queryStudentByParam(List<String> list) throws RuntimeException;

    /**
     * 保存学生关联的项目
     *
     * @param list
     * @return
     * @throws RuntimeException
     */
    int saveProjectStudents(List<Map<String, Object>> list) throws RuntimeException;

    /**
     * 删除学生对应的项目数据
     *
     * @param studentCode
     * @return
     * @throws RuntimeException
     */
    int deleteProjectStudents(String studentCode) throws RuntimeException;

    /**
     * 通过学生的code查询对应的项目列表
     *
     * @param studentCode
     * @return
     * @throws RuntimeException
     */
    List<Map<String, Object>> queryProjectStudentsByStuCode(String studentCode) throws RuntimeException;

    /**
     * 通过项目的编码获取项目下的学生列表
     *
     * @param projectId
     * @return
     * @throws RuntimeException
     */
    List<Student> queryProjectStudentByProCode(String projectId) throws RuntimeException;

    /**
     * 添加成绩
     *
     * @param classScores
     * @return
     * @throws RuntimeException
     */
    int saveClassScore(List<ClassScore> classScores) throws RuntimeException;

    /**
     * 删除学生，项目对应的信息
     *
     * @param classScores
     * @return
     * @throws RuntimeException
     */
    int deleteClassScoreByProClaCode(ClassScore classScores) throws RuntimeException;

    /**
     * 通过项目和学生的编码删除成绩
     *
     * @param map
     * @return
     * @throws RuntimeException
     */
    int deleteClassScoreByName(Map<String, Object> map) throws RuntimeException;

    /**
     * projectId AND studentCode 查询是否有这个信息
     *
     * @param classScore
     * @return
     * @throws RuntimeException
     */
    int queryClasScoreByCNameProId(ClassScore classScore) throws RuntimeException;

    /**
     * 通过项目的编码查询项目下的课程
     *
     * @param projectId
     * @return
     * @throws RuntimeException
     */
    List<String> queryClassScoreCourseNameByProjectId(String projectId) throws RuntimeException;

    /**
     * 查询所有的课程名字当前查询下的所有列
     *
     * @return
     * @throws RuntimeException
     */
    String queryClassScoreName(Map<String, Object> map) throws RuntimeException;

    /**
     * 查询学生和项目对应的列表
     *
     * @param map
     * @return
     * @throws RuntimeException
     */
    List<Map<String, Object>> queryClassScoreSqlParam(Map<String, Object> map) throws RuntimeException;

    /**
     * 通过项目和学生的编码查询数据
     *
     * @param map
     * @return
     * @throws RuntimeException
     */
    List<Map<String, Object>> queryClassScoreByProCoscId(Map<String, Object> map) throws RuntimeException;

    /**
     * 通过项目的编码查询项目排课时的课程
     *
     * @param projectId
     * @return
     * @throws RuntimeException
     */
    List<String> queryProjectToScoreByProjectCode(String projectId) throws RuntimeException;

    /**
     * 查询所有的成绩
     * @return
     * @throws RuntimeException
     */
    List<Map<String,Object>> queryScoreAll() throws RuntimeException;

    /**
     * 添加五分制
     *
     * @param param
     * @return
     * @throws BusinessLevelException
     */
    int insertFivePoint(Map<String, Object> param) throws RuntimeException;

    /**
     * 修改五分制
     *
     * @param param
     * @return
     * @throws BusinessLevelException
     */
    int updateFivePoint(Map<String, Object> param) throws RuntimeException;

    /**
     * 通过五分制编码删除值
     *
     * @param fivePointCode
     * @return
     * @throws BusinessLevelException
     */
    int delFivePoint(String fivePointCode) throws RuntimeException;

    /**
     * 通过五分制的编码查询相应信息
     *
     * @param fivePointCode
     * @return
     */
    Map<String, Object> selectFivePintByCode(String fivePointCode) throws RuntimeException;

    /**
     * 查询五分制列表
     *
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    List<Map<String, Object>> selectFivePint(Map<String, Object> map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取学生列表
     * @Date 2018/12/14 17:28
     * @Param [map]
     * @Return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     **/
    List<Map<String, Object>> getStudentList(Map<String, Object> map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取权限项目下所有学生Code
     * @Date 2018/12/14 17:38
     * @Param [projectList]
     * @Return java.lang.Object
     **/
    List<String> getStudentListByProjectId(List<Project> projectList) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description
     * @Date 2018/12/18 15:16
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> getAllStudentList(Map<String, Object> map) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取学生身份证列表
     * @Date 2018/12/20 14:13
     * @Param []
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getStudentIdcardList()throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取学生电话列表
     * @Date 2018/12/22 19:19
     * @Param []
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getStudentPhoneList()throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取学生学号列表
     * @Date 2018/12/22 19:21
     * @Param []
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getStudentSerialList()throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 模板名称是否存在
     * @Date 2018/12/21 10:20
     * @Param [param]
     * @Return int
     **/
    int isPointNameExist(Map<String, Object> param);
    /**
     * @Author MaZhuli
     * @Description 查询模板分数列表
     * @Date 2018/12/21 14:04
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> queryScoreList(Map<String, Object> map);
    /**
     * @Author MaZhuli
     * @Description 判断模板是否在使用中
     * @Date 2018/12/21 14:58
     * @Param [temp]
     * @Return int
     **/
    int isTempInUse(Map<String, Object> temp) throws RuntimeException;

    /**
     * 查询所有五分制数据
     * @return
     * @throws RuntimeException
     */
    List<Map<String,Object>> queryAllFivePoint () throws RuntimeException;

    /**
     * 查询所有的项目课程
     * @return
     * @throws RuntimeException
     */
    List<Map<String,Object>> queryClassScoreCourseNameAll() throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 根据身份照号查询学生
     * @Date 2019/1/3 11:26
     * @Param [idcard]
     * @Return java.lang.String
     **/
    String getStudentCodeByIdcard(String idcard) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取学生项目列表
     * @Date 2019/1/3 11:29
     * @Param [studentCode]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getProjectListByStudentCode(String studentCode) throws RuntimeException;

    List<Map<String,Object>> queryClassScoreForPrint(Map<String,Object> map);
}
