package com.joysupply.dao;

import com.joysupply.entity.SystemUser;

public interface LoginDao {
    /**
     * 通过参数查询用户当前传name
     * @param systemUser
     * @return
     */
    SystemUser selSystemUserByName (SystemUser systemUser);

    SystemUser queryUserByPersonCode(String personCode);
}
