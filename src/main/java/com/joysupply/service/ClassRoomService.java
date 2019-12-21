package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.ClassRoom;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

/**
 * 教室管理
 * @author zxt
 *
 * 2018年11月3日-下午2:13:53
 */
public interface ClassRoomService {
	/**
	 * 查询教室信息列表
	 * @param map
	 * @return
	 */
	Map<String, Object> getClassRooms(ClassRoom classRoom,Integer page, Integer limit);
	/**
	 * 查询所有教室类型列表
	 * @param roleMenu
	 * @return
	 */
	//List<Menu> getClassRoomTypes();
	/**
	 * 新建教室
	 * @param map
	 * @return
	 */
	OpResult saveNewClassRoom(Map<String, Object> map) throws BusinessLevelException;
	/**
	 * 批量停用或启用
	 * @param map
	 * @return
	 */
	OpResult updateProhibitionStatus(Map<String, Object> map) throws BusinessLevelException;
	/**
	 * 删除
	 * @param map
	 * @return
	 */
	OpResult deleteClassRoom(Map<String, Object> map) throws BusinessLevelException;
	/**
	 * 选出符合条件的教室
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getMactchingRooms(Map<String, Object> map);
	/**
	 * 点击保存教室
	 * @param map
	 * @return
	 */
	OpResult saveUsableRoom(Map<String, Object> map) throws BusinessLevelException;
	/**
	 * 查看教室空闲时间
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getClassRoomSpareTime(Map<String, Object> map);
	/**
	 * 查看该教室已排课情况
	 * @param map
	 * @return
	 */
	List<String> getClassRoomAlreadyClasses(Map<String, Object> map);
	/**
	 * 退回排课
	 * @param map
	 * @return
	 */
	OpResult updateReturnToClass(Map<String, Object> map) throws BusinessLevelException;
	
	/**
	 * 根据id查询教室
	 * @param roomId
	 * @return
	 */
	ClassRoom getClassRoomByRoomId(String roomId);
	
	/**
	 * 设置教室不可用时间段
	 * @param option
	 * @param year
	 * @param week
	 * @param roomId
	 * @return
	 * @throws BusinessLevelException
	 */
	OpResult saveUnUseTime(String option, String year, String week,
			String roomId)throws BusinessLevelException;
	
	/**
	 * 删除当前周的记录
	 * @param year
	 * @param week
	 * @param roomId
	 * @throws BusinessLevelException
	 */
	void deleUnUseTime(String year, String week,String roomId) throws BusinessLevelException;
	
	/**
	 * 复制当前周的记录
	 * @param option
	 * @param year
	 * @param week
	 * @param roomId
	 * @param data
	 * @return
	 */
	OpResult copyUnUseTime(String option, String year, String week,
			String roomId, Map data) throws BusinessLevelException;
	
	/**
	 * 查询空闲时间
	 * @param roomLoc
	 * @param class_room_type
	 * @param class_date
	 * @param person_count
	 * @param starTime
	 * @param endTime
	 * @param year 
	 * @return
	 */
	Map<String, Object> getIdleRoom(String roomLoc, String class_room_type,
			String class_date, String person_count, String starTime,
			String endTime, String year,String scheduleId,String roomId);
	
	/**
	 * 查询教室已排课详情
	 * @param roomId
	 * @param year 
	 * @param endTime 
	 * @param starTime 
	 * @param class_date 
	 * @return
	 */
	Map<String, Object> getRoomPlan(String roomId, String class_date, String starTime, String endTime, String year);
	
	/**
	 * 查询教室不可用时间段
	 * @param roomList
	 * @param classDate 
	 * @return
	 */
	List<Map<String, Object>> getUnTimeData(List<ClassRoom> roomList, String classDate);
	List<Map<String, Object>> getUnTimeDataPsp(List<ClassRoom> roomList, String classDate);

	/**
	 * 空闲教室查询
	 * @param startTime
	 * @param endTime
	 * @param time
	 * @param end
	 * @param roomLoc
	 * @param roomNum
	 * @param weekDay
	 * @param limit
	 * @param page
	 * @param buildingAuth
	 * @return
	 */
	Map<String, Object> idleRoomListData(String startTime, String endTime,
										 String time, String end, String roomLoc, String roomNum,
										 String weekDay, int limit, int page, List<Map<String, Object>> buildingAuth);

    List<Map<String, Object>> getProjectInfo(String personCode);
    
    /**
     * 根据教师权限和教室类型查询楼群
     * @param reqmap
     * @return
     */
	List<Map<String, String>> getRoomLocByAuth(Map<String, Object> reqmap);

	/**
	 * 获取当前月对应的季节
	 * @param month
	 * @return
	 */
//	String getNowSeason(String month);

	/**
	 * 根据月份获取可用季节
	 * @param months
	 * @return
	 */
//	String getSeasonByMonths(List<String> months);

	/**
	 * 保存临时排课
	 * @param map
	 * @return
	 */
	Map<String,Object> saveTempPlan(Map<String,Object> map) throws BusinessLevelException;

	/**
	 * 判断当前时间是否可用
	 * @param classdate
	 * @param startTime
	 * @return
	 */
    Integer getNowTimeIsAble(String classdate, String startTime);

	/**
	 * 查询已排教师
	 * @param param
	 * @return
	 */
	List<String> getIsPlanTeacherCode(Map<String,Object> param);

	/**
	 * 查询excel中是否存在重复数据
	 * @param list
	 * @return
	 */
    List<ClassRoom> existList(List<Map<String,Object>> list);

	/**
	 * 导入数据
	 * @param list
	 */
	void importClassRoom(List<Map<String,Object>> list);

	int getOnWayProjectCount(Map doMap);

	int getFinishedProjectCount(Map doMap);

	Map<String,Object> getTempPlan(String id);
}

