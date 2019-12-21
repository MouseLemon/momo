package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.RoomUnuseTimeFrame;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

public interface RoomUnuseTimeFrameService {
	/**
	 * 根据roomId查询不可用时间段列表
	 * @param map 
	 * @return
	 */
	List<RoomUnuseTimeFrame> getRoomUnuseTimeFrameListByRoomID(Map<String, Object> map) throws BusinessLevelException;
	/**
	 * 修改教室时间段可用或不可用
	 * @param map
	 * @return
	 */
	OpResult updateRoomUnuseTimeFrame(Map<String, Object> map) throws BusinessLevelException;

}
