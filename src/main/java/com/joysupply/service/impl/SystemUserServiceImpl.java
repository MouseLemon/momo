package com.joysupply.service.impl;

import com.joysupply.dao.SettingDao;
import com.joysupply.dao.SystemUserDao;
import com.joysupply.entity.SystemUser;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.SystemUserService;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SystemUserServiceImpl
 * @Author MaZhuli
 * @Date 2018/11/20 14:36
 * @Description
 * @Version 1.0
 **/
@Service("systemUserService")
 public class SystemUserServiceImpl implements SystemUserService {
    private Log log = LogFactory.getLog(getClass());

    @Autowired
    SystemUserDao systemUserDao;
    /**
     * @Author MaZhuli
     * @Description 添加账户
     * @Date 2018/11/20 14:47
     * @Param [user]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult addSystemUser(SystemUser user) throws RuntimeException {
        try {
            user.setIsEnable("1");
            systemUserDao.addSystemUser(user);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("添加账户失败:" + e.getMessage());
            throw new BusinessLevelException("添加账户失败", e);
        }
    }
    /**
     * @Author MaZhuli
     * @Description 修改账户
     * @Date 2018/11/20 14:45
     * @Param [user]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult updSystemUser(SystemUser user) throws RuntimeException {
        try {
            systemUserDao.updSystemUser(user);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("修改账户失败:" + e.getMessage());
            throw new BusinessLevelException("修改账户失败", e);
        }
    }
    /**
     * @Author MaZhuli
     * @Description 查询账户信息
     * @Date 2018/11/20 14:50
     * @Param [user]
     * @Return com.joysupply.entity.SystemUser
     **/
    @Override
    public SystemUser getSystemUser(String personCode) throws RuntimeException {
        try {
            return systemUserDao.getSystemUser(personCode);
        } catch (RuntimeException e) {
            log.error("查询账户信息失败:" + e.getMessage());
            throw new BusinessLevelException("查询账户信息失败", e);
        }
    }

    @Override
    public SystemUser getUserByUserName(String userName) throws BusinessLevelException {
        try{
            return systemUserDao.getUserByUserName(userName);
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            throw new BusinessLevelException("通过用户名查询系统用户",ex);
        }
    }

    @Override
    public OpResult modifyPwd(SystemUser newUser) throws BusinessLevelException {
        try{
           systemUserDao.updSystemUser(newUser);
           return new OpResult();
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            throw new BusinessLevelException("修改用户密码失败",ex);
        }
    }
}
