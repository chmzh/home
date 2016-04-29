package com.cmz.web.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmz.web.constant.GlobalConstant;
import com.cmz.web.dao.UserDao;
import com.cmz.web.domain.User;
import com.cmz.web.util.CommUtil;
import com.cmz.web.util.MD5Util;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public int addUser(User user){
		int id = userDao.addUser(user);
		return id;
	}
	
	//根据ID查询
	public User getById(int id){
		return userDao.getById(id);
	}
	
	/**
	 * 用户列表
	 * @param from
	 * @param num
	 * @return
	 */
	public List<User> getUsers(int from,int num){
		List<User> users = userDao.getUsers(from, num);
		return users;
	}
	
	/**
	 * 查找用户
	 * @param uname
	 * @param pwd
	 * @return
	 */
	public User getUser(String uname,String pwd){
		User user = userDao.getUser(uname,pwd);
		return user;
	}
	
	/**
	 * 查找用户
	 * @param uname
	 * @param pwd
	 * @return
	 */
	public User getUser(String uname){
		User user = userDao.getUserByName(uname);
		return user;
	}
	
	/**
	 * 获取用户总数
	 * @return
	 */
	public int getCount(){
		return userDao.getCount();
	}
	
	/*
	 * 设置使用状态
	 * */
	public int updateEnabled(boolean enabled, int id){
		return userDao.updateEnabled(enabled, id);
	}
	
	/*
	 * 修改密码
	 * */
	public int update(String pwd, boolean enabled, int id){
		return userDao.update(pwd, enabled, id);
	}
	
	
	/**
	 * 是否已登陆
	 * @param session
	 * @return
	 */
	public boolean isLogin(HttpServletRequest request){
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(GlobalConstant.LOGIN_NAME);
		int time = session.getAttribute(GlobalConstant.LOGIN_TIME) == null?0:(int)session.getAttribute(GlobalConstant.LOGIN_TIME);
		int power = session.getAttribute(GlobalConstant.LOGIN_POWER) == null?0:(int)session.getAttribute(GlobalConstant.LOGIN_POWER);
		String ip = CommUtil.getIp(request);
		String sign = (String) session.getAttribute(GlobalConstant.LOGIN_SIGN);
		
		boolean empty = StringUtils.isEmpty(name) || time == 0 || StringUtils.isEmpty(ip) || StringUtils.isEmpty(sign);
		if(empty){
			return false;
		}
		String sign1 = genSign(name,power, time, ip);
		if(!sign.equals(sign1)){
			return false;
		}
		return true;
	}
	
	
	
	
	/**
	 * 保存会话
	 * @param httpSession
	 */
	public void saveSession(HttpSession httpSession,String name,int power,int time,String sign,String ip){
		httpSession.setAttribute(GlobalConstant.LOGIN_NAME, name);
		httpSession.setAttribute(GlobalConstant.LOGIN_TIME, time);
		//httpSession.setAttribute(GlobalConstant.LOGIN_IP, ip);
		httpSession.setAttribute(GlobalConstant.LOGIN_POWER, power);
		httpSession.setAttribute(GlobalConstant.LOGIN_SIGN, sign);
	}
	
	public void unSaveSession(HttpSession httpSession){
		httpSession.removeAttribute(GlobalConstant.LOGIN_NAME);
		httpSession.removeAttribute(GlobalConstant.LOGIN_TIME);
		//httpSession.removeAttribute(GlobalConstant.LOGIN_IP);
		httpSession.removeAttribute(GlobalConstant.LOGIN_POWER);
		httpSession.removeAttribute(GlobalConstant.LOGIN_SIGN);
	}
	
	public String genSign(String name,int power,int time,String ip){
		return MD5Util.getMD5(power+ip+name+time+GlobalConstant.LOGIN_KEY);
	}
}
