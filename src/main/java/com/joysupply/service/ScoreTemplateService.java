package com.joysupply.service;

import com.github.pagehelper.PageInfo;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.ScoreTemplate;
import com.joysupply.exception.BusinessLevelException;

import java.util.List;

/**
 * 证书模板业务层
 */
public interface ScoreTemplateService {

    /**
     * 证书列表
     * @param scoreTemplate
     * @param page
     * @param limit
     * @return
     */
    PageInfo<ScoreTemplate> listData(ScoreTemplate scoreTemplate, int page, int limit);

    void saveTemplate(ScoreTemplate scoreTemplate) throws BusinessLevelException;

    void deleteTemplate(String templateId) throws BusinessLevelException;

    void isDefault(ScoreTemplate scoreTemplate) throws BusinessLevelException;

    ScoreTemplate getTemplate(String templateId);
    ScoreTemplate getTemplateByDefault(String type);

    void updateTemplate(ScoreTemplate scoreTemplate) throws BusinessLevelException;

    /**
     * 获取当前用户所有的项目部
     * @param personCode
     * @return
     */
    List<OrganizeStruct> listOrganizeByPersonCode(String personCode);

    /**
     * 获取当前用户的成绩模板
     * @param listOrganizeByPersonCode
     * @param type
     * @return
     */
    List<ScoreTemplate> listTemplate(List<OrganizeStruct> listOrganizeByPersonCode, String type);
}
