package com.joysupply.controller;

import java.util.HashMap;
import java.util.Map;
import com.joysupply.util.MapObj;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joysupply.entity.DataDictionary;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.DataDictionaryService;
import com.joysupply.util.OpResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * 数据字典
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/api/dic")
public class DataDictionaryController extends BaseController {
    @Autowired
    private DataDictionaryService dataDictionaryService;

    private Log log = LogFactory.getLog(getClass());

    /**
     * 保存数据字典项
     *
     * @param dic
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public OpResult saveDataDictionary(@RequestBody DataDictionary dic) {
        try {
            log.debug(dic);
            return dataDictionaryService.saveDataDictionary(dic);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 修改父字典下的具体子项
     *
     * @param dic
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateDataDictionary(@RequestBody DataDictionary dic) {
        try {
            log.debug(dic);
            return dataDictionaryService.updateDataDictionary(dic);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 删除数据字典子项
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OpResult deleteDataDictionary(@RequestBody Map map) {

        try {
            log.debug(map.get("dicCode").toString());
            return dataDictionaryService.deleteDataDictionary(map.get("dicCode").toString());
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * @Author MaZhuli
     * @Description 跳转数据字典页面
     * @Date 2018/10/30 15:49
     * @Param []
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/getDicListPage", method = RequestMethod.GET)
    public ModelAndView getDicListPage() {
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("dataDictionary/dataDicList", resultMap);
    }

    /**
     * @Author MaZhuli
     * @Description 添加数据字典
     * @Date 2018/10/30 15:48
     * @Param map
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/addDataDicPage", method = RequestMethod.GET)
    public ModelAndView addDataDicPage(@RequestParam Map map) {
        return new ModelAndView("dataDictionary/addDataDicPage", map);
    }

    /**
     * @Author MaZhuli
     * @Description 获取数据字典列表
     * @Date 2018/10/30 15:48
     * @Param [map]
     * @Return java.util.Map<java.lang.String , java.lang.Object>
     **/
    @RequestMapping(value = "/getDicList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getDicList(@RequestParam Map map) {
        try {
            return dataDictionaryService.getDicList(map);
        } catch (BusinessLevelException e) {
            log.error("获取数据字典列表:" + e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/getDicItemList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getDicItemList(@RequestParam Map map) {
        try {
            return dataDictionaryService.getDicItemList(map);
        } catch (BusinessLevelException e) {
            log.error("获取数据字典列表:" + e.getMessage());
            return null;
        }
    }
    /**
     * @Author MaZhuli
     * @Description 编辑数据字典页面
     * @Date 2018/10/30 16:19
     * @Param [code]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editDataDicPage", method = RequestMethod.GET)
    public ModelAndView editDataDicPage(String code) {
        DataDictionary dataDictionary = dataDictionaryService.getDataDic(code);
        return new ModelAndView("dataDictionary/editDataDicPage", MapObj.objectToMap(dataDictionary));
    }

    /**
     * @Author MaZhuli
     * @Description 编辑数据字典子项页面
     * @Date 2018/10/30 16:20
     * @Param [code]
     * @Return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/editDataDicItemPage", method = RequestMethod.GET)
    public ModelAndView editDataDicItemPage(@RequestParam Map dataDic) {
        DataDictionary parentDataDic = dataDictionaryService.getDataDic(dataDic.get("parentCode").toString());
        dataDic.put("isfixed",parentDataDic.getIsfixed());
        return new ModelAndView("dataDictionary/dataDicItemList", dataDic);
    }
}
