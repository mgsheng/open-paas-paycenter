package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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
import cn.com.open.openpaas.payservice.app.channel.alipay.PayError;
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
import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.PaymobileUtils;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
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
    	Map<String, String> bank_map=payserviceDev.getBank_map();
    	String paySwitch []=pay_switch.split("#");
    	String payZhifubao = paySwitch[0];
    	String payWx=paySwitch[1];
    	String payTcl = paySwitch[2];
    	String payEbank = paySwitch[3];
    	String wechat_wap= paySwitch[4];
    	String alifaf= paySwitch[5];
    	String fullUri=payserviceDev.getServer_host()+"alipay/errorPayChannel";
    	String userName=request.getParameter("userName");
        String userId = request.getParameter("userId");
        String merchantId = request.getParameter("merchantId");
        String appId = request.getParameter("appId");
    	String goodsId=request.getParameter("goodsId");
    	String goodsName=new String(request.getParameter("goodsName").getBytes("iso-8859-1"),"utf-8");
    	String phone=request.getParameter("phone");
    	if (!nullEmptyBlankJudge(request.getParameter("userName"))){
    		userName=new String(request.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
    	}
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
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,userId,merchantId,goodsName,totalFee,paymentChannel))){
        	if(!nullEmptyBlankJudge(paymentChannel)&&getErrorType(paymentType)){
        		return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        	}
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.PAY_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
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
   	
		if(!hmacSHA1Verification){
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
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
        }
        //验证用户支付类型和支付渠道是否匹配
       Boolean payType=validatePayType(paymentChannel,paymentType);
        if(!payType){
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
			merchantOrderInfo.setBuyerRealName(buyerRealName);
			merchantOrderInfo.setBuyerCertNo(buyerCertNo);
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
					merchantOrderInfo.setSourceType(Integer.parseInt(alifaf));	
				}else if(String.valueOf(Channel.WECHAT_WAP.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.PAYMAX_WECHAT_CSB.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.PAYMAX_H5.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.PAYMAX.getValue());	
				}else if(String.valueOf(Channel.YEEPAY_WEIXIN.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.YEEPAY.getValue());	
				}else if(String.valueOf(Channel.YEEPAY_ALI.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.YEEPAY.getValue());	
				}else if(String.valueOf(Channel.YEEPAY_ALL.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.YEEPAY.getValue());	
				}else if(String.valueOf(Channel.EHK_ALI_PAY.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.EHKING.getValue());	
				}else if(String.valueOf(Channel.EHK_INSTALLMENT_LOAN.getValue()).equals(paymentChannel)){
					merchantOrderInfo.setSourceType(PaySwitch.EHKING.getValue());	
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
		      //payZhifubao     payWx	 payTcl
             log.info("-----------------------pay start-----------------------------------------");
		     if(String.valueOf(Channel.ALI.getValue()).equals(paymentChannel)){
		    	 payServiceLog.setPaySwitch(payZhifubao);
		    	 if(String.valueOf(PaySwitch.ALI.getValue()).equals(payZhifubao)){
				    	//支付宝-即时到账支付
			        		if((PaymentType.ALIPAY.getValue()).equals(paymentType)){
			            		//调用支付宝即时支付方法  
			                	String url=AlipayController.getAliPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev); 
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
			         			String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
			         			 model.addAttribute("res", res);
			         			 payServiceLog.setLogName(PayLogName.PAY_END);
			        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
			         			return "pay/payRedirect";
			              }
				        }
		    	 
		     }else if(String.valueOf(Channel.ALIFAF.getValue()).equals(paymentChannel)){		    	 
	    		   payServiceLog.setPaySwitch(alifaf);
	        		if(String.valueOf(PaySwitch.ALI.getValue()).equals(alifaf)){
	            		//调用支付宝当面付方法  
	        			AlipayUtil alipayUtil=AlipayPropetyFactory.getAlifafUtil(merchantOrderInfo, dictTradeChannelService);
	        			String alifafCode=alipayUtil.trade_precreate(merchantOrderInfo, dictTradeChannelService);
	                	 fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+alifafCode+"&status=ok";
	                	 payServiceLog.setLogName(PayLogName.PAY_END);
	        		     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
	                	 return "redirect:" + fullUri;
	            	}else if(String.valueOf(PaySwitch.YEEPAY.getValue()).equals(alifaf)){
	            		  //易宝支付宝扫码
			    		 Map <String,Object> map=getYeePayValue(merchantOrderInfo, Channel.YEEPAY_ALI.getValue());
			    			String status=map.get("status").toString();
			    			if(!nullEmptyBlankJudge(status)&&status.equals("ok")){
			    				   String urlCode=map.get("urlCode").toString();
			    				   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode+"&status=ok";
				                   payServiceLog.setLogName(PayLogName.PAY_END);
				        		   UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
				        		   return "redirect:" + fullUri;
			    			}else{
			    				 payServiceLog.setErrorCode("11");
					 			 payServiceLog.setStatus("error");
					 			 model.addAttribute("errorMsg", URLEncoder.encode(map.get("error_msg").toString(), "utf-8"));
					 			  model.addAttribute("failureCode", map.get("error_code").toString());
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8";  
			    			} 
	            	}else if(String.valueOf(PaySwitch.EHKING.getValue()).equals(alifaf)){
				    	 //易汇金支付宝扫码支付
			    	 // merchantId:120140804#orderCurrency:CNY#paymentModeCode:BANK_CARD-B2C#clientIp:127.0.0.1#timeout:10
							  	Map <String,Object> map=new HashMap<String, Object>();
							  	map.put("name", merchantOrderInfo.getMerchantProductName());
							  	map.put("quantity", 1);
							  	//String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
							    String amount =StringTool.getStringValue(merchantOrderInfo.getOrderAmount().doubleValue()*100);
							  	map.put("amount",amount);
							  	String productDetails=JSONObject.fromObject(map).toString();
							  	model.addAttribute("productDetails","["+productDetails+"]");
							  	model.addAttribute("id", merchantOrderInfo.getId());
							  	model.addAttribute("clientIp", merchantOrderInfo.getIp());
							  	model.addAttribute("paymentType", "EHK_ALI_PAY");
							  	model.addAttribute("merid", merchantOrderInfo.getMerchantId());
							  	model.addAttribute("payAmount", amount);
							  	return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
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
				     		
				     	}else if(String.valueOf(PaySwitch.YEEPAY.getValue()).equals(payWx)){
				     		Map <String,Object> map=getYeePayValue(merchantOrderInfo, Channel.YEEPAY_WEIXIN.getValue());
			    			String status=map.get("status").toString();
			    			if(!nullEmptyBlankJudge(status)&&status.equals("ok")){
			    				   String urlCode=map.get("urlCode").toString();
			    				   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode+"&status=ok";
				                   payServiceLog.setLogName(PayLogName.PAY_END);
				        		   UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
				        		   return "redirect:" + fullUri;
			    			}else{
			    				 payServiceLog.setErrorCode("11");
					 			 payServiceLog.setStatus("error");
					 			 model.addAttribute("errorMsg", URLEncoder.encode(map.get("error_msg").toString(), "utf-8"));
					 			  model.addAttribute("failureCode", map.get("error_code").toString());
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8";  
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
		    	    	   String openId = getParmenter(merchantOrderInfo);
						  extra.put("open_id",openId);	
						  chargeMap.put("extra",extra);
				    	  Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
					          JSONObject reqjson = JSONObject.fromObject(res);
					          boolean backValue=analysisValue(reqjson);
					          backValue=true;
					          if(backValue){
					     	        String formValue="";
					        	  formValue=res.get("jsApiParams").toString();
					          	    JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
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
					 			 model.addAttribute("errorMsg", URLEncoder.encode(res.get("failureMsg").toString(), "utf-8"));
					 			  model.addAttribute("failureCode",res.get("failureCode").toString());
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 			return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8";  
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
			    			//       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
			    			//
			    			//**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**//*
			    			Map<String, String> submitFromData = AcpService.sign(requestData,"UTF-8");  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			    			
			    			String requestFrontUrl = PropertiesTool.getAppPropertieByKey("acpsdk.frontTransUrl");  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
			    			String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,"UTF-8");   //生成自动跳转的Html表单
			    			log.info("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
			    			String buf =SendPostMethod.buildRequest(submitFromData, "post", "ok", requestFrontUrl);
			    		    model.addAttribute("res", buf);
			    			 return "pay/payMaxRedirect";
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
			       			String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",paymentType,"GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
			       			model.addAttribute("res", res);
			       		 payServiceLog.setLogName(PayLogName.PAY_END);
					     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	  
		         			return "pay/payRedirect";
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
					  	 DecimalFormat decimalFormat = new DecimalFormat("###################.###########");  
					  	String amount=decimalFormat.format(merchantOrderInfo.getOrderAmount().doubleValue()*100);  
					  // String amount=StringTools.
					    System.out.println(amount);  
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
				     
				     extra.put("return_url",dictTradeChannels.getBackurl());
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
		    	    	String openId = getParmenter(merchantOrderInfo);
						  extra.put("open_id",openId);	
						  chargeMap.put("extra",extra);
				    	  Map<String, Object> res= ce.charge(chargeMap,others.get("private_key"),others.get("secert_key"),others.get("paymax_public_key"));
					          JSONObject reqjson = JSONObject.fromObject(res);
					          boolean backValue=analysisValue(reqjson);
					          if(backValue){
					     	        String formValue="";
					        	  formValue=res.get("jsApiParams").toString();
					          	    JSONObject lakalaWebJson = JSONObject.fromObject(formValue);
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
					          }else{
					        	 payServiceLog.setErrorCode("8");
					 			 payServiceLog.setStatus("error");
					 			 model.addAttribute("errorMsg", URLEncoder.encode(res.get("failureMsg").toString(), "utf-8"));
					 			  model.addAttribute("failureCode",res.get("failureCode").toString());
					 			 payServiceLog.setLogName(PayLogName.PAY_END);
					 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8";  
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
						  	//String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
						  	 String amount =StringTool.getStringValue(merchantOrderInfo.getOrderAmount().doubleValue()*100);
						  	map.put("amount",amount);
						  	String productDetails=JSONObject.fromObject(map).toString();
						  	model.addAttribute("productDetails","["+productDetails+"]");
						  	model.addAttribute("id", merchantOrderInfo.getId());
						  	model.addAttribute("clientIp", merchantOrderInfo.getIp());
						  	model.addAttribute("paymentType", "EHK_WEIXIN_PAY");
						  	model.addAttribute("merid", merchantOrderInfo.getMerchantId());
						  	model.addAttribute("payAmount", amount);
						  	return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
			    	 }
			     }
		       else if(String.valueOf(Channel.EHK_ALI_PAY.getValue()).equals(paymentChannel)){
			    	 //易汇金支付宝扫码支付
		    	 // merchantId:120140804#orderCurrency:CNY#paymentModeCode:BANK_CARD-B2C#clientIp:127.0.0.1#timeout:10
			    	 if(PaymentType.EHK_ALI_PAY.getValue().equals(paymentType)){
						  	Map <String,Object> map=new HashMap<String, Object>();
						  	map.put("name", merchantOrderInfo.getMerchantProductName());
						  	map.put("quantity", 1);
						  	//String amount=String.valueOf((new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());  
						    String amount =StringTool.getStringValue(merchantOrderInfo.getOrderAmount().doubleValue()*100);
						  	map.put("amount",amount);
						  	String productDetails=JSONObject.fromObject(map).toString();
						  	model.addAttribute("productDetails","["+productDetails+"]");
						  	model.addAttribute("id", merchantOrderInfo.getId());
						  	model.addAttribute("clientIp", merchantOrderInfo.getIp());
						  	model.addAttribute("paymentType", "EHK_ALI_PAY");
						  	model.addAttribute("merid", merchantOrderInfo.getMerchantId());
						  	model.addAttribute("payAmount", amount);
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
			  }else if(String.valueOf(Channel.YEEPAY_WEIXIN.getValue()).equals(paymentChannel)){
				  if(PaymentType.YEEPAY_WEIXIN.getValue().equals(paymentType)){
					  //易宝微信扫码
		    			Map <String,Object> map=getYeePayValue(merchantOrderInfo, Channel.YEEPAY_WEIXIN.getValue());
		    			String status=map.get("status").toString();
		    			if(!nullEmptyBlankJudge(status)&&status.equals("ok")){
		    				   String urlCode=map.get("urlCode").toString();
		    				   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode+"&status=ok";
			                   payServiceLog.setLogName(PayLogName.PAY_END);
			        		   UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
			        		   return "redirect:" + fullUri;
		    			}else{
		    				 payServiceLog.setErrorCode("11");
				 			 payServiceLog.setStatus("error");
				 			 model.addAttribute("errorMsg", URLEncoder.encode(map.get("error_msg").toString(), "utf-8"));
				 			  model.addAttribute("failureCode",map.get("error_code").toString());
				 			 payServiceLog.setLogName(PayLogName.PAY_END);
				 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8";  
		    			}
		    	 }
			  }else if(String.valueOf(Channel.YEEPAY_ALI.getValue()).equals(paymentChannel)){
				  if(PaymentType.YEEPAY_ALI.getValue().equals(paymentType)){
					  //易宝支付宝扫码
		    		 Map <String,Object> map=getYeePayValue(merchantOrderInfo, Channel.YEEPAY_ALI.getValue());
		    			String status=map.get("status").toString();
		    			if(!nullEmptyBlankJudge(status)&&status.equals("ok")){
		    				   String urlCode=map.get("urlCode").toString();
		    				   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode+"&status=ok";
			                   payServiceLog.setLogName(PayLogName.PAY_END);
			        		   UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
			        		   return "redirect:" + fullUri;
		    			}else{
		    				 payServiceLog.setErrorCode("11");
				 			 payServiceLog.setStatus("error");
				 			 model.addAttribute("errorMsg", URLEncoder.encode(map.get("error_msg").toString(), "utf-8"));
				 			  model.addAttribute("failureCode", map.get("error_code").toString());
				 			 payServiceLog.setLogName(PayLogName.PAY_END);
				 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"11";  
		    			} 	  
	    	     } 
			  }else if(String.valueOf(Channel.YEEPAY_ALL.getValue()).equals(paymentChannel)){
				  if(PaymentType.YEEPAY_ALL.getValue().equals(paymentType)){
					  //易宝聚合扫码
	    	    	 Map <String,Object> map=getYeePayValue(merchantOrderInfo, Channel.YEEPAY_ALL.getValue());
		    			String status=map.get("status").toString();
		    			if(!nullEmptyBlankJudge(status)&&status.equals("ok")){
		    				   String urlCode=map.get("urlCode").toString();
		    				   fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode+"&status=ok";
			                   payServiceLog.setLogName(PayLogName.PAY_END);
			        		   UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
			        		   return "redirect:" + fullUri;
		    			}else{
		    				 payServiceLog.setErrorCode("11");
				 			 payServiceLog.setStatus("error");
				 			 model.addAttribute("errorMsg", URLEncoder.encode(map.get("error_msg").toString(), "utf-8"));
				 			  model.addAttribute("failureCode", map.get("error_code").toString());
				 			 payServiceLog.setLogName(PayLogName.PAY_END);
				 			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				 	         return "redirect:"+payserviceDev.getServer_host()+"alipay/wxpay/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"8";  
		    			} 	  
	    	     }  
			  }else if(String.valueOf(Channel.EHK_INSTALLMENT_LOAN.getValue()).equals(paymentChannel)){
				  if(PaymentType.EHK_INSTALLMENT_LOAN.getValue().equals(paymentType)){
					  //易汇金分期的款产品接入
						Map <String,Object> map=new HashMap<String, Object>();
							map.put("name", merchantOrderInfo.getMerchantProductName());
							map.put("quantity", 1);
							String amount =StringTool.getStringValue(merchantOrderInfo.getOrderAmount().doubleValue()*100);
							map.put("amount",amount);
							String productDetails=JSONObject.fromObject(map).toString();
							model.addAttribute("productDetails","["+productDetails+"]");
							model.addAttribute("id", merchantOrderInfo.getId());
							model.addAttribute("name", merchantOrderInfo.getBuyerRealName());
							model.addAttribute("idCard", merchantOrderInfo.getBuyerCertNo());
							model.addAttribute("mobliePhone", merchantOrderInfo.getPhone());
							model.addAttribute("clientIp", merchantOrderInfo.getIp());
							model.addAttribute("paymentType", "EHK_ALI_PAY");
							model.addAttribute("merid", merchantOrderInfo.getMerchantId());
							model.addAttribute("payAmount", amount);
						    return "redirect:"+payserviceDev.getServer_host()+"ehk/order/pay";
	            	
				  }  
			  }
       	  return "redirect:" + fullUri;
    }
	
    public Map<String, Object> getYeePayValue(MerchantOrderInfo merchantOrderInfo,int channelId){
		  //易宝微信扫码
			//使用TreeMap
		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),channelId);
		 Map<String, Object> map=new HashMap<String,Object>();
	  	  if(dictTradeChannels!=null){
	  		String other= dictTradeChannels.getOther();
			Map<String, String> others = new HashMap<String, String>();
			others=getPartner(other);
			//解析自定义参数
			 Map<String, Object> parmenterMap=new HashMap<String,Object>();
			 parmenterMap=getParmenters(merchantOrderInfo);
			TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
			treeMap.put("orderid", 			merchantOrderInfo.getId());
			treeMap.put("productcatalog", others.get("productcatalog"));
			treeMap.put("productname", 		merchantOrderInfo.getMerchantProductName());
			treeMap.put("identityid", 		parmenterMap.get("identityid").toString());
			treeMap.put("userip", 			merchantOrderInfo.getIp());
			treeMap.put("paytool", 			 others.get("paytool"));
			treeMap.put("terminalid", 		parmenterMap.get("terminalid").toString());
			treeMap.put("transtime", 		(int)(System.currentTimeMillis()/1000));
			treeMap.put("amount", 	        (new Double(merchantOrderInfo.getOrderAmount().doubleValue()*100)).intValue());
			treeMap.put("identitytype", 	Integer.parseInt(parmenterMap.get("identitytype").toString()) );
			treeMap.put("terminaltype", 	Integer.parseInt(parmenterMap.get("terminaltype").toString()));
			treeMap.put("callbackurl", 		dictTradeChannels.getNotifyUrl());
			treeMap.put("currency", 		Integer.parseInt(others.get("currency")));
			treeMap.put("version", 		   Integer.parseInt(others.get("version")));
			treeMap.put("directpaytype",Integer.parseInt(others.get("directpaytype")));
			treeMap.put("productdesc", 		merchantOrderInfo.getMerchantProductName());
			System.out.println(treeMap);
			//第一步 生成AESkey及encryptkey
			String AESKey		= PaymobileUtils.buildAESKey();
			String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey,others.get("yeepayPublicKey"));
			//String encryptkey	= PaymobileUtils.buildEncyptkey(AESKey);
			//第二步 生成data
			//第三步 获取商户编号及请求地址，并组装支付链接
			String merchantaccount	= others.get("merchantaccount");
			String url				= payserviceDev.getYeepay_pay_url();
			String data			= PaymobileUtils.buildData(treeMap, AESKey,merchantaccount,others.get("merchantPrivateKey"));
			//String data			= PaymobileUtils.buildData(treeMap, AESKey);
			TreeMap<String, String> responseMap	= PaymobileUtils.httpPost(url, merchantaccount, data, encryptkey);
			//第四步 判断请求是否成功
			if(responseMap.containsKey("error_code")) {
				map.put("status", "error");
				map.put("error_msg", responseMap.get("error_msg").toString());
				map.put("error_code", responseMap.get("error_code").toString());
				map.put("urlCode", "");
			}else{
				//第五步 请求成功，则获取data、encryptkey，并将其解密
				String data_response						= responseMap.get("data");
				String encryptkey_response					= responseMap.get("encryptkey");
				
				TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response,others.get("merchantPrivateKey"));
				//TreeMap<String, String> responseDataMap	= PaymobileUtils.decrypt(data_response, encryptkey_response);

				//第六步 sign验签
				if(!PaymobileUtils.checkSign(responseDataMap,others.get("yeepayPublicKey"))) {
			         //验签失败
					map.put("status", "error");
					map.put("error_msg", "验签失败");
					map.put("error_code", "11");
					map.put("urlCode", "");
				}else{
					//第七步 判断请求是否成功
					if(responseDataMap.containsKey("error_code")) {
						map.put("status", "error");
						map.put("error_msg", responseDataMap.get("error_msg").toString());
						map.put("error_code", responseDataMap.get("error_code").toString());
						map.put("urlCode", "");
					}else{
						//第八步 进行业务处理
				    	   map.put("status", "ok");
				    	   String urlCode=responseDataMap.get("payurl");
				    	   map.put("urlCode", urlCode);
					}
				}
			}
	  	 }else{
	  		map.put("status", "error");
			map.put("error_msg", "渠道未开通");
			map.put("error_code", "12");
			map.put("urlCode", "");
	  	 }
	    return  map;
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

    /**
     * 跳转到错误页面
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "errorPayChannel", method = RequestMethod.GET)
    public String errorPayChannel(HttpServletRequest request, Model model,String errorCode,String outTradeNo,String failureCode,String errorMsg) throws UnsupportedEncodingException{
    	if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000001.getType())){
    		errorMsg=PayError.U1000001.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000003.getType())){
    		errorMsg=PayError.U1000003.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000005.getType())){
    		errorMsg=PayError.U1000005.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000002.getType())){
    		errorMsg=PayError.U1000002.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000004.getType())){
    		errorMsg=PayError.U1000004.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000006.getType())){
    		errorMsg=PayError.U1000006.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000007.getType())){
    		errorMsg=PayError.U1000007.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000008.getType())){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+URLDecoder.decode(errorMsg, "UTF-8");
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000009.getType())){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+URLDecoder.decode(errorMsg, "UTF-8");
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000010.getType())){
    		errorMsg=PayError.U1000010.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000011.getType())){
    		errorMsg="获取二维码失败错误码:"+failureCode+"--错误原因："+URLDecoder.decode(errorMsg, "UTF-8");
    	}
    	model.addAttribute("outTradeNo", outTradeNo);
    	model.addAttribute("errorMsg", errorMsg);
    	return "pay/errorPayChannel";
    }	
    /**
     * 跳转到wxpay页面
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "wxpay/errorPayChannel", method = RequestMethod.GET)
    public void wxErrorPayChannel(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String errorMsg) throws UnsupportedEncodingException{
    	 Map<String, Object> map=new HashMap<String,Object>();
    		//String errorMsg=request.getParameter("errorMsg");
        	if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000001.getType())){
        		errorMsg=PayError.U1000001.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000003.getType())){
        		errorMsg=PayError.U1000003.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000005.getType())){
        		errorMsg=PayError.U1000005.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000002.getType())){
        		errorMsg=PayError.U1000002.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000004.getType())){
        		errorMsg=PayError.U1000004.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000006.getType())){
        		errorMsg=PayError.U1000006.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000007.getType())){
        		errorMsg=PayError.U1000007.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000008.getType())){
        		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+URLDecoder.decode(errorMsg, "UTF-8");
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000009.getType())){
        		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+URLDecoder.decode(errorMsg, "UTF-8");
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000010.getType())){
        		errorMsg=PayError.U1000010.getValue();
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000011.getType())){
        		errorMsg="获取二维码失败错误码:"+failureCode+"--错误原因："+URLDecoder.decode(errorMsg, "UTF-8");
        	}
    	   map.put("status", "error");
    	   map.put("outTradeNo", outTradeNo);
    	   map.put("errorMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	   
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
   
    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(HttpServletRequest request,HttpServletResponse response, Model model){
    	String urlCode=request.getParameter("urlCode");
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
        try {
			response.sendRedirect("payIndex?outTradeNo="+outTradeNo+"&totalFee="+totalFee+"goodsName="+goodsName);
		} catch (IOException e) {
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
            		String res=scanCode.bulidPostRequest(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels), payserviceDev.getTcl_pay_url());
         			model.addAttribute("res", res);
         			  payServiceLog.setLogName(PayLogName.PAY_END);
                      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         			return "pay/payRedirect";
            	 }else{
            		//调用支付宝即时支付方法  
            		 String url=AlipayController.getAliPayUrl(merchantId,merchantOrderInfo.getId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev); 
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
                   	 payInfo.setMch_id(others.get("wx_mch_id"));
                   	 payInfo.setNonce_str(WxPayCommonUtil.create_nonce_str());
                   	 payInfo.setBody(goodsName);
                   	 payInfo.setOut_trade_no(merchantOrderInfo.getId());
                   	 payInfo.setProduct_id(goodsId);
                   	 payInfo.setTotal_fee(Integer.parseInt(totalFee));
                   	 payInfo.setSpbill_create_ip(others.get("wx_spbill_create_ip"));
                   	 payInfo.setNotify_url(dictTradeChannel.getNotifyUrl());
                   	 payInfo.setTrade_type(others.get("wx_trade_type"));
                   	 payInfo.setWx_key(dictTradeChannel.getKeyValue());
                   	 String urlCode= WxpayController.weixin_pay(payInfo, payserviceDev);
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
		if(!hmacSHA1Verification){
			paraMandaChkAndReturn(2, response,"认证失败");
			return;
		} 
    	DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(orderInfo.getMerchantId()),Channel.TCL.getValue());
        OrderQryService orderQry = new OrderQryService();
        orderQry.query(OrderQryData.buildGetOrderQryDataMap(orderInfo, dictTradeChannels));
    }

}