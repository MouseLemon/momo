package com.joysupply.entity;

public class CourseScore {
	
	private String projectId;
	private String studentCode;
	private String courseName; //课程名称
	private String score; //分数
	private String attendRate; //出勤率
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getAttendRate() {
		return attendRate;
	}
	public void setAttendRate(String attendRate) {
		this.attendRate = attendRate;
	}
	@Override
	public String toString() {
		return "CourseScore [projectId=" + projectId + ", studentCode=" + studentCode + ", courseName=" + courseName
				+ ", score=" + score + ", attendRate=" + attendRate + "]";
	}
}
