package com.joysupply.entity;

/**
 * 岗位
 * @author Administrator
 *
 */
public class JobType {
	private String jobCode;
	private String jobName;
	public JobType(){
		
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	@Override
	public String toString() {
		return "JobType [jobCode=" + jobCode + ", jobName=" + jobName + "]";
	}
	
}
