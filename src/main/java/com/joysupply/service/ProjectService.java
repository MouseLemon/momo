package com.joysupply.service;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.*;
import com.joysupply.util.OpResult;
import com.joysupply.exception.BusinessLevelException;

/**
 * 项目业务层
 *
 * @author Administrator
 */
public interface ProjectService {

    Map<String, Object> getProjectList(Map map) throws BusinessLevelException;

    Map<String, Object> getProjectListA(Map map) throws BusinessLevelException;

    List<Project> getProjectListNoPage(SystemUser user) throws BusinessLevelException;

    Project getProject(String projectId) throws BusinessLevelException;

    boolean checkProjectCode(Project project) throws BusinessLevelException;

    OpResult addProject(ProjectAndFile projectAndFile) throws BusinessLevelException;

    OpResult delProject(Project project) throws BusinessLevelException;

    OpResult updProject(Project project) throws BusinessLevelException;

    Map<String, Object> getChangeList(Map map) throws BusinessLevelException;

    Map<String, Object> getAccessoryList(Map map) throws BusinessLevelException;

    Map<String, Object> getAuditOpionList(Map map) throws BusinessLevelException;

    OpResult delAccessory(ProjectAddFile projectAddFile) throws BusinessLevelException;

    OpResult addAccessory(ProjectAddFile projectAddFile) throws BusinessLevelException;

    OpResult commitProject(List<ProjectApproveOption> list, Map map) throws BusinessLevelException;

    OpResult changeProject(Project project) throws BusinessLevelException;

    Project getChangeProject(String changeId) throws BusinessLevelException;

    boolean getChangeInAudit(Project project) throws BusinessLevelException;

    Map<String, Object> getCommitProjectList(Map map) throws BusinessLevelException;

    OpResult updateTempCode(Map map) throws BusinessLevelException;

    List<String> getProjectListInFee(String paramCode) throws BusinessLevelException;

    Map<String, Object> getByBmProjectInfo(Map<String, Object> map) throws BusinessLevelException;
}
