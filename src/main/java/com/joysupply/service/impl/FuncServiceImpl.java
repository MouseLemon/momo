package com.joysupply.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.service.FuncService;
import com.joysupply.dao.FuncDao;
@Service("funcService")
public class FuncServiceImpl extends BaseService implements FuncService {

	@Autowired
	private FuncDao funcDao;
	private Log log=LogFactory.getLog(getClass());

	/**
	 * @Author MaZhuli
	 * @Description 获取用户按钮列表
	 * @Date 2018/12/7 15:17
	 * @Param [user]
	 * @Return java.util.List<com.joysupply.entity.Func>
	 **/
	@Override
	public List<String> getFuncListByUser(SystemUser user) throws BusinessLevelException {
		try {
			return funcDao.getFuncListByUser(user);
		} catch (RuntimeException ex) {
			log.error("获取用户按钮列表失败" + ex.getMessage());
			throw new BusinessLevelException("获取用户按钮列表失败", ex);
		}
	}
	/**
	 * @Author MaZhuli
	 * @Description 获取角色按钮列表
	 * @Date 2018/12/8 9:44
	 * @Param [role]
	 * @Return java.util.List<java.lang.String>
	 **/
	@Override
	public List<String> getFuncListByRole(Role role) throws BusinessLevelException {
		try {
			return funcDao.getFuncListByRole(role);
		} catch (RuntimeException ex) {
			log.error("获取角色按钮列表失败" + ex.getMessage());
			throw new BusinessLevelException("获取角色按钮列表失败", ex);
		}
	}

	/**
	 * @Author MaZhuli
	 * @Description 获取按钮列表
	 * @Date 2018/12/7 18:40
	 * @Param []
	 * @Return java.util.List<com.joysupply.entity.Func>
	 **/
	@Override
	public List<Func> getAllFuncList() throws BusinessLevelException {
		try {
			return funcDao.getAllFuncList();
		} catch (RuntimeException ex) {
			log.error("获取按钮列表失败" + ex.getMessage());
			throw new BusinessLevelException("获取按钮列表失败", ex);
		}
	}
	/**
	 * @Author MaZhuli
	 * @Description 根据菜单获取按钮
	 * @Date 2018/12/8 15:41
	 * @Param [menuCode]
	 * @Return java.util.List<com.joysupply.entity.Func>
	 **/
	@Override
	public List<Func> getFuncListByMenu(Menu menu) {
		try {
			return funcDao.getFuncListByMenu(menu);
		} catch (RuntimeException ex) {
			log.error("根据菜单获取按钮失败" + ex.getMessage());
			throw new BusinessLevelException("根据菜单获取按钮失败", ex);
		}
	}


}
