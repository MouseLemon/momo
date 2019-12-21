package com.joysupply.service;

import com.joysupply.entity.TeachResearchRoom;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 教研室业务层
 *
 * @author Administrator
 */
public interface TeachResearchRoomService {

    /**
     * @Author Dreamer
     * @Description 获取教研室列表
     * @Date 上午 10:39 2018/12/15 0015
     * @Param [map]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> getResearchOfficeList(Map map) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 无分页获取教研室列表
     * @Date 上午 10:39 2018/12/15 0015
     * @Param []
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<TeachResearchRoom> getResearchOfficeListNoPage() throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 获取教研室
     * @Date 上午 10:39 2018/12/15 0015
     * @Param [organizeCode]
     * @return com.joysupply.entity.TeachResearchRoom
     **/
    TeachResearchRoom getResearchOffice(String organizeCode) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 添加教研室
     * @Date 上午 10:40 2018/12/15 0015
     * @Param [teachResearchRoom]
     * @return com.joysupply.util.OpResult
     **/
    OpResult addResearchOffice(TeachResearchRoom teachResearchRoom) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 修改教研室
     * @Date 上午 10:40 2018/12/15 0015
     * @Param [teachResearchRoom]
     * @return com.joysupply.util.OpResult
     **/
    OpResult updResearchOffice(TeachResearchRoom teachResearchRoom) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 删除教研室
     * @Date 上午 10:40 2018/12/15 0015
     * @Param [teachResearchRoom]
     * @return com.joysupply.util.OpResult
     **/
    OpResult delResearchOffice(TeachResearchRoom teachResearchRoom) throws BusinessLevelException;

    /**
     * @Author Dreamer
     * @Description 根据名称查询教研室
     * @Date 上午 10:43 2018/12/15 0015
     * @Param [office_name]
     * @return java.util.Map
     **/
    Map selectChecking(String office_name);
}
