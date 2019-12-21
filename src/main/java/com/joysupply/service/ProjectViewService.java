package com.joysupply.service;

import com.joysupply.exception.BusinessLevelException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectViewService
 * @Author MaZhuli
 * @Date 2018/11/14 15:19
 * @Description 项目视图Service
 * @Version 1.0
 **/
public interface ProjectViewService {

    List<Map<String, Object>> getProjectView(Map map) throws BusinessLevelException;

    List<Map<String, Object>> getProjectViewB(Map map) throws BusinessLevelException;
}
