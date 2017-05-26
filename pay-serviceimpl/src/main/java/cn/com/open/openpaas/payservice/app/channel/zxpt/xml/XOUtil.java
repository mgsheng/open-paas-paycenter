/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: XOUtil.java 
 * @Prject: cr-comm-util
 * @Package: com.tcl.tr.util.xml 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月14日 下午4:47:11 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @ClassName: XOUtil
 * @Description: 将Java对象转成xml
 * @author: heqingqing
 * @date: 2016年10月14日 下午4:47:11
 */
public class XOUtil {
	static public String objectToXml(Object target, Class<?>... types) {
		XStream xstream = new XStream(new DomDriver());
		for (int i = 0; i < types.length; i++) {
			xstream.alias(types[i].getSimpleName().toLowerCase(), types[i]);
		}
		xstream.processAnnotations(target.getClass());


		return xstream.toXML(target);
	}
	static public String objectToXml(Object target, String charset, Class<?>... types) {
		charset = (charset == null ? "UTF-8" : charset);
		XStream xstream = new XStream(new DomDriver(charset));
		for (int i = 0; i < types.length; i++) {
			xstream.alias(types[i].getSimpleName().toLowerCase(), types[i]);
		}
		xstream.processAnnotations(target.getClass());
		return xstream.toXML(target);
	}
	static public void xmlToObject(String xml, Object object, Class<?>... types) {
		XStream xstream = new XStream(new DomDriver());
		for (int i = 0; i < types.length; i++) {
			xstream.alias(types[i].getSimpleName().toLowerCase(), types[i]);
		}
		xstream.processAnnotations(object.getClass());
		xstream.fromXML(xml, object);
	}
	static public void xmlToObject(String xml, Object object, String charset, Class<?>... types) {
		charset = (charset == null ? "UTF-8" : charset);
		XStream xstream = new XStream(new DomDriver(charset));
		for (int i = 0; i < types.length; i++) {
			xstream.alias(types[i].getSimpleName().toLowerCase(), types[i]);
		}
		xstream.processAnnotations(object.getClass());
		xstream.fromXML(xml, object);
	}
}
