package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;
import com.joysupply.util.ResultData;

/**
 * 课酬管理Service接口
 * @author Administrator
 *
 */
public interface KcManagerService {
	/**
	 * 按照时段查询课酬
	 * */
	List<Map<String, Object>> queryKcInfo(Map<String, Object> params);
	/**
	 * 保存课酬信息
	 * */
	String saveKcList(Map<String, Object> params);
	/**
	 * 查询上次课酬是否为工作量扣除
	 * */
	Map<String, Object> queryLastProject(Map<String, String> params);
	/**
	 * 检查是否可以保存
	 * */
	OpResult checkSave(Map<String, Object> params);
	/**
	 * 提交保存
	 * */
	OpResult commit(Map<String,Object> params);
	/**
	 * 查询课讲明细
	 * */
	List queryKcDetatils(Map<String,String> params);
	/**
	 * 项目部课酬查询数据
	 * */
	Map<String, Object> projectKcQuery(Map<String, Object> params);

	Map<String, Object> projectKcQueryA(Map<String, Object> params);
	/**
	 * 查询未发已发教师课酬明细
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getKCDetail(Map<String, Object> param);
	/**
	 * 根据流水号删除课酬数据
	 * @param map
	 */
	void deleteCourseFeeBySerialNum(Map<String, Object> map);

	/**
	 * 查询是否已产生课酬
	 * @param map
	 * @return
	 */
	List<Map<String,String>> existFee(Map map);

	/**
	 * 根据项目权限查询流水号
	 * @param params
	 * @return
	 */
	List<String> getSerialNumbers(Map<String,Object> params);

	
	/**
	 * 查询课酬统计页数据明细
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getQueryKCDetail(Map<String, Object> param);
	/**
	 * @Author WangYuelei
	 * @Description 上课信息查询项目
	 * @Date 2019/1/25 18:45
	 * @Param [map]
	 * @Return com.joysupply.util.ResultData<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 **/
	ResultData<List<Map<String,Object>>> classInformationPorjectData(Map<String,Object> map) throws BusinessLevelException;
	/**
	 * @Author WangYuelei
	 * @Description 导出使用 上课信息查询项目
	 * @Date 2019/1/26 15:51
	 * @Param [map]
	 * @Return com.joysupply.util.ResultData<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 **/
	ResultData<List<Map<String,Object>>> classInformationPorjectDataNoPage(Map<String,Object> map) throws BusinessLevelException;
	/**
	 * @Author WangYuelei
	 * @Description 上课信息查询老师
	 * @Date 2019/1/25 18:45
	 * @Param [map]
	 * @Return com.joysupply.util.ResultData<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 **/
	ResultData<List<Map<String,Object>>> classInformationTeacherData(Map<String,Object> map) throws BusinessLevelException;
	/**
	 * @Author WangYuelei
	 * @Description 导出使用 上课信息查询教师
	 * @Date 2019/1/26 15:52
	 * @Param [map]
	 * @Return com.joysupply.util.ResultData<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 **/
	ResultData<List<Map<String,Object>>> classInformationTeacherDataNoPage(Map<String,Object> map) throws BusinessLevelException;

	/**
	 * @Author SongZiXian
	 * @Description 教研室课酬
	 * @Date 2019/2/23 0023 下午 15:42
	 * @Param [param]
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 **/
	List<List<Map<String,Object>>> getResearchKc(Map<String, Object> param);

}
