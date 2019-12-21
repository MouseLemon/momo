package com.joysupply.service;

import java.util.Map;

import com.joysupply.util.OpResult;

public interface ProjectApproveService {

	/**
	 * 获取未审批课酬数据
	 * */
	Map<String, Object> getKcList(Map<String, Object> params);

	/**
	 * 项目部审批
	 * */
	OpResult kcPass(Map<String, Object> params);
	/**
	 * 获取已审批课酬数据
	 * */
	Map<String, Object> kcReadyList(Map<String, Object> params);
	/**
	 * 管理员审核保存年月
	 * */
	OpResult managerSave(Map<String, Object> params);
	/**
	 * 系统管理员审核数据
	 * */
	Map<String, Object> kcSystemList(Map<String, Object> params);
	/**
	 * 财务审批
	 * */
	OpResult cwkcApproval(Map<String, Object> params);
	/**
	 * 系统管理员
	 */
	Map<String, Object> systemKcReadyList(Map<String, Object> params);

	/**
	 * 发送消息
	 * @param personCodes
	 */
	void sendMsg(Map<String, Object> params);

}
