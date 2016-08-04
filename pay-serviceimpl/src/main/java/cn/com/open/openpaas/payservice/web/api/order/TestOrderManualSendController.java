package cn.com.open.openpaas.payservice.web.api.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayCore;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
 */
@Controller
@RequestMapping("/alipay/test")
public class TestOrderManualSendController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(TestOrderManualSendController.class);
	
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
		 log.info("~~~~~~~~~~~~~~~~~~~~~~ordermanualSend start~~~~~~~~~~~~~~~~~~~~~~~~");
		 long startTime = System.currentTimeMillis();
		 //  135-2025-0945
		String outTradeNo=request.getParameter("orderId");//业务方订单唯一ID
		String appId=request.getParameter("appId");
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String signatureNonce=request.getParameter("signatureNonce");
		
		log.info("~~~~~~~outTradeNo："+outTradeNo+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		 PayServiceLog payServiceLog=new PayServiceLog();
		 payServiceLog.setAppId(appId);
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setMerchantOrderId(outTradeNo);
		 payServiceLog.setLogName(PayLogName.ORDER_MANUAL_START);
         payServiceLog.setStatus("ok");
         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		
		String result = "";
    	//获取当前订单
		MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		if(!paraMandatoryCheck(Arrays.asList(outTradeNo,appId))){
			  payServiceLog.setErrorCode("4");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	paraMandaChkAndReturn(4, response,"必传参数中有空值");
            return;
        }
		if(orderInfo==null){
			  payServiceLog.setErrorCode("3");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
			paraMandaChkAndReturn(3, response,"业务方订单号不存在");
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
            return;
		}
		payServiceLog.setAmount(String.valueOf(orderInfo.getPayAmount()*100));
		payServiceLog.setRealAmount(String.valueOf(orderInfo.getPayAmount()*100));
		payServiceLog.setProductName(orderInfo.getMerchantProductName());
		payServiceLog.setOrderId(orderInfo.getId());
		payServiceLog.setChannelId(String.valueOf(orderInfo.getChannelId()));
		payServiceLog.setPaymentId(String.valueOf(orderInfo.getPaymentId()));
		payServiceLog.setPaySwitch(String.valueOf(orderInfo.getSourceType()));
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
			 payServiceLog.setErrorCode("1");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			paraMandaChkAndReturn(1, response,"认证失败");
			return;
		} 
		
		if(orderInfo.getPayStatus()==null || orderInfo.getPayStatus()==0){
			 payServiceLog.setErrorCode("2");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	paraMandaChkAndReturn(2, response,"第三方未处理");
            return;
		}
		if(orderInfo.getPayStatus()==2){
			 payServiceLog.setErrorCode("2");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	paraMandaChkAndReturn(2, response,"第三方处理失败");
            return;
		}
		if(orderInfo.getPayStatus()==1){
			//DictTradeChannel channel=dictTradeChannelService.findByMAI(orderInfo.getMerchantId()+"", orderInfo.getChannelId());
			SortedMap<Object, Object> params = new TreeMap<Object,Object>();
			params.put("orderId", orderInfo.getId());
			params.put("outTradeNo", orderInfo.getMerchantOrderId());
			params.put("merchantId", String.valueOf(orderInfo.getMerchantId()));
			params.put("paymentType", String.valueOf(orderInfo.getPaymentId()));
			params.put("paymentChannel", String.valueOf(orderInfo.getChannelId()));
			params.put("feeType", "CNY");
			params.put("guid", orderInfo.getGuid());
			params.put("appUid",String.valueOf(orderInfo.getSourceUid()));
			//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
			params.put("timeEnd", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
			params.put("totalFee", String.valueOf((int)(orderInfo.getPayAmount()*100)));
			params.put("goodsId", orderInfo.getMerchantProductId());
			params.put("goodsName",orderInfo.getMerchantProductName());
			params.put("goodsDesc", orderInfo.getMerchantProductDesc());
			params.put("parameter", orderInfo.getParameter1());
			params.put("userName", orderInfo.getSourceUserName());
    		
    		log.info("~~~~~~~~~orderManualSend params："+AlipayCore.createLogString(params));
    		
    		String secret=PayUtil.createSign(payserviceDev.getAli_input_charset(),params,merchantInfo.getPayKey());
    		params.put("secret", secret);

    		log.info("~~~~~~~~~orderManualSend secret："+secret);
    		
    		//向业务方发送订单状态信息
    		if(orderInfo.getNotifyUrl()!=null && !("").equals(orderInfo.getNotifyUrl())){
    			result = sendOrderPost(orderInfo.getNotifyUrl(),params);
    		}else{
    			if(merchantInfo!=null){
    				result = sendOrderPost(merchantInfo.getNotifyUrl(),params);
    			}
    		}
    		if(result != null && !("").equals(result)){
    			log.info("~~~~~~~~~~~~~~orderManualSend result："+result+"~~~~~~~~~~~~~~~~~~~~");
    			Map map=(Map) JSONObject.toBean(JSONObject.fromObject(result),Map.class);
    			if("ok".equals(map.get("state"))){//商户处理成功
    				orderInfo.setNotifyStatus(1);
    				    payServiceLog.setStatus("ok");
    			        payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
    			        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    			}else{
    				    payServiceLog.setStatus("error");
    				    payServiceLog.setErrorCode(map.get("errorCode").toString());
    			        payServiceLog.setLogName(PayLogName.ORDER_MANUAL_END);
    			        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    				orderInfo.setNotifyStatus(2);
    			}
				orderInfo.setNotifyTimes();
				orderInfo.setNotifyDate(new Date());
				merchantOrderInfoService.updateNotifyStatus(orderInfo);//更新订单状态
				writeSuccessJson(response, map);
    		}
	    }
	    log.info("~~~~~~~~~~~~~~~orderManualSend end~~~~~~~~~~~~~~~~");
    }
	
}