package com.joysupply.util;

public class ResultData<E> {
	private String message = "获取失败";
	private boolean state = false;
	private int count;
	private String msg;
	private int code;
	private E data;
	public ResultData() {
		super();
	}
	public ResultData(String message) {
		super();
		this.message = message;
	}
	public ResultData(E data) {
		this.message = "获取成功";
		this.state = true;
		this.count = 1;
		this.data = data;
	}
	public ResultData( int count, E data) {
		super();
		this.message = "获取成功";
		this.state = true;
		this.count = count;
		this.msg = "";
		this.code = 0;
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
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
	public E getData() {
		return data;
	}
	public void setData(E data) {
		this.data = data;
	}
	
}
