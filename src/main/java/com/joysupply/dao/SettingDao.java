package com.joysupply.dao;

import com.joysupply.entity.TimeSheet;

import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * @author Administrator
 *
 */
public interface SettingDao {

	/**
	 * 查询时间表信息数量
	 * @param map
	 * @return
	 */
	int getTimeTableCount(Map<String, Object> map) throws RuntimeException;

	/**
	 * 查询时间表信息列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getTimeTableInfo(Map<String, Object> map) throws RuntimeException;

	/**
	 * 编辑时间表，《保存》按钮
	 * @param map
	 * @return
	 */
	int saveTimeTable(Map<String, Object> map) throws RuntimeException;

	/**
	 * 编辑单个时间段
	 * @param param
	 * @return
	 * @throws RuntimeException
	 */
	int editTime(Map param) throws RuntimeException;

	/**
	 * 添加课表时间
	 * @param listMap
	 * @return
	 * @throws RuntimeException
	 */
	int addTime(List listMap) throws RuntimeException;

	/**
	 * 删除时间段
	 * @param timeSheet
	 * @return
	 * @throws RuntimeException
	 */
	int deleteTime(TimeSheet timeSheet) throws RuntimeException;

	/**
	 * 获取所有时间
	 * @return
	 * @throws RuntimeException
	 */
	List<TimeSheet> getTimeSheet(String year) throws RuntimeException;
	/**
	 * 查询课程表表头
	 * @return
	 * @throws RuntimeException
	 */
	List<Map<String,Object>> queryTimeByTableAll() throws RuntimeException;

	/**
	 *  查询总课表数据
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	List<Map<String,Object>> queryTableData(Map<String,Object> map) throws RuntimeException;

	List<Map<String,String>> getTimeSheetList(Map<String, Object> map) throws RuntimeException;

	Map getTimes(Map timeResult) throws RuntimeException;

	Map selectStart(String start_time) throws RuntimeException;

	Map selectEnd(String end_time) throws RuntimeException;

	Map selectStartNext(String start_time) throws RuntimeException;

	Map selectBeginStartTime(String beginStartTime) throws RuntimeException;

	Map selectBeginEndTime(String beginEndTime) throws RuntimeException;
}
