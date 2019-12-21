package com.joysupply.entity;

/**
 * 菜单功能
 * @author Administrator
 *
 */
public class MenuFunc {
	private String menuCode;
	private String funcCode;
	private String funcUrl;
	private String funcName;
	private String funcIcon;
	private Integer isEnable;
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getFuncUrl() {
		return funcUrl;
	}
	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getFuncIcon() {
		return funcIcon;
	}
	public void setFuncIcon(String funcIcon) {
		this.funcIcon = funcIcon;
	}
	public Integer getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	
}
