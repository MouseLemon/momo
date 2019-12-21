package com.joysupply.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.RoleDao;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.AuthorityManageDao;
import com.joysupply.service.AuthorityManageService;
import org.springframework.transaction.annotation.Transactional;

@Service("authorityManageService")
public class AuthorityManageServiceImpl implements AuthorityManageService {

    @Autowired
    AuthorityManageDao authorityManageDao;
    @Autowired
    RoleDao roleDao;
    private Log log = LogFactory.getLog(getClass());

    @Override
    public List<Map<String, Object>> getPersonBuildingAuth(Map<String, Object> reqmap) throws BusinessLevelException {
        try {
            List<String> personCode = roleDao.getPersonRoles(reqmap.get("personCode").toString());
            if (personCode.contains("18") || personCode.contains("1801") || personCode.contains("1802") || personCode.contains("19")|| personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getPersonBuildingAuth(null);
            } else if (personCode.contains("19") || personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getPersonBuildingAuth(null);
            } else {
                return authorityManageDao.getPersonBuildingAuth(reqmap.get("personCode").toString());
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("查询用户对应楼失败", ex);
        }
    }

    @Override
    public List<Map<String, Object>> getPersonProjectAuth(Map<String, Object> reqmap) throws BusinessLevelException {
        try {
            List<String> personCode = roleDao.getPersonRoles(reqmap.get("personCode").toString());
            if (personCode.contains("18") || personCode.contains("1801") || personCode.contains("1802") || personCode.contains("19")|| personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getPersonProjectAuth(null);
            } else if ( personCode.contains("19")|| personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getClassManageProject();
            }
            return authorityManageDao.getPersonProjectAuth(reqmap.get("personCode").toString());
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("查询用户对应项目失败", ex);
        }
    }

    @Override
    public OpResult deleteTeacherProjectAuth(String personCode) throws BusinessLevelException {
        try {
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 3);
                    put("dataId", personCode);
                }
            });
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("删除项目教师权限失败", ex);
        }
    }

    @Override
    public OpResult deletePersonProjectAuth(String projectCode) throws BusinessLevelException {
        try {
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 2);
                    put("dataId", projectCode);
                }
            });
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("删除用户项目权限失败", ex);
        }
    }

    @Override
    public OpResult deletePersonBuildingAuth(String building) throws BusinessLevelException {
        try {
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 1);
                    put("dataId", building);
                }
            });
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("删除用户教学楼权限失败", ex);
        }
    }

    @Override
    public OpResult insertTeacherProjectAuth(Map<String, Object> map) throws BusinessLevelException {
        try {
            String projectIds = map.get("projectIds").toString();
            String[] ids = projectIds.split(",");
            map.put("dataIds", ids);
            map.put("dataCode", 2);
            map.put("personCode", map.get("teacherCode"));
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 2);
                    put("dataId", map.get("teacherCode"));
                }
            });
            authorityManageDao.insertData(map);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("添加项目教师权限失败", ex);
        }
    }

    @Override
    public OpResult insertPersonProjectAuth(Map<String, Object> map) throws BusinessLevelException {
        try {
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 2);
                    put("dataId", map.get("PersonCode"));
                }
            });
            Object projectCode = map.get("projectCode");
            if(projectCode != null && !"".equals(projectCode.toString())) {
                String projectIds = projectCode.toString();
                String[] ids = projectIds.split(",");
                map.put("dataIds", ids);
                map.put("dataCode", 2);
                authorityManageDao.insertData(map);
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("添加用户项目权限失败", ex);
        }
    }

    @Override
    public OpResult insertPersonBuildingAuth(Map<String, Object> map) throws BusinessLevelException {
        try {
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 1);
                    put("dataId", map.get("personCode"));
                }
            });
            Object buildingCodes = map.get("buildingCodes");
            if(buildingCodes != null && !"".equals(buildingCodes.toString())) {
                String projectIds = buildingCodes.toString();
                String[] ids = projectIds.split(",");
                map.put("dataIds", ids);
                map.put("dataCode", 1);
                authorityManageDao.insertData(map);
            }

            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("添加用户教学楼权限失败", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取人员教师权限
     * @Date 2018/11/28 10:09
     * @Param [reqmap]
     * @Return java.util.List<java.util.Map       <       java.lang.String       ,       java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> getPersonTeacherAuth(Map<String, Object> reqmap) throws BusinessLevelException {
        try {
            List<String> personCode = roleDao.getPersonRoles(reqmap.get("personCode").toString());
            if (personCode.contains("18") || personCode.contains("1801") || personCode.contains("1802") || personCode.contains("19")|| personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getPersonTeacherAuth(null);
            } else if (personCode.contains("10") || personCode.contains("1001")|| personCode.contains("1002")) {
                return authorityManageDao.getPersonTeacherAuthA(reqmap.get("personCode").toString());
            } else {
                return authorityManageDao.getPersonTeacherAuth(reqmap.get("personCode").toString());
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("查询用户对应项目失败", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 添加人员教师权限
     * @Date 2018/11/28 10:09
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult insertPersonTeacherAuth(Map<String, Object> map) throws BusinessLevelException {
        try {
            authorityManageDao.deleteData(new HashMap<String, Object>() {
                {
                    put("dataCode", 3);
                    put("dataId", map.get("personCode"));
                }
            });
            if(map.get("teacherCodes") != null && !"".equals(map.get("teacherCodes"))) {
                String projectIds = map.get("teacherCodes").toString();
                String[] ids = projectIds.split(",");
                map.put("dataIds", ids);
                map.put("dataCode", 3);
                authorityManageDao.insertData(map);
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("添加人员教师权限失败", ex);
        }
    }

    /**
     * @Author Dreamer
     * @Description 教师视图下拉选
     * @Date 下午 17:03 2018/12/18 0018
     * @Param [reqmap]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> getTeachersAuth(Map<String, Object> reqmap) throws BusinessLevelException {
        try {
            List<String> personCode = roleDao.getPersonRoles(reqmap.get("personCode").toString());
            if (personCode.contains("18") || personCode.contains("1801") || personCode.contains("1802") || personCode.contains("19")|| personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getTeachersAuth(null);
            } else if (personCode.contains("19") || personCode.contains("1901")|| personCode.contains("1902")) {
                return authorityManageDao.getTeachersAuth(null);
            } else if (personCode.contains("10") || personCode.contains("1001")|| personCode.contains("1002")) {
                Map<String, Object> organizeCode = authorityManageDao.getOrganizeCode(reqmap.get("personCode").toString());
                return authorityManageDao.getTeachersAuthA(organizeCode.get("organize_code").toString());
            } else {
                return authorityManageDao.getTeachersAuth(reqmap.get("personCode").toString());
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("error" + ex);
        }
    }

    /**
     * 查询用户教师权限分页
     * @param personCode
     * @param limit
     * @param page
     * @return
     */
    @Override
    public PageInfo<Teacher> listTeacherAuthPageData(String personCode, int limit, int page,Teacher teacher,Integer type) {
        PageHelper.startPage(page,limit);
        teacher.setPersonCode(personCode);
        teacher.setStatus(type);
        List<Teacher> list = authorityManageDao.listTeacherAuthPageData(teacher);
        PageInfo<Teacher> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * @Author MaZhuli
     * @Description 获取拥有教师权限人员
     * @Date 2019/1/25 17:57
     * @Param [personCode]
     * @Return java.util.List<java.util.Map>
     **/
    @Override
    public List<Map> getTeachersAuthPersons(String personCode) throws BusinessLevelException {
        try{
            return authorityManageDao.getTeachersAuthPersons(personCode);
        }catch (RuntimeException e){
            log.error("获取拥有教师权限人员失败!");
            throw new BusinessLevelException("获取拥有教师权限人员失败",e);
        }
    }
    @Transactional
    @Override
    public OpResult copyOath(Map map) throws BusinessLevelException {
        try{
            List<Map<String, Object>> sourceList = authorityManageDao.getPersonTeacherAuth(map.get("source").toString());
            authorityManageDao.delPersonTeacherAuth(map.get("personCode").toString());
            for (Map authMap:sourceList) {
                authMap.put("pCode",map.get("personCode").toString());
                authMap.put("authType",3);
                authMap.put("createTime", DateUtil.ToDateTimeString());
            }
            if(sourceList.size()>0) {
                authorityManageDao.insertAuthDataInBatch(sourceList);
            }
            return new OpResult();
        }catch (RuntimeException e){
            log.error("复制教师权限失败!");
            throw new BusinessLevelException("复制教师权限失败!",e);
        }
    }
}
