package com.joysupply.dao;

import java.util.List;
import java.util.Map;

public interface UnSendTeacherKcDao {
	
	/**
	 * 获取项目名称
	 * */
	List<Map<String, Object>> getProjectName();
	
	/**
	 * 未发已发教师课酬
	 */
	List<Map<String, Object>> kcTeacherList(Map<String, Object> params);
	
	List<Map<String,Object>> getDepList(List<Map<String,Object>> map);
	/**
	 * @Author SongZiXian
	 * @Description 获取项目名称
	 * @Date 2019/4/24 0024 下午 14:39
	 * @Param [map]
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	List<Map<String,Object>> getDepLists(Map<String,Object> map);
}
