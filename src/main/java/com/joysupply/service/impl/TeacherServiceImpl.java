package com.joysupply.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joysupply.dao.RoleDao;
import com.joysupply.entity.ProjectSchedulePlan;

import com.joysupply.entity.SystemUser;
import com.joysupply.service.ClassManageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.TeacherDao;
import com.joysupply.entity.Teacher;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.TeacherService;
import com.joysupply.util.Constants;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;

@Service("teacherService")
public class TeacherServiceImpl extends BaseService implements TeacherService {
	private Log log=LogFactory.getLog(getClass());
	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private ClassManageService classManageService;
	@Autowired
	RoleDao roleDao;
	@Override
	@Transactional
	public OpResult addTeacher(Teacher teacher) throws BusinessLevelException{
		try {
			if(teacher.getTeacherCode() != null && !"".equals(teacher.getTeacherCode())) {
				return updateTeacher(teacher);
			}
			
			if(existTeacher(teacher.getIdCard())) {
				return new OpResult("该身份证号已存在");
			}
			teacher.setTeacherCode(Constants.GUID());
			teacher.setCreateTime(DateUtil.ToDateTimeString());
			teacherDao.addTeacher(teacher);
			return new OpResult();
		} catch (Exception e) {
			log.error("添加教师信息失败:"+e.getMessage());
			throw new BusinessLevelException("添加教师信息失败",e);
		}
	}
	
