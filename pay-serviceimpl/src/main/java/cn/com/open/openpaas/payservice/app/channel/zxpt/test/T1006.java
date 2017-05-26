package cn.com.open.openpaas.payservice.app.channel.zxpt.test;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.json.XML;

import com.alibaba.fastjson.JSON;

import cn.com.open.openpaas.payservice.app.channel.zxpt.http.HttpClientUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.rsa.RSACoderUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.sign.MD5;
import cn.com.open.openpaas.payservice.app.channel.zxpt.xml.XOUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.DateUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Request;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.RequestHead;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Response;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ResponseHead;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreRequest;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreResponse;

public class T1006 {
	
	//字符集
	private static String charset = "utf-8";
	//征信平台公钥
	private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLfb0CjHdrU3ihTADP6FtgFyvYsIs+u36aYr80+BBCSa3JnJSGw7s6Mdusmyjm98T6RFq5WeJjzRQXHyOcUD/n8abpIbxfhhd9hODJfpYd1i0w3A0uNnbQubhThcb11sHQI1k70IIrJiwpcacuf8Y15F3FXCJBH5710YS9l94MgQIDAQAB";
	//商户私钥
	private static String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMCtreCQkwGo68pyqJ5t6NY4cSHeQFBiGyA8wjeXDVVSXxAdxMfJO9D6xzqE0fPlxhZVEcAQUXsGYw8Zr2Y0zPTfy+om79rCAyNkbP/igb9jYqysCNa0XKl3R/Z1zVx00VWojeUD1+6qA0+QZ//90uQhJlUfU9QNKz51x2766VXnAgMBAAECgYBehVUlMwdK6ykw2WqqvdRZMrsdGECIrngMKoJEbw+VaaFE8LWWJLv5WuzdYkb01SWF0xmwFNFD/vAdekY3Z3ObWj/OrNL297MT8aTjmH0H79OnZfeL+wChoxKk1FE1pA8iUJB0awLInv72GQ7JEJ+GnkbJb0QsSowyo9B9MwAQoQJBAPRcQhCQ5iVgofZej0Xk8T9bHk5jFU8Duu/jR8AfP/S0vbJuxfw9FU5F++rghK2oyS5yOSXCZV8WjOuN5jwzFUkCQQDJ2zWD8LCnMWQCkdBscwOEA83Mq+cur8yoc7uR2QfNpWOuTtuK1gvoPH91A3m5rl8P9pFshTibklna2IZYJIGvAkAxxtFWWo3nM0YKz7xTuo1CIKeNxDVFATeFQkENa9A1YtP5kwMVnMPITA1DDTU5wtYodfAaNv07X3aZTTCHNsixAkBH0GlVq4fts7C1CVNxgem6SfAp5O62uWzCcYpF9UTFcRXpqbyJxGUwFnXyF25zFQpVD4/lX/AnyQWWynnhWfuZAkEAvn7KG3umWpJA+RzNhUXFGpVTtn8JZdfDLQXtl7K7BEN7+xBsZ9zAjFs++QD1vUqLokqqKu4uUFNGkUlFB71dPg==";	
	
