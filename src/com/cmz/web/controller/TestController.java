package com.cndw.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	@RequestMapping(value="/test",method = RequestMethod.GET)
	public String log(HttpServletRequest request, HttpServletResponse response,Model model){
		model.addAttribute("name", "<html>你号</html>");
		return "test";
	}
}
