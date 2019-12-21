package com.joysupply.service.impl;

import com.joysupply.dao.GenerateCodeDao;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.GenerateCodeService;
import com.joysupply.util.Constants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @ClassName GenerateCodeServiceImpl
 * @Author MaZhuli
 * @Date 2018/11/23 10:42
 * @Description 生成编码业务实现
 * @Version 1.0
 **/
@Service("generateCodeService")
public class GenerateCodeServiceImpl implements GenerateCodeService {
    @Autowired
    private GenerateCodeDao generateCodeDao;

    @Override
    @Transactional
    public Integer getCurrentVersion(Map map) throws BusinessLevelException {
        Map recordMap = generateCodeDao.getCurrentVersion(map);
        Integer version = null;
        if(recordMap != null && recordMap.containsKey("version")){
            version = Integer.parseInt(recordMap.get("version").toString());
        }
        if (version == null) {
            map.put("id", Constants.GUID());
            map.put("version", 1);
            version = 1;
            generateCodeDao.addVersion(map);
        } else {
            map.put("id",recordMap.get("id").toString());
            generateCodeDao.updVersion(map);
            version++;
        }
        return version;
    }
}
