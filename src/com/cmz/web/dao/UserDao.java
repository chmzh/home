package com.cmz.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cmz.web.domain.User;

public interface UserDao {
	
	public final static String Table = "user";
	public final static String fields = "`id`,`uname`,`pwd`,`enabled`,`qq`";
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `id`=#{id} LIMIT 1")
	public User getById(@Param("id")int id);
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `uname`=#{uname} AND `pwd`=#{pwd} LIMIT 1")
	public User getUser(@Param("uname")String uname,@Param("pwd")String pwd);
	
	@Select("SELECT "+fields+" FROM "+Table +" WHERE `uname` like '${uname}' LIMIT 1")
	public User getUserByName(@Param("uname")String uname);
	
	//@Select("SELECT "+fields+" FROM "+Table+" LIMIT #{from},#{num}")
	public List<User> getUsers(@Param("from")int from,@Param("num")int num);
	
	@Select("SELECT COUNT(*) FROM "+Table)
	public int getCount();
	
	@Update("UPDATE "+Table+" set pwd=#{pwd},enabled=#{enabled} WHERE `id`=#{id}")
	public int update(@Param("pwd")String pwd,@Param("enabled")boolean enabled,@Param("id")int id);
	
	@Update("UPDATE "+Table+" set enabled=#{enabled} WHERE `id`=#{id}")
	public int updateEnabled(@Param("enabled")boolean enabled,@Param("id")int id);
	
	
	@Insert("INSERT INTO "+Table+"("+fields+") VALUES(0,#{user.uname},#{user.pwd},#{user.enabled},#{user.qq})")
	public int addUser(@Param("user")User user);
}
