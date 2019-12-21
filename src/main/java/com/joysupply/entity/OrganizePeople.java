package com.joysupply.entity;
/**
 * 组织人员表
 * @author Administrator
 *
 */
public class OrganizePeople {
	
	private String personCode;
	private String name;
	private int sex;
	private String organizeCode;
	private String jobType;
	private int inService;
	private String telphone;
	private String createTime;
	public OrganizePeople() {}
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
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
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
	public int getInService() {
		return inService;
	}
	public void setInService(int inService) {
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
		return "OrganizePeople [personCode=" + personCode + ", name=" + name + ", sex=" + sex + ", organizeCode="
				+ organizeCode + ", jobType=" + jobType + ", inService=" + inService + ", telphone=" + telphone
				+ ", createTime=" + createTime + "]";
	}
	
	
}
