package com.cmz.web.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 属性工具
 * @author mingzhou.chen
 * 28458942@qq.com
 * 2015年12月28日 下午3:04:32
 */
public class PropertiesUtil {
	
	private static Map<String, Map<String,String>> propMap = new HashMap<String, Map<String,String>>();
	private static Set<PropertyListener> listeners = new HashSet<PropertyListener>();
	
	public static void setListener(PropertyListener listener){
		listeners.add(listener);
	}
	
	static{
		loadAllConf();
	}
	
	public static Map<String, String> getConf(String name){
		return propMap.get(name);
	}
	
	public static Map<String, String> getJDBCConf(){
		return propMap.get("jdbc");
	}
	
	public static void loadAllConf(){
		File dir;
		try {
			dir = new File(CommUtil.getWebRoot()+"/WEB-INF/conf");
			if(dir!=null){
				File[] files = dir.listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File pathname) {
						if(pathname.getName().indexOf(".properties")>0){
							return true;
						}
						return false;
					}
				});
				if(files!=null && files.length>0){
					for(File file : files){
						load(file.getAbsolutePath());
					}
				}
				
			}
			for (PropertyListener listener : listeners) {
				listener.reload();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void load(String fileName){
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			properties.load(inputStream);
			
			Set<Object> keys = properties.keySet();
			Map<String, String> map = new HashMap<String, String>();
			for(Object key:keys){
				map.put(key.toString(), properties.getProperty((String) key));
			}
			propMap.put(fileName.substring(fileName.lastIndexOf("/")+1, fileName.lastIndexOf(".")), map);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	}
}
