package com.joysupply.entity;

/**
 * @ClassName TeachResearchRoom
 * @Author MaZhuli
 * @Date 2018/11/3 11:43
 * @Description 教研室
 * @Version 1.0
 **/
public class TeachResearchRoom {
    private String officeCode;
    private String officeName;
    private String creater;
    private String createDate;

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TeachResearchRoom[" +
                "officeCode='" + officeCode + '\'' +
                ", officeName='" + officeName + '\'' +
                ", creater='" + creater + '\'' +
                ", createDate='" + createDate + '\'' +
                ']';
    }
}

