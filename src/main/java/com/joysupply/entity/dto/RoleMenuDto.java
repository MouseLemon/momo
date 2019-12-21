package com.joysupply.entity.dto;

import java.util.List;

import com.joysupply.entity.RoleMenu;

/**
 * 角色菜单dto
 * @author Administrator
 *
 */
public class RoleMenuDto extends RoleMenu{
	private List<RoleMenu> roleMenuList;

	public List<RoleMenu> getRoleMenuList() {
		return roleMenuList;
	}

	public void setRoleMenuList(List<RoleMenu> roleMenuList) {
		this.roleMenuList = roleMenuList;
	}
	
}
