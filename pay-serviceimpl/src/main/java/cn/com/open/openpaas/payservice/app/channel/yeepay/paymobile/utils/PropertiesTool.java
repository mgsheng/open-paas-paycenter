package cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils;

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
		return getProperties(new File(PropertiesTool.class.getClassLoader().getResource("/merchantInfo.properties").getFile()));
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
	/**
	 * 根据File获取Properties
	 * @param file
	 * @return
	 */
	public static Properties getProperties(File file){
		Properties props = new Properties();
		FileInputStream fis=null;
		try {
			 fis = new FileInputStream(file);
			props.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}
}
