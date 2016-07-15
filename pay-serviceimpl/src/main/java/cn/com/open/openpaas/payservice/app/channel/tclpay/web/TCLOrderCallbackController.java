package cn.com.open.openpaas.payservice.app.channel.tclpay.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.data.ScanCodeOrderData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.ScanCodeOrderService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.log.AlipayControllerLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.site.DistributedLock;


/**
 * 
 */
@Controller
@RequestMapping("/tcl/order/")
public class TCLOrderCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TCLOrderCallbackController.class);
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
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void dirctPay(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
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
		  String rechargeMsg="";
			String Wsign=HytPacketUtils.map2StrRealURL(orderDataMap);
          String backMsg="";
          // -- 验证签名
				boolean flag = false;	
				
				RSASign rsautil =HytUtils.getRSASignVertifyObject(); 
			    flag = rsautil.verify(Wsign,server_sign,server_cert, HytConstants.CHARSET_GBK);//验证签名
			    if (!flag) {
			    	
			    	
					  MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(out_trade_no);
					  
					  //充值
						if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
							String userId=String.valueOf(merchantOrderInfo.getSourceUid());
							UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
							if(userAccountBalance!=null){
								userAccountBalance.setBalance(Double.parseDouble(total_fee)/100+userAccountBalance.getBalance());
								 DistributedLock lock = null;
				                 try {
				           		  lock = new DistributedLock(payserviceDev.getZookeeper_config(),userAccountBalance.getSourceId()+userAccountBalance.getAppId());
				           		  lock.lock();
				           		  userAccountBalanceService.updateBalanceInfo(userAccountBalance);
				           		  rechargeMsg="SUCCESS";
				       		     } catch (Exception e) {
				       			// TODO Auto-generated catch block
					       			e.printStackTrace();
					       		 rechargeMsg="ERROR";
					       		  }finally{
					       			  lock.unlock(); 
					       		  }
							}else{
								userAccountBalance=new UserAccountBalance();
								userAccountBalance.setUserId(userId);
								userAccountBalance.setStatus(1);
								userAccountBalance.setType(1);
								userAccountBalance.setCreateTime(new Date());
								userAccountBalanceService.saveUserAccountBalance(userAccountBalance);
								rechargeMsg="SUCCESS";
							}
							
						}
					  int payStatus=merchantOrderInfo.getPayStatus();
					  Double payCharge=0.0;
					  if(payStatus!=1){
							merchantOrderInfo.setPayStatus(2);
							merchantOrderInfo.setPayAmount(Double.valueOf(total_fee)/100-payCharge);
							merchantOrderInfo.setAmount(Double.valueOf(total_fee)/100);
							merchantOrderInfo.setPayCharge(0.0);
							merchantOrderInfo.setDealDate(new Date());
							merchantOrderInfo.setPayOrderId(trade_no);
							merchantOrderInfoService.updateOrder(merchantOrderInfo);
						}
					  backMsg="error";
				}else{
					 if (!return_code.equals("000000")) { //请求异常
							  backMsg="error";
							
						}else{
							
							//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(out_trade_no);
								if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
									String userId=String.valueOf(merchantOrderInfo.getSourceUid());
									//流水记录
									UserSerialRecord userSerialRecord=new UserSerialRecord();
					        	    userSerialRecord.setAmount(Double.parseDouble(total_fee));
					        	    userSerialRecord.setAppId(Integer.parseInt(merchantOrderInfo.getAppId()));
					        	    userSerialRecord.setSerialNo(out_trade_no);
					        	    userSerialRecord.setSourceId(merchantOrderInfo.getSourceUid());
					        	    userSerialRecord.setPayType(1);
					        	    userSerialRecord.setCreateTime(new Date());
					        	    userSerialRecord.setUserName(merchantOrderInfo.getUserName());
					        	    userSerialRecordService.saveUserSerialRecord(userSerialRecord);  
									UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
									if(userAccountBalance!=null){
										userAccountBalance.setBalance(Double.parseDouble(total_fee)/100+userAccountBalance.getBalance());
										 DistributedLock lock = null;
						                 try {
						           		  lock = new DistributedLock(payserviceDev.getZookeeper_config(),userAccountBalance.getSourceId()+userAccountBalance.getAppId());
						           		  lock.lock();
						           		  userAccountBalanceService.updateBalanceInfo(userAccountBalance);
						           		  rechargeMsg="SUCCESS";
						       		     } catch (Exception e) {
							       			e.printStackTrace();
							       		 rechargeMsg="ERROR";
							       		  }finally{
							       			  lock.unlock(); 
							       		  }
									}else{
										userAccountBalance=new UserAccountBalance();
										userAccountBalance.setUserId(userId);
										userAccountBalance.setStatus(1);
										userAccountBalance.setType(1);
										userAccountBalance.setCreateTime(new Date());
										userAccountBalanceService.saveUserAccountBalance(userAccountBalance);
										rechargeMsg="SUCCESS";
									}
									
								}
							int notifyStatus=merchantOrderInfo.getNotifyStatus();
							int payStatus=merchantOrderInfo.getPayStatus();
							Double payCharge=0.0;
							if(payStatus!=1){
								merchantOrderInfo.setPayStatus(1);
								merchantOrderInfo.setPayAmount(Double.valueOf(total_fee)/100-payCharge);
								merchantOrderInfo.setAmount(Double.valueOf(total_fee)/100);
								merchantOrderInfo.setPayCharge(0.0);
								merchantOrderInfo.setDealDate(new Date());
								merchantOrderInfo.setPayOrderId(trade_no);
								merchantOrderInfoService.updateOrder(merchantOrderInfo);
							}
							
							if(notifyStatus!=1){
								 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,rechargeMsg));
								   thread.run();	
							}
							  backMsg="success";
						}
						
					
				}
			    WebUtils.writeJson(response, backMsg);
			   
					//如果有做过处理，不执行商户的业务程序
		
	  } 
}