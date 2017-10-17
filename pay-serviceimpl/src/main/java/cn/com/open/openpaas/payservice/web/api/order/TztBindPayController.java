package cn.com.open.openpaas.payservice.web.api.order;

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
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 投资无短充值请求接口
 */
@Controller
@RequestMapping("/tzt/bindPay")
public class TztBindPayController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(TztBindPayController.class);
	
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
	 * 投资通无短充值请求
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("direct")
    public String direct(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception  {
        long startTime = System.currentTimeMillis();
    	String fullUri=payserviceDev.getServer_host()+"tzt/bindPay/errorPayChannel";
        String appId = request.getParameter("appId");
    	String userId=request.getParameter("userId");
    	String amount=request.getParameter("amount");
    	String productName="";
    	if (!nullEmptyBlankJudge(request.getParameter("productName"))){
    		productName=new String(request.getParameter("productName").getBytes("iso-8859-1"),"utf-8");
    	}
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
	    String terminalNo=request.getParameter("terminalNo");
		String newId="";
		newId=SysUtil.careatePayOrderId();
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(newId);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId(String.valueOf(Channel.TZT));//
	    payServiceLog.setPaymentId(PaymentType.TZT_BIND_PAY.getValue());
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.BIND_PAY_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,merchantId,productName,appId,avaliabletime,userId))){
        	//paraMandaChkAndReturn(1, response,"必传参数中有空值");
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.BIND_PAY_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
        if(!StringTool.isNumeric(amount)){
        	payServiceLog.setErrorCode("4");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.BIND_PAY_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"4";
        }
        //获取商户信息
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	//paraMandaChkAndReturn(2, response,"商户ID不存在");
    		payServiceLog.setErrorCode("2");
    		payServiceLog.setStatus("error");
    		payServiceLog.setLogName(PayLogName.BIND_PAY_END);
    		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo );
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("amount", amount);
   		sParaTemp.put("productName", productName);
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("avaliabletime", avaliabletime);
   		sParaTemp.put("parameter", parameter);
   		sParaTemp.put("userId", userId);
   		sParaTemp.put("terminalNo", terminalNo);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			//paraMandaChkAndReturn(3, response,"认证失败");
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.BIND_PAY_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		} 
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		if(merchantOrderInfo!=null){
				payServiceLog.setErrorCode("5");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BIND_PAY_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"5";
		}else{ 
			//创建订单
			merchantOrderInfo=new MerchantOrderInfo();
			merchantOrderInfo.setId(newId);
			merchantOrderInfo.setAppId(appId);
			merchantOrderInfo.setSourceUid(userId);
			merchantOrderInfo.setMerchantId(SysUtil.toInteger(merchantId));
			merchantOrderInfo.setMerchantOrderId(outTradeNo);//拼接_AppId用于防止不同应用订单号相同问题
			merchantOrderInfo.setStatus(0);//未处理
			merchantOrderInfo.setMerchantVersion("0");
			merchantOrderInfo.setMerchantProductName(productName);
			merchantOrderInfo.setOrderAmount(Double.parseDouble(AmountUtil.changeF2Y(amount)));
			merchantOrderInfo.setParameter1(parameter);
			int paymentTypeId=PaymentType.getTypeByValue("TZT_BIND_PAY").getType();
			merchantOrderInfo.setPaymentId(paymentTypeId);
			merchantOrderInfo.setChannelId(Channel.TCL.getValue());
			merchantOrderInfo.setSourceType(PaySwitch.TZT.getValue());
			merchantOrderInfoService.saveMerchantOrderInfo(merchantOrderInfo);
		}
		//根据user_id和app_id查询账户信息
		
		PayCardInfo payCardInfo=payCardInfoService.getCardInfo(userId, appId);
		String cardtop="";
		String cardlast="";
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TZT.getValue());
		if(dictTradeChannels!=null){
			if(payCardInfo!=null){
			    if(!nullEmptyBlankJudge(payCardInfo.getCardNo())){
			    	cardtop=payCardInfo.getCardNo().substring(0, 6);
			    	cardlast=payCardInfo.getCardNo().substring(payCardInfo.getCardNo().length()-4,payCardInfo.getCardNo().length());
			    }else{
			    	    payServiceLog.setErrorCode("6");
						payServiceLog.setStatus("error");
						payServiceLog.setLogName(PayLogName.BIND_PAY_END);
						UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"6";
			     }
				 //无短充值
			    String other= dictTradeChannels.getOther();
		    	Map<String, String> others = new HashMap<String, String>();
		    	others=getPartner(other);
				Map<String, String> payParams 	= new HashMap<String, String>();
				payParams.put("requestno", 		merchantOrderInfo.getId());
				payParams.put("identityid", 	payCardInfo.getIdentityId());
				payParams.put("identitytype", 	payCardInfo.getIdentityType());
				payParams.put("cardtop", 		cardtop);
				payParams.put("cardlast", 		cardlast);
				payParams.put("amount", 		AmountUtil.changeF2Y(amount));
				payParams.put("avaliabletime",	avaliabletime);
				payParams.put("productname", 	productName);
				payParams.put("callbackurl", 	dictTradeChannels.getNotifyUrl());
				payParams.put("requesttime", 	DateTools.dateToString(new Date(),DateTools.FORMAT_ONE));
				payParams.put("terminalid", 	payCardInfo.getTerminalId());
				payParams.put("registtime", 	DateTools.dateToString(payCardInfo.getCreateTime(), DateTools.FORMAT_ONE));
				payParams.put("registip", 	    payCardInfo.getRegistIp());
				payParams.put("terminalNo", 	others.get("terminalNo"));
				payParams.put("lastloginip", 	payCardInfo.getLastloginIp());
				payParams.put("lastlogintime", 	DateTools.dateToString(payCardInfo.getLastloginTime(), DateTools.FORMAT_ONE));
				payParams.put("lastloginterminalid", payCardInfo.getLastLoginTerminalId());
				payParams.put("issetpaypwd", 	payCardInfo.getIsSetPaypwd());
				String merchantno= others.get("merchantAccount");
	 			String merchantPrivateKey=others.get("merchantPrivateKey");
	 			String merchantAESKey= TZTService.getMerchantAESKey();
	 			String yeepayPublicKey= others.get("yeepayPublicKey");
	 			String bindPayRequestURL= payserviceDev.getUnSendBindPayRequestURL();
				Map<String, String> result			= TZTService.bindPayRequest(payParams,merchantno,merchantPrivateKey,merchantAESKey,yeepayPublicKey,bindPayRequestURL);
			    String customError	   				= formatString(result.get("customError")); 
			    String status= formatString(result.get("status"));
			    
			    if("".equals(customError)) {
			    	   if(!nullEmptyBlankJudge(status)&&(status.equals("PAY_SUCCESS")||status.equals("PROCESSING"))){
						      String requestnoback = formatString(result.get("requestno")); 
//						       String yborderid=formatString(result.get("yborderid")); 
//								merchantOrderInfo.setPayStatus(3);
//								merchantOrderInfo.setDealDate(new Date());
//								merchantOrderInfo.setPayOrderId(yborderid);
//								merchantOrderInfoService.updateOrder(merchantOrderInfo);
						     fullUri=payserviceDev.getServer_host()+"/tzt/bindCard/back?requestno="+requestnoback;
				       	     payServiceLog.setLogName(PayLogName.BIND_PAY_END);
						     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
				       	     return "redirect:" + fullUri;
					    }else{
					    	   String errorcode= formatString(result.get("errorcode")); 
							   String errormsg	= formatString(result.get("errormsg")); 
							   //String customError	= formatString(result.get("customError"));
							    payServiceLog.setErrorCode("7");
								payServiceLog.setStatus("error");
								payServiceLog.setLogName(PayLogName.BIND_PAY_END);
								UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"7"+"&failureCode="+errorcode+"&failureMsg="+errormsg;
					    }
						
			    }else{
					    payServiceLog.setErrorCode("8");
						payServiceLog.setStatus("error");
						payServiceLog.setLogName(PayLogName.BIND_PAY_END);
						UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"8";
			    }
			 
			}else{
				    payServiceLog.setErrorCode("9");
					payServiceLog.setStatus("error");
					payServiceLog.setLogName(PayLogName.BIND_PAY_END);
					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"9";
			}
		}else{
			 payServiceLog.setErrorCode("10");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BIND_PAY_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"10";
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
  	     map.put("errMsg","");
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
     * 返回错误信息
     */
    @RequestMapping(value = "errorPayChannel", method = RequestMethod.GET)
    public void payError(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	 Map<String, Object> map=new HashMap<String,Object>();
    		String errorMsg="";
    		if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
        		errorMsg="必传参数中有空值";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
        		errorMsg="验证失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="所选支付渠道与支付类型不匹配";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
        		errorMsg="商户ID不存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
        		errorMsg="金额格式不对";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="订单号重复";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="用户未绑定卡号";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="无短充值失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="无短充值验签失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
        		errorMsg="用户不存在";
        	}
        	else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("10")){
        		errorMsg="渠道未开通！";
        	}
    	   map.put("status", "error");
    	   map.put("requestno", outTradeNo);
    	   map.put("errorCode", errorCode);
    	   map.put("errMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }


}