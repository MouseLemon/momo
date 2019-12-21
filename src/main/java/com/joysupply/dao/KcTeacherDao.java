package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.joysupply.entity.Teacher;

/**
 * 已发教师课酬Dao层
 * @author Administrator
 *
 */
public interface KcTeacherDao {
	
	//已发教师课酬数据
	List<Map<String, Object>> getKcTeacher(Map<String, Object> params);
	
	//获取项目名称Dao
	List<Map<String, Object>> getProductName(String personCode);
	
	//教师类别课酬的Dao层
	List<Map<String, Object>> getKclbTeacher(@Param("teacherType") String teacherType,@Param("year") String year,@Param("month") String month);
	
	//部门维度课酬Dao层
	List<Map<String, Object>> getVdoingInfo(Map map) throws RuntimeException;
	
	//部门已付维度明细
	List<Map<String, Object>> getDepartDetail(Map<String, Object> params);
	
	//获取老师课酬表头
	List<String> getYearMonth() throws RuntimeException;
	
	//获取老师课酬数据
	List<Map<String, Object>> selRemTeacher(Map<String,Object> map) throws RuntimeException;

	//教师维度课酬总条数
	long getVdoingCount(Map<String, Object> param);

	//部门维度应付明细
    List<Map<String, Object>> getShouleDepartDetail(Map<String, Object> params);

    //部门维度已发课酬
	List<Map<String, Object>> getAlreadyKc(Map<String, Object> params);
}
