package cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.web;

import java.io.IOException;
import java.io.PrintWriter;
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
import cn.com.open.openpaas.payservice.app.channel.yeepay.paymobile.utils.PaymobileUtils;
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
@RequestMapping("/yeepay/paymobile/notify/")
public class YeePaymobileNotifyController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(YeePaymobileNotifyController.class);
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
		   log.info("-----------------------callBack paymobile/notify-----------------------------------------");
		long startTime = System.currentTimeMillis();
		//UTF-8编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		String 	backMsg="error";
		PayServiceLog payServiceLog=new PayServiceLog();
		payServiceLog.setLogName(PayLogName.YEEPAY_PAYMOBILE_START);
        payServiceLog.setStatus("ok");
        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		String data			= formatString(request.getParameter("data"));
		String encryptkey	= formatString(request.getParameter("encryptkey"));

		//解密data
		TreeMap<String, String>	dataMap	= PaymobileUtils.decrypt(data, encryptkey,payserviceDev.getMerchant_private_key());
		String yborderid=dataMap.get("yborderid");
		String orderid=dataMap.get("orderid");
		String amount=dataMap.get("amount");
		String status=dataMap.get("status");
		log.info("yborderid====="+yborderid);
		log.info("orderid====="+orderid);
		log.info("amount====="+amount);
		log.info("status====="+status);
			//sign验签
			if(!PaymobileUtils.checkSign(dataMap,payserviceDev.getYeepay_public_key())) {
				  payServiceLog.setErrorCode("4");
		          payServiceLog.setStatus("error");
		          payServiceLog.setLogName(PayLogName.YEEPAY_PAYMOBILE_END);
		    	  merchantOrderInfoService.updatePayInfo(4,String.valueOf(orderid),"VERIFYERROR");
		    	  backMsg="sign 验签失败！dataMap:"+dataMap;
			}else{
				 MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(orderid);
				if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
	 	       		//添加日志
	 	       		 payServiceLog.setAmount(String.valueOf(amount));
	 	       		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
	 	       		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
	 	       		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	 	       		 payServiceLog.setLogType(payserviceDev.getLog_type());
	 	       		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
	 	       		 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
	 	       		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
	 	       		
	 	       		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
	 	       		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
	 	       		 payServiceLog.setLogName(PayLogName.PAYMOBILE_NOTIFY_START);
	 	             payServiceLog.setStatus("ok");
	 	                UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	 	            	log.info("-----------------------callBack paymobile success-start-----------------------------------------");
	 					int notifyStatus=merchantOrderInfo.getNotifyStatus();
	 					int payStatus=merchantOrderInfo.getPayStatus();
	 					Double payCharge=0.0;
	 					payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
	 					if(payStatus!=1){
	 					log.info("-----------------------callBack paymobile update-start-----------------------------------------");
		 					if(!nullEmptyBlankJudge(status)&&status.equals("1")){
		 						merchantOrderInfo.setPayStatus(1);
		 					}else{
		 						merchantOrderInfo.setPayStatus(2);
		 					}
	 						merchantOrderInfo.setPayAmount(Double.parseDouble(amount)/100-payCharge);
	 						merchantOrderInfo.setAmount(Double.parseDouble(amount)/100);
	 						merchantOrderInfo.setPayCharge(payCharge);
	 						merchantOrderInfo.setDealDate(new Date());
	 						merchantOrderInfo.setPayOrderId(yborderid);
	 						merchantOrderInfoService.updateOrder(merchantOrderInfo);
	 						if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
	 						  String	rechargeMsg=UnifyPayUtil.recordAndBalance(Double.parseDouble(amount)*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
	 						}
	 						log.info("-----------------------callBack paymobile update-end-----------------------------------------");
	 					}
	 					if(notifyStatus!=1){
	 						log.info("-----------------------callBack paymobile  thread-start-----------------------------------------");
	 						 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
	 						   thread.run();
	 						 log.info("-----------------------callBack paymobile thread-end-----------------------------------------");
	 					}
				          payServiceLog.setLogName(PayLogName.PAYMOBILE_NOTIFY_END);
				          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	 	              backMsg="SUCCESS";
					}else {
		           	  payServiceLog.setErrorCode("2");
		   	          payServiceLog.setStatus("error");
		   	          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
		   				 {
		   	        	  payServiceLog.setStatus("already processed");
		   	        	  backMsg="SUCCESS";
		   				 }
		   	          payServiceLog.setLogName(PayLogName.YEEPAY_PAYMOBILE_END);
		   	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					}
		}
		WebUtils.writeJson(response, backMsg);
	  } 
    
}