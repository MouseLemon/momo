package com.joysupply.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.service.DataDictionaryService;
import com.joysupply.util.MapObj;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.OrganizeStructService;
import com.joysupply.util.OpResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * 组织结构
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/api/organizeStruct")
public class OrganizeStructController extends BaseController {
    @Autowired
    private OrganizeStructService organizeStructService;
    @Autowired
    private DataDictionaryService dataDictionaryService;

    private Log log = LogFactory.getLog(getClass());


    /**
     * @Author MaZhuli
     * @Description 获取组织结构树形菜单
     * @Date 2018/10/31 9:29
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/getOrganizePeoplePage")
    public ModelAndView getOrganizePeoplePage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());

        return new ModelAndView("organizeStruct/organizeStruct", resultMap);
    }

    @RequestMapping(value = "/tree")
    @ResponseBody
    public String tree() {
        List<Map> result = organizeStructService.queryOrganizeTree();
        JSONArray organizeList = JSONArray.fromObject(result);
        return organizeList.toString();
    }

    @RequestMapping(value = "/addOrganizeStructPage", method = RequestMethod.GET)
    public ModelAndView addOrganizeStructPage(@RequestParam Map map) {
        if (map.get("upCode") == null || map.get("upCode").toString().equals("")) {
            // 新建部门
//            OrganizeStruct organizeStruct = organizeStructService.getOrganizeStruct(map.get("upCode").toString());
//            map.put("companyCode",organizeStruct.getCompanyCode());

            List<DataDictionary> companyList = dataDictionaryService.getDicItemListNoPage("26");
            map.put("companyList", companyList);
        }else{
            // 新建项目部
//            OrganizeStruct organizeStruct = organizeStructService.getOrganizeStruct(map.get("upCode").toString());
//            map.put("companyCode",organizeStruct.getCompanyCode());

            List<DataDictionary> companyList = dataDictionaryService.getDicItemListNoPage("26");
            map.put("companyList", companyList);
        }
        return new ModelAndView("organizeStruct/addOrganizeStructPage", map);
    }

    /**
     * @Author Dreamer
     * @Description 编辑部门(修改一级)
     * @Date 下午 16:58 2019/3/6 0006
     * @Param [organizeCode]
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editOrganizeStructPage", method = RequestMethod.GET)
    public ModelAndView editOrganizeStructPage(@RequestParam String organizeCode) {
        Map map = new HashMap();
        OrganizeStruct organizeStruct = organizeStructService.getOrganizeStruct(organizeCode);
        List<DataDictionary> companyList = dataDictionaryService.getDicItemListNoPage("26");
        map.put("organizeStruct", organizeStruct);
//        map.put("companyList", companyList);
        return new ModelAndView("organizeStruct/editOrganizeStructPage", map);
    }

    /**
     * @Author Dreamer
     * @Description 编辑项目部(修改二级)
     * @Date 下午 17:00 2019/3/6 0006
     * @Param [organizeCode]
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editOrganizeStructChildPage", method = RequestMethod.GET)
    public ModelAndView editOrganizeStructChildPage(@RequestParam String organizeCode) {
        OrganizeStruct organizeStruct = organizeStructService.getOrganizeStruct(organizeCode);
        List<Map> list = organizeStructService.queryFirstOrganizeList();
//        List<DataDictionary> companyList = dataDictionaryService.getDicItemListNoPage("26");
        List<Map<String, Object>> companyList = dataDictionaryService.getCompany("26");
        Map<String, Object> resultMap = MapObj.objectToMap(organizeStruct);
        resultMap.put("organizeList", list);
        resultMap.put("companyList", companyList);
        return new ModelAndView("organizeStruct/editOrganizeStructChildPage", resultMap);
    }

    @RequestMapping(value = "/addOrganizeStruct", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addOrganizeStruct(@RequestBody OrganizeStruct organizeStruct) {
        try {
            log.debug(organizeStruct);
            return organizeStructService.addOrganizeStruct(organizeStruct);
        } catch (BusinessLevelException ex) {
            log.error("添加部门失败" + ex.getMessage());
            return new OpResult("添加部门失败");
        }
    }

    @RequestMapping(value = "/delOrganizeStruct", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delOrganizeStruct(@RequestBody OrganizeStruct organizeStruct) {
        try {
            log.debug(organizeStruct);
            return organizeStructService.delOrganizeStruct(organizeStruct);
        } catch (BusinessLevelException ex) {
            log.error("删除部门失败" + ex.getMessage());
            return new OpResult("删除部门失败");
        }
    }

    /**
     * @Author Dreamer
     * @Description 修改项目部
     * @Date 下午 17:28 2019/3/6 0006
     * @Param [organizeStruct]
     * @return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updOrganizeStruct", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updOrganizeStruct(@RequestBody OrganizeStruct organizeStruct) {
        try {
            log.debug(organizeStruct);
            if(organizeStruct.getCompanyCode() == null || organizeStruct.getCompanyCode().equals("")){
                OrganizeStruct organizeStruct1 = organizeStructService.getOrganizeStruct(organizeStruct.getUpCode());
                organizeStruct.setCompanyCode(organizeStruct1.getCompanyCode());
            }
            return organizeStructService.updOrganizeStruct(organizeStruct);
        } catch (BusinessLevelException ex) {
            log.error("修改部门失败" + ex.getMessage());
            return new OpResult("修改部门失败");
        }
    }

    /**
     * @Author Dreamer
     * @Description 修改部门
     * @Date 下午 14:46 2019/3/15 0015
     * @Param [organizeStruct]
     * @return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updateDepartment", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateDepartment(@RequestBody OrganizeStruct organizeStruct) {
        try {
            log.debug(organizeStruct);
            return organizeStructService.updateDepartment(organizeStruct);
        } catch (BusinessLevelException ex) {
            log.error("修改部门失败" + ex.getMessage());
            return new OpResult("修改部门失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取组织人员列表
     * @Date 2018/11/2 9:06
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    @RequestMapping(value = "/getOrganizePeopleList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getOrganizePeopleList(@RequestParam Map map) {
        try {
            if (map.get("organizeCode") != null && !map.get("organizeCode").toString().equals("")) {
                List<OrganizeStruct> organizeStructList = new ArrayList<>();
                String organizeCode = map.get("organizeCode").toString();
                OrganizeStruct currOrganizeStruct = organizeStructService.getOrganizeStruct(organizeCode);
                organizeStructList.add(currOrganizeStruct);
                OrganizeStruct organizeStruct = new OrganizeStruct();
                organizeStruct.setOrganizeCode(organizeCode);
                List<OrganizeStruct> son = organizeStructService.getSon(organizeStruct);
                if (son != null && son.size() > 0) {
                    organizeStructList.addAll(son);
                }
                map.put("organizeStructList", organizeStructList);
            }
            return organizeStructService.getOrganizePeopleList(map);
        } catch (BusinessLevelException e) {
            log.error("获取组织人员列表失败" + e.getMessage());
            return null;
        }
    }


    /**
     * 查询所有部门
     *
     * @return
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAll() {
        List<OrganizeStruct> list = organizeStructService.getAll();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", list);
        result.put("code", 0);
        result.put("count", list.size());
        result.put("success", true);
        result.put("msg", "获取列表成功！");
        return result;
    }
}
