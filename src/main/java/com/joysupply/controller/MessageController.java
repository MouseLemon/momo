package com.joysupply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.joysupply.service.MessageService;
import com.joysupply.util.OpResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/message")
public class MessageController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    MessageService messageService;

    /**
     * 消息设置页面
     *
     * @return
     */
    @RequestMapping(value = "/MessageSetting")
    public ModelAndView MessageSetting(Model model) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        model.addAllAttributes(resultMap);
        return new ModelAndView("system/customMessage");
    }

    /**
     * 查询自定义消息列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/getCustomMessage")
    @ResponseBody
    public Map<String, Object> getCustomMessage() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = messageService.getCustomMessage();
        map.put("data", list);
        return map;
    }

    /**
     * 添加消息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public OpResult addMessage(@RequestParam Map<String, Object> map) {
        try {
            messageService.addMessage(map);
            return new OpResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("发送失败!");
        }
    }

    /**
     * 查询用户未读消息
     */
    @RequestMapping(value = "/getUnreadMessage")
    @ResponseBody
    public Map<String, Object> getUnreadMessage(String personCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = messageService.getUnreadMessageList(personCode);
        map.put("data", list);
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 读消息
     * @Date 2018/12/3 15:06
     * @Param [rowId]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/readMessage", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateMessageStatus(@RequestBody Map map, HttpServletRequest request) {
        try {
            messageService.updateMessageStatus(map.get("rowId").toString());
            return new OpResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("读消息失败!");
        }

    }

    /**
     * 更新自定义消息内容
     */
    @RequestMapping(value = "/updateMessageContent")
    @ResponseBody
    public OpResult updateMessageContent(@RequestParam Map<String, Object> map) {
        try {
            messageService.updateMessageContent(map);
            return new OpResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("修改失败!");
        }
    }
    /**
     * @Author MaZhuli
     * @Description 获取消息列表
     * @Date 2018/12/11 15:46
     * @Param [map]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value = "/getMessageList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMessageList(@RequestParam Map map) {
        try {
            map.put("personCode",getUser().getPersonCode());
            return messageService.getMessageList(map);
        } catch (BusinessLevelException e) {
            log.error("获取消息列表失败:" + e.getMessage());
            return null;
        }
    }
}
