package com.joysupply.dao;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectViewDao
 * @Author MaZhuli
 * @Date 2018/11/14 15:25
 * @Description 项目视图持久层
 * @Version 1.0
 **/
public interface ProjectViewDao {
    List<Map<String,Object>> getProjectView(Map map) throws RuntimeException;

    List<Map<String, Object>> getBmList(Map map) throws RuntimeException;

    List<Map<String, Object>> getOaProjectList(Map map) throws RuntimeException;
}
