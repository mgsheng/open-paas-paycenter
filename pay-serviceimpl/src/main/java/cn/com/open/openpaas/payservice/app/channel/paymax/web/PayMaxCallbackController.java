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
@RequestMapping("/paymax/")
public class PayMaxCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(PayMaxCallbackController.class);
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
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping(value = "callBack")
	public String payCallBack(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException {
		    log.info("-----------------------callBack paxmax/order-----------------------------------------");
		    
		    long startTime = System.currentTimeMillis();
		    //amount=0.01&amount_refunded=0&amount_settle=0&body=testGoodsDesc&client_ip=127.0.0.1&currency=CNY&description=&extra=%7B%22user_id%22%3A%2220160920155907385438%22%2C%22return_url%22%3A%22http%3A%2F%2F10.96.5.174%3A8080%2Fpay-service%2Fpaymax%2FcallBack%22%7D&id=ch_edf13224121f1f1c322b326f&livemode=false&order_no=20160920155907385438&status=PROCESSING&subject=testGoodsName&time_created=1474358348000&time_expire=1474361948402&sign=vfXIUf483Ec0n3QTX458JcmvshwkRf44UhQsYTgX%2BkkqNlhclmwrYw2I2M9aoxShF99eeY9cjnXTcUf5qVDwQGq7ZFazOAOyjUHqni7zW2OqmsaQw5hRHfcKJcXYig19vmmpfZgbVsvFGi2NHILrU1j7LKJ9nYfOj56%2FN7S0j1s%3D
		    //商户订单号
			String order_no = new String(request.getParameter("order_no").getBytes("ISO-8859-1"),"UTF-8");
			//拉卡拉唯一订单id
			String id=request.getParameter("id");
			String amount=request.getParameter("amount");
			String status=request.getParameter("status");
			MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(order_no);
			log.info("paymax callback out_trade_no========="+order_no);
			 String backMsg="";
			 PayServiceLog payServiceLog=new PayServiceLog();
			 if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
				 DictTradeChannel dictTradeChannel=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.PAYMAX.getValue());
				 payServiceLog.setAmount(String.valueOf(Double.parseDouble(amount)*100));
				 payServiceLog.setAppId(merchantOrderInfo.getAppId());
				 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
				 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				 payServiceLog.setLogType(payserviceDev.getLog_type());
				 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
				 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
				 payServiceLog.setOrderId(order_no);
				 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
				 payServiceLog.setPayOrderId(id);
				 payServiceLog.setProductDesc(merchantOrderInfo.getMerchantProductDesc());
				 payServiceLog.setProductName(merchantOrderInfo.getMerchantProductName());
				 payServiceLog.setRealAmount(String.valueOf(Double.parseDouble(amount)*100));
				 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
				 payServiceLog.setUsername(merchantOrderInfo.getUserName());
				 payServiceLog.setLogName(PayLogName.PAYMAX_RETURN_START);
		         payServiceLog.setStatus("ok");
		         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		         String returnUrl=merchantOrderInfo.getReturnUrl();
		  		MerchantInfo merchantInfo = null;
		  		if(nullEmptyBlankJudge(returnUrl)){
		  			merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
		  			returnUrl=merchantInfo.getReturnUrl();
		  		}
		  		 String other= dictTradeChannel.getOther();
			  	Map<String, String> others = new HashMap<String, String>();
			  	others=getPartner(other);
		  	    String paymaxPublicKey =others.get("paymax_public_key");
		  	    //验证签名
			    boolean flag = _verifySign(request, paymaxPublicKey);
			    if(flag){
			    	if(status.equals("SUCCEED")){
			    		//订单处理成功
			    		 payServiceLog.setLogName(PayLogName.PAYMAX_RETURN_END);
						 if(!nullEmptyBlankJudge(returnUrl)){
							 //Map<String, String> dataMap=new HashMap<String, String>();
							 String buf="";
								int payStatus=merchantOrderInfo.getPayStatus();
								Double payCharge=0.0;
								payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
								if(payStatus!=1){
									merchantOrderInfo.setPayStatus(1);
									merchantOrderInfo.setPayAmount(Double.parseDouble(amount)-payCharge);
									merchantOrderInfo.setAmount(Double.parseDouble(amount));
									merchantOrderInfo.setPayCharge(payCharge);
									merchantOrderInfo.setDealDate(new Date());
									merchantOrderInfo.setPayOrderId(id);
									merchantOrderInfoService.updateOrder(merchantOrderInfo);
									if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
										String rechargeMsg=UnifyPayUtil.recordAndBalance(Double.parseDouble(amount)*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
									}
								}
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
							 backMsg="success";
						 }
			    		
			    	}else if(status.equals("PROCESSING")){
			    		//订单处理中
			    		merchantOrderInfoService.updatePayInfo(3,String.valueOf(merchantOrderInfo.getId()),"PAYPROCESSING");
			    	}else{
			    		//订单处理失败
						  payServiceLog.setErrorCode("2");
				          payServiceLog.setStatus("error");
				          payServiceLog.setLogName(PayLogName.PAYMAX_RETURN_END);
				          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				          merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"PAYFAIL");
				          //merchantOrderInfoService.updatePayStatus(4,String.valueOf(merchantOrderInfo.getId()));
						 backMsg="error";
			    	}
			    	
			    }else{
			    	  payServiceLog.setErrorCode("2");
			          payServiceLog.setStatus("error");
			          payServiceLog.setLogName(PayLogName.PAYMAX_RETURN_END);
			          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					// merchantOrderInfoService.updatePayStatus(2,String.valueOf(merchantOrderInfo.getId()));
					 backMsg="error";
			    }
			    
			 }else{
				  payServiceLog.setErrorCode("2");
				   payServiceLog.setStatus("error");
				   backMsg="error";
				  if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
					 {
					     payServiceLog.setStatus("already processed");
					 }
		          payServiceLog.setLogName(PayLogName.PAYMAX_RETURN_END);
		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				
				 
			 }
			 model.addAttribute("backMsg", backMsg);
			 model.addAttribute("productName", merchantOrderInfo.getMerchantProductName());
			 return "pay/callBack";	
	  } 
	private boolean _verifySign(HttpServletRequest request, String paymaxPublicKey) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()){
		String key = names.nextElement().toString();
		map.put(key, request.getParameter(key));
		}
		Map.Entry<String,String>[] entries = map.entrySet().toArray(new Map.Entry[
		map.size()]);
		resortParams(entries);
		Set<String> ignoreProperties=new HashSet<String>();
		ignoreProperties.add("refunds");
		ignoreProperties.add("credential");
		ignoreProperties.add("sign");
		String toSignStr = generateHttpGetParamString(entries, false, ignoreProperties);
		return RSA.verify(toSignStr, map.get("sign").toString(), paymaxPublicKey);
		}
	
	private void resortParams(Map.Entry[] entries){
		Arrays.sort(entries, new Comparator<Map.Entry>() {
		@Override
		public int compare(Map.Entry o1, Map.Entry o2) {
		return String.valueOf(o1.getKey()).compareTo(String.valueOf(o2.getKey(
		)));
		}
		});
		}
	
	private String generateHttpGetParamString(Map.Entry<String,String>[] entries,boolean urlEncoding,Set<String> ignoreProperties){
		String redirectParams="";
		for(Map.Entry<String,String> entry:entries){
		if(ignoreProperties!=null && ignoreProperties.contains(entry.getKey())
		){
		continue;
		}
		if(entry.getValue()==null){
		continue;
		}
		String value = entry.getValue();
		if(urlEncoding){
		try {
		value= URLEncoder.encode(value,"utf-8");
		} catch (UnsupportedEncodingException e) {
		//LogPortal.error(e.getMessage(),e);
		}
		}
		redirectParams+="&"+entry.getKey()+"="+ value;
		}
		return redirectParams.substring(1);
		}
}