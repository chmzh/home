package com.cmz.web.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cmz.web.constant.URLConfig;
import com.cmz.web.service.UserService;




/**
 * 系统拦截器
 * @author chenmingzhou
 *
 */
public class SystemInterceptor implements HandlerInterceptor {
	
	static Log log = LogFactory.getLog(SystemInterceptor.class);
	@Autowired
	private UserService userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		log.debug("afterCompletion");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		if(arg3!=null && arg3.getModel()!=null){
			arg3.getModel().put("home", URLConfig.HOME_DIR);
//			HttpSession session = arg0.getSession();
//			if(session!=null){
//				arg3.getModel().put("uname", session.getAttribute(GlobalConstant.LOGIN_NAME));
//			}
			
		}
		log.debug("postHandle");
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		String requestUri = arg0.getRequestURI();
		
		if (requestUri.indexOf(URLConfig.LOGIN_AC) > 0 || 
				requestUri.indexOf(URLConfig.LOGIN) > 0 || 
				requestUri.indexOf(URLConfig.LOGIN_CODE_IMG)>0||
				requestUri.indexOf("json")>0) {
			return true;
		}
		
		
		
		boolean isLogin = userService.isLogin(arg0);
		
		if(!isLogin){
			arg1.sendRedirect(URLConfig.HOME_DIR+URLConfig.LOGIN);
			return false;
		}
		
		log.debug("验证成功");

		// TODO 功能权限验证
		// userService.hasPermission(roleId, permission)

		return isLogin;
	}


}
