package com.joysupply.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.joysupply.entity.SystemUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import com.joysupply.entity.Menu;
import com.joysupply.entity.MenuFunc;
import com.joysupply.entity.RoleMenu;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.MenuService;
import com.joysupply.util.OpResult;
import com.joysupply.dao.MenuDao;
@Service("menuService")
public class MenuServiceImpl extends BaseService implements MenuService {

	@Autowired
	private MenuDao menuDao;
	private Log log=LogFactory.getLog(getClass());
	/**
	 * @Author MaZhuli
	 * @Description 获取用户菜单列表
	 * @Date 2018/11/27 17:39
	 * @Param [user]
	 * @Return java.util.List<com.joysupply.entity.Menu>
	 **/
	@Override
	public List<Menu> getMenuListByUser(SystemUser user) {
		try {
			List<Menu> menuList = new ArrayList<>();
			if (user.getUserType().equals("13")) {
				menuList = menuDao.getMenuListByTeacher(user);
			} else {
				menuList = menuDao.getMenuListByUser(user);
			}
			return menuList;
		}catch (RuntimeException ex){
			log.error("获取用户菜单列表失败"+ex.getMessage());
			throw new BusinessLevelException("获取用户菜单列表失败",ex);
		}
	}
	/**
	 * @Author MaZhuli
	 * @Description 获取所有菜单列表
	 * @Date 2018/11/27 17:39
	 * @Param []
	 * @Return java.util.List<com.joysupply.entity.Menu>
	 **/
	@Override
	public List<Menu> getAllMenus() throws BusinessLevelException {
        try {
            return menuDao.getAllMenus();
        }catch (RuntimeException ex){
            log.error("获取所有菜单列表失败"+ex.getMessage());
            throw new BusinessLevelException("获取所有菜单列表失败",ex);
        }
	}
}
