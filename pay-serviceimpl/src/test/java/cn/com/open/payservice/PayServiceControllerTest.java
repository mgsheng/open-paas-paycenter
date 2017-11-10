package cn.com.open.payservice;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.ApiCommonController;
import cn.com.open.openpaas.payservice.web.api.order.OrderManualSendController;
import cn.com.open.openpaas.payservice.web.api.order.PayOrderManagerController;
import cn.com.open.openpaas.payservice.web.api.order.QueryOrderInfoController;
import cn.com.open.openpaas.payservice.web.api.order.QuerySerialRecordController;
import cn.com.open.openpaas.payservice.web.api.order.TclPayController;
import cn.com.open.openpaas.payservice.web.api.order.TestOrderCallbackController;
import cn.com.open.openpaas.payservice.web.api.order.TestOrderManualSendController;
import cn.com.open.openpaas.payservice.web.api.order.TztBindCardController;
import cn.com.open.openpaas.payservice.web.api.order.TztBindPayController;
import cn.com.open.openpaas.payservice.web.api.order.TztChangeCardController;
import cn.com.open.openpaas.payservice.web.api.order.TztUnbindCardController;
import cn.com.open.openpaas.payservice.web.api.order.UnifyCostsController;
import cn.com.open.openpaas.payservice.web.api.order.UnifyPayController;
import cn.com.open.openpaas.payservice.web.api.order.UserOrderQueryController;
import cn.com.open.openpaas.payservice.web.api.order.UserPayDitchController;
import cn.com.open.openpaas.payservice.web.api.order.ZxptController;
import cn.com.open.payservice.signature.Signature;

public class PayServiceControllerTest extends BaseTest {
	
	 @Autowired
	 UnifyPayController unifyPayController;
    @Autowired
    UserOrderQueryController userOrderQueryController;
    @Autowired
    OrderManualSendController orderManualSendController;
    @Autowired
    cn.com.open.openpaas.payservice.web.api.order.OrderRefundController orderRefundController;
    @Autowired
    UserPayDitchController userPayDitchController;
    @Autowired
    UnifyCostsController unifyCostsController;
    @Autowired
    QueryOrderInfoController queryOrderInfoController;
    @Autowired
    QuerySerialRecordController querySerialRecordController;
    @Autowired
    TclPayController tclPayController;
    @Autowired
    PayOrderManagerController payOrderManagerController;
    @Autowired
    TztBindCardController tztBindCardController;
    @Autowired
    TztUnbindCardController tztUnbindCardController;
    @Autowired
    TztChangeCardController tztChangeCardController;
    @Autowired
    TztBindPayController tztBindPayController;
    @Autowired
    ZxptController zxptController;
    @Autowired
    TestOrderManualSendController testOrderManualSendController;
    @Autowired
    TestOrderCallbackController testOrderCallbackController;
    @Autowired
    ApiCommonController apiCommonController;
    
 // 随机字符串范围
 	private final static String RAND_RANGE_NUM = "1234567890";
	private final static char[] CHARS_NUM = RAND_RANGE_NUM.toCharArray();

