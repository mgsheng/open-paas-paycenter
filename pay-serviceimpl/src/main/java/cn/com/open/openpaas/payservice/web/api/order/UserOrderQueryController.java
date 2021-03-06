package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytDateUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 订单查询接口
 */
@Controller
@RequestMapping("/user/order/")
public class UserOrderQueryController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserOrderQueryController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 /**
     * 查询用户订单
     * @return Json
     */
    @RequestMapping("query")
    public void unifyPay(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException, Exception {
    	String outTradeNo=request.getParameter("outTradeNo");
        String appId = request.getParameter("appId");
        String signature=request.getParameter("signature");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
      //获取当前订单
      		MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
      		if(!paraMandatoryCheck(Arrays.asList(outTradeNo,appId))){
    			writeMsgToClient("error",1,response,"必传参数中有空值");
                return;
              }
      		if(orderInfo==null){
    			writeMsgToClient("error",2,response,"业务方订单号不存在");
                return;
      		}
      		
      	MerchantInfo merchantInfo=merchantInfoService.findById(orderInfo.getMerchantId());
        //认证
      	
      	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo);
   		String params=createSign(sParaTemp);
   		
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
       // Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
        
		if(!hmacSHA1Verification){
			writeMsgToClient("error",3,response,"认证失败");
            return;
		} 
		if(orderInfo.getPayStatus()==null || orderInfo.getPayStatus()==0){
			writeMsgToClient("success",4,response,"第三方未处理");
            return;
		}
		if(orderInfo.getPayStatus()==2){
			writeMsgToClient("success",5,response,"第三方未处理失败");
            return;
		}
		if(orderInfo.getPayStatus()==1){
			writeMsgToClient("success",6,response,"第三方处理成功");
            return;
		}
		
		
    }	
    
    
    /**getOrderQuery
     * 商户查询汇银通
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

//    			Map<String, String> orderQryDataMap1 = OrderQryData.buildGetOrderQryDataMap(orderInfo, dictTradeChannels);
    		    
    			Map<String, String> orderQryDataMap = new HashMap<String,String>();
    			String orderTime = HytDateUtils.generateOrderTime();
    			String other= dictTradeChannels.getOther();
    			Map<String, String> others = new HashMap<String, String>();
    			others=getPartner(other);
    			String merchant_code=others.get("merchant_code");
    			orderQryDataMap.put(HytParamKeys.CHARSET, "00");
    			orderQryDataMap.put(HytParamKeys.VERSION, "1.0");
    			orderQryDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
    			orderQryDataMap.put(HytParamKeys.SERVICE, "OrderSearch");
    			orderQryDataMap.put(HytParamKeys.REQUEST_ID, ""); 
    			orderQryDataMap.put(HytParamKeys.MERCHANT_CODE, merchant_code);
    			orderQryDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
    			orderQryDataMap.put(HytParamKeys.OUT_TRADE_NO, orderInfo.getMerchantOrderId());
    			RSASign util = HytUtils.getRSASignObject();
	   			String reqData = HytPacketUtils.map2Str(orderQryDataMap);
	   			String merchant_sign = "";
   				try {
   					merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
   				} catch (Exception e) {
   					e.printStackTrace();
   				}
				String merchant_cert = util.getCertInfo();
	   			orderQryDataMap.put(HytParamKeys.MERCHANT_CERT, merchant_cert);
	   			orderQryDataMap.put(HytParamKeys.MERCHANT_SIGN, merchant_sign);
    			String result=sendPost1("https://ipos.tclpay.cn/hipos/payTrans", orderQryDataMap);
    			orderQryDataMap.remove(HytParamKeys.MERCHANT_CERT);
    			orderQryDataMap.remove(HytParamKeys.MERCHANT_SIGN);
    			String Wsign=HytPacketUtils.map2StrRealURL(orderQryDataMap);
    			boolean flag = false;
    			RSASign rsautil =HytUtils.getRSASignVertifyObject(); 
    			flag = rsautil.verify(Wsign,merchant_sign,merchant_cert, HytConstants.CHARSET_GBK);//验证签名
    			Map<String, String> othere = new HashMap<String, String>();
    			if(flag!=true){
    				othere.put("false", "验证没通过查询失败");
    				writeSuccessJson(response,othere);
    			}else{
        			othere=getResult(result);
        			JSONObject obj = JSONObject.fromObject(othere);
        			System.out.println(obj);
        			writeSuccessJson(response,obj);
    			}
    }
    

  		
  		
  		/**fileDownlond
  	     * 对账文件下载申请
  	     * @throws Exception 
  	     */
  	    @RequestMapping(value = "fileDownlond")
  	    public void fileDownlond(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
//  	    	String outTradeNo=request.getParameter("outTradeNo");
  	        String appId = request.getParameter("appId");
  	        String timestamp=request.getParameter("timestamp");
  		    String signatureNonce=request.getParameter("signatureNonce");
  		    String signature=request.getParameter("signature");  
  		    String acDate=request.getParameter("acDate");
  		    String merchantId=request.getParameter("merchantId");
			 if(!paraMandatoryCheck(Arrays.asList(acDate,appId,merchantId))){
				  	paraMandaChkAndReturn(2, response,"必传参数中有空值");
				  	return ;
		     }
  	  		MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
  	  		if(merchantInfo==null){
  	         	paraMandaChkAndReturn(2, response,"认证失败");
  	         	return ;
  	         } 
  	  		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
  	    	sParaTemp.put("appId",appId);
  	   		sParaTemp.put("timestamp", timestamp);
  	   		sParaTemp.put("signatureNonce", signatureNonce);
  	   		sParaTemp.put("acDate",acDate );
  	   		sParaTemp.put("merchantId",merchantId );
  	   		String params=createSign(sParaTemp);
  	   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
  	        //认证
  			if(!hmacSHA1Verification){
  				paraMandaChkAndReturn(2, response,"认证失败");
  				return;
  			} 
  	    			DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(merchantId,Channel.TCL.getValue());
  	    			Map<String,String> sParaTemp2 = new HashMap<String,String>();
  	    			String other= dictTradeChannels.getOther();
  	    			Map<String, String> others = new HashMap<String, String>();
  	    			others=getPartner(other);
  	    			String merchant_code=others.get("merchant_code");
  					sParaTemp2.put(HytParamKeys.CHARSET,"00");							//字符集
  					sParaTemp2.put(HytParamKeys.VERSION, "1.0");						//接口版本
  					sParaTemp2.put(HytParamKeys.SIGN_TYPE,"RSA");						//签名方式
  					sParaTemp2.put(HytParamKeys.SERVICE, "Statement");					//业务类型
  					sParaTemp2.put(HytParamKeys.REQUEST_ID,  "1");						//商户请求号 暂定
  					sParaTemp2.put(HytParamKeys.MERCHANT_CODE, merchant_code);			//商户编号
  					sParaTemp2.put(HytParamKeys.AC_DATE, acDate); 						//会计日期
  					RSASign util = HytUtils.getRSASignObject();
  		   			 String reqData = HytPacketUtils.map2Str(sParaTemp2);
  		   			 String merchant_sign = "";
  		   				try {
  		   					merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
  		   				} catch (Exception e) {
  		   					e.printStackTrace();
  		   				}
  						String merchant_cert = util.getCertInfo();
  						sParaTemp2.put(HytParamKeys.MERCHANT_CERT, merchant_cert);		//商户证书
  						sParaTemp2.put(HytParamKeys.MERCHANT_SIGN, merchant_sign);		//商户签名
  					String result=sendPost1("https://ipos.tclpay.cn/hipos/payTrans", sParaTemp2);
  					sParaTemp2.remove(HytParamKeys.MERCHANT_CERT);
  					sParaTemp2.remove(HytParamKeys.MERCHANT_SIGN);
  	    			String Wsign=HytPacketUtils.map2StrRealURL(sParaTemp2);
  	    			boolean flag = false;
  	    			RSASign rsautil =HytUtils.getRSASignVertifyObject(); 
  	    			flag = rsautil.verify(Wsign,merchant_sign,merchant_cert, HytConstants.CHARSET_GBK);//验证签名
  	    			Map<String, String> othere = new HashMap<String, String>();
  	    			if(flag!=true){
  	    				othere.put("false", "验证没通过查询失败");
  	    				writeSuccessJson(response,othere);
  	    			}else{
  	        			othere=getDownlond(result);
  	        			JSONObject obj = JSONObject.fromObject(othere);
  	        			System.out.println(obj);
  	        			writeSuccessJson(response,obj);
  	    			}
  	    			
  	    }
}