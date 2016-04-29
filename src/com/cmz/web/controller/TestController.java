package com.cmz.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	@RequestMapping(value="test",method = RequestMethod.POST)
	@ResponseBody
	public String log(@RequestBody String logs, HttpServletRequest request, HttpServletResponse response){
		return "{'code':0,'msg':"+logs+"}";
	}
}
