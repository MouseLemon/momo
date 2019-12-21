package com.joysupply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joysupply.entity.SystemUser;
import com.joysupply.service.HintInfoService;
import com.joysupply.util.OpResult;

@RequestMapping("/hintInfo")
@Controller
public class HintInfoController extends BaseController {
	
	@Autowired
	private HintInfoService hintInfoService;
	
	/**
	 * 保存用户选择的提示信息
	 * @param hintType
	 * @return
	 */
	@RequestMapping(value="/saveHintInfo" ,method=RequestMethod.POST)
	@ResponseBody
	public OpResult saveHintInfo(String hintType) {
		
		SystemUser user = getUser();
		return hintInfoService.saveHintInfo(hintType, user.getPersonCode());
	}
	
	
	/**
	 * 查询用户是否保存选择的提示信息
	 * @param hintType
	 * @return
	 */
	@RequestMapping(value="/getHintInfo" ,method=RequestMethod.POST)
	@ResponseBody
	public OpResult getHintInfo(String hintType) {
		
		SystemUser user = getUser();
		return hintInfoService.getHintInfoByPerson(hintType, user.getPersonCode());
	}
	
	
}
