package com.joysupply.service;

import java.util.List;
import java.util.Map;

public interface KcManagerTeacherService {

	/**
	 * 教师课酬数据
	 * @param requset
	 * @return
	 */
	List<Map<String, Object>> getKCData(Map<String, String> requset);

	/**
	 * 教师项目名称
	 * @param personCode
	 * @return
	 */
	List<Map<String, Object>> getProductName(String personCode);

	/**
	 * 获取教师详细信息
	 * @param personCode
	 * @return
	 */
	Map<String, Object> getTeacherInfo(String personCode);

	/**
	 * 获取课酬明细
	 * @param request
	 * @return
	 */
	List<Map<String, Object>> getKCInfo(Map<String, Object> request);

	/**
	 * 获取本周课酬详情
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getThisWeekKc(Map<String, Object> param);

	/**
	 * 获取本日课酬
	 * @param param
	 * @return
	 */
	String getTodayKc(Map<String, Object> param);

	
}
