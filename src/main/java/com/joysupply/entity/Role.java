package com.joysupply.entity;

/**
 * 角色
 *
 * @author Administrator
 */
public class Role {
    private String roleCode;
    private String roleName;
    private String upRole;
    private Integer isFixed;

    public Integer getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Integer isFixed) {
        this.isFixed = isFixed;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUpRole() {
        return upRole;
    }

    public void setUpRole(String upRole) {
        this.upRole = upRole;
    }

}
