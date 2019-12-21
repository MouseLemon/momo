package com.joysupply.dao;

import com.joysupply.entity.Project;
import com.joysupply.entity.ProjectApproveOption;

import java.util.List;
import java.util.Map;

public interface ProjectApproveOptionDao {
    /**
     * 查询待审批列表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> preApproved(Map<String, Object> map) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 查询已审批列表
     * @Date 2018/11/26 17:08
     * @Param [map]
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> afterApproved(Map<String, Object> map) throws RuntimeException;

    /**
     * 查询审批意见列表
     *
     * @param map
     * @return
     * @throws RuntimeException
     */
    List<Map<String, Object>> queryOpioninList(Map<String, Object> map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description
     * @Date 2018/11/13 9:03
     * @Param [list]
     * @Return int
     **/
    int auditProject(ProjectApproveOption projectApproveOption) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取当前审批记录详细数据
     * @Date 2018/11/13 11:18
     * @Param [projectApproveOption]
     * @Return com.joysupply.entity.ProjectApproveOption
     **/
    ProjectApproveOption getCurrentProjectApproveOption(ProjectApproveOption projectApproveOption) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取下一级审批记录
     * @Date 2018/11/13 11:05
     * @Param [projectApproveOption]
     * @Return com.joysupply.entity.ProjectApproveOption
     **/
    ProjectApproveOption getNextProjectApproveOption(ProjectApproveOption projectApproveOption) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除所有下级审批
     * @Date 2018/11/13 11:15
     * @Param [approve]
     * @Return int
     **/
    int delNextProjectApproveOption(ProjectApproveOption approve) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改项目审批人
     * @Date 2018/11/13 11:33
     * @Param [project]
     * @Return int
     **/
    int updateProjectApprovaler(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改变更审批人
     * @Date 2018/11/13 11:34
     * @Param [project]
     * @Return int
     **/
    int updateChangeApprovaler(Project project) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 修改项目审批状态
     * @Date 2018/11/13 11:40
     * @Param [project]
     * @Return int
     **/
    int updateProjectStatus(Project project) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 修改变更审批状态
     * @Date 2018/11/13 11:40
     * @Param [project]
     * @Return int
     **/
    int updateChangeStatus(Project project) throws RuntimeException;
}
