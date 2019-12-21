package com.joysupply.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.entity.DataDictionary;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.PageHelperUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.MessageDao;
import com.joysupply.service.MessageService;

@Service("messageService")
public class MessageServiceImpl extends BaseService implements MessageService {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    MessageDao messageDao;

    @Override
    public String getContent(String type) {
        return messageDao.getContent(type);
    }

    @Override
    public void addMessage(Map<String, Object> map) {
        Map<String, Object> data = new HashMap<String, Object>();
        String row_id = UUID.randomUUID().toString();
        String personCode = map.get("personCode").toString();
        data.put("row_id", row_id);
        data.put("personCode", personCode);
        //查询模板消息
        String type = map.get("type").toString();
        data.put("type", type);
        String content = getContent(type);
        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue().toString();
            content = content.replace("{" + key + "}", value);
        }
        data.put("content", content);
        messageDao.addMessage(data);
    }

    @Override
    public List<Map<String, Object>> getCustomMessage() {

        return messageDao.getCustomMessage();
    }

    @Override
    public List<Map<String, Object>> getUnreadMessageList(String personCode) {
        return messageDao.getUnreadMessageList(personCode);
    }

    @Override
    public void updateMessageStatus(String messageId) throws BusinessLevelException {
        try {
            messageDao.updateMessageStatus(messageId);
        }catch (RuntimeException ex){
            log.error("读消息失败" + ex.getMessage());
            throw new BusinessLevelException("读消息失败", ex);
        }
    }

    @Override
    public void updateMessageContent(Map<String, Object> map) {
        messageDao.updateCustomMessageContent(map);
    }

    @Override
    public List<Map<String, Object>> getAllMessageList(String personCode) {
        return messageDao.getAllMessageList(personCode);
    }
    
    /**
     * @Author MaZhuli
     * @Description 获取消息列表失败
     * @Date 2018/12/11 15:49
     * @Param [map]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Override
    public Map<String, Object> getMessageList(Map map) throws BusinessLevelException {
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("is_reading,message_time desc");
        List<DataDictionary> list = null;
        try {
            list = messageDao.getMessageList(map);
        } catch (RuntimeException e) {
            log.error("获取消息列表失败:" + e.getMessage());
            throw new BusinessLevelException("获取消息列表失败", e);
        }
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

	@Override
	public String getTeacherNameByPersonCode(String personCode) {
		return messageDao.getTeacherNameByPersonCode(personCode);
	}
	
	
	
	@Override
	public String getProjectNameById(String projectId) {
		return messageDao.getProjectNameById(projectId);
	}

	@Override
	public String getPersonNameByPersonCode(String personCode) {
		return messageDao.getPersonNameByPersonCode(personCode);
	}

}
