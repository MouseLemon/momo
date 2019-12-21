package com.joysupply.service.impl;

import com.joysupply.dao.LoginDao;
import com.joysupply.entity.SystemUser;
import com.joysupply.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LoginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;
    private Log log = LogFactory.getLog(getClass());
    
    @Override
    public SystemUser queryUserByParam(SystemUser systemUser) {
        systemUser.setIsEnable("1");
        return loginDao.selSystemUserByName(systemUser);
    }

    @Override
    public SystemUser queryUserByPersonCode(String personCode) {
        return loginDao.queryUserByPersonCode(personCode);
    }
}
