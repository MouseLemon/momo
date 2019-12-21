package com.joysupply.service;

import com.joysupply.entity.DataDictionary;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 数据字典业务接口
 *
 * @author Administrator
 */
public interface DataDictionaryService {
    /**
     * 保存数据字典项
     *
     * @param dic
     * @return
     * @throws BusinessLevelException
     */
    OpResult saveDataDictionary(DataDictionary dic) throws BusinessLevelException;

    /**
     * 修改指定字典项
     *
     * @param dic
     * @return
     * @throws RuntimeException
     */
    OpResult updateDataDictionary(DataDictionary dic) throws BusinessLevelException;

    /**
     * 删除指定字典项
     *
     * @param dicCode
     * @return
     * @throws RuntimeException
     */
    OpResult deleteDataDictionary(String dicCode) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 获取数据字典父列表
     * @Date 2018/10/30 15:53
     * @Param [map]
     * @Return java.util.Map
     **/
    Map getDicList(Map map) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 根据code查询数据字典
     * @Date 2018/10/30 15:53
     * @Param [code]
     * @Return com.joysupply.entity.DataDictionary
     **/
    DataDictionary getDataDic(String code) throws BusinessLevelException;

    /**
     * @Author MaZhuli
     * @Description 获取数据字典子项列表
     * @Date 2018/10/30 16:29
     * @Param [map]
     * @Return java.util.Map<java.lang.String , java.lang.Object>
     **/
    Map<String, Object> getDicItemList(Map map) throws BusinessLevelException;
    /**
     * @Author MaZhuli
     * @Description 获取数据字典所有子项
     * @Date 2018/11/1 15:32
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    List<DataDictionary> getDicItemListNoPage(String parentCode)throws BusinessLevelException;

    List<Map<String, Object>> getCompany(String parentCode)throws BusinessLevelException;
}
