package cn.com.open.user.platform.manager.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesTool {

	/**
	 * 读取项目中app.properties
	 * @return
	 */
	public static Properties getAppProperties(){
		return getProperties(new File(PropertiesTool.class.getClassLoader().getResource("/spring-oauth-server.properties").getFile()));
	}
	
	/**
	 * 读取项目中app.properties中key的value
	 * @param key
	 * @return
	 */
	public static String getAppPropertieByKey(String key){
		Properties props = getAppProperties();
		return props.getProperty(key);
	}
	
	/**
	 * 根据path获取Properties
	 * @param path
	 * @return
	 */
	public static Properties getProperties(String path){
		return getProperties(new File(path));
	}
	
	/**
	 * 根据File获取Properties
	 * @param file
	 * @return
	 */
	public static Properties getProperties(File file){
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			props.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
}
