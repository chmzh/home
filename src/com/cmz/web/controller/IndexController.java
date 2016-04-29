package com.cmz.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmz.web.constant.GlobalConstant;

@Controller
public class IndexController {
	@RequestMapping("index")
	public String index(HttpServletRequest request,HttpServletResponse response,Model model){
		HttpSession session = request.getSession();
		model.addAttribute("adminUser",session.getAttribute(GlobalConstant.LOGIN_NAME));
		return "index";
	}
}
