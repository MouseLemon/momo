package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.JobType;
import com.joysupply.entity.OrganizeType;


import com.joysupply.entity.OrganizeStruct;

/**
 * 组织结构数据层接口
 *
 * @author Administrator
 */
public interface OrganizeStructDao {
    /**
     * @Author MaZhuli
     * @Description 查询所有一级部门
     * @Date 2018/10/31 17:40
     * @Param []
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> queryFirstOrganizeList() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据父id查询子部门
     * @Date 2018/10/31 17:41
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> querySonOrganizeList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 添加部门
     * @Date 2018/10/31 17:41
     * @Param [map]
     * @Return int
     **/
    int addOrganizeStruct(OrganizeStruct organizeStruct) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除部门
     * @Date 2018/10/31 18:06
     * @Param [organizeStruct]
     * @Return void
     **/
    int delOrganizeStruct(OrganizeStruct organizeStruct) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改部门信息
     * @Date 2018/10/31 18:06
     * @Param [organizeStruct]
     * @Return void
     **/
    int updOrganizeStruct(OrganizeStruct organizeStruct) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取岗位列表
     * @Date 2018/11/1 14:37
     * @Param []
     * @Return java.util.List<com.joysupply.entity.JobType>
     **/
    List<JobType> getJobTypeList() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取组织类型列表
     * @Date 2018/11/1 15:06
     * @Param []
     * @Return java.util.List<com.joysupply.entity.OrganizeType>
     **/
    List<OrganizeType> getOrganizeTypeList() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取所有项目部
     * @Date 2018/12/5 9:31
     * @Param []
     * @Return java.util.List<com.joysupply.entity.OrganizeType>
     **/
    List<OrganizeStruct> getProjectDepList() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据organizeCode获取组织
     * @Date 2018/11/1 16:32
     * @Param [organizeCode]
     * @Return com.joysupply.entity.OrganizeStruct
     **/
    OrganizeStruct getOrganizeStruct(String organizeCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取组织人员列表
     * @Date 2018/11/2 9:08
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    List<Map> getOrganizePeopleList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取所有子部门
     * @Date 2018/11/2 11:54
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<String> getOrganizeCodeList() throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取父部门
     * @Date 2018/11/2 11:54
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    OrganizeStruct getParent(OrganizeStruct organizeStruct) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取子部门列表
     * @Date 2018/11/5 13:54
     * @Param [organizeStruct]
     * @Return java.util.List<com.joysupply.entity.OrganizeStruct>
     **/
    List<OrganizeStruct> getSon(OrganizeStruct organizeStruct) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据personCode获取OrganizeStruct
     * @Date 2018/11/5 14:07
     * @Param [personCode]
     * @Return com.joysupply.entity.OrganizeStruct
     **/
    OrganizeStruct getOrganizeStructByPersonCode(String personCode) throws RuntimeException;

    List<OrganizeStruct> getAll();

    Integer getPeopleCount(List<OrganizeStruct> son);

    void updSonCompanyCode(OrganizeStruct organizeStruct);
    
    void upProjectInfoOsCode (OrganizeStruct organizeStruct);
}
