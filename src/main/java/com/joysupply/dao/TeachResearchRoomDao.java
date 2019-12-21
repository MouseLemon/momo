package com.joysupply.dao;

import com.joysupply.entity.TeachResearchRoom;

import java.util.List;
import java.util.Map;

/**
 * 教研室数据层
 *
 * @author Administrator
 */
public interface TeachResearchRoomDao {
    /**
     * @Author MaZhuli
     * @Description 获取教研室列表
     * @Date 2018/11/3 11:42
     * @Param [map]
     * @Return java.util.Map<java.lang.String       ,       java.lang.Object>
     **/
    List<Map<String, Object>> getResearchOfficeList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取教研室列表无分页
     * @Date 2018/11/3 11:42
     * @Param [map]
     * @Return java.util.Map<java.lang.String       ,       java.lang.Object>
     **/
    List<TeachResearchRoom> getResearchOfficeListNoPage() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据教研室code获取教研室信息
     * @Date 2018/11/3 14:34
     * @Param [organizeCode]
     * @Return com.joysupply.entity.TeachResearchRoom
     **/
    TeachResearchRoom getResearchOffice(String organizeCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 添加教研室
     * @Date 2018/11/3 14:48
     * @Param [teachResearchRoom]
     * @Return int
     **/
    int addResearchOffice(TeachResearchRoom teachResearchRoom) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改教研室
     * @Date 2018/11/3 15:16
     * @Param [teachResearchRoom]
     * @Return int
     **/
    int updResearchOffice(TeachResearchRoom teachResearchRoom) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除教研室
     * @Date 2018/11/3 14:48
     * @Param [teachResearchRoom]
     * @Return void
     **/
    int delResearchOffice(TeachResearchRoom teachResearchRoom) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取所有教研室编码
     * @Date 2018/11/3 15:18
     * @Param []
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getResearchOfficeCodeList() throws RuntimeException;

    /**
     * @Author Dreamer
     * @Description 根据名称查询教研室
     * @Date 上午 10:42 2018/12/15 0015
     * @Param [office_name]
     * @return java.util.Map
     **/
    Map selectChecking(String office_name) throws RuntimeException;
}
