package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.DataDictionaryDao;
import com.joysupply.entity.DataDictionary;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.DataDictionaryService;
import com.joysupply.util.Constants;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;
import com.joysupply.util.PageHelperUtil;
import jdk.nashorn.internal.ir.CatchNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("dataDictionaryService")
public class DataDictionaryServiceImpl extends BaseService implements DataDictionaryService {
    @Autowired
    private DataDictionaryDao dataDictionaryDao;
    private Log log = LogFactory.getLog(getClass());

    @Transactional
    @Override
    public OpResult saveDataDictionary(DataDictionary dic) throws BusinessLevelException {
        try {
            if (dic == null || dic.getName().isEmpty()) {
                return new OpResult("数据字典值不能空");
            }
            dic.setCode(Constants.GUID());
            dic.setCreateTime(DateUtil.ToDateTimeString());
            dataDictionaryDao.saveDataDictionary(dic);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("保存数据字典失败:" + ex.getMessage());
            throw new BusinessLevelException("保存数据字典失败", ex);
        }
    }

    @Override
    public OpResult updateDataDictionary(DataDictionary dic) throws BusinessLevelException {
        try {
            if (dic == null) {
                return null;
            }
            if (dic.getName().isEmpty()) {
                return new OpResult("数据字典值不能空");
            }
            if (dic.getCode().isEmpty()) {
                return new OpResult("数据字典key不能空");
            }
            dataDictionaryDao.updateDataDictionary(dic);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("修改数据字典失败:" + ex.getMessage());
            throw new BusinessLevelException("修改数据字典失败", ex);
        }
    }

    @Transactional
    @Override
    public OpResult deleteDataDictionary(String dicCode) throws BusinessLevelException {
        try {
            if (dicCode == null || dicCode.isEmpty()) {
                return new OpResult("字典编码不能为空!");
            }
            int i = dataDictionaryDao.deleteDataDictionary(dicCode);
            return new OpResult();
        } catch (RuntimeException ex) {
            log.error("删除数据字典项[" + dicCode + "]失败:" + ex.getMessage());
            throw new BusinessLevelException("删除数据字典项[" + dicCode + "]失败:", ex);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取数据字典列表
     * @Date 2018/10/29 17:40
     * @Param []
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    @Override
    public Map<String, Object> getDicList(Map map) throws BusinessLevelException {
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        List<DataDictionary> list = null;
        try {
            list = dataDictionaryDao.getDicList(map);
        } catch (RuntimeException e) {
            log.error("获取数据字典列表失败:" + e.getMessage());
            throw new BusinessLevelException("获取数据字典列表失败", e);
        }
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }

    /**
     * @Author MaZhuli
     * @Description 根据code查询数据字典
     * @Date 2018/10/30 15:54
     * @Param [code]
     * @Return com.joysupply.entity.DataDictionary
     **/
    @Override
    public DataDictionary getDataDic(String code) throws BusinessLevelException {
        try {
            return dataDictionaryDao.getDataDic(code);
        } catch (RuntimeException e) {
            throw new BusinessLevelException("根据code查询数据字典", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取数据字典子项列表
     * @Date 2018/10/30 16:29
     * @Param [map]
     * @Return java.util.Map<java.lang.String       ,       java.lang.Object>
     **/
    @Override
    public Map<String, Object> getDicItemList(Map map) throws BusinessLevelException {
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        List<DataDictionary> list = null;
        try {
            list = dataDictionaryDao.getDicItemList(map.get("parentCode").toString());
        } catch (RuntimeException e) {
            log.error("获取数据字典子项列表:" + e.getMessage());
            throw new BusinessLevelException("获取数据字典子项列表", e);
        }
        PageInfo page = new PageInfo(list);
        long totalNum = page.getTotal();
        map.clear();
        map.put("count", totalNum);
        map.put("data", list);
        map.put("code", 0);
        map.put("msg", "返回数据成功");
        return map;
    }
    /**
     * @Author MaZhuli
     * @Description 获取数据子弟子项列表
     * @Date 2018/12/11 10:07
     * @Param [parentCode]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    @Override
    public List<DataDictionary> getDicItemListNoPage(String parentCode) throws BusinessLevelException {
        try {
            return dataDictionaryDao.getDicItemList(parentCode);
        } catch (RuntimeException e) {
            log.error("获取数据字典子项失败" + e.getMessage());
            throw new BusinessLevelException("获取数据字典子项失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getCompany(String parentCode) throws BusinessLevelException {
        try {
            return dataDictionaryDao.getCompany(parentCode);
        } catch (RuntimeException e) {
            log.error("获取数据字典子项失败" + e.getMessage());
            throw new BusinessLevelException("获取数据字典子项失败", e);
        }
    }

}
