package com.cmz.web.controller;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.patchca.color.ColorFactory;

import org.patchca.filter.predefined.DoubleRippleFilterFactory;

import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//import org.patchca.filter.predefined.MarbleRippleFilterFactory;
//import org.patchca.filter.predefined.WobbleRippleFilterFactory;
//import org.patchca.filter.predefined.CurvesRippleFilterFactory;
//import org.patchca.filter.predefined.DiffuseRippleFilterFactory;

import com.cmz.web.constant.URLConfig;
import com.cmz.web.domain.User;
import com.cmz.web.service.UserService;
import com.cmz.web.util.CommUtil;
import com.cmz.web.util.MD5Util;

@Controller
public class LoginController {
	private static Log Log = LogFactory.getLog(LoginController.class);
	@Autowired
	private UserService userService;
	
	@RequestMapping(URLConfig.LOGIN)
	public String login(){
		Log.info("login");
		return "login";
	}
	
	@RequestMapping(URLConfig.LOGOUT)
	public String logout(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		userService.unSaveSession(session);
		CommUtil.showMsg(request,response, "注销登陆成功!", URLConfig.LOGIN);
		return null;
	}
	
	@RequestMapping(value=URLConfig.LOGIN_AC,method=RequestMethod.POST)
	public String login_ac(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,Model model,String uname,String pwd,String code){
		
		if(!StringUtils.equalsIgnoreCase(session.getAttribute("captchaToken").toString(), code)){
			model.addAttribute("err", "验证码错误");
			return "login";
		}
		session.removeAttribute("captchaToken");
		
		User user = userService.getUser(uname, MD5Util.getMD5(pwd));
		if(user!=null){
			int time = (int)(System.currentTimeMillis()/1000);
			String ip = CommUtil.getIp(request);
			String sign = userService.genSign(uname,user.getPower(), time, ip);
			userService.saveSession(session, uname,user.getPower(), time, sign,ip);
			try {
				response.sendRedirect(CommUtil.getCenterURL("index"));
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "index";
		}else{
			model.addAttribute("err", "用户名或密码错误");
			return "login";
		}
	}
	
	
	
	
    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
    private static Random random = new Random();
    static {
        cs.setColorFactory(new ColorFactory() {
            @Override
            public Color getColor(int x) {
                int[] c = new int[3];
                int i = random.nextInt(c.length);
                for (int fi = 0; fi < c.length; fi++) {
                    if (fi == i) {
                        c[fi] = random.nextInt(71);
                    } else {
                        c[fi] = random.nextInt(256);
                    }
                }
                return new Color(c[0], c[1], c[2]);
            }
        });
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }
    @RequestMapping(URLConfig.LOGIN_CODE_IMG)
    public void crimg(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	cs.setFilterFactory(new DoubleRippleFilterFactory());
//        switch (random.nextInt(5)) {
//            case 0:
//                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
//                break;
//            case 1:
//                cs.setFilterFactory(new MarbleRippleFilterFactory());
//                break;
//            case 2:
//                cs.setFilterFactory(new DoubleRippleFilterFactory());
//                break;
//            case 3:
//                cs.setFilterFactory(new WobbleRippleFilterFactory());
//                break;
//            case 4:
//                cs.setFilterFactory(new DiffuseRippleFilterFactory());
//                break;
//        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        setResponseHeaders(response);
        String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
        session.setAttribute("captchaToken", token);
        System.out.println("当前的SessionID=" + session.getId() + "，验证码=" + token);
    }
    protected void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }
	
}
