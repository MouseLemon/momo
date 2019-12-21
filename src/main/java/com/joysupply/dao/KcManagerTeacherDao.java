package com.joysupply.dao;

import java.util.List;
import java.util.Map;

/**
 * 教师课酬统计管理Dao
 * @author Administrator
 *
 */
public interface KcManagerTeacherDao {

	/**
	 * 教师课酬数据
	 * @param request
	 * @return
	 */
	List<Map<String, Object>> getKCData(Map<String, String> request);

	/**
	 * 教师参与项目名称(动态列使用)
	 * @param personCode
	 * @return
	 */
	List<Map<String, Object>> getProductName(String personCode);

	/**
	 * 获取教师个人详细信息
	 * @param personCode
	 * @return
	 */
	Map<String, Object> getTeacherInfo(String personCode);

	/**
	 * 获取教师课酬明细
	 * @param request
	 * @return
	 */
	List<Map<String, Object>> getKCInfo(Map<String, Object> request);

	/**
	 * 获取本周教师课酬明细
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
