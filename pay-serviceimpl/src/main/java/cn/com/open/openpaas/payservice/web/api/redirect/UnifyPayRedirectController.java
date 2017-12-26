package cn.com.open.openpaas.payservice.web.api.redirect;

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
import cn.com.open.openpaas.payservice.app.order.model.PayLoanInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.order.service.PayLoanInfoService;
import cn.com.open.openpaas.payservice.app.order.service.YeePayPosService;
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
@RequestMapping("/pay/redirect/")
public class UnifyPayRedirectController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UnifyPayRedirectController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private PayserviceDev payserviceDev;
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
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+errorMsg;
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000009.getType())){
    		errorMsg="拉卡拉下单失败！错误码:"+failureCode+"--错误原因："+errorMsg;
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000010.getType())){
    		errorMsg=PayError.U1000010.getValue();
    	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals(PayError.U1000011.getType())){
    		errorMsg="获取二维码失败错误码:"+failureCode+"--错误原因："+errorMsg;
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
       	   map.put("urlCode", payserviceDev.getServer_host()+"pay/redirect/getCode?urlCode="+urlCode);  
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
	        	return "redirect:" + payserviceDev.getServer_host()+"pay/redirect/errorPayChannel"+"?outTradeNo="+outTradeNo+"&errorCode="+"6";
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
             		String fullUri="/pay/redirect/wxpay?urlCode="+qr_code_url+"&status=ok";
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
                	  String fullUri="/pay/redirect/wxpay?urlCode="+urlCode+"&status=ok";
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
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "tzt/errorPayChannel", method = RequestMethod.GET)
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
    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "tzt/back", method = RequestMethod.GET)
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
     * 返回错误信息
     */
    @RequestMapping(value = "tzt/bind/errorPayChannel", method = RequestMethod.GET)
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
    /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "tzt/changeCard/errorPayChannel", method = RequestMethod.GET)
    public void changeCard(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
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
        		errorMsg="用户不存在";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="订单号已存在";
        	}
        	if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="换卡验签失败";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="换卡请求失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="渠道未开通";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
        		errorMsg="换卡订单不存在";
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("10")){
        		errorMsg="换卡确认失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("11")){
        		errorMsg="换卡重发失败！错误码:"+failureCode+"--错误原因："+failureMsg;
        	}
    	   map.put("status", "error");
    	   map.put("outTradeNo", outTradeNo);
    	   map.put("errorCode", errorCode);
    	   
    	   map.put("errorMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    }
    /**
     * 返回错误信息
     */
    @RequestMapping(value = "tzt/unBind/errorPayChannel", method = RequestMethod.GET)
    public void unBind(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
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
    
    /**
     * 返回错误信息
     */
    @RequestMapping(value = "zxpt/bqs/errorPayChannel", method = RequestMethod.GET)
    public void bqs(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	 Map<String, Object> map=new HashMap<String,Object>();
    		String errorMsg="";
    		if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
        		errorMsg="必传参数中有空值";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
        		errorMsg="验证失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="订单信息查询失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
        		errorMsg="商户ID不存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
        		errorMsg="订单号已经存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="白骑士风险结果为空";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="白骑士返回错误:"+failureCode+"--错误原因："+failureMsg;
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="白骑士XML结果为空";
        	}
    	   map.put("status", "error");
    	   map.put("errorCode", errorCode);
    	   map.put("errMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }
    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "zxpt/bqs/bqsBack", method = RequestMethod.GET)
    public void bqsBack(HttpServletRequest request,HttpServletResponse response, Model model){
    	String decision=request.getParameter("decision");
    	 Map<String, Object> map=new HashMap<String,Object>();
     	map.put("status", "ok");
     	map.put("errMsg", "");
     	map.put("errorCode", "");
     	map.put("decision",decision);   
    	writeSuccessJson(response,map);
    }
    /**
     * 返回错误信息
     */
    @RequestMapping(value = "zxpt/errorPayChannel", method = RequestMethod.GET)
    public void zxptError(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
    	 Map<String, Object> map=new HashMap<String,Object>();
    		String errorMsg="";
    		if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("1")){
        		errorMsg="必传参数中有空值";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("3")){
        		errorMsg="验证失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("5")){
        		errorMsg="订单信息查询失败";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("2")){
        		errorMsg="商户ID不存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("4")){
        		errorMsg="订单号已经存在";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("6")){
        		errorMsg="白骑士风险结果为空";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("7")){
        		errorMsg="白骑士返回错误:"+failureCode+"--错误原因："+failureMsg;
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("8")){
        		errorMsg="白骑士XML结果为空";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("9")){
        		errorMsg="该用户为黑名单用户";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("10")){
        		errorMsg="该用户信用分不够";
        	}else if(!nullEmptyBlankJudge(errorCode)&&errorCode.equals("11")){
        		errorMsg="该用户信用分不够";
        	}
    	   map.put("status", "error");
    	   map.put("errorCode", errorCode);
    	   map.put("errMsg", errorMsg);
    	   writeSuccessJson(response,map);
    	 // WebUtils.writeJson(response, urlCode);
    	   
    }
    /**
     * 返回绑卡成功参数
     */
    @RequestMapping(value = "zxpt/back", method = RequestMethod.GET)
    public void zxptBack(HttpServletRequest request,HttpServletResponse response, Model model){
    	 Map<String, Object> map=new HashMap<String,Object>();
     	map.put("status", "ok");
     	map.put("errMsg", "");
     	map.put("errorCode", "");
    	writeSuccessJson(response,map);
    }
    /**
     * 返回错误信息
     */
    @RequestMapping(value = "thirdScore/errorPayChannel", method = RequestMethod.GET)
    public void thirdScoreError(HttpServletRequest request,HttpServletResponse response, Model model,String errorCode,String outTradeNo,String failureCode,String failureMsg){
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