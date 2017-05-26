/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: Xmlformat.java 
 * @Prject: cr-comm-util
 * @Package: com.tcl.tr.util.xml 
 * @Description: json工具类
 * @author: heqingqing   
 * @date: 2016年10月8日 上午8:50:46 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.xml;

public class Xmlformat {
	static String xmlHead = "<?xml version=" + "\"1.0\"" + "encoding=" + "\"UTF-8\"" + "?>";

	public static String format(String xml) {
		return xmlHead + xml.replaceAll("ObjectNode", "xml");
	}
}
