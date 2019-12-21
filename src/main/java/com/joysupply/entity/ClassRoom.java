package com.joysupply.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 教室实体类
 * @author zxt
 *
 * 2018年11月3日-下午2:14:49
 */
public class ClassRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roomId;
	private String roomNum;
	private String roomLoc;
	private String seating;
	private String roomType;
	private String useSeason;
	private Integer status;
	private String createTime;
	private String roomLocName;
	private String[] season;

	public void setSeason(String[] season) {
		this.season = season;
	}

	public static long getSerialVersionUID() {

		return serialVersionUID;
	}

	private List<Map<String,Object>> roomLocList;
	

	public String getRoomLocName() {
		return roomLocName;
	}

	public void setRoomLocName(String roomLocName) {
		this.roomLocName = roomLocName;
	}

	public String getRoomId() {
		return roomId;
	}

    public void setRoomLocList(List<Map<String,Object>> roomLocList) {
        this.roomLocList = roomLocList;
    }

    public List<Map<String,Object>> getRoomLocList() {

        return roomLocList;
    }

    public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getRoomLoc() {
		return roomLoc;
	}

	public void setRoomLoc(String roomLoc) {
		this.roomLoc = roomLoc;
	}

	public String getSeating() {
		return seating;
	}

	public void setSeating(String seating) {
		this.seating = seating;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getUseSeason() {
		return useSeason;
	}

	public void setUseSeason(String useSeason) {
		this.useSeason = useSeason;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
