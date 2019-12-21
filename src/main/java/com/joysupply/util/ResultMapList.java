package com.joysupply.util;

import java.util.List;
import java.util.Map;

public class ResultMapList {
	
	private String message = "获取失败";
	private boolean state = false;
	private int count;
	private List<Map<String,Object>> data;
	
	public ResultMapList() {
		super();
	}
	public ResultMapList(String message) {
		super();
		this.message = message;
	}
	public ResultMapList( int count, List<Map<String, Object>> data) {
		super();
		this.message = "获取成功";
		this.state = true;
		this.count = count;
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResultMapList [message=" + message + ", state=" + state + ", count=" + count + ", data=" + data + "]";
	}
	
}
