package com.joysupply.entity;

public class Student {
	
	private String studentCode;
	private String name;
	private String englishName;
	private String serial; //学号
	private String sex; 
	private String idcard; //身份证号
	private String telephone;
	private String pic;
	private String status;
	private String createTime;
	private String projectIds;
	private String ishave;
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getIshave() {
		return ishave;
	}
	public void setIshave(String ishave) {
		this.ishave = ishave;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public String getProjectIds() {
		return projectIds;
	}
	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Student{" +
				"studentCode='" + studentCode + '\'' +
				", name='" + name + '\'' +
				", englishName='" + englishName + '\'' +
				", serial='" + serial + '\'' +
				", sex='" + sex + '\'' +
				", idcard='" + idcard + '\'' +
				", telephone='" + telephone + '\'' +
				", pic='" + pic + '\'' +
				", status='" + status + '\'' +
				", createTime='" + createTime + '\'' +
				", projectIds='" + projectIds + '\'' +
				", ishave='" + ishave + '\'' +
				'}';
	}
}
