package cn.com.open.openpaas.payservice.web.api.order;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaySwitch;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.tzt.TZTService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.model.PayCardInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.order.service.PayCardInfoService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.DateUtils;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 投资通绑卡请求接口
 */
@Controller
@RequestMapping("/tzt/bindCard")
public class TztBindCardController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(TztBindCardController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private PayCardInfoService payCardInfoService;
	 


	/**
	 * 投资通绑卡请求
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("request")
    public String request(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception  {
        long startTime = System.currentTimeMillis();
    	String fullUri=payserviceDev.getServer_host()+"tzt/bindCard/errorPayChannel";
    	String identityId=request.getParameter("identityId");
    	String identityType=request.getParameter("identityType");
        String cardNo = request.getParameter("cardNo");
        String appId = request.getParameter("appId");
    	String goodsId=request.getParameter("goodsId");
    	String phone =request.getParameter("phone");
    	String userId=request.getParameter("userId");
    	String terminalId=request.getParameter("terminalId");
    	String lastLoginTerminalId=request.getParameter("lastLoginTerminalId");
    	String isSetPaypwd=request.getParameter("isSetPaypwd");
    	String registIp=request.getParameter("registIp");
    	String lastloginIp=request.getParameter("lastloginIp");
    	String lastloginTime=request.getParameter("lastloginTime");
    	String registTime=request.getParameter("registTime");
    	
    	String userName="";
    	if (!nullEmptyBlankJudge(request.getParameter("userName"))){
    		userName=new String(request.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
    	}
    	String clientIp=request.getParameter("clientIp");
    	String parameter="";
    	if (!nullEmptyBlankJudge(request.getParameter("parameter"))){
    		parameter=new String(request.getParameter("parameter").getBytes("iso-8859-1"),"utf-8");
    	}
        String signature=request.getParameter("signature");
        String merchantId=request.getParameter("merchantId");
        String outTradeNo=request.getParameter("outTradeNo");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String avaliabletime=request.getParameter("avaliabletime");
		String newId="";
		newId=SysUtil.careatePayOrderId();
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(newId);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId(String.valueOf(Channel.TZT));
	    payServiceLog.setPaymentId(PaymentType.TZT_BIND_CARD.getValue());
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
	    payServiceLog.setSourceUid(identityId);
	    payServiceLog.setUsername(userName);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.BIND_CARD_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,identityId,merchantId,cardNo,userName,phone,appId,avaliabletime,userId,identityType,terminalId,lastLoginTerminalId,isSetPaypwd))){
        	//paraMandaChkAndReturn(1, response,"必传参数中有空值");
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.BIND_CARD_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
        //获取商户信息
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	//paraMandaChkAndReturn(2, response,"商户ID不存在");
    		payServiceLog.setErrorCode("2");
    		payServiceLog.setStatus("error");
    		payServiceLog.setLogName(PayLogName.BIND_CARD_END);
    		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    		return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo );
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("phone", phone);
   		sParaTemp.put("userName", userName);
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("identityId", identityId);
   		sParaTemp.put("identityType", identityType);
   		sParaTemp.put("avaliabletime", avaliabletime);
   		sParaTemp.put("cardNo", cardNo);
   		sParaTemp.put("parameter", parameter);
   		sParaTemp.put("userId", userId);
   		sParaTemp.put("terminalId", terminalId);
   		sParaTemp.put("lastLoginTerminalId", lastLoginTerminalId);
   		sParaTemp.put("isSetPaypwd", isSetPaypwd);
   		sParaTemp.put("registIp", registIp);
   		sParaTemp.put("lastloginIp", lastloginIp);
   		sParaTemp.put("lastloginTime", lastloginTime);
   		sParaTemp.put("registTime", registTime);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			//paraMandaChkAndReturn(3, response,"认证失败");
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.BIND_CARD_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		} 
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		if(merchantOrderInfo!=null){
				payServiceLog.setErrorCode("4");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BIND_CARD_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
		}else{ 
			//创建订单
			merchantOrderInfo=new MerchantOrderInfo();
			merchantOrderInfo.setId(newId);
			merchantOrderInfo.setAppId(appId);
			merchantOrderInfo.setSourceUid(userId);
			merchantOrderInfo.setMerchantId(SysUtil.toInteger(merchantId));
			merchantOrderInfo.setIp(clientIp);
			merchantOrderInfo.setMerchantOrderId(outTradeNo);//拼接_AppId用于防止不同应用订单号相同问题
			merchantOrderInfo.setStatus(0);//未处理
			merchantOrderInfo.setMerchantVersion("0");
			merchantOrderInfo.setMerchantProductId(goodsId);
			merchantOrderInfo.setParameter1(parameter);
			int paymentTypeId=PaymentType.getTypeByValue("TZT_BIND_CARD").getType();
			merchantOrderInfo.setPaymentId(paymentTypeId);
			merchantOrderInfo.setSourceType(PaySwitch.TZT.getValue());
			merchantOrderInfoService.saveMerchantOrderInfo(merchantOrderInfo);
		}
		//保存用户卡号信息
		PayCardInfo payCardInfo=new PayCardInfo();
		payCardInfo.setAppId(appId);
		payCardInfo.setCardNo(cardNo);
		payCardInfo.setIdentityId(identityId);
		payCardInfo.setIdentityType(identityType);
		payCardInfo.setPhone(phone);
		payCardInfo.setStatus(0);
		payCardInfo.setCreateTime(new Date());
		if(!nullEmptyBlankJudge(registTime)){
			payCardInfo.setRegistTime(DateTools.stringtoDate(registTime, DateTools.FORMAT_ONE));	
		}
		payCardInfo.setUserName(userName);
		payCardInfo.setUserId(userId);
		payCardInfo.setTerminalId(terminalId);
		payCardInfo.setLastLoginTerminalId(lastLoginTerminalId);
		payCardInfo.setLastloginIp(lastloginIp);
		payCardInfo.setRegistIp(registIp);
		payCardInfo.setIsSetPaypwd(isSetPaypwd);
		if(!nullEmptyBlankJudge(lastloginTime)){
			payCardInfo.setLastloginTime(DateTools.stringtoDate(lastloginTime, DateTools.FORMAT_ONE));	
		}
		payCardInfoService.savePayCardInfo(payCardInfo);
        //绑卡操作
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TZT.getValue());
		if(dictTradeChannels!=null){
			String other= dictTradeChannels.getOther();
	    	Map<String, String> others = new HashMap<String, String>();
	    	others=getPartner(other);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String requesttime= df.format(new Date());
			Map<String, String> tztParams 	= new HashMap<String, String>();
			tztParams.put("requestno",merchantOrderInfo.getId());
			tztParams.put("identityid",identityId);
			tztParams.put("identitytype",identityType);
			tztParams.put("cardno",cardNo);
			tztParams.put("idcardno",identityId);
			tztParams.put("idcardtype",others.get("idcardtype"));
			tztParams.put("username",userName);
			tztParams.put("phone",phone);
			tztParams.put("advicesmstype", 	others.get("advicesmstype"));
			tztParams.put("customerenhancedtype", others.get("customerenhancedtype"));
			tztParams.put("avaliabletime",avaliabletime);
			tztParams.put("callbackurl", dictTradeChannels.getNotifyUrl());
			tztParams.put("requesttime", requesttime);
			String merchantno= others.get("merchantAccount");
			String merchantPrivateKey=others.get("merchantPrivateKey");
			String merchantAESKey= TZTService.getMerchantAESKey();
			String yeepayPublicKey= others.get("yeepayPublicKey");
			String bindBankcardURL= payserviceDev.getBindCardRequestURL();
			Map<String, String> result = TZTService.bindCardRequest(tztParams,merchantno,merchantPrivateKey,merchantAESKey,yeepayPublicKey,bindBankcardURL);
		    String status= formatString(result.get("status"));
		    if(!nullEmptyBlankJudge(status)&&status.equals("TO_VALIDATE")){
			      String requestnoback = formatString(result.get("requestno")); 
			       String yborderid=formatString(result.get("yborderid")); 
					merchantOrderInfo.setPayStatus(3);
					merchantOrderInfo.setDealDate(new Date());
					merchantOrderInfo.setPayOrderId(yborderid);
					merchantOrderInfoService.updateOrder(merchantOrderInfo);
			     fullUri=payserviceDev.getServer_host()+"/tzt/bindCard/back?requestno="+requestnoback;
	       	     payServiceLog.setLogName(PayLogName.BIND_CARD_END);
			     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
	       	     return "redirect:" + fullUri;
		    }else{
		    	   String errorcode= formatString(result.get("errorcode")); 
				   String errormsg	= formatString(result.get("errormsg")); 
				   //String customError	= formatString(result.get("customError"));
				    payServiceLog.setErrorCode("10");
					payServiceLog.setStatus("error");
					payServiceLog.setLogName(PayLogName.BIND_CARD_END);
					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"5"+"&failureCode="+errorcode+"&failureMsg="+errormsg;
		    }	
		}else{
			    payServiceLog.setErrorCode("6");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BIND_CARD_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"6";
		}
        
		
    }
    /**
     * 有短验绑卡请求短验确认
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("confirm")
    public String confirm(HttpServletRequest request,HttpServletResponse response,Model model){
    	long startTime = System.currentTimeMillis();
    	String fullUri=payserviceDev.getServer_host()+"tzt/bindCard/errorPayChannel";
    	String requestno=request.getParameter("requestNo");
    	String outTradeNo=request.getParameter("outTradeNo");
    	String signature=request.getParameter("signature");
        String merchantId=request.getParameter("merchantId");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String appId = request.getParameter("appId");
	    String validatecode=request.getParameter("validateCode");
	    
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(requestno);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId(String.valueOf(Channel.TZT));//
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    	 if(!paraMandatoryCheck(Arrays.asList(outTradeNo,merchantId,appId,requestno,validatecode))){
         	payServiceLog.setErrorCode("1");
         	payServiceLog.setStatus("error");
         	payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
         	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         	
         	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
         }
         //获取商户信息
     	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
     	if(merchantInfo==null){
         	//paraMandaChkAndReturn(2, response,"商户ID不存在");
     		payServiceLog.setErrorCode("2");
     		payServiceLog.setStatus("error");
     		payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
     		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
         }
     	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
     		sParaTemp.put("appId",appId);
     		sParaTemp.put("timestamp", timestamp);
    		sParaTemp.put("signatureNonce", signatureNonce);
    		sParaTemp.put("outTradeNo",outTradeNo );
    		sParaTemp.put("merchantId", merchantId);
    		sParaTemp.put("requestNo", requestno);
    		sParaTemp.put("validateCode", validatecode);
    		String params=createSign(sParaTemp);
    	Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
 		if(!hmacSHA1Verification){
 			//paraMandaChkAndReturn(3, response,"认证失败");
 			payServiceLog.setErrorCode("3");
 			payServiceLog.setStatus("error");
 			payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
 			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
 		} 
 		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
 		if(merchantOrderInfo!=null){
 			//有短验绑卡请求短验确认

 			Map<String, String> confirmParams 	= new HashMap<String, String>();
 			confirmParams.put("requestno", 		requestno);
 			confirmParams.put("validatecode", 		validatecode);
 		    Map<String, String> others = getOtherInfo(merchantOrderInfo);
 			String merchantno= others.get("merchantAccount");
 			String merchantPrivateKey=others.get("merchantPrivateKey");
 			String merchantAESKey= TZTService.getMerchantAESKey();
 			String yeepayPublicKey= others.get("yeepayPublicKey");
 			String confirmBindBankcardURL= payserviceDev.getBindCardConfirmURL();

 			Map<String, String> result			= TZTService.bindCardConfirm(confirmParams,merchantno,merchantPrivateKey,merchantAESKey,yeepayPublicKey,confirmBindBankcardURL);
 			
 		    String status 		   			= formatString(result.get("status")); 

 		   if(!nullEmptyBlankJudge(status)&&status.equals("BIND_SUCCESS")){
 			      String requestnoback = formatString(result.get("requestno")); 
 			      String yborderid=formatString(result.get("yborderid")); 
 				  merchantOrderInfo.setPayStatus(1);
 				  merchantOrderInfo.setDealDate(new Date());
 				  merchantOrderInfo.setPayOrderId(yborderid);
 				  merchantOrderInfoService.updateOrder(merchantOrderInfo);
 			     fullUri=payserviceDev.getServer_host()+"/tzt/bindCard/back?requestno="+requestnoback;
 	       	     payServiceLog.setLogName(PayLogName.BIND_CARD_END);
 			     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
 	       	     return "redirect:" + fullUri;
 		    }else{
 		    	   String errorcode= formatString(result.get("errorcode")); 
 				   String errormsg	= formatString(result.get("errormsg")); 
 				   //String customError	= formatString(result.get("customError"));
 				    payServiceLog.setErrorCode("8");
 					payServiceLog.setStatus("error");
 					payServiceLog.setLogName(PayLogName.BIND_CARD_END);
 					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 		        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8"+"&failureCode="+errorcode+"&failureMsg="+errormsg;
 		    }
 		}else{
 			payServiceLog.setErrorCode("7");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"7";
 		}
    }
    /**
     * 有短验绑卡请求短验重发
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("resendsms")
    public String resendsms(HttpServletRequest request,HttpServletResponse response,Model model){
    	long startTime = System.currentTimeMillis();
    	String fullUri=payserviceDev.getServer_host()+"tzt/bindCard/errorPayChannel";
    	String requestno=request.getParameter("requestNo");
    	String outTradeNo=request.getParameter("outTradeNo");
    	String signature=request.getParameter("signature");
        String merchantId=request.getParameter("merchantId");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String appId = request.getParameter("appId");
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(requestno);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId(String.valueOf(Channel.TZT));//
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    	 if(!paraMandatoryCheck(Arrays.asList(outTradeNo,merchantId,appId,requestno))){
         	payServiceLog.setErrorCode("1");
         	payServiceLog.setStatus("error");
         	payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
         	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         	
         	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
         }
         //获取商户信息
     	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
     	if(merchantInfo==null){
         	//paraMandaChkAndReturn(2, response,"商户ID不存在");
     		payServiceLog.setErrorCode("2");
     		payServiceLog.setStatus("error");
     		payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
     		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
         }
     	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
     		sParaTemp.put("appId",appId);
     		sParaTemp.put("timestamp", timestamp);
    		sParaTemp.put("signatureNonce", signatureNonce);
    		sParaTemp.put("outTradeNo",outTradeNo );
    		sParaTemp.put("merchantId", merchantId);
    		sParaTemp.put("requestNo", requestno);
    		String params=createSign(sParaTemp);
    	Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
 		if(!hmacSHA1Verification){
 			//paraMandaChkAndReturn(3, response,"认证失败");
 			payServiceLog.setErrorCode("3");
 			payServiceLog.setStatus("error");
 			payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
 			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
         	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
 		} 
 		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
 		if(merchantOrderInfo!=null){
 			//换卡请求短验重发
 			Map<String, String> resendParams 	= new HashMap<String, String>();
 			Map<String, String> others = getOtherInfo(merchantOrderInfo);
 			resendParams.put("requestno", 		requestno);
 			String merchantno= others.get("merchantAccount");
 			String merchantPrivateKey=others.get("merchantPrivateKey");
 			String merchantAESKey= TZTService.getMerchantAESKey();
 			String yeepayPublicKey= others.get("yeepayPublicKey");
 			String bindCardResendsmsURL		= payserviceDev.getBindCardResendsmsURL();
 			Map<String, String> result			= TZTService.changeCardResendsms(resendParams,merchantno,merchantPrivateKey,merchantAESKey,yeepayPublicKey,bindCardResendsmsURL);
 			
 		    String status 		   			= formatString(result.get("status")); 

 		   if(!nullEmptyBlankJudge(status)&&status.equals("TO_VALIDATE")){
 			      String requestnoback = formatString(result.get("requestno")); 
 			       String yborderid=formatString(result.get("yborderid")); 
 					merchantOrderInfo.setPayStatus(3);
 					merchantOrderInfo.setDealDate(new Date());
 					merchantOrderInfo.setPayOrderId(yborderid);
 					merchantOrderInfoService.updateOrder(merchantOrderInfo);
 			     fullUri=payserviceDev.getServer_host()+"/tzt/bindCard/back?requestno="+requestnoback;
 	       	     payServiceLog.setLogName(PayLogName.BIND_CARD_END);
 			     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
 	       	     return "redirect:" + fullUri;
 		    }else{
 		    	   String errorcode= formatString(result.get("errorcode")); 
 				   String errormsg	= formatString(result.get("errormsg")); 
 				    payServiceLog.setErrorCode("9");
 					payServiceLog.setStatus("error");
 					payServiceLog.setLogName(PayLogName.BIND_CARD_END);
 					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 		        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"9"+"&failureCode="+errorcode+"&failureMsg="+errormsg;
 		    }
 		}else{
 			payServiceLog.setErrorCode("4");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.BIND_CARD_CONFIRM_START);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
 		}
    }
    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "back", method = RequestMethod.GET)
    public void bindBack(HttpServletRequest request,HttpServletResponse response, Model model){
    	String requestno=request.getParameter("requestno");
    	 Map<String, Object> map=new HashMap<String,Object>();
    	 map.put("status", "ok");
    	 map.put("requestno", requestno);
    	 map.put("errorCode", "");
  	     map.put("errMsg", "");
    	 writeSuccessJson(response,map);
    }
	/**
	 * 获取渠道参数信息
	 * @param merchantOrderInfo 订单
	 * @return
	 */
	public Map<String, String> getOtherInfo(MerchantOrderInfo merchantOrderInfo) {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TZT.getValue());
         String other= dictTradeChannels.getOther();
     	 Map<String, String> others = new HashMap<String, String>();
     	  others=getPartner(other);
     	 return others;
	}
    
  
    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "errorPayChannel", method = RequestMethod.GET)
    public void bindError(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	 Map<String, Object> map=new HashMap<String,Object>();
    		String errorMsg="";
    		if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
        		errorMsg="必传参数中有空值";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
        		errorMsg="验证失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
        		errorMsg="商户ID不存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
        		errorMsg="订单号重复";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="绑卡请求失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="渠道未开通！";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="订单不存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="绑卡确认失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
        		errorMsg="绑卡确认失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}
    	   map.put("status", "error");
    	   map.put("requestno", outTradeNo);
    	   map.put("errorCode", errorCode);
    	   map.put("errMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }



}