	public boolean existTeacher(String idCard) {
		if(idCard == null || "".equals(idCard)) {
			return false;
		}
		String card = teacherDao.existTeacher(idCard);
		if(card != null && !"".equals(card)) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public OpResult updateTeacher(Teacher teacher)
			throws BusinessLevelException {
		
		
		try {
			String teacherCode = teacher.getTeacherCode();
			if(teacherCode == null || "".equals(teacherCode)) {
				return new OpResult("教师编码不能为空");
			}
			
			int row = teacherDao.updateTeacher(teacher);
			return new OpResult();
		} catch (Exception e) {
			log.error("修改教师信息失败:"+e.getMessage());
			throw new BusinessLevelException("修改教师信息失败",e);
		}
	}


	@Override
	@Transactional
	public OpResult deleteTeacher(String teacherCode)
			throws BusinessLevelException {
		//判断教室是否已排课，排课不能删除

        try {
            int count = classManageService.getPkCountByTeacherCode(teacherCode);
            if(count > 0) {
                return new OpResult("该教师已排课");
            }

			if(teacherCode == null || "".equals(teacherCode)) {
				return new OpResult("教师编码不能为空");
			}

			// 否则可用删除
			teacherDao.deleteTeacher(teacherCode);
            // 删除用户信息
			teacherDao.deleteUser(teacherCode);
			return new OpResult();
		} catch (Exception e) {
			log.error("删除教师信息失败:"+e.getMessage());
			throw new BusinessLevelException("删除教师信息失败",e);
		}
		
	}


	@Override
	public PageInfo<Teacher> listData(Teacher teacher, int page, int limit)
			throws BusinessLevelException {
		
		try {
			List<String> personCode = roleDao.getPersonRoles(teacher.getPersonCode());
			if (personCode.contains("18") || personCode.contains("1801") || personCode.contains("1802") || personCode.contains("19")|| personCode.contains("1901")|| personCode.contains("1902")) {
				teacher.setPersonCode("");
			}
			PageHelper.startPage(page,limit);
			List<Teacher> listData = teacherDao.listData(teacher);
			PageInfo<Teacher> pageInfo = new PageInfo<Teacher>(listData);
			
			return pageInfo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLevelException("查询教师列表失败",e);
		}
		
	}


	@Override
	public Teacher getTeacherByCode(String teacherCode)
			throws BusinessLevelException {
		try {
			Teacher teacher = teacherDao.getTeacherByCode(teacherCode);
			return teacher;
			
		} catch (Exception e) {
			log.error("查询教师信息失败:"+e.getMessage());
			throw new BusinessLevelException("删除教师信息失败",e);
		}
	}

	/**
	 * 无分页查询教师列表
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public List<Teacher> getTeacherListNoPage() throws BusinessLevelException {
		try {
			return teacherDao.getTeacherListNoPage();
		} catch (RuntimeException e) {
			log.error("获取教研室列表:" + e.getMessage());
			throw new BusinessLevelException("获取教研室列表", e);
		}
	}

	/**
	 * 根据教师查询所有课程
	 * @param teacher_name
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public Map<String, Object> getCourseByTeacherName(String teacher_name) throws BusinessLevelException {

		try {
			List list = teacherDao.getCourseByTeacherName(teacher_name);
			Map result = new HashMap();
			result.put("list", list);
			return result;
		} catch (RuntimeException e) {
			log.error("获取教师课程:" + e.getMessage());
			throw new BusinessLevelException("获取教师课程", e);
		}
	}

	/**
	 * 根据教师名称查询课程
	 * @param param
	 * @return
	 */
	@Override
	public List<ProjectSchedulePlan> getSavedCourseByTeacherName(Map param) throws BusinessLevelException {
		if(param.get("teacher_name") == null || "".equals(param.get("teacher_name").toString())) {
			return new ArrayList<ProjectSchedulePlan>();
		}
		List result = teacherDao.getSavedCourseByTeacherName(param);
		return result;
	}

	@Override
	public Teacher getTeacherInfoByCode(String personCode) {
		// TODO Auto-generated method stub
		return teacherDao.getTeacherInfoByCode(personCode);
	}

	
	/**
	 * 查询教师首页今日课程及课时数
	 */
	@Override
	public Map<String, Object> getTodayClass(String teacherCode) {
		
		String today = DateUtil.ToDateString(new Date(), "yyyy-MM-dd");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("classDate", today);
		param.put("teacherCode", teacherCode);
		List<Map<String, Object>> list = teacherDao.getTodayClass(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		result.put("todayCount", getTodayClassCount(teacherCode, today));
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		result.put("tCount", getTodayClassCount(teacherCode, DateUtil.ToDateString(cal.getTime(), "yyyy-MM-dd")));
		return result;
	}

	@Override
	public int getTodayClassCount(String teacherCode, String dateTime) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("classDate", dateTime);
		param.put("teacherCode", teacherCode);
		return teacherDao.getTodayClassCount(param);
	}

    @Override
    public List<Teacher> getCurrentTeacher(String person_code) {
		try{
			log.debug(person_code);
			return teacherDao.getCurrentTeacher(person_code);
		}catch (RuntimeException e){
			log.error("获取教师:" + e.getMessage());
			throw new BusinessLevelException("获取教师", e);
		}
    }

	/**
	 * 根据project_id查询教师列表
	 * @return
	 * @throws BusinessLevelException
	 */
	@Override
	public List<Teacher> getTeacherListByProjectId(String project_id) throws BusinessLevelException {
		try {
			return teacherDao.getTeacherListByProjectId(project_id);
		} catch (RuntimeException e) {
			log.error("获取教师列表:" + e.getMessage());
			throw new BusinessLevelException("获取教师列表", e);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void importTeacher(List<Map<String, Object>> list) throws RuntimeException {
		try {
			teacherDao.importTeacher(list);
		} catch (RuntimeException e) {
			log.error("导入教师列表:" + e.getMessage());
			throw new BusinessLevelException("导入教师列表", e);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void importUserByTeacher(List<Map<String, Object>> list) throws RuntimeException {
		try {
			teacherDao.importUserByTeacher(list);
		} catch (RuntimeException e) {
			log.error("导入教师用户列表:" + e.getMessage());
			throw new BusinessLevelException("导入教师用户列表", e);
		}
	}

	@Override
	public List<Teacher> existTeacher(List<Map<String, Object>> list) {
		return teacherDao.existTeachers(list);
	}

	@Override
	public List<SystemUser> existUser(List<Map<String, Object>> list) {
		return teacherDao.existUser(list);
	}

	@Override
	public Map<String, String> getSignaTrue(String personCode) {
		return teacherDao.getSignaTrue(personCode);
	}

	@Override
	public void updateSigna(String personCode, String signature) throws BusinessLevelException{
		Map<String,String> param = new HashMap<>();
		param.put("personCode",personCode);
		param.put("signature",signature);
		try {
			teacherDao.updateSigna(param);
		}catch (RuntimeException e) {
			throw new BusinessLevelException("修改签名失败", e);
		}
	}

	@Override
	public void addSigna(String personCode, String signature) throws BusinessLevelException{
		Map<String,String> param = new HashMap<>();
		param.put("personCode",personCode);
		param.put("signature",signature);
		try {
			teacherDao.addSigna(param);
		}catch (RuntimeException e) {
			throw new BusinessLevelException("添加签名失败", e);
		}
	}

	@Override
	public List<Map<String, Object>> listDataNoPage(Teacher teacher) {
		List<Map<String, Object>> listData = teacherDao.listDataNoPage(teacher);
		return listData;
	}

	@Override
	public List<Map<String, Object>> getTeachers(String person_code) {
		try{
			log.debug(person_code);
			return teacherDao.getTeachers(person_code);
		}catch (RuntimeException e){
			log.error("获取教师:" + e.getMessage());
			throw new BusinessLevelException("获取教师", e);
		}
	}

}
