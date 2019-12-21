package com.joysupply.service;

import com.joysupply.entity.ClassScore;
import com.joysupply.entity.Project;
import com.joysupply.entity.Student;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;
import com.joysupply.util.ResultData;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 学生业务层
 *
 * @author Administrator
 */
public interface StudentService {
    /**
     * 添加学员
     *
     * @param student
     * @return
     * @throws BusinessLevelException
     */
    OpResult saveStudent(Student student) throws BusinessLevelException;

    /**
     * 修改学员信息
     *
     * @param student
     * @return
     * @throws BusinessLevelException
     */
    OpResult updateStudent(Student student) throws BusinessLevelException;

    /**
     * 修改学员状态
     *
     * @param map status studentCode
     * @return
     * @throws BusinessLevelException
     */
    OpResult updateStudentStatus(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 删除学员
     *
     * @param studentCode
     * @return
     * @throws BusinessLevelException
     */
    OpResult deleteStudentByCode(String studentCode) throws BusinessLevelException;

    /**
     * 通过学生的code查询对应的信息
     *
     * @param studentCode
     * @return
     * @throws BusinessLevelException
     */
    ResultData<Map<String, Object>> selectStudentByStrCode(String studentCode) throws BusinessLevelException;

    /**
     * 通过学生的编码去重
     *
     * @param serial
     * @return
     * @throws BusinessLevelException
     */
    boolean selectStudentBySerial(String serial) throws BusinessLevelException;

    /**
     * 学生列表分页
     *
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    Map queryStudentByParam(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 获取学生对应的项目 通过学生的code
     *
     * @param studentCode
     * @return
     * @throws BusinessLevelException
     */
    ResultData<List<Map<String, Object>>> queryProjectStudentsByStuCode(String studentCode) throws BusinessLevelException;

    /**
     * 获取学员的信息列表通过项目的编码
     *
     * @param projectId
     * @return
     * @throws BusinessLevelException
     */
    ResultData<List<Student>> queryProjectStudentByProCode(String projectId) throws BusinessLevelException;

    /**
     * 添加学生的成绩
     *
     * @param classScores
     * @return
     * @throws BusinessLevelException
     */
    OpResult saveClassScore(ClassScore classScores) throws BusinessLevelException;

    /**
     * 修改学生成绩信息
     *
     * @param classScore
     * @return
     * @throws BusinessLevelException
     */
    OpResult updateClassScore(ClassScore classScore) throws BusinessLevelException;

    /**
     * 删除学生项目对应的成绩
     *
     * @param classScores
     * @return
     * @throws BusinessLevelException
     */
    OpResult deleteClassScoreByProClaCode(ClassScore classScores) throws BusinessLevelException;

    /**
     * 查询改列表下的课程
     *
     * @return
     * @throws BusinessLevelException
     */
    List<String> queryClassScoreCourseName(Map<String, Object> map) throws BusinessLevelException;

    /**
     * 通过项目的编码查询项目下的课程
     *
     * @param projectId
     * @return
     * @throws BusinessLevelException
     */
    List<String> queryClassScoreCourseNameByProjectId(String projectId) throws BusinessLevelException;

    /**
     * 查找成绩列表分页
     *
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    ResultData<List<Map<String, Object>>> queryClassScore(Map<String, Object> map);

    /**
     * 通过学生和项目的查询相应的成绩记录
     *
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    ResultData<List<Map<String, Object>>> queryClassScoreByProCoscId(Map<String, Object> map);

    /**
     * 获取所有的课程名称以及对应的名字列表
     *
     * @return
     */
    ResultData<List<Map<String, Object>>> returnClassScoreName();

    /**
     * 添加修改五分制
     *
     * @param param
     * @return
     * @throws BusinessLevelException
     */
    OpResult addEditFivePoint(Map<String, Object> param) throws BusinessLevelException;

    /**
     * 通过五分制编码删除值
     *
     * @param fivePointCode
     * @return
     * @throws BusinessLevelException
     */
    OpResult delFivePoint(String fivePointCode) throws BusinessLevelException;

    /**
     * 通过五分制的编码查询相应信息
     *
     * @param fivePointCode
     * @return
     */
    ResultData<Map<String, Object>> queryFivePintByCode(String fivePointCode) throws BusinessLevelException;

    /**
     * 查询五分制列表
     *
     * @param map
     * @return
     * @throws BusinessLevelException
     */
    ResultData<List<Map<String, Object>>> queryFivePint(Map<String, Object> map) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 查询模板分数列表
     * @Date 2018/12/21 11:37
     * @Param [fivePointCode]
     * @Return com.joysupply.util.ResultData<java.util.List   <   java.util.Map   <   java.lang.String   ,   java.lang.Object>>>
     **/
    ResultData<List<Map<String, Object>>> queryScoreList(Map<String, Object> map) throws BusinessLevelException;

    /**
     *  WangYuelei
     * @param map
     * @return 没有分页的列表
     * @throws BusinessLevelException
     */
    ResultData<List<Map<String, Object>>> queryScoreListNoPage(Map<String, Object> map) throws BusinessLevelException;
    /**
     * 查询五分制列表
     *
     * @return
     * @throws BusinessLevelException
     */
    List<Map<String, Object>> queryFivePintALL() throws BusinessLevelException;

    /**
     * 批量导入
     *
     * @param list
     * @return
     * @throws BusinessLevelException
     */
    OpResult addAllStudent(List<Map<String, Object>> list, HttpSession session) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 获取所有成绩模板
     * @Date 2018/12/21 15:27
     * @Param []
     * @Return java.util.List<java.util.Map < java.lang.String , java.lang.Object>>
     **/
    List<Map<String, Object>> getTempListNoPage() throws BusinessLevelException;

    /**
     * 批量导入学成成绩
     * @return
     * @throws BusinessLevelException
     */
    OpResult addBatchStudentScore (List<Map<String,Object>> list,HttpSession session) throws BusinessLevelException;

    ResultData<List<Map<String,Object>>> queryClassScoreForPrint(Map<String,Object> map);
}
