package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.MessageDao;
import com.joysupply.dao.OrganizePeopleDao;
import com.joysupply.dao.ProjectApproveOptionDao;
import com.joysupply.dao.ProjectDao;
import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.Project;
import com.joysupply.entity.ProjectApproveOption;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ProjectApproveOptionService;
import com.joysupply.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ProjectApproveOptionService")
public class ProjectApproveOptionServiceImpl implements ProjectApproveOptionService {
    @Autowired
    private ProjectApproveOptionDao projectApproveOptionDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private OrganizePeopleDao organizePeopleDao;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public ResultData<List<Map<String, Object>>> preApproved(Map<String, Object> map) {
        List<Map<String, Object>> data = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("create_time desc");
        try {
            data = projectApproveOptionDao.preApproved(map);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("获取待审批列表", ex);
        }
        PageInfo page = new PageInfo(data);
        long totalNum = page.getTotal();
        map.clear();
        return new ResultData<>((int) totalNum, data);
    }

    @Override
    public ResultData<List<Map<String, Object>>> afterApproved(Map<String, Object> map) {
        List<Map<String, Object>> data = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("approve_date_time desc");
        try {
            data = projectApproveOptionDao.afterApproved(map);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("获取已审批列表", ex);
        }
        PageInfo page = new PageInfo(data);
        long totalNum = page.getTotal();
        map.clear();
        return new ResultData<>((int) totalNum, data);
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryOpioninList(Map<String, Object> map) throws BusinessLevelException {
        List<Map<String, Object>> data = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        try {
            data = projectApproveOptionDao.queryOpioninList(map);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("获取审批意见", ex);
        }
        PageInfo page = new PageInfo(data);
        long totalNum = page.getTotal();
        map.clear();
        return new ResultData<>((int) totalNum, data);
    }

    /**
     * @Author MaZhuli
     * @Description 项目/变更审批
     * @Date 2018/11/13 8:43
     * @Param [list]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult auditProject(List<ProjectApproveOption> projectList, List<ProjectApproveOption> changeList, boolean isApproved) throws BusinessLevelException {
        try {
            if (isApproved) {
                //审核通过--项目
                List<Map<String, Object>> projectMsgList = new ArrayList<>();
                for (ProjectApproveOption approve : projectList) {
                    approve.setApproveDateTime(DateUtil.ToDateTimeString());
                    //查找下级审批
                    ProjectApproveOption currentProjectApproveOption = projectApproveOptionDao.getCurrentProjectApproveOption(approve);
                    currentProjectApproveOption.setApproveOrder(currentProjectApproveOption.getApproveOrder() + 1);
                    ProjectApproveOption nextProjectApproveOption = projectApproveOptionDao.getNextProjectApproveOption(currentProjectApproveOption);
                    if (null != nextProjectApproveOption) {
                        //有下级审批
                        //修改项目审批人
                        Project project = new Project();
                        project.setProjectId(approve.getProjectId());
                        project.setStatus(6);
                        project.setApprovaler(nextProjectApproveOption.getPersonCode());
                        projectApproveOptionDao.updateProjectApprovaler(project);
                        //修改当前审核记录
                        approve.setRowId(currentProjectApproveOption.getRowId());
                        projectApproveOptionDao.auditProject(approve);

                        //发送消息
                        Map msgMap = new HashMap();
                        OrganizePeople organizePeople = organizePeopleDao.getOrganizePeople(project.getApprovaler());
                        String name = organizePeople.getName();
                        Project curProject = projectDao.getProject(approve.getProjectId());
                        String content = messageDao.getContent("1");
                        String projectName = curProject.getProjectName();
                        content = content.replace("{name}", name).replace("{projectName}", projectName);
                        msgMap.put("content", content);
                        msgMap.put("personCode", organizePeople.getPersonCode());
                        msgMap.put("messageTime", DateUtil.ToDateTimeString());
                        msgMap.put("rowId", Constants.GUID());
                        msgMap.put("isReading", 0);
                        msgMap.put("type", "1");
                        projectMsgList.add(msgMap);
                    } else {
                        //无下级审批
                        //修改项目状态为审核通过
                        Project project = new Project();
                        project.setProjectId(approve.getProjectId());
                        project.setStatus(3);
                        projectApproveOptionDao.updateProjectStatus(project);
                        //修改当前审核记录
                        approve.setRowId(currentProjectApproveOption.getRowId());
                        projectApproveOptionDao.auditProject(approve);
                        Project firstChange = projectDao.getOriginProject(approve.getProjectId());
                        firstChange.setChangeId(Constants.GUID());
                        //变更表备份第一条数据
                        projectDao.changeProject(firstChange);
                    }
                }
                if (projectMsgList.size() > 0) {
                    messageDao.addMessages(projectMsgList);
                }
                List<Map<String, Object>> changeMsgList = new ArrayList<>();
                //审核通过--变更
                for (ProjectApproveOption approve : changeList) {
                    //查找下级审批
                    approve.setApproveDateTime(DateUtil.ToDateTimeString());
                    ProjectApproveOption currentProjectApproveOption = projectApproveOptionDao.getCurrentProjectApproveOption(approve);
                    currentProjectApproveOption.setApproveOrder(currentProjectApproveOption.getApproveOrder() + 1);
                    ProjectApproveOption nextProjectApproveOption = projectApproveOptionDao.getNextProjectApproveOption(currentProjectApproveOption);
                    if (null != nextProjectApproveOption) {
                        //有下级审批
                        //修改变更审批人
                        Project project = new Project();
                        project.setChangeId(approve.getChangeId());
                        project.setStatus(6);
                        project.setApprovaler(nextProjectApproveOption.getPersonCode());
                        projectApproveOptionDao.updateChangeApprovaler(project);
                        //修改当前审核记录
                        approve.setRowId(currentProjectApproveOption.getRowId());
                        projectApproveOptionDao.auditProject(approve);
                        //发送消息
                        Map msgMap = new HashMap();
                        OrganizePeople organizePeople = organizePeopleDao.getOrganizePeople(project.getApprovaler());
                        String name = organizePeople.getName();
                        Project curProject = projectDao.getChangeProject(approve.getChangeId());
                        String content = messageDao.getContent("1");
                        String projectName = curProject.getProjectName();
                        content = content.replace("{name}", name).replace("{projectName}", projectName);
                        msgMap.put("content", content);
                        msgMap.put("personCode", organizePeople.getPersonCode());
                        msgMap.put("messageTime", DateUtil.ToDateTimeString());
                        msgMap.put("rowId", Constants.GUID());
                        msgMap.put("isReading", 0);
                        msgMap.put("type", "1");
                        changeMsgList.add(msgMap);
                    } else {
                        //无下级审批
                        //修改变更状态为审核通过
                        Project project = new Project();
                        project.setChangeId(approve.getChangeId());
                        project.setStatus(3);
                        projectApproveOptionDao.updateChangeStatus(project);
                        //修改当前审核记录
                        approve.setRowId(currentProjectApproveOption.getRowId());
                        projectApproveOptionDao.auditProject(approve);
                    }
                }
                if (changeMsgList.size() > 0) {
                    messageDao.addMessages(changeMsgList);
                }
            } else {
                //审核拒绝--项目
                for (ProjectApproveOption approve : projectList) {
                    approve.setApproveDateTime(DateUtil.ToDateTimeString());
                    //删除所有下级审批
                    ProjectApproveOption currentProjectApproveOption = projectApproveOptionDao.getCurrentProjectApproveOption(approve);
                    int i = projectApproveOptionDao.delNextProjectApproveOption(currentProjectApproveOption);
                    //修改项目状态
                    Project project = new Project();
                    project.setProjectId(approve.getProjectId());
                    project.setStatus(4);
                    projectApproveOptionDao.updateProjectStatus(project);
                    //修改当前审核记录
                    approve.setRowId(currentProjectApproveOption.getRowId());
                    projectApproveOptionDao.auditProject(approve);
                }
                //审核拒绝--变更
                for (ProjectApproveOption approve : changeList) {
                    approve.setApproveDateTime(DateUtil.ToDateTimeString());
                    //删除所有下级审批
                    ProjectApproveOption currentProjectApproveOption = projectApproveOptionDao.getCurrentProjectApproveOption(approve);
                    projectApproveOptionDao.delNextProjectApproveOption(currentProjectApproveOption);
                    //修改项目状态
                    Project project = new Project();
                    project.setChangeId(approve.getChangeId());
                    project.setStatus(4);
                    projectApproveOptionDao.updateChangeStatus(project);
                    //修改当前审核记录
                    approve.setRowId(currentProjectApproveOption.getRowId());
                    projectApproveOptionDao.auditProject(approve);
                }
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("项目/变更审批失败", ex);
        }
    }

}
