package cn.com.open.openpaas.payservice.app.channel.yeepay;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
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
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 
 */
@Controller
@RequestMapping("/yeepay/notify/")
public class YeeNotifyCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(YeeNotifyCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 @Autowired
	 private ChannelRateService channelRateService;
	 @Autowired
	 private  DictTradeChannelService dictTradeChannelService;
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void yeeNotifyCallBack(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException {
		   log.info("-----------------------callBack yeepay/notify-----------------------------------------");
		long startTime = System.currentTimeMillis();
		String r0_Cmd 	  = formatString(request.getParameter("r0_Cmd")); // 业务类型
		String p1_MerId   = formatString(request.getParameter("p1_MerId"));   // 商户编号
		String r1_Code    = formatString(request.getParameter("r1_Code"));// 支付结果
		String r2_TrxId   = formatString(request.getParameter("r2_TrxId"));// 易宝支付交易流水号
		String r3_Amt     = formatString(request.getParameter("r3_Amt"));// 支付金额
		String r4_Cur     = formatString(request.getParameter("r4_Cur"));// 交易币种
		String r5_Pid     = new String(formatString(request.getParameter("r5_Pid")).getBytes("iso-8859-1"),"gbk");// 商品名称
		String r6_Order   = formatString(request.getParameter("r6_Order"));// 商户订单号
		String r7_Uid     = formatString(request.getParameter("r7_Uid"));// 易宝支付会员ID
		String r8_MP      = new String(formatString(request.getParameter("r8_MP")).getBytes("iso-8859-1"),"gbk");// 商户扩展信息
		String r9_BType   = formatString(request.getParameter("r9_BType"));// 交易结果返回类型
		String hmac       = formatString(request.getParameter("hmac"));// 签名数据
		
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(r6_Order);
		log.info("yeepay notify orderId======================="+ r6_Order);
		log.info("yeepay notify p1_MerId======================="+ p1_MerId);
		log.info("yeepay notify hmac======================="+ hmac);
		
		String 	backMsg="error";
		 PayServiceLog payServiceLog=new PayServiceLog();
		 payServiceLog.setAmount(r3_Amt);
		 payServiceLog.setOrderId(r2_TrxId);
		 payServiceLog.setPayOrderId(r2_TrxId);
		 payServiceLog.setProductName(r5_Pid);
		 payServiceLog.setRealAmount(r3_Amt);
		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
			Double total_fee=0.0;
			if(!nullEmptyBlankJudge(r3_Amt)){
				total_fee=Double.parseDouble(r3_Amt);
			}
			String keyValue   ="";
			 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.YEEPAY_EB.getValue());
 		   if(dictTradeChannels!=null){
 			    String other= dictTradeChannels.getOther();
     			Map<String, String> others = new HashMap<String, String>();
     			others=getPartner(other); 
     			keyValue=others.get("keyValue"); 
 		    }
     		log.info("yeepay notify keyValue======================="+ keyValue);
			boolean isOK = false;
			//添加日志
			 payServiceLog.setAppId(merchantOrderInfo.getAppId());
			 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
			 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
			 payServiceLog.setLogType(payserviceDev.getLog_type());
			 payServiceLog.setMerchantId(merchantOrderInfo.getMerchantId()+"");
			 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
			 payServiceLog.setPaymentId(merchantOrderInfo.getPaymentId()+"");
			 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
			 payServiceLog.setUsername(merchantOrderInfo.getUserName());
			 payServiceLog.setStatus("ok");
			 payServiceLog.setLogName(PayLogName.YEEPAY_CALLBACK_START);
			 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			// 校验返回数据包
			 log.info("===================yeepay notify verfy start======================");
			isOK = PaymentForOnlineService.verifyCallback(hmac,p1_MerId,r0_Cmd,r1_Code, r2_TrxId,r3_Amt,r4_Cur,r5_Pid,r6_Order,r7_Uid,r8_MP,r9_BType,keyValue);
			 String returnUrl=merchantOrderInfo.getNotifyUrl();
		 		MerchantInfo merchantInfo = null;
		 		merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
		 		if(nullEmptyBlankJudge(returnUrl)){
		 			returnUrl=merchantInfo.getReturnUrl();
		 	}
			if(isOK) {
				log.info("===================yeepay notify verfy success======================");
				//在接收到支付结果通知后，判断是否进行过业务逻辑处理，不要重复进行业务逻辑处理
				if(r1_Code.equals("1")) {
					// 产品通用接口支付成功返回-浏览器重定向
					if(r9_BType.equals("1")) {
						//out.println("callback方式:产品通用接口支付成功返回-浏览器重定向");
						 if(!nullEmptyBlankJudge(returnUrl)){
							 //Map<String, String> dataMap=new HashMap<String, String>();
							    String  buf="";
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
								sParaTemp.put("parameter", merchantOrderInfo.getParameter1());
								sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
							    String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
							    sParaTemp.put("secret", mySign);
							    buf =SendPostMethod.buildRequest(sParaTemp, "post", "ok", returnUrl);
							  //  model.addAttribute("res", buf);
							   // String url=payserviceDev.getServer_host()+"yeepay/notify/payRedirect";
							    //response.sendRedirect(url);
							    request.setAttribute("res", buf);
							    try {
									request.getRequestDispatcher("payRedirect").forward(request, response);
									return;
								} catch (ServletException e) {
									e.printStackTrace();
								}
							    
						 }else{
							 backMsg="success";
						 }
						// 产品通用接口支付成功返回-服务器点对点通讯
						 payServiceLog.setLogName(PayLogName.YEEPAY_RETURN_END);
						 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					} else if(r9_BType.equals("2")) {
						// 如果在发起交易请求时	设置使用应答机制时，必须应答以"success"开头的字符串，大小写不敏感
						 payServiceLog.setLogName(PayLogName.YEEPAY_NOTIFY_START);
						 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
						backMsg="SUCCESS";
						int notifyStatus=merchantOrderInfo.getNotifyStatus();
						int payStatus=merchantOrderInfo.getPayStatus();
						Double payCharge=0.0;
						payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
						String rechargeMsg="";
						if(payStatus!=1){
							merchantOrderInfo.setPayStatus(1);
							merchantOrderInfo.setPayAmount(total_fee-payCharge);
							merchantOrderInfo.setAmount(total_fee);
							merchantOrderInfo.setPayCharge(payCharge);
							merchantOrderInfo.setDealDate(new Date());
							merchantOrderInfo.setPayOrderId(r2_TrxId);
							merchantOrderInfoService.updateOrder(merchantOrderInfo);
							//账户充值操作
							if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
								rechargeMsg=UnifyPayUtil.recordAndBalance(total_fee,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
							}
						}
						if(notifyStatus!=1){
							 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
							 thread.run();	
						}
						payServiceLog.setLogName(PayLogName.YEEPAY_NOTIFY_END);
						UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
						WebUtils.writeJson(response, backMsg);
						return ;
					}
				}else{
					payServiceLog.setLogName(PayLogName.YEEPAY_NOTIFY_END);
					payServiceLog.setStatus("error");
					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"PAYFAIL");
				}
			  } else {
				log.info("===================yeepay notify verfy error======================");
				payServiceLog.setLogName(PayLogName.YEEPAY_CALLBACK_END);
				payServiceLog.setStatus("error");
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
				backMsg="error";
			}	
		}else{
		   if(!nullEmptyBlankJudge(r9_BType)&&r9_BType.equals("1")){
			   log.info("===================yeepay notify null error======================");
				 payServiceLog.setErrorCode("2");
		          payServiceLog.setStatus("error");
		          backMsg="error";
		          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
					 {
		        	  log.info("===================yeepay notify null error======================");
						 payServiceLog.setErrorCode("2");
				          payServiceLog.setStatus("error");
				          backMsg="error";
				          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
							 {
				        	  payServiceLog.setStatus("already processed");
				        	  String returnUrl=merchantOrderInfo.getNotifyUrl();
						 		MerchantInfo merchantInfo = null;
						 		merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
						 		if(nullEmptyBlankJudge(returnUrl)){
						 			
						 			returnUrl=merchantInfo.getReturnUrl();
						 	     }
								 //Map<String, String> dataMap=new HashMap<String, String>();
								    String  buf="";
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
									sParaTemp.put("parameter", merchantOrderInfo.getParameter1());
									sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
								    String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
								    sParaTemp.put("secret", mySign);
								    buf =SendPostMethod.buildRequest(sParaTemp, "post", "ok", returnUrl);
								    request.setAttribute("res", buf);
								    try {
										request.getRequestDispatcher("payRedirect").forward(request, response);
										return;
									} catch (ServletException e) {
										e.printStackTrace();
									}
							 }
					 
					 }
		          payServiceLog.setLogName(PayLogName.YEEPAY_CALLBACK_END);
					
		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);   
		          model.addAttribute("backMsg", backMsg);
			      model.addAttribute("productName",merchantOrderInfo.getMerchantProductName() ); 
			     String url=payserviceDev.getServer_host()+"/pay/callBack"+"?backMsg="+backMsg+"&productName="+merchantOrderInfo.getMerchantProductName();
				  response.sendRedirect(url);
				  return ;
			 }else{
				 payServiceLog.setErrorCode("2");
		          payServiceLog.setStatus("error");
		          backMsg="error";
		          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
					 {
		        	  payServiceLog.setStatus("already processed");
		        	  backMsg="success";
					 }
		          payServiceLog.setLogName(PayLogName.YEEPAY_CALLBACK_END);
					
		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);    
		          WebUtils.writeJson(response, backMsg);
				return;
			 }
			
			
		}
		
	  } 
	String formatString(String text){ 
		if(text == null) {
			return ""; 
		}
		return text;
	}
	/**
     * 跳转第三方渠道界面
     */
    @RequestMapping(value = "payRedirect")
    public String selectPayChannel(HttpServletRequest request, Model model){
    	  String res = (String) request.getAttribute("res");
    	  model.addAttribute("res", res);
    	return "pay/payReturn";
    }	
    
}