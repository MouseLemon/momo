package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.entity.TeachResearchRoom;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.util.DateUtil;
import com.joysupply.util.OpResult;
import com.joysupply.util.PageHelperUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joysupply.dao.TeachResearchRoomDao;
import com.joysupply.service.TeachResearchRoomService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Dreamer
 * @Description 教研室ServiceImpl
 * @Date 上午 10:43 2018/12/15 0015
 * @Param
 * @return
 **/
@Service("teachResearchRoomService")
public class TeachResearchRoomServiceImpl extends BaseService implements TeachResearchRoomService {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private TeachResearchRoomDao teachResearchRoomDao;

    /**
     * @Author MaZhuli
     * @Description 获取教研室列表
     * @Date 2018/11/3 11:42
     * @Param [map]
     * @Return java.util.Map<java.lang.String       ,       java.lang.Object>
     **/
    @Override
    public Map<String, Object> getResearchOfficeList(Map map) throws BusinessLevelException {
        List<Map<String, Object>> list;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        try {
            list = teachResearchRoomDao.getResearchOfficeList(map);
        } catch (RuntimeException e) {
            log.error("获取教研室列表:" + e.getMessage());
            throw new BusinessLevelException("获取教研室列表", e);
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
     * @Description 获取教研室列表无分页
     * @Date 2018/11/3 11:42
     * @Param [map]
     * @Return java.util.Map<java.lang.String   ,   java.lang.Object>
     **/
    @Override
    public List<TeachResearchRoom> getResearchOfficeListNoPage() throws BusinessLevelException {
        try {
            return teachResearchRoomDao.getResearchOfficeListNoPage();
        } catch (RuntimeException e) {
            log.error("获取教研室列表:" + e.getMessage());
            throw new BusinessLevelException("获取教研室列表", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 根据教研室code获取教研室信息
     * @Date 2018/11/3 14:34
     * @Param [organizeCode]
     * @Return com.joysupply.entity.TeachResearchRoom
     **/
    @Override
    public TeachResearchRoom getResearchOffice(String organizeCode) throws BusinessLevelException {
        try {
            return teachResearchRoomDao.getResearchOffice(organizeCode);
        } catch (RuntimeException e) {
            log.error("获取教研室列表:" + e.getMessage());
            throw new BusinessLevelException("获取教研室列表", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 添加教研室
     * @Date 2018/11/3 14:47
     * @Param [teachResearchRoom]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult addResearchOffice(TeachResearchRoom teachResearchRoom) throws BusinessLevelException {
        try {
            //TODO:编码生成方式有问题
            List<String> codeList = teachResearchRoomDao.getResearchOfficeCodeList();
            for (int i = 10; i < 100; i++) {
                if(!codeList.contains(Integer.toString(i))){
                    teachResearchRoom.setOfficeCode(Integer.toString(i));
                    teachResearchRoom.setCreateDate(DateUtil.ToDateString(new Date(),"yyyy-MM-dd"));
                    break;
                }
            }
            teachResearchRoomDao.addResearchOffice(teachResearchRoom);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("添加教研室:" + e.getMessage());
            throw new BusinessLevelException("添加教研室", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 修改教研室
     * @Date 2018/11/3 14:48
     * @Param [teachResearchRoom]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult updResearchOffice(TeachResearchRoom teachResearchRoom) throws BusinessLevelException {
        try {
            teachResearchRoomDao.updResearchOffice(teachResearchRoom);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("修改教研室:" + e.getMessage());
            throw new BusinessLevelException("修改教研室", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除教研室
     * @Date 2018/11/3 14:50
     * @Param [teachResearchRoom]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult delResearchOffice(TeachResearchRoom teachResearchRoom) throws BusinessLevelException {
        try {
            teachResearchRoomDao.delResearchOffice(teachResearchRoom);
            return new OpResult("删除教研室成功");
        } catch (RuntimeException e) {
            log.error("删除教研室:" + e.getMessage());
            throw new BusinessLevelException("删除教研室", e);
        }
    }

    @Override
    public Map selectChecking(String office_name) {
        try {
            log.debug(office_name);
            Map map = teachResearchRoomDao.selectChecking(office_name);
            return map;
        } catch (RuntimeException e) {
            log.error("删除教研室:" + e.getMessage());
            throw new BusinessLevelException("删除教研室", e);
        }
    }
}
