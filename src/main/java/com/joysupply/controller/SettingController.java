package com.joysupply.controller;

import com.joysupply.entity.TimeSheet;
import com.joysupply.exception.BusinessLevelException;
import com.joysupply.service.SettingService;
import com.joysupply.util.OpResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：WangHao
 * @Create：2018-11-02 14:48
 * @Description：设置
 * @Program：joysupply-byoa
 * @Version：1.0
 **/
@RestController
@RequestMapping(value = "/setting")
public class SettingController extends BaseController {

    private Log log = LogFactory.getLog(getClass());

    private String OK = "OK";

    @Autowired
    SettingService settingService;

    @RequestMapping(value = "/index")
    public ModelAndView index(){
        Map resultMap = new HashMap();
        resultMap.putAll(getMenuMap());
        resultMap.put("user", getUser());
        return new ModelAndView("setting/timeTable", resultMap);
    }

    /**
     * 查询时间表
     * @param map
     * @return
     */
    @RequestMapping(value = "/timeTable", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> timeTable(@RequestParam Map<String, Object> map){

        try{
            log.debug(map);
            Map tableResult = settingService.timeTable(map);
            return tableResult;
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * （批量编辑时间）修改后，《保存》按钮
     * @param map
     * @return
     */
    @RequestMapping(value = "/saveTimeTable", method = RequestMethod.POST)
    @ResponseBody
    public OpResult saveTimeTable(@RequestParam Map<String,Object> map) {
        try {
            log.debug(map);
            return settingService.saveTimeTable(map);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 添加时间页面
     * @return
     */
    @RequestMapping(value = "/addOneTimePage")
    public ModelAndView addOneTimePage() {
        return new ModelAndView("setting/addOneTimePage");
    }

    /**
     * 添加时间段
     * @param map
     * @return
     */
    @RequestMapping(value = "/addTime", method = RequestMethod.POST)
    @ResponseBody
    public OpResult addTime(@RequestParam Map<String,Object> map){
        try{
            log.debug(map);
            OpResult opResult = settingService.addTime(map);
            if (OK.equals(opResult.getResult())){
                return new OpResult();
            } else {
                return new OpResult("请仔细核查时间范围！");
            }
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 编辑时间页面
     * @return
     */
    @RequestMapping(value = "/editOneTimePage", method = RequestMethod.GET)
    public ModelAndView editOneTimePage(@RequestParam Map map) {
        String start_time = map.get("start_time").toString();
        String start_hour = start_time.substring(0,2);
        String start_minute = start_time.substring(3,5);
        String end_time = map.get("end_time").toString();
        String end_hour = end_time.substring(0,2);
        String end_minute = end_time.substring(3,5);
        map.put("start_hour", start_hour);
        map.put("start_minute", start_minute);
        map.put("end_hour", end_hour);
        map.put("end_minute", end_minute);
        return new ModelAndView("setting/editOneTimePage", map);
    }

    /**
     * 编辑单个时间段
     * @param map
     * @return
     */
    @RequestMapping(value = "/editTime", method = RequestMethod.POST)
    @ResponseBody
    public OpResult editTime(@RequestParam Map<String,Object> map) {
        try{
            log.debug(map);
            OpResult opResult = settingService.editTime(map);
            if (OK.equals(opResult.getResult())){
                return new OpResult();
            } else {
                return new OpResult("请仔细核查时间范围！");
            }
        }catch (BusinessLevelException ex){
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

    /**
     * 删除时间段
     * @param timeSheet
     * @return
     */
    @RequestMapping(value = "/deleteTime", method = RequestMethod.POST)
    @ResponseBody
    public OpResult deleteTime(@RequestBody TimeSheet timeSheet) {
        try {
            log.debug(timeSheet);
            return settingService.deleteTime(timeSheet);
        } catch (BusinessLevelException ex) {
            ex.printStackTrace();
            return new OpResult(ex.getMessage());
        }
    }

}
