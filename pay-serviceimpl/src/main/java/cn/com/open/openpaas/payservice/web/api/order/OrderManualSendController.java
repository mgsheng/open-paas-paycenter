package cn.com.open.openpaas.payservice.web.api.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayCore;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
 */
@Controller
@RequestMapping("/alipay/")
public class OrderManualSendController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(OrderManualSendController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 
	 /**
     * 订单手动补发接口
     * @return Json
     */
	 @RequestMapping("orderManualSend")
	 public void orderManualSend(HttpServletRequest request,HttpServletResponse response) throws MalformedURLException, DocumentException, IOException {
		 log.info("~~~~~~~~~~~~~~~~~~~~~~订单手动补发开始执行~~~~~~~~~~~~~~~~~~~~~~~~");
		 //  135-2025-0945
		String outTradeNo=request.getParameter("orderId");//业务方订单唯一ID
		String appId=request.getParameter("appId");
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String signatureNonce=request.getParameter("signatureNonce");
		
		log.info("~~~~~~~传入的业务订单号："+outTradeNo+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String result = "";
    	//获取当前订单
		MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		if(!paraMandatoryCheck(Arrays.asList(outTradeNo,appId))){
        	paraMandaChkAndReturn(4, response,"必传参数中有空值");
            return;
        }
		if(orderInfo==null){
			paraMandaChkAndReturn(3, response,"业务方订单号不存在");
            return;
		}
		
		MerchantInfo merchantInfo=merchantInfoService.findById(orderInfo.getMerchantId());
		//认证
	 	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo);
   		String param=createSign(sParaTemp);
   		
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,param,merchantInfo.getPayKey());
        //Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
		if(!hmacSHA1Verification){
			paraMandaChkAndReturn(1, response,"认证失败");
			return;
		} 
		
		if(orderInfo.getPayStatus()==null || orderInfo.getPayStatus()==0){
        	paraMandaChkAndReturn(2, response,"第三方未处理");
            return;
		}
		if(orderInfo.getPayStatus()==2){
        	paraMandaChkAndReturn(2, response,"第三方处理失败");
            return;
		}
		if(orderInfo.getPayStatus()==1){
			//DictTradeChannel channel=dictTradeChannelService.findByMAI(orderInfo.getMerchantId()+"", orderInfo.getChannelId());
			SortedMap<Object, Object> params = new TreeMap<Object,Object>();
    		params.put("orderId", orderInfo.getId());
    		params.put("outTradeNo", orderInfo.getMerchantOrderId());
    		params.put("merchantId", orderInfo.getMerchantId());
    		params.put("paymentChannel", orderInfo.getChannelId());
    		params.put("paymentType", orderInfo.getPaymentId());
    		params.put("feeType", "CNY");
    		params.put("guid", orderInfo.getGuid());
    		params.put("appUid", orderInfo.getSourceUid());
    		params.put("timeEnd", orderInfo.getDealDate());
    		params.put("totalFee", orderInfo.getOrderAmount());
    		params.put("goodsId", orderInfo.getMerchantProductId());
    		params.put("goodsName", orderInfo.getMerchantProductName());
    		params.put("goodsDesc", orderInfo.getMerchantProductDesc());
    		params.put("parameter", orderInfo.getParameter1());
    		
    		log.info("~~~~~~~~~订单手补发参数："+AlipayCore.createLogString(params));
    		
    		String secret=PayUtil.createSign(payserviceDev.getAli_input_charset(),params,merchantInfo.getPayKey());
    		params.put("secret", secret);

    		log.info("~~~~~~~~~订单手动补发加密信息："+secret);
    		
    		//向业务方发送订单状态信息
    		if(orderInfo.getNotifyUrl()!=null && !("").equals(orderInfo.getNotifyUrl())){
    			result = sendPost(orderInfo.getNotifyUrl(),params);
    		}else{
    			if(merchantInfo!=null){
    				result = sendPost(merchantInfo.getNotifyUrl(),params);
    			}
    		}
    		if(result != null && !("").equals(result)){
    			log.info("~~~~~~~~~~~~~~商户返回结果："+result+"~~~~~~~~~~~~~~~~~~~~");
    			Map map=(Map) JSONObject.toBean(JSONObject.fromObject(result),Map.class);
    			if("ok".equals(map.get("state"))){//商户处理成功
    				orderInfo.setNotifyStatus(1);
    			}else{
    				orderInfo.setNotifyStatus(2);
    			}
				orderInfo.setNotifyTimes();
				orderInfo.setNotifyDate(new Date());
				merchantOrderInfoService.updateNotifyStatus(orderInfo);//更新订单状态
    		}
	    }
	    log.info("~~~~~~~~~~~~~~~订单手动补发结束执行~~~~~~~~~~~~~~~~");
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
    public static String sendPost(String url, SortedMap<Object, Object> params2) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数  
        SortedMap<Object, Object> parameters = params2;
        try {  
            // 编码请求参数  
            if (parameters.size() == 1) {  
                for (Object name : parameters.keySet()) {  
                    sb.append(name).append("=").append(  
                            java.net.URLEncoder.encode((String) parameters.get(name),  
                                    "UTF-8"));  
                }  
                params = sb.toString();  
            } else {  
                for (Object name : parameters.keySet()) {  
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
            //out.write(params); 
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
}