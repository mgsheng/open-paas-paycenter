package cn.com.open.openpaas.payservice.app.channel.tclpay.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 
 */
@Controller
@RequestMapping("/tcl/notify/")
public class TCLNotifyCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TCLNotifyCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	 @Autowired
	 private PayserviceDev payserviceDev;
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
	public void tclNotifyCallBack(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		   log.info("-----------------------callBack tcl/notify-----------------------------------------");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		long startTime = System.currentTimeMillis();
		//商户订单号
		String out_trade_no =request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no =  request.getParameter("trade_no") ;
        String total_fee= request.getParameter("total_amount") ;
		//交易状态
		String status =  request.getParameter("status") ;
		String charset= request.getParameter("charset") ;
		String version= request.getParameter("version") ;
		String server_cert= request.getParameter("server_cert") ;
		String server_sign=request.getParameter("server_sign");
		String sign_type=request.getParameter("sign_type");
		String service= request.getParameter("service") ;
		String return_code= request.getParameter("return_code") ;
		String return_message= request.getParameter("return_message");
		String buyer_id= request.getParameter("buyer_id") ;
		String merchant_code= request.getParameter("merchant_code") ;
		String order_time= request.getParameter("order_time") ;
		String channel_code= request.getParameter("channel_code") ;
		String pay_time= request.getParameter("pay_time") ;
		String ac_date= request.getParameter("ac_date") ;
		String fee= request.getParameter("fee") ;
		String attach= request.getParameter("attach") ;
		String backMsg="error";
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
		log.info("tcl notify orderId======================="+ out_trade_no);
		 PayServiceLog payServiceLog=new PayServiceLog();
		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
		Map<String, String> orderDataMap = new HashMap<String, String>();
		orderDataMap.put("out_trade_no", out_trade_no);
		orderDataMap.put("trade_no", trade_no);
		orderDataMap.put("total_amount", total_fee);
		orderDataMap.put("status", status);
		orderDataMap.put("charset", charset);
		orderDataMap.put("version", version);
		/*orderDataMap.put("server_cert", server_cert);
		orderDataMap.put("server_sign", server_sign);*/
		orderDataMap.put("sign_type", sign_type);
		orderDataMap.put("service", service);
		orderDataMap.put("return_code", return_code);
		orderDataMap.put("return_message", return_message);
		orderDataMap.put("buyer_id", buyer_id);
		orderDataMap.put("merchant_code", merchant_code);
		orderDataMap.put("order_time", order_time);
		orderDataMap.put("channel_code", channel_code);
		orderDataMap.put("pay_time", pay_time);
		orderDataMap.put("ac_date", ac_date);
		orderDataMap.put("fee", fee);
		orderDataMap.put("attach", attach);
	    String Wsign=HytPacketUtils.map2StrRealURL(orderDataMap);
       
        //添加日志
       
 		
 		 payServiceLog.setAmount(total_fee);
		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
		 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
		 payServiceLog.setOrderId(out_trade_no);
		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
		 payServiceLog.setPayOrderId(trade_no);
		 payServiceLog.setRealAmount(total_fee);
		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
		 payServiceLog.setLogName(PayLogName.TCL_NOTIFY_START);
         payServiceLog.setStatus("ok");
         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
          // -- 验证签名
				boolean flag = false;	
				RSASign rsautil =HytUtils.getRSASignVertifyObject(); 
			    flag = rsautil.verify(Wsign,server_sign,server_cert, HytConstants.CHARSET_GBK);//验证签名
			    if (!flag) {
			    	payServiceLog.setErrorCode("2");
			          payServiceLog.setStatus("error");
			          payServiceLog.setLogName(PayLogName.TCL_NOTIFY_END);
			          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			          merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
			          
					  backMsg="error";
				}else{
					 if (!return_code.equals("000000")) { //请求异常
							  backMsg="error";
							  payServiceLog.setErrorCode("2");
					          payServiceLog.setStatus("error");
					          payServiceLog.setLogName(PayLogName.TCL_NOTIFY_END);
					          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					          merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"PAYFAIL");
							
						}else{
							//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							
							String rechargeMsg="";
							int notifyStatus=merchantOrderInfo.getNotifyStatus();
							int payStatus=merchantOrderInfo.getPayStatus();
							Double payCharge=0.0;
							payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
							if(payStatus!=1){
								merchantOrderInfo.setPayStatus(1);
								merchantOrderInfo.setPayAmount(Double.valueOf(total_fee)/100-payCharge);
								merchantOrderInfo.setAmount(Double.valueOf(total_fee)/100);
								merchantOrderInfo.setPayCharge(payCharge);
								merchantOrderInfo.setDealDate(new Date());
								merchantOrderInfo.setPayOrderId(trade_no);
								merchantOrderInfoService.updateOrder(merchantOrderInfo);
								if(!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
									rechargeMsg=UnifyPayUtil.recordAndBalance(Double.parseDouble(total_fee),merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
							  }
							}
							
							if(notifyStatus!=1){
								 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
								   thread.run();	
							}
							backMsg="success";
							payServiceLog.setLogName(PayLogName.TCL_NOTIFY_END);
							UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
							}
							
						}
				}
              else{
            	  payServiceLog.setErrorCode("2");
    	          payServiceLog.setStatus("error");
    	          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
    				 {
    	        	  payServiceLog.setStatus("already processed");
    				 }
    	          payServiceLog.setLogName(PayLogName.TCL_NOTIFY_END);
    				
    	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				backMsg="error";	
			}
			    WebUtils.writeJson(response, backMsg);
					//如果有做过处理，不执行商户的业务程序
		
	  } 
}