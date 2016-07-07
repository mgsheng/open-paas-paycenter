package cn.com.open.openpaas.payservice.app.channel.tclpay.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytDateUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;

/**
 * 名称：扫码订单支付数据构造类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class ScanCodeOrderData {

	public static Map<String, String> buildOrderDataMap(String name) {

		String orderTime = HytDateUtils.generateOrderTime();

		Map<String, String> orderDataMap = new HashMap<String, String>();
		orderDataMap.put(HytParamKeys.CHARSET, "00");
		orderDataMap.put(HytParamKeys.VERSION, "1.0");
		orderDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		String requestId = orderTime;
		orderDataMap.put(HytParamKeys.REQUEST_ID, requestId);
		orderDataMap.put(HytParamKeys.MERCHANT_CODE, "800075500030008");
		orderDataMap.put(HytParamKeys.APP_NO, "");
		orderDataMap.put(HytParamKeys.MERCHANT_NAME, "");
		String outTradeNo = orderTime;
		orderDataMap.put(HytParamKeys.OUT_TRADE_NO, outTradeNo);
		orderDataMap.put(HytParamKeys.SERVICE, "GWDirectPay");
		orderDataMap.put(HytParamKeys.CHANNEL_CODE, "ALIPAY");
		orderDataMap.put(HytParamKeys.CHANNEL_PRODUCT_CODE, "");
		orderDataMap.put(HytParamKeys.PRODUCT_NAME, name + "aa");
		orderDataMap.put(HytParamKeys.SHOW_URL,
				"http://www.test.com/test/showproduct.jsp");
		orderDataMap.put(HytParamKeys.PRODUCT_ID, orderTime);
		orderDataMap.put(HytParamKeys.PRODUCT_DESC, "aa");
		orderDataMap.put(HytParamKeys.ATTACH, "1010");
		orderDataMap.put(HytParamKeys.RETURN_URL,
				"http://www.test.com/test/notify.jsp");
		orderDataMap.put(HytParamKeys.NOTIFY_URL,
				"http://www.test.com/test/notify.jsp");
		orderDataMap.put(HytParamKeys.SPBILL_CREATE_IP, "127.0.0.1");
		orderDataMap.put(HytParamKeys.BUYER_ID, "156XXXXXX32");

		orderDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
		orderDataMap.put(HytParamKeys.TOTAL_AMOUNT,
				"0".equals(orderTime.substring(orderTime.length() - 1)) ? "1"
						: orderTime.substring(orderTime.length() - 1));
		orderDataMap.put(HytParamKeys.CURRENCY, "CNY");
		orderDataMap.put(HytParamKeys.TIME_EXPIRE, "10080");

		orderDataMap.put(HytParamKeys.OUT_MSG_TYPE, "");
		return orderDataMap;
	}
	public static void buildOrderDataMap1(String name,RedirectAttributes attr) {

		String orderTime = HytDateUtils.generateOrderTime();

		Map<String, String> orderDataMap = new HashMap<String, String>();
		orderDataMap.put(HytParamKeys.CHARSET, "02");
		orderDataMap.put(HytParamKeys.VERSION, "1.0");
		orderDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		String requestId = orderTime;
		orderDataMap.put(HytParamKeys.REQUEST_ID, requestId);
		orderDataMap.put(HytParamKeys.MERCHANT_CODE, "800075500030008");
		orderDataMap.put(HytParamKeys.APP_NO, "");
		orderDataMap.put(HytParamKeys.MERCHANT_NAME, "");
		String outTradeNo = orderTime;
		orderDataMap.put(HytParamKeys.OUT_TRADE_NO, outTradeNo);
		orderDataMap.put(HytParamKeys.SERVICE, "GWDirectPay");
		orderDataMap.put(HytParamKeys.CHANNEL_CODE, "ALIPAY");
		orderDataMap.put(HytParamKeys.CHANNEL_PRODUCT_CODE, "");
		orderDataMap.put(HytParamKeys.PRODUCT_NAME, name + "aa");
		orderDataMap.put(HytParamKeys.SHOW_URL,
				"http://www.test.com/test/showproduct.jsp");
		orderDataMap.put(HytParamKeys.PRODUCT_ID, orderTime);
		orderDataMap.put(HytParamKeys.PRODUCT_DESC, "aa");
		orderDataMap.put(HytParamKeys.ATTACH, "1010");
		orderDataMap.put(HytParamKeys.RETURN_URL,
				"http://www.test.com/test/notify.jsp");
		orderDataMap.put(HytParamKeys.NOTIFY_URL,
				"http://www.test.com/test/notify.jsp");
		orderDataMap.put(HytParamKeys.SPBILL_CREATE_IP, "127.0.0.1");
		orderDataMap.put(HytParamKeys.BUYER_ID, "156XXXXXX32");

		orderDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
		orderDataMap.put(HytParamKeys.TOTAL_AMOUNT,
				"0".equals(orderTime.substring(orderTime.length() - 1)) ? "1"
						: orderTime.substring(orderTime.length() - 1));
		orderDataMap.put(HytParamKeys.CURRENCY, "CNY");
		orderDataMap.put(HytParamKeys.TIME_EXPIRE, "10080");

		orderDataMap.put(HytParamKeys.OUT_MSG_TYPE, "");
		
		
		 attr.addFlashAttribute(HytParamKeys.CHARSET, "00");  
		  attr.addAttribute(HytParamKeys.VERSION, "1.0");
		  attr.addAttribute(HytParamKeys.SIGN_TYPE, "RSA"); 
		  attr.addAttribute(HytParamKeys.REQUEST_ID, requestId);  
		  attr.addAttribute(HytParamKeys.MERCHANT_CODE, "800075500030008");  
		  attr.addAttribute(HytParamKeys.APP_NO, "");
		  attr.addAttribute(HytParamKeys.MERCHANT_NAME, "");
		  attr.addAttribute(HytParamKeys.OUT_TRADE_NO, orderTime); 
		  attr.addAttribute(HytParamKeys.SERVICE, "GWDirectPay"); 
		  attr.addAttribute(HytParamKeys.CHANNEL_CODE, "ALIPAY"); 
		  attr.addAttribute(HytParamKeys.CHANNEL_PRODUCT_CODE, "");
		  attr.addAttribute(HytParamKeys.PRODUCT_NAME, Thread.currentThread().getName() + "aa");
		  attr.addAttribute(HytParamKeys.SHOW_URL,
					"http://www.test.com/test/showproduct.jsp");  
		  attr.addAttribute(HytParamKeys.NOTIFY_URL,
					"http://www.test.com/test/notify.jsp");  
		  attr.addAttribute(HytParamKeys.PRODUCT_ID, orderTime);
		  attr.addAttribute(HytParamKeys.ATTACH, "1010");
		  attr.addAttribute(HytParamKeys.PRODUCT_DESC, "aa");
		  attr.addAttribute(HytParamKeys.SPBILL_CREATE_IP, "127.0.0.1");  
		  attr.addAttribute(HytParamKeys.BUYER_ID, "156XXXXXX32"); 
		  attr.addAttribute(HytParamKeys.ORDER_TIME, orderTime); 
		  attr.addAttribute(HytParamKeys.TOTAL_AMOUNT,
				"0".equals(orderTime.substring(orderTime.length() - 1)) ? "1"
						: orderTime.substring(orderTime.length() - 1)); 
		  attr.addAttribute(HytParamKeys.CURRENCY, "CNY"); 
		  attr.addAttribute(HytParamKeys.TIME_EXPIRE, "10080");
		  attr.addAttribute(HytParamKeys.OUT_MSG_TYPE, "");
		  RSASign util = HytUtils.getRSASignObject();
		  String reqData = HytPacketUtils.map2Str(orderDataMap);
		  String merchant_sign = "";
			try {
				merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  String merchant_cert = util.getCertInfo();
		  attr.addAttribute(HytParamKeys.MERCHANT_SIGN, merchant_sign);
		  attr.addAttribute(HytParamKeys.MERCHANT_CERT, merchant_cert);
		
		
	}
	public static  NameValuePair[]  getPair(String name) {

		String orderTime = HytDateUtils.generateOrderTime();
		   NameValuePair CHARSET=new NameValuePair(HytParamKeys.CHARSET, "00");
		   NameValuePair VERSION=new NameValuePair(HytParamKeys.VERSION, "1.0");
		   NameValuePair SIGN_TYPE=new NameValuePair(HytParamKeys.SIGN_TYPE, "RSA");
			String requestId = orderTime;
			  NameValuePair REQUEST_ID=new NameValuePair(HytParamKeys.REQUEST_ID, requestId);
			  NameValuePair MERCHANT_CODE=new NameValuePair(HytParamKeys.MERCHANT_CODE, "800075500030008");
			  NameValuePair APP_NO=new NameValuePair(HytParamKeys.APP_NO, "");
			  NameValuePair MERCHANT_NAME=new NameValuePair(HytParamKeys.MERCHANT_NAME, "");
			String outTradeNo = orderTime;
			  NameValuePair OUT_TRADE_NO=new NameValuePair(HytParamKeys.OUT_TRADE_NO, outTradeNo);
			  NameValuePair SERVICE=new NameValuePair(HytParamKeys.SERVICE, "GWDirectPay");
			  NameValuePair CHANNEL_CODE=new NameValuePair(HytParamKeys.CHANNEL_CODE, "ALIPAY");
			  NameValuePair CHANNEL_PRODUCT_CODE=new NameValuePair(HytParamKeys.CHANNEL_PRODUCT_CODE, "");
			  NameValuePair PRODUCT_NAME=new NameValuePair(HytParamKeys.PRODUCT_NAME, name + "接口验证商品");
			  NameValuePair SHOW_URL=new NameValuePair(HytParamKeys.SHOW_URL,
					"http://www.test.com/test/showproduct.jsp");
			  NameValuePair PRODUCT_ID=new NameValuePair(HytParamKeys.PRODUCT_ID, orderTime);
			  NameValuePair PRODUCT_DESC=new NameValuePair(HytParamKeys.PRODUCT_DESC, "接口验证商品");
			  NameValuePair ATTACH=new NameValuePair(HytParamKeys.ATTACH, "1010");
			  NameValuePair RETURN_URL=new NameValuePair(HytParamKeys.RETURN_URL,
					"http://www.test.com/test/notify.jsp");
			  NameValuePair NOTIFY_URL=new NameValuePair(HytParamKeys.NOTIFY_URL,
					"http://www.test.com/test/notify.jsp");
			  NameValuePair SPBILL_CREATE_IP=new NameValuePair(HytParamKeys.SPBILL_CREATE_IP, "127.0.0.1");
			  NameValuePair BUYER_ID=new NameValuePair(HytParamKeys.BUYER_ID, "156XXXXXX32");

			  NameValuePair ORDER_TIME=new NameValuePair(HytParamKeys.ORDER_TIME, orderTime);
			  NameValuePair TOTAL_AMOUNT=new NameValuePair(HytParamKeys.TOTAL_AMOUNT,
					"0".equals(orderTime.substring(orderTime.length() - 1)) ? "1"
							: orderTime.substring(orderTime.length() - 1));
			  NameValuePair CURRENCY=new NameValuePair(HytParamKeys.CURRENCY, "CNY");
			  NameValuePair TIME_EXPIRE=new NameValuePair(HytParamKeys.TIME_EXPIRE, "10080");

			  NameValuePair OUT_MSG_TYPE=new NameValuePair(HytParamKeys.OUT_MSG_TYPE, "FORM");
	       
	        NameValuePair[] data = {CHARSET,VERSION,SIGN_TYPE,REQUEST_ID,MERCHANT_CODE,APP_NO,SERVICE,OUT_TRADE_NO,CHANNEL_CODE,MERCHANT_NAME,CHANNEL_PRODUCT_CODE,PRODUCT_NAME,SHOW_URL,PRODUCT_ID,PRODUCT_DESC,ATTACH,RETURN_URL,NOTIFY_URL,SPBILL_CREATE_IP,BUYER_ID,ORDER_TIME,TOTAL_AMOUNT,CURRENCY,TIME_EXPIRE,OUT_MSG_TYPE};


		return data;
	}
	public static Map<String, String> buildOrderDataMap(MerchantOrderInfo merchantOrderInfo,String version,String charset,String channelCode,String Service,String returnUrl ) throws UnsupportedEncodingException {

		String orderTime = HytDateUtils.generateOrderTime();

		Map<String, String> orderDataMap = new HashMap<String, String>();
		orderDataMap.put(HytParamKeys.CHARSET, charset);
		orderDataMap.put(HytParamKeys.VERSION, version);
		orderDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		String requestId = orderTime;
		orderDataMap.put(HytParamKeys.REQUEST_ID, requestId);
		orderDataMap.put(HytParamKeys.MERCHANT_CODE, "800075500030008");
		orderDataMap.put(HytParamKeys.APP_NO, "");
		orderDataMap.put(HytParamKeys.MERCHANT_NAME, "");
		orderDataMap.put(HytParamKeys.OUT_TRADE_NO, merchantOrderInfo.getMerchantOrderId());
		orderDataMap.put(HytParamKeys.SERVICE, Service);
		orderDataMap.put(HytParamKeys.CHANNEL_CODE, channelCode);
		orderDataMap.put(HytParamKeys.CHANNEL_PRODUCT_CODE, "");
		String subject =merchantOrderInfo.getMerchantProductName();
		orderDataMap.put(HytParamKeys.PRODUCT_NAME,subject);
		orderDataMap.put(HytParamKeys.SHOW_URL,
				"http://www.test.com/test/showproduct.jsp");
		orderDataMap.put(HytParamKeys.PRODUCT_ID, merchantOrderInfo.getMerchantProductId());
		orderDataMap.put(HytParamKeys.PRODUCT_DESC,subject);
		orderDataMap.put(HytParamKeys.ATTACH, "1010");
		orderDataMap.put(HytParamKeys.RETURN_URL,
				returnUrl);
		orderDataMap.put(HytParamKeys.NOTIFY_URL,
				returnUrl);
		orderDataMap.put(HytParamKeys.SPBILL_CREATE_IP, "127.0.0.1");
		orderDataMap.put(HytParamKeys.BUYER_ID, "156XXXXXX32");

		orderDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
		orderDataMap.put(HytParamKeys.TOTAL_AMOUNT,String.valueOf(merchantOrderInfo.getOrderAmount()*100));
		orderDataMap.put(HytParamKeys.CURRENCY, "CNY");
		orderDataMap.put(HytParamKeys.TIME_EXPIRE, "10080");

		orderDataMap.put(HytParamKeys.OUT_MSG_TYPE, "XML");
		return orderDataMap;
	}
}
