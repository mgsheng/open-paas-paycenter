package cn.com.open.openpaas.payservice.app.channel.yeepay.ehking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson15.JSONObject;
import com.ehking.sdk.FastJsonUtils;
import com.ehking.sdk.exception.HmacVerifyException;
import com.ehking.sdk.exception.ResponseException;
import com.ehking.sdk.exception.UnknownException;
import com.ehking.sdk.executer.ResultListenerAdpater;
import com.ehking.sdk.onlinepay.executer.OnlinePayOrderExecuter;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayStatus;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.model.PayLoanInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.order.service.PayLoanInfoService;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 *易汇金分期贷款异步通知
 */
@Controller
@RequestMapping("/ehk/loan/notify")
public class EhkLoanNotifyCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(EhkLoanNotifyCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private ChannelRateService channelRateService;
	 @Autowired
	 private PayLoanInfoService payLoanInfoService;
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
	@RequestMapping("callBack")
	public void notifyCallBack(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		   log.info("-----------------------callBack  ehk/loan/notify-----------------------------------------");
		 //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		final long startTime = System.currentTimeMillis();
		String backMsg="";
 	    OnlinePayOrderExecuter executer = new OnlinePayOrderExecuter();
 	    try{
 		    executer.callback(FastJsonUtils.convert(request.getInputStream()), new ResultListenerAdpater() {
 		    	
 	            public void success(JSONObject jsonObject) {
 	            //log.info("jsonObject========="+jsonObject.toJSONString());
 	       		 String out_trade_no =  jsonObject.getString("requestId");
 	       	    //易汇金系统交易流水号
 	       		 String serialNumber= jsonObject.getString("serialNumber");
 	       	     String orderAmount=jsonObject.getString("orderAmount");
 	       	     String status=jsonObject.getString("status");
 	       	     String repaymentStatus=jsonObject.getString("repaymentStatus");
 	       	    
 	       	     
 	       	    //log.info("requestId========="+out_trade_no);
 	       	    //log.info("serialNumber========="+serialNumber);
 	            //log.info("orderAmount========="+orderAmount);
 	        
 	       		 MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
 	       		log.info("ehk loan notify orderId======================="+ out_trade_no);
 	       	   PayServiceLog payServiceLog=new PayServiceLog();
 	       	    payServiceLog.setOrderId(out_trade_no);
 	       	    payServiceLog.setPayOrderId(serialNumber);
	       		payServiceLog.setRealAmount(orderAmount);
 	       		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
 	       		//添加日志
 	       		
 	       		 payServiceLog.setAmount(String.valueOf(orderAmount));
 	       		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
 	       		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
 	       		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
 	       		 payServiceLog.setLogType(payserviceDev.getLog_type());
 	       		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
 	       		 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
 	       		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
 	       		
 	       		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
 	       		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
 	       		 payServiceLog.setLogName(PayLogName.EHK_NOTIFY_START);
 	                payServiceLog.setStatus("ok");
 	                UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 	            	log.info("-----------------------callBack  success-start-----------------------------------------");
 	            	
 					//判断该笔订单是否在商户网站中已经做过处理
 					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
 					//账户充值操作
 					
 					int notifyStatus=merchantOrderInfo.getNotifyStatus();
 					int payStatus=merchantOrderInfo.getPayStatus();
 					if(payStatus!=1){
 	 					log.info("-----------------------callBack  update-start-----------------------------------------");
 	 					if(!nullEmptyBlankJudge(status)&&status.equals("LOANED")){
 	 						merchantOrderInfo.setPayStatus(1);
 	 						//保存用户贷款详细信息
 	 						PayLoanInfo payLoanInfo=payLoanInfoService.findByOrderId(out_trade_no);
 	 						if(payLoanInfo!=null&&!nullEmptyBlankJudge(repaymentStatus)&&repaymentStatus.equals("REPAYMENTED")){
 	 							payLoanInfoService.updateStatus(payLoanInfo.getId(),3);
 	 						}else if(payLoanInfo!=null&&!nullEmptyBlankJudge(repaymentStatus)&&repaymentStatus.equals("IN_REPAYMENT")){
 	 							payLoanInfoService.updateStatus(payLoanInfo.getId(),2);
 	 						}else if(payLoanInfo==null) {
 	 							payLoanInfo=new PayLoanInfo();
 	 							payLoanInfo.setAnnualRate(jsonObject.getString("annualRate"));
 	 							payLoanInfo.setTerms(String.valueOf(jsonObject.getInteger("terms")));
 	 							payLoanInfo.setStatus(2);
 	 							payLoanInfo.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
 	 							payLoanInfo.setOrderId(out_trade_no);
 	 							payLoanInfo.setDuePrincipal(merchantOrderInfo.getOrderAmount());
 	 							if(!nullEmptyBlankJudge(jsonObject.getString("completeDateTime"))){
 	 								payLoanInfo.setCompleteTime(DateTools.stringtoDate(jsonObject.getString("completeDateTime"), DateTools.FORMAT_ONE));
 	 							}
 	 							payLoanInfo.setCreateTime(new Date());
 	 							payLoanInfoService.savePayLoanInfo(payLoanInfo);
 	 						}
 	 					}else{
 	 					  merchantOrderInfo.setPayStatus(2);	
 	 					  payServiceLog.setErrorCode("2");
 	 					  payServiceLog.setLogName(PayLogName.EHK_NOTIFY_END);
 	 	    	          payServiceLog.setStatus(status);
 	 	    	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 	 					}
 	 					//创建账户
 	 					if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
 						String	rechargeMsg=UnifyPayUtil.recordAndBalance(Double.parseDouble(orderAmount)*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
 						}
 	 					merchantOrderInfo.setAmount(Double.parseDouble(orderAmount)/100);
 	 					merchantOrderInfo.setDealDate(new Date());
 	 					merchantOrderInfo.setPayCharge(0.0);
 	 					merchantOrderInfo.setPayOrderId(serialNumber);
 	 					merchantOrderInfoService.updateOrder(merchantOrderInfo);
 	 						log.info("-----------------------callBack  update-end-----------------------------------------");
 	 			  }
 					 
 					/*String rechargeMsg="";
 					int payStatus=merchantOrderInfo.getPayStatus();
 					Double payCharge=0.0;
 					payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
 					if(payStatus!=1){
 					log.info("-----------------------callBack  update-start-----------------------------------------");
 						merchantOrderInfo.setPayStatus(1);
 						merchantOrderInfo.setPayAmount(Double.parseDouble(orderAmount)/100-payCharge);
 						merchantOrderInfo.setAmount(Double.parseDouble(orderAmount)/100);
 						merchantOrderInfo.setPayCharge(payCharge);
 						merchantOrderInfo.setDealDate(new Date());
 						merchantOrderInfo.setPayOrderId(serialNumber);
 						merchantOrderInfoService.updateOrder(merchantOrderInfo);
 						if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
 							rechargeMsg=UnifyPayUtil.recordAndBalance(Double.parseDouble(orderAmount)*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
 						}
 						log.info("-----------------------callBack  update-end-----------------------------------------");
 					}*/
 					if(notifyStatus!=1){
 						log.info("-----------------------callBack  thread-start-----------------------------------------");
 						 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
 						   thread.run();
 						 log.info("-----------------------callBack  thread-end-----------------------------------------");
 					}
			          payServiceLog.setLogName(PayLogName.EHK_NOTIFY_END);
			          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 	              }else{
 	            	  payServiceLog.setErrorCode("2");
 	    	          payServiceLog.setStatus("error");
 	    	          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
 	    				 {
 	    	        	  payServiceLog.setStatus("already processed");
 	    				 }
 	    	          payServiceLog.setLogName(PayLogName.EHK_NOTIFY_END);
 	    				
 	    	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
 	              }
 	            }
 	            public void failure(JSONObject jsonObject) {
 	               // LOGGER.info("支付失败！",jsonObject);
 	               // LOGGER.info(jsonObject.toJSONString());
 	            	//更新状态为失败
 	            	 String out_trade_no =  jsonObject.getString("requestId");
 	 	       	    //易汇金系统交易流水号
 	 	       	     MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
 	 	       	     
 	 	       	     merchantOrderInfoService.updatePayStatus(2,String.valueOf(merchantOrderInfo.getId()));
 	            }
 	          
 	        });
 	    }
 		catch(ResponseException e){
 			//LOGGER.info("响应异常");
 			//LOGGER.info(e.toString());
 			backMsg="error";
 		}
 		catch(HmacVerifyException e){
 			//LOGGER.info("签名验证异常");
 			//LOGGER.info(e.toString());
 			backMsg="error";
 		}
 		catch(UnknownException e){
 			//LOGGER.info("未知异常");
 			//LOGGER.info(e.toString());
 			backMsg="error";
 		}
 	     backMsg="SUCCESS";
		 log.info("-----------------------callBack  notify-end-----------------------------------------");
		WebUtils.writeJson(response, backMsg);
	}
	public void updateError(final String serialNumber,
			final String orderAmount,
			final MerchantOrderInfo merchantOrderInfo) {
		Double payCharge=0.0;
		payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
		merchantOrderInfo.setPayStatus(2);
		merchantOrderInfo.setPayAmount(Double.parseDouble(orderAmount)/100-payCharge);
		merchantOrderInfo.setAmount(Double.parseDouble(orderAmount)/100);
		merchantOrderInfo.setPayCharge(payCharge);
		merchantOrderInfo.setDealDate(new Date());
		merchantOrderInfo.setPayOrderId(serialNumber);
		merchantOrderInfoService.updateOrder(merchantOrderInfo);
	}
}