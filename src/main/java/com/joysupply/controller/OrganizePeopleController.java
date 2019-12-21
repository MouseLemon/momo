package com.joysupply.controller;

import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.OrganizePeopleService;
import com.joysupply.service.OrganizeStructService;
import com.joysupply.service.RoleService;
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

import java.util.List;
import java.util.Map;

/**
 * 组织人
 *
 * @author dagu
 */
@Controller
@RequestMapping(value = "/api/organizePeople")
public class OrganizePeopleController {
    @Autowired
    private OrganizePeopleService organizePeopleService;

    @Autowired
    private OrganizeStructService organizeStructService;

    @Autowired
    private RoleService roleService;

    private Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/addOrganizePeoplePage", method = RequestMethod.GET)
    public ModelAndView addOrganizeStructPage(@RequestParam Map map) {
        List<JobType> jobTypeList = organizeStructService.getJobTypeList();
        map.put("jobTypeList", jobTypeList);
        List<Role> roleList = roleService.getRoleList();
        map.put("roleList", roleList);
        return new ModelAndView("organizePeople/addOrganizePeoplePage", map);
    }

    @RequestMapping(value = "/editOrganizePeoplePage", method = RequestMethod.GET)
    public ModelAndView editOrganizePeoplePage(@RequestParam String personCode) {
        OrganizePeople organizePeople = organizePeopleService.getOrganizePeople(personCode);
        List<String> personRoles = roleService.getPersonRoles(personCode);
        Map<String, Object> map = MapObj.objectToMap(organizePeople);
        map.put("personRoles", personRoles);
        List<JobType> jobTypeList = organizeStructService.getJobTypeList();
        map.put("jobTypeList", jobTypeList);
        List<Role> roleList = roleService.getRoleList();
        map.put("roleList", roleList);
        return new ModelAndView("organizePeople/editOrganizePeoplePage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 添加人员
     * @Date 2018/11/19 11:52
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/addOrganizePeople", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addOrganizePeople(@RequestBody Map map) {
        try {
            /**
             * wangyuelei
             * 手机号查询是否有
             */
            String telphone = map.get("telphone").toString();
            if(telphone.equals("")||telphone.isEmpty()){
                return new OpResult("手机号不能为空");
            }else{
                OrganizePeople organizePeople = organizePeopleService.getOrganizePeopleByTelephone(telphone);
                if(organizePeople!=null){
                    return new OpResult("手机号已经存在");
                }
                //校验systemuser表是否存在
                int row = organizePeopleService.getSystemUserByTel(telphone);
                if(row > 0){
                    return new OpResult("该手机号已存在账号");
                }
            }

            String roleList = map.get("roleList").toString();
            String[] roleArr = new String[0];
            if (roleList != null && !roleList.equals("")) {
                roleArr = roleList.split(",");
            }
            OrganizePeople organizePeople = new OrganizePeople();
            organizePeople.setPersonCode(Constants.GUID());
            organizePeople.setCreateTime(DateUtil.ToDateTimeString());
            organizePeople.setJobType(map.get("jobType").toString());
            organizePeople.setName(map.get("name").toString());
            organizePeople.setOrganizeCode(map.get("organizeCode").toString());
            organizePeople.setTelphone(map.get("telphone").toString());
            log.debug(organizePeople);
            return organizePeopleService.addOrganizePeople(organizePeople, roleArr);
        } catch (BusinessLevelException ex) {
           log.error("添加人员失败"+ex.getMessage());
            return new OpResult("添加人员失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 修改人员信息
     * @Date 2018/11/19 11:52
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/updOrganizePeople", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updOrganizePeople(@RequestBody Map map) {
        try {
            /**
             * wangyuelei
             * 手机号查询是否有
             */
            String telphone = map.get("telphone").toString();
            String personCode = map.get("personCode").toString();
            if(telphone.equals("")||telphone.isEmpty()){
                return new OpResult("手机号不能为空");
            }else{
                OrganizePeople organizePeople = organizePeopleService.getOrganizePeopleByTelephone(telphone);
                if(organizePeople!=null&&!organizePeople.getPersonCode().equals(personCode)){
                    if(organizePeople!=null){
                        return new OpResult("手机号已经存在");
                    }
                }
                //校验systemuser表是否存在
                int row = organizePeopleService.getSystemUserByTel(telphone);
                if(row > 0){
                    return new OpResult("该手机号已存在账号");
                }
            }

            String roleList = map.get("roleList").toString();
            String[] roleArr = new String[0];
            if (roleList != null && !roleList.equals("")) {
                roleArr = roleList.split(",");
            }
            OrganizePeople organizePeople = new OrganizePeople();
            organizePeople.setPersonCode(map.get("personCode").toString());
            organizePeople.setCreateTime(DateUtil.ToDateTimeString());
            organizePeople.setJobType(map.get("jobType").toString());
            organizePeople.setName(map.get("name").toString());
            //organizePeople.setOrganizeCode(map.get("organizeCode").toString());
            organizePeople.setTelphone(map.get("telphone").toString());
            log.debug(organizePeople);
            return organizePeopleService.updOrganizePeople(organizePeople, roleArr);
        } catch (BusinessLevelException ex) {
            log.error("修改人员信息失败" + ex.getMessage());
            return new OpResult("修改人员信息失败");
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除人员
     * @Date 2018/11/19 11:53
     * @Param [organizePeople]
     * @Return com.joysupply.util.OpResult
     **/
    @RequestMapping(value = "/delOrganizePeople", method = RequestMethod.POST)
    @ResponseBody
    public OpResult delOrganizePeople(@RequestBody OrganizePeople organizePeople) {
        try {
            log.debug(organizePeople);
            return organizePeopleService.delOrganizePeople(organizePeople);
        } catch (BusinessLevelException ex) {
            log.error("删除人员失败"+ex.getMessage());
            return new OpResult("删除人员失败");
        }
    }
    /**
     * @Author MaZhuli
     * @Description 校验联系方式是否存在
     * @Date 2018/12/11 10:56
     * @Param [map]
     * @Return java.util.Map
     **/
    @RequestMapping(value = "/isTelephoneExist", method = RequestMethod.POST)
    @ResponseBody
    public Map isTelephoneExist(@RequestBody Map map) {
        try {
            log.debug(map);
            boolean isTelephoneExist = organizePeopleService.isTelephoneExist(map.get("telphone").toString());
            map.put("isTelephoneExist", isTelephoneExist);
            return map;
        } catch (BusinessLevelException ex) {
            log.error("校验联系方式是否存在失败"+ex.getMessage());
            map.put("isTelephoneExist", false);
            return map;
        }
    }
    
    /**
     * 获取所有person表数据
     * @return
     */
    @RequestMapping(value="/getAllPersonUser")
    @ResponseBody
    public List<Map<String, Object>> getAllPersonUser(){
    	List<Map<String, Object>> list = organizePeopleService.getAllPersonUser();
    	return list;
    }
}
