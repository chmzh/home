package com.cmz.web.util;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cmz.web.app.CApplicationListener;
import com.cmz.web.constant.URLConfig;

public class CommUtil {
	
	public final static Logger log = Logger.getLogger(CommUtil.class);
	
	/**
	 * 获取网站根目录
	 * 
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static String getWebRoot() throws Exception {
		String webRoot = CApplicationListener.getWebAppRootKey();
		if (webRoot == null || webRoot.equals("")) {
			throw new Exception("找不到网站根目录");
		}
		return webRoot;
	}

	public static String getWorkHome() {
		return System.getProperty("user.dir");
	}

	public static String getCenterURL(String url){
		return URLConfig.HOME_DIR+url;
	}
	
	public static void showMsg(HttpServletRequest request,HttpServletResponse response,String msg,String url){
		try {
			String params = "?url="+url+"&msg="+URLEncoder.encode(msg,"UTF-8");
			response.sendRedirect(CommUtil.getCenterURL("message")+params);
			
			log.info(CommUtil.getCenterURL("message")+params);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	
	
	public static String getIp(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
