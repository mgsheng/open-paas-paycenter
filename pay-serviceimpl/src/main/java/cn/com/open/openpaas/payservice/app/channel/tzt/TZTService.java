package cn.com.open.openpaas.payservice.app.channel.tzt;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 新投资通接口范例--API版
 * @author	：   
 * @since	： 
 */

public class TZTService {
	
	/**
	 * 取得商户编号
	 */
	public static String getMerchantAccount() {
		return Configuration.getInstance().getValue("merchantAccount");
	}
	
	/**
	 * 取得商户私钥
	 */
	public static String getMerchantPrivateKey() {
		return Configuration.getInstance().getValue("merchantPrivateKey");
	}

	/**
	 * 取得商户AESKey
	 */
	public static String getMerchantAESKey() {
		return (RandomUtil.getRandom(16));
	}

	/**
	 * 取得易宝公玥
	 */
	public static String getYeepayPublicKey() {
		return Configuration.getInstance().getValue("yeepayPublicKey");
	}

	/**
	 * 格式化字符串
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}

	/**
	 * String2Integer
	 */
	public static int String2Int(String text) throws NumberFormatException {
		return text == null ? 0 : Integer.valueOf(text);
	}

	/**
	 * 有短验绑卡请求接口
	 */
	public static String getBindBankcardURL() {
		return Configuration.getInstance().getValue("bindCardRequestURL");
	}

	/**
	 * 有短验绑卡确认接口
	 */
	public static String getConfirmBindBankcardURL() {
		return Configuration.getInstance().getValue("bindCardConfirmURL");
	}

	/**
	 * 有短验绑卡短验重发接口
	 */
	public static String getBindCardResendsmsURL() {
		return Configuration.getInstance().getValue("bindCardResendsmsURL");
	}
	
	/**
	 * 无短验绑卡请求接口
	 */
	public static String getUnSendsmsBindCardRequestURL() {
		return Configuration.getInstance().getValue("unSendsmsBindCardRequestURL");
	}
	
	/**
	 * 补充鉴权请求接口
	 */
	public static String getRemitRreuestURL() {
		return Configuration.getInstance().getValue("remitRequestURL");
	}
	
	/**
	 * 补充鉴权确认接口
	 */
	public static String getRemitConfirmURL() {
		return Configuration.getInstance().getValue("remitConfirmURL");
	}
	
	/**
	 * 有短验充值请求接口
	 */
	public static String getBindPayRequestURL() {
		return Configuration.getInstance().getValue("bindPayRequestURL");
	}

	/**
	 * 有短验充值确认接口
	 */
	public static String getBindPayConfirmURL() {
		return Configuration.getInstance().getValue("bindPayConfirmURL");
	}

	/**
	 * 有短验充值短验重发接口
	 */
	public static String getBindPayResendsmsURL() {
		return Configuration.getInstance().getValue("bindPayResendsmsURL");
	}
	
	/**
	 * 无短验充值接口
	 */
	public static String getunSendBindPayRequestURL() {
		return Configuration.getInstance().getValue("unSendBindPayRequestURL");
	}
	
	/**
	 * 首次充值请求接口
	 */
	public static String getFirstPayRequestURL() {
		return Configuration.getInstance().getValue("firstPayRequestURL");
	}
	
	/**
	 * 首次充值确认接口
	 */
	public static String getFisrstPayConfirmURL() {
		return Configuration.getInstance().getValue("firstPayConfirmURL");
	}
	
	/**
	 * 首次充值短验重发接口
	 */
	public static String getFirstPayResendsmsURL() {
		return Configuration.getInstance().getValue("firstPayResendsmsmURL");
	}
	
	/**
	 * 换卡请求接口
	 */
	public static String getChangeCardRequestURL() {
		return Configuration.getInstance().getValue("changeCardRequestURL");
	}
	
	/**
	 * 换卡请求确认接口
	 */
	public static String getChangeCardConfirmURL() {
		return Configuration.getInstance().getValue("changeCardConfirmURL");
	}
	
	/**
	 * 换卡请求短验重发接口
	 */
	public static String getChangeCardResendsmsURL() {
		return Configuration.getInstance().getValue("changeCardResendsmsmURL");
	}
	
	/**
	 * 绑卡充值记录查询请求地址
	 */
	public static String getBindPayRecordURL() {
		return Configuration.getInstance().getValue("bindPayRecordURL");
	}
	/**
	 * 换卡记录查询请求地址
	 */
	public static String getChangeCardURL() {
		return Configuration.getInstance().getValue("changeCardURL");
	}
	/**
	 * 可提现余额查询请求地址
	 */
	public static String getDrawValidAmountURL() {
		return Configuration.getInstance().getValue("drawValidAmountURL");
	}
	
	/**
	 * 提现查询请求地址
	 */
	public static String getQueryDrawRecordURL() {
		return Configuration.getInstance().getValue("queryDrawRecordURL");
	}

	/**
	 * 绑卡列表查询请求地址
	 */
	public static String getQueryBindCardListURL() {
		return Configuration.getInstance().getValue("queryBindCardListURL");
	}

	/**
	 * 绑卡记录查询请求地址
	 */
	public static String getQueryBindCardRecordURL() {
		return Configuration.getInstance().getValue("queryBindCardRecordURL");
	}
	
