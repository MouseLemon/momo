package com.joysupply.controller;

import com.joysupply.entity.ClassScore;
import com.joysupply.entity.Project;
import com.joysupply.entity.Student;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ProjectService;
import com.joysupply.service.RoleService;
import com.joysupply.service.StudentService;
import com.joysupply.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.BuilderException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员controller
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/student")
public class StudentController extends BaseController {
    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private  HttpSession session;

    private String getUserCode() {
        SystemUser user = getUser();
        return user.getPersonCode();
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public ModelAndView viewStudent() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("project/studentManagement/studentIndex", resultMap);
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.GET)
    public ModelAndView viewAddStudent() {
        Map map = new HashMap();
        List<Project> projectListNoPage = projectService.getProjectListNoPage(getUser());
        map.put("projects", projectListNoPage);
        return new ModelAndView("project/studentManagement/addStudent", map);
    }

    @RequestMapping(value = "/editStudent", method = RequestMethod.GET)
    public ModelAndView editStudent(@RequestParam String studentCode) {
        Map map = new HashMap();
        List<Project> projectListNoPage = projectService.getProjectListNoPage(getUser());
        ResultData proStuCode = studentService.queryProjectStudentsByStuCode(studentCode);
        ResultData<Map<String, Object>> resultData = studentService.selectStudentByStrCode(studentCode);
        map.put("projects", projectListNoPage);
        map.put("data", resultData.getData());
        map.put("proStr", proStuCode.getData() + "");
        //获取五分制列表
        List<Map<String, Object>> fivePintAll = studentService.queryFivePintALL();
        map.put("fivePintAll", fivePintAll);
        return new ModelAndView("project/studentManagement/editStudent", map);
    }

