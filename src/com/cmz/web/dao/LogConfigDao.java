package com.cmz.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cmz.web.domain.LogConfig;

public interface LogConfigDao {
	
	public final static String Table = "logconfig";
	public final static String fields = "`id`,`selector`,`logtypename`,`logtypeflag`,`logfields`";
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `id`=#{id} LIMIT 1")
	public LogConfig getById(@Param("id")int id);
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `logtypeflag`=#{logtypeflag} LIMIT 1")
	public LogConfig getByLogtypeflag(@Param("logtypeflag")String logtypeflag);
	
	@Select("SELECT "+fields+" FROM "+Table+" LIMIT #{from},#{num}")
	public List<LogConfig> getPageList(@Param("from")int from,@Param("num")int num);
	
	@Select("SELECT "+fields+" FROM "+Table+"")
	public List<LogConfig> getAll(); 
	
	@Select("SELECT count(*) FROM "+Table)
	public int count();
			
//	@Update("UPDATE "+Table+" set selector=#{selector},"
//			+ "logtypename=#{logtypename},"
//			+ "logtypeflag=#{logtypeflag},"
//			+ "logfields=#{logfields} WHERE `id`=#{id}")
//	public int update(@Param("selector")String selector,
//			@Param("logtypename")String logtypename,
//			@Param("logtypeflag")String logtypeflag,
//			@Param("logfields")String logfields,
//			@Param("id")int id);
	
	
	@Update("UPDATE "+Table+" set selector=#{logConfig.selector},"
			+ "logtypename=#{logConfig.logtypename},"
			+ "logtypeflag=#{logConfig.logtypeflag},"
			+ "logfields=#{logConfig.logfields} WHERE `id`=#{logConfig.id}")
	public int update(@Param("logConfig")LogConfig LogConfig);
	
	@Insert("INSERT INTO "+Table+"("+fields+") VALUES(0,"
			+ "#{logConfig.selector},"
			+ "#{logConfig.logtypename},"
			+ "#{logConfig.logtypeflag},"
			+ "#{logConfig.logfields})")
	public int add(@Param("logConfig")LogConfig LogConfig);
	
	@Delete("delete from "+Table+" WHERE `id`=#{id}")
	public int del(@Param("id")int id);

	
	
	
}
