package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.OrganizePeople;
import com.joysupply.entity.OrganizeStruct;

/**
 * 组织用户数据层接口
 *
 * @author Administrator
 */
public interface OrganizePeopleDao {
    /**
     * @Author MaZhuli
     * @Description 添加组织用户
     * @Date 2018/11/2 18:38
     * @Param [organizePeople]
     * @Return int
     **/
    int addOrganizePeople(OrganizePeople organizePeople) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取联系方式列表
     * @Date 2018/11/30 14:58
     * @Param []
     * @Return java.util.List<java.lang.String>
     **/
    long isTelephoneExist(OrganizePeople organizePeople) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 修改组织用户
     * @Date 2018/11/2 18:38
     * @Param [organizePeople]
     * @Return int
     **/
    int updOrganizePeople(OrganizePeople organizePeople) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 删除组织用户
     * @Date 2018/11/2 18:38
     * @Param [personCode]
     * @Return int
     **/
    int delOrganizePeople(OrganizePeople organizePeople) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据组织用户code查询该用户
     * @Date 2018/11/2 18:37
     * @Param [personCode]
     * @Return java.util.Map<java.lang.String , java.lang.Object>
     **/
    Map<String, Object> queryOrganizePeople(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据组织结构代码查组织人员列表
     * @Date 2018/11/2 18:37
     * @Param [map]
     * @Return java.util.List<java.util.Map < java.lang.String , java.lang.Object>>
     **/
    List<Map<String, Object>> queryOrganizePeoples(Map<String, Object> map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据组织结构代码查组织人员个数
     * @Date 2018/11/2 18:37
     * @Param [map]
     * @Return int
     **/
    int queryOrganizePeopleCount(Map<String, Object> map) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 根据personCode获取人员信息
     * @Date 2018/11/2 19:29
     * @Param [personCode]
     * @Return com.joysupply.entity.OrganizePeople
     **/
    OrganizePeople getOrganizePeople(String personCode) throws RuntimeException;

    /**
     * @Author MaZhuli
     * @Description 获取组织机构下所有人员
     * @Date 2018/11/9 14:05
     * @Param [organizeStruct]
     * @Return java.util.List<com.joysupply.entity.OrganizePeople>
     **/
    List<OrganizePeople> getOrganizePeopleListNoPage(OrganizeStruct organizeStruct) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取部门下审核人员列表
     * @Date 2018/11/19 14:28
     * @Param [organizeStruct]
     * @Return java.util.List<com.joysupply.entity.OrganizePeople>
     **/
    List<OrganizePeople> getOrganizePeopleHaveAuditRole(Map map)throws RuntimeException;

    /**
     * @Author WangYueLei
     * @Description 通过手机号获取该手机号是否已经存在
     * @param telephone
     * @return com.joysupply.entity.OrganizePeople
     * @throws RuntimeException
     */
    OrganizePeople getOrganizePeopleByTelephone (String telephone) throws RuntimeException;

    /**
     * 获取person表中所有用户信息
     * @return
     */
	List<Map<String, Object>> getAllPersonUser();

	/**
	 * @Author SongZiXian
	 * @Description 校验systemUser表数据
	 * @Date 2019/3/15 0015 下午 17:31
	 * @Param [telphone]
	 * @Return java.util.Map<java.lang.String,java.lang.Object>
	 **/
    int getSystemUserByTel(String telphone);
}
