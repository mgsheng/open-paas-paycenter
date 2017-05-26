package cn.com.open.openpaas.payservice.app.channel.zxpt.test;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.XML;

import com.alibaba.fastjson.JSON;

import cn.com.open.openpaas.payservice.app.channel.zxpt.http.HttpClientUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.rsa.RSACoderUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.sign.MD5;
import cn.com.open.openpaas.payservice.app.channel.zxpt.xml.XOUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.BqsFraudlistRequest;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.BqsFraudlistResponse;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.DateUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Request;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.RequestHead;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Response;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ResponseHead;

public class T1301 {
	
	//字符集
	private static String charset = "utf-8";
	//征信平台公钥
	private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLfb0CjHdrU3ihTADP6FtgFyvYsIs+u36aYr80+BBCSa3JnJSGw7s6Mdusmyjm98T6RFq5WeJjzRQXHyOcUD/n8abpIbxfhhd9hODJfpYd1i0w3A0uNnbQubhThcb11sHQI1k70IIrJiwpcacuf8Y15F3FXCJBH5710YS9l94MgQIDAQAB";
	//商户私钥
	private static String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMCtreCQkwGo68pyqJ5t6NY4cSHeQFBiGyA8wjeXDVVSXxAdxMfJO9D6xzqE0fPlxhZVEcAQUXsGYw8Zr2Y0zPTfy+om79rCAyNkbP/igb9jYqysCNa0XKl3R/Z1zVx00VWojeUD1+6qA0+QZ//90uQhJlUfU9QNKz51x2766VXnAgMBAAECgYBehVUlMwdK6ykw2WqqvdRZMrsdGECIrngMKoJEbw+VaaFE8LWWJLv5WuzdYkb01SWF0xmwFNFD/vAdekY3Z3ObWj/OrNL297MT8aTjmH0H79OnZfeL+wChoxKk1FE1pA8iUJB0awLInv72GQ7JEJ+GnkbJb0QsSowyo9B9MwAQoQJBAPRcQhCQ5iVgofZej0Xk8T9bHk5jFU8Duu/jR8AfP/S0vbJuxfw9FU5F++rghK2oyS5yOSXCZV8WjOuN5jwzFUkCQQDJ2zWD8LCnMWQCkdBscwOEA83Mq+cur8yoc7uR2QfNpWOuTtuK1gvoPH91A3m5rl8P9pFshTibklna2IZYJIGvAkAxxtFWWo3nM0YKz7xTuo1CIKeNxDVFATeFQkENa9A1YtP5kwMVnMPITA1DDTU5wtYodfAaNv07X3aZTTCHNsixAkBH0GlVq4fts7C1CVNxgem6SfAp5O62uWzCcYpF9UTFcRXpqbyJxGUwFnXyF25zFQpVD4/lX/AnyQWWynnhWfuZAkEAvn7KG3umWpJA+RzNhUXFGpVTtn8JZdfDLQXtl7K7BEN7+xBsZ9zAjFs++QD1vUqLokqqKu4uUFNGkUlFB71dPg==";		
	
