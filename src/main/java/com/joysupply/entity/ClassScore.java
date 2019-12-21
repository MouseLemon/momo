package com.joysupply.entity;

public class ClassScore {
    private String projectId; //项目code
    private String studentCode; //学生code
    private String courseName; //课程名字
    private String score; //学生成绩
    private String attendRate; //出勤率
    private String type; //百分五分
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public ClassScore() {}
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
}