    /**
     * 添加学生信息
     *
     * @param student
     * @return
     */
    @RequestMapping(value = "/saveStudent", method = RequestMethod.POST)
    @ResponseBody
    public OpResult saveStudent(Student student) {
        OpResult opResult = judgeStudent(student);
        if (opResult.getResult().equals(OpEnum.FAILURE.getValue())) {
            return opResult;
        }
        try {
            log.debug(student);
            student.setStudentCode(Constants.GUID());
            student.setStatus("1");
            return studentService.saveStudent(student);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    @RequestMapping(value = "/queryUploadStudent", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<Map<String, Object>> queryUploadStudent(@RequestParam("file") MultipartFile file) {
        String fileName = "studentPic";
        try {
            return new ResultData<Map<String, Object>>(UpLoadFile.uploadFile(file, fileName));
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            return new ResultData<>(ex.getMessage());
        }
    }

    /**
     * 修改学生信息
     *
     * @param student
     * @return
     */
    @RequestMapping(value = "/updateStudent", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateStudent(Student student) {
        if (student.getStudentCode() == null || student.getStudentCode().equals("")) {
            return new OpResult("学生的编码错误");
        } else if (student.getStatus() == null || student.getStatus().equals("")) {
            return new OpResult("获取用户的状态失败");
        }
        try {
            log.debug(student);
            return studentService.updateStudent(student);
        } catch (BusinessLevelException ex) {
            log.error(ex.getMessage());
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 修改学生的状态
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateStudentStatus", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateStudentStatus(@RequestParam Map<String, Object> map) {
        if (map == null || map.get("status") == null || map.get("status").equals("")) {
            return new OpResult("获取学生的状态失败");
        } else if (map.get("studentCode") == null || map.get("studentCode").equals("")) {
            return new OpResult("获取学生的编码失败");
        }
        try {
            log.error(map);
            return studentService.updateStudentStatus(map);
        } catch (BusinessLevelException ex) {
            log.error(ex.getMessage());
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 删除学生通过学生编码
     */
    @RequestMapping(value = "/deleteStudentByCode", method = RequestMethod.POST)
    @ResponseBody
    public OpResult deleteStudentByCode(@RequestParam String studentCode) {
        if (studentCode == null || studentCode.equals("")) {
            return new OpResult("获取学生的编码失败");
        }
        try {
            log.debug(studentCode);
            return studentService.deleteStudentByCode(studentCode);
        } catch (BusinessLevelException ex) {
            log.error(ex.getMessage());
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 获取学生的信息通过学生的code
     *
     * @param studentCode
     * @return
     */
    @RequestMapping(value = "/queryStudentByStrCode", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<Map<String, Object>> queryStudentByStrCode(@RequestParam String studentCode) {
        if (studentCode == null || studentCode.equals("")) {
            return new ResultData<Map<String, Object>>("获取学生的编码失败");
        }
        try {
            log.debug(studentCode);
            return studentService.selectStudentByStrCode(studentCode);
        } catch (BuilderException ex) {
            log.error(ex.getMessage());
            return new ResultData<>(ex.getMessage());
        }
    }

    /**
     * 获取学生的信息分页
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryStudentByParam", method = RequestMethod.GET)
    @ResponseBody
    public Map queryStudentByParam(@RequestParam Map<String, Object> map) {
        try {
            log.debug(map);
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
            if(!personRoles.contains("18") && !personRoles.contains("1801") && !personRoles.contains("1802") && !personRoles.contains("19") &&!personRoles.contains("1901") && !personRoles.contains("1902")) {
                List<Project> projectList = projectService.getProjectListNoPage(getUser());
                map.put("projectList", projectList);
                map.put("isSystem",0);
            }else{
                map.put("isSystem",1);
            }
            return studentService.queryStudentByParam(map);
        }catch (BusinessLevelException ex){
            log.error("获取学生列表失败");
            return null;
        }
    }

    /**
     * 通过学生的code 获取该学生的项目列表
     *
     * @param studentCode
     * @return
     */
    @RequestMapping(value = "/queryProjectStudentsByStuCode", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryProjectStudentsByStuCode(@RequestParam String studentCode) {
        log.debug(studentCode);
        return studentService.queryProjectStudentsByStuCode(studentCode);
    }

    /**
     * 通过项目编码查询学生列表
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/queryProjectStudentByProCode", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Student>> queryProjectStudentByProCode(@RequestParam String projectId) {
        log.debug(projectId);
        return studentService.queryProjectStudentByProCode(projectId);
    }
    
    @RequestMapping(value = "/importStudent", method = RequestMethod.POST)
    @ResponseBody
    public OpResult importStudent(@RequestParam("file") MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //表头
        String[] titles = {"姓名", "学号", "英文名称", "性别","身份证号","项目编码","联系方式","状态"};
        String[] titles_en = {"*name", "*card", "ename", "sex", "idcard", "projectCode","tel", "status",};
        List<Map<String, Object>> msg = new ArrayList<>();
        try{
            Map<String, Object> readExcel = ExcelUtil_JXL.readExcel(multipartFile.getInputStream(), fileType, titles, titles_en);
            List<Map<String, Object>> list = (List<Map<String, Object>>) readExcel.get("list");
            OpResult opResult = studentService.addAllStudent(list,session);
            if ("OK".equals(opResult.getResult())) {
                return new OpResult();
            } else {
                return new OpResult(opResult.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new OpResult(e.getMessage());
        }
    }
    //学生模板
    @RequestMapping("/templateDown")
    @ResponseBody
    public void templateDown(HttpServletResponse response, HttpServletRequest request) {
        String fileName = "学员导入模板.xls";
        String workbookName = "学员信息";
        Map<String, Object> exportMap = new LinkedMap<>();
        String[] titles = {"姓名", "学号", "英文名称", "性别","身份证号","项目编码","联系方式","状态"};
        String[] titles_en = {"*name", "*card", "ename", "sex", "idcard", "projectCode","tel", "status",};
        List<Map<String, Object>> list =  new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        temp.put("*name","");
        temp.put("*card","");
        temp.put("ename","");
        temp.put("sex","男/女");
        temp.put("idcard","");
        temp.put("projectCode","(多个项目编码以,分割)");
        temp.put("tel","");
        temp.put("status","0/1 (0关闭,1开启(默认))");
        list.add(temp);
        for (int i = 0; i < titles.length; i++) {
            exportMap.put(titles[i],titles_en[i]);
        }
        String result = ExcelUtil_JXL.exportExcel(fileName,workbookName,exportMap,list,response,request);
    }

    /**
     * 添加学生成绩视图
     *
     * @return
     */
    @RequestMapping(value = "/addEditScore", method = RequestMethod.GET)
    public ModelAndView viewAddScore() {
        Map map = new HashMap();
        List<Project> projectListNoPage = projectService.getProjectListNoPage(getUser());
        map.put("projects", projectListNoPage);
        return new ModelAndView("project/studentManagement/addEditScore", map);
    }
        
    /**
     * 修改成绩视图
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/editScore", method = RequestMethod.GET)
    public ModelAndView editScore(@RequestParam Map<String, Object> map) {
        log.debug(map);
        ResultData<List<Map<String, Object>>> data = studentService.queryClassScoreByProCoscId(map);
        //获取五分制列表
        List<Map<String, Object>> fivePintAll = studentService.queryScoreListNoPage(
                new HashMap<String,Object>(){
                    {
                        put("parentCode",map.get("parentCode").toString());
                    }
                }
        ).getData();
        return new ModelAndView("project/studentManagement/editScore")
                .addObject("fivePintAll", fivePintAll)
                .addObject("data", data);
    }

    /**
     * 查询学生成绩视图
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/studentScores", method = RequestMethod.GET)
    public ModelAndView viewQueryScores() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        String personCode = user.getPersonCode();
        resultMap.put("user", user);
        List<String> courseNames = studentService.queryClassScoreCourseName(new HashMap<String, Object>() {
            {
                put("start", 0);
                put("pageSize", 10);
            }
        });
        //获取五分制列表
        List<Map<String, Object>> fivePintAll = studentService.queryFivePintALL();
        return new ModelAndView("project/studentManagement/scoreIndex", resultMap)
                .addObject("courseNames", courseNames)
                .addObject("fivePintAll", fivePintAll);
    }

    /**
     * 添加学生成绩
     *
     * @param classScore
     * @return
     */
    @RequestMapping(value = "/saveClassScore", method = RequestMethod.POST)
    @ResponseBody
    public OpResult saveClassScore(ClassScore classScore) {
        try {
            log.debug(classScore);
            return studentService.saveClassScore(classScore);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 修改学生的成绩
     *
     * @param classScore
     * @return
     */
    @RequestMapping(value = "/updateClassScore", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateClassScore(ClassScore classScore) {
        try {
            log.debug(classScore);
            return studentService.updateClassScore(classScore);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 通过项目id和学生id删除成绩
     *
     * @param classScore
     * @return
     */
    @RequestMapping(value = "/deleteClassScoreByProClaCode", method = RequestMethod.POST)
    @ResponseBody
    public OpResult deleteClassScoreByProClaCode(ClassScore classScore) {
        try {
            log.debug(classScore);
            studentService.deleteClassScoreByProClaCode(classScore);
            return new OpResult();
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 通过project获取所有的课程名字
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/queryClassScoreCourseNameByProjectId", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<String>> queryClassScoreCourseNameByProjectId(String projectId) {
        if (projectId == null) {
            return new ResultData<>("获取项目的编码失败");
        }
        try {
            log.debug(projectId);
            List<String> csList = studentService.queryClassScoreCourseNameByProjectId(projectId);
            return new ResultData<>(csList.size(), csList);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new ResultData<>(ex.getMessage());
        }
    }

    /**
     * 获取当页下的课程集合
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryPageScore", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<String>> queryPageScore(@RequestParam Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page").toString());
        int limit = Integer.parseInt(map.get("limit").toString());
        PageInfo pagei = new PageInfo(page, limit);
        map.put("start", pagei.getStart());
        map.put("pageSize", pagei.getPageSize());
        //增加权限
        List<Project> projectList = projectService.getProjectListNoPage(getUser());
        List<String> courseNames = null;
        if ( projectList.size() > 0 ){
            map.put("projectList",projectList);
            courseNames = studentService.queryClassScoreCourseName(map);
        }
        int count = 0;
        if (courseNames != null) {
            count = courseNames.size();
        }
        return new ResultData<>(count, courseNames);
    }

    /**
     * 获取成绩列表
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryClassScore", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryClassScore(@RequestParam Map<String, Object> map) {
        log.debug(map);
        //增加权限
        List<Project> projectList = projectService.getProjectListNoPage(getUser());
        ResultData<List<Map<String,Object>>>  data = new ResultData<List<Map<String,Object>>>();
        if ( projectList.size() > 0 ) {
            map.put("projectList", projectList);
            data = studentService.queryClassScore(map);
        }
        return data;
    }

    /**
     * 获取所有的课程名称以及对应的名字列表
     *
     * @return
     */
    @RequestMapping(value = "/returnClassScoreName", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> returnClassScoreName() {
        return studentService.returnClassScoreName();
    }

    public OpResult judgeStudent(Student student) {
        String serial = student.getSerial();
        boolean flg = studentService.selectStudentBySerial(serial);
        if (!flg) {
            return new OpResult();
        } else {
            return new OpResult("该学号已存在");
        }

    }

    /**
     * 设置五分制页面
     *
     * @return
     */
    @RequestMapping(value = "/fivePointSystemView", method = RequestMethod.GET)
    public ModelAndView fivePointSystemView() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", getUser());
        return new ModelAndView("project/studentManagement/fivePointSystem/fivePointList", resultMap);
    }

    /**
     * 添加五分制页面
     *
     * @return
     */
    @RequestMapping(value = "/addEditFivePointView", method = RequestMethod.GET)
    public ModelAndView addFivePointView(String fivePointCode) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        if (fivePointCode != null && !fivePointCode.equals("")) {
            resultMap.put("fivePoint", studentService.queryFivePintByCode(fivePointCode));
        }
        //获取五分制相应的内容
        resultMap.put("user", getUser());
        return new ModelAndView("project/studentManagement/fivePointSystem/addFivePoint", resultMap);
    }
    @RequestMapping(value = "/editScoreTemp", method = RequestMethod.GET)
    public ModelAndView editScoreTemp(String fivePointCode) {
        Map resultMap = new HashMap();
        resultMap.put("fivePointCode", fivePointCode);
        return new ModelAndView("project/studentManagement/fivePointSystem/scoreList", resultMap);
    }
    @RequestMapping(value = "/showScoreTemp", method = RequestMethod.GET)
    public ModelAndView showScoreTemp(String fivePointCode) {
        Map resultMap = new HashMap();
        resultMap.put("fivePointCode", fivePointCode);
        return new ModelAndView("project/studentManagement/fivePointSystem/showTempDetail", resultMap);
    }
    @RequestMapping(value = "/addScorePage", method = RequestMethod.GET)
    public ModelAndView addScorePage(String fivePointCode,String parentCode,String section,String pointName) {
        Map resultMap = new HashMap();
        resultMap.put("fivePointCode", fivePointCode);
        resultMap.put("parentCode", parentCode);
        resultMap.put("section", section);
        resultMap.put("pointName", pointName);
        return new ModelAndView("project/studentManagement/fivePointSystem/addScore", resultMap);
    }

    /**
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryFivePointList", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryFivePointList(@RequestParam Map<String, Object> param) {
        log.debug(param);
        return studentService.queryFivePint(param);
    }
    @RequestMapping(value = "/queryScoreList", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryScoreList(@RequestParam Map<String, Object> param) {
        log.debug(param);
        return studentService.queryScoreList(param);
    }

    @RequestMapping(value = "/queryScoreListNoPage", method = RequestMethod.GET)
    @ResponseBody
    public ResultData<List<Map<String, Object>>> queryScoreListNoPage(@RequestParam Map<String, Object> param) {
        log.debug(param);
        return studentService.queryScoreListNoPage(param);
    }

    /**
     * 修改保存五分制
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/insEditFivePointList", method = RequestMethod.POST)
    @ResponseBody
    public OpResult insEditFivePointList(@RequestParam Map<String, Object> param) {
        try {
            log.debug(param);
            return studentService.addEditFivePoint(param);
        } catch (BusinessLevelException e) {
            e.printStackTrace();
            return new OpResult(e.getMessage());
        }
    }

    /**
     * 通过五分制的编码删除数据
     *
     * @param fivePointCode
     * @return
     */
    @RequestMapping(value = "/delFivePointByCode", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delFivePointByCode(String fivePointCode) {
        try {
            log.debug(fivePointCode);
            return studentService.delFivePoint(fivePointCode);
        } catch (BusinessLevelException e) {
            e.printStackTrace();
            return new OpResult(e.getMessage());
        }
    }
    //导入成绩
    @RequestMapping(value = "/importStudentScore", method = RequestMethod.POST)
    @ResponseBody
    public OpResult importStudentScore(@RequestParam("file") MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        //表头
        String[] titles = {"姓名", "学号", "项目编码", "课程名称","分数","出勤率","分制名称"};
        String[] titles_en = {"*name", "*serial", "*projectCode", "*courseName", "score", "*attendRate","fiveName"};
        List<Map<String, Object>> msg = new ArrayList<>();
        try{
            Map<String, Object> readExcel = ExcelUtil_JXL.readExcel(multipartFile.getInputStream(), fileType, titles, titles_en);
            List<Map<String, Object>> list = (List<Map<String, Object>>) readExcel.get("list");
            OpResult opResult = new OpResult("数据为空");
            if(list.size()>0){
                opResult = studentService.addBatchStudentScore(list,session);
            }
            if ("OK".equals(opResult.getResult())) {
                return new OpResult();
            } else {
                return new OpResult(opResult.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new OpResult(e.getMessage());
        }
    }
    //成绩模板
    @RequestMapping("/scoreTemplateDown")
    @ResponseBody
    public void scoreTemplateDown(HttpServletResponse response, HttpServletRequest request) {
        String fileName = "成绩模板.xls";
        String workbookName = "成绩";
        Map<String, Object> exportMap = new LinkedMap<>();
        String[] titles = {"姓名", "学号", "项目编码", "课程名称","分数","出勤率","分制名称"};
        String[] titles_en = {"*name", "*serial", "*projectCode", "*courseName", "score", "*attendRate","fiveName"};
        List<Map<String, Object>> list =  new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        temp.put("*name","");
        temp.put("*serial","");
        temp.put("*projectCode","");
        temp.put("*courseName","");
        temp.put("score","(1-100)成绩和分制名称至少有一个填写，要是都填写会按照成绩自动匹配相应的分制");
        temp.put("*attendRate","(1-100)%");
        temp.put("fiveName","成绩和分制名称至少有一个填写，要是都填写会按照成绩自动匹配相应的分制");
        list.add(temp);
        for (int i = 0; i < titles.length; i++) {
            exportMap.put(titles[i],titles_en[i]);
        }
        ExcelUtil_JXL.exportExcel(fileName,workbookName,exportMap,list,response,request);
    }
}