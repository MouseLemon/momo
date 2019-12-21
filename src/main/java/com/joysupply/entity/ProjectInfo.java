package com.joysupply.entity;

/**
 * @Author：WangHao
 * @Create：2018-11-08 10:02
 * @Description：项目信息
 * @Program：joysupply-byoa
 * @Version：1.0
 **/

public class ProjectInfo {
    private String project_id; // 项目内部编码
    private String project_code; // 项目编号(按系统规则生成)
    private String project_name; // 项目名称
    private String language; // 语种
    private String dep_name; // 项目部名称
    private String project_desc; // 项目说明
    private String start_time; // 开始时间
    private String end_time; // 结束时间
    private Integer count_hour; // 课时总数
    private Integer start_person_count; // 启动人数
    private Integer return_fee_count; // 退费人数
    private Integer join_class_count; // 插班人数
    private Integer current_count; // 当前人数
    private String fee_type; // 收费方式 1-按人数收费  2-按学时收费
    private String fee_hour; // 学时学费
    private String fee_single; // 单人学费
    private String start_fee; // 启动学费
    private String add_fee; // 增加学费
    private String sub_fee; // 减少学费
    private String current_income; // 当前收入
    private String kc_expend; // 课酬支出
    private String hardware_expend; // 硬件支出
    private String manage_expend; // 管理支出
    private String ad_expend; // 广告支出
    private String agent_expend; // 代理支出
    private String total_income; // 总收益
    private String pay_hour; // 已发课酬课时
    private String payed; // 已发课酬
    private Integer status; // 项目状态 1未提交 2待审核 3已审批 4已驳回
    private String project_type; // 项目类型
    private String creater; // 创建人
    private String create_time; // 创建时间

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "project_id='" + project_id + '\'' +
                ", project_code='" + project_code + '\'' +
                ", project_name='" + project_name + '\'' +
                ", language='" + language + '\'' +
                ", dep_name='" + dep_name + '\'' +
                ", project_desc='" + project_desc + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", count_hour=" + count_hour +
                ", start_person_count=" + start_person_count +
                ", return_fee_count=" + return_fee_count +
                ", join_class_count=" + join_class_count +
                ", current_count=" + current_count +
                ", fee_type='" + fee_type + '\'' +
                ", fee_hour='" + fee_hour + '\'' +
                ", fee_single='" + fee_single + '\'' +
                ", start_fee='" + start_fee + '\'' +
                ", add_fee='" + add_fee + '\'' +
                ", sub_fee='" + sub_fee + '\'' +
                ", current_income='" + current_income + '\'' +
                ", kc_expend='" + kc_expend + '\'' +
                ", hardware_expend='" + hardware_expend + '\'' +
                ", manage_expend='" + manage_expend + '\'' +
                ", ad_expend='" + ad_expend + '\'' +
                ", agent_expend='" + agent_expend + '\'' +
                ", total_income='" + total_income + '\'' +
                ", pay_hour='" + pay_hour + '\'' +
                ", payed='" + payed + '\'' +
                ", status=" + status +
                ", project_type='" + project_type + '\'' +
                ", creater='" + creater + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getProject_desc() {
        return project_desc;
    }

    public void setProject_desc(String project_desc) {
        this.project_desc = project_desc;
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

    public Integer getCount_hour() {
        return count_hour;
    }

    public void setCount_hour(Integer count_hour) {
        this.count_hour = count_hour;
    }

    public Integer getStart_person_count() {
        return start_person_count;
    }

    public void setStart_person_count(Integer start_person_count) {
        this.start_person_count = start_person_count;
    }

    public Integer getReturn_fee_count() {
        return return_fee_count;
    }

    public void setReturn_fee_count(Integer return_fee_count) {
        this.return_fee_count = return_fee_count;
    }

    public Integer getJoin_class_count() {
        return join_class_count;
    }

    public void setJoin_class_count(Integer join_class_count) {
        this.join_class_count = join_class_count;
    }

    public Integer getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(Integer current_count) {
        this.current_count = current_count;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getFee_hour() {
        return fee_hour;
    }

    public void setFee_hour(String fee_hour) {
        this.fee_hour = fee_hour;
    }

    public String getFee_single() {
        return fee_single;
    }

    public void setFee_single(String fee_single) {
        this.fee_single = fee_single;
    }

    public String getStart_fee() {
        return start_fee;
    }

    public void setStart_fee(String start_fee) {
        this.start_fee = start_fee;
    }

    public String getAdd_fee() {
        return add_fee;
    }

    public void setAdd_fee(String add_fee) {
        this.add_fee = add_fee;
    }

    public String getSub_fee() {
        return sub_fee;
    }

    public void setSub_fee(String sub_fee) {
        this.sub_fee = sub_fee;
    }

    public String getCurrent_income() {
        return current_income;
    }

    public void setCurrent_income(String current_income) {
        this.current_income = current_income;
    }

    public String getKc_expend() {
        return kc_expend;
    }

    public void setKc_expend(String kc_expend) {
        this.kc_expend = kc_expend;
    }

    public String getHardware_expend() {
        return hardware_expend;
    }

    public void setHardware_expend(String hardware_expend) {
        this.hardware_expend = hardware_expend;
    }

    public String getManage_expend() {
        return manage_expend;
    }

    public void setManage_expend(String manage_expend) {
        this.manage_expend = manage_expend;
    }

    public String getAd_expend() {
        return ad_expend;
    }

    public void setAd_expend(String ad_expend) {
        this.ad_expend = ad_expend;
    }

    public String getAgent_expend() {
        return agent_expend;
    }

    public void setAgent_expend(String agent_expend) {
        this.agent_expend = agent_expend;
    }

    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public String getPay_hour() {
        return pay_hour;
    }

    public void setPay_hour(String pay_hour) {
        this.pay_hour = pay_hour;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
