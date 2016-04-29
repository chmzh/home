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

import com.cmz.web.constant.GlobalConstant;
import com.cmz.web.constant.URLConfig;
import com.cmz.web.domain.GameConfig;
import com.cmz.web.domain.LogConfig;
import com.cmz.web.service.GameConfigService;
import com.cmz.web.service.LogConfigService;
import com.cmz.web.util.CommUtil;
import com.cmz.web.util.DesUtil;
import com.cmz.web.util.HbaseThriftClient;
import com.cmz.web.util.HiveJDBCClinet;
import com.cmz.web.util.ImpalaJDBCClinet;
import com.cmz.web.util.PageUtil;

@Controller
public class GameConfigController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogConfigController.class);
	@Autowired
	private GameConfigService gameConfigService;
	@Autowired
	private LogConfigService logconfigservice;
	@Autowired
	private APIController helloController;
	
	@RequestMapping(value=URLConfig.GAME_LIST, method = RequestMethod.GET)
    public String gameList(
    		@RequestParam(name="page",required=false,defaultValue="1") int page, Model model) {
		logger.info("GAME_LIST");
		
		int count = gameConfigService.count();
		int num = 20;
		int from = (page-1)*num;
		List<GameConfig> gameconfig = gameConfigService.getPageList(from, num);
		model.addAttribute("pages", PageUtil.getPages(page, count, num, URLConfig.GAME_LIST, null));
		model.addAttribute("gameconfig",gameconfig);
        
        return "gameConfigList";
    }
	
	@RequestMapping(URLConfig.GAME_ADD)
    public String gameAdd(HttpServletRequest request,HttpServletResponse response,Model model) {
		return "gameConfigAdd";
	}
	
	@RequestMapping(URLConfig.GAME_ADD_AC)
    public String gameaddAction(HttpServletRequest request,HttpServletResponse response,
    		Model model,GameConfig gameconfig) {
		String orign = null;
        if(StringUtils.isEmpty(gameconfig.getGamename())){
			CommUtil.showMsg(request,response, "请填写游戏名称!", URLConfig.GAME_ADD);
			return null;
		}
        
        if(StringUtils.isEmpty(gameconfig.getGameflag())){
			CommUtil.showMsg(request,response, "请填写游戏标识!", URLConfig.GAME_ADD);
			return null;
		}
        
        if(StringUtils.isEmpty(gameconfig.getSecretkey())){
			CommUtil.showMsg(request,response, "请填写游戏密钥!", URLConfig.GAME_ADD);
			return null;
		}

        try {
        	orign = gameconfig.getSecretkey();
			gameconfig.setSecretkey(DesUtil.encryptDES(GlobalConstant.GAME_KEY, orign));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int id = gameConfigService.add(gameconfig);
		if(id>0){
			helloController.update(gameconfig);
			createHvieTable((gameconfig.getGameflag()).toLowerCase());
			CommUtil.showMsg(request,response, "操作成功", URLConfig.GAME_LIST+"?page=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.GAME_ADD);
			return null;
			
		}

    }
	
/*	@RequestMapping("/gamedel")
    public String gameDel(HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(name="id",required=false,defaultValue="1") int id) {
		
		int result = gameConfigService.del(id);
		if(result>0){
			CommUtil.showMsg(request,response, "操作成功", "gamelist?page=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.USER_ADD);
			return null;
			
		}
    }*/
	
	@RequestMapping(URLConfig.GAME_EDIT)
    public String gameUpdate(HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(name="id",required=false,defaultValue="1") int id) {
		
		GameConfig gameconfig = gameConfigService.getById(id);
		try {
			String secretkey = DesUtil.decryptDES(GlobalConstant.GAME_KEY, gameconfig.getSecretkey());
			gameconfig.setSecretkey(secretkey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("gameconfig",gameconfig);
        return "gameConfigEdit";
    }
	
	@RequestMapping(URLConfig.GAME_EDIT_AC)
    public String gameUpdateAction(
    		HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(value = "gamename", required = false, defaultValue = "")String gamename,
    		@RequestParam(value = "gameflag", required = false, defaultValue = "")String gameflag,
    		@RequestParam(value = "secretkey", required = false, defaultValue = "")String secretkey,
    		@RequestParam(value = "alarm", required = false, defaultValue = "")boolean alarm,
    		@RequestParam(value = "mails", required = false, defaultValue = "")String mails,
    		@RequestParam(value = "open", required = false, defaultValue = "")boolean open,
    		@RequestParam(value = "id", required = false, defaultValue = "0") int id) {
		String orign = secretkey;
		if(StringUtils.isEmpty(gamename)){
			CommUtil.showMsg(request,response, "请填写游戏名称!", URLConfig.GAME_EDIT+"?id="+id);
			return null;
		}
        
        if(StringUtils.isEmpty(gameflag)){
			CommUtil.showMsg(request,response, "请填游戏标识!", URLConfig.GAME_EDIT+"?id="+id);
			return null;
		}
        
        if(StringUtils.isEmpty(secretkey)){
			CommUtil.showMsg(request,response, "请填游戏密钥!", URLConfig.GAME_EDIT+"?id="+id);
			return null;
		}
        
        if(id == 0){
			CommUtil.showMsg(request,response, "id error!", URLConfig.GAME_LIST);
			return null;
		}
        
        if(alarm){
        	if(StringUtils.isEmpty(mails)){
        		CommUtil.showMsg(request,response, "请填填写收件人", URLConfig.GAME_EDIT+"?id="+id);
    			return null;
        	}
        }
        
        try {
        	secretkey = DesUtil.encryptDES(GlobalConstant.GAME_KEY, orign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        GameConfig gameConfig = new GameConfig();
        gameConfig.setGamename(gamename);
        gameConfig.setGameflag(gameflag.toLowerCase());
        gameConfig.setSecretkey(secretkey);
        gameConfig.setAlarm(alarm);
        gameConfig.setId(id);
        gameConfig.setMails(mails);
        gameConfig.setOpen(open);
        int result = gameConfigService.update(gameConfig);
		if(result>0){
			helloController.update(gameConfig);
			CommUtil.showMsg(request,response, "操作成功", URLConfig.GAME_LIST+"?page=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.GAME_EDIT+"?id="+id);
			return null;
			
		}
    }
	
	
	@RequestMapping(URLConfig.HIVE_UPDATE)
    public String hiveUpdate(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam(name="id",required=false,defaultValue="0") int id) {
		
		if(id == 0){
			CommUtil.showMsg(request,response, "id错误", URLConfig.GAME_LIST);
			return null;
		}
		
		GameConfig gameconfig = gameConfigService.getById(id);
		String _gameid = (gameconfig.getGameflag()).toLowerCase();

		ImpalaJDBCClinet impalaJDBCClinet  = new ImpalaJDBCClinet();
		HiveJDBCClinet hiveJDBCClinet = new HiveJDBCClinet();
		
		
//		hiveJDBCClinet.query("DROP DATABASE IF EXISTS "+_gameid +" CASCADE");
//		logger.info("删除hive数据库"+_gameid+"以及所有表");
		
		hiveJDBCClinet.query("CREATE DATABASE IF NOT EXISTS "+_gameid);
		logger.info("创建hive数据库"+_gameid);
		
		//impalaJDBCClinet.query("INVALIDATE METADATA");
		//logger.info("刷新Impala成功");
		
		List<LogConfig> logConfig = logconfigservice.getAll();
		for(LogConfig log : logConfig){
			String _flag  = log.getLogtypeflag();
			String _field = log.getLogfields();

	    	String[] fieldArr = _field.split(",");
	    	String[] Family  = new String[fieldArr.length];
	    	
	    	for(int j=0;j<fieldArr.length;j++){
	    		Family[j] = "info:"+StringUtils.split(fieldArr[j],":")[0];
	    		
	    	}
	    	
	    	String hivesql = "CREATE EXTERNAL TABLE IF NOT EXISTS "+_gameid+"."+_flag+"("
	    					+ "rkey string,"+(_field.replace(":", " ")).toLowerCase()+")ROW FORMAT SERDE "
	    					+ "'org.apache.hadoop.hive.hbase.HBaseSerDe'"
	    					+ " STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'"
	    					+ " WITH SERDEPROPERTIES (\"hbase.columns.mapping\" = \":key,"
	    					+ " "+(StringUtils.join(Family,',')).toLowerCase()+"\")"
	    					+ " TBLPROPERTIES(\"hbase.table.name\" = \""+_gameid+"_"+_flag+"\")";
	    
			
	    	String h_tmp_table = "CREATE TABLE IF NOT EXISTS "+_gameid+".zh_"+_flag+"_tmp ("
	    			+ (_field.replace(":", " ")).toLowerCase()+") PARTITIONED BY (pdate string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','";
	    	
	    	String h_table = "CREATE TABLE IF NOT EXISTS "+_gameid+".zh_"+_flag+" ("
	    			+ (_field.replace(":", " ")).toLowerCase()+") PARTITIONED BY (pdate string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS PARQUET";
	    	
			
			HbaseThriftClient  hbaseThriftClient = new HbaseThriftClient();
			hbaseThriftClient.createTable(_gameid+"_"+_flag, "info");
			logger.info("创建HBASE表："+_gameid+"_"+_flag+"成功");
			
			hiveJDBCClinet.query(hivesql);
			logger.info("创建HIVE表："+_gameid+"."+_flag+"成功");
			
	    	hiveJDBCClinet.query(h_table);
	    	logger.info("创建HIVE历史记录表："+_gameid+"_"+_flag+"成功");
	    	hiveJDBCClinet.query(h_tmp_table);
	    	logger.info("创建HIVE历史记录临时表："+_gameid+"_"+_flag+"成功");

		}
		hiveJDBCClinet.closeConn();
		logger.info("关闭hive连接");

		impalaJDBCClinet.query("INVALIDATE METADATA");
		logger.info("刷新Impala成功");
		
		impalaJDBCClinet.closeConn();
		
		CommUtil.showMsg(request,response, "重新生成hive映射表", URLConfig.GAME_LIST);
		return null;
    }
	
	

	public void createHvieTable(String _gameid){
		_gameid = _gameid.toLowerCase();
		
		HbaseThriftClient  hbaseThriftClient = new HbaseThriftClient();
		ImpalaJDBCClinet impalaJDBCClinet  = new ImpalaJDBCClinet();
		HiveJDBCClinet hiveJDBCClinet = new HiveJDBCClinet();
		hiveJDBCClinet.query("CREATE DATABASE IF NOT EXISTS "+_gameid);
		
		//impalaJDBCClinet.query("INVALIDATE METADATA");
		//logger.info("刷新Impala成功");
		
		List<LogConfig> logConfig = logconfigservice.getAll();
		for(LogConfig log : logConfig){
			String _flag  = log.getLogtypeflag();
			String _field = log.getLogfields();

	    	String[] fieldArr = _field.split(",");
	    	String[] Family  = new String[fieldArr.length];
	    	
	    	for(int j=0;j<fieldArr.length;j++){
	    		Family[j] = "info:"+StringUtils.split(fieldArr[j],":")[0];
	    		
	    	}
	    	
	    	String hivesql = "CREATE EXTERNAL TABLE IF NOT EXISTS "+_gameid+"."+_flag+"("
	    					+ "rkey string,"+(_field.replace(":", " ")).toLowerCase()+")ROW FORMAT SERDE "
	    					+ "'org.apache.hadoop.hive.hbase.HBaseSerDe'"
	    					+ " STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'"
	    					+ " WITH SERDEPROPERTIES (\"hbase.columns.mapping\" = \":key,"
	    					+ " "+(StringUtils.join(Family,',')).toLowerCase()+"\")"
	    					+ " TBLPROPERTIES(\"hbase.table.name\" = \""+_gameid+"_"+_flag+"\")";
	    	
	    	
	    	String h_tmp_table = "CREATE TABLE IF NOT EXISTS "+_gameid+".zh_"+_flag+"_tmp ("
	    			+ (_field.replace(":", " ")).toLowerCase()+") PARTITIONED BY (pdate string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','";
	    	
	    	String h_table = "CREATE TABLE IF NOT EXISTS "+_gameid+".zh_"+_flag+" ("
	    			+ (_field.replace(":", " ")).toLowerCase()+") PARTITIONED BY (pdate string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS PARQUET";
	    	
			hbaseThriftClient.createTable(_gameid+"_"+_flag, "info");
			logger.info("创建HBASE表："+_gameid+"_"+_flag+"成功");
			hiveJDBCClinet.query(hivesql);
			logger.info("创建HIVE表："+_gameid+"."+_flag+"成功");
			
	    	hiveJDBCClinet.query(h_table);
	    	logger.info("创建HIVE历史记录表："+_gameid+"_"+_flag+"成功");
	    	hiveJDBCClinet.query(h_tmp_table);
	    	logger.info("创建HIVE历史记录临时表："+_gameid+"_"+_flag+"成功");
		}
		hiveJDBCClinet.closeConn();
		logger.info("关闭hive连接");

		impalaJDBCClinet.query("INVALIDATE METADATA");
		logger.info("刷新Impala成功");
		
		impalaJDBCClinet.closeConn();

    }
	
}
