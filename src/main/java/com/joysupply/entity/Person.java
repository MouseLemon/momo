package com.joysupply.entity;

public class Person {

    private String personCode;
    private String name;
    private String sex;
    private String organizeCode;
    private String jobType;
    private Integer inService;
    private String telphone;
    private String createTime;

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOrganizeCode() {
        return organizeCode;
    }

    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getInService() {
        return inService;
    }

    public void setInService(Integer inService) {
        this.inService = inService;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Person[" +
                "personCode='" + personCode + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", organizeCode='" + organizeCode + '\'' +
                ", jobType='" + jobType + '\'' +
                ", inService=" + inService +
                ", telphone='" + telphone + '\'' +
                ", createTime='" + createTime + '\'' +
                ']';
    }
}
