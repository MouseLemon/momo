package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.*;
import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.GenerateCodeService;
import com.joysupply.service.ProjectService;
import com.joysupply.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("projectService")
public class ProjectServiceImpl extends BaseService implements ProjectService {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private OrganizeStructDao organizeStructDao;
    @Autowired
    private OrganizePeopleDao organizePeopleDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private AuthorityManageDao authorityManageDao;
    @Autowired
    private ProjectSchedulePlanDao projectSchedulePlanDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    GenerateCodeService generateCodeService;

    private Log log = LogFactory.getLog(getClass());

    /**
     * @Author MaZhuli
     * @Description 获取项目列表
     * @Date 2018/11/5 10:14
     * @Param [map]
     * @Return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    @Override
    public Map<String, Object> getProjectList(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("pi.create_time desc");
        try {
            list = projectDao.getProjectList(map);
        } catch (RuntimeException e) {
            log.error("获取项目列表:" + e.getMessage());
            throw new BusinessLevelException("获取项目列表失败", e);
        }
        com.github.pagehelper.PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * @Author Dreamer
     * @Description 项目部主任
     * @Date 16:38 2019/4/10
     * @Param [map]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/

    @Override
    public Map<String, Object> getProjectListA(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("pi.create_time desc");
        try {
            list = projectDao.getProjectListA(map);
        } catch (RuntimeException e) {
            log.error("获取项目列表:" + e.getMessage());
            throw new BusinessLevelException("获取项目列表失败", e);
        }
        com.github.pagehelper.PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目列表
     * @Date 2018/12/1 15:20
     * @Param [user]
     * @Return java.util.List<com.joysupply.entity.Project>
     **/
    @Override
    public List<Project> getProjectListNoPage(SystemUser user) throws BusinessLevelException {
        try {
            List<String> personRoles = roleDao.getPersonRoles(user.getPersonCode());
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                return projectDao.getProjectListNoPage("");
            } else if (personRoles.contains("19") ||  personRoles.contains("1901") ||personRoles.contains("1902")) {
                return projectDao.getProjectListNoPage("");
            } else if (personRoles.contains("10") ||  personRoles.contains("1001") ||personRoles.contains("1002")) {
                return projectDao.getProjectListNoPageA(user.getPersonCode());
            } else {
                return projectDao.getProjectListNoPage(user.getPersonCode());
            }
        } catch (RuntimeException e) {
            log.error("获取项目列表:" + e.getMessage());
            throw new BusinessLevelException("获取项目列表失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目信息
     * @Date 2018/11/5 14:46
     * @Param [projectId]
     * @Return com.joysupply.entity.Project
     **/
    @Override
    public Project getProject(String projectId) throws BusinessLevelException {
        try {
            return projectDao.getProject(projectId);
        } catch (RuntimeException e) {
            log.error(projectId);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目变更
     * @Date 2018/11/12 10:14
     * @Param [changeId]
     * @Return com.joysupply.entity.Project
     **/
    @Override
    public Project getChangeProject(String changeId) throws BusinessLevelException {
        try {
            return projectDao.getChangeProject(changeId);
        } catch (RuntimeException e) {
            log.error(changeId);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 查询是否有未完成的变更
     * @Date 2018/11/12 11:03
     * @Param [project]
     * @Return boolean
     **/
    @Override
    public boolean getChangeInAudit(Project project) throws BusinessLevelException {
        try {
            int changeInAudit = projectDao.getChangeInAudit(project);
            if (changeInAudit > 0) {
                return false;
            } else {
                return true;
            }
        } catch (RuntimeException e) {
            log.error(project);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    @Override
    public Map<String, Object> getCommitProjectList(Map map) throws BusinessLevelException {
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("submitTime desc");
        List<DataDictionary> list = null;
        try {
            list = projectDao.getCommitProjectList(map);
        } catch (RuntimeException e) {
            log.error("获取提交信息列表失败:" + e.getMessage());
            throw new BusinessLevelException("获取提交信息列表失败", e);
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
    public OpResult updateTempCode(Map map) throws BusinessLevelException {
        try{
            projectDao.updateTempCode(map);
            return new OpResult();
        }catch (RuntimeException e){
            return new OpResult("修改成绩模板失败!");
        }
    }

    @Override
    public List<String> getProjectListInFee(String paramCode) throws BusinessLevelException {
        try{
            Map map = new HashMap();
            map.put("roleCode","16");
            map.put("paramCode",paramCode);
            List<String> personList =  roleDao.getOtherPersonHaveSameRole(map);
            if(personList != null && personList.size()>0) {
                return authorityManageDao.getFeePersonProjectAuth(personList);
            }else{
                return null;
            }
        }catch (RuntimeException e){
            log.error("获取其它财务人员项目列表失败");
            throw new BusinessLevelException("获取其它财务人员项目列表失败",e);
        }
    }

    /**
     * @Author SongZiXian
     * @Description 获取二期相同项目编号数据
     * @Date 2019/3/20 0020 下午 17:51
     * @Param [map]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Override
    public Map<String, Object> getByBmProjectInfo(Map<String, Object> map) {
        Map<String, Object> byBmProjectInfo = projectDao.getByBmProjectInfo(map);
        if(byBmProjectInfo != null && byBmProjectInfo.size() > 0){
            byBmProjectInfo.put("success",true);
        }
        return byBmProjectInfo;
    }

    /**
     * @Author MaZhuli
     * @Description 校验项目编码
     * @Date 2018/12/10 17:43
     * @Param [projectCode]
     * @Return boolean
     **/
    @Override
    public boolean checkProjectCode(Project project) throws BusinessLevelException {
        try {
            int count = projectDao.checkProjectCode(project);
            if (count > 0) {
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            throw new BusinessLevelException("校验项目编码失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 新增项目
     * @Date 2018/11/5 14:47
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult addProject(ProjectAndFile projectAndFile) throws BusinessLevelException {
        try {
            Project project = projectAndFile;
            int count = projectDao.checkProjectCode(project);
            if (count > 0) {
                return new OpResult("该项目编码已经存在!");
            }
            String projectId = Constants.GUID();
            project.setProjectId(projectId);
            project.setCreateTime(DateUtil.ToDateTimeString());
            projectDao.addProject(project);

            // 判断是否为二期项目
            String project_code = project.getProjectCode();
            int bmProjectCount = projectDao.countProject(project_code);
            if (bmProjectCount == 0) {
                // 二期无该项目信息, 是一期新建项目, 添加班级信息
                Map<String, Object> gradeMap = new HashMap<>();
                gradeMap.put("grade_id", UUID.randomUUID().toString());
                gradeMap.put("grade_name", "");
                gradeMap.put("grade_code", project.getProjectCode() + "1");
                gradeMap.put("project_id", project.getProjectId());
                gradeMap.put("person_count", project.getStartPersonCount());
                projectDao.addGrade(gradeMap);
            }

            List<ProjectAddFile> list = new ArrayList<>();
            String fileName = projectAndFile.getFileNames();
            String fileUrl = projectAndFile.getFileUrls();
            if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                String[] fileNames = fileName.split(",");
                String[] fileUrls = fileUrl.split(",");
                for (int i = 0; i < fileNames.length; i++) {
                    ProjectAddFile addFile = new ProjectAddFile();
                    addFile.setRowId(Constants.GUID());
                    addFile.setProjectId(projectId);
                    addFile.setAddName(fileNames[i]);
                    addFile.setUploader(project.getCreater());
                    addFile.setUrl(fileUrls[i]);
                    addFile.setUploadTime(DateUtil.ToDateTimeString());
                    list.add(addFile);
                }
            }
            if (list.size() > 0) {
                projectDao.addProjectAddFile(list);
            }
            Map map = new HashMap();
            String[] ids = projectId.split(",");
            map.put("dataIds", ids);
            map.put("dataCode", 2);
            map.put("personCode", projectAndFile.getCreater());
            authorityManageDao.insertData(map);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error((Project) projectAndFile);
            throw new BusinessLevelException("新增项目失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除项目
     * @Date 2018/11/5 14:48
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult delProject(Project project) throws BusinessLevelException {
        try {
            projectDao.delProject(project);
            projectSchedulePlanDao.delProjectSchedulePlanByProjectId(project);
            authorityManageDao.delPersonprojectAuth(project.getProjectId());
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(project);
            throw new BusinessLevelException("删除项目失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 修改项目
     * @Date 2018/11/5 14:50
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult updProject(Project project) throws BusinessLevelException {
        try {
            if (project.getChangeId() == null || project.getChangeId().equals("")) {
                int count = projectDao.checkProjectCode(project);
                if (count > 0) {
                    return new OpResult("该项目编码已经存在!");
                }
                projectDao.updProject(project);
            } else {
                int count = projectDao.checkProjectCode(project);
                if (count > 0) {
                    return new OpResult("该项目编码已经存在!");
                }
                projectDao.updChange(project);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(project);
            throw new BusinessLevelException("修改项目失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目变更列表
     * @Date 2018/11/7 18:45
     * @Param [map]
     * @Return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    @Override
    public Map<String, Object> getChangeList(Map map) throws BusinessLevelException {
        List<Map<String, Object>> returnList = new ArrayList<>();
        List<Project> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit")+1);
        PageHelper.orderBy("pih.create_time desc");
        try {
            list = projectDao.getChangeList(map);
            for (int i = 0; i < list.size() - 1; i++) {
                String changeContent = "";
                Map<String, Object> map1 = MapObj.objectToMap(list.get(i));
                Map<String, Object> map2 = MapObj.objectToMap(list.get(i + 1));
                for (String key : map1.keySet()) {
                    if (!key.equalsIgnoreCase("changeid")) {
                        if (PROJECT_PARAM.contains(key.toUpperCase())) {
                            if (null == map1.get(key)) {
                                map1.put(key, "无");
                            }
                            if (null == map2.get(key)) {
                                map2.put(key, "无");
                            }
                            if (!map1.get(key).equals(map2.get(key))) {
                                changeContent += ((PROJECT_PARAM.valueOf(key.toUpperCase()).getName()) + "由" + map2.get(key) + "变更为" + map1.get(key) + "<br/>");
                            }
                        }
                    }
                }
                map1.put("changeContent", changeContent);
                returnList.add(map1);
            }
        } catch (RuntimeException e) {
            log.error("获取项目变更列表:" + e.getMessage());
            throw new BusinessLevelException("获取项目变更列表", e);
        }
        com.github.pagehelper.PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum - 1);
        map.put("data", returnList);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目附件列表
     * @Date 2018/11/7 18:48
     * @Param [map]
     * @Return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    @Override
    public Map<String, Object> getAccessoryList(Map map) {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("upload_time desc");
        try {
            list = projectDao.getAccessoryList(map);
        } catch (RuntimeException e) {
            log.error("获取项目附件列表:" + e.getMessage());
            throw new BusinessLevelException("获取项目附件列表", e);
        }
        com.github.pagehelper.PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 获取审批意见列表
     * @Date 2018/11/7 18:51
     * @Param [map]
     * @Return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    @Override
    public Map<String, Object> getAuditOpionList(Map map) {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("pao.approve_date_time desc");
        try {
            if (Integer.parseInt(map.get("type").toString()) == 1) {
                list = projectDao.getProjectAuditOpionList(map);
            } else {
                list = projectDao.getChangeAuditOpionList(map);
            }
        } catch (RuntimeException e) {
            log.error("获取审批意见列表:" + e.getMessage());
            throw new BusinessLevelException("获取审批意见列表", e);
        } finally {
            PageHelper.clearPage();
        }
        com.github.pagehelper.PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 删除附件
     * @Date 2018/11/8 11:35
     * @Param [projectAddFile]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult delAccessory(ProjectAddFile projectAddFile) throws BusinessLevelException {
        try {
            projectDao.delAccessory(projectAddFile);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(projectAddFile);
            throw new BusinessLevelException("删除附件失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 添加附件
     * @Date 2018/11/8 11:36
     * @Param [projectAddFile]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult addAccessory(ProjectAddFile projectAddFile) throws BusinessLevelException {
        try {
            projectDao.addAccessory(projectAddFile);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(projectAddFile);
            throw new BusinessLevelException("添加附件失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 项目提交审核
     * @Date 2018/11/9 15:41
     * @Param [list]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult commitProject(List<ProjectApproveOption> list, Map map) throws BusinessLevelException {
        try {
            String changeId = list.get(0).getChangeId();
            if (null == changeId || changeId.equals("")) {
                projectDao.commitProject(map);
                //发送消息
                List<String> idList = (List<String>) map.get("list");
                List<Map<String, Object>> auditMsgList = new ArrayList<>();
                OrganizePeople organizePeople = organizePeopleDao.getOrganizePeople(map.get("approvaler").toString());
                Map param = new HashMap();
                param.put("roleCode","14");
                String auditContentTemp = messageDao.getContent("1");
                String projectContentTemp = messageDao.getContent("4");
                String name = organizePeople.getName();
                for (String projectId : idList) {
                    Map msgMap = new HashMap();
                    Project project = projectDao.getProject(projectId);
                    //删除从未开始过的审批流程
                    projectDao.delApprove(project);
                    String projectName = project.getProjectName();
                    String auditContent = auditContentTemp.replace("{name}", name).replace("{projectName}", projectName);
                    msgMap.put("content", auditContent);
                    msgMap.put("personCode", organizePeople.getPersonCode());
                    msgMap.put("messageTime", DateUtil.ToDateTimeString());
                    msgMap.put("rowId", Constants.GUID());
                    msgMap.put("isReading", 0);
                    msgMap.put("type", "1");
                    auditMsgList.add(msgMap);
                    param.put("projectId",projectId);
                    List<OrganizePeople> personByRole = roleDao.getPersonByRole(param);
                    for(OrganizePeople person:personByRole){
                        Map projectMsgMap = new HashMap();
                        String projectContent = projectContentTemp.replace("{name}", person.getName()).replace("{projectName}", projectName);
                        projectMsgMap.put("content", projectContent);
                        projectMsgMap.put("personCode", person.getPersonCode());
                        projectMsgMap.put("messageTime", DateUtil.ToDateTimeString());
                        projectMsgMap.put("rowId", Constants.GUID());
                        projectMsgMap.put("isReading", 0);
                        projectMsgMap.put("type", "4");
                        auditMsgList.add(projectMsgMap);
                    }
                }
                messageDao.addMessages(auditMsgList);
            } else {
                projectDao.commitChange(map);
                List<String> changeIdList = (List<String>) map.get("list");
                for (String changeIdItem : changeIdList) {
                    Project change = projectDao.getOriginChange(changeIdItem);
                    projectDao.updProjectByChange(change);
                }
                List<Map<String, Object>> msgList = new ArrayList<>();
                OrganizePeople organizePeople = organizePeopleDao.getOrganizePeople(map.get("approvaler").toString());
                String name = organizePeople.getName();
                for (String changeIdItem : changeIdList) {
                    Map msgMap = new HashMap();
                    Project project = projectDao.getChangeProject(changeIdItem);
                    project.setProjectId(null);
                    projectDao.delApprove(project);
                    String content = messageDao.getContent("1");
                    String projectName = project.getProjectName();
                    content = content.replace("{name}", name).replace("{projectName}", projectName);
                    msgMap.put("content", content);
                    msgMap.put("personCode", organizePeople.getPersonCode());
                    msgMap.put("messageTime", DateUtil.ToDateTimeString());
                    msgMap.put("rowId", Constants.GUID());
                    msgMap.put("isReading", 0);
                    msgMap.put("type", "1");

                    msgList.add(msgMap);
                }
                messageDao.addMessages(msgList);
            }
            projectDao.addProjectApproveOption(list);
            return new OpResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(list);
            throw new BusinessLevelException("项目提交审核失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 变更项目
     * @Date 2018/11/5 14:50
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult changeProject(Project project) throws BusinessLevelException {
        try {
            int count = projectDao.checkProjectCode(project);
            if (count > 0) {
                return new OpResult("该项目编码已经存在!");
            }
            projectDao.changeProject(project);
            return new OpResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(project);
            throw new BusinessLevelException("变更项目失败", e);
        }
    }

    public String generateProjectCode() {
        Map map = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(date);
        String year = currentDate.substring(0, 4);
        String month = currentDate.substring(5, 7);
        String day = currentDate.substring(8, 10);
        String type = "project";
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        map.put("type", type);
        Integer currentVersion = generateCodeService.getCurrentVersion(map);
        String version = String.format("%04d", currentVersion);
        return year + month + version;
    }
}
