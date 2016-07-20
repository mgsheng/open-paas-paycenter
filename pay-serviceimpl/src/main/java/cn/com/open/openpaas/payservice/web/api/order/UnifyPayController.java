package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayController;
import cn.com.open.openpaas.payservice.app.channel.alipay.BusinessType;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.data.OrderQryData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.data.ScanCodeOrderData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.OrderQryService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.ScanCodeOrderService;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxPayCommonUtil;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayController;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayInfo;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.payment.model.DictTradePayment;
import cn.com.open.openpaas.payservice.app.payment.service.DictTradePaymentService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.app.tools.QRCodeEncoderHandler;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
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
	 private DictTradePaymentService dictTradePaymentService;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	 
	 // 支付宝当面付2.0服务
	    private static AlipayTradeService tradeService;

	    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	    private static AlipayTradeService tradeWithHBService;

	    // 支付宝交易保障接口服务
	    private static AlipayMonitorService monitorService;

	    static {
	        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
	         *  Configs会读取classpath下的alipayrisk10.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
	         */
	        Configs.init("zfbinfo.properties");

	        /** 使用Configs提供的默认参数
	         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
	         */
	        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();


	        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();


	        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
	        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
//	                            .setGatewayUrl("http://localhost:7777/gateway.do")
	                            .setCharset("GBK")
	                            .setFormat("json")
	                            .build();
	    }

	 /**
     * 请求统一支付
     * @return Json
     */
    @RequestMapping("unifyPay")
    public String unifyPay(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException, Exception {
    	long startTime=System.currentTimeMillis();
    	String outTradeNo=request.getParameter("outTradeNo");
    	String pay_switch = payserviceDev.getPay_switch();
    	String paySwitch []=pay_switch.split("#");
    	String payZhifubao = paySwitch[0];
    	String payWx=paySwitch[1];
    	String payTcl = paySwitch[2];
    	
    	String pay_channle = payserviceDev.getPay_channle();
    	System.out.println(pay_switch);
    	String userName=request.getParameter("userName");
        String userId = request.getParameter("userId");
        String merchantId = request.getParameter("merchantId");
        String appId = request.getParameter("appId");
    	String goodsId=request.getParameter("goodsId");
    	String goodsName=new String(request.getParameter("goodsName").getBytes("iso-8859-1"),"utf-8");
    	//String goodsName=request.getParameter("goodsName");
    	String goodsDesc=request.getParameter("goodsDesc");
    	String goodsTag=request.getParameter("goodsTag");
    	String showUrl=request.getParameter("showUrl");
    	String buyerRealName=request.getParameter("buyerRealName");
    	String buyerCertNo=request.getParameter("buyerCertNo");
    	String inputCharset=request.getParameter("inputCharset");
    	String paymentOutTime=request.getParameter("paymen tOutTime");
    	String paymentType=request.getParameter("paymentType");
    	String paymentChannel=request.getParameter("paymentChannel");
    	String totalFee=request.getParameter("totalFee");
    	String feeType=request.getParameter("feeType");
    	String clientIp=request.getParameter("clientIp");
    	String parameter=request.getParameter("parameter");
        String signature=request.getParameter("signature");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String businessType=request.getParameter("businessType");
    	
    	Map<String ,Object> map=new HashMap<String,Object>();
    	
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,userId,merchantId,goodsName,totalFee))){
        	paraMandaChkAndReturn(3, response,"必传参数中有空值");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
        	return "";
        }
        //判断用户是否存在
        /*if(userId!=null && !("").equals(userId)){
        	//调用用户中心接口判断用户是否存在
        }*/
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	paraMandaChkAndReturn(3, response,"商户ID不存在");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
        	return "";
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
   		sParaTemp.put("username", userName);
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
			paraMandaChkAndReturn(1, response,"认证失败");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
        	return "";
		} 
    	
        if(!StringTool.isNumeric(totalFee)){
        	paraMandaChkAndReturn(3, response,"订单金额格式有误");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
        	return "";
        }
        if(!("CNY").equals(feeType)){
        	paraMandaChkAndReturn(3, response,"金额类型有误");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
        	return "";
        } 
       Boolean payType=validatePayType(paymentChannel,paymentType);
        if(!payType){
        	paraMandaChkAndReturn(4, response,"所选支付渠道与支付类型不匹配");
        	UnifyPayControllerLog.log(outTradeNo, userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
			return "";
        }
		String newId="";
		newId=SysUtil.careatePayOrderId();
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
		DictTradePayment dictTradePayment=dictTradePaymentService.findByPaymentName(paymentType);
		if(merchantOrderInfo!=null){
			//更新现有订单信息
			merchantOrderInfo.setId(newId);
			merchantOrderInfo.setCreateDate(new Date());
			merchantOrderInfoService.updateOrderId(merchantOrderInfo);			
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
			if(dictTradePayment!=null){
				merchantOrderInfo.setPaymentId(dictTradePayment.getId());
			}
			if(!nullEmptyBlankJudge(paymentChannel)){
				merchantOrderInfo.setChannelId(Integer.parseInt(paymentChannel));
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
				 //response.sendRedirect("selectPayChannel?outTradeNo="+outTradeNo+"&appId="+appId);
	        	  model.addAttribute("totalFee",merchantOrderInfo.getOrderAmount());
	              model.addAttribute("goodsName",merchantOrderInfo.getMerchantProductName());
	              model.addAttribute("orderCreateTime",DateTools.dateToString(merchantOrderInfo.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
				 model.addAttribute("outTradeNo", outTradeNo);
				 model.addAttribute("appId", appId);
				 model.addAttribute("payZhifubao", payZhifubao);
				 model.addAttribute("payWx", payWx);
				 model.addAttribute("payTcl", payTcl);
				 model.addAttribute("totalFee", totalFee);
				 model.addAttribute("goodsDesc", goodsDesc);
				 model.addAttribute("goodsId", goodsId);
				 model.addAttribute("merchantId", merchantId);
				 return "pay/payIndex";
		        }else{
		        	merchantOrderInfo=merchantOrderInfoService.findById(newId);
		        	if(merchantOrderInfo!=null){
		        		merchantOrderInfo.setChannelId(SysUtil.toInteger(paymentChannel));
		            	merchantOrderInfoService.updateOrderInfo(merchantOrderInfo);
		        	}        	
		        	UnifyPayControllerLog.log(merchantOrderInfo.getMerchantOrderId(), userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
		        	if((Channel.ALI.getValue()+"").equals(paymentChannel)){
		        		//支付宝-即时到账支付
		        		if((PaymentType.ALIPAY.getValue()).equals(paymentType)){
		            		//调用支付宝即时支付方法  
		                	String url=AlipayController.getAliPayUrl(merchantId,merchantOrderInfo.getMerchantOrderId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev); 
		                		//response.sendRedirect(url.replace("redirect:", ""));
		                	return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;
		        			
		            	}//支付宝-即时到账支付
		        		if((PaymentType.ALIFAF.getValue()).equals(paymentType)){
		            		//调用支付宝当面付方法  
		        			test_trade_precreate(merchantOrderInfo.getMerchantOrderId(),merchantOrderInfo.getMerchantProductName(),String.valueOf(merchantOrderInfo.getOrderAmount()),"0","",String.valueOf(merchantOrderInfo.getMerchantId()),merchantOrderInfo.getMerchantProductDesc(),"test_operator_id","120m");
		            	}else{
		            		// 支付宝-网银支付
		            		String defaultbank=getDefaultbank(paymentType);
		            		String url=AlipayController.getEBankPayUrl(merchantId,merchantOrderInfo.getMerchantOrderId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev,defaultbank); 
		            		return "redirect:"+payserviceDev.getAli_pay_url()+"?"+url;
		            		
		            	}
		        		
		        	}else if((Channel.WEIXIN.getValue()+"").equals(paymentChannel)){
		        		//微信-扫码支付
		        		if((PaymentType.WEIXIN.getValue()+"").equals(paymentType)){
		                	WxpayInfo payInfo=new WxpayInfo();
		                   	 payInfo.setAppid(payserviceDev.getWx_app_id());
		                   	 //payInfo.setDevice_info("WEB");
		                   	 payInfo.setMch_id(payserviceDev.getWx_mch_id());
		                   	 payInfo.setNonce_str(WxPayCommonUtil.create_nonce_str());
		                   	 payInfo.setBody(goodsDesc);
		                   	 //payInfo.setAttach("某某分店");
		                   	 payInfo.setOut_trade_no(merchantOrderInfo.getId());
		                   	 payInfo.setProduct_id(goodsId);
		                   	 payInfo.setTotal_fee(Integer.parseInt(totalFee));
		                   	 payInfo.setSpbill_create_ip(payserviceDev.getWx_spbill_create_ip());
		                   	 payInfo.setNotify_url(payserviceDev.getWx_notify_url());
		                   	 payInfo.setTrade_type(payserviceDev.getWx_trade_type());
		                   	 String urlCode= WxpayController.weixin_pay(payInfo, payserviceDev);
		                    //调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
		                	 //response.sendRedirect("wxpay?urlCode="+urlCode);  
		                	 String fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode;
		                	  return "redirect:" + fullUri;
		        		}
		        	}else if((Channel.TCL.getValue()+"").equals(paymentChannel)){
		        		//TCL转账
		        		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
		        	     if((PaymentType.ALIPAY.getValue()).equals(paymentType)){
		         			ScanCodeOrderService scanCode = new ScanCodeOrderService();
		         			String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels));
		         			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
		         			return "redirect:" + URL;
		         			//response.sendRedirect(URL);
		              }
		              else if((PaymentType.WEIXIN.getValue()).equals(paymentType)){
		            	  //TCL微信
		              		ScanCodeOrderService scanCode = new ScanCodeOrderService();
		             		String qr_code_url=scanCode.order(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","WXPAY","ScanCodePayment",dictTradeChannels));
		             		//response.sendRedirect("tclwxpay?urlCode="+qr_code_url);  
		             		 String fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+qr_code_url;
		                	  return "redirect:" + fullUri;
		             		// response.getWriter().print(qr_code_url);
		             } else if((PaymentType.UPOP.getValue()).equals(paymentType)){
		            	 //银联支付
		             	ScanCodeOrderService scanCode = new ScanCodeOrderService();
		     			String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","UPOP","GWDirectPay",dictTradeChannels));
		     			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
		     			//response.sendRedirect(URL);
		     			return "redirect:" + URL;
		               }else{
		            	   //TCL直连银行
		             	 ScanCodeOrderService scanCode = new ScanCodeOrderService();
		       			String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",paymentType,"GWDirectPay",dictTradeChannels));
		       			String URL=payserviceDev.getTcl_pay_url()+"?"+returnCode;
		       			return "redirect:" + URL;
		       			//response.sendRedirect(URL);
		               }
		        		
		        	}else{
		        		UnifyPayControllerLog.log(merchantOrderInfo.getMerchantOrderId(), userId, merchantId, goodsName, AmountUtil.changeF2Y(totalFee), map);
		            	//跳转到错误页面
		        		 String fullUri=payserviceDev.getServer_host()+"alipay/errorPayChannel";
	                	  return "redirect:" + fullUri;
		    			//response.sendRedirect("errorPayChannel");
		        	}
		        }

		  String fullUri=payserviceDev.getServer_host()+"alipay/errorPayChannel";
       	  return "redirect:" + fullUri;
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
   		 }if(PaymentType.BOC.getValue().equals(paymentType)){
			   returnValue="BOCB2C";
 		 }if(PaymentType.ICBC.getValue().equals(paymentType)){
			   returnValue="ICBCB2C";
 		 }if(PaymentType.CCB.getValue().equals(paymentType)){
			   returnValue="CCB";
 		 }if(PaymentType.ABC.getValue().equals(paymentType)){
			   returnValue="ABC";
 		 }if(PaymentType.BOC.getValue().equals(paymentType)){
			   returnValue="BOCB2C";
 		 }if(PaymentType.BCOM.getValue().equals(paymentType)){
			   returnValue="COMM-DEBIT";
 		 }if(PaymentType.PSBC.getValue().equals(paymentType)){
			   returnValue="POSTGC";
 		 }if(PaymentType.CGB.getValue().equals(paymentType)){
			   returnValue="CGB";
 		 }if(PaymentType.SPDB.getValue().equals(paymentType)){
			   returnValue="SPDB";
 		 }if(PaymentType.CEB.getValue().equals(paymentType)){
			   returnValue="CEB-DEBIT";
 		 }if(PaymentType.PAB.getValue().equals(paymentType)){
			   returnValue="SPABANK";
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
    	if(paymentChannel!=null&&paymentChannel.equals(Channel.ALI.getValue()+"")){
    	 if(paymentType!=null&&!(PaymentType.WEIXIN.getValue()+"").equals(paymentType)&&!(PaymentType.UPOP.getValue()+"").equals(paymentType)){
    		 returnValue=true;
    	 }else{
    		 returnValue=false; 
    	 }
    	}if(paymentChannel!=null&&paymentChannel.equals(Channel.WEIXIN.getValue()+"")){
    	 if(paymentType!=null&&(PaymentType.WEIXIN.getValue()+"").equals(paymentType)){
    		 returnValue=true;
    	 }else{
    		 returnValue=false; 
    	 }
    	}if(paymentChannel!=null&&paymentChannel.equals(Channel.TCL.getValue()+"")){
    		 returnValue=true;
    	}
    	return returnValue;
    }
 
    /**
     * 跳转到错误页面
     */
    @RequestMapping(value = "errorPayChannel", method = RequestMethod.GET)
    public String errorPayChannel(HttpServletRequest request, Model model){
    	return "pay/errorPayChannel";
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
    	String urlCode=request.getParameter("urlCode");
    	model.addAttribute("urlCode", urlCode);
    	 Map<String, Object> map=new HashMap<String,Object>();
    	
    	//encoderQRCoder(urlCode,response);
//    	  QRCodeEncoderHandler handler = new QRCodeEncoderHandler();   
//    	   handler.encoderQRCode(urlCode, response);
    	   map.put("status", "ok");
    	   map.put("urlCode", payserviceDev.getServer_host()+"alipay/getCode?urlCode="+urlCode);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
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
    public void payChannel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
    	String goodsName=request.getParameter("goodsName");
    	String payZhifubao=request.getParameter("payZhifubao");
    	String payWx=request.getParameter("payWx");
    	String payTcl=request.getParameter("payTcl");
    	String totalFee=request.getParameter("totalFee");
    	String goodsDesc=request.getParameter("goodsDesc");
    	String goodsId=request.getParameter("goodsId");
    	String merchantId=request.getParameter("merchantId");
    	
    	
    	String areaCode=request.getParameter("areaCode");
    	String outTradeNo=request.getParameter("outTradeNo");
    	String appId=request.getParameter("appId");
    	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo, appId);
    	if(merchantOrderInfo!=null){
    		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.TCL.getValue());
         	//支付渠道为支付宝
             if(!nullEmptyBlankJudge(areaCode)&&"1".equals(areaCode)){
            	 if(!nullEmptyBlankJudge(payZhifubao)&&"0".equals(payZhifubao)){
            		ScanCodeOrderService scanCode = new ScanCodeOrderService();
         			String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","ALIPAY","GWDirectPay",dictTradeChannels));
         			String URL="https://ipos.tclpay.cn/hipos/payTrans?"+returnCode;
         			response.setCharacterEncoding("GBK");
         			response.sendRedirect(URL);
            	 }else{
            		 
            		//调用支付宝即时支付方法  
            		 String url=AlipayController.getAliPayUrl(merchantId,merchantOrderInfo.getMerchantOrderId(),goodsName,AmountUtil.changeF2Y(totalFee),goodsDesc,dictTradeChannelService,payserviceDev); 
            		response.sendRedirect(payserviceDev.getAli_pay_url()+"?"+url);
            		 
            	 }
             }
             else if(!nullEmptyBlankJudge(areaCode)&&"3".equals(areaCode)){
            	 if(!nullEmptyBlankJudge(payWx)&&"0".equals(payWx)){
            		ScanCodeOrderService scanCode = new ScanCodeOrderService();
             		String qr_code_url=scanCode.order(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","WXPAY","ScanCodePayment",dictTradeChannels));
             		response.sendRedirect("tclwxpay?urlCode="+qr_code_url);  
             		// response.getWriter().print(qr_code_url);
             		//调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
            	 }else{
            		 
            		 
            		 
            		 WxpayInfo payInfo=new WxpayInfo();
                   	 payInfo.setAppid(payserviceDev.getWx_app_id());
                   	 //payInfo.setDevice_info("WEB");
                   	 payInfo.setMch_id(payserviceDev.getWx_mch_id());
                   	 payInfo.setNonce_str(WxPayCommonUtil.create_nonce_str());
                   	 payInfo.setBody(goodsDesc);
                   	 //payInfo.setAttach("某某分店");
                   	 payInfo.setOut_trade_no(merchantOrderInfo.getId());
                   	 payInfo.setProduct_id(goodsId);
                   	 payInfo.setTotal_fee(Integer.parseInt(totalFee));
                   	 payInfo.setSpbill_create_ip(payserviceDev.getWx_spbill_create_ip());
                   	 payInfo.setNotify_url(payserviceDev.getWx_notify_url());
                   	 payInfo.setTrade_type(payserviceDev.getWx_trade_type());
                   	 String urlCode= WxpayController.weixin_pay(payInfo, payserviceDev);
                    //调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
                	 //response.sendRedirect("wxpay?urlCode="+urlCode);  
                	 String fullUri=payserviceDev.getServer_host()+"alipay/wxpay?urlCode="+urlCode;
                	 response.sendRedirect(fullUri.replace("redirect:", ""));	
            	 }
             		
            } else if(!nullEmptyBlankJudge(areaCode)&&"2".equals(areaCode)){
            	if(!nullEmptyBlankJudge(payTcl)&&"1".equals(payTcl)){
            		ScanCodeOrderService scanCode = new ScanCodeOrderService();
        			String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00","UPOP","GWDirectPay",dictTradeChannels));
        			String URL="https://ipos.tclpay.cn/hipos/payTrans?"+returnCode;
        			response.setCharacterEncoding("UTF-8");
        			response.sendRedirect(URL);
            		//调用微信支付方法,方法未完成，暂时先跳转到错误渠道页面
            	}else{
            		
            	}
            	
              }else{
            	  String newareaCode=getAreaCode(areaCode);
            	  ScanCodeOrderService scanCode = new ScanCodeOrderService();
      			String returnCode= scanCode.Aliorder1(ScanCodeOrderData.buildOrderDataMap(merchantOrderInfo,"1.0","00",newareaCode,"GWDirectPay",dictTradeChannels));
      			String URL="https://ipos.tclpay.cn/hipos/payTrans?"+returnCode;
      			response.setCharacterEncoding("UTF-8");
      			response.sendRedirect(URL);
              }
    	}
       
    }
    
    /**selectAccomplish
     * 查询支付结果获取状态
     * @throws Exception 
     */
    @RequestMapping(value = "selectAccomplish", method = RequestMethod.POST)
    public void payAccomplish(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String outTradeNo=request.getParameter("outTradeNo");
    	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo);
    	int payStatus = merchantOrderInfo.getPayStatus();
    	String backMsg="";
    	if(payStatus==1){
    		backMsg="success";
    	}else{
    		backMsg="error";
    	}
    	WebUtils.writeJson(response, backMsg);
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
    	
    	if(!nullEmptyBlankJudge(areaCode)&&"4".equals(areaCode)){
    		newAreaCode="CMB";
    	}if(!nullEmptyBlankJudge(areaCode)&&"5".equals(areaCode)){
    		newAreaCode="ICBC";
    	}if(!nullEmptyBlankJudge(areaCode)&&"6".equals(areaCode)){
    		newAreaCode="CCB";
    	}if(!nullEmptyBlankJudge(areaCode)&&"7".equals(areaCode)){
    		newAreaCode="ABC";
    	}if(!nullEmptyBlankJudge(areaCode)&&"8".equals(areaCode)){
    		newAreaCode="BOC";
    	}if(!nullEmptyBlankJudge(areaCode)&&"9".equals(areaCode)){
    		newAreaCode="BCOM";
    	}if(!nullEmptyBlankJudge(areaCode)&&"10".equals(areaCode)){
    		newAreaCode="PSBC";
    	}if(!nullEmptyBlankJudge(areaCode)&&"11".equals(areaCode)){
    		newAreaCode="CGB";
    	}if(!nullEmptyBlankJudge(areaCode)&&"12".equals(areaCode)){
    		newAreaCode="SPDB";
    	}if(!nullEmptyBlankJudge(areaCode)&&"13".equals(areaCode)){
    		newAreaCode="CEB";
    	}if(!nullEmptyBlankJudge(areaCode)&&"14".equals(areaCode)){
    		newAreaCode="PAB";
    	}
    	return newAreaCode;
    	
    }
 // 测试当面付2.0生成支付二维码
    public void test_trade_precreate(String outTradeNo,String subject,String totalAmount,String undiscountableAmount,String sellerId,String storeId,String body,String operatorId,String timeExpress ) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        //String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long)(Math.random() * 10000000L);

        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
       // String subject = "喜士多（浦东店）消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        //String totalAmount = "0.01";

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        //String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        //String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        //String body = "购买商品2件共15.00元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        // operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        //String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
/*        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088801478647757");*/

        // 支付超时，定义为120分钟
        //String timeExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "全麦小面包", 1500, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.05元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "黑人牙刷", 505, 2);
        goodsDetailList.add(goods2);

        AlipayTradePrecreateContentBuilder builder = new AlipayTradePrecreateContentBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount)
                .setSellerId(sellerId)
                .setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId)
              //  .setExtendParams(extendParams)
                .setTimeExpress(timeExpress)
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("/pay/qr-%s.png", response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
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
 // 测试当面付2.0支付
    public void test_trade_pay(AlipayTradeService service) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = "tradepay" + System.currentTimeMillis() + (long)(Math.random() * 10000000L);

        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
        String subject = "条码支付-消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = "0.01";

        // (必填) 付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
//        String authCode = "286648048691290423";   // 未用条码
//        String authCode = "286399918342265510"; // 已用条码
        String authCode = "287231759284359794"; // 已用条码

        // (不推荐使用) 订单可打折金额，可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折
        // 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
//        String discountableAmount = "1.00"; //

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品2件共15.00元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);

        // 支付超时，线下扫码交易定义为5分钟
        String timeExpress = "5m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xx小面包", 1500, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“xx牙刷”，单价为5.05元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xx牙刷", 505, 2);
        goodsDetailList.add(goods2);

        // 创建请求builder，设置请求参数
        AlipayTradePayContentBuilder builder = new AlipayTradePayContentBuilder()
                .setOutTradeNo(outTradeNo)
                .setSubject(subject)
                .setAuthCode(authCode)
                .setTotalAmount(totalAmount)
                .setStoreId(storeId)
                .setUndiscountableAmount(undiscountableAmount)
                .setBody(body)
                .setOperatorId(operatorId)
                .setExtendParams(extendParams)
                .setSellerId(sellerId)
                .setGoodsDetailList(goodsDetailList)
                .setTimeExpress(timeExpress);

        // 调用tradePay方法获取当面付应答
        AlipayF2FPayResult result = service.tradePay(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝支付成功: )");
                break;

            case FAILED:
                log.error("支付宝支付失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
    }
}