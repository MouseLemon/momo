package com.joysupply.dao;

import com.joysupply.entity.ScoreTemplate;

import java.util.List;
import java.util.Map;

public interface ScoreTemplateDao {
    /**
     * 证书列表
     * @param scoreTemplate
     * @return
     */
    List<ScoreTemplate> listData(ScoreTemplate scoreTemplate);

    void saveTemplate(ScoreTemplate scoreTemplate) throws RuntimeException;

    void deleteTemplate(String templateId) throws RuntimeException;

    void setDefault(ScoreTemplate scoreTemplate) throws RuntimeException;

    void isDefault(ScoreTemplate scoreTemplate) throws RuntimeException;

    ScoreTemplate getTemplate(String templateId);

    void updateTemplate(ScoreTemplate scoreTemplate) throws RuntimeException;

    ScoreTemplate getTemplateByDefault(String type);

    /**
     * 获取当前用户的打印模板
     * @param params
     * @return
     */
    List<ScoreTemplate> listTemplate(Map<String,Object> params);
}
