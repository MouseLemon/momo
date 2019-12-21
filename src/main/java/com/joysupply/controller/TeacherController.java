package com.joysupply.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.joysupply.entity.*;
import com.joysupply.util.*;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.AuthorityManageService;
import com.joysupply.service.DataDictionaryService;
import com.joysupply.service.TeachResearchRoomService;
import com.joysupply.service.TeacherService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 教师controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/teacher")
public class TeacherController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private TeachResearchRoomService teachResearchRoomService;
    @Autowired
    private AuthorityManageService authorityManageService;
    @Autowired
    private HttpSession session;


    /**
     * 跳转到教师列表
     *
     * @return
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(String officeCode, String officeName) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        List<DataDictionary> teacherType = dataDictionaryService.getDicItemListNoPage("13"); // 教师类型
        List<DataDictionary> nationality = dataDictionaryService.getDicItemListNoPage("11"); // 国籍
        resultMap.put("teacherType", teacherType);
        resultMap.put("nationality", nationality);
        resultMap.put("researchOffice", officeCode);
        resultMap.put("officeName", officeName);
        return new ModelAndView("system/teacherList", resultMap);
    }

    /**
     * 跳转到添加教师页面
     *
     * @param teacherCode
     * @return
     */
    @RequestMapping(value = "/addTeacherInfo")
    public ModelAndView addTeacherInfo(String teacherCode, String researchOffice) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (teacherCode != null && !"".equals(teacherCode)) {
            Teacher teacher = getTeacherByCode(teacherCode);
            result.put("teacher", teacher);
        }
        List<DataDictionary> teacherType = dataDictionaryService.getDicItemListNoPage("13"); // 教师类型
        List<DataDictionary> nationality = dataDictionaryService.getDicItemListNoPage("11"); // 国籍
        List<DataDictionary> language = dataDictionaryService.getDicItemListNoPage("15"); // 语种
        List<DataDictionary> job = dataDictionaryService.getDicItemListNoPage("14"); // 职称
        List<DataDictionary> edu = dataDictionaryService.getDicItemListNoPage("22"); // 学位
        result.put("teacherType", teacherType);
        result.put("nationality", nationality);
        result.put("language", language);
        result.put("job", job);
        result.put("edu", edu);
        List<TeachResearchRoom> researchOfficeList = teachResearchRoomService.getResearchOfficeListNoPage(); // 教研室
        result.put("researchOffice", researchOfficeList);
        result.put("researchOfficeCode", researchOffice);
        return new ModelAndView("system/addTeacher", result);
    }


