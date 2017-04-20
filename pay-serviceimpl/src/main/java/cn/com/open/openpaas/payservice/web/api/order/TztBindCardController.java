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
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
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
	 private UserAccountBalanceService userAccountBalanceService;
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
    	String fullUri=payserviceDev.getServer_host()+"alipay/errorPayChannel";
    	String identityId=request.getParameter("identityId");
    	String identityType=request.getParameter("identityType");
        String cardNo = request.getParameter("cardNo");
        String appId = request.getParameter("appId");
    	String goodsId=request.getParameter("goodsId");
    	String phone =request.getParameter("phone");
    	String userId=request.getParameter("userId");
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
	    payServiceLog.setChannelId(String.valueOf(Channel.TZT));//
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
	    payServiceLog.setSourceUid(identityId);
	    payServiceLog.setUsername(userName);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.BIND_CARD_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,identityId,merchantId,cardNo,userName,phone,appId,avaliabletime,userId,identityType))){
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
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
        //认证 3f1853bfc53f42129789712998c0724f
   	    //3f1853bfc53f42129789712998c0724f
   	 //appId=10026&avaliabletime=30&cardNo=6214680002152344&identityId=142724198902270834&merchantId=10007&outTradeNo=test201704181739&phone=15727398579&signatureNonce=67&timestamp=2017-04-18T09:40:09Z&userId=36133476-3827-4188-AE4A-0B9DBFC6AC64&username=王帅
       // Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
   	
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
				payServiceLog.setErrorCode("10");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BIND_CARD_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"10";
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
			merchantOrderInfo.setSourceType(Channel.TZT.getValue());
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
		payCardInfo.setUserName(userName);
		payCardInfo.setUserId(userId);
		payCardInfoService.savePayCardInfo(payCardInfo);
        //绑卡操作
	    Map<String, String> others = getOtherInfo(merchantOrderInfo);
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
		tztParams.put("callbackurl", "http://localhost:8080/tzt-new-demo/jsp/paymentCallback.jsp");
		tztParams.put("requesttime", requesttime);
		String merchantno= others.get("merchantAccount");
		String merchantPrivateKey=others.get("merchantPrivateKey");
		String merchantAESKey= TZTService.getMerchantAESKey();
		String yeepayPublicKey= others.get("yeepayPublicKey");
		String bindBankcardURL= payserviceDev.getBindCardRequestURL();
		Map<String, String> result = TZTService.bindCardRequest(tztParams,merchantno,merchantPrivateKey,merchantAESKey,yeepayPublicKey,bindBankcardURL);
		
	    String status= formatString(result.get("status"));
	    if(!nullEmptyBlankJudge(status)&&status.equals("TO_VALIDATE")){
	    	//String merchantno 	= formatString(result.get("merchantno")); 
		      String requestnoback = formatString(result.get("requestno")); 
		       String yborderid=formatString(result.get("yborderid")); 
		      String smscode =formatString(result.get("smscode")); 
		    //String codesender= formatString(result.get("codesender")); 
		    //String enhancedtype	= formatString(result.get("enhancedtype")); 
		    //String smstype= formatString(result.get("smstype")); 
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
			    payServiceLog.setErrorCode("10");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.BIND_CARD_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"11"+"&failureCode="+errorcode+"&failureMsg="+errormsg;
	    }
		
    }

    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "back", method = RequestMethod.GET)
    public void bindBack(HttpServletRequest request,HttpServletResponse response, Model model){
    	String requestno=request.getParameter("requestno");
    	 model.addAttribute("requestno", requestno);
    	 Map<String, Object> map=new HashMap<String,Object>();
    	 map.put("status", "ok");
    	 map.put("requestno", requestno);
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
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
    		errorMsg="验证失败";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
    		errorMsg="所选支付渠道与支付类型不匹配";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
    		errorMsg="商户ID不存在";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
    		errorMsg="订单金额格式有误";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
    		errorMsg="订单处理失败，请重新提交！";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
    		errorMsg="订单已处理，请勿重复提交！";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+failureMsg;
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+failureMsg;
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("10")){
    		errorMsg="您好：订单号重复,请检查后重新下单！";
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("10")){
    		errorMsg="绑卡失败！错误码:"+failureCode+"--错误原因："+failureMsg;
    	}
    	model.addAttribute("outTradeNo", outTradeNo);
    	model.addAttribute("errorMsg", errorMsg);
    	return "pay/errorPayChannel";
    }	
    String formatString(String text){
		return text==null ? "" : text.trim();
	}




}