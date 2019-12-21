package com.joysupply.dao;

import com.joysupply.entity.SystemUser;

/**
 * @ClassName SystemUserDao
 * @Author MaZhuli
 * @Date 2018/11/20 11:39
 * @Description 用户持久层
 * @Version 1.0
 **/
public interface SystemUserDao {
    /**
     * @Author MaZhuli
     * @Description 删除账户信息
     * @Date 2018/11/20 12:05
     * @Param [systemUser]
     * @Return int
     **/
    int delSystemUser(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改账户信息
     * @Date 2018/11/20 12:05
     * @Param [systemUser]
     * @Return int
     **/
    int updSystemUser(SystemUser systemUser) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 添加账户
     * @Date 2018/11/20 12:05
     * @Param [systemUser]
     * @Return int
     **/
    int addSystemUser(SystemUser systemUser) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 查询账户详情
     * @Date 2018/11/20 13:56
     * @Param [systemUser]
     * @Return com.joysupply.entity.SystemUser
     **/
    SystemUser getSystemUser(String personCode) throws RuntimeException;

    /**
     * WangYueLei
     * 检查用户名是否存
     *
     * @param userName
     * @return
     * @throws RuntimeException
     */
    SystemUser getUserByUserName(String userName) throws RuntimeException;
}
