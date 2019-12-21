package com.joysupply.service;

import com.joysupply.entity.SystemUser;

public interface LoginService {
    /**
     * 用户登录
     * @param systemUser
     * @return
     */
    SystemUser queryUserByParam(SystemUser systemUser);

    /**
     * @Description: 根据id查询用户
     * @Param: [systemUser]
     * @return: com.joysupply.entity.SystemUser
     * @Author: ZhangXingTong
     * @Date: 2019/2/27 14:40
     */
    SystemUser queryUserByPersonCode(String personCode);
}
