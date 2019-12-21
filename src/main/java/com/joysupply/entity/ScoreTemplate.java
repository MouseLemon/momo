package com.joysupply.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 证书模板实体类
 */
public class ScoreTemplate implements Serializable {

    private String templateId;
    private String templateType;
    private String createTime;
    private String isDefault;
    private String templateName;
    private String templateContent;
    private String organizeCode;

    public void setOrganizeStructs(List<OrganizeStruct> organizeStructs) {
        this.organizeStructs = organizeStructs;
    }

    public List<OrganizeStruct> getOrganizeStructs() {

        return organizeStructs;
    }

    private List<OrganizeStruct> organizeStructs;


    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode;
    }

    public String getOrganizeCode() {

        return organizeCode;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateId() {

        return templateId;
    }

    public String getTemplateType() {
        return templateType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getTemplateContent() {
        return templateContent;
    }
}
