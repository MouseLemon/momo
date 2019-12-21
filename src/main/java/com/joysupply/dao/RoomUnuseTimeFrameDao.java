package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.RoomUnuseTimeFrame;

/**
 * 教室不可用时段数据层
 * @author Administrator
 *
 */
public interface RoomUnuseTimeFrameDao {
	/**
	 * 根据roomId查询不可用时间段列表
	 * @param map
	 * @return
	 */
	List<RoomUnuseTimeFrame> getRoomUnuseTimeFrameListByRoomID(Map<String, Object> map);
	/**
	 * 删除记录
	 * @param map
	 * @return
	 */
	int deleteRoomUnuseTimeFram(Map<String, Object> map);
	/**
	 * 插入记录
	 * @param map
	 * @return
	 */
	int saveRoomUnuseTimeFram(Map<String, Object> map);
	/**
	 * 修改记录
	 * @param map
	 * @return
	 */
	int updateRoomUnuseTimeFram(Map<String, Object> map);
	
	/**
	 * 删除不可用时间段
	 * @param params
	 */
	void deleteRoomUnuseTime(Map params);
	
	/**
	 * 添加不可用时间段
	 * @param frames
	 */
	void saveUnUseTime(List<RoomUnuseTimeFrame> frames) throws RuntimeException;
	
	/**
	 * 查询是否已经设置不可用时间
	 * @param
	 * @return
	 * @throws RuntimeException
	 */
	List<String> selIsSet(Map<String,Object> map) throws RuntimeException;
	void deleteIsSet(List<RoomUnuseTimeFrame> others)throws RuntimeException;

}
