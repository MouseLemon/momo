package com.joysupply.entity;

import java.io.Serializable;
import java.util.List;

public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    private String teacherCode;
    private String personCode;
    private String teacherName;
    private String teleNum;
    private Integer sex;  // 1 男  2 女
    private String teacherType;
    private String degree;
    private String edu;
    private String email;
    private String language;
    private String job;
    private String birthDay;
    private String entryDate;
    private String researchOffice;
    private String accountName;
    private String bankCode;
    private String idCard;
    private String pic;
    private String resume;
    private Integer status;  // 1 正常  2 禁用
    private String nationality;
    private String createTime;
    private String cardNo;
    private String project;
    private String resumeName;
    private String userName;
    private String isEnable;
    private String picName;
    private String joinTime;
    private String updateTime;
    private String prvOutDate;
    private List<String> listTeacherAuth;

    public void setListTeacherAuth(List<String> listTeacherAuth) {
        this.listTeacherAuth = listTeacherAuth;
    }

    public List<String> getListTeacherAuth() {

        return listTeacherAuth;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getJoinTime() {

        return joinTime;
    }
    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getPersonCode() {

        return personCode;
    }
    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicName() {

        return picName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeleNum() {
        return teleNum;
    }

    public void setTeleNum(String teleNum) {
        this.teleNum = teleNum;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(String teacherType) {
        this.teacherType = teacherType;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getResearchOffice() {
        return researchOffice;
    }

    public void setResearchOffice(String researchOffice) {
        this.researchOffice = researchOffice;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getPrvOutDate() {
        return prvOutDate;
    }

    public void setPrvOutDate(String prvOutDate) {
        this.prvOutDate = prvOutDate;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
