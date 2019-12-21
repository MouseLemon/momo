package com.joysupply.controller;

import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.*;
import com.joysupply.util.DateUtil;
import com.joysupply.util.MapObj;
import com.joysupply.util.OpResult;
import com.joysupply.util.ResultData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批
 */
@Controller
@RequestMapping(value = "/projectApproveOption")
public class ProjectApproveOptionController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ProjectApproveOptionService projectApproveOptionService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private OrganizeStructService organizeStructService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RoleService roleService;

    /**
     * 跳转已审批页面
     *
     * @return
     */
    @RequestMapping(value = "/approveOption", method = RequestMethod.GET)
    public ModelAndView ApproveOptionView() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        List<DataDictionary> language = dataDictionaryService.getDicItemListNoPage("15");
        List<DataDictionary> projectType = dataDictionaryService.getDicItemListNoPage("16");
        resultMap.put("languages", language);
        resultMap.put("projectTypes", projectType);
        return new ModelAndView("project/projectManagement/haveBeenApproved/approvalIndex", resultMap);
    }

    /**
     * 获取已审批列表数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/beenApproveData", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryApprovalOptionData(@RequestParam Map<String, Object> map) {
        try {
            log.debug(map);
            if (getUser().getPersonCode() == null || getUser().getPersonCode().isEmpty()) {
                return null;
            }
            List<String> personRoles = roleService.getPersonRoles(getUser().getPersonCode());
            if (!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") && !personRoles.contains("1902")) {
                map.put("personCode", getUser().getPersonCode());
            }
            ResultData data = projectApproveOptionService.afterApproved(map);
            return data;
        } catch (BusinessLevelException ex) {
            log.error("已审批列表数据" + ex.getMessage());
            return new ResultData<>(ex.getMessage());
        }
    }

    /**
     * 审批列表视图
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/opinionList", method = RequestMethod.GET)
    public ModelAndView optionListView(@RequestParam Map<String, Object> map) {
       /* List<DataDictionary> projectType = dataDictionaryService.getDicItemListNoPage("16");
        map.put("projectTypes", projectType);*/
        return new ModelAndView("project/projectManagement/haveBeenApproved/opinionList", map);
    }

    /**
     * 审批列表数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/opioninData", method = RequestMethod.GET)
    @ResponseBody
    public ResultData opioninData(@RequestParam Map<String, Object> map) {
        try {
            log.debug(map);
            ResultData data = projectApproveOptionService.queryOpioninList(map);
            return data;
        } catch (BusinessLevelException ex) {
            log.error(ex.getMessage());
            return new ResultData(ex.getMessage());
        }
    }

    /**
     * 跳转待审批页面waitOption
     *
     * @return
     */
    @RequestMapping(value = "/waitOption", method = RequestMethod.GET)
    public ModelAndView waitOptionView() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        List<DataDictionary> language = dataDictionaryService.getDicItemListNoPage("15");
        List<DataDictionary> projectType = dataDictionaryService.getDicItemListNoPage("16");
        resultMap.put("languages", language);
        resultMap.put("projectTypes", projectType);
        return new ModelAndView("project/projectManagement/waitForApproval/approvalIndex", resultMap);
    }

    /**
     * 待审批列表数据
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/waitOpioninData", method = RequestMethod.GET)
    @ResponseBody
    public ResultData waitOpioninData(@RequestParam Map<String, Object> map) {
        try {
            log.debug(map);
            String personCode = getUser().getPersonCode();
            List<String> personRoles = roleService.getPersonRoles(personCode);
            if (!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") && !personRoles.contains("1902")) {
                map.put("personCode", personCode);
            }
            ResultData data = projectApproveOptionService.preApproved(map);
            return data;
        } catch (BusinessLevelException ex) {
            log.error("已审批列表数据" + ex.getMessage());
            return new ResultData<>(ex.getMessage());
        }
    }

    /**
     * 显示审批项目的内容
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/displayProject", method = RequestMethod.GET)
    public ModelAndView editOrganizeStructPage(@RequestParam String projectId, Integer status) {
        Project project = null;
        if (status == 2) {
            project = projectService.getChangeProject(projectId);
        } else {
            project = projectService.getProject(projectId);
        }
        Map<String, Object> map = MapObj.objectToMap(project);
        SystemUser user = getUser();
        List<DataDictionary> projectTypeList = null;
        List<DataDictionary> languageList = null;
        List<OrganizeStruct> structList = null;
        List<DataDictionary> feeTypeList = null;
        try {
            feeTypeList = dataDictionaryService.getDicItemListNoPage("18");
            projectTypeList = dataDictionaryService.getDicItemListNoPage("16");
            languageList = dataDictionaryService.getDicItemListNoPage("15");
            structList = organizeStructService.getProjectDepList();
        } catch (BusinessLevelException e) {
            log.error("跳转编辑项目页面失败" + e.getMessage());
        }
        map.put("projectTypeList", projectTypeList);
        map.put("structList", structList);
        map.put("languageList", languageList);
        map.put("feeTypeList", feeTypeList);
        return new ModelAndView("project/projectManagement/displayProjectPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 跳转审批页面
     * @Date 2018/11/12 16:25
     * @Param [rowIdList]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping("/auditPage")
    public ModelAndView auditPage(@RequestParam Map map) {
        return new ModelAndView("project/projectManagement/waitForApproval/auditPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 项目/变更审批
     * @Date 2018/11/12 16:31
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping("/auditProject")
    @ResponseBody
    public OpResult auditProject(@RequestBody Map map) {
        try {
            boolean isApproved = false;
            if (map.get("approveResult").toString().equals("1")) {
                isApproved = true;
            }
            List<ProjectApproveOption> projectList = new ArrayList<>();
            List<ProjectApproveOption> changeList = new ArrayList<>();
            String projectIdList = map.get("projectIdList").toString();
            String changeIdList = map.get("changeIdList").toString();
            String[] projectArr;
            String[] changeArr;
            List<String> personRoles = roleService.getPersonRoles(getUser().getPersonCode());
            if (!changeIdList.equals("")) {
                changeArr = changeIdList.split(",");
                for (String changeId : changeArr) {
                    ProjectApproveOption projectApproveOption = new ProjectApproveOption();
                    projectApproveOption.setChangeId(changeId);
                    if (!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") && !personRoles.contains("1902")) {
                        projectApproveOption.setPersonCode(getUser().getPersonCode());
                    }
                    projectApproveOption.setApproveResult(map.get("approveResult").toString());
                    projectApproveOption.setApproveOption(map.get("approveOption").toString());
                    projectApproveOption.setSubmitType(2);
                    projectApproveOption.setApproveDateTime(DateUtil.ToDateTimeString());
                    changeList.add(projectApproveOption);
                }
            }
            if (!projectIdList.equals("")) {
                projectArr = projectIdList.split(",");
                for (String projectId : projectArr) {
                    ProjectApproveOption projectApproveOption = new ProjectApproveOption();
                    projectApproveOption.setProjectId(projectId);
                    if (!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") && !personRoles.contains("1901") && !personRoles.contains("1902")) {
                        projectApproveOption.setPersonCode(getUser().getPersonCode());
                    }
                    projectApproveOption.setApproveResult(map.get("approveResult").toString());
                    projectApproveOption.setApproveOption(map.get("approveOption").toString());
                    projectApproveOption.setSubmitType(1);
                    projectApproveOption.setApproveDateTime(DateUtil.ToDateTimeString());
                    projectList.add(projectApproveOption);
                }
            }
            log.debug(map);
            return projectApproveOptionService.auditProject(projectList, changeList, isApproved);
        } catch (BusinessLevelException ex) {
            log.error("项目/变更审批失败" + ex.getMessage());
            return new OpResult("项目/变更审批失败");
        }
    }
}
