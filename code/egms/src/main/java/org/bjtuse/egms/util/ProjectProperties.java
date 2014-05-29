package org.bjtuse.egms.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public final class ProjectProperties {
	final static Properties properties;
	
	static {
		properties = new Properties();
		try {
			properties.load(new ClassPathResource("application.properties").getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取配置属性.
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Properties getProperties() {
		return ProjectProperties.properties;
	}
	
	/**
	 * 配置文件是否为空.
	 * 
	 * @return
	 */
	public static boolean isEmpty() {
		return properties == null || properties.isEmpty();
	}
}
