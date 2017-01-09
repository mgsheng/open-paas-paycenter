package cn.com.open.openpaas.payservice.app.channel.paymax.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.paymax.sign.RSA;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 
 */
@Controller
@RequestMapping("/paymax/wap/")
public class PayMaxWapCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(PayMaxWapCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private ChannelRateService channelRateService;
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	/**
	 * 拉卡拉微信公众号回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping(value = "callBack")
	public String wapCallBack(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException {
		    log.info("-----------------------callBack  paxmax wap/order-----------------------------------------");
		    long startTime = System.currentTimeMillis();
			String orderId = request.getParameter("orderId");
			MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(orderId);
			log.info("paymax wap callback orderId========="+orderId);
			 String backMsg="";
			 PayServiceLog payServiceLog=new PayServiceLog();
			 payServiceLog.setOrderId(orderId);
			 payServiceLog.setPayOrderId(merchantOrderInfo.getPayOrderId());
			 if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1){
				 payServiceLog.setAppId(merchantOrderInfo.getAppId());
				 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
				 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				 payServiceLog.setLogType(payserviceDev.getLog_type());
				 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
				 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
				
				 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
				 
				 payServiceLog.setProductDesc(merchantOrderInfo.getMerchantProductDesc());
				 payServiceLog.setProductName(merchantOrderInfo.getMerchantProductName());
				 payServiceLog.setRealAmount(String.valueOf(merchantOrderInfo.getAmount()*100));
				 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
				 payServiceLog.setUsername(merchantOrderInfo.getUserName());
				 payServiceLog.setLogName(PayLogName.PAYMAX_WAP_RETURN_START);
		         payServiceLog.setStatus("ok");
		         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		         String returnUrl=merchantOrderInfo.getReturnUrl();
		  		MerchantInfo merchantInfo = null;
		  		merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
		  		if(nullEmptyBlankJudge(returnUrl)){
		  		
		  			returnUrl=merchantInfo.getReturnUrl();
		  		}
			  if(!nullEmptyBlankJudge(returnUrl)){
							 //Map<String, String> dataMap=new HashMap<String, String>();
					            String buf="";
							    SortedMap<String,String> sParaTemp = new TreeMap<String,String>();
								sParaTemp.put("orderId", merchantOrderInfo.getId());
						        sParaTemp.put("outTradeNo", merchantOrderInfo.getMerchantOrderId());
						        sParaTemp.put("merchantId", String.valueOf(merchantOrderInfo.getMerchantId()));
						        sParaTemp.put("paymentType", String.valueOf(merchantOrderInfo.getPaymentId()));
								sParaTemp.put("paymentChannel", String.valueOf(merchantOrderInfo.getChannelId()));
								sParaTemp.put("feeType", "CNY");
								sParaTemp.put("guid", merchantOrderInfo.getGuid());
								sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
								//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
								sParaTemp.put("timeEnd", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
								sParaTemp.put("totalFee", String.valueOf((int)(merchantOrderInfo.getOrderAmount()*100)));
								sParaTemp.put("goodsId", merchantOrderInfo.getMerchantProductId());
								sParaTemp.put("goodsName",merchantOrderInfo.getMerchantProductName());
								sParaTemp.put("goodsDesc", merchantOrderInfo.getMerchantProductDesc());
								sParaTemp.put("parameter", merchantOrderInfo.getParameter1()+"payCharge="+String.valueOf((int)(merchantOrderInfo.getPayCharge()*100))+";");
								sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
							    String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
							    sParaTemp.put("secret", mySign);
							    buf =SendPostMethod.buildRequest(sParaTemp, "post", "ok", returnUrl);
							    model.addAttribute("res", buf);
			     			    return "pay/payMaxRedirect"; 
						 }else{
							 log.info("paymax wap callback returnUrl is  null========="+orderId);
							//订单处理失败
							  payServiceLog.setErrorCode("2");
					          payServiceLog.setStatus("error");
					          payServiceLog.setLogName(PayLogName.PAYMAX_WAP_RETURN_END);
					          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					          merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"returnUrl is null");
							 backMsg="returnUrl is null";
						 }
			 }else{
					//订单为空
			     log.info("paymax wap callback merchantOrderInfo is  null========="+orderId);
				  payServiceLog.setErrorCode("3");
		          payServiceLog.setStatus("error");
		          payServiceLog.setLogName(PayLogName.PAYMAX_WAP_RETURN_END);
		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		          merchantOrderInfoService.updatePayInfo(3,String.valueOf(merchantOrderInfo.getId()),"merchantOrderInfo is null");
				 backMsg="merchantOrderInfo is null"; 
			 }
			 model.addAttribute("backMsg", backMsg);
			 model.addAttribute("productName", merchantOrderInfo.getMerchantProductName());
			 return "pay/callBack";	
	  } 
}