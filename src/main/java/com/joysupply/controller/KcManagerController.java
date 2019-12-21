package com.joysupply.controller;

import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.*;
import com.joysupply.util.Constants;
import com.joysupply.util.ExcelUtil_JXL;
import com.joysupply.util.OpResult;

import com.joysupply.util.ResultData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/kcManager")
public class KcManagerController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private KcManagerService kcManagerService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private OrganizeStructService organizeStructService;
    @Autowired
    private OrganizePeopleService organizePeopleService;
    @Autowired
    private ClassManageService classManageService;
    @Autowired
    private AuthorityManageService authorityManageService;

    //返回课酬统计页面
    @RequestMapping(value = "/kcStatistics")
    public ModelAndView kcStatistics(@RequestParam Map<String, Object> param) {
		Map<String, Object> resultMap = new HashMap<>();
		if(param.containsKey("serialNumber")){
			resultMap.put("serialNumber", param.get("serialNumber"));
			String timeSolt = param.get("timeSolt").toString();
			String startTime = timeSolt.split("-")[0];
			String endTime = timeSolt.split("-")[1];
			resultMap.put("startTime", startTime.replace("/", "-"));
			resultMap.put("endTime", endTime.replace("/", "-"));
			resultMap.put("flag", "1");
		}
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
       /* model.addAllAttributes(resultMap);*/
        return new ModelAndView("kcManagement/kcStatistics",resultMap);
    }

    //课酬统计 查询课酬信息
    @RequestMapping(value = "/queryKcInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryKcInfo(@RequestParam Map<String, Object> params) {
        params.put("personCode",getUserCode());
        List<Map<String, Object>> data = kcManagerService.queryKcInfo(params);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", data);
        map.put("count", data.size());
        return map;
    }

    //保存课酬信息
    @RequestMapping(value = "/saveKcList", method = RequestMethod.POST)
    @ResponseBody
    public String saveKcList(@RequestParam Map<String, Object> params) {
        params.put("personCode",getUserCode());
        return this.kcManagerService.saveKcList(params);
    }

    //检查保存时段
    @RequestMapping("/checkSave")
    @ResponseBody
    public OpResult checkSave(@RequestParam Map<String, Object> params) {
        params.put("personCode",getUserCode());
        return this.kcManagerService.checkSave(params);
    }

    //查询上次课酬是否为工作量扣除
    @RequestMapping(value = "/queryLastProject", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryLastProject(@RequestParam Map<String, String> params) {
        return this.kcManagerService.queryLastProject(params);
    }

    //查询其他项目
    @RequestMapping(value = "/queryOtherProject", method = RequestMethod.GET)
    @ResponseBody
    public List<DataDictionary> queryOtherProject() {
        String parentCode = Constants.COURSE_FEE_TYPE;
        return this.dataDictionaryService.getDicItemListNoPage(parentCode);
    }

    //课酬提交页面
    @RequestMapping(value = "/commitPage", method = RequestMethod.GET)
    public ModelAndView commitPage(@RequestParam Map<String, Object> map) {
        List<OrganizePeople> organizePeopleList = new ArrayList<>();
        try {
            map.put("roleCode","10");
            List<OrganizeStruct> organizeStructList = new ArrayList<>();
            OrganizeStruct organizeStruct = organizeStructService.getOrganizeStructByPersonCode(getUserCode());
            organizeStructList.add(organizeStruct);
            map.put("organizeStructList",organizeStructList);
            organizePeopleList = organizePeopleService.getOrganizePeopleHaveAuditRole(map);
        } catch (BusinessLevelException e) {
            log.error("获取部门人员失败" + e.getMessage());
        }
        map.put("personList", organizePeopleList);
        return new ModelAndView("kcManagement/commitPage", map);
    }

    //保存提交
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ResponseBody
    public OpResult commit(@RequestBody Map<String, Object> map) {
        map.put("personCode",getUserCode());
        return this.kcManagerService.commit(map);
    }

    //返回课酬查询页面
    @RequestMapping(value = "/queryKcPage")
    public String queryKcPage(Model model) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        model.addAllAttributes(resultMap);
        return "kcManagement/kcInfo";
    }

    //项目部课酬查询数据
    @RequestMapping(value = "/projectKcQuery", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> projectKcQuery(@RequestParam Map<String, Object> params) {
        //wangyuelei增加权限
        Map<String,Object> param = new HashMap<>();
        param.put("personCode",getUserCode());
        List<Map<String,Object>> myAuth = authorityManageService.getPersonProjectAuth(param);
        if(myAuth.size() <= 0) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("count", 0);
            map.put("data", new ArrayList<>());
            map.put("code", 0);
            map.put("msg", "");
            return map;
        }
        params.put("myAuth",myAuth);
        // 查询项目对应的流水号
        List<String> serialNumbers = kcManagerService.getSerialNumbers(params);
        params.put("serialNumbers",serialNumbers);
        Map aaa = this.kcManagerService.projectKcQuery(params);
        return aaa;
    }
    
    /**
     * 统计后课酬明细页面
     * @param param
     * @return
     */
    @RequestMapping(value="/showKCDetail")
    public ModelAndView showKCDetail(@RequestParam Map<String, Object> param){
    	Map<String, Object> map = new HashMap<String, Object>();
    	String time = param.get("time").toString();
    	String startTime = time.split("-")[0].replace("/", "-");
    	String endTime = time.split("-")[1].replace("/", "-");
    	map.put("startTime", startTime);
    	map.put("endTime", endTime);
    	map.put("teacherCode", param.get("teacherCode"));
    	map.put("courseId", param.get("courseId"));
    	map.put("projectId", param.get("projectId"));
    	return new ModelAndView("kcManagement/detail",map);
    }
    
    //统计页面课酬明细数据
    @RequestMapping(value="/getQueryKCDetail")
    @ResponseBody
    public Map<String, Object> getQueryKCDetail(@RequestParam Map<String, Object> param){
    	Map<String, Object> result = new HashMap<String, Object>();
    	List<Map<String, Object>> list = kcManagerService.getQueryKCDetail(param);
    	result.put("data",list);
    	result.put("code", 0);
    	result.put("msg", "");
    	return result;
    }

    //返回课酬明细页面
    @RequestMapping(value = "/showDetails")
    public String showDetails(Model model, String serialNumber, String status) {
        Map<String, Object> menuMap = getMenuMap();
        menuMap.put("user", getUser());
        menuMap.put("serialNumber", serialNumber);
        menuMap.put("status", status);
        model.addAllAttributes(menuMap);
        return "kcApprove/details";
    }

    //查询课酬明细数据
    @RequestMapping(value = "/queryKcDetatils", method = RequestMethod.GET)
    @ResponseBody
    public List queryKcDetatils(@RequestParam Map<String, String> params) {
        return this.kcManagerService.queryKcDetatils(params);
    }

    private String getUserCode() {
        SystemUser user = getUser();
        return user.getPersonCode();
    }

    //查询未发已发教师课酬明细
    @RequestMapping(value = "/kcDetailed")
    public ModelAndView kcDetailed(@RequestParam Map<String, Object> param) {
        param.putAll(getMenuMap());
        SystemUser user = getUser();
        param.put("user", user);
        return new ModelAndView("kcManagement/kcDetailed", param);
    }

    //查询未发已发教师课酬明细
    @RequestMapping(value = "/getKCDetail")
    @ResponseBody
    public Map<String, Object> getKCDetail(@RequestParam Map<String, Object> param) {
        List<String> projectIds = Arrays.asList(param.get("projectIds").toString().split(","));
        List<String> serialNumbers = Arrays.asList(param.get("serialNumber").toString().split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        param.put("personCode",getUserCode());
        param.put("projectIds",projectIds);
        param.put("serialNumbers",serialNumbers);
        List<Map<String, Object>> list = kcManagerService.getKCDetail(param);
        result.put("data", list);
        return result;
    }

    //导出课酬明细
    @RequestMapping(value = "/exportKCInfo")
    @ResponseBody
    public void exportKCInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parMap = new HashMap<String, Object>();
        parMap.put("serialNumber", request.getParameter("serialNumber"));
        parMap.put("teacherCode", request.getParameter("teacherCode"));
        parMap.put("mergeSerialNumber", request.getParameter("mergeSerialNumber"));
        parMap.put("projectIds", request.getParameter("projectIds"));
        Map<String, Object> kcDetail = getKCDetail(parMap);
        List<Map<String, Object>> list = (List<Map<String,Object>>) kcDetail.get("data");
        String name = "教师课酬明细";
        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileName = time + "_" + name + "" + ".xls";
        String workbookName = "课酬明细";
        Map<String, Object> exportMap = new LinkedHashMap<String, Object>();
        exportMap.put("序号", "px");
        exportMap.put("年份", "year");
        exportMap.put("月份", "month");
        exportMap.put("项目部名称", "depName");
        exportMap.put("项目名称", "projectName");
        exportMap.put("课程名称", "courseName");
        exportMap.put("基础课酬", "baseFee");
        exportMap.put("课时", "hourActual");
        exportMap.put("人数系数", "personFactor");
        exportMap.put("课程系数", "courseFactor");
        exportMap.put("课酬", "feeCourse");
        ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response,request);
    }

    //根据流水号删除课酬数据
    @RequestMapping(value = "/deleteCourseFeeBySerialNum")
    @ResponseBody
    public OpResult deleteCourseFeeBySerialNum(@RequestParam Map<String, Object> map) {
        try {
            kcManagerService.deleteCourseFeeBySerialNum(map);
            return new OpResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("删除失败!");
        }

    }
    /**
     * @Author WangYuelei
     * @Description 上课信息查寻
     * @Date 2019/1/25 17:05
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/classinformation" , method = RequestMethod.GET)
    public ModelAndView classinformation(){
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("scheduleView/classInformation", resultMap);
    }
    /**
     * @Author WangYuelei
     * @Description 上课信息项目数据
     * @Date 2019/1/25 18:38
     * @Param [param]
     * @Return com.joysupply.util.ResultData<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     **/
    @RequestMapping(value = "/classInformationPorjectData" , method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String,Object>>> classInformationPorjectData(@RequestParam Map<String,Object> param){
        return kcManagerService.classInformationPorjectData(param);
    }

    /**
     * @Author WangYuelei
     * @Description 上课信息教师数据
     * @Date 2019/1/25 18:38
     * @Param [param]
     * @Return com.joysupply.util.ResultData<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     **/
    @RequestMapping(value = "/classInformationTeacherData" , method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String,Object>>> classInformationTeacherData(@RequestParam Map<String,Object> param){
        return kcManagerService.classInformationTeacherData(param);    
    }
    @RequestMapping(value = "/outputInformation" , method = RequestMethod.GET)
    @ResponseBody
    public void outputInformation(HttpServletResponse response, HttpServletRequest request , Map<String,Object> param){
        String fileName = "上课信息查询.xls";
        
        String workbookName1 = "项目";
        String workbookName2 = "老师";
        
        Map<String, Object> exportMap1 = new HashMap<>();
        Map<String, Object> exportMap2 = new HashMap<>();
        
        String[] titles = {"项目编码", "项目名称", "项目部名称", "项目类型", "项目语种","当前人数",
                "总课时","总收益","已发课酬课时","已发课酬","启动时间","结束时间"};
        String[] titles_en = {"projectCode", "projectName", "depName", "projectType","language","currentCount",
                "countHour","totalIncome","payHour","payed","startTime","endTime"};
        
        for (int i = 0; i < titles.length; i++) {
            exportMap1.put(titles[i],titles_en[i]);
        }
        
        String[] titles2 = {"项目编码", "项目名称", "项目部名称", "教师类别",
                "教师姓名","性别","国籍","身份证号","联系电话"};
        String[] titles_en2 = {"projectCode", "projectName", "depName",
                "teacherType","teacherName","sex","nationality","idCard","tel"};
        
        for (int i = 0; i < titles2.length; i++) {
            exportMap2.put(titles2[i],titles_en2[i]);
        }
        
        //获取数据
        ResultData<List<Map<String, Object>>> resultData1 = kcManagerService.classInformationPorjectData(param);
        List<Map<String,Object>> list1 = resultData1.getData();
        
        ResultData<List<Map<String, Object>>> resultData2 = kcManagerService.classInformationTeacherData(param);
        List<Map<String,Object>> list2 = resultData2.getData();
        
        for (Map<String,Object> map:list2) {
            if(map.get("sex") != null && !map.get("sex").toString().equals("")) {
                String sex = map.get("sex").toString();
                if("1".equals(sex)) {
                    map.put("sex","男");
                }else if ("2".equals(sex)) {
                    map.put("sex","女");
                }
            }
        }
        ExcelUtil_JXL.exportExcelTwoSheel(fileName,workbookName1,workbookName2,exportMap1,exportMap2,list1,list2,response);
    }

    /**
     * @Author SongZiXian
     * @Description 教研室课酬页面
     * @Date 2019/2/23 0023 下午 16:23
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/researchOfficeKc")
    public ModelAndView researchOfficeKc(){
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        Map map = new HashMap();
        map.put("parentCode","13");
        Map<String, Object> dicItemList = dataDictionaryService.getDicItemList(map);
        resultMap.put("user", getUser());
        resultMap.put("teacherType", dicItemList.get("data"));
        return new ModelAndView("kcManagement/researchOfficeKc",resultMap);
    }
    
    /**
     * @Author SongZiXian
     * @Description 教研室课酬
     * @Date 2019/2/23 0023 下午 15:16
     * @Param 
     * @Return 
     **/
    @RequestMapping(value = "/getResearchKc")
    @ResponseBody
    public List getResearchKc(@RequestParam Map<String,Object> param){
        Map<String, Object> reqmap = new HashMap<>();
        reqmap.put("personCode",getUser().getPersonCode());
        List<Map<String, Object>> teacherList = authorityManageService.getPersonTeacherAuth(reqmap);
        param.put("teacherList",teacherList);
        List<List<Map<String,Object>>> list = kcManagerService.getResearchKc(param);

        return list;
    }
}