	public static String test() throws Exception {
		String paket="";
		String sign="";
		Request<BqsFraudlistRequest> request=new Request<BqsFraudlistRequest>();
		//白骑士交易码1301
		RequestHead head=initHead("1301", DateUtil.getDateTime(new Date()));
		request.setHead(head);
		List<BqsFraudlistRequest> list=new ArrayList<BqsFraudlistRequest>();
		BqsFraudlistRequest bRequest=init1301Request();
		list.add(bRequest);
		request.setBody(list);
		
		String requestXml=XOUtil.objectToXml(request,  Request.class, RequestHead.class, BqsFraudlistRequest.class);
		
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
		    <tranCode>1301</tranCode>
		    <tranId>2017050612244535</tranId>
		    <tranRef>奥鹏商户</tranRef>
		    <timeStamp>20170506124535</timeStamp>
		    <tranTime>20170506</tranTime>
		    <reserve>奥鹏商户测试</reserve>
		  </head>
		  <body>
		    <bqsfraudlistrequest>
		      <orderId>2017050612244535</orderId>
		      <channelNo>1</channelNo>
		      <certNo>522528199303153636</certNo>
		      <name>朱怀龙</name>
		      <mobile>18798897113</mobile>
		      <entityAuthCode>abcdef</entityAuthCode>
		      <entityAuthDate>2017-04-21 08:37:00</entityAuthDate>
		    </bqsfraudlistrequest>
		  </body>
		</zxpt>
		 */

		paket= RSACoderUtil.encrypt(requestXml.getBytes(charset), charset, publicKey);
		sign = MD5.sign(paket, "123456", "utf-8");
		
		//http请求参数
		Map<String, String> params=new HashMap<String,String>();
		//参数加密串
		params.put("packet", paket);
		//MD5签名
		params.put("checkValue", sign);
		//交易码
		params.put("tranCode", "1301");
		//商户号 奥鹏
		params.put("sender", "800000000003");
		
		//SIT环境
		String url = "http://zx.tcl.com/cr-web/gateway.do";
		
		//http请求
		String responsexml = HttpClientUtil.httpPost(url, params);
		
		byte[] outdata;
		
		//解密
		outdata = RSACoderUtil.decrypt(responsexml, key, charset);
		
		String decode = new String(outdata, charset);
		String decodexml = URLDecoder.decode(decode, charset);
		JSONObject xmlJSONObj = XML.toJSONObject(decodexml);
		com.alibaba.fastjson.JSONObject jsonObject = null;
		jsonObject = JSON.parseObject(xmlJSONObj.toString());
		String zxpt=jsonObject.getString("zxpt");
		com.alibaba.fastjson.JSONObject zxptjson=JSON.parseObject(zxpt);
		String body=zxptjson.getString("body");
		com.alibaba.fastjson.JSONObject bodyjson=JSON.parseObject(body);
		String bqsfraudlistresponse=bodyjson.getString("bqsfraudlistresponse");
		com.alibaba.fastjson.JSONObject bqsresponsejson=JSON.parseObject(bqsfraudlistresponse);
		String decision=bqsresponsejson.getString("decision");
		String orderId=bqsresponsejson.getString("orderId");
		String orderNo=bqsresponsejson.getString("orderNo");
		System.out.println("decision=="+decision);
		System.out.println("orderId=="+orderId);
		System.out.println("orderNo=="+orderNo);
		
		
		System.out.println(zxpt);
		
		
		/*Response<BqsFraudlistResponse> packet=new Response<BqsFraudlistResponse>();
		XOUtil.xmlToObject(decodexml,packet,"utf-8",Response.class,ResponseHead.class,BqsFraudlistResponse.class);
		  if(packet.getBody()==null||packet.getBody().size()==0){
	   	  }else{
	   		List<BqsFraudlistResponse>listback=packet.getBody();
		    	BqsFraudlistResponse bqsFraudlistResponse=null;
		    	if(list!=null && list.size()>0){
		    		bqsFraudlistResponse=listback.get(0);
		    	}
		    //业务方请求流水号
		    String orderId=bqsFraudlistResponse.getOrderId();
		    //第三方征信平台流水号
		    String orderNo=bqsFraudlistResponse.getOrderNo();
	   	  }*/
		//返回报文样例
		/*
		 <zxpt>
		  <head>
		    <version>V1.0</version>
		    <charSet>utf-8</charSet>
		    <source>zxpt</source>
		    <des>奥鹏教育学生平台</des>
		    <app>奥鹏App</app>
		    <tranCode>1301</tranCode>
		    <tranNo>2000100000001450</tranNo>
		    <tranRef>奥鹏商户</tranRef>
		    <timeStamp>2017050612244545</timeStamp>
		    <tranTime>20170506</tranTime>
		    <reserve>奥鹏商户测试</reserve>
		    <messageCode>ES000000</messageCode>
		    <message>交易成功</message>
		  </head>
		  <body>
		    <bqsfraudlistresponse>
		      <orderId>2017050612244535</orderId>
		      <oderNo>1000010000001450</oderNo>
		      <errorCode>ES000000</errorCode>
		      <errorMessage>交易成功</errorMessage>
		      <score>0</score>
		      <decision>Review</decision>
		      <resultMap>[{"hitRules":[{"decision":"Review","memo":"灰名单-信贷行业-模型分值低","ruleId":"14668","ruleName":"身份证比对信贷行业模型分值低灰名单","score":0}],"riskType":"creditRisk","strategyDecision":"Review","strategyId":"1edbb7daad5c49d5a0fc3f48172b5259","strategyMode":"WorstMode","strategyName":"失信风险策略","strategyScore":0}]</resultMap>
		    </bqsfraudlistresponse>
		  </body>
		</zxpt>
		 */
		
		return decodexml;
		
	}
	
	public static RequestHead initHead(String tranCode,String tranId) {
		
		//请求消息头
		RequestHead rHead=new RequestHead();
		
		SimpleDateFormat dFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat dFormat2=new SimpleDateFormat("yyyyMMdd");
		//初始化报文头
		rHead.setVersion("V1.0");
		rHead.setCharSet("utf-8");
		rHead.setSource("奥鹏教育学生平台");
		rHead.setDes("zxpt");
		rHead.setApp("奥鹏App");
		rHead.setTranCode(tranCode);
		rHead.setTranId(tranId);
		rHead.setTranRef("奥鹏商户");
		rHead.setReserve("奥鹏商户测试");
		rHead.setTranTime(dFormat2.format(new Date()));
		rHead.setTimeStamp(dFormat.format(new Date()));
		return rHead;
		
	}
	
	public static BqsFraudlistRequest init1301Request() {
		
		//请求消息体
		BqsFraudlistRequest bRequest=new BqsFraudlistRequest();
		bRequest.setChannelNo("1");
		bRequest.setCertNo("522528199303153636");
		bRequest.setName("朱怀龙a");
		bRequest.setMobile("18798897113");
		bRequest.setEntityAuthCode("abcdef");
		bRequest.setEntityAuthDate("2017-04-21 08:37:00");
		bRequest.setOrderId(DateUtil.getCurrentDateTime());
		return bRequest;
		
	}

	public static void main(String[] args) throws Exception {
		String t1301=test();
		System.out.println("======t1301======"+t1301);
	}

}
