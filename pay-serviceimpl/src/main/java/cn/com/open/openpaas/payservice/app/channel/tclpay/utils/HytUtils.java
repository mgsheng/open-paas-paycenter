package cn.com.open.openpaas.payservice.app.channel.tclpay.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.CommonConfig;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;

import net.sf.json.JSONObject;


/**
 * 名称：Hyt工具换 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */


public class HytUtils {

	public static String getCurProjectPath() {
		return System.getProperty("user.dir");
	}

	public static String getCurCertPath() {
		return System.getProperty("user.dir") + File.separator
				+ HytConstants.CERT + File.separator;
	}

	public static RSASign getRSASignObject() {
		RSASign util = new RSASign(HytUtils.getCurCertPath()
				+ CommonConfig.MERCHANT_CODE + HytConstants.CERT_SUFFIX,
				CommonConfig.MERCHANT_CERT_PWD);
		return util;
	}
	

	public static RSASign getRSASignVertifyObject() {
		RSASign util = new RSASign(HytUtils.getCurCertPath()
				+ CommonConfig.MERCHANT_PUBCERT_PATH);
		return util;
	}
	
	public static String getRequestId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
	
	public static String getOrdNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
	/*
	 * 将返回的json格式字符串组成待验签字符串
	 */
	public static String  getVertifyFromJson(JSONObject jsonObject,Map<String,String> retMap){
         String wsign="";
         Iterator it = jsonObject.keys();  
         while (it.hasNext()) {   //组织待验签字符
        	 String key = (String) it.next();  
             String value = jsonObject.getString(key); 
             retMap.put(key,value);
         }
         Map responseMap = new HashMap();
		 responseMap.putAll(retMap);
		   Set set1 = retMap.keySet();
		    Iterator iterator1 = set1.iterator();
		    while (iterator1.hasNext()) {
		      String key0 = (String)iterator1.next();
		      String tmp = retMap.get(key0);
		      if (HytStringUtils.equals(tmp, "null")||HytStringUtils.isBlank(tmp)||HytStringUtils.equals(key0, "server_sign")||HytStringUtils.equals(key0, "server_cert")) {
		    	  responseMap.remove(key0);
		      }

		    }
		    
         wsign=	coverMap2String(responseMap);
         
         return wsign;
	}
	
	/**
	 * map转出待签名字符串
	 * @param data
	 * @return
	 */
	public static String coverMap2String(Map<String, String> data) {
	    TreeMap tree = new TreeMap();
	    Iterator it = data.entrySet().iterator();
	    while (it.hasNext()) {
	      Map.Entry en = (Map.Entry)it.next();
	      String value = "";
	      if ((HytParamKeys.MERCHANT_SIGN.equals(((String)en.getKey()).trim())) || 
	        (HytParamKeys.SERVER_SIGN.equals(((String)en.getKey()).trim())) || 
	        (HytParamKeys.SERVER_CERT.equals(((String)en.getKey()).trim()))) continue;
	      tree.put(en.getKey(), en.getValue());
	    }

	    it = tree.entrySet().iterator();
	    StringBuffer sf = new StringBuffer();
	    while (it.hasNext()) {
	      Map.Entry en = (Map.Entry)it.next();
	      sf.append((String)en.getKey() + 
	    		  HytConstants.SYMBOL_EQUAL + (String)en.getValue() + HytConstants.SYMBOL_AND);
	    }

	    return sf.substring(0, sf.length() - 1);
	  }
	
	    /*
		 * 组织待验签字符串
		 */
	 public static String  getVertifyFromStr(String response,Map<String,String> retMap){
		 String[] resArr = HytStringUtils.split(response,  HytConstants.SYMBOL_AND);
		    for (int i = 0; i < resArr.length; i++) {
		      String data = resArr[i];
		      int index = HytStringUtils.indexOf(data,  HytConstants.SYMBOL_EQUAL);
		      String key = HytStringUtils.substring(data, 0, index);
		      String value = HytStringUtils.substring(data, index + 1);
		      retMap.put(key,value);
		    }
         String wsign="";
         Map responseMap = new HashMap();
		 responseMap.putAll(retMap);
		   Set set1 = retMap.keySet();
		    Iterator iterator1 = set1.iterator();
		    while (iterator1.hasNext()) {
		      String key0 = (String)iterator1.next();
		      String tmp = retMap.get(key0);
		      if (HytStringUtils.equals(tmp, "null")||HytStringUtils.isBlank(tmp)||HytStringUtils.equals(key0,HytParamKeys.SERVER_SIGN)||HytStringUtils.equals(key0,HytParamKeys.SERVER_CERT)) {
		    	  responseMap.remove(key0);
		      }

		    }
		    
         wsign=	coverMap2String(responseMap);
         
         return wsign;
	}
	 
}
