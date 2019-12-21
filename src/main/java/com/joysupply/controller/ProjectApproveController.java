package com.joysupply.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.service.ClassManageService;
import com.joysupply.service.OrganizeStructService;
import com.joysupply.service.RoleService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joysupply.entity.SystemUser;
import com.joysupply.service.ProjectApproveService;
import com.joysupply.util.Constants;
import com.joysupply.util.OpResult;

@Controller
@RequestMapping("/approve")
public class ProjectApproveController extends BaseController {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private ProjectApproveService projectApproveService;
    @Autowired
    RoleService roleService;
    @Autowired
    private OrganizeStructService organizeStructService;

    @RequestMapping("/waitforApproving")
    public String waitForApprove(Model model) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        model.addAllAttributes(resultMap);
        return "kcApprove/waitforApprove";
    }

    @RequestMapping(value = "/kcList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> kcList(@RequestParam Map<String, Object> params) {
        try {
            return projectApproveService.getKcList(params);
        } catch (Exception e) {
            log.error("获取未审批列表失败:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/kcSystemList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> kcSystemList(@RequestParam Map<String, Object> params) {
        try {
            return projectApproveService.kcSystemList(params);
        } catch (Exception e) {
            log.error("获取未审批列表失败:" + e.getMessage());
            return null;
        }
    }

    //财务审批
    @RequestMapping("/cwkcApproval")
    @ResponseBody
    public OpResult cwkcApproval(@RequestParam Map<String, Object> params) {
        String personCode = getUser().getPersonCode();
        params.put("personCode", personCode);
        if(params.containsKey("personIds") ){
    		projectApproveService.sendMsg(params);
    	}
        int pass = Integer.parseInt(params.get("pass")+"");
        OpResult op = null;
        try {
            List<Map<String,Object>> dat = new ObjectMapper().readValue(params.get("data").toString(),ArrayList.class); 
            String approveOption = params.get("approveOption")==null?"":params.get("approveOption")+"";
            if(pass>0){
                for (int i = 0; i < dat.size(); i++) {
                    Map<String,Object> ckDaMap = dat.get(i);
                    ckDaMap.put("id", Constants.GUID());
                    ckDaMap.put("kcStatus",params.get("kcStatus")+"");
                    ckDaMap.put("pass",pass+"");
                    ckDaMap.put("approveOption",approveOption);
                    op = projectApproveService.cwkcApproval(ckDaMap);
                }
            }else{
                Map<String,Object> ckDaMap = dat.get(0);
                ckDaMap.put("id", Constants.GUID());
                ckDaMap.put("kcStatus",params.get("kcStatus")+"");
                ckDaMap.put("pass",pass+"");
                ckDaMap.put("approveOption",approveOption);
                op = projectApproveService.cwkcApproval(ckDaMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new OpResult("操作异常");
        }
        return op;
    }
    
    //审批
    @RequestMapping(value = "/kcPass", method = RequestMethod.GET)
    @ResponseBody
    public OpResult kcPass(@RequestParam Map<String, Object> params) {
        String personCode = getUser().getPersonCode();
        List<String> personRoles = roleService.getPersonRoles(personCode);
        for (String string : personRoles) {
			if(!"18".equals(string) && !"10".equals(string)){
				params.put("personCode", personCode);
			}
		}
        OpResult op = null;
        try {
            JSONArray jsa = JSON.parseArray(params.get("data").toString());
            String teacherCode = "";
            String mergeSerialNumber = Constants.GUID();
            for (int i = 0; i < jsa.size(); i++) {
                JSONObject jo = jsa.getJSONObject(i);
                if (jo.containsKey("teacherCode") && "".equals(teacherCode)) {
                    teacherCode = jo.getString("teacherCode");
                }
                jo.put("id", Constants.GUID());
                params.put("listArr", jo);
                params.put("teacherCode", teacherCode);
                params.put("mergeSerialNumber", mergeSerialNumber);
                op = projectApproveService.kcPass(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return op;
    }

    private String getUserCode() {
        SystemUser user = getUser();
        return user.getPersonCode();
    }

    //已审批列表
    @RequestMapping("/alreadyList")
    public String alReadyList(Model model) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        model.addAllAttributes(resultMap);
        return "kcApprove/alreadyPage";
    }

    @RequestMapping(value = "/kcReadyList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> kcReadyList(@RequestParam Map<String, Object> params) {
        try {
            SystemUser user = getUser();
            String personCode = user.getPersonCode();
            List<String> personRoles = roleService.getPersonRoles(personCode);
            for (String string : personRoles) {
				if("18".equals(string)){
					//return projectApproveService.systemKcReadyList(params);
					return new HashMap<String,Object>(){
                        {
                            put("count",0);
                            put("data", null);
                            put("code", 0);
                            put("msg", "");
                        }
                    };
				}
			}
            params.put("personCode", user.getPersonCode());
            return projectApproveService.kcReadyList(params);
        } catch (Exception e) {
            log.error("获取已审批列表失败:" + e.getMessage());
            return null;
        }
    }

    //返回系统审核页面
    @RequestMapping("/systemApprove")
    public String systemApprove(Model model) {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        model.addAllAttributes(resultMap);
        return "kcApprove/systemApprove";
    }

    //系统管理员保存年月
    @RequestMapping(value = "/managerSave", method = RequestMethod.GET)
    @ResponseBody
    public OpResult managerSave(@RequestParam Map<String, Object> params) {
        return this.projectApproveService.managerSave(params);
    }
}
