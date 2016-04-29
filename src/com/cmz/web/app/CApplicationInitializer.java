package com.cmz.web.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.WebApplicationInitializer;

import com.cmz.web.util.PropertiesUtil;
import com.cmz.web.zookeeper.DvZookeeperConsumer;
import com.cmz.web.zookeeper.DvZookeeperProvider;

public class CApplicationInitializer implements WebApplicationInitializer {
	 
	@Override
	public void onStartup(ServletContext arg0) throws ServletException {

//		String filePath = arg0.getRealPath("WEB-INF/conf/host.conf");
//		Properties host = load(filePath);
//		DvZookeeperProvider zookeeper = new DvZookeeperProvider();
//		zookeeper.publish(host.getProperty("host"), host.getProperty("port"));

		//new DvZookeeperConsumer();
	}

	
	private static Properties load(String fileName){
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			properties.load(inputStream);
			
			return properties;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return null;
	}
}