	public static String test() throws Exception {
		String paket = "";
		String sign = "";
		Request<ThirdScoreRequest> request = new Request<ThirdScoreRequest>();
		//第三方评分交易码1006
		RequestHead head = initHead("1006",DateUtil.getDateTime(new Date()));
		request.setHead(head);
		List<ThirdScoreRequest> list = new ArrayList<ThirdScoreRequest>();
		ThirdScoreRequest rquestdetail = initThirdScoreRequest();
		list.add(rquestdetail);
		request.setBody(list);
		String requestXml = XOUtil.objectToXml(request, Request.class, RequestHead.class, ThirdScoreRequest.class);
//		XStream xstream=XMLUtil.fromXML(requestXml);
		System.out.println(requestXml);
		//请求xml示例
		/*
		 <zxpt>
		  <head>
		    <version>V1.0</version>
		    <charSet>utf-8</charSet>
		    <source>奥鹏教育学生平台</source>
		    <des>zxpt</des>
		    <app>奥鹏App</app>
		    <tranCode>1006</tranCode>
		    <tranId>2017050612245533</tranId>
		    <tranRef>奥鹏商户</tranRef>
		    <timeStamp>20170506125533</timeStamp>
		    <tranTime>20170506</tranTime>
		    <reserve>奥鹏商户测试</reserve>
		  </head>
		  <body>
		    <thirdscorerequest>
		      <orderId>2017050612245533</orderId>
		      <scoreChannel>2</scoreChannel>
		      <scoreMethod>1</scoreMethod>
		      <certType>0</certType>
		      <certNo>420922198509103814</certNo>
		      <name>张三</name>
		      <mobile>13535413804</mobile>
		      <reasonNo>01</reasonNo>
		      <entityAuthCode>02aa19bc</entityAuthCode>
		      <cardNo>62222744552211111112</cardNo>
		      <authDate>2016-10-12 12:01:01</authDate>
		      <reserve>第三方评分测试</reserve>
		    </thirdscorerequest>
		  </body>
		</zxpt>
		 */	
		
		paket = RSACoderUtil.encrypt(requestXml.getBytes(charset), charset, publicKey);
		sign = MD5.sign(paket, "123456", "utf-8");
		
		//http请求参数
		Map<String, String> params = new HashMap<String, String>();
		
		//参数加密串
		params.put("packet", paket);
		//MD5签名
		params.put("checkValue", sign);
		//交易码
		params.put("tranCode", "1006");
		//商户号 奥鹏
		params.put("sender", "800000000003");
		
		//SIT环境
		String url = "http://zx.tcl.com/cr-web/gateway.do";
		
		//http请求
		String responsexml = HttpClientUtil.httpPost(url, params);
		//解密
		String decode = new String(RSACoderUtil.decrypt(responsexml, key, charset), charset);
		String decodexml = URLDecoder.decode(decode, charset);
		
//		Response<ThirdScoreResponse>packet=new Response<ThirdScoreResponse>();
//		XOUtil.xmlToObject(decodexml,packet,"utf-8",Response.class, ResponseHead.class, ThirdScoreResponse.class);
	/*	
		JSONObject xmlJSONObj = XML.toJSONObject(decodexml);
		com.alibaba.fastjson.JSONObject jsonObject = null;
		jsonObject = JSON.parseObject(xmlJSONObj.toString());
		String zxpt=jsonObject.getString("zxpt");
		com.alibaba.fastjson.JSONObject zxptjson=JSON.parseObject(zxpt);
		String body=zxptjson.getString("body");
		com.alibaba.fastjson.JSONObject bodyjson=JSON.parseObject(body);
		String thirdscoreresponse=bodyjson.getString("thirdscoreresponse");
		net.sf.json.JSONObject obj = new net.sf.json.JSONObject().fromObject(thirdscoreresponse);//将json字符串转换为json对象  
		ThirdScoreResponse jb = (ThirdScoreResponse)net.sf.json.JSONObject.toBean(obj,ThirdScoreResponse.class);//将建json对象转换为Person对象  
		String aa=jb.getOrderId();
		
		Map<String ,Object>map=jb.getMap();
		List<Map<String ,String>>recordsList=(List<Map<String, String>>) map.get("records");*/
		JSONObject xmlJSONObj = XML.toJSONObject(decodexml);
		com.alibaba.fastjson.JSONObject jsonObject = null;
		jsonObject = JSON.parseObject(xmlJSONObj.toString());
		String zxpt=jsonObject.getString("zxpt");
		com.alibaba.fastjson.JSONObject zxptjson=JSON.parseObject(zxpt);
		String body=zxptjson.getString("body");
		com.alibaba.fastjson.JSONObject bodyjson=JSON.parseObject(body);
		String thirdscoreresponse=bodyjson.getString("thirdscoreresponse");    
		com.alibaba.fastjson.JSONObject thirdsponsejson=JSON.parseObject(thirdscoreresponse);
		String orderId=thirdsponsejson.getString("orderId");
		String orderNo=thirdsponsejson.getString("orderNo");
		String errorCode=thirdsponsejson.getString("errorCode");
		String thirdMap=thirdsponsejson.getString("map");
		   Document doc = null;    
	        try {    
	            doc = DocumentHelper.parseText(decodexml);    
	        } catch (DocumentException e2) {    
	            // TODO 自动生成 catch 块    
	            e2.printStackTrace();    
	        }    
	        Element root = doc.getRootElement();// 指向根节点    
	        String score="";
	        // normal解析    
	        Element body1 = root.element("body");  
	        Element thirdscoreresponse1 = body1.element("thirdscoreresponse");  
	        Element map1= thirdscoreresponse1.element("map");
	        try {    
	            List lstTime = map1.elements("entry");// 所有的Item节点    
	            for (int i = 0; i < lstTime.size(); i++) {    
	                Element etime = (Element) lstTime.get(i);    
	                Element start = etime.element("string"); 
	                if(start.getTextTrim().equals("records")){
	                	Element end = etime.element("list");
	                	 Element listmap = end.element("map"); 
	                	 List listentry = listmap.elements("entry");
	                	 for(int j=0;j<listentry.size();j++){
	                		 Element etime1 = (Element) listentry.get(j);
	                		 List aa = etime1.elements("string");
	                		 for(int k=0;k<aa.size();k++){
	                			 Element credooScores = (Element) aa.get(k); 
	                			// Element credooScore=credooScores.element("string");
	                			 if( credooScores.getTextTrim().equals("credooScore")){
	                				 System.out.println("start1.getTextTrim()=" + credooScores.getTextTrim()); 
	                				 Element Scores = (Element) aa.get(k+1);
	                				 score=Scores.getTextTrim();
	                				 System.out.println("score.getTextTrim()=" + Scores.getTextTrim()); 
	                				 break;
	                			 }
	                			 break;
	                		 }
	                	}
	                 }
	                   
	   
	            }    
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        }    
		return decodexml;
	}
	
