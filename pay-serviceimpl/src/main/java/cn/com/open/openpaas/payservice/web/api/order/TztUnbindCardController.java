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
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 投资解绑请求接口
 */
@Controller
@RequestMapping("/tzt/unbind/")
public class TztUnbindCardController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(TztUnbindCardController.class);
	
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
	 * 投资通换卡请求
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("request")
    public String request(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception  {
        long startTime = System.currentTimeMillis();
    	String fullUri=payserviceDev.getServer_host()+"tzt/unbind/errorPayChannel";
    	String identityId=request.getParameter("identityId");
    	String identityType=request.getParameter("identityType");
        String cardNo = request.getParameter("cardNo");
        String appId = request.getParameter("appId");
    	String userId=request.getParameter("userId");
        String signature=request.getParameter("signature");
        String merchantId=request.getParameter("merchantId");
        String outTradeNo=request.getParameter("outTradeNo");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
		String newId="";
		newId=SysUtil.careatePayOrderId();
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(newId);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId(String.valueOf(Channel.TZT));
	    payServiceLog.setPaymentId(PaymentType.TZT_UNBIND_CARD.getValue());
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
	    payServiceLog.setSourceUid(identityId);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.UNBIND_CARD_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,identityId,merchantId,cardNo,appId,userId,identityType))){
        	//paraMandaChkAndReturn(1, response,"必传参数中有空值");
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
        //获取商户信息
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	//paraMandaChkAndReturn(2, response,"商户ID不存在");
    		payServiceLog.setErrorCode("2");
    		payServiceLog.setStatus("error");
    		payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
    		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
    		return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo );
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("identityId", identityId);
   		sParaTemp.put("identityType", identityType);
   		sParaTemp.put("cardNo", cardNo);
   		sParaTemp.put("userId", userId);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			//paraMandaChkAndReturn(3, response,"认证失败");
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		} 
		//更新用户卡号信息
		PayCardInfo payCardInfo=payCardInfoService.getCardInfo(userId, appId);
		if(payCardInfo==null){
			payServiceLog.setErrorCode("4");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode=4";
		}	
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		if(merchantOrderInfo!=null){
				payServiceLog.setErrorCode("5");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode=5";
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
			int paymentTypeId=PaymentType.getTypeByValue("TZT_BIND_CARD").getType();
			merchantOrderInfo.setPaymentId(paymentTypeId);
			merchantOrderInfo.setSourceType(PaySwitch.TZT.getValue());
			merchantOrderInfoService.saveMerchantOrderInfo(merchantOrderInfo);
		}
        //解绑卡操作
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TZT.getValue());
		if(dictTradeChannels!=null){
			String other= dictTradeChannels.getOther();
	    	Map<String, String> others = new HashMap<String, String>();
	    	others=getPartner(other);
			Map<String, String> tztParams 	= new HashMap<String, String>();
			String cardtop=cardNo.substring(0, 6);
	    	String cardlast=cardNo.substring(payCardInfo.getCardNo().length()-4,payCardInfo.getCardNo().length());
			tztParams.put("identityid",identityId);
			tztParams.put("identitytype",identityType);
			tztParams.put("cardtop",cardtop);
			tztParams.put("cardlast",cardlast);
			String merchantno= others.get("merchantAccount");
			String merchantPrivateKey=others.get("merchantPrivateKey");
			String merchantAESKey= TZTService.getMerchantAESKey();
			String yeepayPublicKey= others.get("yeepayPublicKey");
			String unbindRequestURL= payserviceDev.getUnbindRequestURL();
			Map<String, String> result			= TZTService.unbindRequest(tztParams,merchantno,merchantPrivateKey,merchantAESKey,yeepayPublicKey,unbindRequestURL);
		    String customError	   			= formatString(result.get("customError")); 
		    String status= formatString(result.get("status"));
		    if(!nullEmptyBlankJudge(status)&&status.equals("SUCCESS")){
					merchantOrderInfo.setPayStatus(1);
					merchantOrderInfo.setDealDate(new Date());
					try {
						merchantOrderInfoService.updateOrder(merchantOrderInfo);
						payCardInfoService.updateCardStatus(2, payCardInfo.getId());
					} catch (Exception e) {
						  payServiceLog.setErrorCode("9");
						  payServiceLog.setStatus("error");
				    	 payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
					     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	
					     return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode=9";
					}
			     fullUri=payserviceDev.getServer_host()+"/tzt/bindCard/back?status="+status;
	       	     payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
			     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
	       	     return "redirect:" + fullUri;
		    }else if(!"".equals(customError)){
		    	  payServiceLog.setErrorCode("6");
				  payServiceLog.setStatus("error");
		    	 payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
			     UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	
			     return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode=6";
		    }else{
		    	   String errorcode= formatString(result.get("errorcode")); 
				   String errormsg	= formatString(result.get("errormsg")); 
				   //String customError	= formatString(result.get("customError"));
				    payServiceLog.setErrorCode("7");
					payServiceLog.setStatus("error");
					payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					return  "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"7"+"&failureCode="+errorcode+"&failureMsg="+errormsg;
		      }	
		}else{
			    payServiceLog.setErrorCode("8");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.UNBIND_CARD_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode=8";
		}
    }
    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "back", method = RequestMethod.GET)
    public void bindBack(HttpServletRequest request,HttpServletResponse response, Model model){
    	 Map<String, Object> map=new HashMap<String,Object>();
    	 map.put("status", "ok");
    	 map.put("errMsg", "");
         map.put("errorCode", "");
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
    public void wxErrorPayChannel(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	String urlCode=request.getParameter("urlCode");
    	model.addAttribute("urlCode", urlCode);
    	 Map<String, Object> map=new HashMap<String,Object>();
    	
    		String errorMsg="";
        	if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
        		errorMsg="必传参数中有空值";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
        		errorMsg="统一支付验签失败";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
        		errorMsg="商户ID不存在";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
        		errorMsg="用户卡号不存在";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="订单号已存在";
        	}
        	if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="解绑验签失败";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="解绑请求失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="渠道未开通";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
        		errorMsg="系统异常";
        	}
    	   map.put("status", "error");
    	   map.put("outTradeNo", outTradeNo);
    	   map.put("errorMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }

}