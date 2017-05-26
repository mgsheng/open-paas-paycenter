package cn.com.open.openpaas.payservice.web.api.order;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
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
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreRequest;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreResponse;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.app.zxpt.model.PayZxptInfo;
import cn.com.open.openpaas.payservice.app.zxpt.service.PayZxptInfoService;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 *征信平台第三方
 */
@Controller
@RequestMapping("/zxpt/bqs/")
public class ZxptBqsController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(ZxptBqsController.class);
	
	 @Autowired
	 private PayZxptInfoService payZxptInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 
	/**
	 * 1301白骑士黑名单信息请求
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("request")
    public String bqsRequest(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception  {
        long startTime = System.currentTimeMillis();
    	String fullUri=payserviceDev.getServer_host()+"zxpt/bqs/errorPayChannel";
        String appId = request.getParameter("appId");
    	String outTradeNo=request.getParameter("outTradeNo");
    	String certNo=request.getParameter("certNo");
        String merchantId=request.getParameter("merchantId");
        String mobile=request.getParameter("mobile");
	    String channelId=request.getParameter("channelId");
        String signature=request.getParameter("signature");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String userName="";
    	if (!nullEmptyBlankJudge(request.getParameter("userName"))){
    		userName=new String(request.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
    	}
    	String parameter="";
    	if (!nullEmptyBlankJudge(request.getParameter("parameter"))){
    		parameter=new String(request.getParameter("parameter").getBytes("iso-8859-1"),"utf-8");
    	}
		String newId="";
		newId=SysUtil.careatePayOrderId();
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(newId);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId("BQS");//
	    payServiceLog.setPaymentId("BQS");
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.BQS_REQUEST_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,merchantId,appId,mobile,certNo,userName,channelId))){
        	//paraMandaChkAndReturn(1, response,"必传参数中有空值");
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
        //获取商户信息
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	//paraMandaChkAndReturn(2, response,"商户ID不存在");
    		payServiceLog.setErrorCode("2");
    		payServiceLog.setStatus("error");
    		payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
    		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo );
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("userName", userName);
   		sParaTemp.put("mobile", mobile);
   		sParaTemp.put("channelId", channelId);
   		sParaTemp.put("certNo", certNo);
   		sParaTemp.put("parameter", parameter);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			//paraMandaChkAndReturn(3, response,"认证失败");
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		}
		//查询第三方征信报告订单是否存在
		PayZxptInfo payZxptInfo=payZxptInfoService.findByMerchantOrderId(outTradeNo,appId);
		Boolean f=false;
		if(payZxptInfo!=null){
				payServiceLog.setErrorCode("5");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
		}else{ 
			//创建订单
			payZxptInfo=new PayZxptInfo();
			payZxptInfo.setId(newId);
			payZxptInfo.setAppId(appId);
			payZxptInfo.setAuthDate(new Date());
			payZxptInfo.setCertNo(certNo);
			payZxptInfo.setCertType(payserviceDev.getZxpt_cert_type());
			payZxptInfo.setEntityAuthCode(payserviceDev.getZxpt_entityAuthCode());
			payZxptInfo.setMerchantOrderId(outTradeNo);
			payZxptInfo.setPhone(mobile);
			payZxptInfo.setScoreChannel(channelId);
			payZxptInfo.setScoreMethod(payserviceDev.getZxpt_score_method());
			payZxptInfo.setUserName(userName);
			f=payZxptInfoService.savePayZxptInfo(payZxptInfo);
		}
		if(f){
			//白骑士交易码1301
			String paket="";
			String sign="";
			Request<BqsFraudlistRequest> bqsrequest=new Request<BqsFraudlistRequest>();
			//白骑士交易码1301
			RequestHead head=initHead("1301", DateUtil.getDateTime(new Date()));
			bqsrequest.setHead(head);
			List<BqsFraudlistRequest> list=new ArrayList<BqsFraudlistRequest>();
			BqsFraudlistRequest bRequest=init1301Request(payZxptInfo);
			list.add(bRequest);
			bqsrequest.setBody(list);
			String requestXml=XOUtil.objectToXml(bqsrequest,  Request.class, RequestHead.class, BqsFraudlistRequest.class);
			System.out.println(requestXml);
			paket = RSACoderUtil.encrypt(requestXml.getBytes(payserviceDev.getZxpt_charset()), payserviceDev.getZxpt_charset(), payserviceDev.getZxpt_public_key());
			sign = MD5.sign(paket, "123456", "utf-8");
			
			//http请求参数
			Map<String, String> zxptParams = new HashMap<String, String>();
			//参数加密串
			zxptParams.put("packet", paket);
			//MD5签名
			zxptParams.put("checkValue", sign);
			//交易码
			zxptParams.put("tranCode", "1301");
			//商户号 奥鹏
			zxptParams.put("sender", payserviceDev.getZxpt_sender());
			//SIT环境
			String url = payserviceDev.getZxpt_third_url();
			//http请求
			String responsexml = HttpClientUtil.httpPost(url, zxptParams);
			//解密
			String decode = new String(RSACoderUtil.decrypt(responsexml, payserviceDev.getZxpt_key(), payserviceDev.getZxpt_charset()), payserviceDev.getZxpt_charset());
			String decodexml = URLDecoder.decode(decode, payserviceDev.getZxpt_charset());
			
			 String decision="";
			 Map<String, Object> map=new HashMap<String,Object>();
		    	if(!nullEmptyBlankJudge(decodexml)){
		    		JSONObject xmlJSONObj = XML.toJSONObject(decodexml);
		    		com.alibaba.fastjson.JSONObject jsonObject = null;
		    		jsonObject = JSON.parseObject(xmlJSONObj.toString());
		    		String zxpt=jsonObject.getString("zxpt");
		    		com.alibaba.fastjson.JSONObject zxptjson=JSON.parseObject(zxpt);
		    		String body=zxptjson.getString("body");
		    		com.alibaba.fastjson.JSONObject bodyjson=JSON.parseObject(body);
		    		String bqsfraudlistresponse=bodyjson.getString("bqsfraudlistresponse");
		    		com.alibaba.fastjson.JSONObject bqsresponsejson=JSON.parseObject(bqsfraudlistresponse);
		    		decision=bqsresponsejson.getString("decision");
		    		String orderId=bqsresponsejson.getString("orderId");
		    		String orderNo=bqsresponsejson.getString("orderNo");
		    		String errorCode=bqsresponsejson.getString("errorCode");
		    		System.out.println("decision=="+decision);
		    		System.out.println("orderId=="+orderId);
		    		System.out.println("orderNo=="+orderNo);
		    		 if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("ES000000")){
			   		     if(!nullEmptyBlankJudge(decision)){
			   		    	 PayZxptInfo newpayZxptInfo= payZxptInfoService.findById(orderId);
			     	    	if(newpayZxptInfo!=null){
			     	    		newpayZxptInfo.setZxptOrderNo(orderNo);
			     	    		newpayZxptInfo.setDecision(decision);
			     	    		newpayZxptInfo.setZxptRequestStatus("1");
			     	    		payZxptInfoService.updateZxptInfo(newpayZxptInfo);
			     		    	fullUri=payserviceDev.getServer_host()+"zxpt/bqs/bqsBack?decision="+decision;
			     	    	    payServiceLog.setLogName(PayLogName.THIRD_REQUEST_END);
			     			    UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
			     			    return "redirect:"+fullUri;
			     	    	}else{
			     	    		 map.clear();
			     		    	 map.put("status", "error");
			     		    	 map.put("errMsg", "订单信息查询失败");
			     		    	 map.put("errorCode", "N10002");
			     		    	payServiceLog.setErrorCode("5");
								payServiceLog.setStatus("error");
								payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
								UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"5";
			     	    	} 
			   		     }else{
			   		    	 map.clear();
			 		    	 map.put("status", "error");
			 		    	 map.put("errMsg", "白骑士风险结果为空");
			 		    	 map.put("errorCode", "N10003"); 
			 		    	payServiceLog.setErrorCode("6");
							payServiceLog.setStatus("error");
							payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
							UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"6";
			   		     }
			   		    }else{
				   		     map.clear();
					    	 map.put("status", "error");
					    	 map.put("errMsg", "白骑士返回错误");
					    	 map.put("errorCode", errorCode);
					    	payServiceLog.setErrorCode("7");
							payServiceLog.setStatus("error");
							payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
							UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					        return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"7"+"&failureCode="+errorCode+"&failureMsg=白骑士返回错误";
				   		}
		    	/*	
		    		Response<BqsFraudlistResponse>packet=new Response<BqsFraudlistResponse>();
		    		//decodexml=decodexml.replace("&quot;", "'");
		       	try {
		       		//将xml转成对象
		    		XOUtil.xmlToObject(decodexml,packet,"utf-8",Response.class,ResponseHead.class,BqsFraudlistResponse.class);
		   		    if(packet.getBody()==null||packet.getBody().size()==0){
		   	    	 map.put("status", "error");
		   	    	 map.put("errMsg", "获取body失败");
		   	    	 map.put("errorCode","B10001");
		   		    }else{
		   		    	//获取返回信息的body内容
		   		    	List<BqsFraudlistResponse>listback=packet.getBody();
		   		    	BqsFraudlistResponse bqsFraudlistResponse=null;
		   		    	if(list!=null && list.size()>0){
		   		    		bqsFraudlistResponse=listback.get(0);
		   		    	}
		   		    //业务方请求流水号
		   		    String orderId=bqsFraudlistResponse.getOrderId();
		   		    //第三方征信平台流水号
		   		    String orderNo=bqsFraudlistResponse.getOrderNo();
		   		  
		   		    String errorCode=bqsFraudlistResponse.getErrorCode();
		   		    if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("ES000000")){
		   		    	decision= bqsFraudlistResponse.getDecision();
		   		     if(!nullEmptyBlankJudge(decision)){
		   		    	 PayZxptInfo newpayZxptInfo= payZxptInfoService.findById(orderId);
		     	    	if(newpayZxptInfo!=null){
		     	    		newpayZxptInfo.setZxptOrderNo(orderNo);
		     	    		newpayZxptInfo.setDecision(decision);
		     	    		newpayZxptInfo.setZxptRequestStatus("1");
		     	    		payZxptInfoService.updateZxptInfo(newpayZxptInfo);
		     	    		 map.clear();
		     		    	 map.put("status", "");
		     		    	 map.put("errMsg", "订单信息查询失败");
		     		    	 map.put("errorCode", "N10001");
		     	    	}else{
		     	    		 map.clear();
		     		    	 map.put("status", "error");
		     		    	 map.put("errMsg", "订单信息查询失败");
		     		    	 map.put("errorCode", "N10002");	
		     	    	} 
		   		     }else{
		   		    	 map.clear();
		 		    	 map.put("status", "error");
		 		    	 map.put("errMsg", "白骑士风险结果为空");
		 		    	 map.put("errorCode", "N10003"); 
		   		     }
		   		    
		   		    }else{
		   		     map.clear();
			    	 map.put("status", "error");
			    	 map.put("errMsg", "白骑士返回错误");
			    	 map.put("errorCode", errorCode);	
		   		    }
		   		   
		   		    }
		       	} catch (Exception e) {
		       	     e.printStackTrace();
		       	     map.clear();
			    	 map.put("status", "error");
			    	 map.put("errMsg", "白骑士返回错误");
			    	 map.put("errorCode", "N10004");	
		   		}*/
		    	}else{
		    		payServiceLog.setErrorCode("8");
					payServiceLog.setStatus("error");
					payServiceLog.setLogName(PayLogName.BQS_REQUEST_END);
					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8";
		    	}
			 	   
		  }
		  return "redirect:" + fullUri;
    }
  
    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "bqsBack", method = RequestMethod.GET)
    public void bqsBack(HttpServletRequest request,HttpServletResponse response, Model model){
    	String decision=request.getParameter("decision");
    	 Map<String, Object> map=new HashMap<String,Object>();
     	map.put("status", "ok");
     	map.put("errMsg", "");
     	map.put("errorCode", "");
     	map.put("decision",decision);   
    	writeSuccessJson(response,map);
    }
    /**
     * 返回错误信息
     */
    @RequestMapping(value = "errorPayChannel", method = RequestMethod.GET)
    public void payError(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	 Map<String, Object> map=new HashMap<String,Object>();
    		String errorMsg="";
    		if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
        		errorMsg="必传参数中有空值";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
        		errorMsg="验证失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="订单信息查询失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
        		errorMsg="商户ID不存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
        		errorMsg="订单号已经存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="白骑士风险结果为空";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="白骑士返回错误:"+failureCode+"--错误原因："+failureMsg;
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="白骑士XML结果为空";
        	}
    	   map.put("status", "error");
    	   map.put("errorCode", errorCode);
    	   map.put("errMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
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
	
	public static ThirdScoreRequest initThirdScoreRequest(PayZxptInfo payZxptInfo) {
		
	/*	ThirdScoreRequest thirdScoreRequest = new ThirdScoreRequest();
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
		thirdScoreRequest.setAuthDate("2016-10-12 12:01:01");*/
		//请求消息体
	    ThirdScoreRequest thirdScoreRequest = new ThirdScoreRequest();
		thirdScoreRequest.setOrderId(payZxptInfo.getId());
		
		thirdScoreRequest.setCertNo(payZxptInfo.getCertNo());
		thirdScoreRequest.setCertType(payZxptInfo.getCertType());
		thirdScoreRequest.setName(payZxptInfo.getUserName());
		thirdScoreRequest.setReserve(payZxptInfo.getReserve());
		thirdScoreRequest.setScoreChannel(payZxptInfo.getScoreChannel());
		thirdScoreRequest.setScoreMethod(payZxptInfo.getScoreMethod());
		thirdScoreRequest.setMobile(payZxptInfo.getPhone());
		thirdScoreRequest.setCardNo(payZxptInfo.getCardNo());
		thirdScoreRequest.setReasonNo(payZxptInfo.getReasonNo());
		thirdScoreRequest.setEntityAuthCode(payZxptInfo.getEntityAuthCode());
		thirdScoreRequest.setAuthDate(DateTools.dateToString(payZxptInfo.getAuthDate(), DateTools.FORMAT_ONE));
		return thirdScoreRequest;
	}
	
	public static BqsFraudlistRequest init1301Request(PayZxptInfo payZxptInfo) {
		
		/*//请求消息体
		BqsFraudlistRequest bRequest=new BqsFraudlistRequest();
		bRequest.setChannelNo("1");
		bRequest.setCertNo("522528199303153636");
		bRequest.setName("朱怀龙a");
		bRequest.setMobile("18798897113");
		bRequest.setEntityAuthCode("abcdef");
		bRequest.setEntityAuthDate("2017-04-21 08:37:00");
		bRequest.setOrderId(DateUtil.getCurrentDateTime());*/
		
		//请求消息体
		BqsFraudlistRequest bRequest=new BqsFraudlistRequest();
		bRequest.setChannelNo(payZxptInfo.getScoreChannel());
		bRequest.setCertNo(payZxptInfo.getCertNo());
		bRequest.setName(payZxptInfo.getUserName());
		bRequest.setMobile(payZxptInfo.getPhone());
		bRequest.setEntityAuthCode(payZxptInfo.getEntityAuthCode());
		bRequest.setEntityAuthDate(DateTools.dateToString(payZxptInfo.getAuthDate(), DateTools.FORMAT_ONE));
		bRequest.setOrderId(payZxptInfo.getId());
		return bRequest;
		
	}
	

}