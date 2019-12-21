package com.joysupply.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.FuncDao;
import com.joysupply.entity.Func;
import com.joysupply.util.PageHelperUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.joysupply.entity.Role;
import com.joysupply.entity.RoleMenu;
import com.joysupply.entity.dto.RoleMenuDto;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.RoleService;
import com.joysupply.util.Constants;
import com.joysupply.util.OpResult;
import com.joysupply.dao.RoleDao;

import javax.swing.plaf.nimbus.NimbusStyle;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private FuncDao funcDao;

    /**
     * 查询角色列表
     */
    @Override
    public List<Role> getRoleList() {
        List<Role> result = new ArrayList<Role>();
        result = roleDao.getRoleList();
        return result;
    }

    /**
     * @Author MaZhuli
     * @Description 查询人员角色列表
     * @Date 2018/11/19 13:52
     * @Param [personCode]
     * @Return java.util.List<java.util.Map       <       java.lang.String       ,       java.lang.Object>>
     **/
    @Override
    public List<String> getPersonRoles(String personCode) throws BusinessLevelException {
        try {
            return roleDao.getPersonRoles(personCode);
        } catch (RuntimeException e) {
            log.error("查询人员角色列表:" + e.getMessage());
            throw new BusinessLevelException("查询人员角色列表", e);
        }
    }

    @Override
    public Map<String, Object> getRoleListInPage(Map map) throws BusinessLevelException {
        List<Role> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        try {
            list = roleDao.getRoleList();
        } catch (RuntimeException ex) {
            log.error("获取角色列表失败:" + ex.getMessage());
            throw new BusinessLevelException("获取角色列表失败", ex);
        }
        com.github.pagehelper.PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    @Override
    public OpResult addRole(Role role) throws BusinessLevelException {
        try {
            role.setRoleCode(Constants.GUID());
            roleDao.addRole(role);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("获取角色列表失败:" + ex.getMessage());
            throw new BusinessLevelException("获取角色列表失败", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取角色
     * @Date 2018/11/27 15:59
     * @Param [role]
     * @Return com.joysupply.entity.Role
     **/
    @Override
    public Role getRole(Role role) throws BusinessLevelException {
        try {
            return roleDao.getRole(role);
        } catch (RuntimeException ex) {
            log.error("获取角色失败:" + ex.getMessage());
            throw new BusinessLevelException("获取角色失败", ex);
        }
    }

    public OpResult updRole(Role role) throws RuntimeException {
        try {
            roleDao.updRole(role);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("删除角色失败" + ex.getMessage());
            throw new BusinessLevelException("删除角色失败", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除角色
     * @Date 2018/11/27 16:07
     * @Param [role]
     * @Return com.joysupply.util.OpResult
     **/
    public OpResult delRole(Role role) throws RuntimeException {
        try {
            roleDao.delRole(role);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("删除角色失败" + ex.getMessage());
            throw new BusinessLevelException("删除角色失败", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取角色菜单
     * @Date 2018/11/27 17:25
     * @Param [role]
     * @Return java.util.List<com.joysupply.entity.RoleMenu>
     **/
    @Override
    public List<String> getRoleMenus(Role role) throws RuntimeException {
        try {
            return roleDao.getRoleMenus(role);
        } catch (RuntimeException ex) {
            log.error("获取角色菜单失败" + ex.getMessage());
            throw new BusinessLevelException("获取角色菜单失败", ex);
        }
    }

    /**
     * 根据角色编辑菜单
     */
    @Transactional
    @Override
    public OpResult updateMenuByRole(Map map) throws BusinessLevelException {
        try {
            String roleCode = map.get("roleCode").toString();
            List<String> menuList = (List<String>) map.get("menuList");
            List<String> funcList = (List<String>) map.get("funcList");
            //删除原角色的菜单
            roleDao.deleteRoleMenuByRoleCode(roleCode);
            RoleMenuDto roleMenuDto = new RoleMenuDto();
            if (menuList.size() > 0) {
                List<RoleMenu> list = new ArrayList<>();
                for (String menuCode : menuList) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setMenuCode(menuCode);
                    roleMenu.setRoleCode(roleCode);
                    list.add(roleMenu);
                }
                roleMenuDto.setRoleMenuList(list);
                roleDao.saveRoleMenu(roleMenuDto);
            }
            funcDao.delFuncByRoleCode(roleCode);
            if(funcList.size()>0){
                List<Func> list = new ArrayList<>();
                for(String funcCode: funcList){
                    Func func = new Func();
                    func.setRoleCode(roleCode);
                    func.setFuncCode(funcCode);
                    list.add(func);
                }
                funcDao.saveRoleFunc(list);
            }
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new BusinessLevelException("根据角色编辑菜单失败", ex);
        }
    }

}
