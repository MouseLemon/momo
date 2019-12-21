package com.joysupply.dao;

import java.util.List;
import java.util.Map;

/** 
* @Author: WangHao
* @Description: 产品管理数据层接口
* @Date: 2019/12/17 19:55
* @Param: 
* @returns: 
*/
public interface ProductDao {

    // ------------------------------------------------- 爆款单品 -------------------------------------------------------
    
    /*获取爆款列表*/
    List<Map> getPopularList(Map map) throws RuntimeException;

    /*核查爆款编码*/
    int checkPopularCode(Map map) throws RuntimeException;

    /*保存爆款*/
    int addPopular(Map map) throws RuntimeException;

    /*保存爆款图片*/
    int addPopularImg(Map map) throws RuntimeException;

    /*获取爆款信息*/
    Map getPopular(String popularId) throws RuntimeException;

    /*编辑爆款*/
    int editPopular(Map map) throws RuntimeException;

    /*修改图片*/
    int editPopularImg(Map map) throws RuntimeException;

    /*删除爆款*/
    int delPopular(Map map) throws RuntimeException;

    // ------------------------------------------------- 样板间 ---------------------------------------------------------

    /*获取样板间分页数据*/
    List<Map> getSuitList(Map map) throws RuntimeException;

    /*核查样板间编码*/
    int checkSuitCode(Map map) throws RuntimeException;

    /*添加样板间*/
    int addSuit(Map map) throws RuntimeException;

    /*添加样板间图片*/
    int addSuitImg(Map map) throws RuntimeException;

    /*编辑样板间*/
    int editSuit(Map map) throws RuntimeException;

    /*获取样板间信息*/
    Map getSuit(String suitId) throws RuntimeException;

    /*获取样板间图片列表*/
    List getSuitImgList(String suitId) throws RuntimeException;

    /*编辑时删除样板间图片*/
    int deleteSuitImg(Map imgMap1) throws RuntimeException;

    /*删除样板间*/
    int delSuit(Map map) throws RuntimeException;

    /*删除样板间时，将存储图片也删除*/
    int deleteSuitImgs(Map map) throws RuntimeException;

    // ------------------------------------------------- 样板间 ---------------------------------------------------------

    List<Map> getDiaryList(Map map) throws RuntimeException;

    int addDiary(Map map) throws RuntimeException;

    int addDiaryImg(List list) throws RuntimeException;

    Map getDiary(String suitId) throws RuntimeException;

    List<Map> getDiaryImgList(Map map) throws RuntimeException;

    int editDiary(Map map) throws RuntimeException;
}
