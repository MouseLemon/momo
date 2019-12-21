package com.joysupply.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joysupply.dao.*;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.GenerateCodeService;
import com.joysupply.service.ProductService;
import com.joysupply.util.*;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("productService")
public class ProductServiceImpl extends BaseService implements ProductService {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrganizeStructDao organizeStructDao;
    @Autowired
    private OrganizePeopleDao organizePeopleDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private AuthorityManageDao authorityManageDao;
    @Autowired
    private ProjectSchedulePlanDao projectSchedulePlanDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    GenerateCodeService generateCodeService;

    private Log log = LogFactory.getLog(getClass());

    // ----------------------------------------------------- 爆款单品 ---------------------------------------------------

    /** 
    * @Author: WangHao
    * @Description: 获取爆款列表
    * @Date: 2019/12/16 10:45
    * @Param: [map]
    * @returns: java.util.Map<java.lang.String,java.lang.Object>
    */
    @Override
    public Map<String, Object> getPopularList(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("pp.date desc");
        try {
            list = productDao.getPopularList(map);
        } catch (RuntimeException e) {
            log.error("获取爆款列表:" + e.getMessage());
            throw new BusinessLevelException("获取爆款列表失败", e);
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
    * @Author: WangHao
    * @Description: 保存爆款
    * @Date: 2019/12/16 15:35
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @Transactional
    @Override
    public OpResult addPopular(Map map) throws BusinessLevelException {
        try {
            // 根据编码检查是否重复
            int count = productDao.checkPopularCode(map);
            if (count > 0) {
                return new OpResult("该爆款编码已存在!");
            }
            String popularId = Constants.GUID();
            map.put("popularId", popularId);
            map.put("creater", map.get("userName").toString());
            map.put("personCode", map.get("personCode").toString());
            productDao.addPopular(map);

            // 添加文件
            List list = new ArrayList<>();
            String fileName = map.get("fileName").toString();
            String fileUrl = map.get("fileUrl").toString();
            if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                map.put("imgId", UUID.randomUUID().toString());
                map.put("uploader", map.get("userName").toString());
                map.put("uploadTime", DateUtil.ToDateTimeString());
                productDao.addPopularImg(map);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("新增爆款失败", e);
        }
    }

    /** 
    * @Author: WangHao
    * @Description: 获取爆款信息
    * @Date: 2019/12/16 15:35
    * @Param: [popularId]
    * @returns: java.util.Map
    */
    @Override
    public Map getPopular(String popularId) throws BusinessLevelException {
        try {
            return productDao.getPopular(popularId);
        } catch (RuntimeException e) {
            log.error(popularId);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    /** 
    * @Author: WangHao
    * @Description: 编辑爆款
    * @Date: 2019/12/16 15:46
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @Transactional
    @Override
    public OpResult editPopular(Map map) throws BusinessLevelException {
        try {
            map.put("modifier", map.get("userName").toString());
            productDao.editPopular(map);

            if (!map.containsKey("fileName") && !map.containsKey("fileUrl")) {
                return new OpResult();
            } else {
                // 添加文件
                String fileName = map.get("fileName").toString();
                String fileUrl = map.get("fileUrl").toString();
                if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                    map.put("uploader", map.get("userName").toString());
                    map.put("uploadTime", DateUtil.ToDateTimeString());
                    productDao.editPopularImg(map);
                }
                return new OpResult();
            }
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("修改爆款失败", e);
        }
    }

    /**
    * @Author: WangHao
    * @Description: 删除爆款
    * @Date: 2019/12/16 16:22
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @Transactional
    @Override
    public OpResult delPopular(Map map) throws BusinessLevelException {
        try {
            productDao.delPopular(map);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("修改爆款失败", e);
        }
    }

    // ------------------------------------------------------ 样板间 ----------------------------------------------------

    /**
    * @Author: WangHao
    * @Description: 获取样板间分页数据
    * @Date: 2019/12/16 19:32
    * @Param: [map]
    * @returns: java.util.Map<java.lang.String,java.lang.Object>
    */
    @Override
    public Map<String, Object> getSuitList(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("ps.date desc");
        try {
            list = productDao.getSuitList(map);
        } catch (RuntimeException e) {
            log.error("获取样板间列表:" + e.getMessage());
            throw new BusinessLevelException("获取样板间列表失败", e);
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
    * @Author: WangHao
    * @Description: 保存样板间
    * @Date: 2019/12/16 19:37
    * @Param: [map]
    * @returns: com.joysupply.util.OpResult
    */
    @Transactional
    @Override
    public OpResult addSuit(Map map) throws BusinessLevelException {
        try {
            // 根据编码检查是否重复
            int count = productDao.checkSuitCode(map);
            if (count > 0) {
                return new OpResult("该样板间编码已存在!");
            }
            String suitId = Constants.GUID();
            map.put("suitId", suitId);
            map.put("creater", map.get("userName").toString());
            map.put("personCode", map.get("personCode").toString());
            productDao.addSuit(map);

            // 添加图片 * 3
            if (map.containsKey("fileName1") && map.containsKey("fileUrl1")) {
                String fileName = map.get("fileName1").toString();
                String fileUrl = map.get("fileUrl1").toString();
                if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                    Map imgMap1 = new HashMap();
                    imgMap1.put("suitId", suitId);
                    imgMap1.put("imgId", UUID.randomUUID().toString());
                    imgMap1.put("fileName", fileName);
                    imgMap1.put("fileUrl", fileUrl);
                    imgMap1.put("fileXy", map.get("fileXy1").toString());
                    imgMap1.put("uploader", map.get("userName").toString());
                    imgMap1.put("uploadTime", DateUtil.ToDateTimeString());
                    productDao.addSuitImg(imgMap1);
                }
            }

            if (map.containsKey("fileName2") && map.containsKey("fileUrl2")) {
                String fileName = map.get("fileName2").toString();
                String fileUrl = map.get("fileUrl2").toString();
                if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                    Map imgMap2 = new HashMap();
                    imgMap2.put("suitId", suitId);
                    imgMap2.put("imgId", UUID.randomUUID().toString());
                    imgMap2.put("fileName", fileName);
                    imgMap2.put("fileUrl", fileUrl);
                    imgMap2.put("fileXy", map.get("fileXy2").toString());
                    imgMap2.put("uploader", map.get("userName").toString());
                    imgMap2.put("uploadTime", DateUtil.ToDateTimeString());
                    productDao.addSuitImg(imgMap2);
                }
            }

            if (map.containsKey("fileName3") && map.containsKey("fileUrl3")) {
                String fileName = map.get("fileName3").toString();
                String fileUrl = map.get("fileUrl3").toString();
                if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                    Map imgMap3 = new HashMap();
                    imgMap3.put("suitId", suitId);
                    imgMap3.put("imgId", UUID.randomUUID().toString());
                    imgMap3.put("fileName", fileName);
                    imgMap3.put("fileUrl", fileUrl);
                    imgMap3.put("fileXy", map.get("fileXy3").toString());
                    imgMap3.put("uploader", map.get("userName").toString());
                    imgMap3.put("uploadTime", DateUtil.ToDateTimeString());
                    productDao.addSuitImg(imgMap3);
                }
            }

            return new OpResult();
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("新增样板间失败", e);
        }
    }

    @Override
    public Map getSuit(String suitId) throws BusinessLevelException {
        try {
            return productDao.getSuit(suitId);
        } catch (RuntimeException e) {
            log.error(suitId);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    @Override
    public List getSuitImgList(String suitId) throws BusinessLevelException {
        try {
            return productDao.getSuitImgList(suitId);
        } catch (RuntimeException e) {
            log.error(suitId);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    @Transactional
    @Override
    public OpResult editSuit(Map map) throws BusinessLevelException {
        try {
            map.put("modifier", map.get("userName").toString());
            productDao.editSuit(map);

            String fileUrl1 = map.get("fileUrl1").toString();
            String fileUrl2 = map.get("fileUrl2").toString();
            String fileUrl3 = map.get("fileUrl3").toString();

            String fileName1 = map.get("fileName1").toString();
            String fileName2 = map.get("fileName2").toString();
            String fileName3 = map.get("fileName3").toString();

            String fileXy1 = map.get("fileXy1").toString();
            String fileXy2 = map.get("fileXy2").toString();
            String fileXy3 = map.get("fileXy3").toString();

            if (fileName1 != null && !fileName1.equals("")) {
                Map imgMap1 = new HashMap();
                imgMap1.put("suitId", map.get("suitId").toString());
                imgMap1.put("imgId", UUID.randomUUID().toString());
                imgMap1.put("fileName", fileName1);
                imgMap1.put("fileUrl", fileUrl1);
                imgMap1.put("fileXy", fileXy1);
                imgMap1.put("uploader", map.get("userName").toString());
                imgMap1.put("uploadTime", DateUtil.ToDateTimeString());
                // 先将原本的图片删除
                productDao.deleteSuitImg(imgMap1);
                // 再添加新图片
                productDao.addSuitImg(imgMap1);
            }

            if (fileName2 != null && !fileName2.equals("")) {
                Map imgMap2 = new HashMap();
                imgMap2.put("suitId", map.get("suitId").toString());
                imgMap2.put("imgId", UUID.randomUUID().toString());
                imgMap2.put("fileName", fileName2);
                imgMap2.put("fileUrl", fileUrl2);
                imgMap2.put("fileXy", fileXy2);
                imgMap2.put("uploader", map.get("userName").toString());
                imgMap2.put("uploadTime", DateUtil.ToDateTimeString());
                // 先将原本的图片删除
                productDao.deleteSuitImg(imgMap2);
                // 再添加新图片
                productDao.addSuitImg(imgMap2);
            }

            if (fileName3 != null && !fileName3.equals("")) {
                Map imgMap3 = new HashMap();
                imgMap3.put("suitId", map.get("suitId").toString());
                imgMap3.put("imgId", UUID.randomUUID().toString());
                imgMap3.put("fileName", fileName3);
                imgMap3.put("fileUrl", fileUrl3);
                imgMap3.put("fileXy", fileXy3);
                imgMap3.put("uploader", map.get("userName").toString());
                imgMap3.put("uploadTime", DateUtil.ToDateTimeString());
                // 先将原本的图片删除
                productDao.deleteSuitImg(imgMap3);
                // 再添加新图片
                productDao.addSuitImg(imgMap3);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("修改爆款失败", e);
        }
    }

    @Transactional
    @Override
    public OpResult delSuit(Map map) throws BusinessLevelException {
        try {
            productDao.delSuit(map);
            productDao.deleteSuitImgs(map);
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("修改爆款失败", e);
        }
    }

    // ------------------------------------------------------ 家居日志 --------------------------------------------------

    @Override
    public Map<String, Object> getDiaryList(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("pd.date desc");
        try {
            list = productDao.getDiaryList(map);
        } catch (RuntimeException e) {
            log.error("获取样板间列表:" + e.getMessage());
            throw new BusinessLevelException("获取样板间列表失败", e);
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

    @Transactional
    @Override
    public OpResult addDiary(Map map) throws BusinessLevelException {
        try {
            // 根据编码检查是否重复
//            int count = productDao.checkPopularCode(map);
//            if (count > 0) {
//                return new OpResult("该爆款编码已存在!");
//            }
            String diaryId = Constants.GUID();
            map.put("diaryId", diaryId);
            map.put("creater", map.get("userName").toString());
            map.put("personCode", map.get("personCode").toString());
            productDao.addDiary(map);

            List list = new ArrayList<>();
            String fileName = map.get("fileNames").toString();
            String fileUrl = map.get("fileUrls").toString();
            if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                String[] fileNames = fileName.split(",");
                String[] fileUrls = fileUrl.split(",");
                for (int i = 0; i < fileNames.length; i++) {
                    Map imgMap = new HashMap();
                    imgMap.put("imgId", UUID.randomUUID().toString());
                    imgMap.put("diaryId", diaryId);
                    imgMap.put("imgName", fileNames[i]);
                    imgMap.put("imgUrl", fileUrls[i]);
                    imgMap.put("uploader", map.get("userName").toString());
                    imgMap.put("uploadTime", DateUtil.ToDateTimeString());
                    list.add(imgMap);
                }
            }
            if (list.size() > 0) {
                productDao.addDiaryImg(list);
            }
            return new OpResult();
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("新增爆款失败", e);
        }
    }

    @Override
    public Map getDiary(String diaryId) throws BusinessLevelException {
        try {
            return productDao.getDiary(diaryId);
        } catch (RuntimeException e) {
            log.error(diaryId);
            throw new BusinessLevelException("获取项目信息失败", e);
        }
    }

    @Override
    public Map<String, Object> getDiaryImgList(Map map) throws BusinessLevelException {
        List<Map> list = null;
        Map<String, Integer> pageInfo = PageHelperUtil.getPageInfo(map);
        PageHelper.startPage(pageInfo.get("page"), pageInfo.get("limit"));
        PageHelper.orderBy("pdi.upload_time desc");
        try {
            list = productDao.getDiaryImgList(map);
        } catch (RuntimeException e) {
            log.error("获取样板间列表:" + e.getMessage());
            throw new BusinessLevelException("获取样板间列表失败", e);
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

    @Transactional
    @Override
    public OpResult editDiary(Map map) throws BusinessLevelException {
        List delImgList = new ArrayList();
        try {
            // 判断imgList是否有值
            if (map.containsKey("imgList")) {
                String list1 = map.get("imgList").toString();
                list1 = list1.replaceAll("\r|\n", "");
                String list2 = list1.substring(0, list1.length() - 1);
                String list3 = "{[" + list2.replace("=", ":") + "]}";

                int index = list3.indexOf("imgId");
                for (int i = 0; i < index; i ++) {
                    String imgId = list3.substring(index, 36);
                    System.out.println(imgId);
                }

                List<Map> list4 = JSONArray.fromObject(list3);



//                for (String imgString : list3) {
//                    JSONObject parseObject = JSONArray.parseObject(imgString);
//                    JSONObject obj = parseObject.getJSONObject("obj");
//                    String imgId = obj.get("imgId").toString();
//                    delImgList.add(imgId);
//                }
            }


            map.put("modifier", map.get("userName").toString());
            productDao.editDiary(map);

            if (!map.containsKey("fileNames") && !map.containsKey("fileUrls")) {
                return new OpResult();
            } else {
                List list = new ArrayList<>();
                String fileName = map.get("fileNames").toString();
                String fileUrl = map.get("fileUrls").toString();
                if (fileName != null && !fileName.equals("") && fileUrl != null && !fileUrl.equals("")) {
                    String[] fileNames = fileName.split(",");
                    String[] fileUrls = fileUrl.split(",");
                    for (int i = 0; i < fileNames.length; i++) {
                        Map imgMap = new HashMap();
                        imgMap.put("imgId", UUID.randomUUID().toString());
                        imgMap.put("diaryId", map.get("diaryId").toString());
                        imgMap.put("imgName", fileNames[i]);
                        imgMap.put("imgUrl", fileUrls[i]);
                        imgMap.put("uploader", map.get("userName").toString());
                        imgMap.put("uploadTime", DateUtil.ToDateTimeString());
                        list.add(imgMap);
                    }
                }
                if (list.size() > 0) {
                    productDao.addDiaryImg(list);
                }
                return new OpResult();
            }
        } catch (RuntimeException e) {
            log.error(map);
            throw new BusinessLevelException("修改爆款失败", e);
        }
    }






    
}
