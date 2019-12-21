package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.Project;
import com.joysupply.entity.ProjectSchedulePlan;

/**
 * 项目排课计划数据层
 * @author Administrator
 *
 */
public interface ProjectSchedulePlanDao {
	/**
	 * 根据教室编号查询记录
	 * @param projectSchedulePlan
	 * @return
	 */
	List<ProjectSchedulePlan> getProjectSchedulePlanList(ProjectSchedulePlan projectSchedulePlan);
	/**
	 * 根据项目排课计划表主键查询记录
	 * @param map
	 * @return
	 */
	ProjectSchedulePlan getProjectSchedulePlanByScheduleId(Map<String, Object> map);
	/**
	 * 修改项目排课计划表
	 * @param map
	 * @return
	 */
	int updateProjectSchedulePlan(Map<String, Object> map);
	
	/**
	 * 查询当前教室是否已排课
	 * @param roomId
	 * @return
	 */
	Integer getProjectSchedulePlanByRooId(String roomId);
	/**
	 * @Author MaZhuli
	 * @Description 根据项目id删除排课
	 * @Date 2018/12/7 16:49
	 * @Param [project]
	 * @Return int
	 **/
    int delProjectSchedulePlanByProjectId(Project project);

	/**
	 * WYL 发放课酬后修改 is_send 状态
	 * @param list
	 * @throws RuntimeException
	 */
    void upIsSend(List<Map<String,Object>> list) throws RuntimeException;
}
