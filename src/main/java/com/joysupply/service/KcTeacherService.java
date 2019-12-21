package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.ResultData;

/**
 * 已发教师课酬接口层
 * @author Administrator
 *
 */
public interface KcTeacherService {
	
	//已发教师
	List getkcTeacher(Map<String, Object> params);

	//获取项目名称
	List<Map<String, Object>> getProductName(String personCode);
	
	//教师类别课酬
	List<Map<String, Object>> getKclbTeacher(String teacherType, String year, String month);
	
	//部门维度课酬
	List<Map<String, Object>> getVdoingInfo(Map<String,Object> map);

	//部门明细
	List getDepartDetail(Map<String, Object> params);

	//获取老师已发课酬表头
	ResultData<List<String>> getYearMonth() throws BusinessLevelException;
	
	//获取老师已发课酬 
	List<ResultData> selRemTeacher(Map<String,Object> map) throws BusinessLevelException;
	
	//获取教师已发课酬 没有分页
	Map<String,Object> selRemTeacherNoPage(Map<String,Object> map) throws BusinessLevelException;

	//获取部门维度课酬总条数
	long getVdoingCount(Map<String, Object> param);
	
}
