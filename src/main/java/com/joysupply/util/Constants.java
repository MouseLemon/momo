package com.joysupply.util;

public class Constants {  
	public  final static String RUNTIME_ROOT = System.getProperty("user.dir");  
	/**
	 * get guid
	 * @return
	 */
	public static String GUID(){
		return  java.util.UUID.randomUUID().toString();
	}
	
	/**
	 * 非排课项目参数
	 * */
	public static String COURSE_FEE_TYPE = "19";  //COURSE_FEE_TYPE
}
