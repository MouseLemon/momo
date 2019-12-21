package com.joysupply.service;

import com.joysupply.entity.TimeSheet;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;
import com.joysupply.util.ResultData;

import java.util.List;
import java.util.Map;

/**
 * @Author Dreamer
 * @Description 时间设置Service
 * @Date 上午 10:49 2018/12/15 0015
 * @Param
 * @return
 **/
public interface SettingService {

	/**
	 * 查询时间表
	 * @param map
	 * @return
	 */
	Map<String, Object> timeTable(Map<String, Object> map) throws BusinessLevelException;

	/**
	 * 修改整体时间表
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult saveTimeTable(Map<String, Object> map) throws BusinessLevelException;

	/**
	 * 编辑单个时间段
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult editTime(Map<String,Object> map) throws BusinessLevelException;

	/**
	 * 添加课表时间
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult addTime(Map<String,Object> map) throws BusinessLevelException;

	/**
	 * 删除时间
	 * @param timeSheet
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult deleteTime(TimeSheet timeSheet) throws BusinessLevelException;

	/**
	 * 获取所有时间
	 * @return
	 * @throws BusinessLevelException
	 */
	List<TimeSheet> getTimeSheet(String year) throws BusinessLevelException;

	/**
	 * 获取总课表的表头
	 * @return
	 * @throws BusinessLevelException
	 */
	Map<String,Object> queryTabelAllHead() throws BusinessLevelException;

	/**
	 * 查询总课表数据
	 * @param map
	 * @return
	 * @throws BusinessLevelException
	 */
	ResultData<List<Map<String,Object>>> queryTabelData(Map<String,Object> map) throws BusinessLevelException;

	/**
	 * 获取总课表表头
	 * @return
	 * @throws BusinessLevelException
	 */
	List<Map<String,Object>> queryTimeByTableAll() throws BusinessLevelException;

	/**
	 * 获取用户查询的总课表的数据
	 * @return
	 * @throws BusinessLevelException
	 */
	List<Map<String,Object>> queryAllTableBodyParam(Map<String,Object> map) throws BusinessLevelException;

	List<Map<String,String>> getTimeSheetList(Map<String, Object> map);

	Map getTimes(Map timeResult) throws BusinessLevelException;
}