//	@RequestMapping(value="/upTeacherAuth", method=RequestMethod.GET)
//	public ModelAndView upTeacherAuth(String personCode) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		// 查询所有楼群
////		List<DataDictionary> roomLoc = dataDictionaryService.getDicItemListNoPage("21"); // 所属位置
////		Map<String, Object> reqmap = new HashMap<String, Object>();
////		reqmap.put("personCode", personCode);
////		// 查询当前教师所属楼群
////		List<Map<String,Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap );
////		reqmap.put("buildingAuth", buildingAuth);
////		result.put("roomLoc", roomLoc);
////		// 查询所有项目
////		
////		
////		reqmap.put("teacherCode", personCode);
////		List<Map<String,Object>> projectAuth = authorityManageService.getTeacherProjectAuth(reqmap);
////		result.put("projectAuth", projectAuth);
//		result.put("personCode", personCode);
//		return new ModelAndView("system/personDataAuth",result);
//		
//		
//	}

    /**
     * 添加教师楼群权限
     *
     * @param personCode
     * @return
     */
    @RequestMapping(value = "/setTeacherLocAuth", method = RequestMethod.GET)
    public ModelAndView setTeacherLocAuth(String personCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("personCode", personCode);
        // 查询当前教师所属楼群
        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", personCode);
        List<Map<String, Object>> buildingAuth = authorityManageService.getPersonBuildingAuth(reqmap);
        List<String> list = new ArrayList<String>();
        for (Map<String, Object> map : buildingAuth) {
            list.add(map.get("dataCode").toString());
        }
        result.put("buildingAuth", list);
        result.put("status", 1);
        return new ModelAndView("system/setTeacherLocAuth", result);
    }

    @RequestMapping(value = "/setTeacherAuth", method = RequestMethod.GET)
    public ModelAndView setTeacherAuth(String personCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("personCode", personCode);

        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", personCode);
        List<Map<String, Object>> teacherAuth = authorityManageService.getPersonTeacherAuth(reqmap);
        List<String> list = new ArrayList<String>();
        for (Map<String, Object> map : teacherAuth) {
            list.add(map.get("dataCode").toString());
        }
        result.put("teacherAuth", list);
        List<DataDictionary> teacherType = dataDictionaryService.getDicItemListNoPage("13"); // 教师类型
        result.put("teacherType", teacherType);
        return new ModelAndView("system/setTeacherAuth", result);
    }

    /**
     * 添加教师楼群权限
     *
     * @param personCode
     * @return
     */
    @RequestMapping(value = "/setTeacherProjectAuth", method = RequestMethod.GET)
    public ModelAndView setTeacherProjectAuth(String personCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("personCode", personCode);
        // 查询当前教师所属楼群
        Map<String, Object> reqmap = new HashMap<String, Object>();
        reqmap.put("personCode", personCode);
        List<Map<String, Object>> projectAuth = authorityManageService.getPersonProjectAuth(reqmap);
        List<String> list = new ArrayList<String>();
        for (Map<String, Object> map : projectAuth) {
            list.add(map.get("projectId").toString());
        }
        result.put("projectAuth", list);
        return new ModelAndView("system/setTeacherProjectAuth", result);
    }

    /**
     * 跳转到照片页面
     *
     * @param teacherCode
     * @return
     */
    @RequestMapping(value = "/myPic")
    public ModelAndView myPic(String teacherCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (teacherCode != null && !"".equals(teacherCode)) {
            Teacher teacher = getTeacherByCode(teacherCode);
            result.put("pic", teacher.getPic());
        }
        result.put("teacherCode", teacherCode);
        return new ModelAndView("system/myPic", result);
    }


    /**
     * 添加教师信息
     *
     * @param teacher
     * @return
     */
    @RequestMapping(value = "/addTeacher", method = RequestMethod.POST)
    public OpResult addTeacher(Teacher teacher) {
        try {
            log.debug(teacher);
            teacher.setUpdateTime(DateUtil.ToDateTimeString());
            return teacherService.addTeacher(teacher);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }


    /**
     * 修改教师信息
     *
     * @param teacher
     * @return
     */
    @RequestMapping(value = "/updateTeacher", method = RequestMethod.POST)
    public OpResult updateTeacher(Teacher teacher) {
        try {
            log.debug(teacher);
            OpResult opResult = teacherService.updateTeacher(teacher);
            return opResult;
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 删除教师信息
     *
     * @param teacherCode
     * @return
     */
    @RequestMapping(value = "/deleteTeacher", method = RequestMethod.POST)
    public OpResult deleteTeacher(String teacherCode) {
        try {
            log.debug(teacherCode);
            OpResult opResult = teacherService.deleteTeacher(teacherCode);
            return opResult;
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }


    /**
     * 分页查询教师列表
     *
     * @param teacher
     * @param page
     * @param limit
     * @param type    // 0 全部  1 已选
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/listData", method = RequestMethod.GET)
    public Map listData(Teacher teacher, int page, int limit, Integer type, String pcode) {
        Map result = new HashMap();
        try {
            log.debug(teacher);
            // 增加权限
            String personCode = getUser().getPersonCode();
            teacher.setPersonCode(personCode);
            // 判断类型
            if (type != null) {
                // 已选
//                List<Map<String, Object>> teacherAuth = authorityManageService.getPersonTeacherAuth(reqmap);
                PageInfo<Teacher> listTeacherAuthPageData = authorityManageService.listTeacherAuthPageData(pcode, limit, page, teacher, type);
                result.put("code", 0);
                result.put("msg", "");
                result.put("count", listTeacherAuthPageData.getTotal());
                result.put("data", listTeacherAuthPageData.getList());
                return result;
            }
            PageInfo<Teacher> resultData = teacherService.listData(teacher, page, limit);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", resultData.getTotal());
            result.put("data", resultData.getList());
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 根据教师code查询教师详细信息
     *
     * @param teacherCode
     * @return
     */
    @RequestMapping(value = "/getTeacherByCode", method = RequestMethod.GET)
    public Teacher getTeacherByCode(String teacherCode) {
        try {
            log.debug(teacherCode);
            Teacher teacher = teacherService.getTeacherByCode(teacherCode);
            return teacher;
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 根据教师code查询教师详细信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/teacherCenter", method = RequestMethod.GET)
    public ModelAndView teacherCenter() {
        Map<String, Object> result = new HashMap<String, Object>();
        SystemUser user = getUser();
        result.putAll(getMenuMap());
        result.put("user", user);
        Teacher teacher = teacherService.getTeacherInfoByCode(user.getPersonCode());
        // 查询个性签名
        Map<String, String> signaTrue = teacherService.getSignaTrue(getUser().getPersonCode());
        result.put("teacher", teacher);
        result.put("signature", signaTrue);
        return new ModelAndView("teacher/teacherCenter", result);
    }

    /**
     * @Author MaZhuli
     * @Description 复制教师权限页面
     * @Date 2019/1/25 17:49
     * @Param [personCode]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/copyOathPage", method = RequestMethod.GET)
    public ModelAndView copyOathPage(@RequestParam Map map) {
        List<Map> personHaveTeacherAuthList = new ArrayList<>();
        try {
            personHaveTeacherAuthList = authorityManageService.getTeachersAuthPersons(map.get("personCode").toString());
        } catch (BusinessLevelException e) {
            log.error(e.getMessage());
        }
        map.put("personHaveTeacherAuthList", personHaveTeacherAuthList);
        return new ModelAndView("teacher/copyOathPage", map);
    }
    @RequestMapping(value = "/copyOath", method = RequestMethod.POST)
    @ResponseBody
    public OpResult copyOath(@RequestBody Map map) {
        try {
            return authorityManageService.copyOath(map);
        } catch (BusinessLevelException ex) {
            log.error("保存角色失败:" + ex.getMessage());
            return new OpResult("保存角色失败");
        }
    }

    @RequestMapping(value = "/updateSigna", method = RequestMethod.POST)
    public OpResult updateSigna(String signature, String oldsignature) {
        try {
            if (oldsignature != null && !"".equals(oldsignature)) {
                teacherService.updateSigna(getUser().getPersonCode(), signature);
            } else {
                teacherService.addSigna(getUser().getPersonCode(), signature);
            }
            return new OpResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("修改签名失败");
        }
    }


    /**
     * 获取教师首页的今天课程已经今天和明天课时
     *
     * @return
     */
    @RequestMapping(value = "/getTodayClass", method = RequestMethod.POST)
    public Map<String, Object> getTodayClass() {
        Map<String, Object> result = new HashMap<String, Object>();
        result = teacherService.getTodayClass(getUser().getPersonCode()); // 查询今天课程
        Teacher teacher = teacherService.getTeacherByCode(getUser().getPersonCode());
        result.put("teacher_name", getUser().getUserName());
        result.put("teacherPic", teacher.getPic());
        Map<String, String> signaTrue = teacherService.getSignaTrue(getUser().getPersonCode());
        if (signaTrue != null) {
            result.putAll(signaTrue);
        }

        return result;
    }

    /**
     * 批量导入教师
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/importTeacher")
    public OpResult importTeacher(@RequestParam("file") MultipartFile multipartFile) {

        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        String[] titles = {"姓名", "用户名", "开户姓名", "国籍",
                "教师类别", "所得项目", "身份证号", "出生日期", "性别", "银行卡号",
                "联系电话", "语种", "入境日期", "一卡通号", "职称", "学位", "入职日期", "教研室"};
        String[] titles_en = {"teacherName", "systemUserName", "accountName", "nationality", "teacherType",
                "project", "idCard", "birthDay", "sex", "bankCode", "teleNum", "language", "entryDate", "cardNo", "job",
                "degree", "joinTime", "researchOffice"};
        List<Map<String, Object>> msg = new ArrayList<>();
        try {
            Map<String, Object> readExcel = ExcelUtil_JXL.readExcel(multipartFile.getInputStream(), fileType, titles, titles_en);
            List<Map<String, Object>> list = (List<Map<String, Object>>) readExcel.get("list");
            // 用户查重
//            List<Map<String, Object>> list1 = list.stream().filter(distinctByKey(b -> b.get("systemUserName"))).collect(Collectors.toList());
            // excel判重
            distinctList(msg, list);
            // 判断数据字典项是否存在
            List<String> noTeacherType = new ArrayList<>();
            List<String> noNationality = new ArrayList<>();
            List<String> noLanguage = new ArrayList<>();
            List<String> noEdu = new ArrayList<>();
            List<String> noJob = new ArrayList<>();
            List<String> noOffice = new ArrayList<>();
            // 教师类型
            List<DataDictionary> teacherType = dataDictionaryService.getDicItemListNoPage("13");
            // 国籍
            List<DataDictionary> nationality = dataDictionaryService.getDicItemListNoPage("11");
            // 语种
            List<DataDictionary> language = dataDictionaryService.getDicItemListNoPage("15");
            // 职称
            List<DataDictionary> job = dataDictionaryService.getDicItemListNoPage("14");
            // 学位
            List<DataDictionary> edu = dataDictionaryService.getDicItemListNoPage("22");
            // 教研室
            List<TeachResearchRoom> researchOfficeList = teachResearchRoomService.getResearchOfficeListNoPage();
            // 检查教研室
            checkedOffice(list, noOffice, researchOfficeList);
            String key = "teacherType"; // 教室类型
            Tools.getDataDic(list, teacherType, noTeacherType, key);
            key = "nationality";
            Tools.getDataDic(list, nationality, noNationality, key);
            key = "language";
            Tools.getDataDic(list, language, noLanguage, key);
            key = "job";
            Tools.getDataDic(list, job, noJob, key);
            key = "degree";
            Tools.getDataDic(list, edu, noEdu, key);
            Tools.getMsg(msg, noTeacherType, "教师类型");
            Tools.getMsg(msg, noNationality, "国籍");
            Tools.getMsg(msg, noLanguage, "语种");
            Tools.getMsg(msg, noEdu, "学位");
            Tools.getMsg(msg, noJob, "职称");
            Tools.getMsg(msg, noOffice, "教研室");
            // 查询系统中是否已存在
            List<Teacher> existTeachers = teacherService.existTeacher(list);
            for (Teacher teacher : existTeachers) {
                if (teacher != null) {

                    Map<String, Object> option = new HashMap<>();
                    option.put("source", "教师信息");
                    option.put("description", "系统中该教师：“" + teacher.getTeacherName() + "  " + teacher.getIdCard() + "”已存在");
                    msg.add(option);
                }
            }
            // 查询用户是否存在
            List<SystemUser> existUsers = teacherService.existUser(list);
            for (SystemUser user : existUsers) {
                if (user != null && user.getUserName() != null && !"".equals(user.getUserName())) {
                    Map<String, Object> option = new HashMap<>();
                    option.put("source", "教师信息");
                    option.put("description", "系统中该用户：“" + user.getUserName() + "”已存在");
                    msg.add(option);
                }
            }

            checkedSex(msg, list);
            if (msg.size() > 0) {
                session.setAttribute("teacherImportMsg", msg);
                return new OpResult("导入失败，请点击确定下载异常信息");
            }
            key = "teacherType"; // 教室类型
            Tools.setDataDic(list, teacherType, key);
            key = "nationality";
            Tools.setDataDic(list, nationality, key);
            key = "language";
            Tools.setDataDic(list, language, key);
            key = "job";
            Tools.setDataDic(list, job, key);
            key = "degree";
            Tools.setDataDic(list, edu, key);
            teacherService.importTeacher(list);
            Iterator<Map<String, Object>> iterator = list.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> next = iterator.next();
                if (next.get("teleNum") == null || "".equals(next.get("teleNum").toString())) {
                    iterator.remove();
                }
            }
            if (list.size() > 0) {
                teacherService.importUserByTeacher(list);
            }
            return new OpResult();

        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("导入失败");
        }
    }

    private void distinctList(List<Map<String, Object>> msg, List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            Object userName = item.get("systemUserName");
            Object idCard = item.get("idCard");
            Object teleNum = item.get("teleNum");
            Object bankCode = item.get("bankCode");
            Map<String, Object> option1 = new HashMap<>();
//            if(idCard == null || "".equals(idCard.toString())) {
//                option1.put("source", "身份证号");
//                option1.put("description", "第"+(i+2)+"行的身份证号为空");
//                msg.add(option1);
//            }
//            if(bankCode == null || "".equals(bankCode.toString())) {
//                option1.clear();
//                option1.put("source", "银行卡号");
//                option1.put("description", "第"+(i+2)+"行的银行卡号为空");
//                msg.add(option1);
//                continue;
//            }
            boolean flag = (userName != null && !"".equals(userName)) || (idCard != null && !"".equals(idCard)) || (teleNum != null && !"".equals(teleNum));
            if (flag) {
                for (int j = i + 2; j < list.size(); j++) {
                    Map<String, Object> item1 = list.get(j);
                    Object userName1 = item1.get("systemUserName");
                    Object idCard1 = item1.get("idCard");
                    Object teleNum1 = item1.get("teleNum");
                    if (userName1 != null && !"".equals(userName) && userName.equals(userName1)) {
                        Map<String, Object> option = new HashMap<>();
                        option.put("source", "用户名");
                        option.put("description", "excel中第" + (i + 2) + "行的用户名与第" + (j + 2) + "行的相同");
                        msg.add(option);
                    }
                    if (idCard1 != null && !"".equals(idCard1) && idCard.equals(idCard1)) {
                        Map<String, Object> option = new HashMap<>();
                        option.put("source", "身份证号");
                        option.put("description", "excel中第" + (i + 2) + "行的身份证号与第" + (j + 2) + "行的相同");
                        msg.add(option);
                    }
                    if (teleNum1 != null && !"".equals(teleNum1) && teleNum.equals(teleNum1)) {
                        Map<String, Object> option = new HashMap<>();
                        option.put("source", "手机号");
                        option.put("description", "excel中第" + (i + 2) + "行的手机号与第" + (j + 2) + "行的相同");
                        msg.add(option);
                    }
                }
            }
        }
    }

    private void checkedOffice(List<Map<String, Object>> list, List<String> noOffice, List<TeachResearchRoom> researchOfficeList) {
        for (Map<String, Object> item : list) {
            Object type = item.get("researchOffice");
            boolean flag = true;
            if (type != null && !"".equals(type.toString())) {
                for (TeachResearchRoom data : researchOfficeList) {
                    String name = data.getOfficeName();
                    if (name.equals(type.toString())) {
                        item.put("researchOffice", data.getOfficeCode());
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    noOffice.add(type.toString());
                }
            } else {
                noOffice.add("none");
            }
        }
    }

    /**
     * 检查sex
     *
     * @param msg
     * @param list
     */
    private void checkedSex(List<Map<String, Object>> msg, List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            m.put("teacherCode", Constants.GUID());
            m.put("userType", 13);
            m.put("isEnable", 1);
            if (m.get("sex") != null && !"".equals(m.get("sex").toString())) {
                if ("男".equals(m.get("sex").toString())) {
                    m.put("sex", 1);
                } else if ("女".equals(m.get("sex").toString())) {
                    m.put("sex", 2);
                } else {
                    Map<String, Object> option = new HashMap<>();
                    option.put("source", "性别");
                    option.put("description", "第" + (i + 2) + "行的性别填写不正确");
                    msg.add(option);
                }
            } else {
                Map<String, Object> option = new HashMap<>();
                option.put("source", "性别");
                option.put("description", "第" + (i + 2) + "行的性别未填写");
                msg.add(option);
//                    m.put("sex",1);
            }
            m.put("createTime", DateUtil.ToDateTimeString());
        }
    }


    /**
     * 导出异常消息
     *
     * @param response
     */
    @RequestMapping("/exportMsg")
    public void exportMsg(HttpServletResponse response, String key, HttpServletRequest request) {
        String fileName = "异常信息导出.xls";
        String workbookName = "异常信息";
        Map<String, Object> exportMap = new HashMap<>();
        exportMap.put("来源", "source");
        exportMap.put("描述", "description");
        List<Map<String, Object>> list = (List<Map<String, Object>>) session.getAttribute(key);
        session.removeAttribute(key);
        ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response, request);
    }

    /**
     * 模板下载
     *
     * @param response
     */
    @RequestMapping("/templateDownload")
    public void templateDownload(HttpServletResponse response, @RequestParam Map map, HttpServletRequest request) {
        String fileName = "教师导入模板.xls";
        String workbookName = "教师信息";
        Map<String, Object> exportMap = new LinkedMap<>();
        String[] titles = {"姓名", "用户名", "开户姓名", "国籍",
                "教师类别", "所得项目", "身份证号", "出生日期", "性别", "银行卡号",
                "联系电话", "语种", "入境日期", "一卡通号", "职称", "学位", "入职日期", "教研室"};
        String[] titles_en = {"*teacherName", "systemUserName", "*accountName", "nationality", "*teacherType",
                "project", "*idCard", "birthDay", "*sex", "bankCode", "teleNum", "language", "entryDate", "cardNo", "job",
                "degree", "joinTime", "researchOffice"};
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        String[] templateData = {"张XX", "非必填项可以不写", "张XX", "非必填项可以不写", "外聘教师", "非必填项可以不写", "130xxxxxxxxxxxxxxx", "1993/2/2（非必填项可以不写）", "男",
                "非必填项可以不写", "非必填项可以不写", "非必填项可以不写", "1993/2/2（非必填项可以不写）", "非必填项可以不写", "非必填项可以不写", "非必填项可以不写"
                , "1993/2/2（非必填项可以不写）", "英语培训(非必填项可以不写)", "1993/02/02（非必填项可以不写）"};
        for (int i = 0; i < titles.length; i++) {
            exportMap.put(titles[i], titles_en[i]);
            temp.put(titles_en[i], templateData[i]);
        }
        list.add(temp);
        String result = ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response, request);
    }

    /**
     * 教师导出
     *
     * @param response
     */
    @RequestMapping("/teacherExport")
    public void teacherExport(HttpServletResponse response, Teacher teacher, HttpServletRequest request) {
        String fileName = "教师导出数据.xls";
        String workbookName = "教师信息";
        Map<String, Object> exportMap = new LinkedMap<>();
        String[] titles = {"姓名", "用户名", "开户姓名", "国籍",
                "教师类别", "所得项目", "身份证号", "出生日期", "性别", "银行卡号",
                "联系电话", "语种", "入境日期", "一卡通号", "职称", "学位", "入职日期", "最后修改日期"};
        String[] titles_en = {"teacherName", "userName", "accountName", "nationality", "teacherType",
                "project", "idCard", "birthDay", "sex", "bankCode", "teleNum", "language", "entryDate", "cardNo", "job",
                "degree", "joinTime", "updateTime"};
        for (int i = 0; i < titles.length; i++) {
            exportMap.put(titles[i], titles_en[i]);
        }
        List<Map<String, Object>> list = teacherService.listDataNoPage(teacher);
        for (Map<String, Object> map : list) {
            if (map.get("sex") != null && !map.get("sex").toString().equals("")) {
                String sex = map.get("sex").toString();
                if ("1".equals(sex)) {
                    map.put("sex", "男");
                } else if ("2".equals(sex)) {
                    map.put("sex", "女");
                }
            }
        }
        ExcelUtil_JXL.exportExcel(fileName, workbookName, exportMap, list, response, request);
    }


    /**
     * 去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    /**
     * 简历下载
     *
     * @param url
     * @param resumeName
     * @return
     */
    @RequestMapping("/downloadResume")
    public void downloadResume(String url, String resumeName, HttpServletRequest request, HttpServletResponse response) {
        String root = System.getProperty("user.dir");
        File file = new File(root + url);
        if (!file.exists()) {
            log.error("文件未找到!");
        } else {
            try {
                if (MyHttpUtil.isMSBrowser(request)) {
                    response.setHeader("content-disposition", "attachment;filename="
                            + URLEncoder.encode(resumeName, "UTF-8"));
                } else {
                    response.setHeader("content-disposition", "attachment;filename="
                            + new String(resumeName.getBytes("UTF-8"), "ISO-8859-1"));
                }
                FileInputStream in = new FileInputStream(file);
                OutputStream out = response.getOutputStream();
                byte buffer[] = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                log.error("文件未找到!");
            }
        }
    }
}
