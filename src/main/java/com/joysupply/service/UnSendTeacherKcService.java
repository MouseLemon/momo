package com.joysupply.service;

import java.util.List;
import java.util.Map;

public interface UnSendTeacherKcService {
	/**
	 * 获取项目名称
	 * */
	List<Map<String, Object>> getProjectName(String personCode);

	/**
	 * 获取项目名称(SZX添加)
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getProjectName(Map<String,Object> map);
	/**
	 * 查询未发教师课酬
	 * */
	List getUnsendData(Map<String, Object> params);

}
