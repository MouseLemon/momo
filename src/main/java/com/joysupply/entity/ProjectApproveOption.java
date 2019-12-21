package com.joysupply.entity;

public class ProjectApproveOption {

    private String rowId;
    private String projectId;
    private String changeId;
    private String personCode;
    private String approveDateTime;
    private String approveResult;
    private String approveOption;
    private Integer approveOrder;
    private Integer submitType;

    public ProjectApproveOption() {
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getApproveDateTime() {
        return approveDateTime;
    }

    public void setApproveDateTime(String approveDateTime) {
        this.approveDateTime = approveDateTime;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public String getApproveOption() {
        return approveOption;
    }

    public void setApproveOption(String approveOption) {
        this.approveOption = approveOption;
    }

    public Integer getApproveOrder() {
        return approveOrder;
    }

    public void setApproveOrder(Integer approveOrder) {
        this.approveOrder = approveOrder;
    }

    public Integer getSubmitType() {
        return submitType;
    }

    public void setSubmitType(Integer submitType) {
        this.submitType = submitType;
    }

    @Override
    public String toString() {
        return "ProjectApproveOption[" +
                "rowId='" + rowId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", changeId='" + changeId + '\'' +
                ", personCode='" + personCode + '\'' +
                ", approveDateTime='" + approveDateTime + '\'' +
                ", approveResult='" + approveResult + '\'' +
                ", approveOption='" + approveOption + '\'' +
                ", approveOrder=" + approveOrder +
                ", submitType=" + submitType +
                ']';
    }
}
