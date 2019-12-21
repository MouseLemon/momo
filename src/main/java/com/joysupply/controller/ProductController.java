package com.joysupply.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import java.lang.reflect.Array;
import java.util.*;

/**
 * 项目controller
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProductService productService;
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

    // ---------------------------------------------------- 爆款单品 ----------------------------------------------------

    /**
    * @Author: WangHao
    * @Description: 跳转爆款单品列表页
    * @Date: 2019/12/15 22:38
    * @Param: []
    * @returns: org.springframework.web.servlet.ModelAndView
    */
    @RequestMapping(value = "/getPopularListPage")
    public ModelAndView getPopularListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", getUser());
        List<DataDictionary> popularTypeList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        try {
            // 16是爆款类型
            popularTypeList = dataDictionaryService.getDicItemListNoPage("16");
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
            log.error("获取爆款类型" + e.getMessage());
        }
        if (!(structList.size() > 0)) {
            structList = null;
        }
        resultMap.put("popularTypeList", popularTypeList);
        resultMap.put("structList", structList);
        return new ModelAndView("product/productManagement/popularInfo", resultMap);
    }

    /** 
    * @Author: WangHao
    * @Description: 获取爆款列表分页数据
    * @Date: 2019/12/16 9:21
    * @Param: [map]
    * @returns: java.util.Map<java.lang.String,java.lang.Object>
    */
    @RequestMapping(value = "/getPopularList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPopularList(@RequestParam Map map) {
        try {
            return productService.getPopularList(map);
        } catch (BusinessLevelException e) {
            log.error("获取爆款列表:" + e.getMessage());
            return null;
        }
    }

    /** 
    * @Author: WangHao
    * @Description: 新增爆款页面
    * @Date: 2019/12/16 9:47
    * @Param: [map]
    * @returns: org.springframework.web.servlet.ModelAndView
    */
    @RequestMapping(value = "/addPopularPage", method = RequestMethod.GET)
    public ModelAndView addPopularPage(@RequestParam Map map) {
        SystemUser user = getUser();
        List<DataDictionary> popularTypeList = new ArrayList<>();
        try {
            // 16是爆款类型
            popularTypeList = dataDictionaryService.getDicItemListNoPage("16");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
        } catch (BusinessLevelException e) {
            log.error("跳转新增项目页面失败" + e.getMessage());
        }
        map.put("popularTypeList", popularTypeList);
        return new ModelAndView("product/productManagement/addPopularPage", map);
    }

    /**
    * @Author: WangHao
    * @Description: 保存爆款
    * @Date: 2019/12/16 10:56
    * @Param: [projectAndFile]
    * @returns: com.joysupply.util.OpResult
    */
    @RequestMapping(value = "/addPopular", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addPopular(@RequestParam Map map) {
        try {
            log.debug(map);
            SystemUser user = getUser();
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            map.put("userName", userName);
            map.put("personCode", personCode);
            return productService.addPopular(map);
        } catch (BusinessLevelException ex) {
            log.error("保存项目失败" + ex.getMessage());
            return new OpResult("保存项目失败");
        }
    }

    /**
    * @Author: WangHao
    * @Description: 编辑爆款页面
    * @Date: 2019/12/16 15:25
    * @Param: [popularId, preStatus]
    * @returns: org.springframework.web.servlet.ModelAndView
    */
    @RequestMapping(value = "/editPopularPage", method = RequestMethod.GET)
    public ModelAndView editPopularPage(@RequestParam String popularId, @RequestParam Integer preStatus) {
        Map<String, Object> map = productService.getPopular(popularId);
        SystemUser user = getUser();
        List<DataDictionary> popularTypeList = new ArrayList<>();
        try {
            // 16是爆款类型
            popularTypeList = dataDictionaryService.getDicItemListNoPage("16");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
        } catch (BusinessLevelException e) {
            log.error("跳转编辑项目页面失败" + e.getMessage());
        }
        map.put("popularTypeList", popularTypeList);
        map.put("status", preStatus);
        return new ModelAndView("product/productManagement/editPopularPage", map);
    }

    /** 
    * @Author: WangHao
    * @Description: 编辑爆款
    * @Date: 2019/12/16 15:33
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @RequestMapping(value = "/editPopular", method = RequestMethod.POST)
    @ResponseBody
    public OpResult editPopular(@RequestParam Map map) {
        try {
            log.debug(map);
            SystemUser user = getUser();
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            map.put("userName", userName);
            map.put("personCode", personCode);
            return productService.editPopular(map);
        } catch (BusinessLevelException ex) {
            log.error("保存爆款失败" + ex.getMessage());
            return new OpResult("保存爆款失败");
        }
    }

    /** 
    * @Author: WangHao
    * @Description: 删除爆款
    * @Date: 2019/12/16 16:25
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @RequestMapping(value = "/delPopular", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delPopular(@RequestParam Map map) {
        try {
            log.debug(map);
            return productService.delPopular(map);
        } catch (BusinessLevelException ex) {
            log.error("保存爆款失败" + ex.getMessage());
            return new OpResult("保存爆款失败");
        }
    }

    // ------------------------------------------------------- 样板间 ---------------------------------------------------

    /** 
    * @Author: WangHao
    * @Description: 跳转样板间列表页
    * @Date: 2019/12/16 19:03
    * @Param: []
    * @returns: org.springframework.web.servlet.ModelAndView
    */
    @RequestMapping(value = "/getSuitListPage")
    public ModelAndView getSuitListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", getUser());
        List<DataDictionary> suitTypeList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        try {
            // 12是样板间类型
            suitTypeList = dataDictionaryService.getDicItemListNoPage("12");
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
            log.error("获取爆款类型" + e.getMessage());
        }
        if (!(structList.size() > 0)) {
            structList = null;
        }
        resultMap.put("suitTypeList", suitTypeList);
        resultMap.put("structList", structList);
        return new ModelAndView("product/productManagement/suitInfo", resultMap);
    }

    /**
    * @Author: WangHao
    * @Description: 获取样板间分页数据
    * @Date: 2019/12/16 19:21
    * @Param: [map]
    * @returns: java.util.Map<java.lang.String,java.lang.Object>
    */
    @RequestMapping(value = "/getSuitList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSuitList(@RequestParam Map map) {
        try {
            return productService.getSuitList(map);
        } catch (BusinessLevelException e) {
            log.error("获取爆款列表:" + e.getMessage());
            return null;
        }
    }

    /**
    * @Author: WangHao
    * @Description: 新建样板间
    * @Date: 2019/12/16 19:35
    * @Param: [map]
    * @returns: org.springframework.web.servlet.ModelAndView
    */
    @RequestMapping(value = "/addSuitPage", method = RequestMethod.GET)
    public ModelAndView addSuitPage(@RequestParam Map map) {
        SystemUser user = getUser();
        List<DataDictionary> suitTypeList = new ArrayList<>();
        try {
            // 12是样板间类型
            suitTypeList = dataDictionaryService.getDicItemListNoPage("12");
        } catch (BusinessLevelException e) {
            log.error("跳转新增项目页面失败" + e.getMessage());
        }
        map.put("suitTypeList", suitTypeList);
        return new ModelAndView("product/productManagement/addSuitPage", map);
    }

    /**
    * @Author: WangHao
    * @Description: 保存样板间
    * @Date: 2019/12/16 19:35
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @RequestMapping(value = "/addSuit", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addSuit(@RequestParam Map map) {
        try {
            log.debug(map);
            SystemUser user = getUser();
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            map.put("userName", userName);
            map.put("personCode", personCode);
            return productService.addSuit(map);
        } catch (BusinessLevelException ex) {
            log.error("保存项目失败" + ex.getMessage());
            return new OpResult("保存项目失败");
        }
    }

    @RequestMapping(value = "/editSuitPage", method = RequestMethod.GET)
    public ModelAndView editSuitPage(@RequestParam String suitId) {
        Map<String, Object> map = productService.getSuit(suitId);
        SystemUser user = getUser();
        List<DataDictionary> suitTypeList = new ArrayList<>();
        try {
            // 12是样板间类型
            suitTypeList = dataDictionaryService.getDicItemListNoPage("12");
        } catch (BusinessLevelException e) {
            log.error("跳转编辑项目页面失败" + e.getMessage());
        }
        List imgList = productService.getSuitImgList(suitId);

        map.put("imgXy1", "");
        map.put("imgUrl1", "");
        map.put("imgXy2", "");
        map.put("imgUrl2", "");
        map.put("imgXy3", "");
        map.put("imgUrl3", "");

        for (int i = 0; i < imgList.size(); i++) {
            Map iMap = (Map) imgList.get(i);
            String imgUrl = iMap.get("imgUrl").toString();
            String imgXy = iMap.get("imgXy").toString();

            if ("1".equals(imgXy)) {
                map.put("imgXy1", imgXy);
                map.put("imgUrl1", imgUrl);
            } else if ("2".equals(imgXy)) {
                map.put("imgXy2", imgXy);
                map.put("imgUrl2", imgUrl);
            } else if ("3".equals(imgXy)) {
                map.put("imgXy3", imgXy);
                map.put("imgUrl3", imgUrl);
            }
        }

        map.put("suitTypeList", suitTypeList);
        return new ModelAndView("product/productManagement/editSuitPage", map);
    }

    @RequestMapping(value = "/editSuit", method = RequestMethod.POST)
    @ResponseBody
    public OpResult editSuit(@RequestParam Map map) {
        try {
            log.debug(map);
            SystemUser user = getUser();
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            map.put("userName", userName);
            map.put("personCode", personCode);
            return productService.editSuit(map);
        } catch (BusinessLevelException ex) {
            log.error("保存爆款失败" + ex.getMessage());
            return new OpResult("保存爆款失败");
        }
    }

    @RequestMapping(value = "/delSuit", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delSuit(@RequestParam Map map) {
        try {
            log.debug(map);
            return productService.delSuit(map);
        } catch (BusinessLevelException ex) {
            log.error("保存爆款失败" + ex.getMessage());
            return new OpResult("保存爆款失败");
        }
    }

    // ------------------------------------------------------ 家居日志 --------------------------------------------------

    @RequestMapping(value = "/getDiaryListPage")
    public ModelAndView getDiaryListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        SystemUser user = getUser();
        resultMap.put("user", getUser());
        List<DataDictionary> diaryTypeList = new ArrayList<>();
        List<OrganizeStruct> structList = new ArrayList<>();
        try {
            // 11是日记类型
            diaryTypeList = dataDictionaryService.getDicItemListNoPage("11");
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
            log.error("获取日记类型" + e.getMessage());
        }
        if (!(structList.size() > 0)) {
            structList = null;
        }
        resultMap.put("diaryTypeList", diaryTypeList);
        resultMap.put("structList", structList);
        return new ModelAndView("product/productManagement/diaryInfo", resultMap);
    }

    @RequestMapping(value = "/getDiaryList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getDiaryList(@RequestParam Map map) {
        try {
            return productService.getDiaryList(map);
        } catch (BusinessLevelException e) {
            log.error("获取日记列表:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/addDiaryPage", method = RequestMethod.GET)
    public ModelAndView addDiaryPage(@RequestParam Map map) {
        SystemUser user = getUser();
        List<DataDictionary> diaryTypeList = new ArrayList<>();
        try {
            // 11是日记类型
            diaryTypeList = dataDictionaryService.getDicItemListNoPage("11");
        } catch (BusinessLevelException e) {
            log.error("跳转新增项目页面失败" + e.getMessage());
        }
        map.put("diaryTypeList", diaryTypeList);
        return new ModelAndView("product/productManagement/addDiaryPage", map);
    }

    @RequestMapping(value = "/addDiary", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addDiary(@RequestParam Map map) {
        try {
            log.debug(map);
            SystemUser user = getUser();
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            map.put("userName", userName);
            map.put("personCode", personCode);
            return productService.addDiary(map);
        } catch (BusinessLevelException ex) {
            log.error("保存项目失败" + ex.getMessage());
            return new OpResult("保存项目失败");
        }
    }

    @RequestMapping(value = "/editDiaryPage", method = RequestMethod.GET)
    public ModelAndView editDiaryPage(@RequestParam String diaryId) {
        Map<String, Object> map = productService.getDiary(diaryId);
        Map temp = new HashMap();
        temp.put("diaryId", diaryId);
        Map imgMap = productService.getDiaryImgList(temp);
        map.putAll(imgMap);


        String pStr = "";
        List data = (List)imgMap.get("data");
        for (int i = 0; i < data.size(); i ++) {
            Map pMap = (Map) data.get(i);
            String pId = pMap.get("imgId").toString();
            pStr = pStr + pId + ",";
        }
        // pstr 是当前所有图片的id
        map.put("pStr", pStr);

        SystemUser user = getUser();
        List<DataDictionary> diaryTypeList = new ArrayList<>();
        try {
            // 11是日记类型
            diaryTypeList = dataDictionaryService.getDicItemListNoPage("11");
            List<String> personRoles = roleService.getPersonRoles(getUserCode());
        } catch (BusinessLevelException e) {
            log.error("跳转编辑项目页面失败" + e.getMessage());
        }
        map.put("diaryTypeList", diaryTypeList);
        return new ModelAndView("product/productManagement/editDiaryPage", map);
    }

    @RequestMapping(value = "/editDiary", method = RequestMethod.POST)
    @ResponseBody
    public OpResult editDiary(@RequestParam Map map) {
        try {
            log.debug(map);
            SystemUser user = getUser();
            String userName = user.getUserName();
            String personCode = user.getPersonCode();
            map.put("userName", userName);
            map.put("personCode", personCode);
            return productService.editDiary(map);
        } catch (BusinessLevelException ex) {
            log.error("保存爆款失败" + ex.getMessage());
            return new OpResult("保存爆款失败");
        }
    }







    
    
    
    
    
    

}
