package com.cmz.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmz.web.constant.URLConfig;
import com.cmz.web.domain.LogConfig;
import com.cmz.web.service.LogConfigService;
import com.cmz.web.util.CommUtil;
import com.cmz.web.util.PageUtil;

@Controller
public class LogConfigController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogConfigController.class);
	
	@Autowired
	private LogConfigService logconfigservice;
	@Autowired
	private APIController helloController;
	
	@RequestMapping(value=URLConfig.LOG_LIST, method = RequestMethod.GET)
    public String logConfigList(
    		@RequestParam(name="page",required=false,defaultValue="1") int page, Model model) {

		int count = logconfigservice.count();
		int num = 20;
		int from = (page-1)*num;
		List<LogConfig> logconfig = logconfigservice.getPageList(from, num);
		model.addAttribute("pages", PageUtil.getPages(page, count, num, URLConfig.LOG_LIST, null));
		model.addAttribute("logconfig",logconfig);
        
        return "logConfigList";
    }
	
	@RequestMapping(URLConfig.LOG_ADD)
    public String logAdd(HttpServletRequest request,HttpServletResponse response,Model model) {
		return "logConfigAdd";
	}
	
	@RequestMapping(URLConfig.LOG_ADD_AC)
    public String logConfigAction(HttpServletRequest request,HttpServletResponse response,
    		Model model,LogConfig logconfig) {

        if(StringUtils.isEmpty(logconfig.getLogtypename())){
			CommUtil.showMsg(request,response, "请填日志类型名称!", URLConfig.LOG_ADD);
			return null;
		}
        
        if(StringUtils.isEmpty(logconfig.getLogtypeflag())){
			CommUtil.showMsg(request,response, "请填日志类型标识!", URLConfig.LOG_ADD);
			return null;
		}
        
        if(StringUtils.isEmpty(logconfig.getLogfields())){
			CommUtil.showMsg(request,response, "请填日志字段!", URLConfig.LOG_ADD);
			return null;
		}
        
        int id = logconfigservice.add(logconfig);
		if(id>0){
			helloController.update(logconfig);
			CommUtil.showMsg(request,response, "操作成功", URLConfig.LOG_LIST+"?page=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.LOG_ADD);
			return null;
			
		}

    }
	
/*	@RequestMapping("/logdel")
    public String logDel(HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(name="id",required=false,defaultValue="1") int id) {
		
		int result = logconfigservice.del(id);
		if(result>0){
			CommUtil.showMsg(request,response, "操作成功", URLConfig.LOG_LIST+"?page=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.LOG_LIST+"?page=1");
			return null;
			
		}
    }*/
	
	@RequestMapping(URLConfig.LOG_EDIT)
    public String logUpdate(HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(name="id",required=false,defaultValue="1") int id) {
		
		LogConfig logconfig = logconfigservice.getById(id);
		model.addAttribute("logconfig",logconfig);
        return "logConfigEdit";
    }
	
	@RequestMapping(URLConfig.LOG_EDIT_AC)
    public String logUpdateAction(
    		HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(value = "selector", required = false, defaultValue = "HBASE")String selector,
    		@RequestParam(value = "logtypename", required = false, defaultValue = "")String logtypename,
    		@RequestParam(value = "logtypeflag", required = false, defaultValue = "")String logtypeflag,
    		@RequestParam(value = "logfields", required = false, defaultValue = "")String logfields,
    		@RequestParam(value = "id", required = false, defaultValue = "0") int id) {
		
		if(StringUtils.isEmpty(logtypename)){
			CommUtil.showMsg(request,response, "请填日志类型名称!", URLConfig.LOG_EDIT+"?id="+id);
			return null;
		}
        
        if(StringUtils.isEmpty(logtypeflag)){
			CommUtil.showMsg(request,response, "请填日志类型标识!", URLConfig.LOG_EDIT+"?id="+id);
			return null;
		}
        
        if(StringUtils.isEmpty(logfields)){
			CommUtil.showMsg(request,response, "请填日志字段!", URLConfig.LOG_EDIT+"?id="+id);
			return null;
		}
        
        if(id == 0){
			CommUtil.showMsg(request,response, "id error!", URLConfig.LOG_LIST);
			return null;
		}
        
        logger.info("LOG_EDIT_AC");
        LogConfig logConfig = new LogConfig();
        logConfig.setSelector(selector);
        logConfig.setLogfields(logfields);
        logConfig.setLogtypeflag(logtypeflag.toLowerCase());
        logConfig.setLogtypename(logtypename);
        logConfig.setId(id);
        int result = logconfigservice.update(logConfig);
		if(result>0){
			helloController.update(logConfig);
			CommUtil.showMsg(request,response, "操作成功", URLConfig.LOG_LIST+"?page=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.LOG_EDIT+"?id="+id);
			return null;
			
		}
    }

}