	/**
	 * 银行卡信息查询请求地址 
	 */
	public static String getBankCardCheckURL() {
		return Configuration.getInstance().getValue("bankCardCheckURL");
	}
	
	/**
	 * 首次充值记录查询请求地址
	 */
	public static String getFirstPayRecordURL() {
		return Configuration.getInstance().getValue("firstpayrecordURL");
	}
	
	/**
	 * 取现接口请求地址
	 */
	public static String getWithdrawURL() {
		return Configuration.getInstance().getValue("withdrawURL");
	}
	
	/**
	 * 鉴权绑卡对账
	 */
	public static String getaccountCheckAuthURL() {
		return Configuration.getInstance().getValue("accountCheckAuthURL");
	}
	
	/**
	 * 支付对账
	 */
	public static String getaccountCheckPaymentURL() {
		return Configuration.getInstance().getValue("accountCheckPaymentURL");
	}
	
	/**
	 * 提现对账
	 */
	public static String getaccountCheckWithDrawURL() {
		return Configuration.getInstance().getValue("accountCheckWithDrawURL");
	}
	
	/**
	 * 换卡对账
	 */
	public static String getaccountCheckChangeCardURL() {
		return Configuration.getInstance().getValue("accountCheckChangeCardURL");
	}
	
	/**
	 * 退款对账
	 */
	public static String getaccountCheckRefundURL() {
		return Configuration.getInstance().getValue("accountCheckRefundURL");
	}
	
	/**
	 * 退款接口
	 */
	public static String getRefundURL() {
		return Configuration.getInstance().getValue("refundURL");
	}
	
	/**
	 * 退款记录查询接口
	 */
	public static String getRefundRecordURL() {
		return Configuration.getInstance().getValue("refundRecordURL");
	}

	/**
	 * 解绑卡接口
	 */
	public static String getUnbindRequestURL() {
		return Configuration.getInstance().getValue("unbindRequestURL");
	}
	/**
	 * 解析http请求返回
	 */
	public static Map<String, String> parseHttpResponseBody(int statusCode, String responseBody,String merchantPrivateKey,String yeepayPublicKey) throws Exception {

//		String merchantPrivateKey	= getMerchantPrivateKey();
//		String yeepayPublicKey		= getYeepayPublicKey();

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";

		if(statusCode != 200) {
			customError	= "Request failed, response code : " + statusCode;
			result.put("customError", customError);
			return (result);
		}

		Map<String, String> jsonMap	= JSON.parseObject(responseBody, 
											new TypeReference<TreeMap<String, String>>() {});

		if(jsonMap.containsKey("errorcode")) {
			result	= jsonMap;
			return (result);
		}

		String dataFromYeepay		= formatString(jsonMap.get("data"));
		String encryptkeyFromYeepay	= formatString(jsonMap.get("encryptkey"));

		boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay, 
										yeepayPublicKey, merchantPrivateKey);
		if(!signMatch) {
			customError	= "Sign not match error";
			result.put("customError",	customError);
			return (result);
		}

