package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.*;
import com.joysupply.util.OpResult;

/**
 * 项目数据层接口
 *
 * @author Administrator
 */
public interface ProjectDao {

    /**
     * @Author MaZhuli
     * @Description 获取项目列表
     * @Date 2018/11/5 10:15
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> getProjectList(Map map) throws RuntimeException;

    List<Map> getProjectListA(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取项目信息
     * @Date 2018/11/5 14:44
     * @Param [projectId]
     * @Return com.joysupply.entity.Project
     **/
    Project getProject(String projectId) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取变更记录
     * @Date 2018/11/12 8:51
     * @Param [projectId]
     * @Return com.joysupply.entity.Project
     **/
    Project getChangeProject(String changeId) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 新增项目
     * @Date 2018/11/5 14:45
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    int addProject(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除项目
     * @Date 2018/11/5 14:45
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    int delProject(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改项目信息
     * @Date 2018/11/5 14:45
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    int updProject(Project project) throws RuntimeException;

    /**
     * WangYuelei  课酬发放修改项目数据
     * @param project
     * @return
     * @throws RuntimeException
     */
    int updProjectKC(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据变更信息修改项目信息
     * @Date 2018/11/5 14:45
     * @Param [project]
     * @Return com.joysupply.util.OpResult
     **/
    int updProjectByChange(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取项目列表无分页
     * @Date 2018/11/5 10:15
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Project> getProjectListNoPage(String personCode) throws RuntimeException;

    List<Project> getProjectListNoPageA(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取变更列表
     * @Date 2018/11/7 18:44
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Project> getChangeList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取项目附件列表
     * @Date 2018/11/7 18:48
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> getAccessoryList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取项目审批意见
     * @Date 2018/11/7 18:56
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> getProjectAuditOpionList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取变更审批意见
     * @Date 2018/11/12 10:32
     * @Param [map]
     * @Return java.util.List<java.util.Map>
     **/
    List<Map> getChangeAuditOpionList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 添加附件
     * @Date 2018/11/7 20:54
     * @Param [list]
     * @Return int
     **/
    int addProjectAddFile(List<ProjectAddFile> list) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除附件
     * @Date 2018/11/8 11:06
     * @Param [projectAddFile]
     * @Return int
     **/
    int delAccessory(ProjectAddFile projectAddFile) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 添加附件
     * @Date 2018/11/8 11:39
     * @Param [projectAddFile]
     * @Return int
     **/
    int addAccessory(ProjectAddFile projectAddFile) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 插入项目审核记录
     * @Date 2018/11/9 15:42
     * @Param [list]
     * @Return int
     **/
    int addProjectApproveOption(List<ProjectApproveOption> list) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改项目状态为已提交
     * @Date 2018/11/9 15:47
     * @Param [projectIdList]
     * @Return int
     **/
    int commitProject(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 变更项目
     * @Date 2018/11/10 16:23
     * @Param [project]
     * @Return int
     **/
    int changeProject(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改变更状态为已提交
     * @Date 2018/11/10 18:18
     * @Param [idList]
     * @Return int
     **/
    int commitChange(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 查询是否有未完成的变更
     * @Date 2018/11/12 11:03
     * @Param [project]
     * @Return boolean
     **/
    int getChangeInAudit(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取项目原始信息
     * @Date 2018/11/13 16:03
     * @Param [projectId]
     * @Return com.joysupply.entity.Project
     **/
    Project getOriginProject(String projectId) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取变更原始信息
     * @Date 2018/11/13 16:51
     * @Param [changeIdItem]
     * @Return com.joysupply.entity.Project
     **/
    Project getOriginChange(String changeIdItem) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改变更
     * @Date 2018/11/13 18:19
     * @Param [project]
     * @Return int
     **/
    int updChange(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除未审核记录
     * @Date 2018/11/14 13:50
     * @Param [project]
     * @Return int
     **/
    int delApprove(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 校验项目编码是否存在
     * @Date 2018/12/10 17:28
     * @Param [projectCode]
     * @Return int
     **/
    int checkProjectCode(Project project) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取提交信息列表
     * @Date 2018/12/11 16:36
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    List<DataDictionary> getCommitProjectList(Map map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description
     * @Date 2018/12/21 15:35
     * @Param [map]
     * @Return int
     **/
    int updateTempCode(Map map) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 根据项目编码获取项目id
     * @Date 2019/1/3 11:31
     * @Param [projectCode]
     * @Return java.lang.String
     **/
    String getProjectIdByProjectCode(String projectCode) throws RuntimeException;

    /**
     * @Author SongZiXian
     * @Description 获取二期相同项目编号数据
     * @Date 2019/3/20 0020 下午 18:18
     * @Param [map]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> getByBmProjectInfo(Map<String, Object> map) throws RuntimeException;

    int addGrade(Map<String, Object> gradeMap) throws RuntimeException;

    int countProject(String project_code) throws RuntimeException;

}
