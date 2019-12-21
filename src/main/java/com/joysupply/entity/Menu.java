package com.joysupply.entity;

import java.util.List;

/**
 * 菜单
 *
 * @author Administrator
 */
public class Menu {
    private String menuCode;
    private String menuName;
    private String menuUrl;
    private String indexUrl;
    private int isEnable;
    private String icon;
    private String parentCode;
    private Integer isSelected;
    private List<Func> funcList;
    private Integer allFuncSelected;

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public Integer getAllFuncSelected() {
        return allFuncSelected;
    }

    public void setAllFuncSelected(Integer allFuncSelected) {
        this.allFuncSelected = allFuncSelected;
    }

    public List<Func> getFuncList() {
        return funcList;
    }

    public void setFuncList(List<Func> funcList) {
        this.funcList = funcList;
    }

    public Integer getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }


}
