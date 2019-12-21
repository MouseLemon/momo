package com.joysupply.dao;

import java.util.List;
import java.util.Map;

import com.joysupply.entity.DataDictionary;

/**
 * 数据字典数据接口
 *
 * @author Administrator
 */
public interface DataDictionaryDao {
    /**
     * 保存数据字典
     *
     * @param dic
     * @return
     * @throws RuntimeException
     */
    int saveDataDictionary(DataDictionary dic) throws RuntimeException;

    /**
     * 修改指定字典项
     *
     * @param dic
     * @return
     * @throws RuntimeException
     */
    int updateDataDictionary(DataDictionary dic) throws RuntimeException;

    /**
     * 删除指定字典项
     *
     * @param dicCode
     * @return
     * @throws RuntimeException
     */
    int deleteDataDictionary(String dicCode) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取数据字典父列表
     * @Date 2018/10/30 15:53
     * @Param [map]
     * @Return java.util.Map
     **/
    List<DataDictionary> getDicList(Map map) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 根据code查询数据字典
     * @Date 2018/10/30 15:53
     * @Param [code]
     * @Return com.joysupply.entity.DataDictionary
     **/
    DataDictionary getDataDic(String code) throws RuntimeException;
    /**
     * @Author MaZhuli
     * @Description 获取数据字典子项列表
     * @Date 2018/10/30 16:30
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    List<DataDictionary> getDicItemList(String parentCode) throws RuntimeException;

    List<Map<String, Object>> getCompany(String parentCode) throws RuntimeException;
}
