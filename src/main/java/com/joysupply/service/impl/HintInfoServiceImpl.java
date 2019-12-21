package com.joysupply.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joysupply.dao.HintInfoDao;
import com.joysupply.service.HintInfoService;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;

/**
 * 保存用户提示
 * @author zxt
 *
 * 2018年11月7日-上午10:02:51
 */
@Service
public class HintInfoServiceImpl extends BaseService implements HintInfoService {
	
	@Autowired
	private HintInfoDao hintInfoDao;
	
	
	@Override
	@Transactional
	public OpResult saveHintInfo(String hintType, String personCode) throws RuntimeException{
		if(hintType == null || "".equals(hintType)) {
			return new OpResult("请选择提示类型");
		}
		Map param = new HashMap();
		param.put("hintType", hintType);
		param.put("personCode", personCode);
		param.put("createTime", DateUtil.ToDateString(new Date(), "yyyy-MM-dd"));
		try {
			
			hintInfoDao.saveHintInfo(param);
			return new OpResult();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException();
			
		}
	}

	@Override
	public OpResult getHintInfoByPerson(String hintType, String personCode) {
		if(hintType == null || "".equals(hintType)) {
			return new OpResult("请选择提示类型");
		}
		Map param = new HashMap();
		param.put("hintType", hintType);
		param.put("personCode", personCode);
		
		int row = hintInfoDao.getHintInfoByPerson(param);
		if(row > 0) {
			return new OpResult();
		}else {
			
			return new OpResult("当前已有记录");
		}
		
	}

}
