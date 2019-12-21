package com.joysupply.dao;

import java.util.Map; /**
 * @ClassName GenerateCodeDao
 * @Author MaZhuli
 * @Date 2018/11/23 10:43
 * @Description 生成编码持久层
 * @Version 1.0
 **/
public interface GenerateCodeDao {
    //查询当前版本
    Map getCurrentVersion(Map map) throws RuntimeException;
    //新增版本
    int addVersion(Map map)throws RuntimeException;
    //更新版本
    int updVersion(Map map)throws RuntimeException;
}
