package com.joysupply.service;

import com.joysupply.util.OpResult;

public interface HintInfoService {
	
	/**
	 * 添加当前用户的习惯记录
	 * @param hintType
	 * @param personCode
	 * @return
	 */
	OpResult saveHintInfo(String hintType, String personCode) throws RuntimeException;
	
	
	/**
	 * 查询当前用户是否有保存记录
	 * @param personCode
	 * @return
	 */
	OpResult getHintInfoByPerson(String hintType, String personCode);


}
