package com.cmz.web.app;

import javax.servlet.ServletContextEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import com.cmz.web.constant.GlobalConstant;

@Component
public class CApplicationListener extends ContextLoaderListener{    
	private static String webAppRootKey;
	
    public static String getWebAppRootKey() {
		return webAppRootKey;
	}

	public void contextDestroyed(ServletContextEvent sce) {    
        // TODO Auto-generated method stub    
    
    }    
    
    public void contextInitialized(ServletContextEvent sce) {    
    	if(webAppRootKey == null){
    		webAppRootKey = sce.getServletContext().getRealPath("/"); 
            
            System.setProperty(GlobalConstant.WEBROOT , webAppRootKey);  
    	}
        
    }   
    
}
