package com.joysupply.util;

public class PageInfo {

	private int start; //开始数据
	private int pageSize;//每页的个数
	private int page;//查询页数
	public PageInfo(){}
	public PageInfo(int page, int  pageSize) {
		super();
		if(page==0) {
			page = 1;
		}
		if(pageSize==0) {
			pageSize=10;
		}
		this.pageSize = pageSize;
		this.page = page;
	}
	public int getStart() {
		start = ((page-1)<0?0:(page-1))*pageSize;
		return start;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
