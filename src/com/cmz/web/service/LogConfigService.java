package com.cmz.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmz.web.dao.LogConfigDao;
import com.cmz.web.domain.LogConfig;


@Service
public class LogConfigService {
	
	@Autowired
	private LogConfigDao logConfigDao;
	
	/**
	 * 添加
	 * @param logconfig
	 * @return
	 */
	public int add(LogConfig logconfig){
		logconfig.setLogtypeflag((logconfig.getLogtypeflag()).toLowerCase());
		int id = logConfigDao.add(logconfig);
		return id;
	}
	
	/**
	 * 列表
	 * @param from
	 * @param num
	 * @return
	 */
	public List<LogConfig> getPageList(int from,int num){
		List<LogConfig> logconfigs = logConfigDao.getPageList(from, num);
		return logconfigs;
	}
	
	/**
	 * 所有
	 * @return
	 */
	public List<LogConfig> getAll(){
		List<LogConfig> logconfigs = logConfigDao.getAll();
		return logconfigs;
	}
	
	/**
	 * 根据id查找
	 * @param inti id
	 * @return
	 */
	public LogConfig getById(int id){
		LogConfig logconfigs = logConfigDao.getById(id);
		return logconfigs;
	}
	
	/**
	 * 根据id查找
	 * @param inti id
	 * @return
	 */
	public LogConfig getByLogtypeflag(String logtypeflag){
		LogConfig logconfigs = logConfigDao.getByLogtypeflag(logtypeflag);
		return logconfigs;
	}
	
	
	/**
	 * 更新
	 * @param inti id
	 * @return int
	 */
//	public int update(
//			String selector, String logtypename,String logtypeflag,String logfields,int id){
//		logtypeflag = logtypeflag.toLowerCase();
//		int result = logConfigDao.update(selector,logtypename,logtypeflag,logfields,id);
//		return result;
//	}
	
	/**
	 * 更新
	 * @param inti id
	 * @return int
	 */
	public int update(LogConfig logConfig){
		int result = logConfigDao.update(logConfig);
		return result;
	}
	
	/**
	 * delete
	 * @param inti id
	 * @return int
	 */
	public int del(int id){
		int result = logConfigDao.del(id);
		return result;
	}
	
	/**
	 * count
	 * @return int
	 */
	public int count(){
		int result = logConfigDao.count();
		return result;
	}

}
