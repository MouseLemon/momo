package com.joysupply.entity;

/**
 * 组织结构
 *
 * @author Administrator
 */
public class OrganizeStruct {
    private String organizeCode;
    private String organizeName;
    private String organizeType;
    private String upCode;
    private String companyCode;
    private String leader;

    public OrganizeStruct() {
    }

    public String getOrganizeCode() {
        return organizeCode;
    }

    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getOrganizeType() {
        return organizeType;
    }

    public void setOrganizeType(String organizeType) {
        this.organizeType = organizeType;
    }

    public String getUpCode() {
        return upCode;
    }

    public void setUpCode(String upCode) {
        this.upCode = upCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Override
    public String toString() {
        return "OrganizeStruct{" +
                "organizeCode='" + organizeCode + '\'' +
                ", organizeName='" + organizeName + '\'' +
                ", organizeType='" + organizeType + '\'' +
                ", upCode='" + upCode + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", leader='" + leader + '\'' +
                '}';
    }
}