    public static String getRandomNum(int CODE_LENGTH) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++)
			sb.append(CHARS_NUM[random.nextInt(CHARS_NUM.length)]);
		return sb.toString();
	}
    
    
    public void unifyPay(String paymentChannel,String paymentType ) throws Exception{//订单支付接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String merchantId="10002";
	        String goodsName="goodsName";
	        String totalFee="1000";
	        String businessType="1";
	        String phone="15727398579";
	    //    String outTradeNo="00test2016050416425600000";
	        String outTradeNo=getRandomNum(8);
	        
	        String signature="";
	        sParaTemp.put("outTradeNo",outTradeNo);
	        sParaTemp.put("userId","36133476-3827-4188-AE4A-0B9DBFC6AC64");
	        sParaTemp.put("goodsName",goodsName);
	        sParaTemp.put("totalFee",totalFee);
	        sParaTemp.put("businessType",businessType);
	        sParaTemp.put("phone",phone);
	        sParaTemp.put("paymentChannel",paymentChannel);
	        sParaTemp.put("paymentType",paymentType);
	        sParaTemp.put("merchantId",merchantId);
	        
	        
	   		String params=createSign(sParaTemp);
			//signature=HMacSha1.getNewResult(signature);
	   		signature=HMacSha1.HmacSHA1Encrypt(params, "credentials-secret");
	   		request.addParameter("signature",signature);
			request.addParameter("outTradeNo",outTradeNo);
	   		request.addParameter("userId","36133476-3827-4188-AE4A-0B9DBFC6AC64");
	   		request.addParameter("goodsName",goodsName);
	   		request.addParameter("totalFee",totalFee);
	   		request.addParameter("businessType",businessType);
	   		request.addParameter("phone",phone);
	   		request.addParameter("paymentChannel",paymentChannel);
	   		request.addParameter("paymentType",paymentType);
	   		request.addParameter("merchantId",merchantId);
	   		unifyPayQuery(request,"1");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void testorderQuery() throws Exception{//订单查询接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        sParaTemp.put("outTradeNo","10");
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, "945fa18c666a4e0097809f6727bc6997");
			//signature=HMacSha1.getNewResult(signature);
	   		request.addParameter("outTradeNo","10");
	   		request.addParameter("signature",signature);
	   		unifyPayQuery(request,"3");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void orderManualSend() throws Exception{//订单补发
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        sParaTemp.put("orderId","10");
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, "945fa18c666a4e0097809f6727bc6997");
			//signature=HMacSha1.getNewResult(signature);
	   		request.addParameter("orderId","10");
	   		request.addParameter("signature",signature);
	   		unifyPayQuery(request,"4");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void OrderRefund() throws Exception{//退款通知
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("refundMoney","100");
	        sParaTemp.put("outTradeNo","10");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
	   		request.addParameter("refundMoney","100");
	   		request.addParameter("outTradeNo","10");
	   		
	   		unifyPayQuery(request,"5");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void UserPayDitch() throws Exception{//4.6.支付渠道列表
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
	   		
	   		unifyPayQuery(request,"6");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void UunifyCosts() throws Exception{//4.7.统一扣费接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("amount","10");
	        sParaTemp.put("source_id","10");
	        sParaTemp.put("user_name","10");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("serial_no","10");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("serial_no",type);
			request.addParameter("amount",type);
			request.addParameter("source_id",type);
			request.addParameter("merchantId","10001");
		//	request.addParameter("app_id","aa98545f11cb49418f18a2ea9ed9873c");
			
	   		unifyPayQuery(request,"7");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    

    public void queryOrderInfo() throws Exception{//4.8.	查询订单信息列表
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("start_time","2017-05-21 11:23:29");
	        sParaTemp.put("end_time","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("start_time","2017-05-21 11:23:29");
			request.addParameter("end_time","2017-08-21 11:23:29");
			request.addParameter("merchantId","10001");
	   		unifyPayQuery(request,"8");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void QuerySerialRecord() throws Exception{//4.9.	查询流水记录列表
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("start_time","2017-05-21 11:23:29");
	        sParaTemp.put("end_time","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("pay_type","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("start_time","2017-05-21 11:23:29");
			request.addParameter("end_time","2017-08-21 11:23:29");
			request.addParameter("merchantId","10001");
			request.addParameter("pay_type","1");
			
	   		unifyPayQuery(request,"9");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void download() throws Exception{//4.10.	对账单下载接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("startTime","2017-05-21 11:23:29");
	        sParaTemp.put("endTime","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("marking","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("startTime","2017-05-21 11:23:29");
			request.addParameter("endTime","2017-08-21 11:23:29");
			request.addParameter("merchantId","10001");
			request.addParameter("marking","1");
			
	   		unifyPayQuery(request,"10");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void TclPay(String paymentChannel,String paymentType) throws Exception{//4.11.	易学贷请求支付
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("outTradeNo",type);
	        sParaTemp.put("userId",type);
	        sParaTemp.put("goodsName","10001");
	        sParaTemp.put("totalFee","10");
	        sParaTemp.put("clientIp","10001");
	        sParaTemp.put("businessType","1");
	        sParaTemp.put("merchantId","100011");
	        sParaTemp.put("paymentChannel",paymentChannel);
	        sParaTemp.put("paymentType",paymentType);
	        
	   		String params=createSign(sParaTemp);
	   		System.out.println("TclPay==="+params);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("outTradeNo",type);
			request.addParameter("userId",type);
			request.addParameter("goodsName","10001");
			request.addParameter("totalFee","10");
			request.addParameter("clientIp","10001");
			request.addParameter("businessType","1");
			request.addParameter("merchantId","100011");
			request.addParameter("paymentChannel",paymentChannel);
			request.addParameter("paymentType",paymentType);
			
	   		unifyPayQuery(request,"11");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void payOrderManager() throws Exception{//4.12.	订单详情存储接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("startTime","2017-05-21 11:23:29");
	        sParaTemp.put("endTime","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("marking","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("startTime","2017-05-21 11:23:29");
			request.addParameter("endTime","2017-08-21 11:23:29");
			request.addParameter("merchantId","10001");
			request.addParameter("marking","1");
			
	   		unifyPayQuery(request,"12");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void TztBindCard() throws Exception{//4.13.1.	有短验绑卡请求接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("identityId","223201198710241028");
	        sParaTemp.put("identityType","1");
	        sParaTemp.put("cardNo","10001");
	        sParaTemp.put("userName","zhangdan");
	        sParaTemp.put("phone","131203566987");
	        sParaTemp.put("terminalId","655052");
	        sParaTemp.put("lastLoginTerminalId","10001");
	        sParaTemp.put("isSetPaypwd","1");
	        sParaTemp.put("registIp","10.100.123.17");
	        sParaTemp.put("registTime","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","13655887852");
	        sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
	        sParaTemp.put("userId","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("identityId","223201198710241028");
			request.addParameter("identityType","1");
			request.addParameter("cardNo","10001");
			request.addParameter("userName","zhangdan");
			request.addParameter("phone","131203566987");
			request.addParameter("terminalId","655052");
			request.addParameter("lastLoginTerminalId","10001");
			request.addParameter("isSetPaypwd","1");
			request.addParameter("registIp","10.100.123.17");
			request.addParameter("registTime","2017-08-21 11:23:29");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","13655887852");
			request.addParameter("avaliabletime","2017-08-21 11:23:29");
			request.addParameter("userId","1");
			
	   		unifyPayQuery(request,"13");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    

    public void confirm() throws Exception{//4.13.2.	有短验绑卡短验确认请求接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("validateCode","203185");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	        sParaTemp.put("requestNo","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("validateCode","203185");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
			request.addParameter("requestNo","1");
			
	   		unifyPayQuery(request,"14");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void resendsms(String types) throws Exception{//4.13.3.	有短绑卡短验重发请求接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	        sParaTemp.put("requestNo","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
			request.addParameter("requestNo","1");
			
	   		unifyPayQuery(request,types);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    
    public void tztUnbindCard() throws Exception{//4.13.4.	解绑银行卡接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("identityId","223201198710241028");
	        sParaTemp.put("identityType","1");
	        sParaTemp.put("cardNo","10001");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","13655887852");
	        sParaTemp.put("userId","1"); 
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("identityId","223201198710241028");
			request.addParameter("identityType","1");
			request.addParameter("cardNo","10001");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","13655887852");
			request.addParameter("userId","1"); 
			
	   		unifyPayQuery(request,"16");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void changeCard() throws Exception{//4.13.5.	换绑银行卡请求接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("identityId","223201198710241028");
	        sParaTemp.put("identityType","1");
	        sParaTemp.put("cardNo","10001");
	        sParaTemp.put("userName","zhangdan");
	        sParaTemp.put("phone","131203566987");
	        sParaTemp.put("terminalId","655052");
	        sParaTemp.put("lastLoginTerminalId","10001");
	        sParaTemp.put("isSetPaypwd","1");
	        sParaTemp.put("registIp","10.100.123.17");
	        sParaTemp.put("registTime","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","13655887852");
	        sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
	        sParaTemp.put("userId","1");
	        sParaTemp.put("oricardNo","10001");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("identityId","223201198710241028");
			request.addParameter("identityType","1");
			request.addParameter("cardNo","10001");
			request.addParameter("userName","zhangdan");
			request.addParameter("phone","131203566987");
			request.addParameter("terminalId","655052");
			request.addParameter("lastLoginTerminalId","10001");
			request.addParameter("isSetPaypwd","1");
			request.addParameter("registIp","10.100.123.17");
			request.addParameter("registTime","2017-08-21 11:23:29");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","13655887852");
			request.addParameter("avaliabletime","2017-08-21 11:23:29");
			request.addParameter("userId","1");
			request.addParameter("oricardNo","10001");
			
	   		unifyPayQuery(request,"17");
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    

    public void direct() throws Exception{//4.13.9.	无短信充值接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	 //       sParaTemp.put("identityId","223201198710241028");
	//        sParaTemp.put("identityType","1");
	        sParaTemp.put("amount","10001");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","13655887852");
	        sParaTemp.put("userId","1"); 
	        sParaTemp.put("productName","zhangsan");
	        sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
	        sParaTemp.put("terminalNo","13655887852");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
	//		request.addParameter("identityId","223201198710241028");
	//		request.addParameter("identityType","1");
			request.addParameter("amount","10001");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","13655887852");
			request.addParameter("userId","1"); 
			request.addParameter("productName","zhangsan");
			request.addParameter("avaliabletime","2017-08-21 11:23:29"); 
			request.addParameter("terminalNo","13655887852"); 
			
	   		unifyPayQuery(request,"20");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    } 


    public void thirdScore() throws Exception{//4.1.1.	第三方征信评分接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("outTradeNo","10001");
	        sParaTemp.put("certNo","10001");
	        sParaTemp.put("userName","zhangsan");
	        sParaTemp.put("mobile","13655887852");
	        sParaTemp.put("reasonNo","10001");
	        sParaTemp.put("cardNo","10001"); 
	        sParaTemp.put("merchantId","10001");
	         
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("outTradeNo","10001");
			request.addParameter("certNo","10001");
			request.addParameter("userName","zhangsan");
			request.addParameter("mobile","13655887852");
			request.addParameter("reasonNo","10001"); 
			request.addParameter("cardNo","10001");
			request.addParameter("merchantId","10001");
			
	   		unifyPayQuery(request,"21");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    } 
     
     
    public void testOrderManualSend() throws Exception{
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("orderId","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	         
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			request.addParameter("orderId","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	   		unifyPayQuery(request,"22");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    

    public void testOrderCallback() throws Exception{
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	         
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
	   		unifyPayQuery(request,"23");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void ApiCommon() throws Exception{
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	         
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
	   		unifyPayQuery(request,"24");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
      
    public void unifyPayQuery(MockHttpServletRequest request,String type) throws Exception{
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	 Model model=new Model() {
				@Override
				public Model mergeAttributes(Map<String, ?> attributes) {
					return null;
				}
				@Override
				public boolean containsAttribute(String attributeName) {
					return false;
				}
				@Override
				public Map<String, Object> asMap() {
					return null;
				}
				@Override
				public Model addAttribute(String attributeName, Object attributeValue) {
					return null;
				}
				@Override
				public Model addAttribute(Object attributeValue) {
					return null;
				}
				@Override
				public Model addAllAttributes(Map<String, ?> attributes) {
					return null;
				}
				@Override
				public Model addAllAttributes(Collection<?> attributeValues) {
					return null;
				}
			};
			if("1".equals(type)){
				unifyPayController.unifyPay(request, response,model);
			//	unifyPayController.skipPayIndex(request, model);qswsxzs
				unifyPayController.errorPayChannel(request, model,"1","2","3","4");
			}else if("3".equals(type)){
				userOrderQueryController.unifyPay(request, response, model);
				userOrderQueryController.getOrderQuery(request, response, model);
				userOrderQueryController.fileDownlond(request, response, model);
			}else if("4".equals(type)){
				orderManualSendController.orderManualSend(request, response);
			}else if("5".equals(type)){
				orderRefundController.orderRefund(request, response);
			}else if("6".equals(type)){
				userPayDitchController.channelList(request, response, model);
			}else if("7".equals(type)){
				unifyCostsController.unifyPay(request, response, model);
			}else if("8".equals(type)){
				queryOrderInfoController.query(request, response, model);
				queryOrderInfoController.download(request, response, model);
			}else if("9".equals(type)){
				querySerialRecordController.query(request, response, model);
			}else if("10".equals(type)){
				queryOrderInfoController.download(request, response, model);
			}else if("11".equals(type)){
				tclPayController.unifyPay(request, response, model);
				tclPayController.errorPayChannel(request, model,"1","2","3","4");
			}else if("12".equals(type)){
				payOrderManagerController.orderDetailSave(request, response, model);
			}else if("13".equals(type)){
				tztBindCardController.request(request, response, model);
			}else if("14".equals(type)){
				tztBindCardController.confirm(request, response, model);
			}else if("15".equals(type)){
				tztBindCardController.resendsms(request, response, model);
				tztBindCardController.bindError(request, response, model,"1","2","3","4");
			}else if("16".equals(type)){
				tztUnbindCardController.request(request, response, model);
				tztUnbindCardController.wxErrorPayChannel(request, response, model,"1","2","3","4");
			}else if("17".equals(type)){
				tztChangeCardController.request(request, response, model);
			}else if("18".equals(type)){
				tztChangeCardController.resendsms(request, response, model);
				tztChangeCardController.confirm(request, response, model);
				tztChangeCardController.bindBack(request, response, model);
				tztChangeCardController.wxErrorPayChannel(request, response, model,"1","2","3","4");
				
			}else if("20".equals(type)){
				tztBindPayController.direct(request, response, model);
				tztBindPayController.payError(request, response, model,"1","2","3","4");
			}else if("21".equals(type)){
				zxptController.thirdScoreRequest(request, response, model);
				zxptController.payError(request, response, model,"1","2","3","4");
			}else if("22".equals(type)){
				testOrderManualSendController.orderManualSend(request, response);
			}else if("23".equals(type)){
				testOrderCallbackController.testDirctPay(request, response);
			}else if("24".equals(type)){
				apiCommonController.status(request, response);
			}
			
			
			
			
    }
    
    
    @Test
    public void testPay(){
    	try {
    		String s="10001-ALIPAY;10002-WEIXIN;10003-UPOP;10005-PAYMAX;10006-EHK_WECHAT_WAP;10007-PAYMAX;10008-YEEPAY_GW;10009-ALIFAF;10011-WECHAT_WAP;10012-EHK_WEIXIN_PAY;10014-UPOP;10013-PAYMAX_WECHAT_CSB;"
    				+ "10015-PAYMAX_H5;10016 WEIXIN;10017-YEEPAY_WEIXIN;10018-YEEPAY_ALI;10019-YEEPAY_ALL;";
    		String  ss[]=s.split(";");
    		for (int i = 0; i < ss.length; i++) {
				String sss[]=ss[i].split("-");
			//	for (int j = 0; j < sss.length; j++) {
					unifyPay(sss[0],sss[1].trim());
					TclPay(sss[0],sss[1].trim());
					Thread.sleep(10000);
					System.out.println(sss[0]+"==="+sss[1].trim());
			//	}
			}
			testorderQuery();
			orderManualSend();
			OrderRefund();
			UserPayDitch();
    		UunifyCosts();
    		queryOrderInfo();
    		QuerySerialRecord();
    		download();
    		payOrderManager();
    		TztBindCard();
    		confirm();
    		resendsms("15");
    		tztUnbindCard();
    		changeCard();//待优化
    		resendsms("18");//4.13.7.	换卡短验重发请求接口
    		direct();
    		thirdScore();
    		testOrderManualSend();
    		testOrderCallback();
    		ApiCommon();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
     
}
