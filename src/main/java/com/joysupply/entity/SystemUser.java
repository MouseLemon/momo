package com.joysupply.entity;

import java.util.List;
import java.util.Map;

public class SystemUser {
    private String personCode;
    private String password;
    private String userName;
    private String isEnable;
    private String userType;
    private String url;
    private Integer msgCount;
    private List<Map<String,Object>> msgList;

    public List<Map<String,Object>> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Map<String,Object>> msgList) {
        this.msgList = msgList;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SystemUser() {
        super();
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
