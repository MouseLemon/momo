package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.RoleDao;
import com.joysupply.dao.ScoreTemplateDao;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.ScoreTemplate;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.OrganizeStructService;
import com.joysupply.service.ScoreTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreTemplateServiceImpl implements ScoreTemplateService {

    @Autowired
    ScoreTemplateDao scoreTemplateDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    OrganizeStructService organizeStructService;

    @Override
    public PageInfo<ScoreTemplate> listData(ScoreTemplate scoreTemplate, int page, int limit) {
        PageHelper.startPage(page,limit);
        List<ScoreTemplate> list = scoreTemplateDao.listData(scoreTemplate);
        PageInfo<ScoreTemplate> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    @Transactional
    public void saveTemplate(ScoreTemplate scoreTemplate) throws BusinessLevelException {
        try {
            scoreTemplateDao.saveTemplate(scoreTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLevelException(e.toString());
        }
    }

    @Override
    @Transactional
    public void deleteTemplate(String templateId) throws BusinessLevelException {
        try {
            scoreTemplateDao.deleteTemplate(templateId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLevelException(e.toString());
        }
    }

    @Override
    public void isDefault(ScoreTemplate scoreTemplate) throws BusinessLevelException {
        try {
            // 先设置为0
            scoreTemplateDao.setDefault(scoreTemplate);
            // 设置当前为默认
            scoreTemplateDao.isDefault(scoreTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLevelException(e.toString());
        }
    }

    @Override
    public ScoreTemplate getTemplate(String templateId) {
        return scoreTemplateDao.getTemplate(templateId);
    }

    @Override
    public ScoreTemplate getTemplateByDefault(String type) {
        return scoreTemplateDao.getTemplateByDefault(type);
    }


    @Override
    @Transactional
    public void updateTemplate(ScoreTemplate scoreTemplate) throws BusinessLevelException {
        try {
            scoreTemplateDao.updateTemplate(scoreTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLevelException(e.toString());
        }
    }

    /**
     * 获取当前用户的所有项目部
     * @param personCode
     * @return
     */
    @Override
    public List<OrganizeStruct> listOrganizeByPersonCode(String personCode) {
        // 先判断是否超级管理员
        List<String> roles = roleDao.getPersonRoles(personCode);
        if (roles.contains("18")) {
            // 查询所有项目部
            List<OrganizeStruct> projectDepList = organizeStructService.getProjectDepList();
            return projectDepList;
        }
        List<OrganizeStruct> list = organizeStructService.listOrganizeByPersonCode(personCode);
        return list;
    }

    @Override
    public List<ScoreTemplate> listTemplate(List<OrganizeStruct> listOrganizeByPersonCode, String type) {
        if(listOrganizeByPersonCode.size() <= 0) {
            return null;
        }
        Map<String,Object> params = new HashMap<>(2);
        params.put("listOrganizeByPersonCode",listOrganizeByPersonCode);
        params.put("type",type);
        return scoreTemplateDao.listTemplate(params);
    }
}
