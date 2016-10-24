package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.IOException;
import java.net.MalformedURLException;
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

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
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
@RequestMapping("/alipay/notify")
public class AliNotifyCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(AliNotifyCallbackController.class);
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
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void aliNotifyCallBack(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		   log.info("-----------------------callBack  alipay/notify-----------------------------------------");
		//获取支付宝GET过来反馈信息
		long startTime = System.currentTimeMillis();
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		String backMsg="error";
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			  log.info("-----------------------callBack:alipay:notify params:-----------------------------------------"+valueStr);
		//	valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        Double total_fee=Double .valueOf(request.getParameter("total_fee"));
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
	//	String subject = new String(request.getParameter("subject").getBytes("ISO-8859-1"),"UTF-8");
		String subject = request.getParameter("subject");
		String body = "";
//		if(!nullEmptyBlankJudge(request.getParameter("body"))){
//			body=new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");	
//		}
		
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
		log.info("ali notify out_trade_no========="+out_trade_no);
		 PayServiceLog payServiceLog=new PayServiceLog();
		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
			  DictTradeChannel dictTradeChannel=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.ALI.getValue());
		        String key = dictTradeChannel.getKeyValue(); // key
		//添加日志
		
		 payServiceLog.setAmount(String.valueOf(total_fee*100));
		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
		 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
		 payServiceLog.setOrderId(out_trade_no);
		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
		 payServiceLog.setPayOrderId(trade_no);
		 payServiceLog.setProductDesc(body);
		 payServiceLog.setProductName(subject);
		 payServiceLog.setRealAmount(String.valueOf(total_fee*100));
		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
		 payServiceLog.setLogName(PayLogName.CALLBACK_NOTIFY_START);
         payServiceLog.setStatus("ok");
         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params,key,dictTradeChannel.getInputCharset());
		  log.info("-----------------------callBack:alipay:notify sign result:-----------------------------------------"+verify_result+",orderId:"+out_trade_no);
		if(verify_result){
			  log.info("-----------------------callBack:alipay:notify:success-----------------------------------------");
			//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				backMsg="success";
				//账户充值操作
				String rechargeMsg="";
				int notifyStatus=merchantOrderInfo.getNotifyStatus();
				int payStatus=merchantOrderInfo.getPayStatus();
				Double payCharge=0.0;
				payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
				if(payStatus!=1){
					merchantOrderInfo.setPayStatus(1);
					merchantOrderInfo.setPayAmount(total_fee-payCharge);
					merchantOrderInfo.setAmount(total_fee);
					merchantOrderInfo.setPayCharge(payCharge);
					merchantOrderInfo.setDealDate(new Date());
					merchantOrderInfo.setPayOrderId(trade_no);
					merchantOrderInfoService.updateOrder(merchantOrderInfo);
					if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
						rechargeMsg=UnifyPayUtil.recordAndBalance(total_fee*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
					}
				}
				if(notifyStatus!=1){
					 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
					   thread.run();	
				}
					//如果有做过处理，不执行商户的业务程序
		
			//该页面可做页面美工编辑
			
			  //backValue="redirect:"+ALI_ORDER_DISPOSE_URI+"?out_trade_no="+out_trade_no+"&goodsName="+goodsName+"&goodsDesc="+goodsDesc+"&goodsId="+goodsId+"&total_fee"+total_fee;
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			//该页面可做页面美工编辑
			  payServiceLog.setErrorCode("2");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.CALLBACK_NOTIFY_END);
	          merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"PAYFAIL");
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			backMsg="error";
		}
	
	    }else{
	    	  payServiceLog.setErrorCode("4");
	          payServiceLog.setStatus("error");
	          payServiceLog.setLogName(PayLogName.CALLBACK_NOTIFY_END);
	    	merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
	    }
		}else{
			 payServiceLog.setErrorCode("2");
	          payServiceLog.setStatus("error");
	          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
				 {
	        	  payServiceLog.setStatus("already processed");
				 }
	          payServiceLog.setLogName(PayLogName.CALLBACK_NOTIFY_END);
				
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			 backMsg="success";
		} 
		WebUtils.writeJson(response, backMsg);
	}
}