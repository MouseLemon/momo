package com.joysupply.service.impl;

import com.joysupply.dao.AuthorityManageDao;
import com.joysupply.dao.KcManagerDao;
import com.joysupply.dao.RoleDao;
import com.joysupply.dao.UnSendTeacherKcDao;
import com.joysupply.service.AuthorityManageService;
import com.joysupply.service.UnSendTeacherKcService;
import com.joysupply.util.Amount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnSendTeacherKcServiceImpl extends BaseService implements UnSendTeacherKcService {

    @Autowired
    private UnSendTeacherKcDao unSendTeacherKcDao;

    @Autowired
    private AuthorityManageDao authorityManageDao;
    @Autowired
    private KcManagerDao kcManagerDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Map<String, Object>> getProjectName(String personCode) {
        List<String> personRoles = roleDao.getPersonRoles(personCode);
        List<Map<String, Object>> projectIds = null;
        if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
            projectIds = authorityManageDao.getPersonProjectAuth("");
        } else {
            projectIds = authorityManageDao.getPersonProjectAuth(personCode);
        }
        // 查询项目对应对的部门
        return unSendTeacherKcDao.getDepList(projectIds);
    }

    @Override
    public List<Map<String, Object>> getProjectName(Map<String, Object> map) {
        String personCode = map.get("personCode").toString();
        List<String> personRoles = roleDao.getPersonRoles(personCode);
        List<Map<String, Object>> projectIds = null;
        if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
            projectIds = authorityManageDao.getPersonProjectAuth("");
        } else {
            projectIds = authorityManageDao.getPersonProjectAuth(personCode);
        }
        map.put("list",projectIds);
        // 查询项目对应对的部门
        return unSendTeacherKcDao.getDepLists(map);
    }

    @Override
    public List getUnsendData(Map<String, Object> params) {
        String time = params.get("time").toString();
        if (!"".equals(time)) {
            params.put("year", time.split("-")[0]);
            params.put("month", Integer.parseInt(time.split("-")[1].toString()));
        }
        int px = 1;
        //wangyuelei增加权限
        Integer status = Integer.parseInt(params.get("status").toString());
        if (status == 2 || status == 3) {
            List<String> personRoles = roleDao.getPersonRoles(params.get("personCode") + "");
            List<Map<String, Object>> projectIds = null;
            if (personRoles.contains("18") || personRoles.contains("1801") ||personRoles.contains("1802")  || personRoles.contains("19") || personRoles.contains("1901") ||personRoles.contains("1902") ) {
                projectIds = authorityManageDao.getPersonProjectAuth("");
            } else {
                projectIds = authorityManageDao.getPersonProjectAuth(params.get("personCode")+"");
            }
            params.put("projectIds",projectIds);
            if (projectIds.size() < 1) {
                return new ArrayList();
            }
        }else{
            return new ArrayList();
        }
        
        List<Map<String, Object>> dataMap = this.unSendTeacherKcDao.kcTeacherList(params);
        //按照合并流水号分组
        Map<String, List<Map<String, Object>>> mergeSerialNumberGroup = dataMap.stream().collect(Collectors.groupingBy(j -> j.get("mergeSerialNumber").toString()));
        Iterator<Map.Entry<String, List<Map<String, Object>>>> iterMerge = mergeSerialNumberGroup.entrySet().iterator();
        
        //返回结果集
        List<List<Map<String, Object>>> result = new ArrayList<>();
        
        while (iterMerge.hasNext()) {
            List<Map<String, Object>> mergeValue = iterMerge.next().getValue();
            //按照教师类型分组
            Map<String, List<Map<String, Object>>> teacherTypeGroup = mergeValue.stream().collect(Collectors.groupingBy(j -> j.get("teacherType").toString()));
            Iterator<Map.Entry<String, List<Map<String, Object>>>> iterTeacherType = teacherTypeGroup.entrySet().iterator();
            
            while (iterTeacherType.hasNext()) {
                List<Map<String, Object>> teacherValue = iterTeacherType.next().getValue();
                //教师类型list
                List<Map<String, Object>> teacherListGroup = new ArrayList<>();
                //项目课酬小计map
                Map<String, Object> projectList = new HashMap<>();

                Map<String, List<Map<String, Object>>> teacherNameGroup = teacherValue.stream().collect(Collectors.groupingBy(j -> j.get("teacherCode").toString()));
                Iterator<Map.Entry<String, List<Map<String, Object>>>> iterTeacherName = teacherNameGroup.entrySet().iterator();

                while (iterTeacherName.hasNext()) {
                    String totalCount = "0.00";
                    List<Map<String, Object>> nameValue = iterTeacherName.next().getValue();
                    Map<String, Object> groupResult = nameValue.get(0);
                    String prijectIds = "";
                    String prijectName = "";
                    String serialNumbers = "";
                    for (int i = 0; i < nameValue.size(); i++) {
                        Map<String, Object> value1 = nameValue.get(i);
                        String projectId = value1.get("projectId").toString();
                        String serialNumber = value1.get("serialNumber").toString();
                        String feeCourse = value1.get("feeCourse").toString();
                        String deductType = value1.get("deductType").toString();
                        String projectName = value1.get("projectName")==null?"":value1.get("projectName").toString();
                        if (projectId.length() > 5 && !prijectIds.contains(projectId)) {
                            prijectIds += projectId + ",";
                        }
                        if (!serialNumbers.contains(serialNumber)) {
                            serialNumbers += serialNumber + ",";
                        }
                        if (!prijectName.contains(projectName)) {
                            prijectName += projectName + ",";
                        }
                        if("0".equals(deductType)){
                            if(groupResult.containsKey(projectId)){
                                String projectCount = Amount.add(groupResult.get(projectId).toString(), feeCourse);
                                groupResult.put(projectId, projectCount);
                            }else{
                                groupResult.put(projectId, feeCourse);
                            }
                        }
                        if(!"0".equals(deductType)){
                            if(groupResult.containsKey(deductType)){
                                String projectCount = Amount.add(groupResult.get(deductType).toString(), feeCourse);
                                groupResult.put(deductType, projectCount);
                            }else{
                                groupResult.put(deductType, feeCourse);
                            }
                        }
                        groupResult.put("projectId", prijectIds);
                        groupResult.put("serialNumber" , serialNumbers);
                        groupResult.put("projectName" , prijectName.substring(0,prijectName.length()-1));
                        if (!projectList.containsKey(projectId)) {
                            if("0".equals(deductType)){
                                projectList.put(projectId, "0.00");
                            }else{
                                projectList.put(deductType, "0.00");
                            }
                        }
                        if ("1901".equals(deductType)) {
                            totalCount = Amount.sub(totalCount, feeCourse);
                        } else {
                            totalCount = Amount.add(totalCount, feeCourse);
                        }
                        //部门维度查询
                        if(value1.get("depCode")!=null){
                            String feeName = value1.get("depCode").toString();
                            if(groupResult.get(feeName)!=null){
                                if ("1901".equals(deductType)) {
                                   groupResult.put(feeName,Amount.sub(groupResult.get(feeName).toString(), feeCourse));
                                }else{
                                    groupResult.put(feeName,Amount.add(groupResult.get(feeName).toString(), feeCourse));
                                }
                            }else{
                                groupResult.put(feeName,feeCourse);
                            }
                        }
                        
                    }
                    groupResult.put("totalCount", totalCount);
                    groupResult.put("px", px);
                    px++;
                    teacherListGroup.add(groupResult);
                }
                Map<String, Object> xiaoji = new HashMap<>();
                String xiaojiTotalCount = "0.00";
                for (int i = 0; i < teacherListGroup.size(); i++) {
                    Map<String, Object> map = teacherListGroup.get(i);
                    if (xiaoji.isEmpty()) {
                        xiaoji.put("px", "小计");
                        xiaoji.put("month", map.get("month"));
                        xiaoji.put("year", map.get("year"));
                        xiaoji.put("teacherType", map.get("teacherType"));
                        xiaoji.put("totalCount", "0.00");
                    }
                    xiaojiTotalCount = Amount.add(xiaojiTotalCount, map.get("totalCount").toString());
                    projectList.forEach((k, v) -> {
                        if (map.containsKey(k)) {
                            projectList.put(k, Amount.add(v.toString(), map.get(k).toString()));
                        }
                    });
                }
                projectList.forEach((k, v) -> {
                    xiaoji.put(k, v);
                });
                xiaoji.put("totalCount", xiaojiTotalCount);
                teacherListGroup.add(xiaoji);
                result.add(teacherListGroup);
            }
        }
        List<Map<String, Object>> heji = new ArrayList<>();
        Map<String, Object> hejiMap = new HashMap<>();
        String hjTotalCount = "0.00";
        hejiMap.put("px", "合计");
        for (int i = 0; i < result.size(); i++) {
            List<Map<String, Object>> value = result.get(i);
            for (int j = 0; j < value.size(); j++) {
                Map<String, Object> map = value.get(j);
                if (map.get("px").equals("小计")) {
                    hjTotalCount = Amount.add(hjTotalCount, map.get("totalCount").toString());
                }
            }
        }
        hejiMap.put("totalCount", hjTotalCount);
        heji.add(hejiMap);
        result.add(heji);
        return result;
    }


}
