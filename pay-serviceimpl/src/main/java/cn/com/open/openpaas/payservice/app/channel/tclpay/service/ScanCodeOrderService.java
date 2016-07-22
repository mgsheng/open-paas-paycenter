package cn.com.open.openpaas.payservice.app.channel.tclpay.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.CommonConfig;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytHttpsClient;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;


/**
 * 名称：扫码支付服务类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class ScanCodeOrderService {

	/**
	 * 扫码订单提交
	 * 
	 * @param dataMap
	 */
	public String order(Map<String, String> dataMap) {
		RSASign util = HytUtils.getRSASignObject();
		String url = CommonConfig.HYT_SERVICE_URL;
		String reqData = HytPacketUtils.map2Str(dataMap);
		String response = "";
		try {
			String merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
			String merchant_cert = util.getCertInfo();
			// -- ������
			String buf = reqData + HytConstants.SYMBOL_AND
					+ HytParamKeys.MERCHANT_SIGN + HytConstants.SYMBOL_EQUAL
					+ merchant_sign + HytConstants.SYMBOL_AND
					+ HytParamKeys.MERCHANT_CERT + HytConstants.SYMBOL_EQUAL
					+ merchant_cert;
			System.out.println("==================request===============>>>>"
					+ buf);
			HttpsURLConnection httpsURLConnection = HytHttpsClient
					.getHttpsURLConnection(url);
			response = HytHttpsClient.doHttpsPost(httpsURLConnection, buf,
					HytConstants.CHARSET_GBK);
			System.out.println("==================response===============>>>>"
					+ response);
			
			
			 Map<String,String> retMap = new LinkedHashMap<String,String>();
             String Wsign=HytUtils.getVertifyFromStr(response,retMap); //获得待签名报文
             String server_sign=retMap.get(HytParamKeys.SERVER_SIGN);
             String  server_cert=retMap.get(HytParamKeys.SERVER_CERT);
             System.out.println("==================待签名报文===============>>>>"+ Wsign);
             // -- 验证签名
				boolean flag = false;	
				
				RSASign rsautil =HytUtils.getRSASignVertifyObject(); 
			    flag = rsautil.verify(Wsign,server_sign,server_cert, HytConstants.CHARSET_GBK);//验证签名
			    if (!flag) {
					  String info="错误信息：验签错误";
					  String CODE="MCG111111";
					  System.out.println("{\"return_code\":\""+CODE+"\",\"return_message\":\""+URLDecoder.decode(info,"UTF-8")+"\"}");				
					return "";
				}
			    String code = (String)retMap.get("return_code");
				if (!code.equals("000000")) { //请求异常
					System.out.println("{\"return_code\":\""+code+"\",\"return_message\":\""+URLDecoder.decode((String)retMap.get("return_message"),"UTF-8")+"\"}");				
					return "";
				} 
				String qr_code_url=retMap.get("qr_code_url");
				//返回数据正常，业务处理
				/*System.out.print("正常返回,商户可进行后续逻辑处理............{\"charset\":\""+(String)retMap.get("charset")+"\",\"version\":\""
						    +(String)retMap.get("version")+"\" ,\"sign_type\":\""+(String)retMap.get("sign_type")
						    +"\" ,\"return_code\":\""+(String)retMap.get("return_code")+"\",\"return_message\":\""+URLDecoder.decode((String)retMap.get("return_message"),"UTF-8")
						    +"\",\"trade_no\":\""+(String)retMap.get("trade_no")+"\",\"merchant_code\":\""+(String)retMap.get("merchant_code")
						    +"\",\"app_no\":\""+(String)retMap.get("app_no")
						    +"\",\"credential\":"+retMap.get("credential").replaceAll("&","%26")+"}");*/
				return qr_code_url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 扫码订单提交
	 * 
	 * @param dataMap
	 */
	public String Aliorder(Map<String, String> dataMap) {
		RSASign util = HytUtils.getRSASignObject();
		String url = CommonConfig.HYT_SERVICE_URL;
		String reqData = HytPacketUtils.map2Str(dataMap);
		String qr_code_url="";
		String response = "";
		try {
			String merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
			String merchant_cert = util.getCertInfo();
			// -- ������
			String buf = reqData + HytConstants.SYMBOL_AND
					+ HytParamKeys.MERCHANT_SIGN + HytConstants.SYMBOL_EQUAL
					+ merchant_sign + HytConstants.SYMBOL_AND
					+ HytParamKeys.MERCHANT_CERT + HytConstants.SYMBOL_EQUAL
					+ merchant_cert;
			System.out.println("==================request===============>>>>"
					+ buf);
			HttpsURLConnection httpsURLConnection = HytHttpsClient
					.getHttpsURLConnection(url);
			response = HytHttpsClient.doHttpsPost(httpsURLConnection, buf,
					HytConstants.CHARSET_GBK);
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 扫码订单提交
	 * 
	 * @param dataMap
	 */
	public String Aliorder1(Map<String, String> dataMap) {
		RSASign util = HytUtils.getRSASignObject();
		String reqData = HytPacketUtils.map2Str(dataMap);
		String reqData2 = HytPacketUtils.map2StrRealURL(dataMap);
		String buf ="";
		try {
			String merchant_sign = util.sign(reqData, "GBK");
			String merchant_cert = util.getCertInfo();
			buf = reqData2 + HytConstants.SYMBOL_AND
					+ HytParamKeys.MERCHANT_SIGN + HytConstants.SYMBOL_EQUAL
					+ merchant_sign + HytConstants.SYMBOL_AND
					+ HytParamKeys.MERCHANT_CERT + HytConstants.SYMBOL_EQUAL
					+ merchant_cert;
			System.out.println("==================request===============>>>>"
					+ buf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf;
	}
	/**
	 * 扫码订单提交
	 * 
	 * @param dataMap
	 */
	public String bulidPostRequest(Map<String, String> dataMap,String url) {
		RSASign util = HytUtils.getRSASignObject();
		String reqData = HytPacketUtils.map2Str(dataMap);
		String buf ="";
		try {
			String merchant_sign = util.sign(reqData, "GBK");
			String merchant_cert = util.getCertInfo();
			dataMap.put("merchant_sign", merchant_sign);
			dataMap.put("merchant_cert", merchant_cert);
			buf =SendPostMethod.buildRequest(dataMap, "post", "ok", url);
			System.out.println("==================request===============>>>>"
					+ buf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf;
	}

}
