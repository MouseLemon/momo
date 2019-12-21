package com.joysupply.entity;

/**
 * 项目排课计划
 * @author Administrator
 *
 */
public class ProjectSchedulePlan {
	private String schedule_id; // id
	private String project_id; // 项目内部编码
	private String course_name; // 课程名称
	private String teacher_code; // 教师编码
	private String grade_id; // 班级编码
	private String class_room_type; // 教室类型
	private String class_date; // 上课日期
	private String start_time; // 开始时间
	private String end_time; // 结束时间
	private String base_fee; // 基础课酬
	private String person_factor; // 人数系数
	private String course_factor; // 课程系数
	private Integer person_count; // 人数
	private String fee_total; // 课酬合计
	private String year; // 年
	private String week; // 周
	private Integer status; // 0 未提交 1 提交 -1 教研室排课退回
	private String class_room_id; // 教室编号
	private String schedule_type; // 排课类型 1 项目排课 2 临时排课
	private String add_fee_count;
	private String week_day; // 星期

	@Override
	public String toString() {
		return "ProjectSchedulePlan{" +
				"schedule_id='" + schedule_id + '\'' +
				", project_id='" + project_id + '\'' +
				", course_name='" + course_name + '\'' +
				", teacher_code='" + teacher_code + '\'' +
				", grade_id='" + grade_id + '\'' +
				", class_room_type='" + class_room_type + '\'' +
				", class_date='" + class_date + '\'' +
				", start_time='" + start_time + '\'' +
				", end_time='" + end_time + '\'' +
				", base_fee='" + base_fee + '\'' +
				", person_factor='" + person_factor + '\'' +
				", course_factor='" + course_factor + '\'' +
				", person_count=" + person_count +
				", fee_total='" + fee_total + '\'' +
				", year='" + year + '\'' +
				", week='" + week + '\'' +
				", status=" + status +
				", class_room_id='" + class_room_id + '\'' +
				", schedule_type='" + schedule_type + '\'' +
				", add_fee_count='" + add_fee_count + '\'' +
				", week_day='" + week_day + '\'' +
				'}';
	}

	public String getSchedule_id() {
		return schedule_id;
	}

	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getTeacher_code() {
		return teacher_code;
	}

	public void setTeacher_code(String teacher_code) {
		this.teacher_code = teacher_code;
	}

	public String getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}

	public String getClass_room_type() {
		return class_room_type;
	}

	public void setClass_room_type(String class_room_type) {
		this.class_room_type = class_room_type;
	}

	public String getClass_date() {
		return class_date;
	}

	public void setClass_date(String class_date) {
		this.class_date = class_date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getBase_fee() {
		return base_fee;
	}

	public void setBase_fee(String base_fee) {
		this.base_fee = base_fee;
	}

	public String getPerson_factor() {
		return person_factor;
	}

	public void setPerson_factor(String person_factor) {
		this.person_factor = person_factor;
	}

	public String getCourse_factor() {
		return course_factor;
	}

	public void setCourse_factor(String course_factor) {
		this.course_factor = course_factor;
	}

	public Integer getPerson_count() {
		return person_count;
	}

	public void setPerson_count(Integer person_count) {
		this.person_count = person_count;
	}

	public String getFee_total() {
		return fee_total;
	}

	public void setFee_total(String fee_total) {
		this.fee_total = fee_total;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getClass_room_id() {
		return class_room_id;
	}

	public void setClass_room_id(String class_room_id) {
		this.class_room_id = class_room_id;
	}

	public String getSchedule_type() {
		return schedule_type;
	}

	public void setSchedule_type(String schedule_type) {
		this.schedule_type = schedule_type;
	}

	public String getAdd_fee_count() {
		return add_fee_count;
	}

	public void setAdd_fee_count(String add_fee_count) {
		this.add_fee_count = add_fee_count;
	}

	public String getWeek_day() {
		return week_day;
	}

	public void setWeek_day(String week_day) {
		this.week_day = week_day;
	}
}
