package com.joysupply.service;

import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

/**
 * @ClassName SystemService
 * @Author MaZhuli
 * @Date 2018/11/20 14:35
 * @Description 用户管理业务层
 * @Version 1.0
 **/
public interface SystemUserService {
    OpResult addSystemUser(SystemUser user) throws RuntimeException;

    OpResult updSystemUser(SystemUser user) throws RuntimeException;

    SystemUser getSystemUser(String personCode) throws RuntimeException;

    SystemUser getUserByUserName(String userName) throws BusinessLevelException;

    OpResult modifyPwd(SystemUser newUser) throws BusinessLevelException;
}
