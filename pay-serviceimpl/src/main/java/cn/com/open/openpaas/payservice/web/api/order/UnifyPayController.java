package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alipay.api.AlipayResponse;
import com.alipay.demo.trade.service.AlipayTradeService;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
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
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxPayCommonUtil;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayController;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayInfo;
import cn.com.open.openpaas.payservice.app.channel.yeepay.HmacUtils;
import cn.com.open.openpaas.payservice.app.kafka.KafkaProducer;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.payservice.app.tools.QRCodeEncoderHandler;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;
import net.sf.json.JSONObject;


/**
 * BANK_CARD-B2C(易慧金)
 */
@Controller
@RequestMapping("/alipay/")
public class UnifyPayController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UnifyPayController.class);
	
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
/*	 @Autowired
	 private KafkaProducer kafkaProducer;
*/

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
    	String fullUri=payserviceDev.getServer_host()+"alipay/errorPayChannel";
    	String userName=request.getParameter("userName");
        String userId = request.getParameter("userId");
        String merchantId = request.getParameter("merchantId");
        String appId = request.getParameter("appId");
    	String goodsId=request.getParameter("goodsId");
    	String goodsName=new String(request.getParameter("goodsName").getBytes("iso-8859-1"),"utf-8");
    	String phone=request.getParameter("phone");
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
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
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
        	if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
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
    		if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
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
   		sParaTemp.put("phone", phone);
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
			if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
        	}
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.PAY_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		} 
    	
        if(!StringTool.isNumeric(totalFee)){
        	if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
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
        	if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
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
		/*	//更新现有订单信息
					
			if(merchantOrderInfo.getPayStatus()==0){
				if(nullEmptyBlankJudge(merchantOrderInfo.getChannelOrderId()))
				{
					merchantOrderInfo.setChannelOrderId(merchantOrderInfo.getId());
				}
				merchantOrderInfo.setId(newId);
				merchantOrderInfo.setCreateDate(new Date());
				
				merchantOrderInfoService.updateOrderId(merchantOrderInfo);
 			}
		       else if(merchantOrderInfo.getPayStatus()==2){
				//订单处理中或者订单处理失败
				//paraMandaChkAndReturn(3, response,"认证失败");
				payServiceLog.setErrorCode("6");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.PAY_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"6";
 			}else{
 				//订单已经提交
 				//paraMandaChkAndReturn(3, response,"认证失败");
 				payServiceLog.setErrorCode("7");
 				payServiceLog.setStatus("error");
 				payServiceLog.setLogName(PayLogName.PAY_END);
 				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"7";
 			}
			payEbank=getBankSwitch(paymentType, bank_map);*/
				//订单处理中或者订单处理失败
				//paraMandaChkAndReturn(3, response,"认证失败");
				if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
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
			merchantOrderInfo.setPhone(phone);
			int paymentTypeId=PaymentType.getTypeByValue(paymentType).getType();
			merchantOrderInfo.setPaymentId(paymentTypeId);
			if(!nullEmptyBlankJudge(paymentChannel)){
				merchantOrderInfo.setChannelId(Integer.parseInt(paymentChannel));
				if(String.valueOf(Channel.ALI.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(Integer.parseInt(payZhifubao));	
				}
				else if(String.valueOf(Channel.WEIXIN.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(Integer.parseInt(payWx));	
				}
				else if(String.valueOf(Channel.UPOP.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(Integer.parseInt(payTcl));	
				}
				else if(String.valueOf(Channel.EBANK.getValue()).equals(paymentChannel)){
					payEbank=bank_map.get(paymentType);
					merchantOrderInfo.setSourceType(Integer.parseInt(payEbank));	
				}else if(String.valueOf(Channel.WECHAT_WAP.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(Integer.parseInt(wechat_wap));	
				}else if(String.valueOf(Channel.PAYMAX.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.EHK_BANK.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.EHKING.getValue());	
				}else if(String.valueOf(Channel.EHK_WEIXIN_PAY.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.EHKING.getValue());	
				}else if(String.valueOf(Channel.YEEPAY_EB.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.YEEPAY.getValue());	
				}else if(String.valueOf(Channel.ALIFAF.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.ALI.getValue());	
				}else if(String.valueOf(Channel.WECHAT_WAP.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.PAYMAX_H5.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}
			}
			merchantOrderInfo.setBusinessType(Integer.parseInt(businessType));
			merchantOrderInfoService.saveMerchantOrderInfo(merchantOrderInfo);
		}
		
		//sendSms(merchantOrderInfo,merchantInfo,kafkaProducer);
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
		     if(String.valueOf(Channel.ALI.getValue()).equals(paymentChannel)){
		    	 payServiceLog.setPaySwitch(payZhifubao);
		    	 if(String.valueOf(PaySwitch.ALI.getValue()).equals(payZhifubao)){
				    	//支付宝-即时到账支付
			        		if((PaymentType.ALIPAY.getValue()).equals(paymentType)){
			            		//调用支付宝即时支付方法  
			                	String url=AlipayController.getAliPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev); 
			                		//response.sendRedirect(url.replace("redirect:", ""));
			                	 payServiceLog.setLogName(PayLogName.PAY_END);
			        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			        		    
			                	return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;
			            	}
				     }
		    	 else if(String.valueOf(PaySwitch.TCL.getValue()).equals(payZhifubao)){
					    	//汇银通支付宝-即时到账支付
				    	 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
				    	  if((PaymentType.ALIPAY.getValue()).equals(paymentType)){
			         			ScanCodeOrderService scanCode = new ScanCodeOrderService();
			         			//String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels));
			         			/*String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
			         			return "redirect:" + URL;*/
			         			String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
			         			//response.sendRedirect(URL);
			         			 model.addAttribute("res", res);
			         			 payServiceLog.setLogName(PayLogName.PAY_END);
			        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
			         			return "pay/payRedirect";
			              }
				        } 
		     }else if(String.valueOf(Channel.ALIFAF.getValue()).equals(paymentChannel)){		    	 
	    		   payServiceLog.setPaySwitch(String.valueOf(merchantOrderInfo.getSourceType()));
	        		if((PaymentType.ALIFAF.getValue()).equals(paymentType)){
	            		//调用支付宝当面付方法  
	        			AlipayUtil alipayUtil=AlipayPropetyFactory.getAlifafUtil(merchantOrderInfo, dictTradeChannelService);
	        			String alifafCode=alipayUtil.trade_precreate(merchantOrderInfo, dictTradeChannelService);
	                	 fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+alifafCode+"&status=ok";
	                	 payServiceLog.setLogName(PayLogName.PAY_END);
	        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
	                	 return "redirect:" + fullUri;
	            	}
             }
		     else if(String.valueOf(Channel.WEIXIN.getValue()).equals(paymentChannel)){
		    	 payServiceLog.setPaySwitch(payWx);
		    	if(PaymentType.WEIXIN.getValue().equals(paymentType)){
		    		 if(String.valueOf(PaySwitch.WEIXIN.getValue()).equals(payWx)){
			    		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.WEIXIN.getValue());
				    		//微信-扫码支付
			        		if(String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)){
			                	WxpayInfo payInfo=new WxpayInfo();
			                	String other= dictTradeChannels.getOther();
			            		Map<String, String> others = new HashMap<String, String>();
			            		others=getPartner(other);
			                   	 payInfo.setAppid(others.get("wx_app_id"));
			                   	 payInfo.setMch_id(others.get("wx_mch_id"));
			                   	 payInfo.setNonce_str(WxPayCommonUtil.create_nonce_str());
			                   	 payInfo.setBody(goodsName);
			                   	 payInfo.setOut_trade_no(merchantOrderInfo.getId());
			                   	 payInfo.setProduct_id(goodsId);
			                   	 payInfo.setTotal_fee(Integer.parseInt(totalFee));
			                   	 payInfo.setSpbill_create_ip(others.get("wx_spbill_create_ip"));
			                   	 payInfo.setNotify_url(dictTradeChannels.getNotifyUrl());
			                   	 payInfo.setWx_key(others.get("wx_key"));
			                   	 payInfo.setTrade_type(others.get("wx_trade_type"));
			                   	 String urlCode= WxpayController.weixin_pay(payInfo, payserviceDev);
			                    //调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
			                	 fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode+"&status=ok";
			                	 payServiceLog.setLogName(PayLogName.PAY_END);
			        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
			                	 return "redirect:" + fullUri;
			        		}
				     	}  	
			    	 else if(String.valueOf(PaySwitch.TCL.getValue()).equals(payWx)){
				    	 if(String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)){
				    	 //TCL微信
				    	 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
		           		ScanCodeOrderService scanCode = new ScanCodeOrderService();
		          		String qr_code_url=scanCode.order(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","WXPAY","ScanCodePayment",dictTradeChannels));
		          		 fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+qr_code_url+"&status=ok";
		          		 payServiceLog.setLogName(PayLogName.PAY_END);
		    		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
		          		 return "redirect:" + fullUri;
				    	 }
				     	}else if(String.valueOf(PaySwitch.EHKING.getValue()).equals(payWx)){
				     		 //易汇金扫码支付
							Map <String,Object> map=new HashMap<String, Object>();
								map.put("name", merchantOrderInfo.getMerchantProductName());
								map.put("quantity", 1);
								String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
								map.put("amount",amount);
								String productDetails=JSONObject.fromObject(map).toString();
								model.addAttribute("productDetails","["+productDetails+"]");
								model.addAttribute("id", merchantOrderInfo.getId());
								model.addAttribute("clientIp", merchantOrderInfo.getIp());
								model.addAttribute("paymentType", "EHK_WEIXIN_PAY");
								model.addAttribute("merid", merchantOrderInfo.getMerchantId());
								model.addAttribute("payAmount", String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue()));
								return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
				     	}else if(String.valueOf(PaySwitch.PAYMAX.getValue()).equals(payWx)){
				     		//拉卡拉微信扫码
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
							     extra.put("user_id",merchantOrderInfo.getMerchantOrderId());
							     extra.put("return_url",dictTradeChannels.getBackurl());
					    	    chargeMap.put("extra",extra);
					    	    Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
						          JSONObject reqjson = JSONObject.fromObject(res);
						          boolean backValue=analysisValue(reqjson);
						          if(backValue){
						        	  String formValue="";
						        	  formValue=res.get("wechat_csb").toString();
						        	  if(!nullEmptyBlankJudge(formValue)){
						        		  JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
							        	  String qr_code=lakalaWebJson.getString("qr_code");
							        	   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+qr_code+"&status=ok";
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
		     }
		     else if(String.valueOf(Channel.WEIXIN_WECHAT_WAP.getValue()).equals(paymentChannel)){
		    	 //微信公众号支付
		    	 if(String.valueOf(PaySwitch.PAYMAX.getValue()).equals(wechat_wap)){
		    		 //拉卡拉维系内功中好支付
		    	  DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.WECHAT_WAP.getValue());
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
			          //channel:lakala_web#client_ip:127.0.0.1#app:app_xx2sq4oZ9054H43x#currency:CNY#private_key:MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJD048OK0qb84WJjkNco2fTqrlvJkiIhmh79Q3XNkJvVE23UXEK9SDYXngbTjhL3W3Bcm3h9r/gmRqJErulJkdvsJ4XKiDlffKIBVcueDQix1kRGXXvXhwI/cnpo+WDYzi5lU71uIdh8hi7eO7jQ5r7iMGlWCZJTfccTGDrAY2BvAgMBAAECgYBWul6HwUh15rlG78FaKjP1yG/XtQt85lPbFLbHBd3ujpbYUIV+3NcWnhzLgsnvaRXJbW8LsU+WjfgW4DcylvTwE55800MealsgCEUkDKprkMyiiOT4cnkYMjQ8WQFxm7WTD9Ao8/iBZPeBzHO6gGA/ScsTMy0LRnYBGWo5KpKYqQJBANOE4e4cJdQ0UD8lDv1BFJFVxZB83C8qR1yAhh5sATN/DpWlSzjbvEf16ZjseNb1fgvvsjPKnCDAHMmf9ocq5usCQQCvcJ7FLG3rO4Hi2kzbTfdbdkgiB6gxxRnsi9B+CiDJH/E4J01JeI5A5aYjdf+fGJdjgdIGMK/VkRvv8EL9rFONAkAskx1Vo4LpVFjw5ath/XwLIKswxs9T9THysXcSJCqgoo79REc05UGpXI5s1rCkhDma5FmGhpUeZb3rU5WNaKIfAkBgjBez5qlvBMaL8xrMrXFs8gDsSU50ZUXI+YB5fFVimaOEBYzw29ldOYRei3drNHtLlYvhQDXj0AGR36TeOVGZAkAEbWcNlVqymY2ND2EHbqdBKtLMOxeweEUtqqkT/+33+wpWNVMqvhcKgSCJMeHiIs0XLMnL/JvwTC9IM/MauqZs#secert_key:edd52aad608d4e7a82876f664eadf1f6#paymax_public_key:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ9OPDitKm/OFiY5DXKNn06q5byZIiIZoe/UN1zZCb1RNt1FxCvUg2F54G044S91twXJt4fa/4JkaiRK7pSZHb7CeFyog5X3yiAVXLng0IsdZERl1714cCP3J6aPlg2M4uZVO9biHYfIYu3ju40Oa+4jBpVgmSU33HExg6wGNgbwIDAQAB
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
		    	    	//拉卡拉微信公众号支付
		    	    	   String parameters[]=new String[10];
					          String openId="";
					          if(!nullEmptyBlankJudge(merchantOrderInfo.getParameter1())){
					        	  parameters= merchantOrderInfo.getParameter1().split(";");
					          }
					          for(int i=0;i<parameters.length;i++){
					        	  if(!nullEmptyBlankJudge(parameters[i])){
					        		  String []openIds=parameters[i].split("=");
					        		  if(openIds[0].equals("open_id")){
					        			  openId=openIds[1];
					        			  break;
					        		  }
					        	  }  
					        }
						  extra.put("open_id",openId);	
						  chargeMap.put("extra",extra);
				    	  Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
					          JSONObject reqjson = JSONObject.fromObject(res);
					          boolean backValue=analysisValue(reqjson);
					          backValue=true;
					          if(backValue){
					     	        String formValue="";
					        	  formValue=res.get("jsApiParams").toString();
//					          	 // formValue="{\'appId\':\'wxbdf94d9e022e2d07\',\'timeStamp\':\'1474960493\',\'signType\':\'MD5\',\'package\':\'prepay_id=wx201609271514528c217a70c40323801340\',\'nonceStr\':\'zOa9bo3ZlNyyXmAt\',\'paySign\':\'7AF10A5BC2D82B90D59B82BFA1DD406A\'}";
					          	    JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
					          	   /*    SortedMap<String,String> lakalasParaTemp = new TreeMap<String,String>();
					        	    lakalasParaTemp.put("appId", lakalaWebJson.getString("appId"));
					        	    lakalasParaTemp.put("timeStamp",lakalaWebJson.getString("timeStamp"));
					        	    lakalasParaTemp.put("nonceStr", lakalaWebJson.getString("nonceStr"));
					        	    lakalasParaTemp.put("package", lakalaWebJson.getString("package"));
					        	    lakalasParaTemp.put("signType",lakalaWebJson.getString("signType"));
					        	    lakalasParaTemp.put("paySign", lakalaWebJson.getString("paySign"));
								    String buf =SendPostMethod.buildRequest(lakalasParaTemp, "post", "ok", returnUrl);
								    model.addAttribute("res", buf);
						    	  return "pay/payMaxRedirect";*/
					        	  //测试微信公众号支付流程demo
					        	  model.addAttribute("appId", lakalaWebJson.getString("appId"));
					        	  model.addAttribute("timeStamp", lakalaWebJson.getString("timeStamp"));
					        	  model.addAttribute("nonceStr", lakalaWebJson.getString("nonceStr"));
					        	  model.addAttribute("wechatWapPackage", lakalaWebJson.getString("package"));
					        	  model.addAttribute("signType", lakalaWebJson.getString("signType"));
					        	  model.addAttribute("paySign", lakalaWebJson.getString("paySign"));
					        	  return "pay/wechat_wap";
					          }else{
					 			 payServiceLog.setStatus("error");
					 			 String failureMsg=res.get("failureMsg").toString();
					 			 String failureCode=res.get("failureCode").toString();
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 			return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8"+"&failureCode="+failureCode+"&failureMsg="+failureMsg;  
					      } 
		    	  }else{
			    		 payServiceLog.setErrorCode("9");
			 			 payServiceLog.setStatus("error");
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"9";
			    		 
			    	 }
		     }
		     }
		     else if(String.valueOf(Channel.UPOP.getValue()).equals(paymentChannel)){
		    	 payServiceLog.setPaySwitch(payTcl);
		    	 if(String.valueOf(PaySwitch.ALI.getValue()).equals(payTcl)){
			    	 
			     	}  	
		    	 else if(String.valueOf(PaySwitch.TCL.getValue()).equals(payTcl)){
			    	 //TCL微信
			    	if(String.valueOf(PaymentType.UPOP.getValue()).equals(paymentType)){ 
			    	DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
			   	 //银联支付
	             	ScanCodeOrderService scanCode = new ScanCodeOrderService();
	     		/*	String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","UPOP","GWDirectPay",dictTradeChannels));
	     			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
	     			
	     			//response.sendRedirect(URL);
	     			return "redirect:" + URL;*/
	            	String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","UPOP","GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
	       			model.addAttribute("res", res);
	       		 payServiceLog.setLogName(PayLogName.PAY_END);
			     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
	       			return "pay/payRedirect";
			    	}
			     }
		    	 else if(String.valueOf(PaySwitch.UNIONPAY.getValue()).equals(payTcl)){
			     //银联网关支付
		    		//前台页面传过来的 
		    		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.UPOP.getValue());
		    		 if(dictTradeChannels!=null){
		    				String other= dictTradeChannels.getOther();
						  	Map<String, String> others = new HashMap<String, String>();
						  	others=getPartner(other);
		    			 Map<String, String> requestData = new HashMap<String, String>();
			    			//***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***//*
			    			requestData.put("version", others.get("version"));   			  //版本号，全渠道默认值
			    			requestData.put("encoding", dictTradeChannels.getInputCharset()); 			  //字符集编码，可以使用UTF-8,GBK两种方式
			    			requestData.put("signMethod", others.get("signMethod"));            			  //签名方法，只支持 01：RSA方式证书加密
			    			requestData.put("txnType", others.get("txnType"));               			  //交易类型 ，01：消费
			    			requestData.put("txnSubType", others.get("txnSubType"));            			  //交易子类型， 01：自助消费
			    			requestData.put("bizType", others.get("bizType"));           			  //业务类型，B2C网关支付，手机wap支付
			    			requestData.put("channelType", others.get("channelType"));           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
			    			
			    			//***商户接入参数***//*
			    			requestData.put("merId", others.get("merId"));    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
			    			requestData.put("accessType", others.get("accessType"));             			  //接入类型，0：直连商户 
			    			requestData.put("orderId",merchantOrderInfo.getId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
			    			requestData.put("txnTime", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
			    			requestData.put("currencyCode", others.get("currencyCode"));         			  //交易币种（境内商户一般是156 人民币）		
			    			requestData.put("txnAmt", String.valueOf((int)(merchantOrderInfo.getOrderAmount()*100)));             			      //交易金额，单位分，不要带小数点
			    			//requestData.put("reqReserved", "透传字段");        		      //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
			    			
			    			//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
			    			//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
			    			//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
			    			requestData.put("frontUrl", dictTradeChannels.getBackurl());
			    			
			    			//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
			    			//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
			    			//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
			    			//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
			    			//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
			    			requestData.put("backUrl",dictTradeChannels.getNotifyUrl());
			    			
			    			//////////////////////////////////////////////////
			    			//
			    			//       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
			    			//
			    			//////////////////////////////////////////////////
			    			
			    			//**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**//*
			    			Map<String, String> submitFromData = AcpService.sign(requestData,"UTF-8");  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			    			
			    			String requestFrontUrl = PropertiesTool.getAppPropertieByKey("acpsdk.frontTransUrl");  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
			    			String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,"UTF-8");   //生成自动跳转的Html表单
			    			log.info("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
			    			String buf =SendPostMethod.buildRequest(submitFromData, "post", "ok", requestFrontUrl);
			    		    model.addAttribute("res", buf);
			    			 return "pay/payMaxRedirect";
			    			//resp.getWriter().write(html);
			    			
		    		 }else{
		    			 payServiceLog.setErrorCode("9");
			 			 payServiceLog.setStatus("error");
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8"; 
		    		 }
			     }
		    	 
		     }else if(String.valueOf(Channel.EBANK.getValue()).equals(paymentChannel)){
		    	 
		    	  payServiceLog.setPaySwitch(payEbank);
		    	 // payEbank=getBankSwitch(paymentType, bank_map);
		    	  
		    	 if(!nullEmptyBlankJudge(payEbank)&&String.valueOf(PaySwitch.ALI.getValue()).equals(payEbank)){
				    	// 支付宝-网银支付
				    	 if(!String.valueOf(PaymentType.UPOP.getValue()).equals(paymentType)&&!String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)&&!String.valueOf(PaymentType.ALIPAY.getValue()).equals(paymentType)){ 
		         		String defaultbank=getDefaultbank(paymentType);
		         		String url=AlipayController.getEBankPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev,defaultbank); 
		         		 payServiceLog.setLogName(PayLogName.PAY_END);
		    		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
		         		return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;
				    	 }
				     	}  	
		    	 else if(!nullEmptyBlankJudge(payEbank)&&String.valueOf(PaySwitch.TCL.getValue()).equals(payEbank)){
				  	   //TCL直连银行
				    	 if(!String.valueOf(PaymentType.UPOP.getValue()).equals(paymentType)&&!String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)&&!String.valueOf(PaymentType.ALIPAY.getValue()).equals(paymentType)){ 
				    		 
				    	if(PaymentType.SPDB.getValue().equals(paymentType)){
				    		String defaultbank=getDefaultbank(paymentType);
			         		String url=AlipayController.getEBankPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev,defaultbank); 
			         		 payServiceLog.setLogName(PayLogName.PAY_END);
			    		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
			         		return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;	
			         		
				    	}else{
				    		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
			             	 ScanCodeOrderService scanCode = new ScanCodeOrderService();
			       			/*String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",paymentType,"GWDirectPay",dictTradeChannels));
			       			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;*/
			       			String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",paymentType,"GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
			       			model.addAttribute("res", res);
			       		 payServiceLog.setLogName(PayLogName.PAY_END);
					     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
		         			return "pay/payRedirect";
			       			//return "redirect:" + URL;
				    	  }	 
				      } 
				  }else if(!nullEmptyBlankJudge(payEbank)&&String.valueOf(PaySwitch.YEEPAY.getValue()).equals(payEbank)){
					  	   //易宝支付
					    	 if(!String.valueOf(PaymentType.UPOP.getValue()).equals(paymentType)&&!String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)&&!String.valueOf(PaymentType.ALIPAY.getValue()).equals(paymentType)){ 
					    		totalFee=AmountUtil.changeF2Y(totalFee);
					    		 String res = getYeePayUrl(totalFee,
										merchantOrderInfo,paymentType);
					    		 model.addAttribute("res", res);
					    		 payServiceLog.setLogName(PayLogName.PAY_END);
							     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
				         		 return "pay/payRedirect";
					        }
				      }
				  else if(!nullEmptyBlankJudge(payEbank)&&String.valueOf(PaySwitch.PAYMAX.getValue()).equals(payEbank)){
					  //拉卡拉直连银行
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
				          String newpaymentType=getPayMaxbank(paymentType);
				  	    //请根据渠道要求确定是否需要传递extra字段
				          Map<String, Object> extra = new HashMap<String, Object>();
					          extra.put("user_id",merchantOrderInfo.getId());
					          extra.put("return_url",dictTradeChannels.getBackurl());
					          if(!nullEmptyBlankJudge(newpaymentType)){
					        	  extra.put("bank_code",newpaymentType);  
					          }
			    	      chargeMap.put("extra",extra);
			    	    Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
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
				     
				  }  else if(!nullEmptyBlankJudge(payEbank)&&String.valueOf(PaySwitch.EHKING.getValue()).equals(payEbank)){
					  //易汇金直连银行
					  	Map <String,Object> map=new HashMap<String, Object>();
					  	map.put("name", merchantOrderInfo.getMerchantProductName());
					  	map.put("quantity", 1);
					  	String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
					  	map.put("amount",amount);
					  	String productDetails=JSONObject.fromObject(map).toString();
					  	model.addAttribute("productDetails","["+productDetails+"]");
					  	model.addAttribute("id", merchantOrderInfo.getId());
					  	String newpaymentType=getEhkbankCode(paymentType);
					  	model.addAttribute("paymentType", newpaymentType);
					  	model.addAttribute("clientIp", merchantOrderInfo.getIp());
					  	model.addAttribute("merid", merchantOrderInfo.getMerchantId());
					  	model.addAttribute("payAmount", String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue()));
					  	return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
				     
				  }
		    	 //channel:wechat_csb#client_ip:127.0.0.1#app:app_52a8zBD2Erp56D8s#currency:CNY#private_key:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJLNUhm0TOy8BwKlb+G4fWZbLOr8mXOVcgQMuUR/sT+V/992LwckvnCuaimeoxuiwpdaSelFPrRLkGGzdQeGxMd0foaNmLa/EZ+ZELyxxeYapS+3RmUAhB2UKsQO33s9R4LpJH1ljcZwae19vTIJfQRQ72kBafCUvBm3q4YM2cc9AgMBAAECgYEAkku5WNJcosNS/SkfUUPI/Fs6bUekKRKymCSR8RiL7EEwyGH/xc+xVZwLQkTMaXsPD0Q0ShruvUBct3De3MxKhrREb5hYG1xSxUaxUI/z1IFThaWld5zKz/KFJnII2dewoWDRYCQF95pjDDPR+ui59Z57D+JZdYV5cW7p0fx8UEUCQQDDXGzTiVFxExTP1xoldl4TqEx4BO3J0PQBD+6JTP4NHyACIrpchXGZpeOCfSBhGstilkcwupRbElv+CSIaaOUPAkEAwF5Z0ZxkheCGzkN48HksKxiYoIDSo8ufVUVj9ZOiUT//lkUMNvOU+cgqBIoM84po6BLvwUZ5KysJdNar/3YG8wJAITUguoRo95OKwhmKNDv+mdDNzsjnspp2H4gZv/T6ajiUNEi67Ocx/DAakB+81US8tbFdwIa2mRRx1qiux1Z1OQJABvEgsqS/J+mjU7wxmBP3WRLJJzme4FRPyqb3ZXxPZjk2Avk46J6/qIflpEZLE1rSUFWmm0Xsx3cFH1dD27MpqwJAQEbcYgFlmYK5iDemWxncZG6zbo74COGLfAdUbUSEUaCKU/XD+7LxL8quxj4iKyODh/m9AYrOFUfyDMutk8LZBQ==#secert_key:ca8e2aca50dd41ab8f27e7b07ecbb498#paymax_public_key:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDE0gdZi/icODd0WAQlxfw8FgoJ/BwCgMLn+Fh1DquRXqYkgGtv35B5j+CCSpag3wxNwtrHRHpar3dpo38+KIqaMy8bhQj1hmBJmHkgM1yZZpVC5S4uc4FYT21zQn0HBI1E1gg2nAA7JkFV1+VnvFKk9rzOA2UtrYXID+ZG4BMBwwIDAQAB

		      }else if(String.valueOf(Channel.PAYMAX.getValue()).equals(paymentChannel)){
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
				     
				     //http://10.96.15.44:8080/payNotify/back
				     //http://10.96.15.44:8080/payRetrun/back
				     extra.put("return_url",dictTradeChannels.getBackurl());
				     //extra.put("show_url","http://www.abc.cn/charge");
		    	    chargeMap.put("extra",extra);
		    	    Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
			          JSONObject reqjson = JSONObject.fromObject(res);
			          boolean backValue=analysisValue(reqjson);
			          if(backValue){
			        	  String formValue="";
			        	  formValue=res.get("lakala_web").toString();
			        	  //formValue=res.get("wechat_csb").toString();
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
		      else if(String.valueOf(Channel.PAYMAX_H5.getValue()).equals(paymentChannel)){
		    	  DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.PAYMAX_H5.getValue());
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
				     extra.put("show_url",dictTradeChannels.getBackurl());
		    	    chargeMap.put("extra",extra);
		    	    Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
			          JSONObject reqjson = JSONObject.fromObject(res);
			          boolean backValue=analysisValue(reqjson);
			          if(backValue){
			        	  String formValue="";
			        	 // formValue=res.get("lakala_web").toString();
			        	  formValue=res.get("wechat_csb").toString();
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
		    	    Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
			          JSONObject reqjson = JSONObject.fromObject(res);
			          boolean backValue=analysisValue(reqjson);
			          if(backValue){
			        	  String formValue="";
			        	  formValue=res.get("wechat_csb").toString();
			        	  if(!nullEmptyBlankJudge(formValue)){
			        		  JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
				        	  String qr_code=lakalaWebJson.getString("qr_code");
				        	   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+qr_code+"&status=ok";
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
		      else if(String.valueOf(Channel.WECHAT_WAP.getValue()).equals(paymentChannel)){
		    	  DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.WECHAT_WAP.getValue());
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
		    	    	//拉卡拉微信公众号支付
		    	    	   String parameters[]=new String[10];
					          String openId="";
					          if(!nullEmptyBlankJudge(merchantOrderInfo.getParameter1())){
					        	  parameters= merchantOrderInfo.getParameter1().split(";");
					          }
					          for(int i=0;i<parameters.length;i++){
					        	  if(!nullEmptyBlankJudge(parameters[i])){
					        		  String []openIds=parameters[i].split("=");
					        		  if(openIds[0].equals("open_id")){
					        			  openId=openIds[1];
					        			  break;
					        		  }
					        	  }  
					        }
						  extra.put("open_id",openId);	
						  chargeMap.put("extra",extra);
				    	  Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
					          JSONObject reqjson = JSONObject.fromObject(res);
					          boolean backValue=analysisValue(reqjson);
					          if(backValue){
					     	        String formValue="";
					        	  formValue=res.get("jsApiParams").toString();
//					          	 // formValue="{\'appId\':\'wxbdf94d9e022e2d07\',\'timeStamp\':\'1 474960493\',\'signType\':\'MD5\',\'package\':\'prepay_id=wx201609271514528c217a70c40323801340\',\'nonceStr\':\'zOa9bo3ZlNyyXmAt\',\'paySign\':\'7AF10A5BC2D82B90D59B82BFA1DD406A\'}";
					          	    JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
					          	/*  SortedMap<String,String> lakalasParaTemp = new TreeMap<String,String>();
					        	    lakalasParaTemp.put("appId", lakalaWebJson.getString("appId"));
					        	    lakalasParaTemp.put("timeStamp",lakalaWebJson.getString("timeStamp"));
					        	    lakalasParaTemp.put("nonceStr", lakalaWebJson.getString("nonceStr"));
					        	    lakalasParaTemp.put("package", lakalaWebJson.getString("package"));
					        	    lakalasParaTemp.put("signType",lakalaWebJson.getString("signType"));
					        	    lakalasParaTemp.put("paySign", lakalaWebJson.getString("paySign"));
								    String buf =SendPostMethod.buildRequest(lakalasParaTemp, "post", "ok", dictTradeChannels.getBackurl());
								    model.addAttribute("res", buf);
						    	    return "pay/payMaxRedirect";*/
						          	  model.addAttribute("appId", lakalaWebJson.getString("appId"));
						        	  model.addAttribute("timeStamp", lakalaWebJson.getString("timeStamp"));
						        	  model.addAttribute("nonceStr", lakalaWebJson.getString("nonceStr"));
						        	  model.addAttribute("wxpackage", lakalaWebJson.getString("package"));
						        	  model.addAttribute("signType", lakalaWebJson.getString("signType"));
						        	  model.addAttribute("paySign", lakalaWebJson.getString("paySign"));
						        	  model.addAttribute("orderId", merchantOrderInfo.getId());
						    		 String wechatWapUri=payserviceDev.getServer_host()+"alipay/wechatWap";
				                	 payServiceLog.setLogName(PayLogName.PAY_END);
				        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
				                	 return "redirect:" + wechatWapUri;
					        	  //测试微信公众号支付流程demo
						    	  /*    model.addAttribute("appId", lakalaWebJson.getString("appId"));
					        	  model.addAttribute("timeStamp", lakalaWebJson.getString("timeStamp"));
					        	  model.addAttribute("nonceStr", lakalaWebJson.getString("nonceStr"));
					        	  model.addAttribute("wxpackage", lakalaWebJson.getString("package"));
					        	  model.addAttribute("signType", lakalaWebJson.getString("signType"));
					        	  model.addAttribute("paySign", lakalaWebJson.getString("paySign"));
					        	  model.addAttribute("orderId", merchantOrderInfo.getId());
					        	  return "pay/wechat_wap";*/
					          }else{
					        	 payServiceLog.setErrorCode("8");
					 			 payServiceLog.setStatus("error");
					 			 String failureMsg=res.get("failureMsg").toString();
					 			 String failureCode=res.get("failureCode").toString();
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8"+"&failureCode="+failureCode+"&failureMsg="+failureMsg;  
		    	      }
		    	  }else{
			    		 payServiceLog.setErrorCode("9");
			 			 payServiceLog.setStatus("error");
			 			 payServiceLog.setLogName(PayLogName.PAY_END);
			 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 	         return "redirect:"+payserviceDev.getServer_host()+"?outTradeNo="+outTradeNo+"&errorCode="+"9";
			    		 
			    	 }
			     }
		      else if(String.valueOf(Channel.EHK_WEIXIN_PAY.getValue()).equals(paymentChannel)){
			    	 //易汇金扫码支付
		    	 // merchantId:120140804#orderCurrency:CNY#paymentModeCode:BANK_CARD-B2C#clientIp:127.0.0.1#timeout:10
			    	 if(PaymentType.EHK_WEIXIN_PAY.getValue().equals(paymentType)){
						  	Map <String,Object> map=new HashMap<String, Object>();
						  	map.put("name", merchantOrderInfo.getMerchantProductName());
						  	map.put("quantity", 1);
						  	String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
						  	map.put("amount",amount);
						  	String productDetails=JSONObject.fromObject(map).toString();
						  	model.addAttribute("productDetails","["+productDetails+"]");
						  	model.addAttribute("id", merchantOrderInfo.getId());
						  	model.addAttribute("clientIp", merchantOrderInfo.getIp());
						  	model.addAttribute("paymentType", "EHK_WEIXIN_PAY");
						  	model.addAttribute("merid", merchantOrderInfo.getMerchantId());
						  	model.addAttribute("payAmount", String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue()));
						  	return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
			    	 }
			     }
		      else if(String.valueOf(Channel.EHK_BANK.getValue()).equals(paymentChannel)){
			    	 if(PaymentType.EHK_BANK.getValue().equals(paymentType)){
			    		 //易汇金收银台
						  	Map <String,Object> map=new HashMap<String, Object>();
						  	map.put("name", merchantOrderInfo.getMerchantProductName());
						  	map.put("quantity", 1);
						  	String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
						  	map.put("amount",amount);
						  	String productDetails=JSONObject.fromObject(map).toString();
						  	model.addAttribute("productDetails","["+productDetails+"]");
						  	model.addAttribute("id", merchantOrderInfo.getId());
						  	model.addAttribute("paymentType", "EHK_BANK");
						  	model.addAttribute("clientIp", merchantOrderInfo.getIp());						 
						  	model.addAttribute("merid", merchantOrderInfo.getMerchantId());
						  	model.addAttribute("payAmount", String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue()));
						  	return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
			    	 }
			     }
		      else if(String.valueOf(Channel.YEEPAY_EB.getValue()).equals(paymentChannel)){
			    	 if(PaymentType.YEEPAY_GW.getValue().equals(paymentType)){
						  //易宝直连银行
					    		totalFee=AmountUtil.changeF2Y(totalFee);
					    		 String res = getYeePayUrl(totalFee,
										merchantOrderInfo,paymentType);
					    		 model.addAttribute("res", res);
					    		 payServiceLog.setLogName(PayLogName.PAY_END);
							     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
				         		 return "pay/payRedirect";
			    	 }
			  }
		}
		  
       	  return "redirect:" + fullUri;
    }
    
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
          String orderCreateTime=request.getParameter("orderCreateTime");
          String merchantOrderId=request.getParameter("merchantOrderId");
          String appId=request.getParameter("appId");
          String payZhifubao=request.getParameter("payZhifubao");
          String payWx=request.getParameter("payWx");
          String payTcl=request.getParameter("payTcl");
          String totalFeeValue=request.getParameter("totalFeeValue");
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
          //认证
         // Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
     	
  		if(!hmacSHA1Verification){
  			//paraMandaChkAndReturn(3, response,"认证失败");
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
    /**
     * 获取易宝支付地址
     * @param totalFee
     * @param merchantOrderInfo
     * @return
     */
	public String getYeePayUrl(String totalFee,
			MerchantOrderInfo merchantOrderInfo,String paymentType) {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.YEEPAY_EB.getValue());
		 String other= dictTradeChannels.getOther();
		 Map<String, String> others = new HashMap<String, String>();
		 others=getPartner(other);
		 String p0_Cmd=others.get("p0_Cmd");
		 String p1_MerId=others.get("p1_MerId");
		 String p2_Order=merchantOrderInfo.getId();
		 String p3_Amt=totalFee;
		 String p4_Cur=others.get("p4_Cur");
		 String p5_Pid=merchantOrderInfo.getMerchantProductName();
		 String p6_Pcat="";
		 String p7_Pdesc="";
		 if(!nullEmptyBlankJudge(merchantOrderInfo.getMerchantProductDesc())){
			 p7_Pdesc=merchantOrderInfo.getMerchantProductDesc();
		 }
		 String p8_Url=dictTradeChannels.getNotifyUrl();
		 String p9_SAF=others.get("p9_SAF");
		 String pa_MP="";
		// String pd_FrpId=getYeePayFrpId(paymentType);
		 String pd_FrpId="";
		 if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.YEEPAY_GW.getValue())){
			 pd_FrpId="";
		 }else{
			 pd_FrpId=getYeePayFrpId(paymentType);
		 }
		
		 String pr_NeedResponse=others.get("pr_NeedResponse");
		 String keyValue=others.get("keyValue");
		 String hmac=HmacUtils.getReqMd5HmacForOnlinePayment(others.get("p0_Cmd"), p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		 Map<String, String> dataMap=new HashMap<String, String>();
		 dataMap.put("p0_Cmd", p0_Cmd);
		 dataMap.put("p1_MerId", p1_MerId);
		 dataMap.put("p2_Order", p2_Order);
		 dataMap.put("p3_Amt", p3_Amt);
		 dataMap.put("p4_Cur", p4_Cur);
		 dataMap.put("p5_Pid", p5_Pid);
		 dataMap.put("p6_Pcat", p6_Pcat);
		 dataMap.put("p7_Pdesc", p7_Pdesc);
		 dataMap.put("p8_Url", p8_Url);
		 dataMap.put("p9_SAF", p9_SAF);
		 dataMap.put("pa_MP", pa_MP);
		 dataMap.put("pd_FrpId", pd_FrpId);
		 dataMap.put("pr_NeedResponse", pr_NeedResponse);
		 dataMap.put("hmac", hmac);
		 String res =SendPostMethod.buildRequest(dataMap, "post", "ok",payserviceDev.getYeepayCommonReqURL());
		return res;
	}	
    
	public String getYeePayFrpId(String paymentType){
		 String returnValue="";
		 if(!nullEmptyBlankJudge(paymentType)){
			 if(PaymentType.CMB.getValue().equals(paymentType)){
				   returnValue="CMBCHINA-NET-B2C";
	   		 }else if(PaymentType.BOC.getValue().equals(paymentType)){
				   returnValue="BOC-NET-B2C";
	 		 }else if(PaymentType.ICBC.getValue().equals(paymentType)){
				   returnValue="ICBC-NET-B2C";
	 		 }else if(PaymentType.CCB.getValue().equals(paymentType)){
				   returnValue="CCB-NET-B2C";
	 		 }else if(PaymentType.ABC.getValue().equals(paymentType)){
				   returnValue="ABC-NET-B2C";
	 		 }else if(PaymentType.BCOM.getValue().equals(paymentType)){
				   returnValue="BOCO-NET-B2C";
	 		 }else if(PaymentType.PSBC.getValue().equals(paymentType)){
				   returnValue="POST-NET-B2C";
	 		 }else if(PaymentType.CGB.getValue().equals(paymentType)){
				   returnValue="GDB-NET-B2C";
	 		 }else if(PaymentType.SPDB.getValue().equals(paymentType)){
				   returnValue="SPDB-NET-B2C";
	 		 }else if(PaymentType.CEB.getValue().equals(paymentType)){
				   returnValue="CEB-NET-B2C";
	 		 }else if(PaymentType.PAB.getValue().equals(paymentType)){
				   returnValue="PINGANBANK-NET";
			 }else if(PaymentType.BOB.getValue().equals(paymentType)){
				   returnValue="BCCB-NET-B2C";
			 } 
		 }
		 return returnValue;
	}
	/**
	 * 根据银行编码和银行集合判断选择那种方式进行支付
	 * 0：支付宝；1：tcl;2：易宝支付
	 * @param bankName
	 * @param bankMap
	 * @return
	 */
	private String getBankSwitch(String bankName,Map<String, String> bankMap){
		String returnValue="";
		for (String key : bankMap.keySet()) {  
			if(bankName.equals(key)){
				returnValue=bankMap.get(key);
				break;
			}  
		}
		return returnValue;
		
	}
	/**
	 * 获取银行map银行
	 * @param banks_switch
	 * @return 
	 */
	public Map<String,String>  getBankMap(String banks_switch) {
		Map<String, String> bank_map=null;
		if(!nullEmptyBlankJudge(banks_switch)){
			bank_map=new HashMap<String, String>();
			String banks[]=banks_switch.split(",");
			for(int i=0;i<banks.length;i++){
			String bank[]=banks[i].split(":");
			for(int j=0;j<bank.length;j++){
				bank_map.put(bank[0], bank[1]);
			 }
		  }
		}
	
		return  bank_map;
	//	this.bank_map = bank_map;
	}
   /**
    * 返回银行代码
    * @param paymentType
    * @return
    */
   private String getDefaultbank(String paymentType){
	   String returnValue="";
	   if(!nullEmptyBlankJudge(paymentType)){
		 if(PaymentType.CMB.getValue().equals(paymentType)){
			   returnValue="CMB";
   		 }else if(PaymentType.BOC.getValue().equals(paymentType)){
			   returnValue="BOCB2C";
 		 }else if(PaymentType.ICBC.getValue().equals(paymentType)){
			   returnValue="ICBCB2C";
 		 }else if(PaymentType.CCB.getValue().equals(paymentType)){
			   returnValue="CCB";
 		 }else if(PaymentType.ABC.getValue().equals(paymentType)){
			   returnValue="ABC";
 		 }else if(PaymentType.BCOM.getValue().equals(paymentType)){
			   returnValue="COMM-DEBIT";
 		 }else if(PaymentType.PSBC.getValue().equals(paymentType)){
			   returnValue="POSTGC";
 		 }else if(PaymentType.CGB.getValue().equals(paymentType)){
			   returnValue="GDB";
 		 }else if(PaymentType.SPDB.getValue().equals(paymentType)){
			   returnValue="SPDB";
 		 }else if(PaymentType.CEB.getValue().equals(paymentType)){
			   returnValue="CEB-DEBIT";
 		 }else if(PaymentType.PAB.getValue().equals(paymentType)){
			   returnValue="SPABANK";
		 }else if(PaymentType.BOB.getValue().equals(paymentType)){
			   returnValue="BJBANK";
		 }
	   }else{
		   returnValue="";  
	   }
	   return returnValue;
   }
   /**
    * 返回拉卡拉银行代码
    * @param paymentType
    * @return
    */
   private String getPayMaxbank(String paymentType){
	   String returnValue="";
	   if(!nullEmptyBlankJudge(paymentType)){
		if(PaymentType.CGB.getValue().equals(paymentType)){
			   returnValue="GDB";
 		 }else if(PaymentType.PAB.getValue().equals(paymentType)){
			   returnValue="PABC";
		 }else{
			 returnValue=paymentType;
		 }
	   }else{
		   returnValue="";  
	   }
	   return returnValue;
   }
   
   /**
    * 返回易汇金银行编码
    * @param paymentType
    * @return
    */
   private String getEhkbankCode(String paymentType){
	   String returnValue="";
	   if(!nullEmptyBlankJudge(paymentType)){
		if(PaymentType.PSBC.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-POST-P2P";
 		 }else if(PaymentType.CMBC.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-CMBC-P2P";
		 }else if(PaymentType.BOB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-BCCB-P2P";
		 }
		 else if(PaymentType.CMB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-CMBCHINA-P2P";
		 }
		 else if(PaymentType.SPDB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-SPDB-P2P";
		 }
		 else if(PaymentType.CIB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-CIB-P2P";
		 }
		 else if(PaymentType.ABC.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-ABC-P2P";
		 }
		 else if(PaymentType.CGB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-GDB-P2P";
		 }else if(PaymentType.ICBC.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-ICBC-P2P";
		 }
		 else if(PaymentType.BOC.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-BOC-P2P";
		 }
		 else if(PaymentType.BCOM.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-BOCO-P2P";
		 }
		 else if(PaymentType.CCB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-CCB-P2P";
		 } else if(PaymentType.PAB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-PINGANBANK-P2P";
		 } else if(PaymentType.CEB.getValue().equals(paymentType)){
			   returnValue="BANK_CARD-B2C-CEB-P2P";
		 }else{
			 returnValue=paymentType;
		 }
	   }else{
		   returnValue="";  
	   }
	   return returnValue;
   }
    public Boolean validatePayType(String paymentChannel,String paymentType){
    	Boolean returnValue=false;
    	if(nullEmptyBlankJudge(paymentChannel)&&nullEmptyBlankJudge(paymentType)){
    		 returnValue=true;	
    	}
    	if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.ALI.getValue()))){
    	 if(paymentType!=null&&(PaymentType.ALIPAY.getValue()).equals(paymentType)){
    		 returnValue=true;
    	 }else{
    		 returnValue=false; 
    	 }
    	}
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.WEIXIN.getValue()))){
    	 if(paymentType!=null&&String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)){
    		 returnValue=true;
    	 } else{
    		 returnValue=false; 
    	 }
    	}
    	
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.EBANK.getValue()))){
    	if(ifTruePayMentType(paymentType)){
       		 returnValue=true;
       	 }else{
       		 returnValue=false; 
       	 }
    	}
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.WECHAT_WAP.getValue()))){
    		if(paymentType!=null&&PaymentType.WECHAT_WAP.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
          }
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()))){
    		if(paymentType!=null&&PaymentType.PAYMAX_WECHAT_CSB.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
          }
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.PAYMAX_H5.getValue()))){
    		if(paymentType!=null&&PaymentType.PAYMAX_H5.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
          }
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.WEIXIN_WECHAT_WAP.getValue()))){
    		if(paymentType!=null&&PaymentType.WECHAT_WAP.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
          }
    	else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.UPOP.getValue()))){
    		if(paymentType!=null&&PaymentType.UPOP.getValue().equals(paymentType)){
          		 returnValue=true;
          	 }else{
          		 returnValue=false; 
          	 }
       	}else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.EHK_WEIXIN_PAY.getValue()))){
    		if(paymentType!=null&&PaymentType.EHK_WEIXIN_PAY.getValue().equals(paymentType)){
         		 returnValue=true;
         	 }else{
         		 returnValue=false; 
         	 }
      	}else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.EHK_BANK.getValue()))){
    		if((paymentType!=null&&PaymentType.EHK_BANK.getValue().equals(paymentType))){
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
      	}else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.YEEPAY_EB.getValue()))){
      		if(paymentType!=null&&PaymentType.YEEPAY_GW.getValue().equals(paymentType)){
        		 returnValue=true; 
        	 }else{
     		 returnValue=false; 
     	 }
  	  }else if(paymentChannel!=null&&paymentChannel.equals(String.valueOf(Channel.ALIFAF.getValue()))){
  		if(paymentType!=null&&PaymentType.ALIFAF.getValue().equals(paymentType)){
   		 returnValue=true; 
   	  }else{
	 	 returnValue=false; 
	   }
  	  }
    	return returnValue;
    }
    /**
     * 判断直连银行的选择支付方式是否为直连银行
     * @param paymentType
     * @return
     */
    public Boolean ifTruePayMentType(String paymentType){
    	boolean returnValue=false;
    	if(paymentType!=null&&!String.valueOf(PaymentType.WEIXIN.getValue()).equals(paymentType)){
      		 returnValue=true;
      	 }else if(!String.valueOf(PaymentType.UPOP.getValue()).equals(paymentType)){
      		returnValue=true;
      	 }else if(!String.valueOf(PaymentType.ALIPAY.getValue()).equals(paymentType)){
       		returnValue=true;
       	 }else if(!String.valueOf(PaymentType.PAYMAX.getValue()).equals(paymentType)){
       		returnValue=true;
       	 }else if(!String.valueOf(PaymentType.PAYMAX_H5.getValue()).equals(paymentType)){
       		returnValue=true;
       	 }else if(!String.valueOf(PaymentType.WECHAT_WAP.getValue()).equals(paymentType)){
        		returnValue=true;
         }else{
      		 returnValue=false;  
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
    @RequestMapping(value = "wxpay/errorPayChannel", method = RequestMethod.GET)
    public void wxErrorPayChannel(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	String urlCode=request.getParameter("urlCode");
    	model.addAttribute("urlCode", urlCode);
    	 Map<String, Object> map=new HashMap<String,Object>();
    	
    	//encoderQRCoder(urlCode,response);
//    	  QRCodeEncoderHandler handler = new QRCodeEncoderHandler();   
//    	   handler.encoderQRCode(urlCode, response);
    	 
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
    	   map.put("status", "error");
    	   map.put("outTradeNo", outTradeNo);
    	   map.put("errorMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }

    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "tclwxpay", method = RequestMethod.GET)
    public void tclwxpay(HttpServletRequest request,HttpServletResponse response, Model model){
    	String urlCode=request.getParameter("urlCode");
    	//model.addAttribute("urlCode", urlCode);
    	// Map<String, Object> map=new HashMap<String,Object>();
    	
    	//encoderQRCoder(urlCode,response);
//    	  QRCodeEncoderHandler handler = new QRCodeEncoderHandler();   
//    	   handler.encoderQRCode(urlCode, response);
    	   /*map.put("status", "ok");
    	   map.put("urlCode", payserviceDev.getServer_host()+"alipay/getCode?urlCode="+urlCode);
    	   writeSuccessJson(response,map);*/
    	  WebUtils.writeJson(response, urlCode);
    	  return;
    	   
    }	
    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "wxpay", method = RequestMethod.GET)
    public void wxpay(HttpServletRequest request,HttpServletResponse response, Model model){
    	 
    	 String status=request.getParameter("status");
    	 Map<String, Object> map=new HashMap<String,Object>();
    	  if(!nullEmptyBlankJudge(status)&&status.equals("ok")){
    	   String urlCode=request.getParameter("urlCode");
    	   map.put("status", "ok");
       	   map.put("urlCode", payserviceDev.getServer_host()+"alipay/getCode?urlCode="+urlCode);  
    	  }else{
    	   String errorCode=request.getParameter("errorCode");
    	   String errorMsg=request.getParameter("errorMsg");
    	   map.put("status", "error");
    	   map.put("errorCode", errorCode);
    	   map.put("errorMsg",errorMsg);
    	  }
    	  writeSuccessJson(response,map);
    }
    /**
     * 跳转到微信公众号页面
     */
    @RequestMapping(value = "wechatWap", method = RequestMethod.GET)
    public void wechatWap(HttpServletRequest request,HttpServletResponse response, Model model){
    	 Map<String, Object> map=new HashMap<String,Object>();
    	   map.put("status", "ok");
    	   map.put("appId", request.getParameter("appId"));
    	   map.put("timeStamp", request.getParameter("timeStamp"));
    	   map.put("nonceStr",request.getParameter("nonceStr"));
    	   map.put("package", request.getParameter("wxpackage"));
    	   map.put("signType", request.getParameter("signType"));
    	   map.put("paySign", request.getParameter("paySign"));
    	   writeSuccessJson(response,map);
    	   
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
    public Boolean getErrorType(String paymentType){
    	Boolean errorType = false;
    	if(PaymentType.WECHAT_WAP.getValue().equals(paymentType)){
    		errorType=true;
    	}if(PaymentType.WEIXIN.getValue().equals(paymentType)){
    		errorType=true;
    	}if(PaymentType.ALIFAF.getValue().equals(paymentType)){
    		errorType=true;
    	}if(PaymentType.PAYMAX_WECHAT_CSB.getValue().equals(paymentType)){
    		errorType=true;
    	}if(PaymentType.EHK_WEIXIN_PAY.getValue().equals(paymentType)){
    		errorType=true;
    	}
    	return errorType;
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
    /**
     * 跳转选择支付渠道页面
     */
    @RequestMapping(value = "selectPayChannel", method = RequestMethod.GET)
    public String payChannel(HttpServletRequest request, Model model){
    	  String outTradeNo = request.getParameter("outTradeNo");
          String appId = request.getParameter("appId");
          MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
    	//获取商户下的渠道信息
          if(merchantOrderInfo!=null){
        	 model.addAttribute("outTradeNo", outTradeNo);
        	  model.addAttribute("totalFee",merchantOrderInfo.getOrderAmount());
              model.addAttribute("goodsName",merchantOrderInfo.getMerchantProductName());
              model.addAttribute("orderCreateTime",DateTools.dateToString(merchantOrderInfo.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
              model.addAttribute("appId",appId);
          }
//          return "redirect:" +payserviceDev.getPayIndex_url();
          return "pay/payIndex";
    }	
    /**
     * 跳转选择支付渠道页面
     */
    @RequestMapping(value = "selectPayChannel")
    public void selectPayChannel(HttpServletRequest request,HttpServletResponse response, Model model){
        String outTradeNo = request.getParameter("outTradeNo");
        String totalFee = request.getParameter("totalFee");
        String goodsName=request.getParameter("goodsName");
    	//MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	//获取商户下的渠道信息
        try {
			response.sendRedirect("payIndex?outTradeNo="+outTradeNo+"&totalFee="+totalFee+"goodsName="+goodsName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
    /**
     * 跳转选择支付渠道页面
     */
    @RequestMapping(value = "payIndex", method = RequestMethod.GET)
    public String selectPayChannel(HttpServletRequest request, Model model){
    	  String outTradeNo = request.getParameter("outTradeNo");
          String totalFee = request.getParameter("totalFee");
          String goodsName=request.getParameter("goodsName");
    	  model.addAttribute("outTradeNo", outTradeNo);
          model.addAttribute("totalFee",totalFee);
          model.addAttribute("goodsName",goodsName);
    	return "pay/payIndex";
    }	
    
    /**selectChannelPay
     * 提交支付渠道更新相应订单
     * @throws Exception 
     */
    @RequestMapping(value = "selectChannelPay", method = RequestMethod.POST)
    public String payChannel(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
    	long startTime = System.currentTimeMillis();
    	String goodsName=request.getParameter("goodsName");
    	String totalFee=request.getParameter("totalFee");
    	String goodsDesc=request.getParameter("goodsDesc");
    	String goodsId=request.getParameter("goodsId");
    	String merchantId=request.getParameter("merchantId");
//    	String paymentType=request.getParameter("paymentType");
    	String pay_switch = payserviceDev.getPay_switch();
    	String paySwitch []=pay_switch.split("#");
    	String payZhifubao = paySwitch[0];
    	String payWx=paySwitch[1];
    	String payTcl = paySwitch[2];
    	String payEbank = paySwitch[3];
    	
    	
    	String areaCode=request.getParameter("areaCode");
    	
    	String outTradeNo=request.getParameter("outTradeNo");
    	String merchantOrderId=request.getParameter("merchantOrderId");
    	String appId=request.getParameter("appId");
    	
    	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(merchantOrderId, appId);
    	int paymentId=0;
    	int channelId=0;
    	if(areaCode.equals("1")){
    		paymentId = PaymentType.ALIPAY.getType();
    		channelId = Channel.ALI.getValue();
    		merchantOrderInfo.setSourceType(Integer.parseInt(payZhifubao));	
    	}else if(areaCode.equals("2")){
    		paymentId = PaymentType.UPOP.getType();
    		channelId = Channel.UPOP.getValue();
    		merchantOrderInfo.setSourceType(Integer.parseInt(payTcl));	
    	}else if(areaCode.equals("3")){
    		paymentId = PaymentType.WEIXIN.getType();
    		channelId = Channel.WEIXIN.getValue();
    		merchantOrderInfo.setSourceType(Integer.parseInt(payWx));
    	}else{
//    		paymentId = Integer.parseInt(areaCode);
    		paymentId = getPaymentId(areaCode);
    		channelId = Channel.EBANK.getValue();
    		merchantOrderInfo.setSourceType(Integer.parseInt(payEbank));
    	}
    	//添加支付方式与支付渠道
    	merchantOrderInfo.setChannelId(channelId);
    	merchantOrderInfo.setPaymentId(paymentId);
    	merchantOrderInfoService.updatePayWay(merchantOrderInfo);
    	//日志添加
    	PayServiceLog payServiceLog=new PayServiceLog();
  	    payServiceLog.setAmount(totalFee);
  	    payServiceLog.setAppId(appId);
  	    if(merchantOrderInfo.getChannelId()!=null){
  	      payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));	
  	    }
  	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
  	    payServiceLog.setLogType(payserviceDev.getLog_type());
  	    payServiceLog.setMerchantId(merchantId);
  	    payServiceLog.setMerchantOrderId(outTradeNo);
  	    payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
  	    payServiceLog.setProductDesc(goodsDesc);
  	    payServiceLog.setProductName(goodsName);
  	    payServiceLog.setLogName(PayLogName.PAY_START);
  	    payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
  	    payServiceLog.setUsername(merchantOrderInfo.getUserName());
  	    payServiceLog.setStatus("ok");
        log.info("-------------------------支付开始--------------------------");
        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
    	if(merchantOrderInfo!=null){
    		
    		
    		if(merchantOrderInfo.getPayStatus()!=0){
				//订单处理中或者订单处理失败
				//paraMandaChkAndReturn(3, response,"认证失败");
				payServiceLog.setErrorCode("6");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.PAY_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + payserviceDev.getServer_host()+"alipay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"6";
 			}else{
 				
         	//支付渠道为支付宝
             if(!nullEmptyBlankJudge(areaCode)&&"1".equals(areaCode)){
            	 if(!nullEmptyBlankJudge(payZhifubao)&&"1".equals(payZhifubao)){
            		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
            		 merchantOrderInfoService.updateSourceType(Integer.parseInt(payZhifubao), merchantOrderInfo.getId());
    		    	 payServiceLog.setPaySwitch(payZhifubao);
            		ScanCodeOrderService scanCode = new ScanCodeOrderService();
         			/*String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels));
         			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
         			response.setCharacterEncoding("GBK");
         			response.sendRedirect(URL);*/
            		String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
         			//response.sendRedirect(URL);
         			model.addAttribute("res", res);
         			  payServiceLog.setLogName(PayLogName.PAY_END);
                      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         			return "pay/payRedirect";
            	 }else{
            		//调用支付宝即时支付方法  
            		 String url=AlipayController.getAliPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev); 
            		//response.sendRedirect(payserviceDev.getAli_pay_url()+"?"+url);
            		  payServiceLog.setLogName(PayLogName.PAY_END);
                      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
            		 return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;
            	 }
             }
             else if(!nullEmptyBlankJudge(areaCode)&&"3".equals(areaCode)){
            	 if(!nullEmptyBlankJudge(payWx)&&"1".equals(payWx)){
            		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
            		 merchantOrderInfoService.updateSourceType(Integer.parseInt(payWx), merchantOrderInfo.getId());
//            		 updatePayWay
            		 payServiceLog.setPaySwitch(payWx);
            		ScanCodeOrderService scanCode = new ScanCodeOrderService();
//             		String qr_code_url=scanCode.order(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","WXPAY","ScanCodePayment",dictTradeChannels));
//             		response.sendRedirect("tclwxpay?urlCode="+qr_code_url);  
//             		// response.getWriter().print(qr_code_url);
//             		//调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
             		String qr_code_url=scanCode.order(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","WXPAY","ScanCodePayment",dictTradeChannels));
             		String fullUri="/alipay/wxpay?urlCode="+qr_code_url+"&status=ok";
             		  payServiceLog.setLogName(PayLogName.PAY_END);
                      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	             	return "redirect:" + fullUri;
             		
            	 }else{
            		 DictTradeChannel dictTradeChannel=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.WEIXIN.getValue());
            			String other= dictTradeChannel.getOther();
	            		Map<String, String> others = new HashMap<String, String>();
	            		others=getPartner(other);
            		 WxpayInfo payInfo=new WxpayInfo();
                   	 payInfo.setAppid(others.get("wx_app_id"));
                   	 //payInfo.setDevice_info("WEB");
                   	 payInfo.setMch_id(others.get("wx_mch_id"));
                   	 payInfo.setNonce_str(WxPayCommonUtil.create_nonce_str());
                   	 payInfo.setBody(goodsName);
                   	 //payInfo.setAttach("某某分店");
                   	 payInfo.setOut_trade_no(merchantOrderInfo.getId());
                   	 payInfo.setProduct_id(goodsId);
                   	 payInfo.setTotal_fee(Integer.parseInt(totalFee));
                   	 payInfo.setSpbill_create_ip(others.get("wx_spbill_create_ip"));
                   	 payInfo.setNotify_url(dictTradeChannel.getNotifyUrl());
                   	 payInfo.setTrade_type(others.get("wx_trade_type"));
                   	 payInfo.setWx_key(dictTradeChannel.getKeyValue());
                   	 String urlCode= WxpayController.weixin_pay(payInfo, payserviceDev);
                    //调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
                	 //response.sendRedirect("wxpay?urlCode="+urlCode);  
                	  String fullUri="/alipay/wxpay?urlCode="+urlCode+"&status=ok";
                	  payServiceLog.setLogName(PayLogName.PAY_END);
                      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
               	     return "redirect:" + fullUri;
            	 }
             		
            } else if(!nullEmptyBlankJudge(areaCode)&&"2".equals(areaCode)){
            	if(!nullEmptyBlankJudge(payTcl)&&"1".equals(payTcl)){
            		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
            		 merchantOrderInfoService.updateSourceType(Integer.parseInt(payTcl), merchantOrderInfo.getId());
    		    	 payServiceLog.setPaySwitch(payTcl);
            		ScanCodeOrderService scanCode = new ScanCodeOrderService();
        		/*	String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","UPOP","GWDirectPay",dictTradeChannels));
        			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
        			response.setCharacterEncoding("UTF-8");
        			response.sendRedirect(URL);*/
            		String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","UPOP","GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
	       			model.addAttribute("res", res);
	       		  payServiceLog.setLogName(PayLogName.PAY_END);
	              UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         			return "pay/payRedirect";
            	}else{
            		//银联支付给自己带开发
            	}
              }else{
            	  if(!nullEmptyBlankJudge(payEbank)&&"1".equals(payEbank)){
            		  DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
            		  merchantOrderInfoService.updateSourceType(Integer.parseInt(payEbank), merchantOrderInfo.getId());
     		    	 payServiceLog.setPaySwitch(payEbank);
            		  String newareaCode=getAreaCode(areaCode);
                	  ScanCodeOrderService scanCode = new ScanCodeOrderService();
          			/*  String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",newareaCode,"GWDirectPay",dictTradeChannels));
          			  String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
          			  response.setCharacterEncoding("UTF-8");
          			  response.sendRedirect(URL);*/
                		String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",newareaCode,"GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
		       			model.addAttribute("res", res);
		       		  payServiceLog.setLogName(PayLogName.PAY_END);
		              UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	         			return "pay/payRedirect";
            	  }else if(!nullEmptyBlankJudge(payEbank)&&"2".equals(payEbank)){
				  	   //易宝支付
				    		totalFee=AmountUtil.changeF2Y(totalFee);
				    		 String res = getYeePayUrl(totalFee,
									merchantOrderInfo,areaCode);
				    		 model.addAttribute("res", res);
				    		  payServiceLog.setLogName(PayLogName.PAY_END);
				              UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			         		 return "pay/payRedirect";
			      }else{
            		// 支付宝-网银支付
     		    	 if(!String.valueOf(PaymentType.UPOP.getValue()).equals(areaCode)&&!String.valueOf(PaymentType.WEIXIN.getValue()).equals(areaCode)&&!String.valueOf(PaymentType.ALIPAY.getValue()).equals(areaCode)){ 
     		    		 String defaultbank=getDefaultbank(areaCode);
     		    		 String url=AlipayController.getEBankPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev,defaultbank); 
//     		    		 String fullUri=payserviceDev.getAli_pay_url()+"?"+url;
//     		    		 response.sendRedirect(fullUri.replace("redirect:", ""));
     		    		  payServiceLog.setLogName(PayLogName.PAY_END);
     		             UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
     		    		return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;
     		    	 }
            	  }
            	  
              }
 			}
           
    	}
		return "";
    }
    
    
    /**selectAccomplish
     * 查询支付结果获取状态
     * @throws Exception 
     */
    @RequestMapping(value = "selectAccomplish", method = RequestMethod.POST)
    public void payAccomplish(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	try{
    		String outTradeNo=request.getParameter("merchantOrderId");
        	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo);
        	int payStatus = merchantOrderInfo.getPayStatus();
        	String backMsg="";
        	if(payStatus==1){
        		backMsg="success";
        	}else{
        		backMsg="error";
        	}
        	WebUtils.writeJson(response, backMsg);
    	}catch (Exception e) {
    		String backMsg="error";
    		WebUtils.writeJson(response, backMsg);
		}
    	
    }


    /**selectAccomplish
     * 查询支付结果获取状态
     * @throws Exception 
     */
    @RequestMapping(value = "getOrderQuery")
    public void getOrderQuery(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
    	String outTradeNo=request.getParameter("outTradeNo");
        String appId = request.getParameter("appId");
        String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String signature=request.getParameter("signature");
	    
      //获取当前订单
  		MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
  		if(orderInfo==null){
         	paraMandaChkAndReturn(1, response,"订单号不存在");
         	return ;
         } 
  		MerchantInfo merchantInfo=merchantInfoService.findById(orderInfo.getMerchantId());
  		if(merchantInfo==null){
         	paraMandaChkAndReturn(2, response,"认证失败");
         	return ;
         } 
  		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo );
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
        //认证
       // Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
		if(!hmacSHA1Verification){
			paraMandaChkAndReturn(2, response,"认证失败");
			return;
		} 
    	DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(orderInfo.getMerchantId()),Channel.TCL.getValue());
        String notify_url =dictTradeChannels.getNotifyUrl();
        OrderQryService orderQry = new OrderQryService();
        orderQry.query(OrderQryData.buildGetOrderQryDataMap(orderInfo, dictTradeChannels));
//    			String URL="https://ipos.tclpay.cn/hipos/payTrans?"+returnCode;
//    			response.setCharacterEncoding("GBK");
//    			response.sendRedirect(URL);
    	
    }
    

    public String getAreaCode(String areaCode){
    	String newAreaCode="";
    	if(!nullEmptyBlankJudge(areaCode)){
    		if(PaymentType.CMB.getValue().equals(areaCode)){
        		newAreaCode="CMB";
        	}
    		else if(PaymentType.ICBC.getValue().equals(areaCode)){
        		newAreaCode="ICBC";
        	}
    		else if(PaymentType.CCB.getValue().equals(areaCode)){
        		newAreaCode="CCB";
        	}
    		else if(PaymentType.ABC.getValue().equals(areaCode)){
        		newAreaCode="ABC";
        	}
    		else if(PaymentType.BOC.getValue().equals(areaCode)){
        		newAreaCode="BOC";
        	}
    		else if(PaymentType.BCOM.getValue().equals(areaCode)){
        		newAreaCode="BCOM";
        	}
    		else if(PaymentType.PSBC.getValue().equals(areaCode)){
        		newAreaCode="PSBC";
        	}
    		else if(PaymentType.CGB.getValue().equals(areaCode)){
        		newAreaCode="CGB";
        	}
    		else if(PaymentType.SPDB.getValue().equals(areaCode)){
        		newAreaCode="SPDB";
        	}
    		else if(PaymentType.CEB.getValue().equals(areaCode)){
        		newAreaCode="CEB";
        	}
    		else if(PaymentType.PAB.getValue().equals(areaCode)){
        		newAreaCode="PAB";
        	}
    	}
    	else{
    		newAreaCode="";  
  	   }
  	   return newAreaCode;
    }
    /**
     * 易宝支付
     * @param areaCode
     * @return
     */
    public String getYeePayCode(String paymentType){
    	String returnValue="";
    	if(!nullEmptyBlankJudge(paymentType)){
    		 if(PaymentType.CMB.getValue().equals(paymentType)){
				   returnValue="CMBCHINA-NET-B2C";
	   		 }else if(PaymentType.BOC.getValue().equals(paymentType)){
				   returnValue="BOC-NET-B2C";
	 		 }else if(PaymentType.ICBC.getValue().equals(paymentType)){
				   returnValue="ICBC-NET-B2C";
	 		 }else if(PaymentType.CCB.getValue().equals(paymentType)){
				   returnValue="CCB-NET-B2C";
	 		 }else if(PaymentType.ABC.getValue().equals(paymentType)){
				   returnValue="ABC-NET-B2C";
	 		 }else if(PaymentType.BCOM.getValue().equals(paymentType)){
				   returnValue="BOCO-NET-B2C";
	 		 }else if(PaymentType.PSBC.getValue().equals(paymentType)){
				   returnValue="POST-NET-B2C";
	 		 }else if(PaymentType.CGB.getValue().equals(paymentType)){
				   returnValue="GDB-NET-B2C";
	 		 }else if(PaymentType.SPDB.getValue().equals(paymentType)){
				   returnValue="SPDB-NET-B2C";
	 		 }else if(PaymentType.CEB.getValue().equals(paymentType)){
				   returnValue="CEB-NET-B2C";
	 		 }else if(PaymentType.PAB.getValue().equals(paymentType)){
				   returnValue="PINGANBANK-NET";
			 } 
    	}
  	   return returnValue;
    }
   
    
    public void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(), response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

}