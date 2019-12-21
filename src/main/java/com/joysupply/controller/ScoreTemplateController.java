package com.joysupply.controller;

import com.github.pagehelper.PageInfo;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.PrintTemplateParam;
import com.joysupply.entity.ScoreTemplate;
import com.joysupply.service.ScoreTemplateService;
import com.joysupply.service.StudentService;
import com.joysupply.util.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 证书模板控制层
 */
@RequestMapping("/scoreTemplateController")
@RestController
public class ScoreTemplateController extends BaseController {

    @Autowired
    private ScoreTemplateService scoreTemplateService;
    @Autowired
    private StudentService studentService;

    /**
     * 列表页面
     * @return
     */
    @RequestMapping("/templateList")
    public ModelAndView templateList() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("printTemplate/templateList",resultMap);
    }

    /**
     * 列表数据
     * @param scoreTemplate
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/listData")
    public Map<String,Object> listData(ScoreTemplate scoreTemplate,int page, int limit) {
        Map<String,Object> result = new HashMap<>();
        String personCode = getUser().getPersonCode();
        List<OrganizeStruct> listOrganizeByPersonCode = scoreTemplateService.listOrganizeByPersonCode(personCode);
        scoreTemplate.setOrganizeStructs(listOrganizeByPersonCode);
        PageInfo<ScoreTemplate> pageInfo = scoreTemplateService.listData(scoreTemplate,page,limit);
        result.put("code",0);
        result.put("count",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        result.put("msg","");
        return result;

    }

    /**
     * 添加页面
     * @return
     */
    @RequestMapping("/addTempaltePage")
    public ModelAndView addTempalte(String templateId) {
        Map<String,Object> param = new HashMap<>();
        if(!StringUtils.isEmpty(templateId)) {
            // 查询模板
            ScoreTemplate scoreTemplate = scoreTemplateService.getTemplate(templateId);
            param.put("scoreTemplate",scoreTemplate);
        }
        // 获取当前用户的所有项目部
        String personCode = getUser().getPersonCode();
        List<OrganizeStruct> listOrganizeByPersonCode = scoreTemplateService.listOrganizeByPersonCode(personCode);
        param.put("listOrganize",listOrganizeByPersonCode);
        return new ModelAndView("printTemplate/addTemplate",param);
    }

    /**
     * 编辑器上传
     * @param request
     * @param fileDir
     * @return
     */
    @RequestMapping(value="/uploadimage")
    public Map uploadimage(HttpServletRequest request,String fileDir){
        Map<String, Object> map = new HashMap<String, Object>();
        //判断文件是否为空
        if(request instanceof MultipartHttpServletRequest){
            MultipartFile files1=((MultipartHttpServletRequest) request).getFile("upfile");
            Map<String, Object> result = UpLoadFile.uploadFile(files1, fileDir);
            result.put("state","SUCCESS");
            return result;
        }
        return map;
    }

    /**
     * 保存模板
     * @param scoreTemplate
     * @return
     */
    @RequestMapping("/saveTemplate")
    public OpResult saveTemplate(ScoreTemplate scoreTemplate) {
        try {
            if(StringUtils.isEmpty(scoreTemplate.getTemplateId())) {
                scoreTemplate.setCreateTime(DateUtil.ToDateTimeString());
                scoreTemplate.setTemplateId(Constants.GUID());
                scoreTemplateService.saveTemplate(scoreTemplate);
            }else {
                scoreTemplateService.updateTemplate(scoreTemplate);
            }
            return new OpResult();
        }catch (Exception e) {
            e.printStackTrace();
            return new OpResult("保存失败");
        }
    }

    /**
     * 删除模板
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public OpResult delete(String templateId) {
        try {
            scoreTemplateService.deleteTemplate(templateId);
            return new OpResult();
        }catch (Exception e) {
            e.printStackTrace();
            return new OpResult("保存失败");
        }
    }

    /**
     * 设置为默认模板
     * @param scoreTemplate
     * @return
     */
    @RequestMapping(value = "/isDefault", method = RequestMethod.POST)
    public OpResult isDefault(ScoreTemplate scoreTemplate) {
        try {
            scoreTemplateService.isDefault(scoreTemplate);
            return new OpResult();
        }catch (Exception e) {
            e.printStackTrace();
            return new OpResult("保存失败");
        }
    }

    /**
     * 获取打印内容
     * @param templateId
     * @param studentCode
     * @param projectId
     * @return
     */
    @RequestMapping(value = "getPrint", method = RequestMethod.POST)
    public Map<String,Object> getPrint(String studentCode, String projectId, String templateId) {
        List<String> studentCodes = Arrays.asList(studentCode.split(","));
        List<String> projectIds = Arrays.asList(projectId.split(","));
        Map<String,Object> result = new HashMap<>(2);
        ScoreTemplate scoreTemplate = scoreTemplateService.getTemplate(templateId);
        // 如果没设定默认模板
//        if(scoreTemplate == null) {
//            result.put("success",false);
//        }else {
            // 如果设置默认模板
            String css = "<div style='page-break-before: auto; page-break-after: always;'>";
            String template = scoreTemplate.getTemplateContent();
            template = css + template + "</div>";
            String tem = "";
            for ( int i = 0 ; i < studentCodes.size() ; i ++ ){
                tem += getPrintTemplate(studentCodes.get(i), projectIds.get(i), template,scoreTemplate.getTemplateType());
            }
            result.put("success",true);
            result.put("printTemplate",tem);
//        }
        return result;
    }

    /**
     * 替换打印内容
     * @param studentCode
     * @param projectId
     * @param template
     */
    private String getPrintTemplate(String studentCode, String projectId, String template, String type) {
        List<Map<String, Object>> list = listClassScore(studentCode, projectId);
        // 取学生第一个信息，如果有课程名称则需要遍历
        Map<String, Object> item = list.get(0);
        String projectName = item.get("projectName").toString();
        String name = item.get("studentName").toString();
        String sex = item.get("sex").toString();
        String eGender = "female";
        if(sex.equals("1")) {
            sex = "男";
            eGender = "male";
        }else {
            sex = "女";
        }
        String[] startTimeArr = item.get("startTime").toString().split("-");
        String[] endTimeArr = item.get("endTime").toString().split("-");
        String startYear = startTimeArr[0];
        String startMonth = startTimeArr[1];
        String startDay = startTimeArr[2];
        String endYear = endTimeArr[0];
        String endMonth = endTimeArr[1];
        String endDay = endTimeArr[2];
        String nowTime = DateUtil.ToDateString(new Date(),"yyyy-MM-dd");
        String englishDate = DateUtil.getEnglishDate(new Date());
        String englishMonth = englishDate.split("-")[1];
        String issueYear = nowTime.split("-")[0];
        String issueMonth = nowTime.split("-")[1];
        String issueDay = nowTime.split("-")[2];
        String idcard = item.get("idcard").toString();
        String englishName = item.get("englishName")==null?"":item.get("englishName").toString();
        
        template = template.replace(PrintTemplateParam.NAME,name);
        template = template.replace(PrintTemplateParam.PROJECTNAME,projectName);
        template = template.replace(PrintTemplateParam.GENDER,sex);
        template = template.replace(PrintTemplateParam.STARTYEAR,startYear);
        template = template.replace(PrintTemplateParam.STARTMONTH,startMonth);
        template = template.replace(PrintTemplateParam.STARTDAY,startDay);
        template = template.replace(PrintTemplateParam.ENDYEAR,endYear);
        template = template.replace(PrintTemplateParam.ENDMONTH,endMonth);
        template = template.replace(PrintTemplateParam.ENDDAY,endDay);
        template = template.replace(PrintTemplateParam.ENGLISHGENDER,eGender);
        template = template.replace(PrintTemplateParam.ISSUEYEAR,issueYear);
        template = template.replace(PrintTemplateParam.ISSUEMONTH,issueMonth);
        template = template.replace(PrintTemplateParam.ISSUEDAY,issueDay);
        template = template.replace(PrintTemplateParam.IDCARD,idcard);
        template = template.replace(PrintTemplateParam.ENGLISHNAME,englishName);
        template = template.replace(PrintTemplateParam.ENGLISHMONTH,englishMonth);
        if("1".equals(type)) {
            // 成绩模板
            Document document = Jsoup.parse(template);
            Elements table = document.getElementsByTag("table");
            if(!table.isEmpty()) {
                Elements firstRow = table.first().getElementsByTag("tr");
                if(!firstRow.isEmpty()) {
                    // 获取所有的列
                    List<String> list1 = firstRow.eachText();
                    Element element = firstRow.get(0);
                    Elements td = element.select("td");
                    // 遍历成绩数据
                    String html1 = table.first().html();
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        String html = element.html();
                        Iterator<Element> iterator = td.iterator();
                        while (iterator.hasNext()) {
                            String column = iterator.next().text();
                            if(column.contains(PrintTemplateParam.courseName)){
                                if(i == 0) {
                                    Elements select = table.select(".firstRow");
                                    select.remove();
                                    html1 = html1.replace("tbody","thead");
                                    html1 = html1.replace("td","th");
                                    html1 = html1.replace(PrintTemplateParam.courseName,"");
                                }
                                String value = map.get("courseName").toString();
                                html = html.replace(column,value);
                            }
                            if(column.contains(PrintTemplateParam.SCORE)){
                                if(i == 0) {
                                    html1 = html1.replace(PrintTemplateParam.SCORE,"");
                                }
                                String value = map.get("score").toString();
                                html = html.replace(column,value);
                            }
                            if(column.contains(PrintTemplateParam.GRADE)){
                                if(i == 0) {
                                    html1 = html1.replace(PrintTemplateParam.GRADE,"");
                                }
                                String value = map.get("grade").toString();
                                html = html.replace(column,value);
                            }
                        }
//                        for (String column : list1) {
//
//                            if(column.contains(PrintTemplateParam.courseName)){
//                                if(i == 0) {
//                                    Elements select = table.select(".firstRow");
//                                    select.remove();
//                                    html1 = html1.replace("tbody","thead");
//                                    html1 = html1.replace("td","th");
//                                    html1 = html1.replace(PrintTemplateParam.courseName,"");
//                                }
//                                String value = map.get("courseName").toString();
//                                html = html.replace(column,value);
//                            }
//                            if(column.contains(PrintTemplateParam.SCORE)){
//                                if(i == 0) {
//                                    html1 = html1.replace(PrintTemplateParam.SCORE,"");
//                                }
//                                String value = map.get("score").toString();
//                                html = html.replace(column,value);
//                            }
//                            if(column.contains(PrintTemplateParam.GRADE)){
//                                if(i == 0) {
//                                    html1 = html1.replace(PrintTemplateParam.GRADE,"");
//                                }
//                                String value = map.get("grade").toString();
//                                html = html.replace(column,value);
//                            }
//
//                        }
                        if(i == 0) {
                            table.append(html1);
                        }
                        table.append(html);
                    }
                }
            }
            template = document.html();
        }
        return template;
    }

    /**
     * 获取学生成绩及项目信息
     * @param studentCode
     * @param projectId
     * @return
     */
    private List<Map<String, Object>> listClassScore(String studentCode, String projectId) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("projectId",projectId);
        map.put("studentCode",studentCode);
//        ResultData<List<Map<String, Object>>> data = studentService.queryClassScoreByProCoscId(map);
        ResultData<List<Map<String, Object>>> data = studentService.queryClassScoreForPrint(map);
        return data.getData();
    }

    @RequestMapping("/chooseTemplatePage")
    public ModelAndView chooseTemplatePage(String type) {
        String personCode = getUser().getPersonCode();
        List<OrganizeStruct> listOrganizeByPersonCode = scoreTemplateService.listOrganizeByPersonCode(personCode);
        List<ScoreTemplate> list = scoreTemplateService.listTemplate(listOrganizeByPersonCode,type);
        Map<String,List<ScoreTemplate>> param = new HashMap<>(1);
        param.put("listTemplate",list);
        return new ModelAndView("printTemplate/chooseTemplate",param);
    }
}
