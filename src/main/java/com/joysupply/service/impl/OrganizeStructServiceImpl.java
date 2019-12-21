package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.OrganizeStructDao;
import com.joysupply.entity.DataDictionary;
import com.joysupply.entity.JobType;
import com.joysupply.entity.OrganizeStruct;
import com.joysupply.entity.OrganizeType;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.OrganizeStructService;
import com.joysupply.util.Constants;
import com.joysupply.util.OpResult;
import com.joysupply.util.PageHelperUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("organizeStructService")
public class OrganizeStructServiceImpl extends BaseService implements OrganizeStructService {

    @Autowired
    private OrganizeStructDao organizeStructDao;
    private Log log = LogFactory.getLog(getClass());
    /**
     * @Author MaZhuli
     * @Description 获取部门列表
     * @Date 2019/1/16 11:57
     * @Param []
     * @Return java.util.List<java.util.Map>
     **/
    @Override
    public List<Map> queryFirstOrganizeList(){
        try{
            return organizeStructDao.queryFirstOrganizeList();
        }catch (RuntimeException e){
            log.error("获取部门列表失败" + e.getMessage());
            throw new BusinessLevelException("获取部门列表失败", e);
        }
    }
    /**
     * @Author MaZhuli
     * @Description 获取组织结构
     * @Date 2019/1/16 13:55
     * @Param []
     * @Return java.util.List<java.util.Map>
     **/
    @Override
    public List<Map> queryOrganizeTree() throws BusinessLevelException {
        try {
            List<Map> list = organizeStructDao.queryFirstOrganizeList();
            List<Map> treeList = new ArrayList<>();
            for (Map map : list) {
                String name = map.get("name").toString();
                String id = map.get("id").toString();
                name = "<span id='" + id + "' style='margin-right: 10px;'>" + name + "</span>" +
                        "<span class='treeBtn' onclick ='addNode(" + "&quot;" + id + "&quot;" + ")'>添加</span>" +
                        "<span class='treeDelBtn' onclick='delNode(" + "&quot;" + id + "&quot;" + ")' >删除</span>" +
                        "<span class='treeBtn' onclick ='editNode(" + "&quot;" + id + "&quot;" + ")'>修改</span>";
                map.put("name", name);
                map.put("spread", true);
                treeList.add(querySecondOrganizeList(map));
            }
            return treeList;
        } catch (RuntimeException e) {
            log.error("获取组织结构失败" + e.getMessage());
            throw new BusinessLevelException("获取组织结构失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 添加部门
     * @Date 2018/10/31 17:39
     * @Param [map]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult addOrganizeStruct(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            String upCode = organizeStruct.getUpCode();
            List<String> organizeCodeList = organizeStructDao.getOrganizeCodeList();
            //TODO:编码生成方式有问题
            for (int i = 10; i < 100; i++) {
                if (upCode == null || upCode.equals("")) {
                    if (!organizeCodeList.contains(Integer.toString(i))) {
                        organizeStruct.setOrganizeCode(Integer.toString(i));
                        break;
                    }
                } else {
                    if (!organizeCodeList.contains(upCode + Integer.toString(i))) {
                        organizeStruct.setOrganizeCode(upCode + Integer.toString(i));
                        break;
                    }
                }
            }
            organizeStructDao.addOrganizeStruct(organizeStruct);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("添加部门:" + e.getMessage());
            throw new BusinessLevelException("添加部门", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 删除部门
     * @Date 2018/10/31 18:06
     * @Param [organizeStruct]
     * @Return com.joysupply.util.OpResult
     **/
    @Override
    public OpResult delOrganizeStruct(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            // 查询是否有人员
            List<OrganizeStruct> son = organizeStructDao.getSon(organizeStruct);
            son.add(organizeStruct);
            Integer count = organizeStructDao.getPeopleCount(son);
            if (count > 0) { // 有人员
                return new OpResult("当前部门下有人员，不能删除");
            }
            organizeStructDao.delOrganizeStruct(organizeStruct);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("删除部门:" + e.getMessage());
            throw new BusinessLevelException("删除部门", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 修改项目部
     * @Date 2018/10/31 18:20
     * @Param [organizeStruct]
     * @Return com.joysupply.util.OpResult
     **/
    @Transactional
    @Override
    public OpResult updOrganizeStruct(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            organizeStructDao.updOrganizeStruct(organizeStruct);
            organizeStructDao.upProjectInfoOsCode(organizeStruct);
            if( organizeStruct.getUpCode() == null ||  organizeStruct.getUpCode().equals("")){
                organizeStructDao.updSonCompanyCode(organizeStruct);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("修改项目部信息:" + e.getMessage());
            throw new BusinessLevelException("修改项目部信息", e);
        }
    }

    /**
     * @Author Dreamer
     * @Description 修改部门
     * @Date 下午 14:45 2019/3/15 0015
     * @Param [organizeStruct]
     * @return com.joysupply.util.OpResult
     **/
    @Transactional
    @Override
    public OpResult updateDepartment(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            organizeStructDao.updOrganizeStruct(organizeStruct);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error("修改部门信息:" + e.getMessage());
            throw new BusinessLevelException("修改部门信息", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取岗位列表
     * @Date 2018/11/1 14:36
     * @Param []
     * @Return java.util.List<com.joysupply.entity.JobType>
     **/
    @Override
    public List<JobType> getJobTypeList() throws BusinessLevelException {
        try {
            return organizeStructDao.getJobTypeList();
        } catch (RuntimeException e) {
            log.error("获取岗位列表:" + e.getMessage());
            throw new BusinessLevelException("获取岗位列表", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取组织类型
     * @Date 2018/11/1 14:36
     * @Param []
     * @Return java.util.List<com.joysupply.entity.OrganizeType>
     **/
    @Override
    public List<OrganizeType> getOrganizeTypeList() throws BusinessLevelException {
        try {
            return organizeStructDao.getOrganizeTypeList();
        } catch (RuntimeException e) {
            log.error("获取岗位列表:" + e.getMessage());
            throw new BusinessLevelException("获取岗位列表", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 根据organizeCode获取组织
     * @Date 2018/11/1 16:32
     * @Param [organizeCode]
     * @Return com.joysupply.entity.OrganizeStruct
     **/
    @Override
    public OrganizeStruct getOrganizeStruct(String organizeCode) throws BusinessLevelException {
        try {
            return organizeStructDao.getOrganizeStruct(organizeCode);
        } catch (RuntimeException e) {
            log.error("根据organizeCode获取组织:" + e.getMessage());
            throw new BusinessLevelException("根据organizeCode获取组织", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取组织人员列表
     * @Date 2018/11/2 9:06
     * @Param [map]
     * @Return java.util.List<com.joysupply.entity.DataDictionary>
     **/
    @Override
    public Map<String, Object> getOrganizePeopleList(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("p.create_time desc");
        try {
            list = organizeStructDao.getOrganizePeopleList(map);
        } catch (RuntimeException e) {
            log.error("获取组织人员列表:" + e.getMessage());
            throw new BusinessLevelException("获取组织人员列表", e);
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
     * @Description 根据人员获取组织机构
     * @Date 2018/11/5 14:09
     * @Param [personCode]
     * @Return com.joysupply.entity.OrganizeStruct
     **/
    @Override
    public OrganizeStruct getOrganizeStructByPersonCode(String personCode) throws BusinessLevelException {
        try {
            return organizeStructDao.getOrganizeStructByPersonCode(personCode);
        } catch (RuntimeException e) {
            log.error("根据人员获取组织机构失败"+e.getMessage());
            throw new BusinessLevelException("根据人员获取组织机构失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取子级部门列表
     * @Date 2018/11/5 13:57
     * @Param [organizeStruct]
     * @Return java.util.List<com.joysupply.entity.OrganizeStruct>
     **/
    @Override
    public List<OrganizeStruct> getSon(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            return organizeStructDao.getSon(organizeStruct);
        } catch (RuntimeException e) {
            log.error("获取子级部门列表失败"+e.getMessage());
            throw new BusinessLevelException("获取子级部门列表失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取部门
     * @Date 2018/11/5 13:59
     * @Param [organizeStruct]
     * @Return com.joysupply.entity.OrganizeStruct
     **/
    @Override
    public OrganizeStruct getParent(OrganizeStruct organizeStruct) throws BusinessLevelException {
        try {
            return organizeStructDao.getParent(organizeStruct);
        } catch (RuntimeException e) {
            log.error("获取部门失败"+e.getMessage());
            throw new RuntimeException("获取部门失败", e);
        }
    }

    /**
     * @Author MaZhuli
     * @Description 获取所有项目部
     * @Date 2018/12/5 9:37
     * @Param []
     * @Return java.util.List<com.joysupply.entity.OrganizeStruct>
     **/
    @Override
    public List<OrganizeStruct> getProjectDepList() throws BusinessLevelException {
        try {
            return organizeStructDao.getProjectDepList();
        } catch (RuntimeException e) {
            log.error("获取所有项目部失败" + e.getMessage());
            throw new RuntimeException("获取所有项目部失败", e);
        }
    }

    @Override
    public List<OrganizeStruct> listOrganizeByPersonCode(String personCode) {
        OrganizeStruct organizeStructByPersonCode = organizeStructDao.getOrganizeStructByPersonCode(personCode);
        String upCode = organizeStructByPersonCode.getUpCode();
        if(upCode == null || "".equals(upCode)) {
            // 查询子级
            List<OrganizeStruct> son = organizeStructDao.getSon(organizeStructByPersonCode);
            return son;
        }
        // 否则返回自己
        List<OrganizeStruct> list = new ArrayList<>();
        list.add(organizeStructByPersonCode);
        return list;
    }

    /**
     * @Author MaZhuli
     * @Description 查询所有部门
     * @Date 2018/12/5 9:36
     * @Param []
     * @Return java.util.List<com.joysupply.entity.OrganizeStruct>
     **/
    @Override
    public List<OrganizeStruct> getAll() {
        try {
            return organizeStructDao.getAll();
        } catch (RuntimeException e) {
            log.error("查询所有部门失败" + e.getMessage());
            throw new RuntimeException("查询所有部门失败", e);
        }
    }

    public Map querySecondOrganizeList(Map map) {
        List<Map> maps = organizeStructDao.querySonOrganizeList(map);
        if (maps != null && maps.size() > 0) {
            for (Map mapItem : maps) {
                String name = mapItem.get("name").toString();
                String id = mapItem.get("id").toString();
                name = "<span id='" + id + "' style='margin-right: 10px;'>" + name + "</span>" +
//                        "<span class='treeBtn' onclick ='addNode(" + "&quot;" + id + "&quot;" + ")' th:each='func:${session.funcList}' th:if='${func == 'bmda_addOrganize'}'>添加</span>" +
                        "<span class='treeDelBtn' onclick='delNode(" + "&quot;" + id + "&quot;" + ")' th:each='func:${session.funcList}' th:if='${func == 'bmda_delOrganize'}'>删除</span>" +
                        "<span class='treeBtn' onclick ='editChildNode(" + "&quot;" + id + "&quot;" + ")' th:each='func:${session.funcList}' th:if='${func == " +
                        "'bmda_updOrganize'}'>修改</span>";
                mapItem.put("name", name);
                mapItem.put("spread", true);
                queryThirdOrganizeList(mapItem);
            }
            map.put("children", maps);
        }
        return map;
    }

    public Map queryThirdOrganizeList(Map map) {
        List<Map> maps = organizeStructDao.querySonOrganizeList(map);
        if (maps != null && maps.size() > 0) {
            for (Map mapItem : maps) {
                String name = mapItem.get("name").toString();
                String id = mapItem.get("id").toString();
                name = "<span id='" + id + "' style='margin-right: 10px;'>" + name + "</span>" +
                        "<span class='treeDelBtn' onclick='delNode(" + "&quot;" + id + "&quot;" + ")' >删除</span>" +
                        "<span class='treeBtn' onclick ='editNode(" + "&quot;" + id + "&quot;" + ")'>修改</span>";
                mapItem.put("name", name);
                mapItem.put("spread", true);
            }
            map.put("children", maps);
        }
        return map;
    }
}
