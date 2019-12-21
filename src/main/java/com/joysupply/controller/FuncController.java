package com.joysupply.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.joysupply.entity.Func;
import com.joysupply.service.FuncService;

/**
 * 功能管理
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/api/func")
public class FuncController extends BaseController {
    @Autowired
    private FuncService funcService;
    private Log log = LogFactory.getLog(getClass());
}
