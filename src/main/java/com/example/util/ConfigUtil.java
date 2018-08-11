package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtil {
	  
	private static Logger logger=Logger.getLogger(ConfigUtil.class.getName());
	
	private static Properties props=new Properties();
	
	private static final String path="init.properties";
	
	static {
		
		ClassLoader loader = ConfigUtil.class.getClassLoader();
		InputStream inputStream=null;
		try {
			inputStream=loader.getResourceAsStream(path);
			props.load(inputStream);
		} catch (IOException e) {
			logger.error(e);
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
					inputStream=null;
				} catch (IOException e) {
					logger.error(e);
				}
				
			}
		}
	}
	
	public static String getProperty(String key){
		return props.getProperty(key);
	}
	
	public static String getProperty(String key,String defaultValue){
		return props.getProperty(key, defaultValue);
	}
}
