package com.joysupply.service;

import com.joysupply.exception.BusinessLevelException;

import java.util.Map;

/**
 * @ClassName GenerateCodeService
 * @Author MaZhuli
 * @Date 2018/11/23 10:40
 * @Description 生成编码业务层
 * @Version 1.0
 **/
public interface GenerateCodeService {
    Integer getCurrentVersion(Map map) throws BusinessLevelException;
}
