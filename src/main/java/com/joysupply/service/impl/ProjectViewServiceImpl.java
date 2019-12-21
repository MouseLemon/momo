package com.joysupply.service.impl;

import com.joysupply.dao.ClassManageDao;
import com.joysupply.dao.ProjectViewDao;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.ClassManageService;
import com.joysupply.service.ProjectViewService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectViewServiceImpl
 * @Author MaZhuli
 * @Date 2018/11/14 15:19
 * @Description 项目视图业务层实现
 * @Version 1.0
 **/
@Service("projectViewService")
public class ProjectViewServiceImpl implements ProjectViewService {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ProjectViewDao projectViewDao;
    @Autowired
    private ClassManageService classManageService;

    /**
     * @Author MaZhuli
     * @Description 获取项目视图记录
     * @Date 2018/11/15 14:05
     * @Param [map]
     * @Return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     **/
    @Override
    public List<Map<String, Object>> getProjectView(Map map) throws BusinessLevelException {
        try {
            // 判断当前项目是否为二期项目
            List<Map<String, Object>> plist = projectViewDao.getOaProjectList(map);
            if (plist.size() > 0) {
                // 一期项目
                return projectViewDao.getProjectView(map);
            } else {
                // 二期项目
                return projectViewDao.getBmList(map);
            }
        } catch (RuntimeException e) {
            log.error("获取项目视图记录:" + e.getMessage());
            throw new BusinessLevelException("获取项目视图记录", e);
        }
    }

    @Override
    public List<Map<String, Object>> getProjectViewB(Map map) throws BusinessLevelException {
        try {
            return projectViewDao.getProjectView(map);
        } catch (RuntimeException e) {
            log.error("获取项目视图记录:" + e.getMessage());
            throw new BusinessLevelException("获取项目视图记录", e);
        }
    }
}
