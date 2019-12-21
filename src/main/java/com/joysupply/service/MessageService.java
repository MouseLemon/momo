package com.joysupply.service;

import com.joysupply.exception.BusinessLevelException;

import java.util.List;
import java.util.Map;

public interface MessageService {

    /**
     * 获取模板内容
     *
     * @param type
     * @return
     */
    String getContent(String type);

    /**
     * 添加消息
     *
     * @param map
     */
    void addMessage(Map<String, Object> map);

    /**
     * 查询自定义消息列表
     *
     * @return
     */
    List<Map<String, Object>> getCustomMessage();

    /**
     * 查询未读消息列表
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
    void updateMessageStatus(String messageId) throws BusinessLevelException;

    /**
     * 更新自定义模板消息
     *
     * @param map
     */
    void updateMessageContent(Map<String, Object> map);

    List<Map<String, Object>> getAllMessageList(String personCode);

    Map<String, Object> getMessageList(Map map) throws BusinessLevelException;
    
    /**
     * personCode查询教师姓名
     */
    String getTeacherNameByPersonCode(String personCode);
    
    /**
     * personCode查询用户姓名
     */
    String getPersonNameByPersonCode(String personCode);
    
    /**
     * ID查询项目名称
     * @param personCode
     * @return
     */
    String getProjectNameById(String personCode);
}
