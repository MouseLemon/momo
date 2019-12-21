package com.joysupply.dao;

import com.joysupply.entity.DataDictionary;

import java.util.List;
import java.util.Map;

public interface MessageDao {

    /**
     * 查询自定义内容
     *
     * @param type
     * @return
     */
    String getContent(String type);

    /**
     * 添加消息数据
     *
     * @param data
     */
    void addMessage(Map<String, Object> data);

    /**
     * 自定义消息列表
     *
     * @return
     */
    List<Map<String, Object>> getCustomMessage();

    /**
     * 查询用户未读消息列表
     *
     * @param personCode
     * @return
     */
    List<Map<String, Object>> getUnreadMessageList(String personCode);

    /**
     * 修改消息为已读
     *
     * @param messageId
     */
    void updateMessageStatus(String messageId) throws RuntimeException;

    /**
     * 修改自定义消息模板
     *
     * @param map
     */
    void updateCustomMessageContent(Map<String, Object> map);

    /**
     * @Author MaZhuli
     * @Description 批量发送消息
     * @Date 2018/11/28 14:31
     * @Param [msgList]
     * @Return int
     **/
    int addMessages(List<Map<String, Object>> msgList) throws RuntimeException;

    List<Map<String, Object>> getAllMessageList(String personCode);

    /**
     * @Author MaZhuli
     * @Description 获取消息列表
     * @Date 2018/12/11 15:49
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    List<DataDictionary> getMessageList(Map map) throws RuntimeException;

    /**
     * personCode查询教师姓名
     * @param personCode
     * @return
     */
	String getTeacherNameByPersonCode(String personCode);

	/**
	 * 查询项目名称
	 * @param projectId
	 * @return
	 */
	String getProjectNameById(String projectId);

	/**
	 * 查询用户姓名
	 * @param personCode
	 * @return
	 */
	String getPersonNameByPersonCode(String personCode);
}
