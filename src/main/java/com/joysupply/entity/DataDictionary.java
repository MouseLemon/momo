package com.joysupply.entity;

/**
 * 数据字典
 *
 * @author Administrator
 */
public class DataDictionary {
    private String code;
    private String name;
    private String parentCode;
    private String note;
    private String createTime;
    private Integer isfixed;

    public Integer getIsfixed() {
        return isfixed;
    }

    public void setIsfixed(Integer isfixed) {
        this.isfixed = isfixed;
    }

    public DataDictionary() {

    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "DataDictionary [code=" + code + ", name=" + name + ", parentCode=" + parentCode + ",note=" + note + "]";
    }

}
