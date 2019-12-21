package com.joysupply.entity.dto;

import java.util.List;

/**
 *教室不可用时间dto
 * @author Administrator
 *
 */
public class ClassRoomDisableTimeDto{
	private String roomId;
	private List<StartTimeAndEndTimeDto> startTimeAndEndTimeList;
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public List<StartTimeAndEndTimeDto> getStartTimeAndEndTimeList() {
		return startTimeAndEndTimeList;
	}
	public void setStartTimeAndEndTimeList(List<StartTimeAndEndTimeDto> startTimeAndEndTimeList) {
		this.startTimeAndEndTimeList = startTimeAndEndTimeList;
	}
	
	
}
