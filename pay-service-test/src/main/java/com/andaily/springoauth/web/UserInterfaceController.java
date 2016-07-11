package com.andaily.springoauth.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import com.andaily.springoauth.service.dto.DirctPayDto;
import com.andaily.springoauth.service.dto.OrderRefund;
import com.andaily.springoauth.service.dto.UnifyPayDto;

import com.andaily.springoauth.tools.AESUtil;
import com.andaily.springoauth.tools.DESUtil;
import com.andaily.springoauth.tools.DateTools;
import com.andaily.springoauth.tools.HMacSha1;
import com.andaily.springoauth.tools.LoadPopertiesFile;
import com.andaily.springoauth.tools.PayUtil;
import com.andaily.springoauth.tools.WebUtils;

/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class UserInterfaceController {

    private static final Logger LOG = LoggerFactory.getLogger(UserInterfaceController.class);
    @Value("#{properties['dirct-pay-uri']}")
    private String dirctPayUri;
    @Value("#{properties['unify-pay-uri']}")
    private String unifyPayUri;
    @Value("#{properties['alipay-order-callback']}")
    private String aliPayCallBackUri;
    @Value("#{properties['order-manual-send']}")
    private String orderManualSendUri;
    @Value("#{properties['order-query-uri']}")
    private String orderQueryUri;
    @Value("#{properties['order-refund-uri']}")
    private String orderRefundUri;
    final static String  SEPARATOR = "&";
    private Map<String,String> map=LoadPopertiesFile.loadProperties();
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "dirctPay", method = RequestMethod.GET)
 	public String dirctPay(Model model) {
 		model.addAttribute("dirctPayUri", dirctPayUri);
 		return "usercenter/user_dirct_pay";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
     @RequestMapping(value = "dirctPay", method = RequestMethod.POST)
     public String dirctPay(DirctPayDto dirctPayDto) throws Exception {
         final String fullUri =dirctPayDto.getFullUri();
         LOG.debug("Send to Oauth-Server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "alipayCallBack", method = RequestMethod.GET)
 	public String alipayCallBack(Model model) {
 		model.addAttribute("aliPayCallBackUri", aliPayCallBackUri);
 		return "usercenter/alipay_order_callback";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
     @RequestMapping(value = "alipayCallBack", method = RequestMethod.POST)
     public String alipayCallBack(String notify_time,String notify_type,String notify_id,String sign_type,String sign) throws Exception {
    	 String out_trade_no=AlipayCallbackConfig.out_trade_no;
    	 String total_fee=AlipayCallbackConfig.total_fee;
    	 String trade_status=AlipayCallbackConfig.trade_status;
    	 String subject=AlipayCallbackConfig.subject;
    	 String body=AlipayCallbackConfig.body;
         final String fullUri =aliPayCallBackUri+"?notify_time="+notify_time+"&notify_type="+notify_type+"&notify_id="+notify_id+"&sign_type="+sign_type+"&sign="+sign+"&out_trade_no="+out_trade_no+"&total_fee="+total_fee+"&trade_status="+trade_status+"&subject="+subject+"&body="+body;
         LOG.debug("Send to Oauth-Server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
     
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "unifyPay", method = RequestMethod.GET)
 	public String unifyPay(Model model) {
 		model.addAttribute("unifyPayUri", unifyPayUri);
 		return "usercenter/user_unify_pay";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
     @RequestMapping(value = "unifyPay", method = RequestMethod.POST)
     public String unifyPay(UnifyPayDto unifyPayDto,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	 String key=map.get(unifyPayDto.getAppId());
   	  	 String signature="";
   	  	 String timestamp="";
   	  	 String signatureNonce="";
   	   	 if(key!=null){
	   		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	   		timestamp=DateTools.getSolrDate(new Date());
	   		signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	   		sParaTemp.put("appId",unifyPayDto.getAppId());
	   		sParaTemp.put("timestamp", timestamp);
	   		sParaTemp.put("signatureNonce", signatureNonce);
	   		sParaTemp.put("outTradeNo",unifyPayDto.getOutTradeNo() );
	   		sParaTemp.put("userId", unifyPayDto.getUserId());
	   		sParaTemp.put("goodsName", unifyPayDto.getGoodsName());
	   		sParaTemp.put("totalFee", unifyPayDto.getTotalFee());
	   		sParaTemp.put("merchantId", unifyPayDto.getMerchantId());
	   		sParaTemp.put("businessType", unifyPayDto.getBusinessType());
	   		String params=createSign(sParaTemp);
	   		signature=HMacSha1.HmacSHA1Encrypt(params, key);
	   		signature=HMacSha1.getNewResult(signature);
   	   	 }
   		 final String fullUri =unifyPayDto.getFullUri()+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
         LOG.debug("Send to pay-service-server URL: {}", fullUri);
         return "redirect:" +  fullUri;
	   /*	 if(key!=null){
	   		timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(unifyPayDto.getAppId());
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
				result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				
				
				
				result=HMacSha1.getNewResult(result);
	   	 }
    	 final String fullUri =unifyPayDto.getFullUri()+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
         LOG.debug("Send to pay-service-server URL: {}", fullUri);
         return "redirect:" +  fullUri;*/
     }
     
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "orderManualSend", method = RequestMethod.GET)
 	public String orderManualSend(Model model) {
 		model.addAttribute("orderManualSendUri", orderManualSendUri);
 		return "usercenter/order_manual_send";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
     @RequestMapping(value = "orderManualSend", method = RequestMethod.POST)
     public String orderManualSend(String orderId, String appId) throws Exception {
    	 
    	 String key=map.get(appId);
   	  	 String result="";
   	  	 String timestamp="";
   	  	 String signatureNonce="";
	   	 if(key!=null){
	   		timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(appId);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
				result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				result=HMacSha1.getNewResult(result);
	   	 }
    	 
    	 final String fullUri = orderManualSendUri+"?orderId="+orderId+"&appId="+appId+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
         LOG.debug("Send to pay-service-server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "orderQuery", method = RequestMethod.GET)
 	public String orderQuery(Model model) {
 		model.addAttribute("orderQueryUri", orderQueryUri);
 		return "usercenter/user_order_query";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
     @RequestMapping(value = "orderQuery", method = RequestMethod.POST)
     public String orderQuery(String  outTradeNo,String appId) throws Exception {
    	 String key=map.get(appId);
   	  	 String result="";
   	  	 String timestamp="";
   	  	 String signatureNonce="";
	   	 if(key!=null){
	   		    timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(appId);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
				result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				result=HMacSha1.getNewResult(result);
	   	 }
    	 final String fullUri =orderQueryUri+"?outTradeNo="+outTradeNo+"&appId="+appId+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
         LOG.debug("Send to pay-service-server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }
     /*
      *  Entrance:   step-1
      * */
 	@RequestMapping(value = "orderRefund", method = RequestMethod.GET)
 	public String orderRefund(Model model) {
 		model.addAttribute("orderRefundUri", orderRefundUri);
 		return "usercenter/user_order_refund";
 	}
 	/* 
     * Redirect to oauth-server bind page:   step-2
     * */
     @RequestMapping(value = "orderRefund", method = RequestMethod.POST)
     public String orderRefund(OrderRefund orderRefund) throws Exception {
    	 String key=map.get(orderRefund.getAppId());
   	  	 String result="";
   	  	 String timestamp="";
   	  	 String signatureNonce="";
	   	 if(key!=null){
	   		    timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(orderRefund.getAppId());
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
				result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				result=HMacSha1.getNewResult(result);
	   	 }
    	 final String fullUri =orderRefund.getFullUri()+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
         LOG.debug("Send to pay-service-server URL: {}", fullUri);
         return "redirect:" + fullUri;
     }     
     
     /** 
      * 发送POST请求 
      *  
      * @param url 
      *            目的地址 
      * @param parameters 
      *            请求参数，Map类型。 
      * @return 远程响应结果 
      */  
     public static String sendPost(String url, Map<String, String> parameters) {  
         String result = "";// 返回的结果  
         BufferedReader in = null;// 读取响应输入流  
         PrintWriter out = null;  
         StringBuffer sb = new StringBuffer();// 处理请求参数  
         String params = "";// 编码之后的参数  
         try {  
             // 编码请求参数  
             if (parameters.size() == 1) {  
                 for (String name : parameters.keySet()) {  
                     sb.append(name).append("=").append(  
                             java.net.URLEncoder.encode(parameters.get(name),  
                                     "UTF-8"));  
                 }  
                 params = sb.toString();  
             } else {  
                 for (String name : parameters.keySet()) {  
                     sb.append(name).append("=").append(  
                            parameters.get(name)).append("&");  
                 }  
                 String temp_params = sb.toString();  
                 params = temp_params.substring(0, temp_params.length() - 1);  
             }  
             // 创建URL对象  
             java.net.URL connURL = new java.net.URL(url);  
             // 打开URL连接  
             java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                     .openConnection();  
             // 设置通用属性  
             httpConn.setRequestProperty("Accept", "*/*");  
             httpConn.setRequestProperty("Connection", "Keep-Alive");  
             httpConn.setRequestProperty("User-Agent",  
                     "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
             // 设置POST方式  
             httpConn.setDoInput(true);  
             httpConn.setDoOutput(true);  
             // 获取HttpURLConnection对象对应的输出流  
             out = new PrintWriter(httpConn.getOutputStream());  
             // 发送请求参数  
             out.write(params);  
             // flush输出流的缓冲  
             out.flush();  
             // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
             in = new BufferedReader(new InputStreamReader(httpConn  
                     .getInputStream(), "gb2312"));  
             String line;  
             // 读取返回的内容  
             while ((line = in.readLine()) != null) {  
                 result += line;  
                 
             }  
             System.out.println(result);
         } catch (Exception e) {  
             e.printStackTrace();  
         } finally {  
             try {  
                 if (out != null) {  
                     out.close();  
                 }  
                 if (in != null) {  
                     in.close();  
                 }  
             } catch (IOException ex) {  
                 ex.printStackTrace();  
             }  
         }  
         return result;  
     }  

     /**
 	 * 获取 HMAC-SHA1 签名方法对对encryptText进行签名 值
 	 * @param request
 	 * @param response
 	 * @param appId
 	 * @param accessToken
 	 */
 	    @RequestMapping(value = "/getSignature",method = RequestMethod.POST)
 	    public void getSignature(HttpServletRequest request,HttpServletResponse response,String appId){
 	    	Boolean flag=true;
 	    	String key=map.get(appId);
 	   	  	String result="";
 	   	  	String timestamp="";
 	   	  	String signatureNonce="";
 		   	if(key!=null){
 		   		timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(appId);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
				try {
					result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				} catch (Exception e) {
					flag=false;
					e.printStackTrace();
				}				
				result=HMacSha1.getNewResult(result);
 		   	 }
 	      	Map<String,Object> returnMap = new HashMap<String, Object>();
 	      	returnMap.put("flag",flag);
 	      	returnMap.put("signature",result);
 	      	returnMap.put("timestamp",timestamp);
 	      	returnMap.put("signatureNonce",signatureNonce);
 	    	WebUtils.writeJsonToMap(response, returnMap);
 	    }

     /* 
      * Redirect to oauth-server bind page:   step-2
      * */
      @RequestMapping(value = "/getAlipayCallBackUrl", method = RequestMethod.POST)
      public void getAlipayCallBackUrl(HttpServletRequest request,HttpServletResponse response,String notify_time,String notify_type,String notify_id,String sign_type,String sign) throws Exception {
     	 String out_trade_no=AlipayCallbackConfig.out_trade_no;
     	 String total_fee=AlipayCallbackConfig.total_fee;
     	 String trade_status=AlipayCallbackConfig.trade_status;
     	 String subject=AlipayCallbackConfig.subject;
     	 String body=AlipayCallbackConfig.body;
         final String fullUri =aliPayCallBackUri+"?notify_time="+notify_time+"&notify_type="+notify_type+"&notify_id="+notify_id+"&sign_type="+sign_type+"&sign="+sign+"&out_trade_no="+out_trade_no+"&total_fee="+total_fee+"&trade_status="+trade_status+"&subject="+subject+"&body="+body;
         Map<String,Object> returnMap = new HashMap<String, Object>();
	     returnMap.put("fullUri",fullUri);
         WebUtils.writeJsonToMap(response, returnMap);
      }
      /**
		 * 生成加密串
		 * @param characterEncoding
		 * @param parameters
		 * @return
		 */
		public static String createSign(SortedMap<Object,Object> parameters){
			StringBuffer sb = new StringBuffer();
			Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
			Iterator it = es.iterator();
			while(it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				String k = (String)entry.getKey();
				Object v = entry.getValue();
				if(null != v && !"".equals(v)&& !"null".equals(v) 
						&& !"sign".equals(k) && !"key".equals(k)) {
					sb.append(k + "=" + v + "&");
				}
			}
			 String temp_params = sb.toString();  
			return sb.toString().substring(0, temp_params.length()-1);
		}
}