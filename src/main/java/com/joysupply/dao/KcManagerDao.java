package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.joysupply.util.OpResult;

/**
 * 课酬管理Dao
 * @author Administrator
 *
 */
public interface KcManagerDao {
	/**
	 * 查询课酬信息
	 * */
	List<Map<String, Object>> queryKcInfo(Map<String, Object> params);

	List<Map<String, Object>> getKCData();
	/**
	 * 保存课酬信息
	 * */
	int saveKcList(List<JSONObject> params);
	/**
	 * 查询上次课酬是否为工作量扣除
	 * */
	Map<String, Object> queryLastProject(Map<String, String> params);
	/**
	 * check时间段
	 * */
	List<Map<String, Object>> checkTimeSolt(Map<String,Object> params);
	/**
	 * 生成流水号
	 * */
	String queryExamineData(Map<String,Object> params);
	/**
	 * 保存
	 * */
	void saveExamineData(List<Map<String,String>> serialNumber);
	/**
	 * 获取流水号
	 * */
	String querySerialNum(Map<String, Object> params);
	/**
	 * 保存教师课酬修改数据
	 * */
	void saveDeList(List<JSONObject> deductListMap);

	/**
	 * 保存教师的工作量扣除
	 * @param deductList
	 */
	void saveFeeDeduct(List<JSONObject> deductList);
	/**
	 * 保存之前删除上次保存记录
	 * */
	void deleteLastSave(Map<String, Object> params);
	/**
	 * 修改审批表为提交
	 * */
	void updatefeeStatus(String serialNumber);
	/**
	 * 查询课讲明细
	 * */
	List<Map<String, Object>> queryKcDetatils(Map<String,String> params);
	/**
	 * 项目部课酬查询数据
	 * */
	List<Map<String, Object>> projectKcQuery(Map<String, Object> params);
	/**
	 * 查询未发已发教师课酬明细
	 */
	List<Map<String, Object>> getKCDetail(Map<String, Object> param);
	/**
	 * 根据流水号删除课酬数据
	 * @param map
	 */
	void deleteCourseFeeBySerialNum(Map<String, Object> map);
	void deleteCourseFeeApproveBySerialNum(Map<String, Object> map);
	void deleteCourseFeeBackBySerialNum(Map<String, Object> map);
	void deleteFeeDeduct(Map<String, Object> map);

	/**
	 * 获取最大流水号
	 * @return
	 */
	String getMaxSerialNum(Map<String, Object> map);

	/**
	 * 查询项目是否产生课酬
	 * @param map
	 * @return
	 */
    List<Map<String,String>> existFee(Map map);

	/**
	 * 根据项目权限查询流水
	 * @param params
	 * @return
	 */
	List<String> getSerialNumbers(Map<String,Object> params);

	/**
	 * 查询课酬统计页明细
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getQueryKCDetail(Map<String, Object> param);
	/**
	 * @Author WangYuelei
	 * @Description 查询发过课酬的项目数据
	 * @Date 2019/1/25 18:51
	 * @Param [map]
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	List<Map<String,Object>> classInformationPorjectData(Map<String,Object> map) throws RuntimeException;
	/**
	 * @Author WangYuelei
	 * @Description 查询已发教师的教师数据
	 * @Date 2019/1/25 18:51
	 * @Param [map]
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
 	List<Map<String,Object>> classInformationTeacherData(Map<String,Object> map) throws RuntimeException;

 	/**
 	 * @Author SongZiXian
 	 * @Description 教研室课酬
 	 * @Date 2019/2/23 0023 下午 15:45
 	 * @Param [params]
 	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
 	 **/
    List<Map<String, Object>> getResearchKc(Map<String, Object> params);

}
