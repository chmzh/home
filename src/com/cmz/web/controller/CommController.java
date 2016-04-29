package com.cmz.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommController {
	@RequestMapping("empty")
	public String empty(){
		return "empty";
	}
	
	@RequestMapping("message")
	public String succMsg(Model model,String msg,String url){
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "message";
	}
}
