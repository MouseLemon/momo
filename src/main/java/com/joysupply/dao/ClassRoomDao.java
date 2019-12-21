package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.ClassRoom;


/**
 * 教室数据层
 * @author Administrator
 *
 */
public interface ClassRoomDao {
	/**
	 * 查询教室信息列表
	 * @param map
	 * @return
	 */
	List<ClassRoom> getClassRooms(ClassRoom classRoom);
	/**
	 * 新建教室
	 * @param map
	 * @return
	 */
	int saveNewClassRoom(Map<String, Object> map);
	/**
	 * 编辑教室
	 * @param map
	 */
	int updateClassRoom(Map<String, Object> map);
	int updateProhibitionStatus(Map<String, Object> map);
	int deleteClassRoom(Map<String, Object> map);
	/**
	 * 根据人数、教室类型和可用时间，筛选出符合的教室
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getMactchingRooms(Map<String, Object> paramMap);
	List<Map<String, Object>> getClassRoomSpareTime(Map<String, Object> map);
	
	/**
	 * 根据id查询教室
	 * @param roomId
	 * @return
	 */
	ClassRoom getClassRoomByRoomId(String roomId);
	
	/**
	 * 查询空闲教室
	 * @param params
	 * @return
	 */
	List<ClassRoom> getIdleRoom(Map params);
	
	/**
	 * 查看教室已排课详情
	 * @param params
	 * @return
	 */
	Map<String, Object> getRoomPlan(Map<String, Object> params);
	
	/**
	 * 查看教室
	 * @param roomId
	 * @return
	 */
	Map<String,Object> getRoom(String roomId);
	
	/**
	 * 查看教室不可用和已排课的时间段
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getUnTimeData(Map<String, Object> param);
	List<Map<String, Object>> getUnTimeDataPsp(Map<String, Object> param);

	/**
	 * 查询时间段内的不可用教室情况
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getIdleRoomListData(Map<String, Object> param);
	
	/**
	 * 查询是否重复
	 * @param map
	 * @return
	 */
	int getRoomByLocAndNum(Map<String, Object> map);

    List<Map<String, Object>> getProjectInfo(Map<String, Object> param);

	int getOnWayProjectCount(Map doMap);

	int getFinishedProjectCount(Map doMap);

	/**
	 * 根据教师权限及教室类型查询楼群
	 * @param reqmap
	 * @return
	 */
	List<Map<String, String>> getRoomLocByAuth(Map<String, Object> reqmap);

	/**
	 * 获取当前月对应的季节
	 * @param month
	 * @return
	 */
    //String getNowSeason(String month);

	/**
	 * 根据月份获取可用季节
	 * @param months
	 * @return
	 */
//	String getSeasonByMonths(List<String> months);

	/**
	 * 保存临时排课
	 * @param map
	 */
	void saveTempPlan(Map<String,Object> map);

	/**
	 * 查询当前楼群已排教室
	 * @param params
	 * @return
	 */
	List<Map<String,String>> getIsPlanRoomId(Map params);

	List<String> getIsPlanTeacherCode(Map<String,Object> param);

	/**
	 * 查询excel重复数据
	 * @param list
	 * @return
	 */
    List<ClassRoom> existList(List<Map<String,Object>> list);

	/**
	 * 批量导入数据
	 * @param list
	 */
	void importClassRoom(List<Map<String,Object>> list);

	Map<String,Object> getTempPlan(String id);

	void updateTempPlan(Map<String,Object> map);
}
