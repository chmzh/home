package com.cmz.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cmz.web.domain.GameConfig;

public interface GameConfigDao {
	
	public final static String Table = "gameconfig";
	public final static String fields = "`id`,`gamename`,`gameflag`,`secretkey`,`alarm`,`mails`,`open`";
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `id`=#{id} LIMIT 1")
	public GameConfig getById(@Param("id")int id);
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `gameflag`=#{gameflag} LIMIT 1")
	public GameConfig getByGameflag(@Param("gameflag")String gameflag);
	
	@Select("SELECT "+fields+" FROM "+Table+" LIMIT #{from},#{num}")
	public List<GameConfig> getPageList(@Param("from")int from,@Param("num")int num);
	
	@Select("SELECT count(*) FROM "+Table)
	public int count();
			
	
	@Select("SELECT "+fields+" FROM "+Table)
	public List<GameConfig> getGames();
	
//	@Update("UPDATE "+Table+" set gamename=#{gamename},"
//			+ "gameflag=#{gameflag},"
//			+ "secretkey=#{secretkey} WHERE `id`=#{id}")
//	public int update(@Param("gamename")String gamename,
//			@Param("gameflag")String gameflag,
//			@Param("secretkey")String secretkey,
//			@Param("id")int id);
	
	@Update("UPDATE "+Table+" set gamename=#{gameConfig.gamename},"
			+ "gameflag=#{gameConfig.gameflag},"
			+ "secretkey=#{gameConfig.secretkey},"
			+ "alarm=#{gameConfig.alarm},"
			+ "mails=#{gameConfig.mails},"
			+ "open=#{gameConfig.open} WHERE `id`=#{gameConfig.id}")
	public int update(@Param("gameConfig")GameConfig GameConfig);
	
	@Insert("INSERT INTO "+Table+"("+fields+") VALUES(0,"
			+ "#{GameConfig.gamename},"
			+ "#{GameConfig.gameflag},"
			+ "#{GameConfig.secretkey},"
			+ "#{GameConfig.alarm},"
			+ "#{GameConfig.mails},"
			+ "#{GameConfig.open})")
	public int add(@Param("GameConfig")GameConfig GameConfig);
	
	@Delete("delete from "+Table+" WHERE `id`=#{id}")
	public int del(@Param("id")int id);
	
}
