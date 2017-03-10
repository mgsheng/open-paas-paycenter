package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlifafUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayController;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayPropetyFactory;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.BusinessType;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaySwitch;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.paymax.example.ChargeUtil;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.data.OrderQryData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.data.ScanCodeOrderData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.OrderQryService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.ScanCodeOrderService;
import cn.com.open.openpaas.payservice.app.channel.unionpay.sdk.AcpService;
import cn.com.open.openpaas.payservice.app.channel.unionpay.sdk.SDKConfig;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxPayCommonUtil;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayController;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayInfo;
import cn.com.open.openpaas.payservice.app.channel.yeepay.HmacUtils;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.DateUtils;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.payservice.app.tools.QRCodeEncoderHandler;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;

/**
 * tcl支付
 */
@Controller
@RequestMapping("/tcl/")
public class TclPayController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(TclPayController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	 
	 // 支付宝当面付2.0服务
	 private static AlipayTradeService tradeService;


	 /**
     * 请求统一支付
     * @return Json
     */
    @RequestMapping("unifyPay")
    public String unifyPay(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException, Exception {
    	
    	long startTime = System.currentTimeMillis();
    	
    	String outTradeNo=request.getParameter("outTradeNo");
    	String pay_switch = payserviceDev.getPay_switch();
    	//String banks_switch=payserviceDev.getBanks_switch();
    	Map<String, String> bank_map=payserviceDev.getBank_map();
    	String paySwitch []=pay_switch.split("#");
    	String payZhifubao = paySwitch[0];
    	String payWx=paySwitch[1];
    	String payTcl = paySwitch[2];
    	String payEbank = paySwitch[3];
    	String wechat_wap= paySwitch[4];
    	String fullUri=payserviceDev.getServer_host()+"tcl/errorPayChannel";
    	String userName=request.getParameter("userName");
        String userId = request.getParameter("userId");
        String merchantId = request.getParameter("merchantId");
        String appId = request.getParameter("appId");
    	String goodsId=request.getParameter("goodsId");
    	String goodsName=new String(request.getParameter("goodsName").getBytes("iso-8859-1"),"utf-8");
    	//String goodsName=request.getParameter("goodsName");
    	
    	String goodsDesc="";
    	if (!nullEmptyBlankJudge(request.getParameter("goodsDesc"))){
    		goodsDesc=new String(request.getParameter("goodsDesc").getBytes("iso-8859-1"),"utf-8");
    	}
    	
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
    	String paymentType=request.getParameter("paymentType");
    	String paymentChannel=request.getParameter("paymentChannel");
    	String totalFee=request.getParameter("totalFee");
    	String feeType=request.getParameter("feeType");
    	String clientIp=request.getParameter("clientIp");
    	String parameter="";
    	if (!nullEmptyBlankJudge(request.getParameter("parameter"))){
    		parameter=new String(request.getParameter("parameter").getBytes("iso-8859-1"),"utf-8");
    	}
        String signature=request.getParameter("signature");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String businessType=request.getParameter("businessType");
	    String notifyUrl=request.getParameter("notifyUrl");
	    String returnUrl=request.getParameter("returnUrl");
		String newId="";
		newId=SysUtil.careatePayOrderId();
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(newId);
	    payServiceLog.setAmount(totalFee);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId(paymentChannel);
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
	    if(!nullEmptyBlankJudge(paymentType)){
	    	 payServiceLog.setPaymentId(String.valueOf(getPaymentId(paymentType)));
	    }
	    payServiceLog.setProductDesc(goodsDesc);
	    payServiceLog.setProductName(goodsName);
	    payServiceLog.setRealAmount(totalFee);
	    payServiceLog.setSourceUid(userId);
	    payServiceLog.setUsername(userName);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.PAY_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,userId,merchantId,goodsName,totalFee))){
        	//paraMandaChkAndReturn(1, response,"必传参数中有空值");
        	if(!nullEmptyBlankJudge(paymentChannel)&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        	}
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.PAY_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
        //判断用户是否存在
        /*if(userId!=null && !("").equals(userId)){
        	//调用用户中心接口判断用户是否存在
        }*/
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	//paraMandaChkAndReturn(2, response,"商户ID不存在");
    		payServiceLog.setErrorCode("2");
    		payServiceLog.setStatus("error");
    		payServiceLog.setLogName(PayLogName.PAY_END);
    		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    		if(!nullEmptyBlankJudge(paymentChannel)&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        	}
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        }
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
   		sParaTemp.put("notifyUrl", notifyUrl);
   		sParaTemp.put("returnUrl", returnUrl);
   		sParaTemp.put("parameter", parameter);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
        //认证
       // Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
   	
		if(!hmacSHA1Verification){
			//paraMandaChkAndReturn(3, response,"认证失败");
			if(!nullEmptyBlankJudge(paymentChannel)&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
        	}
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.PAY_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		} 
    	
        if(!StringTool.isNumeric(totalFee)){
        	if(!nullEmptyBlankJudge(paymentChannel)&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
        	}
        	payServiceLog.setErrorCode("4");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.PAY_END);
        	//paraMandaChkAndReturn(3, response,"订单金额格式有误");
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
        }
      /*  if(!("CNY").equals(feeType)){
        	paraMandaChkAndReturn(3, response,"金额类型有误");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName,totalFee, map);
        	return "";
        }*/ 
       Boolean payType=validatePayType(paymentChannel,paymentType);
        if(!payType){
        	//paraMandaChkAndReturn(4, response,"所选支付渠道与支付类型不匹配");
        	if(!nullEmptyBlankJudge(paymentChannel)&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"5";
        	}
        	payServiceLog.setErrorCode("5");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.PAY_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"5";
        }
	
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		if(merchantOrderInfo!=null){
			if(!nullEmptyBlankJudge(paymentChannel)&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"10";
        	}
				payServiceLog.setErrorCode("10");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.PAY_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"10";
		}else{ 
			//创建订单
			merchantOrderInfo=new MerchantOrderInfo();
			merchantOrderInfo.setId(newId);
			merchantOrderInfo.setAppId(appId);
			merchantOrderInfo.setSourceUid(userId);
			merchantOrderInfo.setSourceUserName(userName);
			merchantOrderInfo.setMerchantId(SysUtil.toInteger(merchantId));
			merchantOrderInfo.setIp(clientIp);
			merchantOrderInfo.setMerchantOrderId(outTradeNo);//拼接_AppId用于防止不同应用订单号相同问题
			merchantOrderInfo.setMerchantProductName(goodsName);
			merchantOrderInfo.setStatus(0);//未处理
			merchantOrderInfo.setOrderAmount(Double.parseDouble(AmountUtil.changeF2Y(totalFee)));
			merchantOrderInfo.setMerchantVersion("0");
			merchantOrderInfo.setMerchantProductDesc(goodsDesc);//商品描述
			merchantOrderInfo.setMerchantProductId(goodsId);
			merchantOrderInfo.setParameter1(parameter);
			merchantOrderInfo.setNotifyUrl(notifyUrl);
			merchantOrderInfo.setReturnUrl(returnUrl);
			int paymentTypeId=PaymentType.getTypeByValue(paymentType).getType();
			merchantOrderInfo.setPaymentId(paymentTypeId);
			if(!nullEmptyBlankJudge(paymentChannel)){
				merchantOrderInfo.setChannelId(Integer.parseInt(paymentChannel));
			    if(String.valueOf(Channel.PAYMAX.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}
			}
			merchantOrderInfo.setBusinessType(Integer.parseInt(businessType));
			merchantOrderInfoService.saveMerchantOrderInfo(merchantOrderInfo);
		}
		  //用户账户创
			if(!nullEmptyBlankJudge(businessType)&&String.valueOf(BusinessType.COSTS.getValue()).equals(businessType)){
				UserAccountBalance  userAccountBalance=userAccountBalanceService.getBalanceInfo(userId, Integer.parseInt(appId));
				if(userAccountBalance==null){
					SortedMap<Object,Object> sParaTemp2 = new TreeMap<Object,Object>();
					timestamp=DateTools.getSolrDate(new Date());
		  		 	signatureNonce=StringTool.getRandom(100,1);
					sParaTemp2.put("app_id",appId);
					sParaTemp2.put("timestamp", timestamp);
					sParaTemp2.put("signatureNonce", signatureNonce);
					sParaTemp2.put("source_id", userId);
					String params2=createSign(sParaTemp2);
			   		signature=HMacSha1.HmacSHA1Encrypt(params2, merchantInfo.getPayKey());
			   		signature=HMacSha1.getNewResult(signature);
			   		sParaTemp2.put("signature", signature);
					String result=sendPost(payserviceDev.getUserCenter_getUserId_url(), sParaTemp2);
					 JSONObject obj = JSONObject.fromObject(result);
					 String status = obj.getString("status");
					 //用户中心返回的用户的唯一ID
					 String user_id="";
						if(status.equals("1")){
							user_id= obj.getString("user_id");
						}else{
							response.sendRedirect("errorPayChannel");
							return "";
						}
					userAccountBalance=new UserAccountBalance();
					userAccountBalance.setUserName(userName);
					userAccountBalance.setStatus(1);
					userAccountBalance.setType(2);
					userAccountBalance.setSourceId(userId);
					userAccountBalance.setUserId(user_id);
					userAccountBalance.setAppId(Integer.parseInt(appId));
					userAccountBalance.setCreateTime(new Date());
					userAccountBalanceService.saveUserAccountBalance(userAccountBalance);
				}
				
			}			
			 if(paymentChannel==null || ("").equals(paymentChannel)){
				 //跳转到收银台
//		        	  model.addAttribute("totalFee",merchantOrderInfo.getOrderAmount());
//		              model.addAttribute("goodsName",merchantOrderInfo.getMerchantProductName());
//		              model.addAttribute("orderCreateTime",DateTools.dateToString(merchantOrderInfo.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
//					  model.addAttribute("outTradeNo", merchantOrderInfo.getId());
//					  model.addAttribute("merchantOrderId", merchantOrderInfo.getMerchantOrderId());
//					  model.addAttribute("appId", appId);
//					  model.addAttribute("payZhifubao", payZhifubao);
//					  model.addAttribute("payWx", payWx);
//					  model.addAttribute("payTcl", payTcl);
//					  model.addAttribute("totalFeeValue", totalFee);
//					  model.addAttribute("goodsDesc", goodsDesc);
//					  model.addAttribute("goodsId", goodsId);
//					  model.addAttribute("merchantId", merchantId);
//					  model.addAttribute("paymentType", paymentType);
//					  model.addAttribute("signature", signature);
				 model.addAttribute("appId",appId);
				 model.addAttribute("timestamp", timestamp);
				 model.addAttribute("signatureNonce", signatureNonce);
				 model.addAttribute("outTradeNo",outTradeNo );
				 model.addAttribute("userId", userId);
				 model.addAttribute("goodsName", goodsName);
				 model.addAttribute("totalFee", totalFee);
				 model.addAttribute("merchantId", merchantId);
				 model.addAttribute("businessType", merchantId);
				 model.addAttribute("userName", userName);
				 model.addAttribute("merchantId", merchantId);
				 model.addAttribute("goodsId", goodsId);
				 model.addAttribute("businessType", businessType);
				 model.addAttribute("goodsDesc", goodsDesc);
				 model.addAttribute("goodsTag", goodsTag);
				 model.addAttribute("showUrl", showUrl);
				 model.addAttribute("buyerRealName",buyerRealName);
				 model.addAttribute("buyerCertNo",buyerCertNo);
				 model.addAttribute("inputCharset", inputCharset);
				 model.addAttribute("paymentOutTime", paymentOutTime);
				 model.addAttribute("paymentType", paymentType);
				 model.addAttribute("paymentChannel",paymentChannel);
				 model.addAttribute("feeType", feeType);
				 model.addAttribute("clientIp", clientIp);
				 model.addAttribute("parameter", parameter);
				 model.addAttribute("signature", signature);
				  payServiceLog.setLogName(PayLogName.PAY_END);
    		      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    		     return "redirect:" +payserviceDev.getPayIndex_url(); 
    		     //"http://localhost:8080/pay-service/alipay/skipPayIndex";
		        }else{
		        	
		      //payZhifubao     payWx	 payTcl
             log.info("-----------------------pay start-----------------------------------------");
              if(String.valueOf(Channel.PAYMAX.getValue()).equals(paymentChannel)){
		    	  DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.PAYMAX.getValue());
		    	  if(dictTradeChannels!=null){
		    		String other= dictTradeChannels.getOther();
				  	Map<String, String> others = new HashMap<String, String>();
				  	others=getPartner(other);
				  	 ChargeUtil ce = new ChargeUtil();
			      	  Map<String, Object> chargeMap=new HashMap<String, Object>();
			          chargeMap.put("amount", merchantOrderInfo.getOrderAmount());
			          chargeMap.put("subject", merchantOrderInfo.getMerchantProductName());
			          if(nullEmptyBlankJudge(merchantOrderInfo.getMerchantProductDesc())){
			        	  chargeMap.put("body", merchantOrderInfo.getMerchantProductName());  
			          }else{
			        	  chargeMap.put("body", merchantOrderInfo.getMerchantProductDesc());  
			          }
			          chargeMap.put("order_no", merchantOrderInfo.getId());
			          chargeMap.put("channel", others.get("channel"));
			          chargeMap.put("client_ip", others.get("client_ip"));
			          chargeMap.put("app", others.get("app"));
			          if(!nullEmptyBlankJudge(feeType)){
			        	  chargeMap.put("currency",feeType);  
			          }else{
			        	  chargeMap.put("currency",others.get("currency")); 
			          }
			          chargeMap.put("description",merchantOrderInfo.getMemo());
			  	    //请根据渠道要求确定是否需要传递extra字段
			          Map<String, Object> extra = new HashMap<String, Object>();
		    		 //拉卡拉网关支付
				     extra.put("user_id",merchantOrderInfo.getMerchantOrderId());
				     extra.put("return_url",dictTradeChannels.getBackurl());
		    	    chargeMap.put("extra",extra);
		    	    Map<String, Object> res= ce.charge(chargeMap);
			          JSONObject reqjson = JSONObject.fromObject(res);
			          boolean backValue=analysisValue(reqjson);
			          if(backValue){
			        	  String formValue="";
			        	  formValue=res.get("lakala_web").toString();
			        	  formValue+="<script>document.forms[0].submit();</script>";
				          model.addAttribute("res", formValue);
				    	  return "pay/payMaxRedirect";
			        		
			          }else{
			        	 payServiceLog.setErrorCode("8");
			 			 payServiceLog.setStatus("error");
			 			 String failureMsg=res.get("failureMsg").toString();
			 			 String failureCode=res.get("failureCode").toString();
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8"+"&failureCode="+failureCode+"&failureMsg="+failureMsg;  
			          } 
		    	  }else{
			    		 payServiceLog.setErrorCode("9");
			 			 payServiceLog.setStatus("error");
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8";
			    		 
			    	 }
			     }
		     //拉卡拉微信扫码支付
		      else if(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()).equals(paymentChannel)){
		    	  DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.PAYMAX_WECHAT_CSB.getValue());
		    	  if(dictTradeChannels!=null){
		    		String other= dictTradeChannels.getOther();
				  	Map<String, String> others = new HashMap<String, String>();
				  	others=getPartner(other);
				  	 ChargeUtil ce = new ChargeUtil();
			      	  Map<String, Object> chargeMap=new HashMap<String, Object>();
			          chargeMap.put("amount", merchantOrderInfo.getOrderAmount());
			          chargeMap.put("subject", merchantOrderInfo.getMerchantProductName());
			          if(nullEmptyBlankJudge(merchantOrderInfo.getMerchantProductDesc())){
			        	  chargeMap.put("body", merchantOrderInfo.getMerchantProductName());  
			          }else{
			        	  chargeMap.put("body", merchantOrderInfo.getMerchantProductDesc());  
			          }
			          chargeMap.put("order_no", merchantOrderInfo.getId());
			          chargeMap.put("channel", others.get("channel"));
			          chargeMap.put("client_ip", others.get("client_ip"));
			          chargeMap.put("app", others.get("app"));
			          if(!nullEmptyBlankJudge(feeType)){
			        	  chargeMap.put("currency",feeType);  
			          }else{
			        	  chargeMap.put("currency",others.get("currency")); 
			          }
			          chargeMap.put("description",merchantOrderInfo.getMemo());
			  	    //请根据渠道要求确定是否需要传递extra字段
			          Map<String, Object> extra = new HashMap<String, Object>();
		    		 //拉卡拉网关支付
				     extra.put("user_id",merchantOrderInfo.getMerchantOrderId());
				     extra.put("return_url",dictTradeChannels.getBackurl());
		    	    chargeMap.put("extra",extra);
		    	    Map<String, Object> res= ce.charge(chargeMap);
			          JSONObject reqjson = JSONObject.fromObject(res);
			          boolean backValue=analysisValue(reqjson);
			          if(backValue){
			        	  String formValue="";
			        	  formValue=res.get("wechat_csb").toString();
			        	  if(!nullEmptyBlankJudge(formValue)){
			        		  JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
				        	  String qr_code=lakalaWebJson.getString("qr_code");
				        	   fullUri=payserviceDev.getServer_host()+"tcl/wxpay?urlCode="+qr_code;
			                   payServiceLog.setLogName(PayLogName.PAY_END);
			        		   UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
			        		   return "redirect:" + fullUri;
			        	  }else{
			        		  payServiceLog.setErrorCode("9");
					 			 payServiceLog.setStatus("error");
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 	         return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8";
			        	  }
			          }else{
			        	 payServiceLog.setErrorCode("8");
			 			 payServiceLog.setStatus("error");
			 			 String failureMsg=res.get("failureMsg").toString();
			 			 String failureCode=res.get("failureCode").toString();
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8"+"&failureCode="+failureCode+"&failureMsg="+failureMsg;  
			          } 
		    	  }else{
			    		 payServiceLog.setErrorCode("9");
			 			 payServiceLog.setStatus("error");
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8";
			    		 
			    	 }
			     }
		}
		  
       	  return "redirect:" + fullUri;
    }
    
    public Boolean validatePayType(String paymentChannel,String paymentType){
    	Boolean returnValue=false;
    	if(nullEmptyBlankJudge(paymentChannel)&&nullEmptyBlankJudge(paymentType)){
    		 returnValue=true;	
    	}
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()))){
    		if(paymentType!=null&&PaymentType.PAYMAX_WECHAT_CSB.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
          }
       	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.PAYMAX.getValue()))){
    		 if(paymentType!=null&&PaymentType.PAYMAX.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
      	}
    	return returnValue;
    }
 
    /**
     * 跳转到错误页面
     */
    @RequestMapping(value = "errorPayChannel", method = RequestMethod.GET)
    public String errorPayChannel(HttpServletRequest request, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	String errorMsg="";
    	if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
    		errorMsg="必传参数中有空值";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
    		errorMsg="验证失败";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
    		errorMsg="所选支付渠道与支付类型不匹配";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
    		errorMsg="商户ID不存在";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
    		errorMsg="订单金额格式有误";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
    		errorMsg="订单处理失败，请重新提交！";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
    		errorMsg="订单已处理，请勿重复提交！";
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+failureMsg;
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+failureMsg;
    	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("10")){
    		errorMsg="您好：订单号重复,请检查后重新下单！";
    	}
    	model.addAttribute("outTradeNo", outTradeNo);
    	model.addAttribute("errorMsg", errorMsg);
    	return "pay/errorPayChannel";
    }	
    
    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "wxpay", method = RequestMethod.GET)
    public void wxpay(HttpServletRequest request,HttpServletResponse response, Model model){
    	String urlCode=request.getParameter("urlCode");
    	model.addAttribute("urlCode", urlCode);
    	 Map<String, Object> map=new HashMap<String,Object>();
    	
    	//encoderQRCoder(urlCode,response);
//    	  QRCodeEncoderHandler handler = new QRCodeEncoderHandler();   
//    	   handler.encoderQRCode(urlCode, response);
    	   map.put("status", "ok");
    	   map.put("urlCode", payserviceDev.getServer_host()+"tcl/getCode?urlCode="+urlCode);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }
    public Integer getPaymentId(String areaCode){
    	int returnValue=0;
    	if(PaymentType.CMB.getValue().equals(areaCode)){
    		returnValue=PaymentType.CMB.getType();
    	}
		else if(PaymentType.ICBC.getValue().equals(areaCode)){
			returnValue=PaymentType.ICBC.getType();
    	}
		else if(PaymentType.CCB.getValue().equals(areaCode)){
			returnValue=PaymentType.CCB.getType();
    	}
		else if(PaymentType.ABC.getValue().equals(areaCode)){
			returnValue=PaymentType.ABC.getType();
    	}
		else if(PaymentType.BOC.getValue().equals(areaCode)){
			returnValue=PaymentType.BOC.getType();
    	}
		else if(PaymentType.BCOM.getValue().equals(areaCode)){
			returnValue=PaymentType.BCOM.getType();
    	}
		else if(PaymentType.PSBC.getValue().equals(areaCode)){
			returnValue=PaymentType.PSBC.getType();
    	}
		else if(PaymentType.CGB.getValue().equals(areaCode)){
			returnValue=PaymentType.CGB.getType();
    	}
		else if(PaymentType.SPDB.getValue().equals(areaCode)){
			returnValue=PaymentType.SPDB.getType();
    	}
		else if(PaymentType.CEB.getValue().equals(areaCode)){
			returnValue=PaymentType.CEB.getType();
    	}
		else if(PaymentType.PAB.getValue().equals(areaCode)){
			returnValue=PaymentType.PAB.getType();
    	}else if(PaymentType.ALIPAY.getValue().equals(areaCode)){
			returnValue=PaymentType.ALIPAY.getType();
    	}else if(PaymentType.UPOP.getValue().equals(areaCode)){
			returnValue=PaymentType.UPOP.getType();
    	}
    	return returnValue;
    	
    }
    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(HttpServletRequest request,HttpServletResponse response, Model model){
    	String urlCode=request.getParameter("urlCode");
    	//model.addAttribute("urlCode", urlCode);
    	
    	//encoderQRCoder(urlCode,response);
    	  QRCodeEncoderHandler handler = new QRCodeEncoderHandler();   
    	   handler.encoderQRCode(urlCode, response);
      return ;    	   
    }	

  
    
   

}