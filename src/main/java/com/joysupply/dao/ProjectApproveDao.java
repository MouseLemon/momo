package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface ProjectApproveDao {

	List<Map<String, Object>> getKcList(Map<String, Object> params);
	/**
	 * 更新课酬表
	 * */
	void projectApproveKc(Map<String,Object> params);

	/**
	 * 更行教师工作量扣除
	 * @param params
	 */
	void feeDeductKc(Map<String,Object> params);
	/**
	 * 新增审批表
	 * */
	void insertApprove(Map<String, Object> params);
    /**
     * 新增审批表
     * */
    void insertApproves(List<Map<String, Object>> params);
	/**
	 * 新增审批退回日志表
	 * */
	void insertApproveDoc(Map<String, Object> params);
	/**
	 * 新增审批退回日志表多条记录
	 * */
	void insertApproveDocs(List<Map<String, Object>> params);
	
	List<Map<String, Object>> kcReadyList(Map<String, Object> params);
	/**
	 * 系统管理员保存年月信息
	 * */
	void managerSave(Map<String, Object> params);
	/**
	 * 验证是否是最后一位审核人
	 * @param params 
	 * */
	int queryListCheckPerson(Map<String, Object> params);
	/**
	 * 获取系统管理员审批列表
	 * */
	List<Map<String, Object>> getSystemKcList(Map<String, Object> params);
	/**
	 * 更新项目审核次序
	 * */
	void projectApproveOrder(Map<String, Object> params);
	
	void updateApproveStatus(Map<String, Object> params);
	/**
	 * 系统管理员审核数据
	 * */
	List<Map<String, Object>> kcSystemList(Map<String, Object> params);

	/**
	 * WangYuelei 查询发放课酬的数据
	 * @param parms
	 * @return
	 * @throws RuntimeException
	 */
	List<Map<String, Object>> getGrantKC(Map<String,Object> parms) throws RuntimeException;
	/**
	 * 财务审批
	 * */
	void cwApproveKc(Map<String, Object> params);
	/**
	 * 财务审批退回
	 * */
	void cwApproveKcRetu(Map<String, Object> params);
	/**
	 * 获取项目部名称
	 * @param projectId
	 * @return
	 */
	String getDepNameByProjectId(String projectId);

	/**
	 * 通过老师的code查询出所有关于 项目-流水-老师 集合
	 * @param teacherCode
	 * @return
	 */
	List<Map<String,Object>> teaProNumByTeaCode(String teacherCode);

	/**
	 * 通过项目的的id获取每个项目已经发的课酬和课时
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> getProjectListKCKS(List<String> param);

	/**
	 * 修改工作量扣除状态
	 * @param param
	 */
	void cwFeeDeductKc(Map<String,Object> param);
	/**
	 * 修改工作量扣除状态 退回
	 * @param param
	 */
	void cwFeeDeductKcRetu(Map<String,Object> param);
	/**
	 * 通过合并流水号和状态查询该流水号是否有数据
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> objByMergeSerialNumber (Map<String,Object> param );
	/**
	 * 查询下一位审核人Code
	 * @param params
	 * @return
	 */
	String getNextPersonCode(Map<String, Object> params);

	/**
	 * 查询项目的已发课酬
	 * @param projectId
	 * @return
	 * @throws RuntimeException
	 */
	Map<String,Object> getProjectKC (String projectId) throws RuntimeException;

	/**
	 * 查询工作量扣除
	 * @param projectId
	 * @return
	 * @throws RuntimeException
	 */
	Map<String,Object> getProjectFC (String projectId) throws RuntimeException;
	/**
	 * 系统管理员已审批列表
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> systemKcReadyList(Map<String, Object> params);
}