		String yeepayAESKey		= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
		String decryptData		= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);

		result	= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		return(result);
	}

	/**
	 * decryptCallbackData() : 解密支付回调参数data
	 *
	 */

	public static Map<String, String> decryptCallbackData(String data, String encryptkey) {
		
		System.out.println("##### decryptCallbackData() #####");
		String merchantPrivateKey	= getMerchantPrivateKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		
		System.out.println("data : " + data);
		System.out.println("encryptkey : " + encryptkey);
		
		Map<String, String> callbackResult	= new HashMap<String, String>();
		String customError			= "";
		
		try {
			boolean signMatch = EncryUtil.checkDecryptAndSign(data, encryptkey, yeepayPublicKey, merchantPrivateKey);

			if(!signMatch) {
				customError	= "Sign not match error";
				callbackResult.put("customError",	customError);
				return callbackResult;
			}

			String yeepayAESKey	= RSA.decrypt(encryptkey, merchantPrivateKey);
			String decryptData	= AES.decryptFromBase64(data, yeepayAESKey);
			callbackResult		= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			callbackResult.put("customError", customError);
			e.printStackTrace();
		}

		System.out.println("callbackResult : " + callbackResult);

		return (callbackResult);
	}

	/**
	 * bindCardRequest() : 有短验绑卡请求接口
	 */

	public static Map<String, String> bindCardRequest(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String bindBankcardURL) {

		System.out.println("##### bindCardRequest() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
		
//		String merchantno			= getMerchantAccount();
//		String merchantPrivateKey	= getMerchantPrivateKey();
//		String merchantAESKey		= getMerchantAESKey();
//		String yeepayPublicKey		= getYeepayPublicKey();
//		String bindBankcardURL		= getBindBankcardURL();

		String requestno            = formatString(params.get("requestno"));
		String identityid           = formatString(params.get("identityid"));
		String identitytype         = formatString(params.get("identitytype"));
		String cardno               = formatString(params.get("cardno"));
		String idcardno             = formatString(params.get("idcardno"));
		String idcardtype           = formatString(params.get("idcardtype"));
		String username             = formatString(params.get("username"));
		String phone                = formatString(params.get("phone"));
		String advicesmstype        = formatString(params.get("advicesmstype"));
		String customerenhancedtype = formatString(params.get("customerenhancedtype"));
		String avaliabletime        = formatString(params.get("avaliabletime"));
		String callbackurl          = formatString(params.get("callbackurl"));
		String requesttime     		= formatString(params.get("requesttime"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 		merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("cardno", 			cardno);
		dataMap.put("idcardno", 		idcardno);
		dataMap.put("idcardtype", 		idcardtype);
		dataMap.put("username", 		username);
		dataMap.put("phone", 			phone);
		dataMap.put("advicesmstype", 	advicesmstype);
		dataMap.put("customerenhancedtype", 	customerenhancedtype);
		dataMap.put("avaliabletime", 	avaliabletime);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("requesttime", 		requesttime);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bindBankcardURL : " + bindBankcardURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(bindBankcardURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data=" + data);
			System.out.println("encryptkey=" + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	
	/**
	 * bindCardConfirm() : 有短验绑卡请求确认接口
	 */

	public static Map<String, String> bindCardConfirm(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String confirmBindBankcardURL) {

		System.out.println("##### bindCardConfirm() #####");
				
//		String merchantno				= getMerchantAccount();
//		String merchantPrivateKey		= getMerchantPrivateKey();
//		String merchantAESKey			= getMerchantAESKey();
//		String yeepayPublicKey			= getYeepayPublicKey();
//		String confirmBindBankcardURL	= getConfirmBindBankcardURL();

		String requestno            	= formatString(params.get("requestno"));
		String validatecode      		= formatString(params.get("validatecode"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("validatecode", 	validatecode);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("confirmBindBankcardURL : " + confirmBindBankcardURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(confirmBindBankcardURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * bindCardResendsms() : 有短验绑卡请求重发短验接口
	 */

	public static Map<String, String> bindCardResendsms(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String bindCardResendsmsURL) {

		System.out.println("##### bindCardResendsms() #####");
				
//		String merchantno				= getMerchantAccount();
//		String merchantPrivateKey		= getMerchantPrivateKey();
//		String merchantAESKey			= getMerchantAESKey();
//		String yeepayPublicKey			= getYeepayPublicKey();
//		String bindCardResendsmsURL		= getBindCardResendsmsURL();

		String requestno            	= formatString(params.get("requestno"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bindCardResendsmsURL : " + bindCardResendsmsURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(bindCardResendsmsURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}


	/**
	 * remitRreuest() : 补充鉴权请求接口
	 */

	public static Map<String, String> remitRreuest(Map<String, String> params) {

		System.out.println("##### remitRreuest() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
		
		String merchantno			= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String remitRreuestURL		= getRemitRreuestURL();

		String requestno            = formatString(params.get("requestno"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 		merchantno);
		dataMap.put("requestno", 		requestno);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("remitRreuestURL : " + remitRreuestURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(remitRreuestURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data=" + data);
			System.out.println("encryptkey=" + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	
	/**
	 * remitConfirm() : 补充鉴权确认接口
	 */

	public static Map<String, String> remitConfirm(Map<String, String> params) {

		System.out.println("##### remitConfirm() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
		
		String merchantno			= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String remitConfirmURL		= getRemitConfirmURL();

		String requestno            = formatString(params.get("requestno"));
		String amount            	= formatString(params.get("amount"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 		merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("amount", 			amount);
		
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("remitConfirmURL : " + remitConfirmURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(remitConfirmURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data=" + data);
			System.out.println("encryptkey=" + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * bindPayRequest() : 有短验充值请求接口
	 */

	public static Map<String, String> bindPayRequest(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String bindPayRequestURL) {

		System.out.println("##### bindPayRequest() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
//		String merchantno					= getMerchantAccount();
//		String merchantPrivateKey			= getMerchantPrivateKey();
//		String merchantAESKey				= getMerchantAESKey();
//		String yeepayPublicKey				= getYeepayPublicKey();
//		String bindPayRequestURL			= getBindPayRequestURL();

		String requestno 				= formatString(params.get("requestno"));
		String identityid 				= formatString(params.get("identityid"));
		String identitytype 			= formatString(params.get("identitytype"));
		String cardtop 					= formatString(params.get("cardtop"));
		String cardlast 				= formatString(params.get("cardlast"));
		String amount 					= formatString(params.get("amount"));
		String advicesmstype 			= formatString(params.get("advicesmstype"));
		String avaliabletime 			= formatString(params.get("avaliabletime"));
		String productname 				= formatString(params.get("productname"));
		String callbackurl 				= formatString(params.get("callbackurl"));
		String requesttime				= formatString(params.get("requesttime"));
		String terminalid 				= formatString(params.get("terminalid"));
		String terminalNo 				= formatString(params.get("terminalNo"));
		String registtime 				= formatString(params.get("registtime"));
		String registip 				= formatString(params.get("registip"));
		String lastloginip 				= formatString(params.get("lastloginip"));
		String lastlogintime			= formatString(params.get("lastlogintime"));
		String lastloginterminalid 		= formatString(params.get("lastloginterminalid"));
		String issetpaypwd 				= formatString(params.get("issetpaypwd"));
		String free1 					= formatString(params.get("free1"));
		String free2 					= formatString(params.get("free2"));
		String free3 					= formatString(params.get("free3"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 		identitytype);
		dataMap.put("cardtop", 		cardtop);
		dataMap.put("cardlast", 		cardlast);
		dataMap.put("amount", 		amount);
		dataMap.put("advicesmstype", 	advicesmstype);
		dataMap.put("avaliabletime",	 	avaliabletime);
		dataMap.put("productname", 	productname);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("requesttime", 	requesttime);
		dataMap.put("terminalid", 	terminalid);
		dataMap.put("terminalno", 	terminalNo);
		dataMap.put("registtime", 	registtime);
		dataMap.put("registip", 	registip);
		dataMap.put("lastloginip", 	lastloginip);
		dataMap.put("lastlogintime", 	lastlogintime);
		dataMap.put("lastloginterminalid", 	lastloginterminalid);
		dataMap.put("issetpaypwd", 	issetpaypwd);
		dataMap.put("free1", 	free1);
		dataMap.put("free2", 	free2);
		dataMap.put("free3", 	free3);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bindPayRequestURL : " + bindPayRequestURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(bindPayRequestURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("jsonStr : " + jsonStr);
			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result						= parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * bindPayConfirm() : 有短验充值请求确认接口
	 */

	public static Map<String, String> bindPayConfirm(Map<String, String> params) {

		System.out.println("##### bindPayConfirm() #####");
				
		String merchantno				= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String bindPayConfirmURL		= getBindPayConfirmURL();

		String requestno            	= formatString(params.get("requestno"));
		String validatecode      		= formatString(params.get("validatecode"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("validatecode", 	validatecode);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bindPayConfirmURL : " + bindPayConfirmURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(bindPayConfirmURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * bindPayResendsms() : 有短验充值请求重发短验接口
	 */

	public static Map<String, String> bindPayResendsms(Map<String, String> params) {

		System.out.println("##### bindPayResendsms() #####");
				
		String merchantno				= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String bindPayResendsmsURL		= getBindPayResendsmsURL();

		String requestno            	= formatString(params.get("requestno"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bindPayResendsmsURL : " + bindPayResendsmsURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(bindPayResendsmsURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * unSendBindPayRequest() : 无短验充值请求接口
	 */

	public static Map<String, String> unSendBindPayRequest(Map<String, String> params) {

		System.out.println("##### unSendBindPayRequest() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
		String merchantno					= getMerchantAccount();
		String merchantPrivateKey			= getMerchantPrivateKey();
		String merchantAESKey				= getMerchantAESKey();
		String yeepayPublicKey				= getYeepayPublicKey();
		String unSendBindPayRequestURL		= getunSendBindPayRequestURL();

		String requestno 				= formatString(params.get("requestno"));
		String identityid 				= formatString(params.get("identityid"));
		String identitytype 			= formatString(params.get("identitytype"));
		String requesttime				= formatString(params.get("requesttime"));
		String amount 					= formatString(params.get("amount"));
		String productname 				= formatString(params.get("productname"));
		String avaliabletime 			= formatString(params.get("avaliabletime"));
		String cardtop 					= formatString(params.get("cardtop"));
		String cardlast 				= formatString(params.get("cardlast"));
		String callbackurl 				= formatString(params.get("callbackurl"));
		String terminalid 				= formatString(params.get("terminalid"));
		String registtime 				= formatString(params.get("registtime"));
		String registip 				= formatString(params.get("registip"));
		String lastloginip 				= formatString(params.get("lastloginip"));
		String lastlogintime			= formatString(params.get("lastlogintime"));
		String lastloginterminalid 		= formatString(params.get("lastloginterminalid"));
		String issetpaypwd 				= formatString(params.get("issetpaypwd"));
		String free1 					= formatString(params.get("free1"));
		String free2 					= formatString(params.get("free2"));
		String free3 					= formatString(params.get("free3"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 		identitytype);
		dataMap.put("requesttime", 	requesttime);
		dataMap.put("amount", 		amount);
		dataMap.put("productname", 	productname);
		dataMap.put("avaliabletime",	 	avaliabletime);
		dataMap.put("cardtop", 		cardtop);
		dataMap.put("cardlast", 		cardlast);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("terminalid", 	terminalid);
		dataMap.put("registtime", 	registtime);
		dataMap.put("registip", 	registip);
		dataMap.put("lastloginip", 	lastloginip);
		dataMap.put("lastlogintime", 	lastlogintime);
		dataMap.put("lastloginterminalid", 	lastloginterminalid);
		dataMap.put("issetpaypwd", 	issetpaypwd);
		dataMap.put("free1", 	free1);
		dataMap.put("free2", 	free2);
		dataMap.put("free3", 	free3);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("unSendBindPayRequestURL : " + unSendBindPayRequestURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(unSendBindPayRequestURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("jsonStr : " + jsonStr);
			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * firstPayRequest() : 首次充值请求接口
	 */

	public static Map<String, String> firstPayRequest(Map<String, String> params) {

		System.out.println("##### firstPayRequest() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
		String merchantno					= getMerchantAccount();
		String merchantPrivateKey			= getMerchantPrivateKey();
		String merchantAESKey				= getMerchantAESKey();
		String yeepayPublicKey				= getYeepayPublicKey();
		String firstPayRequestURL			= getFirstPayRequestURL();

		String requestno 				= formatString(params.get("requestno"));
		String identityid 				= formatString(params.get("identityid"));
		String identitytype 			= formatString(params.get("identitytype"));
		String cardno 					= formatString(params.get("cardno"));
		String idcardno 				= formatString(params.get("idcardno"));
		String idcardtype 				= formatString(params.get("idcardtype"));
		String username 				= formatString(params.get("username"));
		String phone 					= formatString(params.get("phone"));
		String amount 					= formatString(params.get("amount"));
		String advicesmstype 			= formatString(params.get("advicesmstype"));
		String callbackurl 				= formatString(params.get("callbackurl"));
		String avaliabletime 			= formatString(params.get("avaliabletime"));
		String requesttime				= formatString(params.get("requesttime"));
		String productname 				= formatString(params.get("productname"));
		String terminalid 				= formatString(params.get("terminalid"));
		String registtime 				= formatString(params.get("registtime"));
		String registip 				= formatString(params.get("registip"));
		String issetpaypwd 				= formatString(params.get("issetpaypwd"));
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 		identitytype);
		dataMap.put("cardno", 		cardno);
		dataMap.put("idcardno", 		idcardno);
		dataMap.put("idcardtype", 		idcardtype);
		dataMap.put("username", 		username);
		dataMap.put("phone", 		phone);
		dataMap.put("amount", 		amount);
		dataMap.put("advicesmstype", 	advicesmstype);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("avaliabletime",	 	avaliabletime);
		dataMap.put("requesttime", 	requesttime);
		dataMap.put("productname", 	productname);
		dataMap.put("terminalid", 	terminalid);
		dataMap.put("registtime", 	registtime);
		dataMap.put("registip", 	registip);
		dataMap.put("issetpaypwd", 	issetpaypwd);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("firstPayRequestURL : " + firstPayRequestURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(firstPayRequestURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("jsonStr : " + jsonStr);
			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * fisrstPayConfirm() : 首次充值请求确认接口
	 */

	public static Map<String, String> fisrstPayConfirm(Map<String, String> params) {

		System.out.println("##### fisrstPayConfirm() #####");
				
		String merchantno				= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String fisrstPayConfirmURL		= getFisrstPayConfirmURL();

		String requestno            	= formatString(params.get("requestno"));
		String validatecode      		= formatString(params.get("validatecode"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("validatecode", 	validatecode);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("fisrstPayConfirmURL : " + fisrstPayConfirmURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(fisrstPayConfirmURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * firstPayResendsms() : 首次支付请求重发短验接口
	 */

	public static Map<String, String> firstPayResendsms(Map<String, String> params) {

		System.out.println("##### firstPayResendsms() #####");
				
		String merchantno				= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String firstPayResendsmsURL		= getFirstPayResendsmsURL();

		String requestno            	= formatString(params.get("requestno"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("firstPayResendsmsURL : " + firstPayResendsmsURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(firstPayResendsmsURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * changeCardRequest() : 换卡请求接口
	 */

	public static Map<String, String> changeCardRequest(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String changeCardRequestURL) {

		System.out.println("##### changeCardRequest() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
//		String merchantno					= getMerchantAccount();
//		String merchantPrivateKey			= getMerchantPrivateKey();
//		String merchantAESKey				= getMerchantAESKey();
//		String yeepayPublicKey				= getYeepayPublicKey();
//		String changeCardRequestURL			= getChangeCardRequestURL();

		String requestno 				= formatString(params.get("requestno"));
		String identityid 				= formatString(params.get("identityid"));
		String identitytype 			= formatString(params.get("identitytype"));
		String cardno 					= formatString(params.get("cardno"));
		String idcardno 				= formatString(params.get("idcardno"));
		String idcardtype 				= formatString(params.get("idcardtype"));
		String username 				= formatString(params.get("username"));
		String phone 					= formatString(params.get("phone"));
		String oricardno 				= formatString(params.get("oricardno"));
		String advicesmstype 			= formatString(params.get("advicesmstype"));
		String callbackurl 				= formatString(params.get("callbackurl"));
		String avaliabletime 			= formatString(params.get("avaliabletime"));
		String requesttime				= formatString(params.get("requesttime"));
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 		identitytype);
		dataMap.put("cardno", 		cardno);
		dataMap.put("idcardno", 		idcardno);
		dataMap.put("idcardtype", 		idcardtype);
		dataMap.put("username", 		username);
		dataMap.put("phone", 		phone);
		dataMap.put("oricardno", 		oricardno);
		dataMap.put("advicesmstype", 	advicesmstype);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("avaliabletime",	 	avaliabletime);
		dataMap.put("requesttime", 	requesttime);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("changeCardRequestURL : " + changeCardRequestURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(changeCardRequestURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("jsonStr : " + jsonStr);
			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * changeCardConfirm() : 换卡请求确认接口
	 */

	public static Map<String, String> changeCardConfirm(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String changeCardConfirmURL) {

		System.out.println("##### changeCardConfirm() #####");
				
//		String merchantno				= getMerchantAccount();
//		String merchantPrivateKey		= getMerchantPrivateKey();
//		String merchantAESKey			= getMerchantAESKey();
//		String yeepayPublicKey			= getYeepayPublicKey();
//		String changeCardConfirmURL		= getChangeCardConfirmURL();

		String requestno            	= formatString(params.get("requestno"));
		String validatecode      		= formatString(params.get("validatecode"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("validatecode", 	validatecode);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("changeCardConfirmURL : " + changeCardConfirmURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(changeCardConfirmURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * changeCardResendsms() : 换卡请求重发短验接口
	 */

	public static Map<String, String> changeCardResendsms(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String changeCardResendsmsURL) {

		System.out.println("##### changeCardResendsms() #####");
				
//		String merchantno				= getMerchantAccount();
//		String merchantPrivateKey		= getMerchantPrivateKey();
//		String merchantAESKey			= getMerchantAESKey();
//		String yeepayPublicKey			= getYeepayPublicKey();
//		String changeCardResendsmsURL	= getChangeCardResendsmsURL();

		String requestno            	= formatString(params.get("requestno"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno", 		requestno);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("changeCardResendsmsURL : " + changeCardResendsmsURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(changeCardResendsmsURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");
			System.out.println("statusCode : " + statusCode);
			
			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * queryBindPayRecord() : 绑卡充值记录查询接口
	 */
	
	public static Map<String, String> queryBindPayRecord(String requestno, String yborderid) {
		
		System.out.println("##### queryBindPayRecord() #####");
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bindPayRecordURL		= getBindPayRecordURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno",		requestno);
		dataMap.put("yborderid",		yborderid);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		System.out.println("requestno : " + requestno);
		System.out.println("yborderid : " + yborderid);
		System.out.println("merchantno : " + merchantno);
		System.out.println("merchantPrivateKey : " + merchantPrivateKey);
		System.out.println("yeepayPublicKey : " + yeepayPublicKey);
		System.out.println("bindPayRecordURL : " + bindPayRecordURL);
		System.out.println("dataMap : " + dataMap);
		
		Map<String, String> result		= new HashMap<String, String>();
        String customError     			= "";  // 自定义参数，非接口返回。

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= bindPayRecordURL + 
										  "?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();

			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * changeCardRecord() : 换卡记录查询接口
	 */
	
	public static Map<String, String> changeCardRecord(String requestno, String yborderid) {
		
		System.out.println("##### changeCardRecord() #####");
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String changeCardURL		= getChangeCardURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno",		requestno);
		dataMap.put("yborderid",		yborderid);
		
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
		
		System.out.println("requestno : " + requestno);
		System.out.println("yborderid : " + yborderid);
		System.out.println("merchantno : " + merchantno);
		System.out.println("merchantPrivateKey : " + merchantPrivateKey);
		System.out.println("yeepayPublicKey : " + yeepayPublicKey);
		System.out.println("changeCardURL : " + changeCardURL);
		System.out.println("dataMap : " + dataMap);
		
		Map<String, String> result		= new HashMap<String, String>();
		String customError     			= "";  // 自定义参数，非接口返回。
		
		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			
			String url					= changeCardURL + 
					"?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
					"&data=" + URLEncoder.encode(data, "UTF-8") +
					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
			
			getMethod					= new GetMethod(url);
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();
			
			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);
			
			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);
			
		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);
		
		return (result);
	}
	
	/**
	 * drawValidAmount() : 可提现余额查询
	 */
	
	public static Map<String, String> drawValidAmount() {
		
		System.out.println("##### drawValidAmount() #####");
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String drawValidAmountURL	= getDrawValidAmountURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		System.out.println("drawValidAmountURL : " + drawValidAmountURL);
		System.out.println("dataMap : " + dataMap);
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url				= drawValidAmountURL + 
									  "?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
									  "&data=" + URLEncoder.encode(data, "UTF-8") +
									  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			System.out.println("url	 : " + url);

			getMethod				= new GetMethod(url);
			int statusCode			= httpClient.executeMethod(getMethod);
			String responseBody		= getMethod.getResponseBodyAsString();
			
			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * queryDrawRecord() : 提现查询接口
	 */
	
	public static Map<String, String> queryDrawRecord(String requestno, String yborderid) {
		
		System.out.println("##### queryDrawRecord() #####");
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String queryDrawRecordURL		= getQueryDrawRecordURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("requestno",		requestno);
		dataMap.put("yborderid",		yborderid);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		System.out.println("requestno : " + requestno);
		System.out.println("yborderid : " + yborderid);
		System.out.println("merchantno : " + merchantno);
		System.out.println("merchantPrivateKey : " + merchantPrivateKey);
		System.out.println("yeepayPublicKey : " + yeepayPublicKey);
		System.out.println("queryDrawRecordURL : " + queryDrawRecordURL);
		System.out.println("dataMap : " + dataMap);
		
		Map<String, String> result		= new HashMap<String, String>();
        String customError     			= "";  // 自定义参数，非接口返回。

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= queryDrawRecordURL + 
										  "?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();

			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);

		return (result);
	}

		
	/**
	 * queryBindCardList() : 绑卡查询接口 
	 *
	 */

	public static Map<String, String> queryBindCardList(String identityid, String identitytype) {
		
		System.out.println("##### queryBindCardList() #####");
		
		Map<String, String> result		= new HashMap<String, String>();
		String customError				= "";	//自定义，非接口返回 

		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bindCardListURL	= getQueryBindCardListURL();
		
		System.out.println("merchantAESKey="+merchantAESKey);

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", merchantno);
		dataMap.put("identityid", identityid);
		dataMap.put("identitytype", identitytype);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		System.out.println("bindCardListURL : " + bindCardListURL);
		System.out.println("dataMap : " + dataMap);
		
		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			System.out.println("json="+jsonStr);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			System.out.println("data="+data);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			System.out.println("encryptkey="+encryptkey);
			String url					= bindCardListURL + 
										  "?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);

			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();

			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);

		return (result);
	}
	/**
	 * queryBindCardRecord() : 绑卡记录查询 
	 *
	 */
	
	public static Map<String, String> queryBindCardRecord(String requestno, String yborderid) {
		
		System.out.println("##### queryBindCardRecord() #####");
		
		Map<String, String> result		= new HashMap<String, String>();
		String customError				= "";	//自定义，非接口返回 
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bindCardRecordURL	= getQueryBindCardRecordURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", merchantno);
		dataMap.put("requestno", requestno);
		dataMap.put("yborderid", yborderid);
		
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
		
		System.out.println("bindCardRecordURL : " + bindCardRecordURL);
		System.out.println("dataMap : " + dataMap);
		
		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			
			String url					= bindCardRecordURL + 
					"?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
					"&data=" + URLEncoder.encode(data, "UTF-8") +
					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
			
			getMethod					= new GetMethod(url);
			
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();
			
			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);
			
			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);
			
		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);
		
		return (result);
	}

				
	/**
	 * bankCardCheck() : 银行卡信息查询接口 
	 *
	 */

	public static Map<String, String> bankCardCheck(String cardno) {
		
		System.out.println("##### bankCardCheck() #####");
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bankCardCheckURL		= getBankCardCheckURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("cardno", 			cardno);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bankCardCheckURL : " + bankCardCheckURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";

		HttpClient httpClient		= new HttpClient();
		PostMethod postMethod		= new PostMethod(bankCardCheckURL);

		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data=" + data);
			System.out.println("encryptkey=" + encryptkey);

			NameValuePair[] datas	= {new NameValuePair("merchantno", merchantno),
									   new NameValuePair("data", data),
									   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode			= httpClient.executeMethod(postMethod);
			byte[] responseByte		= postMethod.getResponseBody();
			String responseBody		= new String(responseByte, "UTF-8");

			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	/**
	 * firstpayrecord() : 首次充值记录查询 
	 *
	 */
	
	public static Map<String, String> firstpayrecord(String requestno, String yborderid) {
		
		System.out.println("##### firstpayrecord() #####");
		
		Map<String, String> result		= new HashMap<String, String>();
		String customError				= "";	//自定义，非接口返回 
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String firstpayrecordURL	= getFirstPayRecordURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", merchantno);
		dataMap.put("requestno", requestno);
		dataMap.put("yborderid", yborderid);
		
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
		
		System.out.println("firstpayrecordURL : " + firstpayrecordURL);
		System.out.println("dataMap : " + dataMap);
		
		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			
			String url					= firstpayrecordURL + 
					"?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
					"&data=" + URLEncoder.encode(data, "UTF-8") +
					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
			
			getMethod					= new GetMethod(url);
			
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();
			
			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);
			
			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);
			
		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);
		
		return (result);
	}
	
	/**
	 * withdraw() : 提现接口
	 */

	public static Map<String, String> withdraw(Map<String, String> params) {

		System.out.println("##### withdraw() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
		String merchantno					= getMerchantAccount();
		String merchantPrivateKey			= getMerchantPrivateKey();
		String merchantAESKey				= getMerchantAESKey();
		String yeepayPublicKey				= getYeepayPublicKey();
		String withdrawURL					= getWithdrawURL();

		String requestno            		= formatString(params.get("requestno"));
		String identityid           		= formatString(params.get("identityid"));
		String identitytype           		= formatString(params.get("identitytype"));
		String cardtop             			= formatString(params.get("cardtop"));
		String cardlast            			= formatString(params.get("cardlast"));
		String amount                   	= formatString(params.get("amount"));
		String currency                   	= formatString(params.get("currency"));
		String drawtype             		= formatString(params.get("drawtype"));
		String callbackurl                  = formatString(params.get("callbackurl"));
		String userip               		= formatString(params.get("userip"));
		String requesttime             		= formatString(params.get("requesttime"));
		String free1                   		= formatString(params.get("free1"));
		String free2                   		= formatString(params.get("free2"));
		String free3                   		= formatString(params.get("free3"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno",	merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("cardtop", 		cardtop);
		dataMap.put("cardlast", 		cardlast);
		dataMap.put("amount", 			amount);
		dataMap.put("currency", 		currency);
		dataMap.put("drawtype", 		drawtype);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("userip", 			userip);
		dataMap.put("requesttime", 		requesttime);
		dataMap.put("free1", 			free1);
		dataMap.put("free2", 			free2);
		dataMap.put("free3", 			free3);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("withdrawURL : " + withdrawURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(withdrawURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * accountCheck() : 对账
	 */

	public static Map<String, String> accountCheck(Map<String, String> params) {

		System.out.println("##### accountCheck() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
		String merchantno					= getMerchantAccount();
		String merchantPrivateKey			= getMerchantPrivateKey();
		String merchantAESKey				= getMerchantAESKey();
		String yeepayPublicKey				= getYeepayPublicKey();
		
		String startdate 				= formatString(params.get("startdate"));
		String enddate 					= formatString(params.get("enddate"));
		String orderType 				= formatString(params.get("orderType"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", 	merchantno);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 		enddate);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= null;
		System.out.println("orderType : " + orderType);
		if("auth".equals(orderType)){
			String accountCheckAuthURL			= getaccountCheckAuthURL();
			System.out.println("accountCheckAuthURL : " + accountCheckAuthURL);			
			postMethod			= new PostMethod(accountCheckAuthURL);
		}
		if("payment".equals(orderType)){
			String accountCheckPaymentURL			= getaccountCheckPaymentURL();
			System.out.println("accountCheckPaymentURL : " + accountCheckPaymentURL);			
			postMethod			= new PostMethod(accountCheckPaymentURL);
		}
		if("withdraw".equals(orderType)){
			String accountCheckWithDrawURL			= getaccountCheckWithDrawURL();
			System.out.println("accountCheckWithDrawURL : " + accountCheckWithDrawURL);		
			postMethod			= new PostMethod(accountCheckWithDrawURL);
		}
		if("changcard".equals(orderType)){
			String accountCheckChangeCardURL			= getaccountCheckChangeCardURL();
			System.out.println("accountCheckChangeCardURL : " + accountCheckChangeCardURL);			
			postMethod			= new PostMethod(accountCheckChangeCardURL);
		}
		if("refund".equals(orderType)){
			String refundURL			= getaccountCheckRefundURL();
			System.out.println("refundURL : " + refundURL);			
			postMethod			= new PostMethod(refundURL);
		}
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("jsonStr : " + jsonStr);
			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError	= "Caught Exception!" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * refund() : 退款接口
	 */

	public static Map<String, String> refund(Map<String, String> params) {

		System.out.println("##### refund() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回
				
		String merchantno					= getMerchantAccount();
		String merchantPrivateKey			= getMerchantPrivateKey();
		String merchantAESKey				= getMerchantAESKey();
		String yeepayPublicKey				= getYeepayPublicKey();
		String refundURL					= getRefundURL();

		String requestno            		= formatString(params.get("requestno"));
		String paymentyborderid           	= formatString(params.get("paymentyborderid"));
		String amount                   	= formatString(params.get("amount"));
		String callbackurl                  = formatString(params.get("callbackurl"));
		String requesttime             		= formatString(params.get("requesttime"));
		String remark 						= formatString(params.get("remark"));
		String free1                   		= formatString(params.get("free1"));
		String free2                   		= formatString(params.get("free2"));
		String free3                   		= formatString(params.get("free3"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno",		merchantno);
		dataMap.put("requestno", 		requestno);
		dataMap.put("paymentyborderid", 		paymentyborderid);
		dataMap.put("amount", 			amount);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("requesttime", 		requesttime);
		dataMap.put("remark", 			remark);
		dataMap.put("free1", 			free1);
		dataMap.put("free2", 			free2);
		dataMap.put("free3", 			free3);
						
		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("refundURL : " + refundURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(refundURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	/**
	 * queryRefundRecord() : 退款记录查询 
	 *
	 */
	
	public static Map<String, String> queryRefundRecord(String requestno, String yborderid) {
		
		System.out.println("##### queryRefundRecord() #####");
		
		Map<String, String> result		= new HashMap<String, String>();
		String customError				= "";	//自定义，非接口返回 
		
		String merchantno		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundrecordURL	= getRefundRecordURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", merchantno);
		dataMap.put("requestno", requestno);
		dataMap.put("yborderid", yborderid);
		
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
		
		System.out.println("refundrecordURL : " + refundrecordURL);
		System.out.println("dataMap : " + dataMap);
		
		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			
			String url					= refundrecordURL + 
					"?merchantno=" + URLEncoder.encode(merchantno, "UTF-8") + 
					"&data=" + URLEncoder.encode(data, "UTF-8") +
					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
			
			getMethod					= new GetMethod(url);
			
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();
			
			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);
			
			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);
			
		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		System.out.println("result : " + result);
		
		return (result);
	}
	
	/**
	 * unbindRequest() : 解绑卡
	 *
	 */
	
	public static Map<String, String> unbindRequest(Map<String, String> params,String merchantno,String merchantPrivateKey,String merchantAESKey,String yeepayPublicKey,String unbindRequestURL) {
		
		System.out.println("##### unbindRequest() #####");
		
		Map<String, String> result		= new HashMap<String, String>();
		String customError				= "";	//自定义，非接口返回 
		
//		String merchantno			= getMerchantAccount();
//		String merchantPrivateKey	= getMerchantPrivateKey();
//		String merchantAESKey		= getMerchantAESKey();
//		String yeepayPublicKey		= getYeepayPublicKey();
//		String unbindRequestURL		= getUnbindRequestURL();
		
		String identityid 			= formatString(params.get("identityid"));
		String identitytype 		= formatString(params.get("identitytype"));
		String cardtop 				= formatString(params.get("cardtop"));
		String cardlast 			= formatString(params.get("cardlast"));

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantno", merchantno);
		dataMap.put("identityid", identityid);
		dataMap.put("identitytype", identitytype);
		dataMap.put("cardtop", cardtop);
		dataMap.put("cardlast", cardlast);
		
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
		
		System.out.println("unbindRequestURL : " + unbindRequestURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(unbindRequestURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantno", merchantno),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			System.out.println("responseBody : " + responseBody);

			result = parseHttpResponseBody(statusCode, responseBody,merchantPrivateKey,yeepayPublicKey);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}
	
	public static void main(String[] args) throws Exception {

	}
}
