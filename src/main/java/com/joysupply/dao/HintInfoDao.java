package com.joysupply.dao;

import java.util.Map;

public interface HintInfoDao {

	/**
	 * 添加当前用户的习惯记录
	 * @param hintType
	 * @param personCode
	 * @return
	 */
	void saveHintInfo(Map param) throws RuntimeException;
	
	
	/**
	 * 查询当前用户是否有保存记录
	 * @param personCode
	 * @return
	 */
	int getHintInfoByPerson(Map param);
}