	public static RequestHead initHead(String tranCode,String tranId) {
		//请求消息头
		RequestHead rh = new RequestHead();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		//初始化报文头
		rh.setVersion("V1.0");
		rh.setCharSet("utf-8");
		rh.setSource("奥鹏教育学生平台");
		rh.setDes("zxpt");
		rh.setApp("奥鹏App");
		rh.setTranCode(tranCode);
		rh.setTranId(tranId);
		rh.setTranRef("奥鹏商户");
		rh.setReserve("奥鹏商户测试");
		rh.setTranTime(df2.format(new Date()));
		rh.setTimeStamp(df.format(new Date()));
		return rh;
	}
	
	public static ThirdScoreRequest initThirdScoreRequest() {
		//请求消息体
		ThirdScoreRequest thirdScoreRequest = new ThirdScoreRequest();
		thirdScoreRequest.setOrderId(DateUtil.getCurrentDateTime());
		
		thirdScoreRequest.setCertNo("420922198509103814");
		thirdScoreRequest.setCertType("0");
		thirdScoreRequest.setName("张四");
		thirdScoreRequest.setReserve("第三方评分测试");
		thirdScoreRequest.setScoreChannel("2");
		thirdScoreRequest.setScoreMethod("1");
		thirdScoreRequest.setMobile("13535413805");
		thirdScoreRequest.setCardNo("62222744552211111112");
		thirdScoreRequest.setReasonNo("01");
		thirdScoreRequest.setEntityAuthCode("02aa19bc");
		thirdScoreRequest.setAuthDate("2016-10-12 12:01:01");
		return thirdScoreRequest;
	}
	
	public static void main(String[] args) throws Exception {
		String t1006 = test();
		//System.out.println("=====t1006=====" + t1006);
	}
}
