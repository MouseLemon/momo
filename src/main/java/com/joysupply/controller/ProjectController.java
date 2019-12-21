package com.joysupply.controller;

import com.joysupply.dao.StudentDao;
import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.*;
import com.joysupply.util.Constants;
import com.joysupply.util.DateUtil;
import com.joysupply.util.MapObj;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 项目controller
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/project")
public class ProjectController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private OrganizeStructService organizeStructService;
    @Autowired
    private OrganizePeopleService organizePeopleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StudentService studentService;

    private String getUserCode() {
        SystemUser user = getUser();
        return user.getPersonCode();
    }

    /**
     * @Author MaZhuli
     * @Description 测试页面跳转
     * @Date 2018/10/29 13:44
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    //TODO:需要删除
    @RequestMapping(value = "/page")
    public ModelAndView hello() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("fragments/content", resultMap);
    }

    /**
     * @Author MaZhuli
     * @Description 跳转项目列表页
     * @Date 2018/11/5 10:10
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/getProjectListPage")
    public ModelAndView getProjectListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", getUser());
        List<DataDictionary> projectTypeList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        try {
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                structList = organizeStructService.getProjectDepList();
            } else {
                OrganizeStruct curentStruct = organizeStructService.getOrganizeStructByPersonCode(user.getPersonCode());
                OrganizeStruct parent = organizeStructService.getParent(curentStruct);
                if (parent == null) {
                    //部门级别
                    structList = organizeStructService.getSon(curentStruct);
                } else {
                    String upCode = parent.getUpCode();
                    if (null == upCode || upCode.equals("")) {
                        //项目部级别
                        structList.add(curentStruct);
                    } else {
                        //项目部子级别
                        structList.add(parent);
                    }
                }
            }
        } catch (BusinessLevelException e) {
            log.error("获取项目类型和项目部失败" + e.getMessage());
        }
        if (!(structList.size() > 0)) {
            structList = null;
        }
        resultMap.put("projectTypeList", projectTypeList);
        resultMap.put("structList", structList);
        return new ModelAndView("project/projectManagement/projectInfo", resultMap);
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目列表分页数据
     * @Date 2018/11/5 14:34
     * @Param [map]
     * @Return java.util.Map<java.lang.String                                                               ,                                                               java.lang.Object>
     **/
    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProjectList(@RequestParam Map map) {
        try {
            List<String> projectInFeeList = new ArrayList<>();
            Object paramCode1 = map.get("paramCode");
            if (null != paramCode1 && !paramCode1.toString().equals("")) {
                String paramCode = map.get("paramCode").toString();
                List<String> personRoles = roleService.getPersonRoles(paramCode);
                if (personRoles.contains("16") || personRoles.contains("1601") ||personRoles.contains("1602")) {
                    projectInFeeList = projectService.getProjectListInFee(paramCode);
                }
            }
            if (projectInFeeList != null && projectInFeeList.size() > 0) {
                map.put("projectInFeeList", projectInFeeList);
            }
            List<String> personRoles = roleService.getPersonRoles(getUser().getPersonCode());
            if (null != map.get("isAuthList") && map.get("isAuthList").toString().equals("0")) {
                if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ){
                    return projectService.getProjectList(map);
                } else if (personRoles.contains("10") || personRoles.contains("1001") || personRoles.contains("1002")) {
                    map.put("personCode", getUserCode());
                    return projectService.getProjectListA(map);
                } else {
                    map.put("personCode", getUserCode());
                    return projectService.getProjectList(map);
                }
            } else {
                return projectService.getProjectList(map);
            }
        } catch (BusinessLevelException e) {
            log.error("获取项目列表:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/updateTempCode", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateTempCode(@RequestParam Map map) {
        try {
            return projectService.updateTempCode(map);
        } catch (BusinessLevelException e) {
            log.error("修改成绩模板失败:" + e.getMessage());
            return new OpResult("修改成绩模板失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 新增项目页面
     * @Date 2018/11/6 9:48
     * @Param [map]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/addProjectPage", method = RequestMethod.GET)
    public ModelAndView addOrganizeStructPage(@RequestParam Map map) {

        SystemUser user = getUser();
        List<DataDictionary> projectTypeList = new ArrayList<>();
        List<DataDictionary> languageList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        List<DataDictionary> feeTypeList = new ArrayList<>();
        try {
            feeTypeList = dataDictionaryService.getDicItemListNoPage("18");
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
            languageList = dataDictionaryService.getDicItemListNoPage("15");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                structList = organizeStructService.getProjectDepList();
            } else {
                OrganizeStruct curentStruct = organizeStructService.getOrganizeStructByPersonCode(user.getPersonCode());
                OrganizeStruct parent = organizeStructService.getParent(curentStruct);
                if (parent == null) {
                    //部门级别
                    structList = organizeStructService.getSon(curentStruct);
                } else {
                    String upCode = parent.getUpCode();
                    if (null == upCode || upCode.equals("")) {
                        //项目部级别
                        structList.add(curentStruct);
                    } else {
                        //项目部子级别
                        structList.add(parent);
                    }
                }
            }
        } catch (BusinessLevelException e) {
            log.error("跳转新增项目页面失败" + e.getMessage());
        }
        if (!(structList.size() > 0)) {
            structList = null;
        }
        map.put("projectTypeList", projectTypeList);
        map.put("structList", structList);
        map.put("languageList", languageList);
        map.put("feeTypeList", feeTypeList);
        return new ModelAndView("project/projectManagement/addProjectPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 编辑项目页面
     * @Date 2018/11/6 9:48
     * @Param [map]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editProjectPage", method = RequestMethod.GET)
    public ModelAndView editOrganizeStructPage(@RequestParam String projectId, @RequestParam Integer status) {
        Project project = projectService.getProject(projectId);
        Map<String, Object> map = MapObj.objectToMap(project);
        SystemUser user = getUser();
        List<DataDictionary> projectTypeList = new ArrayList<>();
        List<DataDictionary> languageList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        List<DataDictionary> feeTypeList = new ArrayList<>();
        try {
            feeTypeList = dataDictionaryService.getDicItemListNoPage("18");
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
            languageList = dataDictionaryService.getDicItemListNoPage("15");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                structList = organizeStructService.getProjectDepList();
            } else {
                OrganizeStruct curentStruct = organizeStructService.getOrganizeStructByPersonCode(user.getPersonCode());
                OrganizeStruct parent = organizeStructService.getParent(curentStruct);
                if (parent == null) {
                    //部门级别
                    structList = organizeStructService.getSon(curentStruct);
                } else {
                    String upCode = parent.getUpCode();
                    if (null == upCode || upCode.equals("")) {
                        //项目部级别
                        structList.add(curentStruct);
                    } else {
                        //项目部子级别
                        structList.add(parent);
                    }
                }
            }
        } catch (BusinessLevelException e) {
            log.error("跳转编辑项目页面失败" + e.getMessage());
        }
        map.put("projectTypeList", projectTypeList);
        map.put("structList", structList);
        map.put("languageList", languageList);
        map.put("feeTypeList", feeTypeList);
        map.put("status", status);
        return new ModelAndView("project/projectManagement/editProjectPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 跳转编辑变更页面
     * @Date 2018/11/12 8:57
     * @Param [changeId]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editChangePage", method = RequestMethod.GET)
    public ModelAndView editChangePage(@RequestParam String changeId, @RequestParam Integer status) {
        Project project = projectService.getChangeProject(changeId);
        Map<String, Object> map = MapObj.objectToMap(project);
        SystemUser user = getUser();
        List<DataDictionary> projectTypeList = new ArrayList<>();
        List<DataDictionary> languageList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        List<DataDictionary> feeTypeList = new ArrayList<>();
        try {
            feeTypeList = dataDictionaryService.getDicItemListNoPage("18");
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
            languageList = dataDictionaryService.getDicItemListNoPage("15");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                structList = organizeStructService.getProjectDepList();
            } else {
                OrganizeStruct curentStruct = organizeStructService.getOrganizeStructByPersonCode(user.getPersonCode());
                OrganizeStruct parent = organizeStructService.getParent(curentStruct);
                if (parent == null) {
                    //部门级别
                    structList = organizeStructService.getSon(curentStruct);
                } else {
                    String upCode = parent.getUpCode();
                    if (null == upCode || upCode.equals("")) {
                        //项目部级别
                        structList.add(curentStruct);
                    } else {
                        //项目部子级别
                        structList.add(parent);
                    }
                }
            }
        } catch (BusinessLevelException e) {
            log.error("跳转编辑变更页面" + e.getMessage());
        }
        map.put("projectTypeList", projectTypeList);
        map.put("structList", structList);
        map.put("languageList", languageList);
        map.put("feeTypeList", feeTypeList);
        map.put("status", status);
        return new ModelAndView("project/projectManagement/editProjectChangePage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 变更项目页面
     * @Date 2018/11/6 9:48
     * @Param [map]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/changeProjectPage", method = RequestMethod.GET)
    public ModelAndView changeProjectPage(@RequestParam String projectId) {
        Project project = projectService.getProject(projectId);
        Map<String, Object> map = MapObj.objectToMap(project);
        SystemUser user = getUser();
        List<DataDictionary> projectTypeList = new ArrayList<>();
        List<DataDictionary> languageList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        List<DataDictionary> feeTypeList = new ArrayList<>();
        try {
            feeTypeList = dataDictionaryService.getDicItemListNoPage("18");
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
            languageList = dataDictionaryService.getDicItemListNoPage("15");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                structList = organizeStructService.getProjectDepList();
            } else {
                OrganizeStruct curentStruct = organizeStructService.getOrganizeStructByPersonCode(user.getPersonCode());
                OrganizeStruct parent = organizeStructService.getParent(curentStruct);
                if (parent == null) {
                    //部门级别
                    structList = organizeStructService.getSon(curentStruct);
                } else {
                    String upCode = parent.getUpCode();
                    if (null == upCode || upCode.equals("")) {
                        //项目部级别
                        structList.add(curentStruct);
                    } else {
                        //项目部子级别
                        structList.add(parent);
                    }
                }
            }
        } catch (BusinessLevelException e) {
            log.error("跳转编辑项目页面失败" + e.getMessage());
        }
        map.put("projectTypeList", projectTypeList);
        map.put("structList", structList);
        map.put("languageList", languageList);
        map.put("feeTypeList", feeTypeList);
        return new ModelAndView("project/projectManagement/changeProjectPage", map);
    }

    @RequestMapping(value = "/commitPage", method = RequestMethod.GET)
    public ModelAndView commitPage(@RequestParam Map map) {
        List<OrganizePeople> organizePeopleList = new ArrayList<>();
        try {
            map.put("roleCode","11");
            organizePeopleList = organizePeopleService.getOrganizePeopleHaveAuditRole(map);
        } catch (BusinessLevelException e) {
            log.error("获取部门人员失败" + e.getMessage());
        }
        map.put("personList", organizePeopleList);
        return new ModelAndView("project/projectManagement/commitPage", map);
    }

    @RequestMapping(value = "/choseTemplate", method = RequestMethod.GET)
    public ModelAndView choseTemplate(@RequestParam Map map) {
        List<Map<String, Object>> templateList = new ArrayList<>();
        try {
            templateList = studentService.getTempListNoPage();
        } catch (BusinessLevelException e) {
            log.error("获取成绩模板失败" + e.getMessage());
        }
        map.put("templateList", templateList);
        return new ModelAndView("project/projectManagement/choseTemplate", map);
    }

    /**
     * @Author MaZhuli
     * @Description 项目提交审核
     * @Date 2018/11/9 15:40
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/commitProject", method = RequestMethod.POST)
    @ResponseBody
    public OpResult commitProject(@RequestBody Map map) {
        List<ProjectApproveOption> list = new ArrayList<>();
        String idList = "";
        String[] split = null;
        if (null != map.get("projectIdList") && !map.get("projectIdList").toString().equals("")) {
            idList = map.get("projectIdList").toString();
            split = idList.split(",");
        } else {
            idList = map.get("changeIdList").toString();
            split = idList.split(",");
        }
        if (null != map.get("personCode1") && !map.get("personCode1").toString().equals("")) {
            for (String id : split) {
                ProjectApproveOption projectApproveOption = new ProjectApproveOption();
                projectApproveOption.setRowId(Constants.GUID());
                if (null != map.get("projectIdList") && !map.get("projectIdList").toString().equals("")) {
                    projectApproveOption.setProjectId(id);
                    projectApproveOption.setSubmitType(1);
                } else {
                    projectApproveOption.setProjectId(null);
                    projectApproveOption.setChangeId(id);
                    projectApproveOption.setSubmitType(2);
                }
                projectApproveOption.setPersonCode(map.get("personCode1").toString());
                projectApproveOption.setApproveOrder(1);
                list.add(projectApproveOption);
            }
        }
        if (null != map.get("personCode2") && !map.get("personCode2").toString().equals("")) {
            for (String id : split) {
                ProjectApproveOption projectApproveOption = new ProjectApproveOption();
                projectApproveOption.setRowId(Constants.GUID());
                if (null != map.get("projectIdList") && !map.get("projectIdList").toString().equals("")) {
                    projectApproveOption.setProjectId(id);
                    projectApproveOption.setSubmitType(1);
                } else {
                    projectApproveOption.setProjectId(null);
                    projectApproveOption.setChangeId(id);
                    projectApproveOption.setSubmitType(2);
                }
                projectApproveOption.setProjectId(id);
                projectApproveOption.setPersonCode(map.get("personCode2").toString());
                projectApproveOption.setApproveOrder(2);
                list.add(projectApproveOption);
            }
        }
        if (null != map.get("personCode3") && !map.get("personCode3").toString().equals("")) {
            for (String id : split) {
                ProjectApproveOption projectApproveOption = new ProjectApproveOption();
                projectApproveOption.setRowId(Constants.GUID());
                if (null != map.get("projectIdList") && !map.get("projectIdList").toString().equals("")) {
                    projectApproveOption.setProjectId(id);
                    projectApproveOption.setSubmitType(1);
                } else {
                    projectApproveOption.setProjectId(null);
                    projectApproveOption.setChangeId(id);
                    projectApproveOption.setSubmitType(2);
                }
                projectApproveOption.setProjectId(id);
                projectApproveOption.setPersonCode(map.get("personCode3").toString());
                projectApproveOption.setApproveOrder(3);
                list.add(projectApproveOption);
            }
        }
        Map param = new HashMap();
        param.put("list", Arrays.asList(split));
        param.put("submitter", getUserCode());
        param.put("submitTime", DateUtil.ToDateTimeString());
        param.put("approvaler", map.get("personCode1").toString());
        try {
            return projectService.commitProject(list, param);
        } catch (BusinessLevelException e) {
            log.error("项目提交审核失败:" + e.getMessage());
            return new OpResult("项目提交审核失败");
        }
    }


    /**
     * @Author MaZhuli
     * @Description 跳转项目变更页面
     * @Date 2018/11/7 18:07
     * @Param [list]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/changePage", method = RequestMethod.GET)
    public ModelAndView changePage(@RequestParam String projectId) {
        Project project = new Project();
        project.setProjectId(projectId);
        Map<String, Object> map = MapObj.objectToMap(project);
        List<DataDictionary> projectTypeList = new ArrayList<>();
        try {
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
        } catch (BusinessLevelException e) {
            log.error("跳转项目变更页面失败" + e.getMessage());
        }
        map.put("projectTypeList", projectTypeList);
        return new ModelAndView("project/projectManagement/change", map);
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目变更列表
     * @Date 2018/11/7 18:33
     * @Param [map]
     **/
    @RequestMapping(value = "/getChangeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getChangeList(@RequestParam Map map) {
        try {
            return projectService.getChangeList(map);
        } catch (BusinessLevelException e) {
            log.error("获取项目变更列表:" + e.getMessage());
            return null;
        }
    }

    /**
     * @Author MaZhuli
     * @Description 跳转项目附件页面
     * @Date 2018/11/7 18:29
     * @Param [project]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/accessoryPage", method = RequestMethod.GET)
    public ModelAndView accessoryPage(@RequestParam String projectId) {
        Project project = new Project();
        project.setProjectId(projectId);
        Map<String, Object> map = MapObj.objectToMap(project);
        return new ModelAndView("project/projectManagement/accessoryList", map);
    }

    /**
     * @Author MaZhuli
     * @Description 获取项目附件列表
     * @Date 2018/11/7 18:47
     * @Param [map]
     **/
    @RequestMapping(value = "/getAccessoryList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAccessoryList(@RequestParam Map map) {
        try {
            return projectService.getAccessoryList(map);
        } catch (BusinessLevelException e) {
            log.error("获取项目附件列表:" + e.getMessage());
            return null;
        }
    }

    /**
     * @Author MaZhuli
     * @Description 跳转项目变更页面
     * @Date 2018/11/12 9:12
     * @Param [param]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/auditOptionPage", method = RequestMethod.GET)
    public ModelAndView auditOptionPage(@RequestParam Map param) {
        Project project = new Project();
        if (null != param.get("projectId") && !param.get("projectId").equals("")) {
            project.setProjectId(param.get("projectId").toString());
        }
        if (null != param.get("changeId") && !param.get("changeId").equals("")) {
            project.setChangeId(param.get("changeId").toString());
        }
        Map<String, Object> map = MapObj.objectToMap(project);
        List<DataDictionary> projectTypeList = new ArrayList<>();
        try {
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
        } catch (BusinessLevelException e) {
            log.error("跳转项目变更页面失败" + e.getMessage());
        }
        map.put("projectTypeList", projectTypeList);
        return new ModelAndView("project/projectManagement/auditOpinionList", map);
    }

    /**
     * @Author MaZhuli
     * @Description 获取审批意见列表
     * @Date 2018/11/7 18:47
     * @Param [map]
     * @Return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    @RequestMapping(value = "/getAuditOpionList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAuditOpionList(@RequestParam Map map) {
        if (Integer.parseInt(map.get("type").toString()) == 1) {
            map.put("projectId", map.get("id").toString());
        } else {
            map.put("changeId", map.get("id").toString());
        }
        try {
            return projectService.getAuditOpionList(map);
        } catch (BusinessLevelException e) {
            log.error("获取审批意见列表:" + e.getMessage());
            return null;
        }
    }

    /**
     * @Author MaZhuli
     * @Description 保存项目
     * @Date 2018/11/6 9:48
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addProject(@RequestBody ProjectAndFile projectAndFile) {
        try {
            log.debug(projectAndFile);
            projectAndFile.setCreater(getUserCode());
            return projectService.addProject(projectAndFile);
        } catch (BusinessLevelException ex) {
            log.error("保存项目失败" + ex.getMessage());
            return new OpResult("保存项目失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 校验项目编码是否存在
     * @Date 2018/12/10 17:53
     * @Param [map]
     * @Return java.util.Map
     **/
    @RequestMapping(value = "/isProjectCodeExist", method = RequestMethod.POST)
    @ResponseBody
    public Map isProjectCodeExist(@RequestBody Map map) {
        try {
            log.debug(map);
            Project project = new Project();
            project.setProjectCode(map.get("projectCode").toString());
            if (map.get("projectId") != null && !map.get("projectId").toString().equals("")) {
                project.setProjectId(map.get("projectId").toString());
            }
            boolean isProjectCodeExist = projectService.checkProjectCode(project);
            map.put("isProjectCodeExist", isProjectCodeExist);
            return map;
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            map.put("isProjectCodeExist", false);
            return map;
        }
    }

    /**
     * @Author MaZhuli
     * @Description 查询当前项目是否可变更
     * @Date 2018/11/12 11:15
     * @Param [project]
     * @Return boolean
     **/
    @RequestMapping(value = "/getChangeInAudit", method = RequestMethod.POST)
    @ResponseBody
    public boolean getChangeInAudit(@RequestBody Project project) {
        try {
            log.debug(project);
            return projectService.getChangeInAudit(project);
        } catch (BusinessLevelException ex) {
            log.error("查询当前项目是否可变更失败" + ex.getMessage());
            return false;
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除项目
     * @Date 2018/11/6 9:49
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/delProject", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delProject(@RequestBody Project project) {
        try {
            log.debug(project);
            return projectService.delProject(project);
        } catch (BusinessLevelException ex) {
            log.error("删除项目失败" + ex.getMessage());
            return new OpResult("删除项目失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除附件
     * @Date 2018/11/6 9:49
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/delAccessory", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delAccessory(@RequestBody ProjectAddFile projectAddFile) {
        try {
            log.debug(projectAddFile);
            return projectService.delAccessory(projectAddFile);
        } catch (BusinessLevelException ex) {
            log.error("删除附件失败" + ex.getMessage());
            return new OpResult("删除附件失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 添加附件
     * @Date 2018/11/6 9:49
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/addAccessory", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addAccessory(@RequestBody ProjectAddFile projectAddFile) {
        projectAddFile.setUploader(getUserCode());
        projectAddFile.setRowId(Constants.GUID());
        projectAddFile.setUploadTime(DateUtil.ToDateTimeString());
        try {
            return projectService.addAccessory(projectAddFile);
        } catch (BusinessLevelException ex) {
            log.error("添加附件失败" + ex.getMessage());
            return new OpResult("添加附件失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 编辑项目
     * @Date 2018/11/6 9:49
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updProject", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updProject(@RequestBody Project project) {
        try {
            log.debug(project);
            return projectService.updProject(project);
        } catch (BusinessLevelException ex) {
            log.error("编辑项目失败" + ex.getMessage());
            return new OpResult("编辑项目失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 变更项目
     * @Date 2018/11/6 9:49
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/changeProject", method = RequestMethod.POST)
    @ResponseBody
    public OpResult changeProject(@RequestBody Project project) {
        try {
            log.debug(project);
            project.setChangeId(Constants.GUID());
            project.setStatus(1);
            project.setCreater(getUserCode());
            project.setCreateTime(DateUtil.ToDateTimeString());
            return projectService.changeProject(project);
        } catch (BusinessLevelException ex) {
            log.error("变更项目失败" + ex.getMessage());
            return new OpResult("变更项目失败");
        }
    }

    @RequestMapping(value = "/getCommitProjectList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMessageList(@RequestParam Map map) {
        try {
            map.put("personCode", getUser().getPersonCode());
            return projectService.getCommitProjectList(map);
        } catch (BusinessLevelException e) {
            log.error("获取消息列表失败:" + e.getMessage());
            return null;
        }
    }

    /**
     * @Author SongZiXian
     * @Description 获取二期同一项目编号项目数据
     * @Date 2019/3/20 0020 下午 17:34
     * @Param [map]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value = "/getByBmProjectInfo")
    @ResponseBody
    public Map<String, Object> getByBmProjectInfo(@RequestParam Map<String,Object> map){
        Map<String, Object> byBmProjectInfo = projectService.getByBmProjectInfo(map);
        return byBmProjectInfo;
    }

    public OpResult judgeStudent(Student student) {
        if (student == null || student.getName() == null || student.getName().equals("")) {
            return new OpResult("用户名不能为空");
        } else if (student.getSerial() == null || student.getSerial().equals("")) {
            return new OpResult("学号不能为空");
        } else if (student.getSex() == null || student.getSex().equals("")) {
            return new OpResult("性别获取失败");
        } else if (student.getIdcard() == null || student.getIdcard().equals("")) {
            return new OpResult("身份证号错误");
        } else if (student.getTelephone() == null || student.getTelephone().equals("")) {
            return new OpResult("手机号不能为空");
        } else if (student.getProjectIds() == null || student.getProjectIds().equals("")) {
            return new OpResult("获取项目信息失败");
        }
        return new OpResult();
    }

    public OpResult judgeProject(Project project) {
        if (project == null || project.getProjectName() == null || project.getProjectName().isEmpty()) {
            return new OpResult("项目名字出错了");
        } else if (project.getLanguage() == null || project.getLanguage().isEmpty()) {
            return new OpResult("语种出错了");
        } else if (project.getProjectCode() == null || project.getProjectCode().isEmpty()) {
            return new OpResult("项目编码出错了");
        } else if (project.getDepName() == null || project.getDepName().isEmpty()) {
            return new OpResult("项目部出错了");
        } else if (project.getStartTime() == null || project.getStartTime().isEmpty()) {
            return new OpResult("项目开始时间出错了");
        } else if (project.getEndTime() == null || project.getEndTime().isEmpty()) {
            return new OpResult("项目结束时间出错了");
        } else if (project.getProjectType() == null || project.getProjectType().isEmpty()) {
            return new OpResult("项目类型出错了");
        } else if (project.getCreater() == null || project.getCreater().isEmpty()) {
            return new OpResult("项目创建人出错了");
        } else if (project.getCountHour() <= 0) {
            return new OpResult("课时总数出错了");
        } else if (project.getFeeType() == null || project.getFeeType().isEmpty()) {
            return new OpResult("收费方式出错了");
        } else {
            return new OpResult();
        }
    }

}
