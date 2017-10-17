package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;
@Controller
@RequestMapping("/pay/redic/")
public class RedicPayIndexController extends BaseControllerUtil {
	

	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	
	   /**
     * 跳转收银台页面
     * @throws  
     */
    @RequestMapping(value = "skipPayIndex", method = RequestMethod.GET)
    public String skipPayIndex(HttpServletRequest request, Model model) throws MalformedURLException, DocumentException, IOException, Exception{
    	long startTime = System.currentTimeMillis();
    	  String signature=request.getParameter("signature");
    	  String outTradeNo = request.getParameter("outTradeNo");
          String totalFee = request.getParameter("totalFee");
          String goodsName=request.getParameter("goodsName");
          String appId=request.getParameter("appId");
          String payZhifubao=request.getParameter("payZhifubao");
          String payWx=request.getParameter("payWx");
          String payTcl=request.getParameter("payTcl");
          String goodsDesc=request.getParameter("goodsDesc");
          String goodsId=request.getParameter("goodsId");
          String merchantId=request.getParameter("merchantId");
          String paymentType=request.getParameter("paymentType");
          String timestamp=request.getParameter("timestamp");
          String signatureNonce=request.getParameter("signatureNonce");
          String userId = request.getParameter("userId");
          String userName=request.getParameter("userName");
          String businessType=request.getParameter("businessType");
          String goodsTag="";
	      if (!nullEmptyBlankJudge(request.getParameter("goodsTag"))){
	      		goodsTag=new String(request.getParameter("goodsTag").getBytes("iso-8859-1"),"utf-8");
	      }
	      String showUrl=request.getParameter("showUrl");
	      String buyerRealName="";
	      if (!nullEmptyBlankJudge(request.getParameter("buyerRealName"))){
	    	  buyerRealName=new String(request.getParameter("buyerRealName").getBytes("iso-8859-1"),"utf-8");
	      }
	      
	      String buyerCertNo=request.getParameter("buyerCertNo");
	      String inputCharset=request.getParameter("inputCharset");
	      String paymentOutTime=request.getParameter("paymentOutTime");
	      String paymentChannel=request.getParameter("paymentChannel");
	      String feeType=request.getParameter("feeType");
	      String clientIp=request.getParameter("clientIp");
	      String parameter="";
    	  if (!nullEmptyBlankJudge(request.getParameter("parameter"))){
    		parameter=new String(request.getParameter("parameter").getBytes("iso-8859-1"),"utf-8");
    	  }
    	  String fullUri=payserviceDev.getServer_host()+"alipay/errorPayChannel";
    	  PayServiceLog payServiceLog=new PayServiceLog();
      	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
      	if(merchantInfo==null){
      		payServiceLog.setErrorCode("2");
      		payServiceLog.setStatus("error");
      		payServiceLog.setLogName(PayLogName.PAY_END);
      		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
          	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
          }
          payServiceLog.setLogName(PayLogName.PAY_END);
          SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
      		sParaTemp.put("appId",appId);
     		sParaTemp.put("timestamp", timestamp);
     		sParaTemp.put("signatureNonce", signatureNonce);
     		sParaTemp.put("outTradeNo",outTradeNo );
     		sParaTemp.put("userId", userId);
     		sParaTemp.put("goodsName", goodsName);
     		sParaTemp.put("totalFee", totalFee);
     		sParaTemp.put("merchantId", merchantId);
     		sParaTemp.put("businessType", merchantId);
     		sParaTemp.put("userName", userName);
     		sParaTemp.put("merchantId", merchantId);
     		sParaTemp.put("goodsId", goodsId);
     		sParaTemp.put("businessType", businessType);
     		sParaTemp.put("goodsDesc", goodsDesc);
     		sParaTemp.put("goodsTag", goodsTag);
     		sParaTemp.put("showUrl", showUrl);
     		sParaTemp.put("buyerRealName",buyerRealName);
     		sParaTemp.put("buyerCertNo",buyerCertNo);
     		sParaTemp.put("inputCharset", inputCharset);
     		sParaTemp.put("paymentOutTime", paymentOutTime);
     		sParaTemp.put("paymentType", paymentType);
     		sParaTemp.put("paymentChannel",paymentChannel);
     		sParaTemp.put("feeType", feeType);
     		sParaTemp.put("clientIp", clientIp);
     		sParaTemp.put("parameter", parameter);
     		String params=createSign(sParaTemp);
     	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
  		if(!hmacSHA1Verification){
  			payServiceLog.setErrorCode("3");
  			payServiceLog.setStatus("error");
  			payServiceLog.setLogName(PayLogName.PAY_END);
  			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
          	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
  		}
  		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
          model.addAttribute("totalFee",merchantOrderInfo.getOrderAmount());
          model.addAttribute("goodsName",merchantOrderInfo.getMerchantProductName());
          model.addAttribute("orderCreateTime",DateTools.dateToString(merchantOrderInfo.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
		  model.addAttribute("outTradeNo", merchantOrderInfo.getId());
		  model.addAttribute("merchantOrderId", merchantOrderInfo.getMerchantOrderId());
		  model.addAttribute("appId", appId);
		  model.addAttribute("payZhifubao", payZhifubao);
		  model.addAttribute("payWx", payWx);
		  model.addAttribute("payTcl", payTcl);
		  model.addAttribute("totalFeeValue", totalFee);
		  model.addAttribute("goodsDesc", goodsDesc);
		  model.addAttribute("goodsId", goodsId);
		  model.addAttribute("merchantId", merchantId);
		  model.addAttribute("paymentType", paymentType);
    	return "pay/payIndex";
    }

}
