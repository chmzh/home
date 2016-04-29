package com.cmz.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmz.web.constant.URLConfig;
import com.cmz.web.domain.User;
import com.cmz.web.service.UserService;
import com.cmz.web.util.CommUtil;
import com.cmz.web.util.MD5Util;
import com.cmz.web.util.PageUtil;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 用户列表
	 * @param model
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping(URLConfig.USER_LIST)
	public String listUsers(Model model,@RequestParam(name="page",required=false,defaultValue="1")int page){
		int count = userService.getCount();
		int num = 20;
		int from = (page-1)*num;
		List<User> users = userService.getUsers(from, num);
		model.addAttribute("pages", PageUtil.getPages(page, count, num, "ulist",null));
		model.addAttribute("users",users);
		return "userList";
	}
	/**
	 * 添加用户页面
	 * @param user
	 * @return
	 */
	@RequestMapping(URLConfig.USER_ADD)
	public String userForm(){
		return "userAdd";
	}
	
	@RequestMapping(value=URLConfig.USER_ADD_AC,method=RequestMethod.POST)
	public String addUser(HttpServletRequest request,HttpServletResponse response,User user){
		
		if(StringUtils.isEmpty(user.getUname())){
			CommUtil.showMsg(request,response, "请填写用户名!", URLConfig.USER_ADD);
			return null;
		}
		
		if(StringUtils.isEmpty(user.getPwd())){
			CommUtil.showMsg(request,response, "请填写密码!", URLConfig.USER_ADD);
			return null;
		}
		
		if(userService.getUser(user.getUname()) != null){
			CommUtil.showMsg(request,response, "用户已存在!", URLConfig.USER_ADD);
			return null;
		}
		
		
		user.setPwd(MD5Util.getMD5(user.getPwd()));
		int id = userService.addUser(user);
		if(id>0){
			CommUtil.showMsg(request,response, "操作成功", "ulist?pageIndex=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.USER_ADD);
			return null;

			
		}
	}
	
	
	@RequestMapping(URLConfig.USER_EDIT)
    public String userEdit(HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(name="id",required=false,defaultValue="1") int id) {
		
		User user = userService.getById(id);
		model.addAttribute("user",user);
        return "userEdit";
    }
	
	@RequestMapping(value=URLConfig.USER_EDIT_AC,method=RequestMethod.POST)
	public String editUser(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false, defaultValue = "0")int id,
			@RequestParam(value = "oldpwd", required = false, defaultValue = "")String oldpwd,
			@RequestParam(value = "pwd", required = false, defaultValue = "")String pwd,
			@RequestParam(value = "enabled", required = false, defaultValue = "true")boolean enabled
			){
		
		int result = 0;
		if(StringUtils.isEmpty(oldpwd) || StringUtils.isEmpty(pwd)){
			result = userService.updateEnabled(enabled, id);
		}else{
			User user = userService.getById(id);
			if(!user.getPwd().equals(MD5Util.getMD5(oldpwd))){
				CommUtil.showMsg(request,response, "原密码密码不对", "uedit?id="+id);
				return null;
			}
			String newPwd = MD5Util.getMD5(pwd);
			result = userService.update(newPwd, enabled, id);
		}
		
		if(result>0){
			CommUtil.showMsg(request,response, "操作成功", "ulist?pageIndex=1");
			return null;
			
		}else{
			CommUtil.showMsg(request,response, "操作失败", URLConfig.USER_ADD);
			return null;

			
		}
	}
	
}
