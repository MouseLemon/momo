package com.joysupply.entity;

public class CourseFee {
    private String rowId;
    private String startTime;
    private String endTime;
    private String projectId;
    private String teacherCode;
    private String courseId;
    private String language;
    private String baseFee;
    private Short hourActual;
    private String personFactor;
    private String courseFactor;
    private String feeCourse;
    private String serialNumber;
    private String year;
    private String month;
    private Byte isSend;
    private String feeType;
    private String status;
    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId == null ? null : rowId.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode == null ? null : teacherCode.trim();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId == null ? null : courseId.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee == null ? null : baseFee.trim();
    }

    public Short getHourActual() {
        return hourActual;
    }

    public void setHourActual(Short hourActual) {
        this.hourActual = hourActual;
    }

    public String getPersonFactor() {
        return personFactor;
    }

    public void setPersonFactor(String personFactor) {
        this.personFactor = personFactor == null ? null : personFactor.trim();
    }

    public String getCourseFactor() {
        return courseFactor;
    }

    public void setCourseFactor(String courseFactor) {
        this.courseFactor = courseFactor == null ? null : courseFactor.trim();
    }

    public String getFeeCourse() {
        return feeCourse;
    }

    public void setFeeCourse(String feeCourse) {
        this.feeCourse = feeCourse == null ? null : feeCourse.trim();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public Byte getIsSend() {
        return isSend;
    }

    public void setIsSend(Byte isSend) {
        this.isSend = isSend;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}