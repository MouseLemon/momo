package com.joysupply.entity;

/**
 * @Author：WangHao
 * @Create：2018-11-05 08:45
 * @Description：时间设置
 * @Program：joysupply-byoa
 * @Version：1.0
 **/

public class TimeSheet {
    private String step;
    private String section;
    private String start_time;
    private String end_time;
    private String year;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TimeSheet{" +
                "step='" + step + '\'' +
                ", section='" + section + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
