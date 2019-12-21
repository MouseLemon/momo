package com.joysupply.entity;

/**
 * 项目信息
 *
 * @author Administrator
 */
public class Project {
    private String changeId; //变更的id
    private String projectId; //项目内部编码
    private String projectCode; //项目编号(按系统规则生成)
    private String projectName; //项目名称
    private String language; //语种
    private String depName; //项目部名称
    private String depCode;//项目部编码
    private String upCode;//部门编码
    private String projectDesc; //项目说明
    private String startTime; //开始时间
    private String endTime; //结束时间
    private int countHour; //课时总数
    private int startPersonCount; //启动人数
    private int returnFeeCount; //退费人数
    private int joinClassCount; //插班人数
    private int currentCount; //当前人数
    private String feeType; //收费方式
    private String feeHour; //学时学费
    private String feeSingle; //单人学费
    private String startFee; //启动学费
    private String addFee; //增加学费
    private String subFee; //减少学费
    private String currentIncome; //当前收入
    private String kcExpend; //课酬支出
    private String hardwareExpend; //硬件支出
    private String manageExpend; //管理支出
    private String adExpend; //广告支出
    private String agentExpend; //代理支出
    private String totalIncome; //总收益
    private String payHour; //已发课时
    private String payed; //已发课酬
    private int status; //状态
    private String projectType; //项目类型
    private String creater;  //项目 创建人
    private String personCode;  //项目 创建人code
    private String submitter;  //项目 提交人
    private String submitTime;  //项目 提交时间
    private String createTime; //创建时间
    private String opinion; //变更内容
    private String approvaler; //审批人,标识当前审批流程进行位置
    private String approvalTime; //变更时间
    private String tempCode; //变更时间

    public String getTempCode() {
        return tempCode;
    }

    public void setTempCode(String tempCode) {
        this.tempCode = tempCode;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Project() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getUpCode() {
        return upCode;
    }

    public void setUpCode(String upCode) {
        this.upCode = upCode;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getApprovaler() {
        return approvaler;
    }

    public void setApprovaler(String approvaler) {
        this.approvaler = approvaler;
    }

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCountHour() {
        return countHour;
    }

    public void setCountHour(int countHour) {
        this.countHour = countHour;
    }

    public int getStartPersonCount() {
        return startPersonCount;
    }

    public void setStartPersonCount(int startPersonCount) {
        this.startPersonCount = startPersonCount;
    }

    public int getReturnFeeCount() {
        return returnFeeCount;
    }

    public void setReturnFeeCount(int returnFeeCount) {
        this.returnFeeCount = returnFeeCount;
    }

    public int getJoinClassCount() {
        return joinClassCount;
    }

    public void setJoinClassCount(int joinClassCount) {
        this.joinClassCount = joinClassCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeHour() {
        return feeHour;
    }

    public void setFeeHour(String feeHour) {
        this.feeHour = feeHour;
    }

    public String getFeeSingle() {
        return feeSingle;
    }

    public void setFeeSingle(String feeSingle) {
        this.feeSingle = feeSingle;
    }

    public String getStartFee() {
        return startFee;
    }

    public void setStartFee(String startFee) {
        this.startFee = startFee;
    }

    public String getAddFee() {
        return addFee;
    }

    public void setAddFee(String addFee) {
        this.addFee = addFee;
    }

    public String getSubFee() {
        return subFee;
    }

    public void setSubFee(String subFee) {
        this.subFee = subFee;
    }

    public String getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(String currentIncome) {
        this.currentIncome = currentIncome;
    }

    public String getKcExpend() {
        return kcExpend;
    }

    public void setKcExpend(String kcExpend) {
        this.kcExpend = kcExpend;
    }

    public String getHardwareExpend() {
        return hardwareExpend;
    }

    public void setHardwareExpend(String hardwareExpend) {
        this.hardwareExpend = hardwareExpend;
    }

    public String getManageExpend() {
        return manageExpend;
    }

    public void setManageExpend(String manageExpend) {
        this.manageExpend = manageExpend;
    }

    public String getAdExpend() {
        return adExpend;
    }

    public void setAdExpend(String adExpend) {
        this.adExpend = adExpend;
    }

    public String getAgentExpend() {
        return agentExpend;
    }

    public void setAgentExpend(String agentExpend) {
        this.agentExpend = agentExpend;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getPayHour() {
        return payHour;
    }

    public void setPayHour(String payHour) {
        this.payHour = payHour;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Project [changeId=" + changeId + ", projectId=" + projectId + ", projectCode=" + projectCode
                + ", projectName=" + projectName + ", language=" + language + ", depName=" + depName + ", projectDesc="
                + projectDesc + ", startTime=" + startTime + ", endTime=" + endTime + ", countHour=" + countHour
                + ", startPersonCount=" + startPersonCount + ", returnFeeCount=" + returnFeeCount + ", joinClassCount="
                + joinClassCount + ", currentCount=" + currentCount + ", feeType=" + feeType + ", feeHour=" + feeHour
                + ", feeSingle=" + feeSingle + ", startFee=" + startFee + ", addFee=" + addFee + ", subFee=" + subFee
                + ", currentIncome=" + currentIncome + ", kcExpend=" + kcExpend + ", hardwareExpend=" + hardwareExpend
                + ", manageExpend=" + manageExpend + ", adExpend=" + adExpend + ", agentExpend=" + agentExpend
                + ", totalIncome=" + totalIncome + ", payHour=" + payHour + ", payed=" + payed + ", status=" + status
                + ", projectType=" + projectType + ", creater=" + creater + ", createTime=" + createTime + ", opinion="
                + opinion + ", approvaler=" + approvaler + ", approvalTime=" + approvalTime + "]";
    }


}
