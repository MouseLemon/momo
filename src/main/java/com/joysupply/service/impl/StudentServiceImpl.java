package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.joysupply.dao.ProjectDao;
import com.joysupply.dao.StudentDao;
import com.joysupply.entity.ClassScore;
import com.joysupply.entity.Project;
import com.joysupply.entity.Student;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.StudentService;
import com.joysupply.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private ProjectDao projectDao;

    private Log log = LogFactory.getLog(getClass());

    //学员业务
    @Override
    @Transactional
    public OpResult saveStudent(Student student) throws BusinessLevelException {
        if (student == null) {
            return new OpResult("学生信息错误");
        }
        try {
            //身份证号
            List<String> idcardList = studentDao.getStudentIdcardList();
            //手机号
            List<String> phoneList = studentDao.getStudentPhoneList();
            
            if(idcardList.contains(student.getIdcard()) && !student.getIdcard().equals("")){
                return new OpResult("身份证号不能重复");
            }else if(phoneList.contains(student.getTelephone()) && !student.getTelephone().equals("")){
                return new OpResult("手机号不能重复");
            }else{
                studentDao.saveStudent(student);
                List<String> projectIds = Arrays.asList(student.getProjectIds().split(","));
                if (projectIds.size() > 0) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (String projectId : projectIds) {
                        Map<String, Object> mapLis = new HashMap<String, Object>();
                        mapLis.put("projectId", projectId);
                        mapLis.put("studentCode", student.getStudentCode());
                        list.add(mapLis);
                    }
                    studentDao.saveProjectStudents(list);
                }
                return new OpResult();
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("添加学生失败", ex);
        }
    }

    @Override
    @Transactional
    public OpResult updateStudent(Student student) throws BusinessLevelException {
        if (student == null) {
            return new OpResult("学生信息错误");
        }
        try {
            studentDao.updateStudent(student);
            studentDao.deleteProjectStudents(student.getStudentCode());
            List<String> projectIds = Arrays.asList(student.getProjectIds().split(","));
            if (projectIds.size() > 0) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (String projectId : projectIds) {
                    Map<String, Object> mapLis = new HashMap<String, Object>();
                    mapLis.put("projectId", projectId);
                    mapLis.put("studentCode", student.getStudentCode());
                    list.add(mapLis);
                }
                studentDao.saveProjectStudents(list);
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("修改学生失败", ex);
        }
    }

    @Override
    public OpResult updateStudentStatus(Map<String, Object> map) throws BusinessLevelException {
        if (map == null) {
            return new OpResult("参数获取失败");
        }
        try {
            studentDao.updateStudentStatus(map);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("修改学生状态失败", ex);
        }
    }

    @Override
    public OpResult deleteStudentByCode(String studentCode) throws BusinessLevelException {
        if (studentCode.equals("")) {
            return new OpResult("学生的编码错误");
        }
        try {
            //删除学员
            studentDao.deleteStudentByCode(studentCode);
            //删学员和项目的链接
            studentDao.deleteProjectStudentByCode(studentCode);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("删除学生失败", ex);
        }
    }

    @Override
    public Map queryStudentByParam(Map<String, Object> map) throws BusinessLevelException {
        //当前用户项目列表
        Integer isSystem = (Integer) map.get("isSystem");
        List<String> studentCodeList = new ArrayList<>();
        if (isSystem == 0) {
            List<Project> projectList = (List<Project>) map.get("projectList");
            //有权限项目下所有学生列表
            if (projectList != null && projectList.size() > 0) {
                studentCodeList = studentDao.getStudentListByProjectId(projectList);
            }
            map.put("studentCodeList", studentCodeList);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("s.create_time desc");
        try {
            if (isSystem == 1) {
                list = studentDao.getAllStudentList(map);
            } else if (studentCodeList != null && studentCodeList.size() > 0) {
                list = studentDao.getStudentList(map);
            }
        } catch (RuntimeException e) {
            log.error("获取学生列表失败:" + e.getMessage());
            throw new BusinessLevelException("获取学生列表失败", e);
        } finally {
            PageHelper.clearPage();
        }
        com.github.pagehelper.PageInfo page = new com.github.pagehelper.PageInfo(list);
        long totalNum = page.getTotal();
        PageHelper.clearPage();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public ResultData<Map<String, Object>> selectStudentByStrCode(String studentCode) throws BusinessLevelException {
        return new ResultData<>(studentDao.selectStudentByStrCode(studentCode));
    }

    @Override
    public boolean selectStudentBySerial(String serial) throws BusinessLevelException {
        Student student = studentDao.selectStudentBySerial(serial);
        if (student == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryProjectStudentsByStuCode(String studentCode)
            throws BusinessLevelException {
        List<Map<String, Object>> data = studentDao.queryProjectStudentsByStuCode(studentCode);
        return new ResultData<List<Map<String, Object>>>(data.size(), data);
    }

    @Override
    public ResultData<List<Student>> queryProjectStudentByProCode(String projectId) throws BusinessLevelException {
        List<Student> data = studentDao.queryProjectStudentByProCode(projectId);
        return new ResultData<List<Student>>(data.size(), data);
    }

    public List<Map<String, Object>> distinctData(List<Map<String, Object>> data) {
        if (data == null || data.size() < 1) {
            return null;
        }
        List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : data) {
            if (map != null && map.get("student_code") != null) {
                Map<String, Object> resuMap = new HashMap<>();
                if (resultData.size() > 0) {
                    resuMap = resultData.get(resultData.size() - 1);
                }
                if (resuMap != null && resuMap.get("student_code") != null
                        && resuMap.get("student_code").toString().equals(map.get("student_code") + "")) {
                    resuMap.put("project_name", resuMap.get("project_name") + "," + map.get("project_name"));
                } else {
                    resultData.add(map);
                }
            }
        }
        return resultData;
    }

    //学生成绩
    @Override
    public OpResult saveClassScore(ClassScore classScores) throws BusinessLevelException {
        if (classScores == null) {
            return new OpResult("项目不能为空");
        }
        try {
            List<String> classNames = studentDao.queryClassScoreCourseNameByProjectId(classScores.getProjectId());
            List<String> courseNames = Arrays.asList(classScores.getCourseName().split(","));
            List<String> scores = Arrays.asList(classScores.getScore().split(","));
            List<String> types = Arrays.asList(classScores.getType().split(","));
            List<ClassScore> classScoreList = new ArrayList<ClassScore>();
            for (int i = 0; i < scores.size(); i++) {
                ClassScore classScore = new ClassScore();
                classScore.setAttendRate(classScores.getAttendRate());
                classScore.setCourseName(courseNames.get(i));
                classScore.setProjectId(classScores.getProjectId());
                classScore.setStudentCode(classScores.getStudentCode());
                classScore.setScore(scores.get(i));
                classScore.setType(types.get(i));
                classScoreList.add(classScore);
            }
            if (classNames.size() < 1) {
                for (int i = 0; i < courseNames.size(); i++) {
                    ClassScore classScore = new ClassScore();
                    classScore.setAttendRate("1");
                    classScore.setCourseName(courseNames.get(i));
                    classScore.setProjectId(classScores.getProjectId());
                    classScore.setStudentCode("1");
                    classScore.setScore("0");
                    classScore.setType("0");
                    classScoreList.add(classScore);
                }
            } else {
                for (int i = 0; i < courseNames.size(); i++) {
                    boolean flg = false;
                    for (int j = 0; j < classNames.size(); j++) {
                        if (courseNames.get(i).equals(classNames.get(j) + "")) {
                            flg = false;
                            break;
                        } else {
                            flg = true;
                        }
                    }
                    if (flg) {
                        ClassScore classScore = new ClassScore();
                        classScore.setAttendRate("1");
                        classScore.setCourseName(courseNames.get(i));
                        classScore.setProjectId(classScores.getProjectId());
                        classScore.setStudentCode("1");
                        classScore.setScore("0");
                        classScore.setType("0");
                        classScoreList.add(classScore);
                        flg = false;
                    }
                }
            }
            studentDao.saveClassScore(classScoreList);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("添加成绩失败", ex);
        }
    }

    @Transactional
    @Override
    public OpResult updateClassScore(ClassScore classScores) throws BusinessLevelException {
        if (classScores == null) {
            return new OpResult("修改对象不能为空");
        }
        try {
            studentDao.deleteClassScoreByProClaCode(classScores);
            saveClassScore(classScores);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("修改失败", ex);
        }
    }

    @Override
    public OpResult deleteClassScoreByProClaCode(ClassScore classScores) throws BusinessLevelException {
        if (classScores == null) {
            return new OpResult("删除内容不能为空");
        }
        try {
            studentDao.deleteClassScoreByProClaCode(classScores);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("删除失败", ex);
        }
    }

    @Override
    public List<String> queryClassScoreCourseName(Map<String, Object> map) throws BusinessLevelException {
        String courseNames = studentDao.queryClassScoreName(map);
        if (courseNames == null || courseNames.equals("")) {
            return null;
        }
        HashSet h = new HashSet(Arrays.asList(courseNames.split(",")));
        return new ArrayList<String>() {
            {
                addAll(h);
            }
        };
    }

    @Override
    public List<String> queryClassScoreCourseNameByProjectId(String projectId) throws BusinessLevelException {
        return studentDao.queryProjectToScoreByProjectCode(projectId);
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryClassScore(Map<String, Object> map)
            throws BusinessLevelException {
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageInfo pagei = new PageInfo(pageInfo.get("page"), pageInfo.get("limit"));
        map.put("start", pagei.getStart());
        map.put("pageSize", pagei.getPageSize());
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        List<Map<String, Object>> scorelist = studentDao.queryClassScoreSqlParam(map);
        List<String> courseName = queryClassScoreCourseName(map);
        List<Map<String, Object>> data = integrationCourse(courseName, scorelist);
        com.github.pagehelper.PageInfo page = new com.github.pagehelper.PageInfo(scorelist);
        long totalNum = page.getTotal();
        return new ResultData<List<Map<String, Object>>>((int) totalNum, data);
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryClassScoreByProCoscId(Map<String, Object> map)
            throws BusinessLevelException {
        List<Map<String, Object>> data = studentDao.queryClassScoreByProCoscId(map);
        return new ResultData<List<Map<String, Object>>>(data.size(), data);
    }

    @Override
    public ResultData<List<Map<String, Object>>> returnClassScoreName() {
        return null;
    }

    @Override
    public OpResult addEditFivePoint(Map<String, Object> param) throws BusinessLevelException {
        try {
            if (param.get("parentCode") != null && !param.get("parentCode").toString().equals("")) {
                //分制
                if (param.get("fivePointCode") == null || param.get("fivePointCode").toString().equals("")) {
                    param.put("fivePointCode", Constants.GUID());
                    param.put("createTime", DateUtil.ToDateTimeString());
                    studentDao.insertFivePoint(param);
                } else {
                    studentDao.updateFivePoint(param);
                }
            } else {
                if (param.get("fivePointCode") == null || param.get("fivePointCode").toString().equals("")) {
                    //新增模板
                    param.put("fivePointCode", Constants.GUID());
                    param.put("createTime", DateUtil.ToDateTimeString());
                    int isExist = studentDao.isPointNameExist(param);
                    if (isExist == 0) {
                        studentDao.insertFivePoint(param);
                    } else {
                        return new OpResult("成绩模板名称已存在,请修改!");
                    }
                } else {
                    //修改模板
                    int isExist = studentDao.isPointNameExist(param);
                    if (isExist == 0) {
                        studentDao.updateFivePoint(param);
                    } else {
                        return new OpResult("成绩模板名称已存在,请修改!");
                    }
                }
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("操作失败", ex);
        }
    }

    @Override
    public OpResult delFivePoint(String fivePointCode) throws BusinessLevelException {
        try {
            studentDao.delFivePoint(fivePointCode);
            return new OpResult() {
                {
                    setMessage("删除成功");
                }
            };
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("删除失败", ex);
        }
    }

    @Override
    public ResultData<Map<String, Object>> queryFivePintByCode(String fivePointCode) throws BusinessLevelException {
        try {
            Map<String, Object> data = studentDao.selectFivePintByCode(fivePointCode);
            return new ResultData<>(data);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("获取失败", ex);
        }
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryFivePint(Map<String, Object> map) throws BusinessLevelException {
        try {
            Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
            PageInfo pagei = new PageInfo(pageInfo.get("page"), pageInfo.get("limit"));
            map.put("start", pagei.getStart());
            map.put("pageSize", pagei.getPageSize());
            PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
            List<Map<String, Object>> data = studentDao.selectFivePint(map);
            for (Map<String, Object> temp : data) {
                int count = studentDao.isTempInUse(temp);
                if (count > 0) {
                    temp.put("isInUse", 1);
                } else {
                    temp.put("isInUse", 0);
                }
            }
            com.github.pagehelper.PageInfo page = new com.github.pagehelper.PageInfo(data);
            long totalNum = page.getTotal();
            return new ResultData<>((int) totalNum, data);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new BusinessLevelException("获取数据失败", e);
        }
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryScoreList(Map<String, Object> map) throws BusinessLevelException {
        try {
            Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
            PageInfo pagei = new PageInfo(pageInfo.get("page"), pageInfo.get("limit"));
            map.put("start", pagei.getStart());
            map.put("pageSize", pagei.getPageSize());
            PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
            List<Map<String, Object>> data = studentDao.queryScoreList(map);
            com.github.pagehelper.PageInfo page = new com.github.pagehelper.PageInfo(data);
            long totalNum = page.getTotal();
            return new ResultData<>((int) totalNum, data);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new BusinessLevelException("获取数据失败", e);
        }
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryScoreListNoPage(Map<String, Object> map) throws BusinessLevelException {
        try {
            List<Map<String, Object>> data = studentDao.queryScoreList(map);
            return new ResultData<>(data.size(), data);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new BusinessLevelException("获取数据失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> queryFivePintALL() throws BusinessLevelException {
        return studentDao.selectFivePint(new HashMap<>());
    }

    @Override
    @Transactional
    public OpResult addAllStudent(List<Map<String, Object>> list, HttpSession session) throws BusinessLevelException {
        try {
            //获取所有的项目
            List<Map<String,String>> errorMsgList = new ArrayList<>();
            List<Map> projectList = projectDao.getProjectList(new HashMap());
            List<Map<String, Object>> studentProject = new ArrayList<Map<String, Object>>();
            List<Student> students = new ArrayList<Student>();
            List<String> idcardList = studentDao.getStudentIdcardList();
            List<String> phoneList = studentDao.getStudentPhoneList();
            List<String> serialList = studentDao.getStudentSerialList();
            boolean hasError = false;
            int i = 2;
            for (Map<String, Object> map1 : list) {
                Map<String,String> errorMsgMap = new HashMap<>();
                errorMsgMap.put("source","第"+i+"行");
                errorMsgMap.put("description","");
                Student student = new Student();
                String studentCode = Constants.GUID();
                student.setStudentCode(studentCode);
                student.setName(map1.get("*name") == null || map1.get("*name").toString().trim().equals("") ? "" : map1.get("*name").toString());
                student.setSerial(map1.get("*card") == null || map1.get("*card").toString().trim().equals("") ? "" : map1.get("*card").toString());
                student.setEnglishName(map1.get("ename") == null || map1.get("ename").toString().trim().equals("") ? "" : map1.get("ename").toString());
                student.setSex(map1.get("sex") == null || map1.get("sex").toString().trim().equals("") ? "1" : map1.get("sex").toString().equals("男") ? "1" : map1.get("sex").toString().equals("女") ? "0" : "");
                student.setIdcard(map1.get("idcard") == null || map1.get("idcard").toString().trim().equals("") ? "" : map1.get("idcard").toString());
                student.setTelephone(map1.get("tel") == null || map1.get("tel").toString().trim().equals("") ? "" : map1.get("tel").toString());
                student.setStatus(map1.get("status") == null || map1.get("status").toString().trim().equals("") ? "1" : map1.get("status").toString());
                student.setCreateTime(DateUtil.ToDateTimeString());
                //项目名称
                String projectCodes = map1.get("projectCode").toString();
                List<String> projectCode = Arrays.asList(projectCodes.split(","));

                boolean isProjectExist = false;
                String projectId = "";
                for (String str : projectCode) {
                    for (Map<String, Object> project : projectList) {
                        if (project.get("projectCode").equals(str)) {
                            isProjectExist = true;
                            projectId =project.get("projectId").toString();
                        }
                    }
//                    if(!isProjectExist){
//                        hasError = true;
//                        errorMsgMap.put("description",errorMsgMap.get("description").toString()+"项目编码"+str+"不存在;");
//                    }
                }
                if (!idcardList.contains(student.getIdcard()) || student.getIdcard().equals("")) {
                    if(phoneList.contains(student.getTelephone()) && !student.getTelephone().equals("")){
                        hasError = true;
                        errorMsgMap.put("description",errorMsgMap.get("description").toString()+"联系方式"+student.getTelephone()+"已存在;");
                    }
                    if(serialList.contains(student.getSerial())){
                        hasError = true;
                        errorMsgMap.put("description",errorMsgMap.get("description").toString()+"学号"+student.getSerial()+"已存在;");
                    }
                    if((!phoneList.contains(student.getTelephone()) || student.getTelephone().equals("")) && !serialList.contains(student.getSerial())) {
                        Map<String, Object> proStu = new HashMap<>(3);
                        idcardList.add(map1.get("idcard").toString());
                        phoneList.add(map1.get("tel").toString());
                        serialList.add(map1.get("*card").toString());
                        students.add(student);
                        proStu.put("projectId",projectId);
                        proStu.put("studentCode", studentCode);
                        studentProject.add(proStu);
                    }
                }else{
                    if(student.getIdcard() != null && !student.getIdcard().equals("")){
                        studentCode = studentDao.getStudentCodeByIdcard(student.getIdcard());
                        if(studentCode != null && !studentCode.equals("")) {
                            Map proStu = new HashMap();
                            proStu.put("studentCode", studentCode);
                            List<String> stuProjectList = studentDao.getProjectListByStudentCode(studentCode);
                            for (String str : projectCode) {
                                projectId = projectDao.getProjectIdByProjectCode(str);
                                if (!stuProjectList.contains(projectId)) {
                                    proStu.put("projectId", projectId);
                                    studentProject.add(proStu);
                                } else {
                                    hasError = true;
                                    errorMsgMap.put("description", errorMsgMap.get("description").toString() + "该学生" + student.getName() + "已存在于项目" + str + "中;");
                                }
                            }
                        }else{
                            hasError = true;
                            errorMsgMap.put("description", errorMsgMap.get("description").toString()+ "该学生" + student.getName() + "与其他学生身份证号重复!");
                        }
                    }
                }
                i++;
                if(hasError) {
                    errorMsgList.add(errorMsgMap);
                }
            }
            if (students.size() > 0) {
                studentDao.saveAllStudent(students);
            }
            if (!hasError && studentProject.size() > 0) {
                studentDao.saveProjectStudents(studentProject);
            }
            if(!hasError) {
                return new OpResult();
            }else{
                session.setAttribute("importStudentErrorMsg",errorMsgList);
                return new OpResult("导入失败，请点击确定下载异常信息!");
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("批量添加失败", ex);
        }

    }

    @Override
    public List<Map<String, Object>> getTempListNoPage() throws BusinessLevelException {
        try {
            return studentDao.selectFivePint(null);
        } catch (RuntimeException e) {
            log.error("获取成绩模板失败!");
            throw new BusinessLevelException("获取成绩模板失败!");
        }
    }

    @Override
    public OpResult addBatchStudentScore(List<Map<String, Object>> list,HttpSession session) throws BusinessLevelException {
        //查询学生是否存在学号
        //查询该学生是否有该项目
        List<Map<String, Object>> studentsList = studentDao.getAllStudentList(new HashMap<>());
        //查询项目是否存在
        //查询项是否有成绩模板
        List<Map> projectList = projectDao.getProjectList(new HashMap());
        //查询该学生是否已经有该项目下的该课程的成绩
        List<Map<String,Object>> scoreList = studentDao.queryScoreAll();
        //查询所有五分制列表
        List<Map<String,Object>> fivePoints = studentDao.queryAllFivePoint();
        //查询所有的项目对应的课程
        List<Map<String,Object>> scoreCourseNames = studentDao.queryClassScoreCourseNameAll();
        //错误与信息
        List<Map<String,Object>> listErrorMsg = new ArrayList<Map<String,Object>>();
        boolean flog = true;
        //需要添加的成绩
        List<ClassScore> scoreData = new ArrayList<ClassScore>();
        int i = 1;
        for ( Map<String,Object> map : list ) {
            i++;
            Map<String,Object> mapErrorMap = new HashMap<>();
            mapErrorMap.put("source","第"+i+"行");
            if(map.get("*name").toString().equals("")||map.get("*serial").toString().equals("")
                || map.get("*courseName").toString().equals("")||map.get("*projectCode").toString().equals("")
                ||(map.get("score").toString().equals("") && map.get("fiveName").toString().equals(""))
                || map.get("*attendRate").toString().equals("")){
                continue;
            }
            Map<String,Object> score = judgementScore(map,studentsList,projectList,scoreList);
            if(!(boolean)score.get("flg")){
                //全局控制插入的数据是否有误
                flog = false;
                mapErrorMap.put("description",score.get("errorMsg").toString());
                listErrorMsg.add(mapErrorMap);
                continue;
            }
            //获取相应的五分制列表
            List<Map<String,Object>> fivePint = (List<Map<String, Object>>) fivePoints.stream()
                    .filter(e -> e.get("parentCode").toString().equals(score.get("tempCode").toString()))
                    .collect(Collectors.toList());
            //获取成绩
            ClassScore classScore = addClassScoreData(score,fivePint);
            if(classScore.getProjectId()==null){
                //全局控制插入的数据是否有误
                flog = false;
                mapErrorMap.put("description",classScore.getCourseName());
                listErrorMsg.add(mapErrorMap);
                continue;
            }
            //需要添加的数据
            scoreData.add(classScore);
            List<Map<String,Object>> scoreCourseName = scoreCourseNames.stream()
                    .filter(e -> e.get("projectId").toString().equals(classScore.getProjectId()))
                    .collect(Collectors.toList());
            AtomicBoolean flg = new AtomicBoolean(false);
            scoreCourseName.forEach( e -> {
                if(e.get("courseName").toString().equals(classScore.getCourseName())){
                    flg.set(true);
                }
            });
            //判断该项目是否已经有改课程
            if(!flg.get()){
                scoreData.add(new ClassScore(){
                    {
                        setStudentCode("1");
                        setProjectId(classScore.getProjectId());
                        setCourseName(classScore.getCourseName());
                        setAttendRate("1");
                        setScore("0");
                        setType("0");
                    }
                });
                scoreCourseNames.add(new HashMap<String,Object>(){
                    {
                        put("projectId",classScore.getProjectId());
                        put("courseName",classScore.getCourseName());
                    }
                });
            }
        }
        try {
            if(scoreData.size()>0){
                studentDao.saveClassScore(scoreData);
            }
            session.setAttribute("importStudentScoreErrorMsg",listErrorMsg);
            if(flog){
                return new OpResult(){
                    {
                        setMessage("导入成功");
                    }
                };
            }else{
                return new OpResult("数据存在错误信息是否下载");
            }
            
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            throw new BusinessLevelException("修改失败", ex);
        }
        
    }

    @Override
    public ResultData<List<Map<String, Object>>> queryClassScoreForPrint(Map<String, Object> map) {
        List<Map<String, Object>> data = studentDao.queryClassScoreForPrint(map);
        return new ResultData<List<Map<String, Object>>>(data.size(), data);
    }

    public ClassScore addClassScoreData(Map<String,Object> map , List<Map<String,Object>> list){
        ClassScore classScore = new ClassScore();
        //成绩存在
        if(!map.get("score").toString().equals("")){
            double score = Double.parseDouble(map.get("score").toString());
            if(score<0 || score>100){
                classScore.setCourseName("成绩不在1-100之间");
                return classScore;
            }else{
                AtomicBoolean flg = new AtomicBoolean(false);
                list.forEach( e -> {
                    List<String> ld = Arrays.asList(e.get("section").toString().split("-"));
                    if(ld.size()!=2){
                        flg.set(false);
                        return;
                    }
                    int num1 = (int) Double.parseDouble(ld.get(0));
                    int num2 = (int) Double.parseDouble(ld.get(1));
                    int sc = (int) score;
                    if(sc>=num1&& sc<=num2){
                        classScore.setScore(map.get("score").toString());
                        classScore.setType(e.get("fivePointCode").toString());
                        flg.set(true);
                        return;
                    }
                });
                if(!flg.get()){
                    classScore.setCourseName("成绩模板不存在该成绩的区间");
                    return classScore;
                }
            }
        //成绩不存在
        }else if (!map.get("fiveName").toString().equals("")){
            String fName = map.get("fiveName").toString();
            AtomicBoolean flg = new AtomicBoolean(false);
            list.forEach( e -> {
                if(e.get("pointName").toString().equals(map.get("fiveName").toString())){
                    map.put("fiveType",e.get("fivePointCode").toString());
                    flg.set(true);
                }
            });
            if(flg.get()){
                classScore.setScore("0");
                classScore.setType(map.get("fiveType").toString()); 
            }else {
                classScore.setCourseName("五分制未找到对应该项目的成绩模板");
                return classScore;
            }
        }else{
            classScore.setCourseName("分数和五分制名称不能都为空");
            return classScore;
        }
        classScore.setProjectId(map.get("projectId").toString());
        classScore.setCourseName(map.get("*courseName").toString());
        classScore.setAttendRate(map.get("*attendRate").toString());
        classScore.setStudentCode(map.get("studentCode").toString());
        return classScore;
    }
    //排除限制成绩问题
    public Map<String,Object> judgementScore(Map<String,Object> map,List<Map<String,Object>> studentsList 
            , List<Map> projectList , List<Map<String,Object>> scoreList){
        List<String> errorMsg = new ArrayList<String>();
        Map<String,Object> result = new HashMap<String,Object>(16){
            {
                put("flg",false);
            }
        };
        AtomicBoolean flg = new AtomicBoolean(false);
        projectList.forEach( e -> {
            //获取项目的id并且项目的成绩编码不能为空
            if(e.get("projectCode").toString().equals(map.get("*projectCode").toString())
                && e.get("tempCode") != null && !e.get("tempCode").toString().equals("")){
                //获取项目添加的projectId
                map.put("tempCode",e.get("tempCode").toString());
                map.put("projectId",e.get("projectId").toString());
                flg.set(true);
                return;
            }
        });
        if(!flg.get()){
            errorMsg.add("项目编码为"+map.get("*projectCode").toString()+"的项目未找到，或该项目没有成绩模板！");
            result.put("errorMsg", StringUtils.join(errorMsg.toArray(),","));
            return result;
        }
        flg.set(false);
        studentsList.forEach( e -> {
            
            //查询学号是否存在改学生
            if(e.get("serial").toString().equals(map.get("*serial")) && e.get("project_id")!=null){
                
                //查询项目该学生是否有这个项目
                List<String> projectIds = Arrays.asList(e.get("project_id").toString().split(","));
                if( projectIds.contains(map.get("projectId").toString())){
                    map.put("studentCode",e.get("student_code").toString());
                    flg.set(true);
                    return;
                }
            }
        });
        if(!flg.get()){
            errorMsg.add("学号为"+map.get("*serial").toString()+"的学员未找到");
            result.put("errorMsg", StringUtils.join(errorMsg.toArray(),","));
            return result;
        }
        flg.set(false);
        
        //判断是否已经有成绩
        scoreList.forEach( e -> {
            if(e.get("projectId").toString().equals(map.get("projectId").toString())
                && e.get("studentCode").toString().equals(map.get("studentCode").toString())
                && e.get("courseName").toString().equals(map.get("*courseName").toString())){
                flg.set(true);
                return ;
            }
        });
        if(!flg.get()){
            map.put("courseName",map.get("*courseName").toString());
            scoreList.add(map);
        }else{
            errorMsg.add("学号为"+map.get("*serial").toString()+"的学员已经有课程:"+map.get("*courseName").toString()+"的成绩了");
        }
        result.put("flg",!flg.get());
        result.putAll(map);
        result.put("errorMsg", StringUtils.join(errorMsg.toArray(),","));
        return result;
    }

    public List<Map<String, Object>> integrationCourse(List<String> listName, List<Map<String, Object>> listdata) {
        if (listName == null || listName.size() < 1 || listdata == null) {
            return null;
        }
        for (Map<String, Object> map : listdata) {
            if (map != null && !map.get("information").toString().equals("")) {
                List<String> information = Arrays.asList((map.get("information") + "").split(","));
                if (information != null && information.size() > 0) {
                    for (String str1 : information) {
                        if (!str1.equals("")) {
                            List<String> course = Arrays.asList(str1.split("--"));
                            for (int i = 0; i < listName.size(); i++) {
                                String name = listName.get(i);
                                String courseName = "courseName" + i;
                                String fivePints = "fivePint" + i;
                                if (name.equals(course.get(0))) {
                                    map.put(courseName, course.get(1));
                                    map.put(fivePints, course.get(2));
                                } else {
                                    if (map.get(courseName) == null || map.get(courseName).toString().isEmpty()) {
                                        map.put(courseName, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Map<String, Object> map : listdata) {
            List<String> scours = new ArrayList<String>();
            List<String> fivePints = new ArrayList<String>();
            for (int i = 0; i < listName.size(); i++) {
                scours.add(map.get("courseName" + i) + "");
                fivePints.add(map.get("fivePint" + i) + "");
            }
            map.put("course", scours);
            map.put("fivePints", fivePints);
        }
        return listdata;
    }

}
