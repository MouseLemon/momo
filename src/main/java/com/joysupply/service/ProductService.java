package com.joysupply.service;

import com.joysupply.entity.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 项目业务层
 *
 * @author Administrator
 */
public interface ProductService {

    // ------------------------------------------------- 爆款单品 -------------------------------------------------------

    /*获取爆款列表分页数据*/
    Map<String, Object> getPopularList(Map map) throws BusinessLevelException;

    /*保存爆款*/
    OpResult addPopular(Map map) throws BusinessLevelException;

    /*获取爆款信息*/
    Map<String, Object> getPopular(String popularId) throws BusinessLevelException;

    /*编辑爆款*/
    OpResult editPopular(Map map) throws BusinessLevelException;

    /*删除爆款*/
    OpResult delPopular(Map map) throws BusinessLevelException;

    // ------------------------------------------------- 样板间 ---------------------------------------------------------

    /*获取样板间分页数据*/
    Map<String, Object> getSuitList(Map map) throws BusinessLevelException;

    /*保存样板间*/
    OpResult addSuit(Map map) throws BusinessLevelException;

    /*编辑样板间*/
    OpResult editSuit(Map map) throws BusinessLevelException;

    /*获取样板间信息*/
    Map<String, Object> getSuit(String suitId) throws BusinessLevelException;

    /*获取样板间图片列表*/
    List getSuitImgList(String suitId) throws BusinessLevelException;

    /*删除样板间*/
    OpResult delSuit(Map map) throws BusinessLevelException;

    // ------------------------------------------------- 样板间 ---------------------------------------------------------

    Map<String, Object> getDiaryList(Map map) throws BusinessLevelException;

    OpResult addDiary(Map map) throws BusinessLevelException;

    Map<String, Object> getDiary(String diaryId) throws BusinessLevelException;

    Map getDiaryImgList(Map temp) throws BusinessLevelException;

    OpResult editDiary(Map map) throws BusinessLevelException;
}
