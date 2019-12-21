package com.joysupply.service.impl;

import com.joysupply.dao.*;
import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.OrganizePeopleService;
import com.joysupply.util.OpResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("organizePeopleServer")
@Transactional
public class OrganizePeopleServiceImpl implements OrganizePeopleService {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private OrganizePeopleDao organizePeopleDao;
    @Autowired
    private AuthorityManageDao authorityManageDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private SystemUserDao systemUserDao;
    @Autowired
    private MenuDao menuDao;


    /**
     * @Author MaZhuli
     * @Description 添加人员
     * @Date 2018/11/2 18:25
     * @Param [organizePeople]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult addOrganizePeople(OrganizePeople organizePeople, String[] roleArr) throws BusinessLevelException {
        try {
            organizePeopleDao.addOrganizePeople(organizePeople);
            SystemUser user = new SystemUser();
            user.setPersonCode(organizePeople.getPersonCode());
            user.setPassword("a123123");
            user.setUserName(organizePeople.getTelphone());
            user.setIsEnable("1");
            user.setUserType("1");
            systemUserDao.addSystemUser(user);
            List<Map<String, Object>> personRoleList = new ArrayList<>();
            for (int i = 0; i < roleArr.length; i++) {
                Map map = new HashMap();
                map.put("personCode", organizePeople.getPersonCode());
                map.put("roleCode", roleArr[i]);
                personRoleList.add(map);
            }
            if (personRoleList.size() > 0) {
                roleDao.savePersonRole(personRoleList);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("添加人员:" + e.getMessage());
            throw new BusinessLevelException("添加人员", e);
        }
    }

    @Override
    @Transactional
    public OpResult updOrganizePeople(OrganizePeople organizePeople, String[] roleArr) throws BusinessLevelException {
        try {
            organizePeopleDao.updOrganizePeople(organizePeople);
            roleDao.delPersonRole(organizePeople.getPersonCode());
            List<Map<String, Object>> personRoleList = new ArrayList<>();
            for (int i = 0; i < roleArr.length; i++) {
                Map map = new HashMap();
                map.put("personCode", organizePeople.getPersonCode());
                map.put("roleCode", roleArr[i]);
                personRoleList.add(map);
            }
            if (personRoleList.size() > 0) {
                roleDao.savePersonRole(personRoleList);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("删除人员:" + e.getMessage());
            throw new BusinessLevelException("删除人员", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除人员
     * @Date 2018/11/2 18:36
     * @Param [organizeStruct]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult delOrganizePeople(OrganizePeople organizePeople) throws BusinessLevelException {
        try {
            organizePeopleDao.delOrganizePeople(organizePeople);
            roleDao.delPersonRole(organizePeople.getPersonCode());
            systemUserDao.delSystemUser(organizePeople.getPersonCode());
            authorityManageDao.delPersonAuth(organizePeople.getPersonCode());
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("删除人员:" + e.getMessage());
            throw new BusinessLevelException("删除人员", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 根据personCode获取人员信息
     * @Date 2018/11/2 19:28
     * @Param [map]
     * @Return com.joysupply.entity.OrganizePeople
     **/
    @Override
    public OrganizePeople getOrganizePeople(String personCode) throws BusinessLevelException {
        try {
            return organizePeopleDao.getOrganizePeople(personCode);
        } catch (RuntimeException e) {
            log.error("根据personCode获取人员信息:" + e.getMessage());
            throw new BusinessLevelException("根据personCode获取人员信息", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取部门下所有人员
     * @Date 2018/11/9 14:10
     * @Param [organizeStruct]
     * @Return java.util.List<com.joysupply.entity.OrganizePeople>
     **/
    @Override
    public List<OrganizePeople> getOrganizePeopleListNoPage(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            return organizePeopleDao.getOrganizePeopleListNoPage(organizeStruct);
        } catch (RuntimeException e) {
            log.error("获取部门下所有人员" + e.getMessage());
            throw new BusinessLevelException("获取部门下所有人员", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取部门下审核人员列表
     * @Date 2018/11/19 14:27
     * @Param [organizeStruct]
     * @Return java.util.List<com.joysupply.entity.OrganizePeople>
     **/
    @Override
    public List<OrganizePeople> getOrganizePeopleHaveAuditRole(Map map) throws BusinessLevelException {
        try {
            return organizePeopleDao.getOrganizePeopleHaveAuditRole(map);
        } catch (RuntimeException e) {
            log.error("获取部门下审核人员列表" + e.getMessage());
            throw new BusinessLevelException("获取部门下审核人员列表失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 判断手机号是否存在
     * @Date 2018/11/30 15:04
     * @Param [telephone]
     * @Return boolean
     **/
    @Override
    public boolean isTelephoneExist(String telphone) throws BusinessLevelException {
        try {
            OrganizePeople organizePeople = new OrganizePeople();
            organizePeople.setTelphone(telphone);
            long isTelephoneExist = organizePeopleDao.isTelephoneExist(organizePeople);
            if (isTelephoneExist > 0) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException ex) {
            throw new BusinessLevelException("判断手机号是否存在失败", ex);
        }
    }

    /**
     * WangYuelLei
     *
     * @param telphone
     * @return
     * @throws BusinessLevelException
     * @Description 通过手机号查询是否有手机号
     */
    @Override
    public OrganizePeople getOrganizePeopleByTelephone(String telphone) throws BusinessLevelException {
        try {
            return organizePeopleDao.getOrganizePeopleByTelephone(telphone);
        } catch (RuntimeException ex) {
            log.error("通过手机号查询信息" + ex.getMessage());
            throw new BusinessLevelException("通过手机号获取人员", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取用户所有可用菜单
     * @Date 2018/12/13 9:22
     * @Param [user]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    @Override
    public List<Menu> getAllShortCutMenusByUser(SystemUser user) throws BusinessLevelException {
        try {
            List<Menu> menuListByUser = new ArrayList<>();
            if (user.getUserType() != null && user.getUserType().equals("13")) {
                menuListByUser = menuDao.getMenuListByTeacher(user);
            } else {
                menuListByUser = menuDao.getMenuListByUser(user);
            }
            List<Menu> list = new ArrayList<>();
            for (Menu menu : menuListByUser) {
                if (menu.getParentCode() != null && !menu.getParentCode().equals("") && menu.getMenuUrl() != null && !menu.getMenuUrl().equals("")) {
                    list.add(menu);
                }
            }
            return list;
        } catch (RuntimeException ex) {
            log.error("获取用户所有可用菜单失败" + ex.getMessage());
            throw new BusinessLevelException("获取用户所有可用菜单失败!", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取用户当前可用菜单
     * @Date 2018/12/13 9:24
     * @Param [user]
     * @Return java.util.List<com.joysupply.entity.Menu>
     **/
    @Override
    public List<Menu> getShortCutMenuByUser(SystemUser user) throws BusinessLevelException {
        try {
            List<Menu> shortCutMenuByUser = menuDao.getShortCutMenuByUser(user);
            Iterator iterator = shortCutMenuByUser.iterator();
            while (iterator.hasNext()) {
                Menu menu = (Menu) iterator.next();
                String menuIndexUrl = menuDao.getMenuIndexUrl(menu);
                menu.setIndexUrl(menuIndexUrl);
            }
            return shortCutMenuByUser;
        } catch (RuntimeException ex) {
            log.error("获取用户当前可用菜单失败" + ex.getMessage());
            throw new BusinessLevelException("获取用户当前可用菜单失败!", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 更新快捷菜单
     * @Date 2018/12/13 14:57
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    @Transactional
    public OpResult updShortCutMenus(Map map) throws BusinessLevelException {
        try {
            menuDao.delShortCutMenuByUser(map);
            List<String> menuCodeList = (List<String>) map.get("shortCutList");
            List<Map<String, Object>> list = new ArrayList<>();
            if (menuCodeList != null && menuCodeList.size() > 0) {
                int showOrder = 1;
                for (String item : menuCodeList) {
                    Map shortCutMap = new HashMap();
                    shortCutMap.put("showOrder", showOrder);
                    shortCutMap.put("menuCode", item);
                    list.add(shortCutMap);
                    showOrder++;
                }
                map.put("shortCutList", list);
                menuDao.addShortCutMenuByUser(map);
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("获取用户当前可用菜单失败" + ex.getMessage());
            throw new BusinessLevelException("获取用户当前可用菜单失败!", ex);
        }
    }
    /**
     * 获取person表中用户信息
     */
	@Override
	public List<Map<String, Object>> getAllPersonUser() {
		return organizePeopleDao.getAllPersonUser();
	}

    @Override
    public int getSystemUserByTel(String telphone) {
        return organizePeopleDao.getSystemUserByTel(telphone);
    }
}
